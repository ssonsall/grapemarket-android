package com.bitc502.grapemarket.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class BoardForList implements Serializable {
    private int id; // 시퀀스
    private String title; // 제목
    private String content; // 내용
    private String price; // 가격
    private String addressRange; // 범위
    private String state; // 상품 상태 (판매중, 판매완료)
    private String category; // 상품 카테고리

    // 댓글
    private List<Comment> comment;

    private List<Likes> like; // 좋아요

    // id, username, address
    private User user;

    // 상품 사진 메인사진
    private Bitmap image1;

    private Timestamp createDate;

    private Timestamp updateDate;
}
