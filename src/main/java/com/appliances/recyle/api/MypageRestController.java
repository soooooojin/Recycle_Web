package com.appliances.recyle.api;


import com.appliances.recyle.domain.Member;
import com.appliances.recyle.dto.PasswordChangeRequestDTO;
import com.appliances.recyle.service.MemberService2;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageRestController {

    private final MemberService2 memberService2;
    private final PasswordEncoder passwordEncoder;  // 비밀번호 암호화

    // 사용자 정보를 JSON으로 반환하는 API (RESTful)
    @GetMapping("/user-info")
        public ResponseEntity<Member> getUserInfo() {
            // 현재 인증된 사용자 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 사용자의 이메일 가져오기 (UserDetailsService로 사용자를 설정했다는 가정)
            String email = ((User) authentication.getPrincipal()).getUsername();

            // MemberService2를 통해 데이터베이스에서 회원 정보 가져오기
            Optional<Member> member = memberService2.getMemberByEmail(email);

            // 회원 정보가 있으면 JSON 형식으로 반환, 없으면 404 에러 반환
            return member.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/changepassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequestDTO passwordChangeRequestDTO) {
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // 로그인된 사용자의 이메일

        // 데이터베이스에서 사용자 정보 가져오기
        Member member = memberService2.getMemberByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(passwordChangeRequestDTO.getCurrentPassword(), member.getPw())) {
            return ResponseEntity.badRequest().body("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
        if (!passwordChangeRequestDTO.getNewPassword().equals(passwordChangeRequestDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 암호화 후 저장
        member.setPw(passwordEncoder.encode(passwordChangeRequestDTO.getNewPassword()));
        memberService2.save(member);  // 변경된 비밀번호를 DB에 저장

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}