package com.bitc502.grapemarket.recycler;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.BoardBuyerListActivity;
import com.bitc502.grapemarket.DetailActivity;
import com.bitc502.grapemarket.MotherActivity;
import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.TradeLogActivity;
import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.model.TradeLogBuy;
import com.bitc502.grapemarket.model.TradeLogSell;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TradeLogSellAdapter extends RecyclerView.Adapter<TradeLogSellHolder> {

    private List<TradeLogSell> tradeLogSellList;
    private Context context;
    public TradeLogSellAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public TradeLogSellHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trade_log_recycler_item, parent, false);

        TradeLogSellHolder holder = new TradeLogSellHolder(view,context);

        ImageView iv = view.findViewById(R.id.tradeLogImage);
        Drawable drawable = parent.getContext().getDrawable(R.drawable.round_imageview);
        iv.setBackground(drawable);
        iv.setClipToOutline(true);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TradeLogSellHolder holder, int position) {
        TradeLogSell tradeLogSell = tradeLogSellList.get(position);

        holder.getTradeLogCurrentState().setText("상태 : " + tradeLogSell.getState());
        if(tradeLogSell.getState().equals("판매완료") || tradeLogSell.getState().equals("판매취소")){
            //판매완료 비활성화
            holder.getChangeTradeState().setClickable(false);
            holder.getChangeTradeState().setTextColor(context.getResources().getColor(R.color.colorGray));

            //판매취소 비활성화
            holder.getCancelTrade().setClickable(false);
            holder.getCancelTrade().setTextColor(context.getResources().getColor(R.color.colorGray));
        }

        holder.getTradeLogoBoardImage().setImageBitmap(tradeLogSell.getTradeLogoBoardImage());
        holder.getTradeLogTitle().setText(tradeLogSell.getBoard().getTitle());
        holder.getChangeTradeState().setText("판매완료");
        if(holder.getChangeTradeState().isClickable()) {
            holder.getChangeTradeState().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //판매완료 로직
                        Intent intent = new Intent(context, BoardBuyerListActivity.class);
                        intent.putExtra("boardId", tradeLogSell.getBoard().getId());
                        context.startActivity(intent);
                }
            });
        }

        if(holder.getCancelTrade().isClickable()){
            holder.getCancelTrade().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //판매취소
                    new AsyncTask<Void, Boolean, Boolean>() {
                        CustomAnimationDialog podoLoading = new CustomAnimationDialog(context);
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            podoLoading.show();
                        }

                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            return Connect2Server.tradeCancel(tradeLogSell.getBoard().getId());
                        }

                        @Override
                        protected void onProgressUpdate(Boolean... values) {
                            super.onProgressUpdate(values);
                        }

                        @Override
                        protected void onPostExecute(Boolean result) {
                            super.onPostExecute(result);
                            podoLoading.dismiss();
                            if(result){
                                Intent intent = new Intent(context, TradeLogActivity.class);
                                context.startActivity(intent);
                                ((TradeLogActivity)context).finish();
                            }else{
                                Toast.makeText(context,"판매취소 실패",Toast.LENGTH_LONG).show();
                            }
                        }
                    }.execute();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tradeLogSellList.size();
    }
}
