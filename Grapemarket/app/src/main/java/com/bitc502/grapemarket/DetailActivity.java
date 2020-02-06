package com.bitc502.grapemarket;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.model.Likes;
import com.bitc502.grapemarket.singleton.Session;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.model.BoardForDetail;
import com.bitc502.grapemarket.model.Chat;
import com.bitc502.grapemarket.recycler.BoardDetailImageAdapter;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;


public class DetailActivity extends AppCompatActivity {

    private int boardId;
    private BoardForDetail boardForDetail;
    private RecyclerView recyclerViewImage;
    private CircleImageView userProfile;
    private TextView username;
    private TextView location;
    private TextView title;
    private TextView category;
    private TextView price;
    private TextView content;
    private Context detailContext;
    private BoardDetailImageAdapter boardDetailImageAdapter;
    private LinearLayoutManager linearLayoutManagerImage;
    private LinearLayout comment1, comment2, comment3, comment4, comment5;
    private CircleImageView commentUserprofile1, commentUserprofile2, commentUserprofile3, commentUserprofile4, commentUserprofile5;
    private TextView commentUsername1, commentUsername2, commentUsername3, commentUsername4, commentUsername5;
    private TextView commentLocation1, commentLocation2, commentLocation3, commentLocation4, commentLocation5;
    private TextView commentUpdatedate1, commentUpdatedate2, commentUpdatedate3, commentUpdatedate4, commentUpdatedate5;
    private TextView commentContent1, commentContent2, commentContent3, commentContent4, commentContent5;
    private TextView noComment;
    private TextView comment_size, show_all_comment;
    private LinearLayout sellerMenu, buyerMenu;
    private View commentSpaceView1, commentSpaceView2, commentSpaceView3, commentSpaceView4, commentSpaceView5;
    private ImageView btnEmptyHeart, btnHeart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        boardId = getIntent().getExtras().getInt("id");
        detailContext = getApplicationContext();
        recyclerViewImage = findViewById(R.id.recycler_image);
        userProfile = findViewById(R.id.userProfile);
        username = findViewById(R.id.detail_username);
        location = findViewById(R.id.location);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        category = findViewById(R.id.category);
        content = findViewById(R.id.content);
        sellerMenu = findViewById(R.id.detail_seller_menu);
        buyerMenu = findViewById(R.id.detail_buyer_menu);
        btnEmptyHeart = findViewById(R.id.detail_empty_heart);
        btnHeart = findViewById(R.id.detail_heart);

        noComment = findViewById(R.id.no_comment);
        comment1 = findViewById(R.id.comment_1);
        comment2 = findViewById(R.id.comment_2);
        comment3 = findViewById(R.id.comment_3);
        comment4 = findViewById(R.id.comment_4);
        comment5 = findViewById(R.id.comment_5);

        commentUserprofile1 = findViewById(R.id.comment_userProfile_1);
        commentUserprofile2 = findViewById(R.id.comment_userProfile_2);
        commentUserprofile3 = findViewById(R.id.comment_userProfile_3);
        commentUserprofile4 = findViewById(R.id.comment_userProfile_4);
        commentUserprofile5 = findViewById(R.id.comment_userProfile_5);
        commentUsername1 = findViewById(R.id.comment_username_1);
        commentUsername2 = findViewById(R.id.comment_username_2);
        commentUsername3 = findViewById(R.id.comment_username_3);
        commentUsername4 = findViewById(R.id.comment_username_4);
        commentUsername5 = findViewById(R.id.comment_username_5);

        commentLocation1 = findViewById(R.id.comment_location_1);
        commentLocation2 = findViewById(R.id.comment_location_2);
        commentLocation3 = findViewById(R.id.comment_location_3);
        commentLocation4 = findViewById(R.id.comment_location_4);
        commentLocation5 = findViewById(R.id.comment_location_5);

        commentUpdatedate1 = findViewById(R.id.comment_updateDate_1);
        commentUpdatedate2 = findViewById(R.id.comment_updateDate_2);
        commentUpdatedate3 = findViewById(R.id.comment_updateDate_3);
        commentUpdatedate4 = findViewById(R.id.comment_updateDate_4);
        commentUpdatedate5 = findViewById(R.id.comment_updateDate_5);

        commentContent1 = findViewById(R.id.comment_content_1);
        commentContent2 = findViewById(R.id.comment_content_2);
        commentContent3 = findViewById(R.id.comment_content_3);
        commentContent4 = findViewById(R.id.comment_content_4);
        commentContent5 = findViewById(R.id.comment_content_5);

        commentSpaceView1 = findViewById(R.id.commentSpaceView1);
        commentSpaceView2 = findViewById(R.id.commentSpaceView2);
        commentSpaceView3 = findViewById(R.id.commentSpaceView3);
        commentSpaceView4 = findViewById(R.id.commentSpaceView4);
        commentSpaceView5 = findViewById(R.id.commentSpaceView5);

        comment_size = findViewById(R.id.comment_size);
        show_all_comment = findViewById(R.id.show_all_comment);

        setDetailData();
    }

    public void btnEmptyHeartClicked(View v) {
        //좋아요 설정
        new AsyncTask<Void, Boolean, Likes>() {
            CustomAnimationDialog podoLoading = new CustomAnimationDialog(DetailActivity.this);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                podoLoading.show();
            }

            @Override
            protected Likes doInBackground(Void... voids) {
                return Connect2Server.saveLike(boardId);
            }

            @Override
            protected void onProgressUpdate(Boolean... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Likes result) {
                super.onPostExecute(result);
                if(result != null) {
                    btnEmptyHeart.setVisibility(View.GONE);
                    btnHeart.setVisibility(View.VISIBLE);
                    boardForDetail.getLike().add(result);
                }else{
                    Toast.makeText(detailContext,"좋아요 상태 업데이트 실패",Toast.LENGTH_LONG).show();
                }
                podoLoading.dismiss();
            }
        }.execute();
    }

    public void btnHeartClicked(View v) {
        //좋아요 해제
        for(int i = 0 ; i < boardForDetail.getLike().size(); i++){
            if(boardForDetail.getLike().get(i).getUser().getId() == Session.currentUserInfo.getUser().getId()){
                int index = i;
                new AsyncTask<Void, Boolean, Boolean>() {
                    CustomAnimationDialog podoLoading = new CustomAnimationDialog(DetailActivity.this);
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        podoLoading.show();
                    }

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        return Connect2Server.deleteLike(boardForDetail.getLike().get(index).getId());
                    }

                    @Override
                    protected void onProgressUpdate(Boolean... values) {
                        super.onProgressUpdate(values);
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        super.onPostExecute(result);
                        if(result) {
                            btnHeart.setVisibility(View.GONE);
                            btnEmptyHeart.setVisibility(View.VISIBLE);
                            boardForDetail.getLike().remove(index);
                        }else{
                            Toast.makeText(detailContext,"좋아요 상태 업데이트 실패",Toast.LENGTH_LONG).show();
                        }

                        podoLoading.dismiss();
                    }
                }.execute();
            }
        }
    }

    public void btnToolbarBack(View v) {
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setDetailData();
    }

    //글 수정
    public void btnDetailModifyClicked(View v) {
        Intent intent = new Intent(detailContext, BoardModifyActivity.class);
        intent.putExtra("boardId", boardId);
        startActivity(intent);
    }

    //글 삭제
    public void btnDetailDeleteClicked(View v) {
        new AsyncTask<Void, Boolean, Boolean>() {
            CustomAnimationDialog podoLoading = new CustomAnimationDialog(DetailActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                podoLoading.show();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                return Connect2Server.deleteBoard(boardId);
            }

            @Override
            protected void onProgressUpdate(Boolean... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                podoLoading.dismiss();
                if (aBoolean) {
                    //삭제 성공 , 리스트 프래그먼트로 이동
                    //근데 리스트 프래그먼트가 초기화면이므로 그냥 MotherActivity로 넘어가면 됨.
                    Intent intent = new Intent(detailContext, MotherActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(detailContext, "글 삭제 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    //거래완료
    public void btnDetailCompleteTrade(View v) {

    }

    //채팅으로 거래하기
    public void btnRequestChattingClicked(View v) {
        new AsyncTask<Void, Boolean, Chat>() {
            CustomAnimationDialog podoLoading = new CustomAnimationDialog(DetailActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                podoLoading.show();
            }

            @Override
            protected Chat doInBackground(Void... voids) {
                return Connect2Server.requestChatting(boardId);
            }

            @Override
            protected void onProgressUpdate(Boolean... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Chat chat) {
                super.onPostExecute(chat);
                podoLoading.dismiss();
                if (chat != null) {
                    Toast.makeText(detailContext, "채팅요청 성공", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(detailContext, ChattingRoomActivity.class);
                    intent.putExtra("chattingRoomId", chat.getId() + "");
                    intent.putExtra("chattingTitle", boardForDetail.getTitle());
                    intent.putExtra("chattingImageUrl", boardForDetail.getCurrentImage1());
                    startActivity(intent);
                } else {
                    Toast.makeText(detailContext, "채팅요청 실패", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    //댓글쓰기
    public void btnDetailWriteComment(View v) {
        Intent intent = new Intent(detailContext, CommentActivity.class);
        intent.putExtra("boardId", boardId);
        startActivity(intent);
    }

    //댓글전체보기 = 댓글쓰기와 같은 동작
    public void btnDetailShowAllComment(View v) {
        Intent intent = new Intent(detailContext, CommentActivity.class);
        intent.putExtra("boardId", boardId);
        startActivity(intent);
    }

    public void setDetailData() {
        new AsyncTask<Void, BoardForDetail, BoardForDetail>() {
            CustomAnimationDialog podoLoading = new CustomAnimationDialog(DetailActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                podoLoading.show();
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
            protected void onPostExecute(BoardForDetail result) {
                super.onPostExecute(result);
                boardForDetail = result;
                int likeSize = result.getLike().size();
                for (int i = 0; i < likeSize; i++) {
                    if(result.getLike().get(i).getUser().getId() == Session.currentUserInfo.getUser().getId()){
                        btnEmptyHeart.setVisibility(View.GONE);
                        btnHeart.setVisibility(View.VISIBLE);
                    }else{
                        btnHeart.setVisibility(View.GONE);
                        btnEmptyHeart.setVisibility(View.VISIBLE);
                    }
                }

                username.setText(result.getUser().getName());
                location.setText(result.getUser().getAddress());
                title.setText(result.getTitle());
                price.setText(new DecimalFormat("###,###").format(Integer.parseInt(result.getPrice())) + "원");
                //카테고리 표시랑 업데이트 날짜랑 TextView를 안나누고 아래와 같이 처리함
                //다른데는 다 나눴는데 여기만 이렇게한 이유는
                //나중에 추가하는데 다시 View만들고 세팅하기 귀찮아서. 딴 이유 없음.
                category.setText(getCategoryFullname(result.getCategory()) + " · " + result.getUpdateDate());
                content.setText(result.getContent());
                userProfile.setImageBitmap(result.getUserProfile());


                //CurrentUserInfo currentUserInfo = CurrentUserInfo.getInstance();
                //수정, 삭제, 거래완료 버튼 표시할지 체크
                if (result.getUser().getId() == Session.currentUserInfo.getUser().getId()) {
                    sellerMenu.setVisibility(View.VISIBLE);
                    buyerMenu.setVisibility(View.GONE);
                } else {
                    sellerMenu.setVisibility(View.GONE);
                    buyerMenu.setVisibility(View.VISIBLE);
                }

                //상품 이미지 recycler view
                linearLayoutManagerImage = new LinearLayoutManager(detailContext);
                linearLayoutManagerImage.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerViewImage.setLayoutManager(linearLayoutManagerImage);
                boardDetailImageAdapter = new BoardDetailImageAdapter();
                boardDetailImageAdapter.setDetailImages(result.getImages());
                recyclerViewImage.setAdapter(boardDetailImageAdapter);

                //Detail 페이지에서 댓글 5개 보여주기
                int commentSize = result.getComment().size();
                comment_size.setText("댓글 " + commentSize + "개");
                show_all_comment.setText(commentSize + "개 댓글 전체보기");
                if (commentSize > 4) {
                    noComment.setVisibility(View.GONE);
                    if (commentSize > 5) {
                        show_all_comment.setVisibility(View.VISIBLE);
                    }
                    comment1.setVisibility(View.VISIBLE);
                    commentUserprofile1.setImageBitmap(result.getComment().get(0).getUserProfile());
                    commentUsername1.setText(result.getComment().get(0).getUser().getName());
                    commentLocation1.setText(result.getComment().get(0).getUser().getAddress());
                    commentUpdatedate1.setText(result.getComment().get(0).getUpdateDate().toString());
                    commentContent1.setText(result.getComment().get(0).getContent());

                    comment2.setVisibility(View.VISIBLE);
                    commentUserprofile2.setImageBitmap(result.getComment().get(1).getUserProfile());
                    commentUsername2.setText(result.getComment().get(1).getUser().getName());
                    commentLocation2.setText(result.getComment().get(1).getUser().getAddress());
                    commentUpdatedate2.setText(result.getComment().get(1).getUpdateDate().toString());
                    commentContent2.setText(result.getComment().get(1).getContent());

                    comment3.setVisibility(View.VISIBLE);
                    commentUserprofile3.setImageBitmap(result.getComment().get(2).getUserProfile());
                    commentUsername3.setText(result.getComment().get(2).getUser().getName());
                    commentLocation3.setText(result.getComment().get(2).getUser().getAddress());
                    commentUpdatedate3.setText(result.getComment().get(2).getUpdateDate().toString());
                    commentContent3.setText(result.getComment().get(2).getContent());

                    comment4.setVisibility(View.VISIBLE);
                    commentUserprofile4.setImageBitmap(result.getComment().get(3).getUserProfile());
                    commentUsername4.setText(result.getComment().get(3).getUser().getName());
                    commentLocation4.setText(result.getComment().get(3).getUser().getAddress());
                    commentUpdatedate4.setText(result.getComment().get(3).getUpdateDate().toString());
                    commentContent4.setText(result.getComment().get(3).getContent());

                    comment5.setVisibility(View.VISIBLE);
                    commentUserprofile5.setImageBitmap(result.getComment().get(4).getUserProfile());
                    commentUsername5.setText(result.getComment().get(4).getUser().getName());
                    commentLocation5.setText(result.getComment().get(4).getUser().getAddress());
                    commentUpdatedate5.setText(result.getComment().get(4).getUpdateDate().toString());
                    commentContent5.setText(result.getComment().get(4).getContent());
                } else {
                    noComment.setVisibility(View.GONE);
                    if (commentSize == 1) {
                        comment1.setVisibility(View.VISIBLE);
                        commentUserprofile1.setImageBitmap(result.getComment().get(0).getUserProfile());
                        commentUsername1.setText(result.getComment().get(0).getUser().getName());
                        commentLocation1.setText(result.getComment().get(0).getUser().getAddress());
                        commentUpdatedate1.setText(result.getComment().get(0).getUpdateDate().toString());
                        commentContent1.setText(result.getComment().get(0).getContent());
                        commentSpaceView2.setVisibility(View.GONE);
                        commentSpaceView3.setVisibility(View.GONE);
                        commentSpaceView4.setVisibility(View.GONE);
                        commentSpaceView5.setVisibility(View.GONE);
                    } else if (commentSize == 2) {
                        comment1.setVisibility(View.VISIBLE);
                        commentUserprofile1.setImageBitmap(result.getComment().get(0).getUserProfile());
                        commentUsername1.setText(result.getComment().get(0).getUser().getName());
                        commentLocation1.setText(result.getComment().get(0).getUser().getAddress());
                        commentUpdatedate1.setText(result.getComment().get(0).getUpdateDate().toString());
                        commentContent1.setText(result.getComment().get(0).getContent());

                        comment2.setVisibility(View.VISIBLE);
                        commentUserprofile2.setImageBitmap(result.getComment().get(1).getUserProfile());
                        commentUsername2.setText(result.getComment().get(1).getUser().getName());
                        commentLocation2.setText(result.getComment().get(1).getUser().getAddress());
                        commentUpdatedate2.setText(result.getComment().get(1).getUpdateDate().toString());
                        commentContent2.setText(result.getComment().get(1).getContent());

                        commentSpaceView3.setVisibility(View.GONE);
                        commentSpaceView4.setVisibility(View.GONE);
                        commentSpaceView5.setVisibility(View.GONE);
                    } else if (commentSize == 3) {
                        comment1.setVisibility(View.VISIBLE);
                        commentUserprofile1.setImageBitmap(result.getComment().get(0).getUserProfile());
                        commentUsername1.setText(result.getComment().get(0).getUser().getName());
                        commentLocation1.setText(result.getComment().get(0).getUser().getAddress());
                        commentUpdatedate1.setText(result.getComment().get(0).getUpdateDate().toString());
                        commentContent1.setText(result.getComment().get(0).getContent());

                        comment2.setVisibility(View.VISIBLE);
                        commentUserprofile2.setImageBitmap(result.getComment().get(1).getUserProfile());
                        commentUsername2.setText(result.getComment().get(1).getUser().getName());
                        commentLocation2.setText(result.getComment().get(1).getUser().getAddress());
                        commentUpdatedate2.setText(result.getComment().get(1).getUpdateDate().toString());
                        commentContent2.setText(result.getComment().get(1).getContent());

                        comment3.setVisibility(View.VISIBLE);
                        commentUserprofile3.setImageBitmap(result.getComment().get(2).getUserProfile());
                        commentUsername3.setText(result.getComment().get(2).getUser().getName());
                        commentLocation3.setText(result.getComment().get(2).getUser().getAddress());
                        commentUpdatedate3.setText(result.getComment().get(2).getUpdateDate().toString());
                        commentContent3.setText(result.getComment().get(2).getContent());

                        commentSpaceView4.setVisibility(View.GONE);
                        commentSpaceView5.setVisibility(View.GONE);
                    } else if (commentSize == 4) {
                        comment1.setVisibility(View.VISIBLE);
                        commentUserprofile1.setImageBitmap(result.getComment().get(0).getUserProfile());
                        commentUsername1.setText(result.getComment().get(0).getUser().getName());
                        commentLocation1.setText(result.getComment().get(0).getUser().getAddress());
                        commentUpdatedate1.setText(result.getComment().get(0).getUpdateDate().toString());
                        commentContent1.setText(result.getComment().get(0).getContent());

                        comment2.setVisibility(View.VISIBLE);
                        commentUserprofile2.setImageBitmap(result.getComment().get(1).getUserProfile());
                        commentUsername2.setText(result.getComment().get(1).getUser().getName());
                        commentLocation2.setText(result.getComment().get(1).getUser().getAddress());
                        commentUpdatedate2.setText(result.getComment().get(1).getUpdateDate().toString());
                        commentContent2.setText(result.getComment().get(1).getContent());

                        comment3.setVisibility(View.VISIBLE);
                        commentUserprofile3.setImageBitmap(result.getComment().get(2).getUserProfile());
                        commentUsername3.setText(result.getComment().get(2).getUser().getName());
                        commentLocation3.setText(result.getComment().get(2).getUser().getAddress());
                        commentUpdatedate3.setText(result.getComment().get(2).getUpdateDate().toString());
                        commentContent3.setText(result.getComment().get(2).getContent());

                        comment4.setVisibility(View.VISIBLE);
                        commentUserprofile4.setImageBitmap(result.getComment().get(3).getUserProfile());
                        commentUsername4.setText(result.getComment().get(3).getUser().getName());
                        commentLocation4.setText(result.getComment().get(3).getUser().getAddress());
                        commentUpdatedate4.setText(result.getComment().get(3).getUpdateDate().toString());
                        commentContent4.setText(result.getComment().get(3).getContent());

                        commentSpaceView5.setVisibility(View.GONE);
                    } else if (commentSize == 0) {
                        noComment.setVisibility(View.VISIBLE);
                        comment1.setVisibility(View.GONE);
                        comment2.setVisibility(View.GONE);
                        comment3.setVisibility(View.GONE);
                        comment4.setVisibility(View.GONE);
                        comment5.setVisibility(View.GONE);

                        commentSpaceView1.setVisibility(View.GONE);
                        commentSpaceView2.setVisibility(View.GONE);
                        commentSpaceView3.setVisibility(View.GONE);
                        commentSpaceView4.setVisibility(View.GONE);
                        commentSpaceView5.setVisibility(View.GONE);
                    }
                }
                podoLoading.dismiss();
            }

        }.execute();
    }

    public String getCategoryFullname(String categoryNumber) {
        if (categoryNumber.equals("3")) {
            return "디지털/가전";
        } else if (categoryNumber.equals("4")) {
            return "가구/인테리어";
        } else if (categoryNumber.equals("5")) {
            return "유아동/유아도서";
        } else if (categoryNumber.equals("6")) {
            return "생활/가공식품";
        } else if (categoryNumber.equals("7")) {
            return "여성의류";
        } else if (categoryNumber.equals("8")) {
            return "여성잡화";
        } else if (categoryNumber.equals("9")) {
            return "뷰티/미용";
        } else if (categoryNumber.equals("10")) {
            return "남성패션/잡화";
        } else if (categoryNumber.equals("11")) {
            return "스포츠/레저";
        } else if (categoryNumber.equals("12")) {
            return "게임/취미";
        } else if (categoryNumber.equals("13")) {
            return "도서/티켓/음반";
        } else if (categoryNumber.equals("14")) {
            return "반려동물용품";
        } else if (categoryNumber.equals("15")) {
            return "기타 중고물품";
        } else if (categoryNumber.equals("16")) {
            return "삽니다";
        } else {
            return "알 수 없음";
        }
    }
}
