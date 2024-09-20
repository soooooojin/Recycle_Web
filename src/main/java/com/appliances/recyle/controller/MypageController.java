package com.appliances.recyle.controller;

import com.appliances.recyle.domain.Member;
import com.appliances.recyle.service.MemberService2;
import com.appliances.recyle.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.appliances.recyle.domain.Question;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/echopickup/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MemberService2 memberService2;
    private final QuestionService questionService;

    @GetMapping
    public String mypageGet() {
        return "/echopickup/mypage";
    }

    // 회원탈퇴 처리
    @GetMapping("/delete")
    public String deleteMember(Authentication authentication) {
        String email = ((User) authentication.getPrincipal()).getUsername();

        // 회원 삭제 로직 실행
        memberService2.deleteMemberByEmail(email);

        // 회원탈퇴 후 로그인 페이지로 리다이렉트
        return "redirect:/echopickup/member/login";
    }

    @GetMapping("/changepassword")
    public String changepasswordGet() {
        return "/echopickup/mypage/changepassword";
    }

    @PostMapping("/changepassword")
    public String changePasswordPost(@RequestParam("currentPassword") String currentPassword,
                                     @RequestParam("newPassword") String newPassword,
                                     @RequestParam("confirmPassword") String confirmPassword,
                                     Authentication authentication) {
        String email = ((User) authentication.getPrincipal()).getUsername();

        try {
            boolean isChanged = memberService2.changeMemberPassword(email, currentPassword, newPassword, confirmPassword);
            if (isChanged) {
                return "redirect:/echopickup/mypage/changepassword?success=true";
            } else {
                return "redirect:/echopickup/mypage/changepassword?error=true";
            }
        } catch (Exception e) {
            log.error("비밀번호 변경 중 오류 발생", e);
            return "redirect:/echopickup/mypage/changepassword?error=true";
        }
    }

    @GetMapping("/progress")
    public void progressGet() {
    }

    @GetMapping("/question-list")
    public String questionListGet(Model model, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Page<Question> questions = questionService.findQuestionsByMemberEmail(currentUserEmail, pageable);
        model.addAttribute("questions", questions);

        return "echopickup/mypage/question-list";
    }
}
