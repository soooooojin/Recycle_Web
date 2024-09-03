package com.appliances.recyle.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;

    @OneToOne
    @JoinColumn(name = "qno", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @Column(length = 200)
    private String acomment; // 답변 내용// 답변 내용
}
