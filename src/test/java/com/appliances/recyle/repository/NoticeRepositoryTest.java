package com.appliances.recyle.repository;

import com.appliances.recyle.domain.Member;
import com.appliances.recyle.domain.Notice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class NoticeRepositoryTest {

    private static final Logger log = LogManager.getLogger(NoticeRepositoryTest.class);

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertNotice() {
        // 이미 존재하는 Member를 가져오기 (예: "ngy1@gmail.com"으로 조회)
        String memberId = "ngy1@gmail.com";
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("Member not found")
        );

        IntStream.range(1, 11).forEach(i -> {
            Notice notice = Notice.builder()
                    .ntitle("제목test" + i)
                    .ncomment("내용입력test " + i)
                    .member(member)  // Notice와 Member 연관 관계 설정
                    .build();

            noticeRepository.save(notice);
        });
    }

    @Test
    public void testFindById() {
        Optional<Notice> result = noticeRepository.findById(1L);
        result.ifPresent(notice -> log.info(notice));
    }
}
