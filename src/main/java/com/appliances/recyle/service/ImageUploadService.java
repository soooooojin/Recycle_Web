package com.appliances.recyle.service;

import com.appliances.recyle.domain.FileImage;
import com.appliances.recyle.dto.PredictionResponseDTO;
import com.appliances.recyle.repository.FileImageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;

@Log4j2
@Service
public class ImageUploadService {

    private final OkHttpClient client;
    private final ObjectMapper objectMapper; // JSON 파싱을 위한 ObjectMapper

    private final FileImageRepository fileImageRepository;

    // 생성자 주입을 통해 의존성 주입
    public ImageUploadService(FileImageRepository fileImageRepository) {
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
        this.fileImageRepository = fileImageRepository;
    }


    public PredictionResponseDTO sendImageToDjangoServer(byte[] imageBytes, String filename) throws IOException {
        // 이미지 파일을 MultipartBody로 구성
        RequestBody fileBody = RequestBody.create(imageBytes, MediaType.parse("image/jpeg"));

        // Multipart request body
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", filename, fileBody)
                .build();

        // Request 객체 생성
        Request request = new Request.Builder()
                .url("http://localhost:8000/classify/") // Django 서버의 URL
                .post(requestBody)
                .build();

        // 요청 실행
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 응답 바디를 String으로 읽기
            String responseBody = response.body().string();
            log.info("responseBody : " + responseBody);

            // 응답을 PredictionResponseDTO 객체로 변환
            return objectMapper.readValue(responseBody, PredictionResponseDTO.class);
        }
    }

    public String saveFileImage(MultipartFile file) throws IOException {
        FileImage fileImage = new FileImage(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        FileImage savedFileImage = fileImageRepository.save(fileImage);
        return savedFileImage.getId();
    }

    public FileImage getImage(String id) {
        return fileImageRepository.findById(id).orElse(null);
    }
    public boolean deleteImage(String id) {
        if (fileImageRepository.existsById(id)) {
            fileImageRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


//    public FileImage getFileImage(String imageId) {
//        return FileImageRepository.findById(imageId)
//                .orElseThrow(() -> new RuntimeException("Image not found"));
//    }

}
