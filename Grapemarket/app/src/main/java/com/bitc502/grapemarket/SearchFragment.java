package com.bitc502.grapemarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.model.BoardForList;
import com.bitc502.grapemarket.recycler.BoardListAdapter;
import com.bitc502.grapemarket.singleton.Session;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private EditText searchInput;
    private Spinner searchCategory;
    private String category;
    private Context searchContext;
    private RecyclerView boardListRecylerView;
    private BoardListAdapter boardListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayAdapter spinnerAdpater;
    private TextView rangeSet, currentRangeTv,btnGoAddressSetting;
    private SeekBar searchSeekbar;
    private Integer range, pageNumber;
    private List<BoardForList> boardForLists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_search, container, false);
        searchContext = getContext();
        pageNumber = 0;
        boardForLists = new ArrayList<>();
        searchSeekbar = v.findViewById(R.id.search_range_seek);
        boardListRecylerView = v.findViewById(R.id.search_list);
        searchCategory = v.findViewById(R.id.search_category);
        searchInput = v.findViewById(R.id.searchInput);

        currentRangeTv = v.findViewById(R.id.search_current_range);
        rangeSet = getActivity().findViewById(R.id.toolbar_range_set);
        rangeSet.setVisibility(View.INVISIBLE);
        btnGoAddressSetting = getActivity().findViewById(R.id.toolbar_go_address_setting);
        if(TextUtils.isEmpty(Session.currentUserInfo.getUser().getAddress()) ||Session.currentUserInfo.getUser().getAddress().equals("")){
            btnGoAddressSetting.setVisibility(View.VISIBLE);
            rangeSet.setVisibility(View.GONE);
        }else{
            rangeSet.setVisibility(View.VISIBLE);
            btnGoAddressSetting.setVisibility(View.GONE);
        }


        spinnerAdpater = ArrayAdapter.createFromResource(getContext(), R.array.search_category, R.layout.spinner_dialog_layout);
        spinnerAdpater.setDropDownViewResource(R.layout.spinner_text_setting);
        searchCategory.setAdapter(spinnerAdpater);
        searchCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = (String) adapterView.getItemAtPosition(i);
                Log.d("spintest3", category);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Integer currentValue = 5 + (seekBar.getProgress() * 5);
                currentRangeTv.setText("반경 : " + currentValue.toString() + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Integer currentValue = 5 + (seekBar.getProgress() * 5);
                currentRangeTv.setText("반경 : " + currentValue.toString() + "km");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Integer currentValue = 5 + (seekBar.getProgress() * 5);
                currentRangeTv.setText("반경 : " + currentValue.toString() + "km");
            }
        });

        //RecyclerView 끝에 왔을때 추가데이터 로딩
        boardListRecylerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalItemCnt = recyclerView.getAdapter().getItemCount() - 1;

                if (lastItemPosition == totalItemCnt && totalItemCnt > 6) {
                    Log.d("crazy", "loadingExtraData() 호출");
                    pageNumber++;
                    loadingExtraData();
                }
            }
        });
        return v;
    }

    public void btnGoAddressSetting(View v){
        Intent intent = new Intent(searchContext, MyLocationSetting.class);
        startActivity(intent);
    }

    public void search_spinner_arrow_btn_clicked(View v) {
        searchCategory.performClick();
    }


    public void loadingExtraData() {
        new AsyncTask<Void, List<BoardForList>, List<BoardForList>>() {
            CustomAnimationDialog podoLoading = new CustomAnimationDialog(searchContext);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (currentRangeTv.getText().toString().equals("반경 : 5km")) {
                    range = 5;
                } else if (currentRangeTv.getText().toString().equals("반경 : 10km")) {
                    range = 10;
                } else if (currentRangeTv.getText().toString().equals("반경 : 15km")) {
                    range = 15;
                }
                podoLoading.show();
            }

            @Override
            protected List<BoardForList> doInBackground(Void... voids) {
                return Connect2Server.search(category, searchInput.getText().toString(), range, pageNumber);
            }

            @Override
            protected void onProgressUpdate(List<BoardForList>... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(List<BoardForList> result) {
                super.onPostExecute(result);
                int resultSize = result.size();
                for (int i = 0; i < resultSize; i++) {
                    boardForLists.add(result.get(i));
                }
                boardListAdapter.notifyDataSetChanged();
                podoLoading.dismiss();
            }
        }.execute();
    }

    public void btnProductSearchClicked(View v) {
        try {
            new AsyncTask<Void, List<BoardForList>, List<BoardForList>>() {
                CustomAnimationDialog podoLoading = new CustomAnimationDialog(searchContext);

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pageNumber = 0;
                    if (currentRangeTv.getText().toString().equals("반경 : 5km")) {
                        range = 5;
                    } else if (currentRangeTv.getText().toString().equals("반경 : 10km")) {
                        range = 10;
                    } else if (currentRangeTv.getText().toString().equals("반경 : 15km")) {
                        range = 15;
                    }
                    podoLoading.show();
                }

                @Override
                protected List<BoardForList> doInBackground(Void... voids) {
                    return Connect2Server.search(category, searchInput.getText().toString(), range, pageNumber);
                }

                @Override
                protected void onProgressUpdate(List<BoardForList>... values) {
                    super.onProgressUpdate(values);
                }

                @Override
                protected void onPostExecute(List<BoardForList> result) {
                    super.onPostExecute(result);
                    boardForLists.clear();
                    boardForLists = result;
                    linearLayoutManager = new LinearLayoutManager(searchContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    boardListRecylerView.setLayoutManager(linearLayoutManager);
                    boardListAdapter = new BoardListAdapter();
                    boardListAdapter.setBoardList(result);
                    boardListRecylerView.setAdapter(boardListAdapter);
                    podoLoading.dismiss();
                }
            }.execute();
        } catch (Exception e) {
            Log.d("searcht", e.toString());
        }
    }
}
