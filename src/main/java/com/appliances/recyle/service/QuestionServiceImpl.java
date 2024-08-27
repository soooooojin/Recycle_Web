package com.appliances.recyle.service;

import com.appliances.recyle.domain.Question;
import com.appliances.recyle.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Question> existingQuestion = questionRepository.findById(qno);
        if (existingQuestion.isPresent()) {
            Question updatedQuestion = existingQuestion.get();
            updatedQuestion.setQtitle(question.getQtitle());
            updatedQuestion.setQcomment(question.getQcomment());
            return questionRepository.save(updatedQuestion);
        }
        return null;
    }

    @Override
    public void deleteQuestion(Long qno) {
        questionRepository.deleteById(qno);
    }
}