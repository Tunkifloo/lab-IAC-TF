package com.makethediference.mtdapi.service.aws;

import io.minio.*;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class R2Service {

    private final MinioClient minioClient;

    @Value("${CLOUDFLARE_R2_BUCKET_NAME}")
    private String bucketName;

    @Value("${CLOUDFLARE_R2_ENDPOINT}")
    private String endpoint;

    public R2Service(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Sube un archivo a Cloudflare R2.
     */
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            InputStream fileStream = file.getInputStream();

            System.out.println("🔥 Uploading file to Cloudflare R2:");
            System.out.println("Name: " + fileName);
            System.out.println("Type: " + file.getContentType());
            System.out.println("Size: " + file.getSize());

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(fileStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            System.out.println("✅ Upload successful!");
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();  // Print full error details
            throw new RuntimeException("Error al subir archivo a Cloudflare R2: " + e.getMessage(), e);
        }
    }

    /**
     * Genera la URL pública del archivo.
     */
    public String getFileUrl(String fileKey) {
        return "https://pub-98b219d2225448e198655a0ecbea1653.r2.dev/" + fileKey;
    }

    /**
     * Verifica si un objeto existe en Cloudflare R2.
     */
    public boolean doesObjectExist(String fileKey) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileKey)
                            .build()
            );
            return true;
        } catch (MinioException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia del archivo en Cloudflare R2", e);
        }
    }

    /**
     * Descarga un archivo de Cloudflare R2.
     */
    public byte[] downloadFile(String fileKey) {
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileKey)
                            .build()
            );

            return inputStream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error al descargar archivo de Cloudflare R2", e);
        }
    }

    /**
     * Elimina un archivo de Cloudflare R2.
     */
    public void deleteFile(String fileKey) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileKey)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar archivo de Cloudflare R2", e);
        }
    }
}
