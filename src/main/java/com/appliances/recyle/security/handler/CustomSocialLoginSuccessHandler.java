package com.appliances.recyle.security.handler;

import com.appliances.recyle.security.dto.SecurityMemberDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("=====CustomSocialLoginSuccessHandler  onAuthenticationSuccess 확인 ===============================");
        log.info(authentication.getPrincipal());

        SecurityMemberDTO securityMemberDTO = (SecurityMemberDTO) authentication.getPrincipal();

        // 소셜 로그인 사용자가 로그인에 성공하면, 추가적인 비밀번호 변경 요구 없이 바로 서비스로 리다이렉트
        log.info("소셜 로그인 사용자의 정보: " + securityMemberDTO);
        response.sendRedirect("/echopickup/index");

        /* 마이페이지 이동을 위함. 현재 필요없음

        // 소셜 로그인에 성공하면, 패스워드를 새로 만들기.. 기본은 1111로 하고.. 근데 이거 왜함?
        String encodePw = securityMemberDTO.getPw();
        log.info("패스워드를 변경해주세요. encodePw = memberSecurityDTO.getPw(); : " + encodePw);

        boolean test1 = securityMemberDTO.getPw().equals("1111");
        boolean test2 = passwordEncoder.matches("1111", securityMemberDTO.getPw());
        log.info("패스워드 일치 여부1 memberSecurityDTO.getPw().equals(\"1111\"); : " + test1);
        log.info("패스워드 일치 여부2  passwordEncoder.matches(\"1111\", memberSecurityDTO.getPw()); : " + test2);

        // 소셜 로그인은 무조건 패스워드를 1111, 설정
        // 변경이 필요함.
        // 처음에 소셜 로그인으로 최초 로그인시, 사용하는 패스워드 1111를 사용시
        // 마이페이지 수정페이지로 이동.
        if( securityMemberDTO.isSocial()
                && securityMemberDTO.getPw().equals("1111") || passwordEncoder.matches("1111", securityMemberDTO.getPw())){
            log.info("패스워드를 변경해주세요.");
            log.info("회원 정보 변경하는 페이지로 리다이렉트, 마이 페이지가 없음. 일단 수동으로 임의로 변경하기 ");
            log.info(("memberSecurityDTO 확인: " + securityMemberDTO));
            boolean test3 = securityMemberDTO.getPw().equals("1111");
            boolean test4 = passwordEncoder.matches("1111", securityMemberDTO.getPw());
            log.info("패스워드 일치 여부3 memberSecurityDTO.getPw().equals(\"1111\"); : " + test3);
            log.info("패스워드 일치 여부4  passwordEncoder.matches(\"1111\", memberSecurityDTO.getPw()); : " + test4);
            response.sendRedirect("/member/update");
            return;
        } else {
            // 기본 패스워드 1111를 사용안하고, 변경했다면, 목록 리스트 이동.
            response.sendRedirect("/echopickup/index");
        }*/
    }
}
