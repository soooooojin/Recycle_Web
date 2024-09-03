package com.appliances.recyle.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Member {

    @Id
    @Column(length = 40, nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String mname;
    @Column(length = 100, nullable = false)
    private String pw;
    @Column(length = 100, nullable = false)
    private String address;
    @Column(length = 20, nullable = false)
    private String phone;

//    // 권한 [0: 일반사용자, 1: 공지만 쓸 수 있는 관리자, 2:전부 다 가능한 관리자]
//    @Column(length = 10, nullable = false)
//    private String roll;

    // 소셜 로그인 확인
    private boolean social;

    private boolean del;

    // 멤버를 조회시 roleSet을 같이 조회를 하기.
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void addRole(MemberRole memberRole) {
        this.roleSet.add(memberRole);
    }
}
