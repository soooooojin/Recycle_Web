package com.appliances.recyle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {
    private int page;
    private int size;
    private int total;

    //시작, 끝 페이지 번호
    private int start;
    private int end;

    //이전,다음 페이지 존재 여부
    private boolean prev;
    private boolean next;

    // 받아온 데이터의 목록,(페이징이 되고, 검색된 결과물 목록)
    private List<E> dtoList;

    // PageResponseDTO 생성자 정의.
    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO,
                           List<E> dtoList,int total) {
        // 전체 갯수가 없을 경우
        if(total <= 0) {
            return;
        }

        // 파라미터로 받아온 데이터로 교체 작업.
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        // 선택시, 1 ~ 10
        // 화면에 그려지는 페이지수, 1,2,3, ... 7,8,9,10,
        // 선택시, 1 ~ 20
        // 화면에 그려지는 페이지수, 11,12,13, ... 17,18,19,20,
        this.end = (int)(Math.ceil(this.page/10.0)) * 10 ;
        this.start = this.end - 9;

        // 예) total = 75 , size = 10 , last = 8페이지,
        int last = (int)(Math.ceil(total/(double)size));

        // end > last ? last : end
        this.end = end > last ? last : end;

        this.prev = this.start > 1;
        // ) total = 75 , size = 10 , last = 8페이지,
        this.next = total > this.end * this.size;

    }


}






