package com.appliances.recyle.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class ImageClassifyController {

    @PostMapping("/classify")
    public ResponseEntity<String> classifyImage(@RequestParam("image") MultipartFile image) {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("No file was submitted.");
        }

        // PyCharm에서 실행 중인 Python 서버의 URL
        String apiUrl = "http://localhost:8000/classify/";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // MultipartFile을 임시 파일로 변환
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + image.getOriginalFilename());
            image.transferTo(convFile);

            // POST 요청 준비
            HttpPost uploadFile = new HttpPost(apiUrl);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("image", convFile);
            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);

            // 요청 실행
            HttpResponse response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();
            String apiResult = EntityUtils.toString(responseEntity, "UTF-8");

            // 임시 파일 정리
            if (!convFile.delete()) {
                System.err.println("Failed to delete the temporary file.");
            }

            // API 응답 반환
            return new ResponseEntity<>(apiResult, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File processing error: " + e.getMessage());
        }
    }
}