package com.appliances.recyle.controller;

import com.appliances.recyle.dto.MemberDTO;
import com.appliances.recyle.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    // 회원가입 처리 (웹 요청: Form data)
    @PostMapping(value = "/join", consumes = "application/x-www-form-urlencoded")
    public String joinPostWeb(@ModelAttribute MemberDTO memberDTO, RedirectAttributes redirectAttributes) {
        log.info("joinPostWeb====================");
        log.info("JoinDTO = " + memberDTO);

        try {
            memberService.join(memberDTO);
        } catch (MemberService.IdExistException e) {
            redirectAttributes.addFlashAttribute("error", "아이디 중복입니다");
            return "redirect:/echopickup/member/join";
        }
        // 회원 가입 성공시
        redirectAttributes.addFlashAttribute("result", "회원가입 성공");
        return "redirect:/echopickup/member/login";
    }

    // 회원가입 처리 (앱 요청: JSON data)
    @PostMapping(value = "/join", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> joinPostApp(@RequestBody MemberDTO memberDTO) {
        log.info("joinPostApp====================");
        log.info("JoinDTO = " + memberDTO);

        try {
            memberService.join(memberDTO);
        } catch (MemberService.IdExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("아이디 중복입니다");
        }
        // 회원 가입 성공시
        return ResponseEntity.ok("회원가입 성공");
    }

//    @PostMapping("/join")
//    public String createUser(@ModelAttribute Member member, @RequestParam("profileImage") MultipartFile file) {
//        log.info("lsy User created" + member, "multipart : " + file
//        );
//        // 비밀번호 암호화
//        member.setPw(bCryptPasswordEncoder.encode(member.getPw()));
//        // 사용자 저장
////            Member savedMember = memberService.createUser(member);
//        return "redirect:/echopickup/member/login";
//        // Redirect to the list of users
//    }

}
