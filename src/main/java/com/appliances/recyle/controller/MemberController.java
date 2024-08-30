package com.appliances.recyle.controller;

import com.appliances.recyle.dto.MemberDTO;
import com.appliances.recyle.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
@RequestMapping("/echopickup/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인
    @GetMapping("/login")
    public void loginGet(String error, String logout, RedirectAttributes redirectAttributes) {
        log.info("========logout Test========\n" + logout);

        if (logout != null) {
            log.info("logged out successfully");
        }
        if (error != null) {
            log.info("logged error successfully" + error);
            redirectAttributes.addFlashAttribute("error", "");
        }
    }

//    // 자체 security.. 임시(아이디만 있는지 확인함).. security 걸면 필요 없어짐..
//    @PostMapping("/login")
//    public String loginPost(MemberDTO memberDTO, RedirectAttributes redirectAttributes) {
//        log.info("loginPost====================");
//        log.info("loginDTO = " + memberDTO);
//
//        boolean value  = memberService.checkid(memberDTO.getEmail());
//        log.info("value = " + value);
//
//        if(!value){
//            redirectAttributes.addFlashAttribute("error", "아이디 중복입니다");
//            return "redirect:/echopickup/member/login";
//        }
//
//        redirectAttributes.addFlashAttribute("result","로그인 성공");
//        return "redirect:/echopickup/index";
//    }
    // 회원 수정
//    @GetMapping("/update")
//    public void updateGet(@AuthenticationPrincipal UserDetails user, Model model) {
//        model.addAttribute("user", user);
//    }

    // 회원가입
    @GetMapping("/join")
    public void joinGet() {
        log.info("joinGet====================");
    }

    // 회원 가입 로직 처리
    @PostMapping("/join")
    public String joinPost(MemberDTO memberDTO,
                           RedirectAttributes redirectAttributes) {
        log.info("joinPost====================");
        log.info("JoinDTO = " + memberDTO);


        try {
            memberService.join(memberDTO);
        } catch (MemberService.IdExistException e) {
            redirectAttributes.addFlashAttribute("error", "아이디 중복입니다");
            return "redirect:/echopickup/member/join";
        }
        // 회원 가입 성공시
        redirectAttributes.addFlashAttribute("result","회원가입 성공");
        return "redirect:/echopickup/member/login";
    }

}
