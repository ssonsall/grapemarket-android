package com.bitc502.grapemarket.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TradeLogList {
    List<TradeLogBuy> tradeLogBuyList = new ArrayList<>();
    List<TradeLogSell> tradeLogSellList = new ArrayList<>();
}
