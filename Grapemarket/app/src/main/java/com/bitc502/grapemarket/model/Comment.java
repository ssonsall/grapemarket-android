package com.bitc502.grapemarket.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private int id; // 시퀀스
    private String content; //내용

    // 쓴사람
//	private User user; //id, username

    private User user;

    //	private Board board; //id

    private Board board; //id


    private Timestamp createDate;

    private Timestamp updateDate;
}
