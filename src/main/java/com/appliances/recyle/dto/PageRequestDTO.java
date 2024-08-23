package com.appliances.recyle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    // 제목, 내용, 작성자
    // 검색 종류 , t, c, w, tc,tw, twc
    private String type;

    private String keyword;

    // 페이징 정보와, 검색 정보를 , URL에서, 쿼리스트링으로
    // 파라미터를 한번에 작성 하기위한 멤버.
    private String link;

    // 페이지와 사이즈 정보만 유지.
    private String link2;

    // 타입 분리작업
    public String[] getTypes() {
        if (type == null || type.isEmpty()) {
            return null;
        }
        //예시 type={"twc"} -> "t", "w", "c" , 분리함.
        return type.split("");
    }

    // 페이징 정보 반환하기.
    // ...props , 가변 인자 -> (String title, String content, ...)
    // 매개변수에 인자 값으로 여러개의 문자열 타입의 요소를 추가 가능.
    public Pageable getPageable(String... props) {
        // 화면에서 1페이지 -> 0
        // 화면에서 2페이지 -> 1
        return PageRequest.of(this.page - 1, 10, Sort.by(props).descending());
    }

    // 페이징 정보와, 검색 정보를 , URL에서, 쿼리스트링으로
    // 파라미터를 한번에 작성 하기위한 멤버.
    public String getLink() {
        if (link == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("page=" + this.page);
            stringBuilder.append("&size=" + this.size);

            // 타입, 검색 조건, t,w,c
            if (type != null && type.length() > 0) {
                stringBuilder.append("&type=" + type);
            }

            // 검색어, keyword
            if (keyword != null) {
                try {
                    stringBuilder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            link = stringBuilder.toString();

        } // if null 닫는 태그
        return link;
    } // getLink 닫는 태그

    public String getLink2() {
        if (link2 == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("page=" + this.page);
            stringBuilder.append("&size=" + this.size);

            link2 = stringBuilder.toString();

        } // if null 닫는 태그
        return link2;
    } // getLink 닫는 태그

}


