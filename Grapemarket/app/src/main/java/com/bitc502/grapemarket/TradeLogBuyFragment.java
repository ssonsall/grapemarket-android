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
import com.bitc502.grapemarket.model.TradeLogList;
import com.bitc502.grapemarket.recycler.ChattingBuyListAdapter;
import com.bitc502.grapemarket.recycler.TradeLogBuyAdapter;


public class TradeLogBuyFragment extends Fragment {

    private Context tradeLogoBuyFragmentContext;
    private LinearLayoutManager linearLayoutManagerBuy;
    private RecyclerView tradeLogoBuyRecycler;
    private TradeLogBuyAdapter tradeLogBuyAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("crazy", "ListFragment !!!!");
        View v = inflater.inflate(R.layout.fragment_trade_log_buy, container, false);
        tradeLogoBuyFragmentContext = getContext();
        tradeLogoBuyRecycler = v.findViewById(R.id.tradeLogRecyclerBuy);

        if(!(((TradeLogActivity)getActivity()).getIsLoaded())) {
            setInitTradeLogoBuy();
        }else{
            setTradeLogoBuy();
        }

        return v;
    }

    public void setInitTradeLogoBuy(){
        new AsyncTask<Void, Boolean, TradeLogList>() {
            CustomAnimationDialog podoLoading = new CustomAnimationDialog(tradeLogoBuyFragmentContext);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                podoLoading.show();
            }

            @Override
            protected TradeLogList doInBackground(Void... voids) {
                return Connect2Server.getTradeLog();
            }

            @Override
            protected void onProgressUpdate(Boolean... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(TradeLogList result) {
                super.onPostExecute(result);

                ((TradeLogActivity)getActivity()).setTradeLogListGlobal(result);

                //구매내역
                linearLayoutManagerBuy = new LinearLayoutManager(tradeLogoBuyFragmentContext);
                linearLayoutManagerBuy.setOrientation(LinearLayoutManager.VERTICAL);

                tradeLogoBuyRecycler.setLayoutManager(linearLayoutManagerBuy);

                tradeLogBuyAdapter = new TradeLogBuyAdapter(tradeLogoBuyFragmentContext);
                tradeLogBuyAdapter.setTradeLogBuyList(result.getTradeLogBuyList());

                tradeLogoBuyRecycler.setAdapter(tradeLogBuyAdapter);

                podoLoading.dismiss();
            }
        }.execute();
    }

    public void setTradeLogoBuy(){
        CustomAnimationDialog podoLoading = new CustomAnimationDialog(tradeLogoBuyFragmentContext);
        podoLoading.show();
        //구매내역
        linearLayoutManagerBuy = new LinearLayoutManager(tradeLogoBuyFragmentContext);
        linearLayoutManagerBuy.setOrientation(LinearLayoutManager.VERTICAL);

        tradeLogoBuyRecycler.setLayoutManager(linearLayoutManagerBuy);

        tradeLogBuyAdapter = new TradeLogBuyAdapter(tradeLogoBuyFragmentContext);
        tradeLogBuyAdapter.setTradeLogBuyList(((TradeLogActivity)getActivity()).getTradeLogListGlobal().getTradeLogBuyList());

        tradeLogoBuyRecycler.setAdapter(tradeLogBuyAdapter);
        podoLoading.dismiss();
    }
}
