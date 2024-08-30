package com.appliances.recyle.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    @Column(length = 20, nullable = false)
    private String iname; //제품 이름

    @Column(nullable = false)
    private Long iprice; //스티커 가격

    private String imageUrl;

}
