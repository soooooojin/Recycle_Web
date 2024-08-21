package com.appliances.recyle.service;

import com.appliances.recyle.dto.MemberDTO;

public interface MemberService {

    // 중복 아이디 예외처리
    static class MidExistException extends Exception {

    }
    // 중복 아이디 검사
    boolean checkid(String email);

    // 회원 가입
    void join(MemberDTO memberDTO) throws MidExistException;
    // 회원 수정 재사용 join
    void update(MemberDTO memberDTO) throws MidExistException;

//    //소셜 로그인시 수정하는 서비스
//    void updateSocial(String mid, String mpw) throws MidExistException;
}
