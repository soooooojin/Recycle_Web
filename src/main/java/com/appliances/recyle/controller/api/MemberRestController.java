package com.appliances.recyle.controller.api;

import com.appliances.recyle.dto.MemberDTO;
import com.appliances.recyle.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequestMapping("/echopickup/api/member")
@RestController
@RequiredArgsConstructor
public class MemberRestController {

    @Autowired
    private final MemberService memberService;

    // 회원가입 처리 (앱 요청: JSON data)
    @PostMapping(value = "/join")
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

}
