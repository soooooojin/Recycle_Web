package com.appliances.recyle.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    @NotEmpty
    private Long ino;

    @NotEmpty
    private String iname;

    @NotEmpty
    private Long price;  //스티커 금액
}
