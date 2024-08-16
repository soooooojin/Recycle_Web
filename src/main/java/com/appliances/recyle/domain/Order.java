package com.appliances.recyle.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ono;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @OneToOne
    @JoinColumn(name = "ino", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name ="pno", nullable = false)
    private Pay pay;

    private String purl; //사진 url

    @Column(length = 20, nullable = false)
    private String ostatus; //진행 상태 [진행 중, 대기, 완료]

    @Column(length = 100, nullable = false)
    private String oaddress; //주소
}
