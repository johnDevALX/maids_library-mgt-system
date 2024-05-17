package com.ekene.maids_librarymanagementsystem.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties("app")
public class AppConfig {
    private String secret;
    private String redisConn;
    private String expirationDuration;
}
