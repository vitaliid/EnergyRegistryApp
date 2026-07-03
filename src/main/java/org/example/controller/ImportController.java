package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.EnergyUploadResponse;
import org.example.service.EnergyCsvUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/energy")
@RequiredArgsConstructor
public class ImportController {

    private final EnergyCsvUploadService energyCsvUploadService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<EnergyUploadResponse> uploadReadings(
            @RequestParam("files") List<MultipartFile> files
    ) {
        EnergyUploadResponse response = energyCsvUploadService.processFiles(files);
        return ResponseEntity.ok(response);
    }
}
