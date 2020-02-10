package com.bitc502.grapemarket.model;

import android.graphics.Bitmap;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TradeStateForBuyerList {
    private int id; // 시퀀스
    private User user;
    //	|0:판매취소|1:판매중|2:판매 완료|10:구매취소|11:구매중|12:구매 완료|
    private String State;
    // id, title
    private Bitmap buyerListUserProfile;
    private Board board;
    private Timestamp createDate;
    private Timestamp updateDate;
}
