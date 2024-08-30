package com.appliances.recyle.security;


import com.appliances.recyle.domain.Member;
import com.appliances.recyle.domain.MemberRole;
import com.appliances.recyle.repository.MemberRepository;
import com.appliances.recyle.security.dto.SecurityMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 카카오 소셜 로그인시 , 로그인 로직 처리를 여기서 함.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // userRequest : 카카오 로그인 관련 정보
        log.info("Oauth2UserService : userRequest = " + userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName(); //소셜 이름(카카오, 구글 등)

        log.info("Oauth2UserService_clientRegistration : clientName = " + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // paramMap : 소셜 로그인 정보가 다 들어가 있음.
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        paramMap.forEach((k, v) -> {
            log.info("Oauth2UserService : key = " + k + " value = " + v);
        });

        String email = null;
        String mname = null;
//        String profileUrlThumbnail = null;

        // 카카오 말고 다른 소셜로그인 사용 시, 분리 가능
        switch (clientName) {
            case "kakao":
                // 소셜 로그인 정보에서, 이메일만 추출.
                email = getKakaoEmail(paramMap);
                // 소셜 로그인 정보에서, 프로필 이미지 외부 미디어 서버 주소 추출.
//                profileUrlThumbnail = getKakaoProfile(paramMap);
                mname = getKakaoName(paramMap);
                break;
        }

        log.info("Oauth2UserService : email = " + email + ", mname = " + mname);

        return generateDTO(email, mname, paramMap);
    }

    private SecurityMemberDTO generateDTO(String email, String mname, Map<String, Object> paramMap) {

        Optional<Member> result = memberRepository.findByEmail(email);
        // 디비에 유저가 없다면, 소셜 로그인(이메일 포함)
        if (result.isEmpty()) {

            String randomPassword = generateRandomPassword();

            // 회원 추가 하기
            // DTO -> entity
            Member member = Member.builder()
                    .email(email)
                    .mname(mname)
                    .pw(passwordEncoder.encode(randomPassword))
                    .social(true)
                    .build();

            //권한, 일반 USER
            member.addRole(MemberRole.USER);

            memberRepository.save(member);

            // entity -> DTO
            SecurityMemberDTO securityMemberDTO =
                    new SecurityMemberDTO(
                            email, randomPassword,
                            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")),
                            mname, null, null, true,
                            false, paramMap);

            return securityMemberDTO;
        }
        else { // 직접 로그인한 정보가 있다, 디비에 소셜 로그인한 이메일이 존재
            Member member = result.get();
            SecurityMemberDTO securityMemberDTO =
                    new SecurityMemberDTO(
                            member.getEmail(),
                            member.getPw(),
                            member.getRoleSet().stream()
                                    .map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                                    .collect(Collectors.toList()),
                            member.getMname(),
                            member.getAddress(),
                            member.getPhone(),
                            member.isSocial(),
                            member.isDel(), paramMap //기존 사용자도 최신 정보 갱신
                    );
            return securityMemberDTO;
        }
    }

    private String generateRandomPassword() {
        // UUID를 사용해 임의의 비밀번호 생성
        return UUID.randomUUID().toString();
    }

    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("Oauth2UserService : kakao = ");

        Object value = paramMap.get("kakao_account");
        log.info("Oauth2UserService : kakao_account = " + value);

        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String) accountMap.get("email");
        log.info("Oauth2UserService : email = " + email);
        return email;
    }

    private String getKakaoName(Map<String, Object> paramMap) {
        log.info("Oauth2UserService : kakao = ");

        Object value = paramMap.get("properties");
        log.info("Oauth2UserService : properties = " + value);

        LinkedHashMap propertiesMap = (LinkedHashMap) value;

        String nickname = (String) propertiesMap.get("nickname");
        log.info("Oauth2UserService : nickname = " + nickname);
        return nickname;
    }

}
