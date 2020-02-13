package com.bitc502.grapemarket.recycler;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.TradeLogActivity;
import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.model.TradeLogBuy;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TradeLogBuyAdapter extends RecyclerView.Adapter<TradeLogBuyHolder> {

    private List<TradeLogBuy> tradeLogBuyList;
    private Context context;

    public TradeLogBuyAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TradeLogBuyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trade_log_recycler_item, parent, false);

        TradeLogBuyHolder holder = new TradeLogBuyHolder(view, context);

        ImageView iv = view.findViewById(R.id.tradeLogImage);
        Drawable drawable = parent.getContext().getDrawable(R.drawable.round_imageview);
        iv.setBackground(drawable);
        iv.setClipToOutline(true);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TradeLogBuyHolder holder, int position) {
        TradeLogBuy tradeLogBuy = tradeLogBuyList.get(position);

        holder.getTradeLogCurrentState().setText("상태 : " + tradeLogBuy.getState());
        if(tradeLogBuy.getState().equals("구매완료") || tradeLogBuy.getState().equals("구매취소")){
            holder.getChangeTradeState().setClickable(false);
            holder.getChangeTradeState().setTextColor(context.getResources().getColor(R.color.colorGray));
        }
        holder.getTradeLogoBoardImage().setImageBitmap(tradeLogBuy.getTradeLogoBoardImage());
        holder.getTradeLogTitle().setText(tradeLogBuy.getBoard().getTitle());
        holder.getCancelTrade().setVisibility(View.INVISIBLE);
        holder.getChangeTradeState().setText("구매완료");
        if(holder.getChangeTradeState().isClickable()) {
            holder.getChangeTradeState().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //구매완료 로직
                    if (holder.getChangeTradeState().getText().toString().equals("구매완료")) {
                        CustomAnimationDialog podoLoading = new CustomAnimationDialog(context);
                        new AsyncTask<Void, Boolean, Boolean>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                podoLoading.show();
                            }

                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                return Connect2Server.buyComplete(tradeLogBuy.getBoard().getId());
                            }

                            @Override
                            protected void onProgressUpdate(Boolean... values) {
                                super.onProgressUpdate(values);
                            }

                            @Override
                            protected void onPostExecute(Boolean aBoolean) {
                                super.onPostExecute(aBoolean);
                                if (aBoolean) {
                                    Toast.makeText(context, "구매완료 업데이트 성공", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context, TradeLogActivity.class);
                                    context.startActivity(intent);
                                    ((TradeLogActivity)context).finish();
                                } else {
                                    Toast.makeText(context, "구매완료 업데이트 실패", Toast.LENGTH_LONG).show();
                                }
                                podoLoading.dismiss();
                            }
                        }.execute();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tradeLogBuyList.size();
    }
}
