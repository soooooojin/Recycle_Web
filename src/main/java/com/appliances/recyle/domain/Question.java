package com.appliances.recyle.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qno;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @Column(length = 20, nullable = false)
    private String qtitle; // 질문 제목

    @Column(length = 200)
    private String qcomment; // 질문 내용

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private Answer answer;
}