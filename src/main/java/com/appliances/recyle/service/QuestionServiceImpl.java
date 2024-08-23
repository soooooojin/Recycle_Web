package com.appliances.recyle.service;

import com.appliances.recyle.domain.Question;
import com.appliances.recyle.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public Question readQuestion(Long qno) {
        return questionRepository.findById(qno).orElse(null);
    }

    @Override
    public List<Question> readAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Question updateQuestion(Long qno, Question question) {
        if (questionRepository.existsById(qno)) {
            question.setQno(qno);
            return questionRepository.save(question);
        }
        return null;
    }

    @Override
    public void deleteQuestion(Long qno) {
        questionRepository.deleteById(qno);
    }
}
