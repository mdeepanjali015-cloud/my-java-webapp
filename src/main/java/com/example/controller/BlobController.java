package com.example.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/api/blob")
public class BlobController {

    private final String connectionString;
    private final String containerName;

    public BlobController(@Value("${azure.storage.connection-string:}") String conn,
                          @Value("${azure.storage.container:app-files}") String container) {
        this.connectionString = (conn != null && !conn.isEmpty()) ? conn : System.getenv("AZURE_STORAGE_CONNECTION_STRING");
        this.containerName = container;
    }

    private BlobContainerClient getContainerClient() {
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        BlobContainerClient containerClient = serviceClient.getBlobContainerClient(containerName);
        if (!containerClient.exists()) {
            containerClient.create();
        }
        return containerClient;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        if (connectionString == null || connectionString.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Azure connection string not configured");
        }
        try {
            BlobContainerClient containerClient = getContainerClient();
            String blobName = file.getOriginalFilename();
            if (blobName == null || blobName.isEmpty()) blobName = "upload-" + System.currentTimeMillis();
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            try (InputStream is = file.getInputStream()) {
                blobClient.upload(is, file.getSize(), true);
            }
            return ResponseEntity.ok(Map.of("name", blobClient.getBlobName(), "url", blobClient.getBlobUrl()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/download")
    public void download(@RequestParam(value = "name", required = false) String name, HttpServletResponse response) throws IOException {
        if (connectionString == null || connectionString.isEmpty()) {
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Azure connection string not configured");
            return;
        }
        BlobContainerClient containerClient = getContainerClient();
        BlobClient blobClient;
        if (name == null || name.isEmpty()) {
            Iterator<BlobItem> iterator = containerClient.listBlobs().iterator();
            if (!iterator.hasNext()) {
                response.sendError(HttpStatus.NOT_FOUND.value(), "No blobs available");
                return;
            }
            name = iterator.next().getName();
        }
        blobClient = containerClient.getBlobClient(name);
        if (!blobClient.exists()) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "Blob not found: " + name);
            return;
        }
        BlobProperties props = blobClient.getProperties();
        String filename = name;
        String contentType = props.getContentType() != null ? props.getContentType() : "application/octet-stream";
        response.setContentType(contentType);
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encoded);
        try (OutputStream os = response.getOutputStream()) {
            blobClient.download(os);
        }
    }
}
