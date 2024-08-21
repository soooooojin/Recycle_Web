package com.appliances.recyle.repository;

import com.appliances.recyle.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    //일반 로그인으로 검색하기
    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.email = :email and m.social = false")
    Optional<Member> getWithRoles(String email);

    // 이메일으로 유저 확인.
    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(String email);

}
