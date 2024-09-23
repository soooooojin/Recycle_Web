package com.appliances.recyle.controller;

import com.appliances.recyle.domain.Question;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                                     Authentication authentication,Model model) {
        // 현재 로그인된 사용자 정보 가져오기
        String email = ((User) authentication.getPrincipal()).getUsername();

        try {
            boolean isChanged = memberService2.changeMemberPassword(email, currentPassword, newPassword, confirmPassword);
            if (isChanged) {
                model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
                model.addAttribute("status", "success");
//                return "redirect:/echopickup/mypage/changepassword?success=true";
            } else {
                model.addAttribute("message", "비밀번호 변경 중 오류가 발생했습니다. 다시 시도해주세요.");
                model.addAttribute("status", "error");
//                return "redirect:/echopickup/mypage/changepassword?error=true";
            }
        } catch (Exception e) {
            log.error("비밀번호 변경 중 오류 발생", e);
            model.addAttribute("message", "시스템 오류가 발생했습니다.");
            model.addAttribute("status", "error");
//            return "redirect:/echopickup/mypage/changepassword?error=true";
        }

        // 비밀번호 변경 페이지로 다시 리다이렉트 (메시지 전달)
        return "/echopickup/mypage/changepassword";
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
