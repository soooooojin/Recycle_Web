package com.appliances.recyle.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    @Column(length = 20, nullable = false)
    private String pmethod; //결제 방식 [카드, 통장, 페이]

    @Column(length = 20, nullable = false)
    private String pstatus; //결제 상태 [진행중, 환불]

    private Long amount;  //결제 총금액
    private LocalDate pdate; //결제 날짜
}
