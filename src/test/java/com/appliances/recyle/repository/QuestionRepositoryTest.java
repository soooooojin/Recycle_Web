package com.appliances.recyle.repository;

import com.appliances.recyle.domain.Member;
import com.appliances.recyle.domain.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class QuestionRepositoryTest {

    private static final Logger log = LogManager.getLogger(QuestionRepositoryTest.class);

    @Autowired
    private QuestionRepository questionRepository;

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
            Question question = Question.builder()
                    .qtitle("제목test" + i)
                    .qcomment("내용입력test " + i)
                    .member(member)  // Notice와 Member 연관 관계 설정
                    .build();

            questionRepository.save(question);
        });
    }

    @Test
    public void testFindById() {
        Optional<Question> result = questionRepository.findById(1L);
        result.ifPresent(question -> log.info(question));
    }
}
