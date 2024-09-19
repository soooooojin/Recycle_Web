package com.appliances.recyle.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// jwt 구성 설명
//부분 속성 설명
//        Header
//typ 토큰 타입
//alg 해싱 알고리즘
//
//        Payload
//
//iss 토큰 발급자
//sub 토큰 제목
//exp 토큰 만료 시간
//iat 토큰 발급 시간
//aud 토큰 대상자
//nbf 토큰 활성 시간
//jti JWT 고유 식별자
//
//signature Header의 인코딩 + Payload의 인코딩값을 해싱 + 비밀키


@Component
@Log4j2
public class JWTUtil {

    // application.properties 에 설정
    @Value("${com.busanit501team2.jwt.secret}")
    private String key;

    public String generateToken(Map<String, Object> valueMap, int days){

        log.info("lsy generateKey..." + key);

        //헤더 부분
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ","JWT");
        headers.put("alg","HS256");

        //payload 부분 설정
        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        //테스트 시에는 짧은 유효 기간
        int time = (60 * 24) * days; //테스트는 분단위로 나중에 60*24 (일)단위변경

        //2분 단위로 조정
//        int time = (2) * days; //테스트는 분단위로 나중에 60*24 (일)단위변경

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                // test 시 분 단위 , plusMinutes()
                // 나중에 plusDays() 로 변경
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        return jwtStr;
    }


    public Map<String, Object> validateToken(String token)throws JwtException {

        Map<String, Object> claim = null;

        claim = Jwts.parser()
                //이 키는 서명 검증에 사용
                .setSigningKey(key.getBytes()) // Set Key
                // 주어진 token을 파싱하여 클레임을 검증
                .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                // JWT의 바디(body) 부분인 클레임을 추출
                .getBody();
        return claim;
        // claim 의 샘플 예시, 사용자 정의 포함
//        {
//            "sub": "user123",                // subject, 사용자 ID 또는 주체 식별자
//                "iss": "example.com",            // issuer, 토큰을 발급한 서버의 정보
//                "iat": 1622470420,               // issued at, 토큰 발급 시간 (유닉스 타임스탬프)
//                "exp": 1622474020,               // expiration, 토큰 만료 시간 (유닉스 타임스탬프)
//                "aud": "exampleClient",          // audience, 토큰의 대상
//                "role": "admin",                 // 사용자 정의 클레임, 사용자의 역할 정보
//                "email": "user123@example.com",  // 사용자 정의 클레임, 사용자의 이메일
//                "name": "John Doe"               // 사용자 정의 클레임, 사용자의 이름
//        }
    }

}
