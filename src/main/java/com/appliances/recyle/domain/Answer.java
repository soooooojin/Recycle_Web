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

    @ManyToOne
    @JoinColumn(name = "qno", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @Column(length = 200)
    private Long qcomment; //답변 내용
}
