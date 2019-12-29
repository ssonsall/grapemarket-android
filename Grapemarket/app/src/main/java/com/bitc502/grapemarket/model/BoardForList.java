package com.bitc502.grapemarket.model;

import android.graphics.Bitmap;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardForList {
    private int id; // 시퀀스
    private String title; // 제목
    private String content; // 내용
    private String price; // 가격
    private String addressRange; // 범위
    private String state; // 상품 상태 (판매중, 판매완료)
    private int category; // 상품 카테고리

    // 댓글
    private List<Comment> comment;

    private List<Likes> like; // 좋아요

//	@OneToMany(mappedBy = "board")
//	@JsonIgnoreProperties({ "user","board" })
//	@OnDelete(action = OnDeleteAction.CASCADE)
//	private List<Chat> chat;

    // id, username, address
    private User user;

    // 상품 사진 시작
    private Bitmap image1;

    // 상품 사진 끝


    private Timestamp createDate;

    private Timestamp updateDate;
}
