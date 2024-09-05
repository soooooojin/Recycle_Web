package com.appliances.recyle.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    @NotEmpty
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    private String mname;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
//    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String pw;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
//    @Pattern(regexp = "^(010|011|016|017|018|019)\\d{3,4}\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. (예: 01012345678)")
    private String phone;

//    private String roll;
}
