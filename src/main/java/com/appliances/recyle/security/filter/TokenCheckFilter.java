package com.appliances.recyle.security.filter;

import com.appliances.recyle.security.CustomUserDetailsService;
import com.appliances.recyle.security.exception.AccessTokenException;
import com.appliances.recyle.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    //JWT 토큰을 검사하는 역할
    // OncePerRequestFilter : 하나의 요청에 대해서 한번씩 동작하는 필터

    private final CustomUserDetailsService customUserDetailsService;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        log.info("Request URI: " + path); // 요청된 URI 로그


        if (!path.startsWith("/api/")) {
            log.info("Skipping token check for non-API path");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Token Check Filter..........................");
        log.info("JWTUtil: " + jwtUtil);



        try{
//            validateAccessToken(request);

            Map<String, Object> payload = validateAccessToken(request);
            log.info("payload: " + payload);
            //username
            String username = (String)payload.get("username");

            log.info("username: " + username);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            log.info("UserDetails: " + userDetails);  // 사용자 정보 출력

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());


            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authentication set in SecurityContext");

            filterChain.doFilter(request,response);
        }catch(AccessTokenException accessTokenException){

            accessTokenException.sendResponseError(response);
        }


//        filterChain.doFilter(request, response);
    }

    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {

        String headerStr = request.getHeader("Authorization");

        if(headerStr == null  || headerStr.length() < 8){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        //Bearer 생략
        String tokenType = headerStr.substring(0,6);
        String tokenStr =  headerStr.substring(7);

        if(tokenType.equalsIgnoreCase("Bearer") == false){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        log.info("TOKENSTR-----------------------------");
        log.info(tokenStr);
        log.info("----------------------------------------");

        try{
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);
            log.info("TokenCheckFilter 의 jwtUtil.validateToken(tokenStr) values-----------------------------");
            log.info(values);
            log.info("----------------------------------------");
            return values;
        }catch(MalformedJwtException malformedJwtException){
            log.error("MalformedJwtException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        }catch(SignatureException signatureException){
            log.error("SignatureException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        }catch(ExpiredJwtException expiredJwtException){
            log.error("ExpiredJwtException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }

}
