package com.bitc502.grapemarket.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int id; // 시퀀스
    private String username; // 사용자 아이디
    private String password; // 암호화된 패스워드
    private String name; // 사용자 이름
    private String email; //EMAIL
    private String phone; //핸드폰 번호
    private String userProfile;
    private String address;  //주소1
    private Double addressX;
    private Double addressY;
    private Integer addressAuth; //주소1 인증

    private List<Board> board;

    // 댓글
    private List<Comment> comment;

    private List<Likes> like; // 좋아요

    private Timestamp createDate;
    private Timestamp updateDate;
}
