package com.appliances.recyle.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

    @NotEmpty
    private Long qno;

    @NotEmpty
    private String email;

    @NotEmpty
    private String qtitle; //질문 제목

    private String qcomment; //질문 내용

    private LocalDateTime qdate; //질문 작성 일시
}
