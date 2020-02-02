package com.bitc502.grapemarket.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationSetting implements Serializable {
    private String address;
    private String addressX;
    private String addressY;
    private Integer addressAuth;
}
