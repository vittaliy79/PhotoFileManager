package com.vitalmoments.controller;

import com.vitalmoments.model.ImageRequest;
import com.vitalmoments.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ImageController {
    private ImageService imageService;

    @PostMapping(value = "/images", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> getImage(@RequestBody ImageRequest request) {
        return imageService.getImage(request);
    }
}


