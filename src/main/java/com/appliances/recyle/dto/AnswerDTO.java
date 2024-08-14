package com.appliances.recyle.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDTO {

    @NotEmpty
    private Long ano;

    @NotEmpty
    private Long qno;

    @NotEmpty
    private String email;

    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    private String acomment; //답변 내용
}
