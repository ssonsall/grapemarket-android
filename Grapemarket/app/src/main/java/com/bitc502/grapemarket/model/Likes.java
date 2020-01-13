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
public class Likes {

    private int id; // 시퀀스

    //누르는 사람
    //private User user; //id, username

    private User user;

    // id, title

    private Board board;


    private Timestamp createDate;

    private Timestamp updateDate;
}
