package com.vitalmoments.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "photofile.manager")
public class PhotoFileManagerConfig {
    private String magickHome;
    private List<String> rawExtensions;
}
