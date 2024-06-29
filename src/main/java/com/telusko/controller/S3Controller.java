// S3Controller.java

package com.telusko.controller;

import com.telusko.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/s3") // Base URL path for S3-related endpoints
@CrossOrigin // Allow cross-origin requests (useful for frontend apps)
public class S3Controller {

    @Autowired
    private S3Service s3Service; // Inject the S3Service to use its methods

    // Endpoint to upload a file to S3
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        // Call the uploadFile method in S3Service
        return s3Service.uploadFile(file);
    }

    // Endpoint to download a file from S3
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        // Call the downloadFile method in S3Service
        byte[] file = s3Service.downloadFile(fileName);

        // Return the file in the response with appropriate headers
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}
