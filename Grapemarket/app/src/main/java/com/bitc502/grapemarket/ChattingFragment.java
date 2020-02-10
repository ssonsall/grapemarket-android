package com.bitc502.grapemarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.model.ChatList;
import com.bitc502.grapemarket.recycler.ChattingBuyListAdapter;
import com.bitc502.grapemarket.recycler.ChattingSellListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChattingFragment extends Fragment {

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    //private FragmentManager fragmentManager = getChildFragmentManager();
    private BottomNavigationView chattingListNavgationView;
    private Fragment chattingListBuyFragment = new ChattingListBuyFragment();
    private Fragment chattingListSellFragment = new ChattingListSellFragment();
    private TextView rangeSet;
    private ChatList chatListGlobal;
    private Boolean isLoaded;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_chatting, container, false);
        chattingListNavgationView = v.findViewById(R.id.chatting_list_navgationView);
        rangeSet = getActivity().findViewById(R.id.toolbar_range_set);
        rangeSet.setVisibility(View.INVISIBLE);
        chatListGlobal = new ChatList();
        isLoaded = false;

        // 첫 화면 지정
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.chattinglist_mainframe, chattingListBuyFragment).commitAllowingStateLoss();

        // tradeLogNavgationView의 아이템이 선택될 때 호출될 리스너 등록
        chattingListNavgationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_menu1: {
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.replace(R.id.chattinglist_mainframe, chattingListBuyFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu2: {
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.replace(R.id.chattinglist_mainframe, chattingListSellFragment).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });

        return v;
    }

    public void setChatListGlobal(ChatList chatList){
        chatListGlobal.getChatForBuy().clear();
        chatListGlobal.getChatForSell().clear();
        isLoaded = true;
        this.chatListGlobal = chatList;
    }

    public ChatList getChatListGlobal(){
        return this.chatListGlobal;
    }

    public Boolean getIsLoaded(){
        return this.isLoaded;
    }

}
