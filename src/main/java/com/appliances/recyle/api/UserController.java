package com.appliances.recyle.api;

import com.appliances.recyle.dto.MemberDTO;
import com.appliances.recyle.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

//    @Autowired
//    private MemberService memberService;
//
//    // 특정 유저 정보를 가져오는 API (예시로 ID 기반 조회)
//    @GetMapping("/{id}")
//    public ResponseEntity<MemberDTO> getUserById(@PathVariable String email) {
//        MemberDTO user = memberService.getUserById(email);
//        if (user != null) {
//            return ResponseEntity.ok(user);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }
}
