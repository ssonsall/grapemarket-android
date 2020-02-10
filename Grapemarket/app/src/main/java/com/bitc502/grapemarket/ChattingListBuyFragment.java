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


public class ChattingListBuyFragment extends Fragment {

    private ChattingBuyListAdapter chattingListBuyAdapter;
    private LinearLayoutManager linearLayoutManagerBuy;
    private Context chattingListBuyContext;
    private RecyclerView chattingListBuyRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chatting_list_buy, container, false);
        chattingListBuyContext = getContext();
        chattingListBuyRecycler = v.findViewById(R.id.chatting_buy_list);

        if(!((ChattingFragment)getParentFragment()).getIsLoaded()){
            setInitChattingListBuy();
        }else{
            setChattingListBuy();
        }

        return v;
    }

    public void setChattingListBuy(){
        CustomAnimationDialog podoLoading = new CustomAnimationDialog(chattingListBuyContext);
        //구매 채팅
        podoLoading.show();
        linearLayoutManagerBuy = new LinearLayoutManager(chattingListBuyContext);
        linearLayoutManagerBuy.setOrientation(LinearLayoutManager.VERTICAL);

        chattingListBuyRecycler.setLayoutManager(linearLayoutManagerBuy);

        chattingListBuyAdapter = new ChattingBuyListAdapter(getContext(),((ChattingFragment)getParentFragment()));
        chattingListBuyAdapter.setChatList(((ChattingFragment)getParentFragment()).getChatListGlobal());

        chattingListBuyRecycler.setAdapter(chattingListBuyAdapter);

        podoLoading.dismiss();
    }

    public void setInitChattingListBuy(){
        new AsyncTask<Void, ChatList, ChatList>(){
            CustomAnimationDialog podoLoading = new CustomAnimationDialog(chattingListBuyContext);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                podoLoading.show();
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
                    ((ChattingFragment)getParentFragment()).setChatListGlobal(chatList);

                    //구매 채팅
                    linearLayoutManagerBuy = new LinearLayoutManager(chattingListBuyContext);
                    linearLayoutManagerBuy.setOrientation(LinearLayoutManager.VERTICAL);

                    chattingListBuyRecycler.setLayoutManager(linearLayoutManagerBuy);

                    chattingListBuyAdapter = new ChattingBuyListAdapter(getContext(), ((ChattingFragment)getParentFragment()));
                    chattingListBuyAdapter.setChatList(chatList);

                    chattingListBuyRecycler.setAdapter(chattingListBuyAdapter);

                    podoLoading.dismiss();
                }catch (Exception e){
                    Log.d("mychatlist", e.toString());
                }
            }
        }.execute();
    }

}
