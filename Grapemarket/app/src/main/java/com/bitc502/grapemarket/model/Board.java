package com.bitc502.grapemarket.model;

//RecyclerView 안에 들어갈 데이터

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Board {
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

//	@OneToMany(mappedBy = "board")
//	@JsonIgnoreProperties({ "user","board" })
//	@OnDelete(action = OnDeleteAction.CASCADE)
//	private List<Chat> chat;

    // id, username, address
    private User user;

    // 상품 사진 시작
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;

    // 상품 사진 끝


    private Timestamp createDate;

    private Timestamp updateDate;
}
