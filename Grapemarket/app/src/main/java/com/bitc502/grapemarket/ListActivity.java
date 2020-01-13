package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.model.Board;
import com.bitc502.grapemarket.model.BoardForList;
import com.bitc502.grapemarket.model.CurrentUserInfo;
import com.bitc502.grapemarket.recycler.BoardListAdapter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView boardListRecylerView;
    private BoardListAdapter boardListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Context listContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listContext = getApplicationContext();
        //Recycler View 가져오기
        boardListRecylerView = findViewById(R.id.board_list);
        setBoardList();
    }

    //글쓰기
    public void btnBottomWrite(View v){
        Intent intent = new Intent(listContext,WriteActivity.class);
        startActivity(intent);
    }

    public void btnMySettingClicked(View v){
        Intent intent = new Intent(listContext,MySettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //로그인 성공으로 ListActivity 진입한 후
        //백 키로 다시 로그인 화면으로 가는걸 막기 위해
        //걍 아무것도 안적으면 이 액티비티에서 백 키 동작 안함
    }

    public void setBoardList(){
        new AsyncTask<Void,List<BoardForList>,List<BoardForList>>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<BoardForList> doInBackground(Void... voids) {
                return Connect2Server.requestAllBoard();
            }

            @Override
            protected void onProgressUpdate(List<BoardForList>... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(List<BoardForList> boardForList) {
                super.onPostExecute(boardForList);
                linearLayoutManager = new LinearLayoutManager(listContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                boardListRecylerView.setLayoutManager(linearLayoutManager);

                boardListAdapter = new BoardListAdapter();
                boardListAdapter.setBoardList(boardForList);

                boardListRecylerView.setAdapter(boardListAdapter);
            }
        }.execute();
    }

}
