package com.bitc502.grapemarket.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Chat {

    private int id; // 시퀀스

    //id, content, createDate
//	@OneToMany(mappedBy = "chat")
//	@JsonIgnoreProperties({ "user","chat" })
//	@OnDelete(action = OnDeleteAction.CASCADE)
//	private List<Message> message;

    //누르는 사람
    //private User user; //id, username

    private User buyerId;


    private int buyerState;


    private User sellerId;


    private int sellerState;
    // id, title

    private Board board;


    private Timestamp createDate;

    private Timestamp updateDate;
}
