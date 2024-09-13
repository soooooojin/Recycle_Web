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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/echopickup/mypage")  // REST API 경로 설정
@RequiredArgsConstructor
public class MypageController {

    private final MemberService2 memberService2;
    private final QuestionService questionService;

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
