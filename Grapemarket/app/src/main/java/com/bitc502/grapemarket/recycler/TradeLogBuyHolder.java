package com.bitc502.grapemarket.recycler;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.R;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class TradeLogBuyHolder extends RecyclerView.ViewHolder {

    private ImageView tradeLogoBoardImage;
    private TextView tradeLogTitle, tradeLogCurrentState;
    private Button changeTradeState, cancelTrade;
    private Context context;


    public TradeLogBuyHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        tradeLogoBoardImage = itemView.findViewById(R.id.tradeLogImage);
        tradeLogTitle = itemView.findViewById(R.id.tradeLogTitle);
        changeTradeState = itemView.findViewById(R.id.tradeLogo_changeTradeState);
        cancelTrade = itemView.findViewById(R.id.tradeLogo_changeTradeState_cancel_trade);
        tradeLogCurrentState = itemView.findViewById(R.id.tradelog_currentState);
    }
}
