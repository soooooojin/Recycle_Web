package com.appliances.recyle.controller;

import com.appliances.recyle.domain.Question;
import com.appliances.recyle.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/echopickup")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // 문의 작성 폼을 보여주는 메서드
    @GetMapping("/question")
    public String showQuestionForm() {
        return "echopickup/question";  // echopickup/question.html로 이동
    }

    // 문의 작성 폼 제출을 처리하는 메서드
    @PostMapping("/question")
    public String createQuestion(@RequestParam String qtitle,
                                 @RequestParam String qcomment) {
        Question question = Question.builder()
                .qtitle(qtitle)
                .qcomment(qcomment)
                .build();
        questionService.createQuestion(question);
        return "redirect:/echopickup/questions";  // 등록 후 목록 페이지로 리다이렉트
    }
}