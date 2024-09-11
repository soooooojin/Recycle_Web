package com.appliances.recyle.controller;

import com.appliances.recyle.domain.Member;
import com.appliances.recyle.service.MemberService2;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("/echopickup/mypage")  // REST API 경로 설정
@RequiredArgsConstructor
public class MypageController {


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


}
