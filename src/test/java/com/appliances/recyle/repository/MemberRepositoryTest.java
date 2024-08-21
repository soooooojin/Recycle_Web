package com.appliances.recyle.repository;

import com.appliances.recyle.domain.Member;
import com.appliances.recyle.domain.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest

public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    //private PasswordEncoder passwordEncoder;


    @Test
    public void insertMember() {
        // 샘플로 10명의 더미 디비 넣기. 병렬처리
        IntStream.rangeClosed(1,10).forEach(i ->{
            Member member = Member.builder()
                    .email("ngy"+i+"@gmail.com")
                    .mname("테스터"+i)
                    // 주의사항, 멤버 넣을 때, 패스워드 평문 안됨, 암호화 필수.
                    //.pw(passwordEncoder.encode("1234"))
                    .pw("비번")
                    .address("여기가 주소입니까?")
                    .phone("암튼 전화번호임")
                    .build();

//            // 권한주기. USER, ADMIN
//            member.addRole(MemberRole.USER);
//            // 90번 이상부터는, 동시권한, USER 이면서 ADMIN 주기.
//            if(i >= 90) {
//                member.addRole(MemberRole.ADMIN);
//            }

            // 엔티티 클래스를 저장, 실제 디비 반영이되는 비지니스 모델.
            memberRepository.save(member);

        });
    }

    @Test
    public void readMember() {
        Optional<Member> result = memberRepository.getWithRoles("ngy2@gmail.com");
        Member member = result.orElseThrow();

        log.info("MemberRepositoryTests read, member:  "+member);
        log.info("MemberRepositoryTests read, member.getRoleSet():  "+member.getRoleSet());

        member.getRoleSet().forEach(memberRole -> {
            log.info("MemberRepositoryTests testRead, memberRole:  "+memberRole);
        });
    }

}
