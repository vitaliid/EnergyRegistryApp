package org.example.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.example.dto.EnergyReadingRequest;
import org.example.dto.EnergyReadingResponse;
import org.example.dto.EnergyUploadResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnergyCsvUploadService {

    private final EnergyRegistryService energyRegistryService;

    public EnergyCsvUploadService(EnergyRegistryService energyRegistryService) {
        this.energyRegistryService = energyRegistryService;
    }

    public EnergyUploadResponse processFiles(List<MultipartFile> files) {
        validateFiles(files);

        List<EnergyReadingResponse> registered = new ArrayList<>();

        for (MultipartFile file : files) {
            validateCsv(file);
            registered.addAll(processFile(file));
        }

        return new EnergyUploadResponse(
                files.size(),
                registered.size(),
                registered
        );
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