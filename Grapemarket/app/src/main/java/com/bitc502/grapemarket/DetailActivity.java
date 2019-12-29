package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.model.Board;
import com.bitc502.grapemarket.model.BoardForDetail;
import com.bitc502.grapemarket.model.BoardForList;
import com.bitc502.grapemarket.recycler.BoardDetailImageAdapter;
import com.bitc502.grapemarket.recycler.BoardListAdapter;

import java.util.ArrayList;
import java.util.List;

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
    private LinearLayoutManager linearLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        boardId = getIntent().getExtras().getInt("id");
        recyclerViewImage = findViewById(R.id.recycler_image);
        userProfile = findViewById(R.id.userProfile);
        username = findViewById(R.id.username);
        location = findViewById(R.id.location);
        title = findViewById(R.id.title);
        category = findViewById(R.id.category);
        content = findViewById(R.id.content);
        detailContext = getApplicationContext();
        recyclerViewImage = findViewById(R.id.recycler_image);

        Log.d("idtest", "Detail >>>> "+getIntent().getExtras().getInt("id")+"");
        setDetailData();
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

                linearLayoutManager = new LinearLayoutManager(detailContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                recyclerViewImage.setLayoutManager(linearLayoutManager);

                boardDetailImageAdapter = new BoardDetailImageAdapter();
                boardDetailImageAdapter.setDetailImages(boardForDetail.getImages());

                recyclerViewImage.setAdapter(boardDetailImageAdapter);
            }

        }.execute();
    }
}
