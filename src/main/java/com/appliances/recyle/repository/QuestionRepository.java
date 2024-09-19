package com.appliances.recyle.repository;

import com.appliances.recyle.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // 이메일을 기준으로 문의 목록을 페이지네이션으로 가져오는 메서드
    Page<Question> findByMemberEmail(String email, Pageable pageable);
}
