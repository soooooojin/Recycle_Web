package com.appliances.recyle.security.handler;

import com.appliances.recyle.domain.Member;
import com.appliances.recyle.security.dto.SecurityMemberDTO;
import com.appliances.recyle.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("Login Success Handler................................");

        // Content-Type에 UTF-8 설정 추가
        // 앱에서 응답 받을시 한글 깨지는 문제 방지
        response.setContentType("application/json; charset=UTF-8");
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info("authentication : " +authentication);
        log.info("authentication.getName() : " +authentication.getName()); //username

        // 인증한 유저명 으로 교체
        Map<String, Object> claim = Map.of("username", authentication.getName());
        //Access Token 유효기간 1일
        String accessToken = jwtUtil.generateToken(claim, 1);
        //Refresh Token 유효기간 30일
        String refreshToken = jwtUtil.generateToken(claim, 30);

        // JWT를 응답 헤더에 추가
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh-Token", refreshToken);

        //
        SecurityMemberDTO securityMemberDTO = (SecurityMemberDTO) authentication.getPrincipal();

        Gson gson = new Gson();

        Map<String,String> keyMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "username", authentication.getName(),
                "email",securityMemberDTO.getEmail(),
                "name",securityMemberDTO.getMname(),
                "phone",securityMemberDTO.getPhone(),
                "address",securityMemberDTO.getAddress(),
                "social", String.valueOf(securityMemberDTO.isSocial())
                );
        log.info("====keyMap 확인 ===============================" + keyMap);

        String jsonStr = gson.toJson(keyMap);
        log.info("====jsonStr 확인 ===============================" + jsonStr);
        response.getWriter().println(jsonStr);

        //카카오 로그인 후, 처리 로직
        log.info("=====CustomSocialLoginSuccessHandler  onAuthenticationSuccess 확인 ===============================");
        log.info("====authentication.getPrincipal()====="+authentication.getPrincipal());

        log.info("====memberSecurity 확인 ===============================" + securityMemberDTO);
        String encodePw = securityMemberDTO.getPw();
        log.info("패스워드를 변경해주세요. encodePw = securityMemberDTO.getPw(); : " + encodePw);

        boolean test1 = securityMemberDTO.getPw().equals("1111");
        boolean test2 = passwordEncoder.matches("1111", securityMemberDTO.getPw());
        log.info("패스워드 일치 여부1 securityMemberDTO.getMpw().equals(\"1111\"); : " + test1);
        log.info("패스워드 일치 여부2  passwordEncoder.matches(\"1111\", securityMemberDTO.getMpw()); : " + test2);

        // 소셜 로그인은 무조건 패스워드를 1111 , 설정
        // 변경이 필요함.
        // 처음에 소셜 로그인으로 최초 로그인시, 사용하는 패스워드 1111 를 사용시
        // 마이페이지 수정페이지로 이동.
        if( securityMemberDTO.isSocial()
                && securityMemberDTO.getPw().equals("1111") || passwordEncoder.matches("1111", securityMemberDTO.getPw())){
            log.info("패스워드를 변경해주세요.");
            log.info("회원 정보 변경하는 페이지로 리다이렉트, 마이 페이지가 없음. 일단 수동으로 임의로 변경하기 ");
            log.info(("securityMemberDTO 확인: " + securityMemberDTO));
            boolean test3 = securityMemberDTO.getPw().equals("1111");
            boolean test4 = passwordEncoder.matches("1111", securityMemberDTO.getPw());
            log.info("패스워드 일치 여부3 securityMemberDTO.getMpw().equals(\"1111\"); : " + test3);
            log.info("패스워드 일치 여부4  passwordEncoder.matches(\"1111\", securityMemberDTO.getMpw()); : " + test4);
            response.sendRedirect("/echopickup/member/mypage");
            return;
        }
        else if(securityMemberDTO.isSocial()) {
            // 기본 패스워드 1111를 사용안하고, 변경했다면, 목록 리스트 이동.
            response.sendRedirect("/echopickup/index");
        }
    }
}