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

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.model.ChatList;
import com.bitc502.grapemarket.recycler.BoardListAdapter;
import com.bitc502.grapemarket.recycler.ChattingBuyListAdapter;
import com.bitc502.grapemarket.recycler.ChattingSellListAdapter;

public class ChattingFragment extends Fragment {

    private Context chattingListContext;
    private RecyclerView chattingListBuy,chattingListSell;
    private ChattingBuyListAdapter chattingListBuyAdapter;
    private ChattingSellListAdapter chattingListSellAdapter;
    private LinearLayoutManager linearLayoutManagerBuy,linearLayoutManagerSell;
    private ConstraintLayout progressBarLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_chatting, container, false);
        chattingListContext = getContext();
        chattingListBuy = v.findViewById(R.id.chatting_buy_list);
        chattingListSell = v.findViewById(R.id.chatting_sell_list);
        progressBarLayout = v.findViewById(R.id.progressBarLayout);
        setChattingList();
        return v;
    }


    public void setChattingList(){
        new AsyncTask<Void,ChatList, ChatList>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarLayout.setVisibility(View.VISIBLE);
            }

            @Override
            protected ChatList doInBackground(Void... voids) {
                return Connect2Server.getChatList();
            }

            @Override
            protected void onProgressUpdate(ChatList... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(ChatList chatList) {
                super.onPostExecute(chatList);
                try {
                    //구매 채팅
                    linearLayoutManagerBuy = new LinearLayoutManager(chattingListContext);
                    linearLayoutManagerBuy.setOrientation(LinearLayoutManager.VERTICAL);

                    chattingListBuy.setLayoutManager(linearLayoutManagerBuy);

                    chattingListBuyAdapter = new ChattingBuyListAdapter(getContext());
                    chattingListBuyAdapter.setChatList(chatList);

                    chattingListBuy.setAdapter(chattingListBuyAdapter);
                    progressBarLayout.setVisibility(View.GONE);

                    //판매 채팅
                    linearLayoutManagerSell = new LinearLayoutManager(chattingListContext);
                    linearLayoutManagerSell.setOrientation(LinearLayoutManager.VERTICAL);

                    chattingListSell.setLayoutManager(linearLayoutManagerSell);

                    chattingListSellAdapter = new ChattingSellListAdapter(getContext());
                    chattingListSellAdapter.setChatList(chatList);

                    chattingListSell.setAdapter(chattingListSellAdapter);
                    progressBarLayout.setVisibility(View.GONE);
                }catch (Exception e){
                    Log.d("mychatlist", e.toString());
                }
            }
        }.execute();
    }
}
