package com.appliances.recyle.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "persistent_logins")
@Getter
@Setter
public class PersistentLogin {

    @Id
    private String series;  // 토큰을 구분하는 고유 키

    private String username;  // 사용자의 username

    private String token;  // remember-me 토큰

    private Date lastUsed;  // 마지막 사용 날짜
}