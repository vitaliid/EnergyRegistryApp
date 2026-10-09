package org.example.service;

import org.example.components.UserActionPublisher;
import org.example.domain.UserActionType;
import org.example.dto.EnergyReadingRequest;
import org.example.dto.EnergyReadingResponse;
import org.example.dto.EnergyUploadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link EnergyCsvUploadService} using the CSV fixtures in src/test/resources/testfiles.
 * Verifies that every imported file produces an ENERGY_CSV_IMPORTED audit event via {@link UserActionPublisher}.
 */
@ExtendWith(MockitoExtension.class)
class EnergyCsvUploadServiceTest {

    private static final double THRESHOLD = 1000.0;

    @Mock
    private EnergyRegistryService energyRegistryService;

    @Mock
    private ObjectStorageService objectStorageService;

    @Mock
    private UserActionPublisher userActionPublisher;

    @InjectMocks
    private EnergyCsvUploadService service;

    @Captor
    private ArgumentCaptor<Map<String, Object>> metadataCaptor;

    @BeforeEach
    void stubCollaborators() {
        // lenient: the rejection tests never reach these collaborators
        lenient().when(energyRegistryService.registerReading(any(EnergyReadingRequest.class)))
                .thenAnswer(invocation -> {
                    EnergyReadingRequest request = invocation.getArgument(0);
                    return new EnergyReadingResponse(
                            request.meterId(),
                            request.value(),
                            THRESHOLD,
                            request.value() > THRESHOLD
                    );
                });

        lenient().when(objectStorageService.upload(any(MultipartFile.class)))
                .thenAnswer(invocation -> {
                    MultipartFile file = invocation.getArgument(0);
                    return new ObjectStorageService.StoredObject(
                            "energy",
                            "2026/10/06/" + file.getOriginalFilename(),
                            file.getSize(),
                            "text/csv"
                    );
                });
    }

    @Test
    @DisplayName("single file: all readings registered and one ENERGY_CSV_IMPORTED success event published")
    void importsSingleFileAndPublishesSuccess() throws IOException {
        MultipartFile file = fixture("meter-a.csv");

        EnergyUploadResponse response = service.processFiles(List.of(file));

        assertThat(response.filesProcessed()).isEqualTo(1);
        // meter-a.csv: 850, 920, 980
        assertThat(response.readingsProcessed()).isEqualTo(3);
        assertThat(response.readings())
                .extracting(EnergyReadingResponse::meterId)
                .containsOnly("meter-a");

        verify(energyRegistryService, times(3)).registerReading(any(EnergyReadingRequest.class));
        verify(objectStorageService).upload(file);

        verify(userActionPublisher).success(
                eq(UserActionType.ENERGY_CSV_IMPORTED),
                eq(EnergyCsvUploadService.TARGET_TYPE),
                eq("meter-a.csv"),
                metadataCaptor.capture()
        );
        verify(userActionPublisher, never()).failure(any(), anyString(), any(), anyString());

        assertThat(metadataCaptor.getValue())
                .containsEntry("readingsProcessed", 3)
                .containsEntry("objectKey", "2026/10/06/meter-a.csv")
                .containsKey("size");
    }

    @Test
    @DisplayName("multiple files: one success event per file, readings aggregated")
    void importsMultipleFilesAndPublishesOneEventPerFile() throws IOException {
        List<MultipartFile> files = List.of(
                fixture("meter-a.csv"),
                fixture("meter-b-threshold.csv"),
                fixture("mixed-meters.csv")
        );

        EnergyUploadResponse response = service.processFiles(files);

        assertThat(response.filesProcessed()).isEqualTo(3);
        // 3 (meter-a) + 3 (meter-b-threshold) + 5 (mixed-meters)
        assertThat(response.readingsProcessed()).isEqualTo(11);
        // strictly above 1000: meter-b 1001 and 1250, meter-4 1000.5, meter-5 1400 (meter-3 at exactly 1000 is not)
        assertThat(response.readings())
                .filteredOn(EnergyReadingResponse::aboveThreshold)
                .extracting(EnergyReadingResponse::meterId)
                .containsExactly("meter-b", "meter-b", "meter-4", "meter-5");

        verify(userActionPublisher, times(3)).success(
                eq(UserActionType.ENERGY_CSV_IMPORTED),
                eq(EnergyCsvUploadService.TARGET_TYPE),
                any(),
                any()
        );
        verify(userActionPublisher).success(eq(UserActionType.ENERGY_CSV_IMPORTED), anyString(), eq("meter-a.csv"), any());
        verify(userActionPublisher).success(eq(UserActionType.ENERGY_CSV_IMPORTED), anyString(), eq("meter-b-threshold.csv"), any());
        verify(userActionPublisher).success(eq(UserActionType.ENERGY_CSV_IMPORTED), anyString(), eq("mixed-meters.csv"), any());
        verify(userActionPublisher, never()).failure(any(), anyString(), any(), anyString());
    }

    @Test
    @DisplayName("non-CSV file: rejected, ENERGY_CSV_IMPORTED failure event published, nothing stored")
    void rejectsNonCsvAndPublishesFailure() {
        MultipartFile txt = new MockMultipartFile(
                "files", "readings.txt", "text/plain", "meterId,value\nm,1".getBytes());

        assertThatThrownBy(() -> service.processFiles(List.of(txt)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Only CSV files are allowed");

        verify(userActionPublisher).failure(
                eq(UserActionType.ENERGY_CSV_IMPORTED),
                eq(EnergyCsvUploadService.TARGET_TYPE),
                eq("readings.txt"),
                anyString()
        );
        verify(userActionPublisher, never()).success(any(), anyString(), any(), any());
        verify(energyRegistryService, never()).registerReading(any());
        verify(objectStorageService, never()).upload(any());
    }

    @Test
    @DisplayName("empty file: rejected with failure event")
    void rejectsEmptyFileAndPublishesFailure() {
        MultipartFile empty = new MockMultipartFile("files", "empty.csv", "text/csv", new byte[0]);

        assertThatThrownBy(() -> service.processFiles(List.of(empty)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("File is empty");

        verify(userActionPublisher).failure(
                eq(UserActionType.ENERGY_CSV_IMPORTED),
                eq(EnergyCsvUploadService.TARGET_TYPE),
                eq("empty.csv"),
                anyString()
        );
        verify(objectStorageService, never()).upload(any());
    }

    @Test
    @DisplayName("no files: rejected before any file-level event is published")
    void rejectsEmptyListWithoutEvents() {
        assertThatThrownBy(() -> service.processFiles(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("At least one CSV file is required");

        verify(userActionPublisher, never()).success(any(), anyString(), any(), any());
        verify(userActionPublisher, never()).failure(any(), anyString(), any(), anyString());
    }

    private static MultipartFile fixture(String name) throws IOException {
        try (InputStream in = EnergyCsvUploadServiceTest.class
                .getResourceAsStream("/testfiles/" + name)) {
            if (in == null) {
                throw new IllegalStateException("Missing test fixture: " + name);
            }
            return new MockMultipartFile("files", name, "text/csv", in.readAllBytes());
        }
    }
}
