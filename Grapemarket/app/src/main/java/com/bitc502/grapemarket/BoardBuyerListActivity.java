package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.model.TradeStateForBuyerList;
import com.bitc502.grapemarket.recycler.BoardBuyerListAdapter;
import com.bitc502.grapemarket.recycler.BoardListAdapter;

import java.util.ArrayList;
import java.util.List;

public class BoardBuyerListActivity extends AppCompatActivity {

    private Context boardBuyerListContext;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView boardBuyerListRecyclerView;
    private BoardBuyerListAdapter boardBuyerListAdapter;
    private Integer boardId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_buyer_list);
        boardBuyerListContext = getApplicationContext();
        boardBuyerListRecyclerView = findViewById(R.id.board_buyer_list_recycler);
        boardId = getIntent().getExtras().getInt("boardId");

        setBuyerList();
    }

    public void btnToolbarBack(View v) {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBuyerList();
    }

    public void setBuyerList() {
        new AsyncTask<Void, Boolean, List<TradeStateForBuyerList>>() {
            CustomAnimationDialog podoLoading = new CustomAnimationDialog(BoardBuyerListActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                podoLoading.show();
            }

            @Override
            protected List<TradeStateForBuyerList> doInBackground(Void... voids) {
                return Connect2Server.getBuyerList(boardId);
            }

            @Override
            protected void onProgressUpdate(Boolean... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(List<TradeStateForBuyerList> result) {
                super.onPostExecute(result);
                if (result == null) {
                    result = new ArrayList<>();
                }
                linearLayoutManager = new LinearLayoutManager(boardBuyerListContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                boardBuyerListRecyclerView.setLayoutManager(linearLayoutManager);

                boardBuyerListAdapter = new BoardBuyerListAdapter(BoardBuyerListActivity.this, boardId);
                boardBuyerListAdapter.setTradeStateList(result);

                boardBuyerListRecyclerView.setAdapter(boardBuyerListAdapter);
                podoLoading.dismiss();
            }
        }.execute();
    }
}
