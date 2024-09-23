package com.appliances.recyle.service;

import com.appliances.recyle.domain.Member;
import com.appliances.recyle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService2 {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 이메일로 멤버 정보를 가져오는 메서드
    public Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    // 비밀번호 변경 메서드
    public boolean changeMemberPassword(String email, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }

        Member member = getMemberByEmail(email).orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, member.getPw())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 업데이트
        member.setPw(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        return true;
    }

    // 회원 저장 메서드
    public void save(Member member) {
        memberRepository.save(member);
    }

    // 회원탈퇴 (이메일로 멤버 삭제)
    public void deleteMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
        memberRepository.delete(member);
    }
}
