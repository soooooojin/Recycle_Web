package com.appliances.recyle.config;


import com.appliances.recyle.security.handler.Custom403Handler;
import com.appliances.recyle.security.handler.CustomSocialLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity()
@EnableWebSecurity
@Log4j2
public class CustomSecurityConfig {

//    private DataSource dataSource;
//    private CustomUserDetailsService customUserDetailsService;
    // ip에서 분당 요청 횟수 제한
    private final RateLimitingFilter rateLimitingFilter;

    // 평문 패스워드를 해시 함수 이용해서 인코딩함.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("시큐리티 동작 확인 ====CustomSecurityConfig======================");

        // 폼 방식 일경우
        http
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/echopickup/member/login")
                                .permitAll()
                );
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/api/users", "/users/new", "/refreshToken").permitAll()
//                                .requestMatchers("/users/**").authenticated()
//
//                )
//                .sessionManagement(sessionManagement ->
//                        sessionManagement
//                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                )
//                .authorizeRequests(authorizeRequests ->
//                                authorizeRequests
////                                .requestMatchers("/users/new","/refreshToken").permitAll()
////                                .requestMatchers("/users/**","/reservations/**","/payments/**").authenticated()
//                                        .anyRequest().permitAll()
//
//                );


        // 로그 아웃 설정.
        http.logout(logout -> logout
                .logoutUrl("/echopickup/member/logout")
                .logoutSuccessUrl("/echopickup/member/login?logout")
        );

        //로그인 성공 후, 리다이렉트 될 페이지. 간단한 버전.
        http.formLogin(formLogin ->
                formLogin.defaultSuccessUrl("/echopickup/index", true)
        );

        // 기본은 csrf 설정이 on, 작업시에는 끄고 작업하기.
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

//        // 특정 페이지에 접근 권한 설정.
//        http.authorizeRequests()
//
//                /*
//                // 정적 자원 모두 허용.
//                .requestMatchers("/css/**", "/js/**","/images/**","/images2/**").permitAll()
//                // 리스트는 기본으로 다 들어갈수 있게., 모두 허용
//                .requestMatchers("/", "/board/list","/member/join", "/login","/member/login", "/joinUser","/joinForm","/findAll","/images/**","/members/**", "/item/**").permitAll()
//                // 로그인 후 확인 하기. 권한 예제) hasRole("USER"),hasRole("ADMIN")
//                .requestMatchers("/board/register","/board/read","/board/update" ).authenticated()
//                // 권한  관리자만, 예제로 , 수정폼은 권한이 관리자여야 함.
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                // 위의 접근 제어 목록 외의 , 다른 어떤 요청이라도 반드시 인증이 되어야 접근이 된다.
////                .anyRequest().authenticated();
//
//                 */
//                //확인용으로 사용하기.(제한두지 않기)
//                .anyRequest().permitAll();

        //403 핸들러 적용하기.
        http.exceptionHandling(
                accessDeny -> {
                    accessDeny.accessDeniedHandler(accessDeniedHandler());
                }
        );

        //401 핸들러 적용하기.
//        http.exceptionHandling(
//                handle -> {
//                        handle.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
//                }
//                        );


//        // 자동로그인 설정 1
//        http.rememberMe(
//                httpSecurityRememberMeConfigurer ->
//                        httpSecurityRememberMeConfigurer
//                                // 토큰 생성시 사용할 암호
//                                .key("12345678")
//                                // 스프링 시큐리티에서 정의해둔 Repository
//                                .tokenRepository(persistentTokenRepository())
//                                // UserDetail를 반환하는 사용자가 정의한 클래스
//                                .userDetailsService(customUserDetailsService)
//                                // 토큰의 만료 시간.
//                                .tokenValiditySeconds(60*60*24*30)
//        );

        //카카오 로그인 API 설정
        http.oauth2Login(
                // 소셜로그인 성공 후 처리, 적용하기.
                oauthLogin -> oauthLogin.loginPage("/echopickup/member/login")
                        .successHandler(authenticationSuccessHandler())
        );

        // 동일 아이피에서 분당 요청 횟수 10회 제한 , 필터 설정.
        http.addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);

        // 캐시 설정 비활성화
//        http.headers(
//                cacheDisable -> cacheDisable.cacheControl(
//                        disable -> disable.disable()
//                )
//        );
        return http.build();
    }

    // 소셜 로그인 성공 후, 후처리 하는 빈등록.
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }

    // 자동로그인 설정 2. 시스템에서 정의해둔 기본 약속.
//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//        // 시큐리티에서 정의 해둔 구현체
//        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
//        repo.setDataSource(dataSource);
//        return repo;
//    }

    //정적 자원 시큐리티 필터 항목에 제외하기.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("시큐리티 동작 확인 ====webSecurityCustomizer======================");
        return (web) ->
                web.ignoring()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    //사용자 정의한 403 예외 처리 (403 : 접근 권한 없을 때 발생)
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }
}

