package com.bitc502.grapemarket.recycler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.ChattingRoomActivity;
import com.bitc502.grapemarket.R;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ChattingBuyListHolder extends RecyclerView.ViewHolder {
    private TextView chattingInfo;
    private ImageView chattingBoardImage;
    private int chattingBuyId;
    private String chattingTitle;
    private String chattingImageUrl;
    private Context context;

    public ChattingBuyListHolder(@NonNull View itemView, Context context) {
        super(itemView);
        chattingInfo = itemView.findViewById(R.id.chatting_list_tv);
        chattingBoardImage = itemView.findViewById(R.id.chattingListImage);
        this.context = context;

        chattingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    Intent intent = new Intent(v.getContext(), ChattingRoomActivity.class);
                    intent.putExtra("chattingRoomId", chattingBuyId + "");
                    intent.putExtra("chattingTitle", chattingTitle);
                    intent.putExtra("chattingImageUrl", chattingImageUrl);
                    v.getContext().startActivity(intent);

                    //MotherActivity motherActivity = (MotherActivity) context;
                    // motherActivity.replaceFragment(ChattingRoomFragment.getInstance(chattingBuyId+"",chattingTitle,chattingImageUrl));

                    Log.d("idtest", "chattingBuyId >>>  " + chattingBuyId + "");
                }
            }
        });
    }
}
