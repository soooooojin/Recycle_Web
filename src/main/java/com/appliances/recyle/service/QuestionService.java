package com.appliances.recyle.service;

import com.appliances.recyle.domain.Question;

import java.util.List;

public interface QuestionService {
    Question createQuestion(Question question);

    Question readQuestion(Long qno);

    List<Question> readAllQuestions();

    Question updateQuestion(Long qno, Question question);

    void deleteQuestion(Long qno);
}