package com.bitc502.grapemarket;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.model.ChatList;
import com.bitc502.grapemarket.recycler.ChattingBuyListAdapter;
import com.bitc502.grapemarket.recycler.ChattingSellListAdapter;


public class ChattingListSellFragment extends Fragment {
    private ChattingSellListAdapter chattingListSellAdapter;
    private LinearLayoutManager linearLayoutManagerSell;
    private Context chattingListSellContext;
    private RecyclerView chattingListSellRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chatting_list_sell, container, false);
        chattingListSellContext = getContext();
        chattingListSellRecycler = v.findViewById(R.id.chatting_sell_list);
        setChattingListSell();
        return v;
    }

    public void setChattingListSell() {
        CustomAnimationDialog podoLoading = new CustomAnimationDialog(chattingListSellContext);
        podoLoading.show();
        //판매 채팅
        linearLayoutManagerSell = new LinearLayoutManager(chattingListSellContext);
        linearLayoutManagerSell.setOrientation(LinearLayoutManager.VERTICAL);

        chattingListSellRecycler.setLayoutManager(linearLayoutManagerSell);

        chattingListSellAdapter = new ChattingSellListAdapter(getContext(),((ChattingFragment)getParentFragment()));
        chattingListSellAdapter.setChatList(((ChattingFragment) getParentFragment()).getChatListGlobal());

        chattingListSellRecycler.setAdapter(chattingListSellAdapter);

        podoLoading.dismiss();

    }
}