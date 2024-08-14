package com.appliances.recyle.dto;

import com.appliances.recyle.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {

    @NotEmpty
    private Long nno;

    @NotEmpty
    private String email;

    @NotEmpty
    private String ntitle; //공지 제목

    private Long ncomment; //공지 내용
}
