package com.appliances.recyle.service;


import com.appliances.recyle.dto.MemberDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    public void findemailtest() {
        boolean value  = memberService.checkid("ngy2@gmail.com");
        log.info("해당하는 아이디 찾기 :"+ value);
    }

//    @Test
//    public void test2() {
//
//    }
}
