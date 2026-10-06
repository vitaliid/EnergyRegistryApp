package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.example.components.UserActionPublisher;
import org.example.domain.UserActionType;
import org.example.dto.EnergyReadingRequest;
import org.example.dto.EnergyReadingResponse;
import org.example.dto.EnergyUploadResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnergyCsvUploadService {

    static final String TARGET_TYPE = "CSV_FILE";

    private final EnergyRegistryService energyRegistryService;
    private final ObjectStorageService objectStorageService;
    private final UserActionPublisher userActionPublisher;

    /**
     * Transactional so that the ENERGY_CSV_IMPORTED audit events published per file are
     * persisted by {@code UserActionEventListener} (BEFORE_COMMIT) in the same transaction.
     */
    @Transactional
    public EnergyUploadResponse processFiles(List<MultipartFile> files) {
        validateFiles(files);

        List<EnergyReadingResponse> registered = new ArrayList<>();

        for (MultipartFile file : files) {
            registered.addAll(importFile(file));
        }

        return new EnergyUploadResponse(
                files.size(),
                registered.size(),
                registered
        );
    }

    private List<EnergyReadingResponse> importFile(MultipartFile file) {
        String filename = file.getOriginalFilename();

        try {
            validateCsv(file);
            List<EnergyReadingResponse> readings = processFile(file);

            ObjectStorageService.StoredObject storedFile =
                    objectStorageService.upload(file);

            log.info("File saved in storage: {}", storedFile.objectKey());

            userActionPublisher.success(
                    UserActionType.ENERGY_CSV_IMPORTED,
                    TARGET_TYPE,
                    filename,
                    Map.of(
                            "readingsProcessed", readings.size(),
                            "objectKey", storedFile.objectKey(),
                            "size", file.getSize()
                    )
            );

            return readings;

        } catch (RuntimeException ex) {
            userActionPublisher.failure(
                    UserActionType.ENERGY_CSV_IMPORTED,
                    TARGET_TYPE,
                    filename,
                    ex.getMessage()
            );
            throw ex;
        }
    }

    private List<EnergyReadingResponse> processFile(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get()
                    .parse(reader);

            List<EnergyReadingResponse> responses = new ArrayList<>();

            for (CSVRecord record : records) {
                EnergyReadingRequest request = toRequest(record);
                responses.add(energyRegistryService.registerReading(request));
            }

            return responses;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to process CSV file: " + file.getOriginalFilename(), ex);
        }
    }

    private EnergyReadingRequest toRequest(CSVRecord record) {
        return new EnergyReadingRequest(
                record.get("meterId"),
                Double.parseDouble(record.get("value"))
        );
    }

    private void validateFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("At least one CSV file is required");
        }
    }

    private void validateCsv(MultipartFile file) {
        String filename = file.getOriginalFilename();

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty: " + filename);
        }

        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("Only CSV files are allowed: " + filename);
        }
    }
}