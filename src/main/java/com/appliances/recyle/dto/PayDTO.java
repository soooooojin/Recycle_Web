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
public class PayDTO {

    @NotEmpty
    private Long pno;

    @NotEmpty
    private String pmethod; //결제 방식 [카드, 통장, 페이]

    @NotEmpty
    private String pstatus; //결제 상태 [진행중, 환불]

    @NotEmpty
    private Long amount;  //결제 금액

    @NotEmpty
    private LocalDateTime pdate; //결제 날짜
}
