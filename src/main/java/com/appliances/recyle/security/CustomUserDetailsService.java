package com.appliances.recyle.security;

import com.appliances.recyle.domain.Member;
import com.appliances.recyle.repository.MemberRepository;
import com.appliances.recyle.security.dto.SecurityMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    // 일반 로그인 로직 처리시, 여기를 반드시 거쳐 감.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("CustomUserDetailsService loadUserByUsername 확인 : "+ email);

        // 로그인 한 이메일을 디비에서 검색을 함.
        Optional<Member> result = memberRepository.getWithRoles(email);

        if(result.isEmpty()){
            //예외 처리. 걍 알려주고 그만? > 어디로 연결해줘야하나...? 레퍼런스 찾아보기
            throw new UsernameNotFoundException("해당하는 유저가 존재하지 않습니다.");
        }

        // 디비에 해당 유저가 있다면, 로그인 처리하기.
        Member member = result.get();

        // entity -> dto
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
                        false,
                        member.isDel(),
                        null //DB에서 조회하고 인증하기 때문에 소셜 로그인 정보가 필요없음.
                );
        log.info("CustomUserDetailsService loadUserByUsername securityMemberDTO 확인 :" + securityMemberDTO);

        return securityMemberDTO;
    }
}
