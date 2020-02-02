package com.bitc502.grapemarket.model;

import java.io.Serializable;
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
public class ChatList implements Serializable {
    List<Chat> chatForBuy = new ArrayList<>();
    List<Chat> chatForSell = new ArrayList<>();
}
