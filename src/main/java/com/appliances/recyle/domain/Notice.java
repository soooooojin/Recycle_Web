package com.appliances.recyle.domain;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nno;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @Column(length = 20, nullable = false)
    private String ntitle; //공지 제목

    @Column(length = 200)
    private String ncomment; //공지 내용
}
