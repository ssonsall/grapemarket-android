package com.bitc502.grapemarket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationSetting {
    private String address;
    private String addressX;
    private String addressY;
}
