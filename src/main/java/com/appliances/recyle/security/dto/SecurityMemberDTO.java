package com.appliances.recyle.security.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class SecurityMemberDTO extends User implements OAuth2User {

    private String email;
    private String mname;
    private String pw;
    private String address;
    private String phone;

    private boolean social;
    private boolean del;

    //소셜 로그인 정보
    private Map<String, Object> props;

    public SecurityMemberDTO(String email, String password, Collection<? extends GrantedAuthority> authorities,
                             String mname, String address, String phone, boolean social, boolean del,
                             Map<String, Object> props) {
        super(email, password, authorities);
        this.email = email;
        this.pw = password;
        this.mname = mname;
        this.address = address;
        this.phone = phone;
        this.social = social;
        this.del = del;
        this.props = props;

    }



    // 카카오 인증 시, 필수 재정의 매소드
    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    @Override
    public String getName() {
        return this.email;
    }
}
