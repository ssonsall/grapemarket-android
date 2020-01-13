package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.model.BoardForDetail;
import com.bitc502.grapemarket.model.CurrentUserInfo;
import com.bitc502.grapemarket.recycler.BoardDetailImageAdapter;



public class DetailActivity extends AppCompatActivity {

    private int boardId;
    private RecyclerView recyclerViewImage;
    private ImageView userProfile;
    private TextView username;
    private TextView location;
    private TextView title;
    private TextView category;
    private TextView content;
    private Context detailContext;
    private BoardDetailImageAdapter boardDetailImageAdapter;
    private LinearLayoutManager linearLayoutManagerImage;
    private Button btnDetailModify;
    private Button btnDetailDelete;
    private Button btnCompleteTrade;
    private LinearLayout comment1,comment2,comment3,comment4,comment5;
    private ImageView commentUserprofile1,commentUserprofile2,commentUserprofile3,commentUserprofile4,commentUserprofile5;
    private TextView commentUsername1,commentUsername2,commentUsername3,commentUsername4,commentUsername5;
    private TextView commentLocation1,commentLocation2,commentLocation3,commentLocation4,commentLocation5;
    private TextView commentUpdatedate1,commentUpdatedate2,commentUpdatedate3,commentUpdatedate4,commentUpdatedate5;
    private TextView commentContent1,commentContent2,commentContent3,commentContent4,commentContent5;
    private TextView comment_size, show_all_comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        boardId = getIntent().getExtras().getInt("id");
        recyclerViewImage = findViewById(R.id.recycler_image);
        userProfile = findViewById(R.id.userProfile);
        username = findViewById(R.id.detail_username);
        location = findViewById(R.id.location);
        title = findViewById(R.id.title);
        category = findViewById(R.id.category);
        content = findViewById(R.id.content);
        detailContext = getApplicationContext();
        btnDetailModify = findViewById(R.id.btnDetailModify);
        btnDetailDelete = findViewById(R.id.btnDetailDelete);
        btnCompleteTrade = findViewById(R.id.btnCompleteTrade);
        comment1=findViewById(R.id.comment_1);
        comment2=findViewById(R.id.comment_2);
        comment3=findViewById(R.id.comment_3);
        comment4=findViewById(R.id.comment_4);
        comment5=findViewById(R.id.comment_5);
        commentUserprofile1=findViewById(R.id.comment_userProfile_1);
        commentUserprofile2=findViewById(R.id.comment_userProfile_2);
        commentUserprofile3=findViewById(R.id.comment_userProfile_3);
        commentUserprofile4=findViewById(R.id.comment_userProfile_4);
        commentUserprofile5=findViewById(R.id.comment_userProfile_5);
        commentUsername1=findViewById(R.id.comment_username_1);
        commentUsername2=findViewById(R.id.comment_username_2);
        commentUsername3=findViewById(R.id.comment_username_3);
        commentUsername4=findViewById(R.id.comment_username_4);
        commentUsername5=findViewById(R.id.comment_username_5);

        commentLocation1=findViewById(R.id.comment_location_1);
        commentLocation2=findViewById(R.id.comment_location_2);
        commentLocation3=findViewById(R.id.comment_location_3);
        commentLocation4=findViewById(R.id.comment_location_4);
        commentLocation5=findViewById(R.id.comment_location_5);

        commentUpdatedate1=findViewById(R.id.comment_updateDate_1);
        commentUpdatedate2=findViewById(R.id.comment_updateDate_2);
        commentUpdatedate3=findViewById(R.id.comment_updateDate_3);
        commentUpdatedate4=findViewById(R.id.comment_updateDate_4);
        commentUpdatedate5=findViewById(R.id.comment_updateDate_5);

        commentContent1=findViewById(R.id.comment_content_1);
        commentContent2=findViewById(R.id.comment_content_2);
        commentContent3=findViewById(R.id.comment_content_3);
        commentContent4=findViewById(R.id.comment_content_4);
        commentContent5=findViewById(R.id.comment_content_5);

        comment_size = findViewById(R.id.comment_size);
        show_all_comment = findViewById(R.id.show_all_comment);

        setDetailData();
    }

    //댓글쓰기
    public void btnDetailWriteComment(View v){
        Intent intent = new Intent(detailContext, CommentActivity.class);
        intent.putExtra("boardId",boardId);
        startActivity(intent);
    }

    //댓글전체보기 = 댓글쓰기와 같은 동작
    public void btnDetailShowAllComment(View v){
        Intent intent = new Intent(detailContext, CommentActivity.class);
        intent.putExtra("boardId",boardId);
        startActivity(intent);
    }
    public void setDetailData(){
        new AsyncTask<Void, BoardForDetail, BoardForDetail>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected BoardForDetail doInBackground(Void... voids) {
                return Connect2Server.requestDetailBoard(boardId);
            }

            @Override
            protected void onProgressUpdate(BoardForDetail... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(BoardForDetail boardForDetail) {
                super.onPostExecute(boardForDetail);

                username.setText(boardForDetail.getUser().getName());
                location.setText(boardForDetail.getUser().getAddress());
                title.setText(boardForDetail.getTitle());
                category.setText(boardForDetail.getCategory()+"");
                content.setText(boardForDetail.getContent());
                userProfile.setImageBitmap(boardForDetail.getUserProfile());

                CurrentUserInfo currentUserInfo = CurrentUserInfo.getInstance();
                //수정, 삭제, 거래완료 버튼 표시할지 체크
                if(boardForDetail.getUser().getId()==currentUserInfo.getUser().getId()){
                    btnDetailModify.setVisibility(View.VISIBLE);
                    btnDetailDelete.setVisibility(View.VISIBLE);
                    btnCompleteTrade.setVisibility(View.VISIBLE);
                }

                //상품 이미지 recycler view
                linearLayoutManagerImage = new LinearLayoutManager(detailContext);
                linearLayoutManagerImage.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerViewImage.setLayoutManager(linearLayoutManagerImage);
                boardDetailImageAdapter = new BoardDetailImageAdapter();
                boardDetailImageAdapter.setDetailImages(boardForDetail.getImages());
                recyclerViewImage.setAdapter(boardDetailImageAdapter);

                //Detail 페이지에서 댓글 5개 보여주기
                int commentSize = boardForDetail.getComment().size();
                comment_size.setText("댓글 "+commentSize+"개");
                show_all_comment.setText(commentSize+"개 댓글 전체보기");
                if(commentSize > 4){
                    comment1.setVisibility(View.VISIBLE);
                    commentUserprofile1.setImageBitmap(boardForDetail.getComment().get(0).getUserProfile());
                    commentUsername1.setText(boardForDetail.getComment().get(0).getUser().getName());
                    commentLocation1.setText(boardForDetail.getComment().get(0).getUser().getAddress());
                    commentUpdatedate1.setText(boardForDetail.getComment().get(0).getUpdateDate().toString());
                    commentContent1.setText(boardForDetail.getComment().get(0).getContent());

                    comment2.setVisibility(View.VISIBLE);
                    commentUserprofile2.setImageBitmap(boardForDetail.getComment().get(1).getUserProfile());
                    commentUsername2.setText(boardForDetail.getComment().get(1).getUser().getName());
                    commentLocation2.setText(boardForDetail.getComment().get(1).getUser().getAddress());
                    commentUpdatedate2.setText(boardForDetail.getComment().get(1).getUpdateDate().toString());
                    commentContent2.setText(boardForDetail.getComment().get(1).getContent());

                    comment3.setVisibility(View.VISIBLE);
                    commentUserprofile3.setImageBitmap(boardForDetail.getComment().get(2).getUserProfile());
                    commentUsername3.setText(boardForDetail.getComment().get(2).getUser().getName());
                    commentLocation3.setText(boardForDetail.getComment().get(2).getUser().getAddress());
                    commentUpdatedate3.setText(boardForDetail.getComment().get(2).getUpdateDate().toString());
                    commentContent3.setText(boardForDetail.getComment().get(2).getContent());

                    comment4.setVisibility(View.VISIBLE);
                    commentUserprofile4.setImageBitmap(boardForDetail.getComment().get(3).getUserProfile());
                    commentUsername4.setText(boardForDetail.getComment().get(3).getUser().getName());
                    commentLocation4.setText(boardForDetail.getComment().get(3).getUser().getAddress());
                    commentUpdatedate4.setText(boardForDetail.getComment().get(3).getUpdateDate().toString());
                    commentContent4.setText(boardForDetail.getComment().get(3).getContent());

                    comment5.setVisibility(View.VISIBLE);
                    commentUserprofile5.setImageBitmap(boardForDetail.getComment().get(4).getUserProfile());
                    commentUsername5.setText(boardForDetail.getComment().get(4).getUser().getName());
                    commentLocation5.setText(boardForDetail.getComment().get(4).getUser().getAddress());
                    commentUpdatedate5.setText(boardForDetail.getComment().get(4).getUpdateDate().toString());
                    commentContent5.setText(boardForDetail.getComment().get(4).getContent());
                }else{
                    if(commentSize == 1){
                        comment1.setVisibility(View.VISIBLE);
                        commentUserprofile1.setImageBitmap(boardForDetail.getComment().get(0).getUserProfile());
                        commentUsername1.setText(boardForDetail.getComment().get(0).getUser().getName());
                        commentLocation1.setText(boardForDetail.getComment().get(0).getUser().getAddress());
                        commentUpdatedate1.setText(boardForDetail.getComment().get(0).getUpdateDate().toString());
                        commentContent1.setText(boardForDetail.getComment().get(0).getContent());
                    }else if(commentSize == 2){
                        comment1.setVisibility(View.VISIBLE);
                        commentUserprofile1.setImageBitmap(boardForDetail.getComment().get(0).getUserProfile());
                        commentUsername1.setText(boardForDetail.getComment().get(0).getUser().getName());
                        commentLocation1.setText(boardForDetail.getComment().get(0).getUser().getAddress());
                        commentUpdatedate1.setText(boardForDetail.getComment().get(0).getUpdateDate().toString());
                        commentContent1.setText(boardForDetail.getComment().get(0).getContent());

                        comment2.setVisibility(View.VISIBLE);
                        commentUserprofile2.setImageBitmap(boardForDetail.getComment().get(1).getUserProfile());
                        commentUsername2.setText(boardForDetail.getComment().get(1).getUser().getName());
                        commentLocation2.setText(boardForDetail.getComment().get(1).getUser().getAddress());
                        commentUpdatedate2.setText(boardForDetail.getComment().get(1).getUpdateDate().toString());
                        commentContent2.setText(boardForDetail.getComment().get(1).getContent());
                    }else if(commentSize == 3){
                        comment1.setVisibility(View.VISIBLE);
                        commentUserprofile1.setImageBitmap(boardForDetail.getComment().get(0).getUserProfile());
                        commentUsername1.setText(boardForDetail.getComment().get(0).getUser().getName());
                        commentLocation1.setText(boardForDetail.getComment().get(0).getUser().getAddress());
                        commentUpdatedate1.setText(boardForDetail.getComment().get(0).getUpdateDate().toString());
                        commentContent1.setText(boardForDetail.getComment().get(0).getContent());

                        comment2.setVisibility(View.VISIBLE);
                        commentUserprofile2.setImageBitmap(boardForDetail.getComment().get(1).getUserProfile());
                        commentUsername2.setText(boardForDetail.getComment().get(1).getUser().getName());
                        commentLocation2.setText(boardForDetail.getComment().get(1).getUser().getAddress());
                        commentUpdatedate2.setText(boardForDetail.getComment().get(1).getUpdateDate().toString());
                        commentContent2.setText(boardForDetail.getComment().get(1).getContent());

                        comment3.setVisibility(View.VISIBLE);
                        commentUserprofile3.setImageBitmap(boardForDetail.getComment().get(2).getUserProfile());
                        commentUsername3.setText(boardForDetail.getComment().get(2).getUser().getName());
                        commentLocation3.setText(boardForDetail.getComment().get(2).getUser().getAddress());
                        commentUpdatedate3.setText(boardForDetail.getComment().get(2).getUpdateDate().toString());
                        commentContent3.setText(boardForDetail.getComment().get(2).getContent());
                    }else if(commentSize == 4){
                        comment1.setVisibility(View.VISIBLE);
                        commentUserprofile1.setImageBitmap(boardForDetail.getComment().get(0).getUserProfile());
                        commentUsername1.setText(boardForDetail.getComment().get(0).getUser().getName());
                        commentLocation1.setText(boardForDetail.getComment().get(0).getUser().getAddress());
                        commentUpdatedate1.setText(boardForDetail.getComment().get(0).getUpdateDate().toString());
                        commentContent1.setText(boardForDetail.getComment().get(0).getContent());

                        comment2.setVisibility(View.VISIBLE);
                        commentUserprofile2.setImageBitmap(boardForDetail.getComment().get(1).getUserProfile());
                        commentUsername2.setText(boardForDetail.getComment().get(1).getUser().getName());
                        commentLocation2.setText(boardForDetail.getComment().get(1).getUser().getAddress());
                        commentUpdatedate2.setText(boardForDetail.getComment().get(1).getUpdateDate().toString());
                        commentContent2.setText(boardForDetail.getComment().get(1).getContent());

                        comment3.setVisibility(View.VISIBLE);
                        commentUserprofile3.setImageBitmap(boardForDetail.getComment().get(2).getUserProfile());
                        commentUsername3.setText(boardForDetail.getComment().get(2).getUser().getName());
                        commentLocation3.setText(boardForDetail.getComment().get(2).getUser().getAddress());
                        commentUpdatedate3.setText(boardForDetail.getComment().get(2).getUpdateDate().toString());
                        commentContent3.setText(boardForDetail.getComment().get(2).getContent());

                        comment4.setVisibility(View.VISIBLE);
                        commentUserprofile4.setImageBitmap(boardForDetail.getComment().get(3).getUserProfile());
                        commentUsername4.setText(boardForDetail.getComment().get(3).getUser().getName());
                        commentLocation4.setText(boardForDetail.getComment().get(3).getUser().getAddress());
                        commentUpdatedate4.setText(boardForDetail.getComment().get(3).getUpdateDate().toString());
                        commentContent4.setText(boardForDetail.getComment().get(3).getContent());
                    }
                }
            }

        }.execute();
    }
}
