package com.appliances.recyle.service;

import com.appliances.recyle.domain.Member;
import com.appliances.recyle.domain.MemberRole;
import com.appliances.recyle.dto.MemberDTO;
import com.appliances.recyle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


//    @Override
//    public MemberDTO getUserById(String email) {
//        Member member = memberRepository.findById(email).orElse(null);
//        if (member != null) {
//            return new MemberDTO(member.getEmail(), member.getMname(), member.getPhone(), member.getAddress());
//        }
//        return null;
//    }

    @Override
    public boolean checkid(String email) {
        Optional<Member> result = memberRepository.findByEmail(email);
        return result.isPresent(); //result 존재 여부에 따라 true, false 보냄
    }

    // 회원 가입
    @Override
    public void join(MemberDTO memberDTO) throws IdExistException {
        // 기존 아이디와 중복 확인
        String email = memberDTO.getEmail();
        Optional<Member> result = memberRepository.findByEmail(email);

        if (result.isPresent()) {
            throw new IdExistException();
        }

        // 중복 확인 후 회원 가입
        Member member = dtoToEntity(memberDTO);
        // 패스워드는 현재 평문 -> 암호로 변경.
        member.changePassword(passwordEncoder.encode(member.getPw()));
        // 역할. 기본은 USER
        member.addRole(MemberRole.USER);

        // 데이터 가 잘 알맞게 변경이 됐는지 여부,
        log.info("joinMember: " + member);
        log.info("joinMember (encoded password): " + member.getPw());
        log.info("joinMember: " + member.getRoleSet());

        memberRepository.save(member);
    }

    // 일반 회원 정보 수정.(이름, 비번)
    @Override
    public void update(MemberDTO memberDTO) throws IdExistException {

    }

    @Override
    public Member dtoToEntity(MemberDTO memberDTO) {

        return Member.builder()
                .email(memberDTO.getEmail())
                .mname(memberDTO.getMname())
                .pw(memberDTO.getPw())
                .address(memberDTO.getAddress())
                .phone(memberDTO.getPhone())
                .build();
    }

    @Override
    public MemberDTO entityToDto(Member member) {
        MemberDTO memberDTO = MemberDTO.builder()
                .email(member.getEmail())
                .mname(member.getMname())
                .pw(member.getPw())
                .address(member.getAddress())
                .phone(member.getPhone())
                .build();

        return memberDTO;
    }
}
