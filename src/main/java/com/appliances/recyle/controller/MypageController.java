package com.appliances.recyle.controller;

import com.appliances.recyle.domain.Member;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/echopickup/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MemberService2 memberService2;
    private final QuestionService questionService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String mypageGet() {
        return "/echopickup/mypage";
    }

    // 주소 업데이트를 위한 POST 요청 처리
    @PostMapping
    public String updateAddressPost(@RequestBody Map<String, String> requestData) {
        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((User) authentication.getPrincipal()).getUsername();

        // 사용자의 회원 정보를 가져오기
        Member member = memberService2.getMemberByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 새로운 정보 업데이트
        String address = requestData.get("address");
        String phone = requestData.get("phone");


        // 새로운 정보 업데이트
        member.setAddress(address);
        member.setPhone(phone);
        memberService2.save(member);

        log.info("주소값 : "+address);
        log.info("휴대폰: "+phone);
        log.info(member);

        if (address == null || phone == null) {
            log.error("데이터가 비어 있습니다.");
            return "redirect:/echopickup/mypage?error=true";
        }
        // 성공 시 마이페이지로 리다이렉트
        return "redirect:/echopickup/mypage";
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
            // 사용자 정보 가져오기
            Member member = memberService2.getMemberByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

            // 현재 비밀번호가 올바른지 확인
            if (!passwordEncoder.matches(currentPassword, member.getPw())) {
                model.addAttribute("message", "현재 비밀번호가 일치하지 않습니다.");
                model.addAttribute("status", "error");
                return "/echopickup/mypage/changepassword";
            }

            // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("message", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
                model.addAttribute("status", "error");
                return "/echopickup/mypage/changepassword";
            }

            // 비밀번호가 이전과 동일한지 확인 (옵션)
            if (passwordEncoder.matches(newPassword, member.getPw())) {
                model.addAttribute("message", "새 비밀번호는 이전 비밀번호와 동일할 수 없습니다.");
                model.addAttribute("status", "error");
                return "/echopickup/mypage/changepassword";
            }

            // 새 비밀번호 저장
            member.setPw(passwordEncoder.encode(newPassword));
            memberService2.save(member);

            // 성공 메시지 설정
            model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
            model.addAttribute("status", "success");

        } catch (Exception e) {
            log.error("비밀번호 변경 중 오류 발생", e);
            model.addAttribute("message", "시스템 오류가 발생했습니다.");
            model.addAttribute("status", "error");
        }

        // 변경 후 다시 비밀번호 변경 페이지로 이동 (메시지 전달)
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
