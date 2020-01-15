package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.model.BoardForList;
import com.bitc502.grapemarket.recycler.BoardListAdapter;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchInput;
    private Spinner searchCategory;
    private String category;
    private Context searchContext;
    private RecyclerView boardListRecylerView;
    private BoardListAdapter boardListAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchContext = getApplicationContext();
        boardListRecylerView = findViewById(R.id.search_list);
        searchCategory = findViewById(R.id.search_category);
        searchInput = findViewById(R.id.searchInput);
        searchCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = (String)adapterView.getItemAtPosition(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void btnProductSearchClicked(View v){
        try {
            new AsyncTask<Void, List<BoardForList>, List<BoardForList>>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
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
                }
            }.execute();
        }catch (Exception e){
            Log.d("searcht", e.toString());
        }
    }
}
