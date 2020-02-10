package com.bitc502.grapemarket;

import android.content.Context;
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

import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.recycler.TradeLogBuyAdapter;
import com.bitc502.grapemarket.recycler.TradeLogSellAdapter;


public class TradeLogSellFragment extends Fragment {

    private LinearLayoutManager linearLayoutManagerSell;
    private Context tradeLogoSellFragmentContext;
    private RecyclerView tradeLogoSellRecycler;
    private TradeLogSellAdapter tradeLogSellAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("crazy", "ListFragment !!!!");
        View v = inflater.inflate(R.layout.fragment_trade_log_sell, container, false);
        tradeLogoSellFragmentContext = getContext();
        tradeLogoSellRecycler = v.findViewById(R.id.tradeLogRecyclerSell);
        setTradeLogSell();
        return v;
    }

    public void setTradeLogSell() {
        CustomAnimationDialog podoLoading = new CustomAnimationDialog(tradeLogoSellFragmentContext);
        podoLoading.show();

        //판매내역
        linearLayoutManagerSell = new LinearLayoutManager(tradeLogoSellFragmentContext);
        linearLayoutManagerSell.setOrientation(LinearLayoutManager.VERTICAL);

        tradeLogoSellRecycler.setLayoutManager(linearLayoutManagerSell);

        tradeLogSellAdapter = new TradeLogSellAdapter(tradeLogoSellFragmentContext);
        tradeLogSellAdapter.setTradeLogSellList(((TradeLogActivity) getActivity()).getTradeLogListGlobal().getTradeLogSellList());

        tradeLogoSellRecycler.setAdapter(tradeLogSellAdapter);
        podoLoading.dismiss();
    }
}

