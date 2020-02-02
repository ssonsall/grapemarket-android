package com.bitc502.grapemarket.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.sql.Timestamp;
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
public class BoardForDetail implements Serializable {
    private int id; // 시퀀스
    private String title; // 제목
    private String content; // 내용
    private String price; // 가격
    private String addressRange; // 범위
    private String state; // 상품 상태 (판매중, 판매완료)
    private String category; // 상품 카테고리

    // 댓글
    private List<CommentForDetail> comment;

    private List<Likes> like; // 좋아요

//	@OneToMany(mappedBy = "board")
//	@JsonIgnoreProperties({ "user","board" })
//	@OnDelete(action = OnDeleteAction.CASCADE)
//	private List<Chat> chat;

    // id, username, address
    private User user;

    private Bitmap userProfile;

    // 상품 사진 시작
    private Bitmap image1;
    private Bitmap image2;
    private Bitmap image3;
    private Bitmap image4;
    private Bitmap image5;

    // 상품 사진 시작
    private String currentImage1;
    private String currentImage2;
    private String currentImage3;
    private String currentImage4;
    private String currentImage5;

    private List<Bitmap> images = new ArrayList<>();

    // 상품 사진 끝


    private Timestamp createDate;

    private Timestamp updateDate;
}
