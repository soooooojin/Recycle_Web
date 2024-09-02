package com.appliances.recyle.service;

import com.appliances.recyle.domain.Answer;
import com.appliances.recyle.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public Answer createAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    @Override
    public Answer readAnswer(Long ano) {
        return answerRepository.findById(ano)
                .orElseThrow(() -> new IllegalArgumentException("Invalid answer ID: " + ano));
    }

    @Override
    public List<Answer> readAllAnswers() {
        return answerRepository.findAll();
    }

    @Override
    public void deleteAnswer(Long ano) {
        answerRepository.deleteById(ano);
    }
}