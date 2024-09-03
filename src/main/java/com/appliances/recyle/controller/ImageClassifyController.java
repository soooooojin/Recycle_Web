package com.appliances.recyle.controller;

import com.appliances.recyle.dto.PredictionResponseDTO;
import com.appliances.recyle.service.ImageUploadService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@Log4j2
public class ImageClassifyController {
        private final ImageUploadService imageUploadService;

    @Autowired
    public ImageClassifyController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/classify")
    public ResponseEntity<PredictionResponseDTO> classifyImage(@RequestParam("image") MultipartFile image) {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            // Django 서버로 이미지 전송 및 응답 처리
            PredictionResponseDTO predictionResponse = imageUploadService.sendImageToDjangoServer(image.getBytes(), image.getOriginalFilename());

            // 응답을 PredictionResponseDTO 객체로 반환
            return new ResponseEntity<>(predictionResponse, HttpStatus.OK);

//            // 응답을 String 형식으로 변환하여 반환
//            String apiResult = String.format("predicted_class_label: %s, Confidence: %.2f", predictionResponse.getPredictedClassLabel(), predictionResponse.getConfidence());
//            return new ResponseEntity<>(apiResult, HttpStatus.OK);

        } catch (IOException e) {
            log.error("File processing error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}