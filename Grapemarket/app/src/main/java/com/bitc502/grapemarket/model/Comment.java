package com.bitc502.grapemarket.model;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Comment implements Serializable {

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
