package com.appliances.recyle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequestDTO {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

}
