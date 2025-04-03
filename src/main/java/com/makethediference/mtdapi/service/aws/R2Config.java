package com.makethediference.mtdapi.service.aws;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class R2Config {

    @Value("${CLOUDFLARE_R2_ACCESS_KEY}")
    private String accessKey;

    @Value("${CLOUDFLARE_R2_SECRET_KEY}")
    private String secretKey;

    @Value("${CLOUDFLARE_R2_ENDPOINT}")
    private String endpoint;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}