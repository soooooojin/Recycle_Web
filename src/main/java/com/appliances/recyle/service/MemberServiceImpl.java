package com.appliances.recyle.service;

import com.appliances.recyle.domain.Member;
import com.appliances.recyle.dto.MemberDTO;
import com.appliances.recyle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public boolean checkid(String email) {
        Optional<Member> result = memberRepository.findByEmail(email);
        if(result.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void join(MemberDTO memberDTO) throws MidExistException {

    }

    // 일반 회원 정보 수정.
    @Override
    public void update(MemberDTO memberDTO) throws MidExistException {

    }
}
