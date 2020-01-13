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
public class Message {

    private int id; // 시퀀스
    private String message; //메시지 내용

    private String sender; //메시지 내용

    private Chat chat; //id


    private Timestamp createDate;

    private Timestamp updateDate;

    private int temp;
}
