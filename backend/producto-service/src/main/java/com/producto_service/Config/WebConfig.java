package com.producto_service.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:/data/uploads/productos}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // uploadDir = /data/uploads/productos
        // URL:  /uploads/productos/generales/file.jpg
        // File: /data/uploads/productos/generales/file.jpg
        String baseLocation = "file:" + uploadDir.replaceAll("/productos$", "") + "/productos/";
        registry.addResourceHandler("/uploads/productos/**")
                .addResourceLocations(baseLocation);
    }
}
