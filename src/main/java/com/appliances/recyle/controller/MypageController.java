package com.appliances.recyle.controller;

import com.appliances.recyle.domain.Question;
import com.appliances.recyle.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/echopickup/mypage")  // REST API 경로 설정
@RequiredArgsConstructor
public class MypageController {

    private final QuestionService questionService;

    @GetMapping
    public String mypageGet() {
        return "/echopickup/mypage";
    }

    @GetMapping("/changepassword")
    public void changepasswordGet() {

    }

    @GetMapping("/progress")
    public void progressGet() {

    }
    @GetMapping("/question-list")
    public String questionListGet(Model model, Pageable pageable) {
        // 로그인한 사용자의 이메일 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // 이메일로 필터링된 문의 목록을 페이지네이션하여 가져오기
        Page<Question> questions = questionService.findQuestionsByMemberEmail(currentUserEmail, pageable);

        // 데이터를 모델에 추가
        model.addAttribute("questions", questions);

        return "echopickup/mypage/question-list";
    }

}
