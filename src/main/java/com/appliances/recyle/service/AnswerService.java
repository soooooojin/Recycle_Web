package com.appliances.recyle.service;

import com.appliances.recyle.domain.Answer;
import java.util.List;

public interface AnswerService {
    Answer createAnswer(Answer answer);

    Answer readAnswer(Long ano);

    List<Answer> readAllAnswers();

    void deleteAnswer(Long ano);


}
