package com.bitc502.grapemarket.recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.model.TradeState;
import com.bitc502.grapemarket.model.TradeStateForBuyerList;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BoardBuyerListAdapter extends RecyclerView.Adapter<BoardBuyerListHolder> {

    private Context context;
    private List<TradeStateForBuyerList> tradeStateList;
    private Integer boardId;


    public BoardBuyerListAdapter(Context context, Integer boardId) {
        this.context = context;
        this.boardId = boardId;
    }

    @NonNull
    @Override
    public BoardBuyerListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.board_buyer_list_items, parent, false);
        BoardBuyerListHolder holder = new BoardBuyerListHolder(view, context, boardId);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardBuyerListHolder holder, int position) {
        TradeStateForBuyerList tradeState = tradeStateList.get(position);
        Log.d("tradetest", "리사이클러 아이템에서 >> " + tradeState.getUser().getName());
        holder.setBuyerId(tradeState.getUser().getId());
        holder.getBuyerListName().setText(tradeState.getUser().getName());
        holder.getBuyerListUserprofile().setImageBitmap(tradeState.getBuyerListUserProfile());
        if (tradeState.getState().equals("구매완료")) {
            holder.getBuyerListCheckGreen().setVisibility(View.VISIBLE);
            holder.getBuyerListXRed().setVisibility(View.GONE);
        } else if (tradeState.getState().equals("구매중")) {
            holder.getBuyerListXRed().setVisibility(View.VISIBLE);
            holder.getBuyerListCheckGreen().setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tradeStateList.size();
    }
}
