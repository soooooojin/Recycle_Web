package com.appliances.recyle.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private Long amount;  //결제 총금액

    @NotEmpty
    private LocalDate pdate; //결제 날짜
}
