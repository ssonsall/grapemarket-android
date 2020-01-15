package com.bitc502.grapemarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.model.BoardForList;
import com.bitc502.grapemarket.recycler.BoardListAdapter;

import java.util.List;

public class ListFragment extends Fragment {
    private RecyclerView boardListRecylerView;
    private BoardListAdapter boardListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Context listContext;
    private ConstraintLayout progressBarLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_list, container, false);
        listContext = getContext();
        //Recycler View 가져오기
        boardListRecylerView = v.findViewById(R.id.board_list);
        progressBarLayout = v.findViewById(R.id.progressBarLayout);
        setBoardList();
        return v;
    }

    public void setBoardList(){
        new AsyncTask<Void, List<BoardForList>,List<BoardForList>>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarLayout.setVisibility(View.VISIBLE);
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
                progressBarLayout.setVisibility(View.GONE);
            }
        }.execute();
    }
}
