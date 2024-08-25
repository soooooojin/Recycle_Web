package com.appliances.recyle.controller;

import com.appliances.recyle.domain.Question;
import com.appliances.recyle.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/echopickup/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        return ResponseEntity.ok(questionService.createQuestion(question));
    }

    @GetMapping("/{qno}")
    public ResponseEntity<Question> readQuestion(@PathVariable Long qno) {
        return ResponseEntity.ok(questionService.readQuestion(qno));
    }

    @GetMapping
    public ResponseEntity<List<Question>> readAllQuestions() {
        return ResponseEntity.ok(questionService.readAllQuestions());
    }

    @PutMapping("/{qno}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long qno, @RequestBody Question question) {
        return ResponseEntity.ok(questionService.updateQuestion(qno, question));
    }

    @DeleteMapping("/{qno}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long qno) {
        questionService.deleteQuestion(qno);
        return ResponseEntity.noContent().build();
    }
}
