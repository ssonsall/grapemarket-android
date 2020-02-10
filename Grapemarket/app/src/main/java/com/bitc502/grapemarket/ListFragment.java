package com.bitc502.grapemarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.dialog.CustomRangeSetDialog;
import com.bitc502.grapemarket.model.BoardForList;
import com.bitc502.grapemarket.recycler.BoardListAdapter;
import com.bitc502.grapemarket.singleton.CurrentRangeForListFragment;
import com.bitc502.grapemarket.singleton.Session;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private RecyclerView boardListRecylerView;
    private BoardListAdapter boardListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Context listContext;
    private Integer pageNumber,range;
    private List<BoardForList> boardForLists;
    private TextView btnSetRange;
    private SwipeRefreshLayout listSwipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("crazy", "ListFragment !!!!");
        View v = inflater.inflate(R.layout.activity_list, container, false);

        listContext = getContext();
        pageNumber = 0;

        range = CurrentRangeForListFragment.getInstance().getCurrentRangeInteger();
        boardForLists = new ArrayList<>();

        //Recycler View 가져오기
        listSwipeRefresh = v.findViewById(R.id.list_refresh);
        boardListRecylerView = v.findViewById(R.id.board_list);

        //Fragment에서 Toolbar는 MotherActivity가 들고있기때문에 아래와 같이 찾아와야 한다.
        btnSetRange = getActivity().findViewById(R.id.toolbar_range_set);
        btnSetRange.setText(CurrentRangeForListFragment.getInstance().getCurrentRange());

        btnSetRange.setVisibility(View.VISIBLE);
        setListFragmentData();

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

        //스와이프 리프레시(당겨서 새로고침)
        listSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setListFragmentData();
                listSwipeRefresh.setRefreshing(false);
            }
        });
        return v;
    }

    public void btnRangeSetClicked(View v) {
        //반경 seekbar 다이얼로그 띄워주기
        //반경설정 다이얼로그
        CustomRangeSetDialog rangeSetDialog = new CustomRangeSetDialog(listContext, CurrentRangeForListFragment.getInstance().getCurrentRange());
        rangeSetDialog.show();
        rangeSetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //반경을 바꾸고 설정을 눌렀을때만 새로운 데이터를 받아와서 리로딩하도록
                //근데 반경 안바꿨지만 설정을 누른다면 새로 데이터 받아와서 리로딩이 된다.
                if(CurrentRangeForListFragment.getInstance().getIsSaveAction()) {
                    btnSetRange.setText(CurrentRangeForListFragment.getInstance().getCurrentRange());
                    range = CurrentRangeForListFragment.getInstance().getCurrentRangeInteger();
                    setListFragmentData();
                }
            }
        });
    }


    public void loadingExtraData() {
        new AsyncTask<Void, List<BoardForList>, List<BoardForList>>() {
            CustomAnimationDialog podoLoading = new CustomAnimationDialog(listContext);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                podoLoading.show();
            }

            @Override
            protected List<BoardForList> doInBackground(Void... voids) {
                return Connect2Server.requestAllBoard(pageNumber,range);
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

    public void setListFragmentData() {
        new AsyncTask<Void, List<BoardForList>, List<BoardForList>>() {
            CustomAnimationDialog podoLoading = new CustomAnimationDialog(listContext);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d("crazy", "setListFragment() 호출");
                pageNumber = 0;
                podoLoading.show();
            }

            @Override
            protected List<BoardForList> doInBackground(Void... voids) {
                return Connect2Server.requestAllBoard(pageNumber,range);
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
                linearLayoutManager = new LinearLayoutManager(listContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                boardListRecylerView.setLayoutManager(linearLayoutManager);

                boardListAdapter = new BoardListAdapter(listContext);
                boardListAdapter.setBoardList(boardForLists);

                boardListRecylerView.setAdapter(boardListAdapter);
                podoLoading.dismiss();
            }
        }.execute();
    }
}
