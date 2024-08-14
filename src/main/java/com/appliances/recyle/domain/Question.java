package com.appliances.recyle.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qno;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @Column(length = 20, nullable = false)
    private String qtitle; //질문 제목

    @Column(length = 200)
    private Long qcomment; //질문 내용
}
