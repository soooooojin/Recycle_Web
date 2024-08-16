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

    @NotEmpty
    private String acomment; //답변 내용
}
