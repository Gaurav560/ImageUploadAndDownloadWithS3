// S3Service.java

package com.telusko.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Service
public class S3Service {

    private final S3Client s3Client; // AWS S3 client for interacting with S3

    @Value("${aws.s3.bucket}")
    private String bucketName; // S3 bucket name, injected from properties

    // Constructor to initialize the S3 client with AWS credentials and region
    public S3Service(@Value("${aws.accessKeyId}") String accessKeyId,
                     @Value("${aws.secretKey}") String secretKey,
                     @Value("${aws.region}") String region) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    // Method to upload a file to the S3 bucket
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename(); // Get the original filename
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName) // Set the bucket name
                .key(fileName) // Set the file key (name)
                .build();

        // Upload the file to S3
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return "File uploaded: " + fileName; // Return a success message
    }

    // Method to download a file from the S3 bucket
    public byte[] downloadFile(String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName) // Set the bucket name
                .key(fileName) // Set the file key (name)
                .build();

        // Download the file from S3 and get its bytes
        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
        return objectBytes.asByteArray(); // Return the file as a byte array
    }
}
