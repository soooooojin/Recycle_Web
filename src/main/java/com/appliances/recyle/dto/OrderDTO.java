package com.appliances.recyle.dto;

import com.appliances.recyle.domain.Item;
import com.appliances.recyle.domain.Member;
import com.appliances.recyle.domain.Pay;
import jakarta.persistence.*;
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
public class OrderDTO {

    @NotEmpty
    private Long ono;

    @NotEmpty
    private String email;

    @NotEmpty
    private String iname;

    @NotEmpty
    private Long iprice;

    private String purl; //사진 url

    @NotEmpty
    private String ostatus; //진행 상태 [진행 중, 대기, 수거 완료]

    @NotEmpty
    private String oaddress; //최종 넘기는 주소

    @NotEmpty
    private String pmethod; //결제 방식 [카드, 통장, 페이]
}
