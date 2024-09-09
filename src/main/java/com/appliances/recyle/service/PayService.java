package com.appliances.recyle.service;

import com.siot.IamportRestClient.IamportClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PayService {

    private IamportClient api;

    // 포트원
    // 관린자 콘솔 , https://admin.portone.io/
    // 로그인 후, 연동관리 -> 연동정보 -> REST API KEY, REST API SECRET 가져오기,
    public PayService() {
        this.api = new IamportClient("7511002570835853", "Mg6IKyh8OFdxgHyfK9PTQptGKTNwCD7y98lPxk5Y1beI4nLd73e1NwVKXJUj3SjPp1cVr9MKR0FHdnON");
    }

}
