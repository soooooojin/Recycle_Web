package com.appliances.recyle.service;

import com.appliances.recyle.domain.Question;
import com.appliances.recyle.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return questionRepository.findById(qno).orElseThrow(() -> new IllegalArgumentException("Invalid question ID: " + qno));
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
        } else {
            throw new IllegalArgumentException("Invalid question ID: " + qno);
        }
    }

    @Override
    public void deleteQuestion(Long qno) {
        if (questionRepository.existsById(qno)) {
            questionRepository.deleteById(qno);
        } else {
            throw new IllegalArgumentException("Invalid question ID: " + qno);
        }
    }
    @Override
    public Page<Question> getQuestions(Pageable pageable) {
        Page<Question> questions = questionRepository.findAll(pageable);
        questions.forEach(question -> {
            // Answer를 강제 로드하면서 로그 출력
            if (question.getAnswer() != null) {
                System.out.println("Question ID: " + question.getQno() + " has an Answer: " + question.getAnswer().getAcomment());
            } else {
                System.out.println("Question ID: " + question.getQno() + " has no Answer.");
            }
        });
        return questions;
    }
}