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

    // 페이지와 사이즈 정보만 유지.
    private String link;

    // 페이징 정보 반환하기.
    public Pageable getPageable(String... props) {
        return PageRequest.of(this.page - 1, 10, Sort.by(props).descending());
    }

    // 페이징 정보와, 검색 정보를 , URL에서, 쿼리스트링으로
    // 파라미터를 한번에 작성 하기위한 멤버.
    public String getLink() {
        if (link == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("page=" + this.page);
            stringBuilder.append("&size=" + this.size);

            link = stringBuilder.toString();
        }
        return link;
    }

}


