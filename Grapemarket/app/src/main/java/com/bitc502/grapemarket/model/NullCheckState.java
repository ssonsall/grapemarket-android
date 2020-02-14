package com.bitc502.grapemarket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NullCheckState {
    private Boolean isValidate;
    private String message;
}
