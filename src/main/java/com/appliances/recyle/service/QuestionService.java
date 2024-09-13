package com.appliances.recyle.service;

import com.appliances.recyle.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {
    Question createQuestion(Question question);

    Question readQuestion(Long qno);

    List<Question> readAllQuestions();

    Question updateQuestion(Long qno, Question question);

    void deleteQuestion(Long qno);

    // 페이징을 위한 메서드 추가
    Page<Question> getQuestions(Pageable pageable);

    // 이메일로 문의 목록을 페이지네이션으로 조회하는 메서드
    Page<Question> findQuestionsByMemberEmail(String email, Pageable pageable);
}
