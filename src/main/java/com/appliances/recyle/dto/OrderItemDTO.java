package com.appliances.recyle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private String iname; //상품명
    private String purl; //상품 이미지 경로
    private String oaddress; //주소
    private int amount;
    private String odate; //수거 예정일

//    public OrderItemDTO(String iname, String purl, String oaddress, int amount, String odate) {
//        this.iname = iname;
//        this.purl = purl;
//        this.oaddress = oaddress;
//        this.amount = amount;
//        this.odate = odate;
//    }
}
