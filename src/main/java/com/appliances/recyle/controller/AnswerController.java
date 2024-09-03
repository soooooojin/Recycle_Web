package com.appliances.recyle.controller;

import com.appliances.recyle.domain.Answer;
import com.appliances.recyle.domain.Member;
import com.appliances.recyle.domain.Question;
import com.appliances.recyle.repository.MemberRepository;
import com.appliances.recyle.service.AnswerService;
import com.appliances.recyle.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/echopickup")
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;
    private final MemberRepository memberRepository;

    @Autowired
    public AnswerController(AnswerService answerService, QuestionService questionService, MemberRepository memberRepository) {
        this.answerService = answerService;
        this.questionService = questionService;
        this.memberRepository = memberRepository;
    }

    // 답글 작성 폼을 보여주는 메서드
    @GetMapping("/answer/{qno}")
    public String showAnswerForm(@PathVariable Long qno, Model model) {
        model.addAttribute("qno", qno);
        return "echopickup/answer";  // answer.html로 이동
    }

    @PostMapping("/answer")
    public String createAnswer(@RequestParam Long qno,
                               @RequestParam String acomment,
                               @RequestParam String email,
                               RedirectAttributes redirectAttributes) {
        Question question = questionService.readQuestion(qno);

        // 이미 답글이 있는지 확인
        if (question.getAnswer() != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 답글이 존재합니다.");
            return "redirect:/echopickup/question-list/" + qno;
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email: " + email));

        Answer answer = Answer.builder()
                .question(question)
                .member(member)
                .acomment(acomment)
                .build();

        answerService.createAnswer(answer);

        redirectAttributes.addFlashAttribute("message", "답글이 등록되었습니다.");

        return "redirect:/echopickup/question-list";
    }
}
