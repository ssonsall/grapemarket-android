package com.bitc502.grapemarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.model.BoardForList;
import com.bitc502.grapemarket.recycler.BoardListAdapter;

import java.util.List;

public class SearchFragment extends Fragment{
    private EditText searchInput;
    private Spinner searchCategory;
    private String category;
    private Context searchContext;
    private RecyclerView boardListRecylerView;
    private BoardListAdapter boardListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ConstraintLayout progressBarLayout;
    private ArrayAdapter spinnerAdpater;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_search, container, false);
        searchContext = getContext();
        progressBarLayout = v.findViewById(R.id.progressBarLayout);
        boardListRecylerView = v.findViewById(R.id.search_list);
        searchCategory = v.findViewById(R.id.search_category);
        searchInput = v.findViewById(R.id.searchInput);
        spinnerAdpater= ArrayAdapter.createFromResource(getContext(),R.array.search_category,R.layout.spinner_dialog_layout);
        spinnerAdpater.setDropDownViewResource(R.layout.spinner_text_setting);
        searchCategory.setAdapter(spinnerAdpater);
        searchCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = (String)adapterView.getItemAtPosition(i);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return v;
    }


    public void btnProductSearchClicked(View v){
        try {
            new AsyncTask<Void, List<BoardForList>, List<BoardForList>>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressBarLayout.setVisibility(View.VISIBLE);
                }

                @Override
                protected List<BoardForList> doInBackground(Void... voids) {
                    return Connect2Server.search(category, searchInput.getText().toString());
                }

                @Override
                protected void onProgressUpdate(List<BoardForList>... values) {
                    super.onProgressUpdate(values);
                }

                @Override
                protected void onPostExecute(List<BoardForList> boardForList) {
                    super.onPostExecute(boardForList);
                    linearLayoutManager = new LinearLayoutManager(searchContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    boardListRecylerView.setLayoutManager(linearLayoutManager);
                    boardListAdapter = new BoardListAdapter();
                    boardListAdapter.setBoardList(boardForList);
                    boardListRecylerView.setAdapter(boardListAdapter);
                    progressBarLayout.setVisibility(View.GONE);
                }
            }.execute();
        }catch (Exception e){
            Log.d("searcht", e.toString());
        }
    }
}
