package com.appliances.recyle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class PredictionResponseDTO {
    @JsonProperty("predicted_class_index")
    private int predictedClassIndex;    // 예측된 클래스 인덱스

    @JsonProperty("predicted_class_label")
    private String predictedClassLabel; // 예측된 클래스 레이블

    @JsonProperty("confidence")
    private double confidence;          // 예측에 대한 신뢰도

//    @JsonProperty("class_confidences")
//    private Map<String, Double> classConfidences; // 각 클래스에 대한 확률

}
