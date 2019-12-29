package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.model.Board;
import com.bitc502.grapemarket.model.BoardForList;
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
