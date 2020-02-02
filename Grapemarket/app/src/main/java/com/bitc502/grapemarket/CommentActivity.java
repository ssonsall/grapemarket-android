package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.model.BoardForDetail;
import com.bitc502.grapemarket.recycler.CommentAdapter;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView recyclerViewComment;
    private LinearLayoutManager linearLayoutManagerComment;
    private CommentAdapter commentAdapter;
    private Context commentContext;
    private int boardId;
    private Button btnWriteCommentComplete;
    private EditText commentInput;
    private ConstraintLayout progressBarLayout;
    private View systemSoftKey;
    final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            |View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            |View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        commentContext = getApplicationContext();
        progressBarLayout = findViewById(R.id.progressBarLayout);
        recyclerViewComment = findViewById(R.id.comment_recycler);
        boardId = getIntent().getExtras().getInt("boardId");
        btnWriteCommentComplete = findViewById(R.id.btnWriteCommentComplete);
        commentInput = findViewById(R.id.comment_input);
        linearLayoutManagerComment = new LinearLayoutManager(commentContext);
        linearLayoutManagerComment.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewComment.setLayoutManager(linearLayoutManagerComment);
        commentAdapter = new CommentAdapter();
        setCommentData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        systemSoftKey = getWindow().getDecorView();
        systemSoftKey.setSystemUiVisibility(uiOptions);
    }

    public void btnWriteCommentCompleteClicked(View v){
        new AsyncTask<Void, Boolean,Boolean>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                return Connect2Server.commentWrite(commentInput.getText().toString()+"",boardId+"");
            }

            @Override
            protected void onProgressUpdate(Boolean... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Boolean commentResult) {
                super.onPostExecute(commentResult);
                if(commentResult){
                    commentInput.setText("");
                    setCommentData();
                }else{
                    Toast.makeText(commentContext,"댓글 쓰기 실패",Toast.LENGTH_LONG).show();
                }
            }

        }.execute();
    }

    public void setCommentData() {
        new AsyncTask<Void, BoardForDetail, BoardForDetail>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarLayout.setVisibility(View.VISIBLE);
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
                //comment recycler view
                commentAdapter.setCommentForDetails(boardForDetail.getComment());
                recyclerViewComment.setAdapter(commentAdapter);
                progressBarLayout.setVisibility(View.GONE);
            }
        }.execute();
    }
}
