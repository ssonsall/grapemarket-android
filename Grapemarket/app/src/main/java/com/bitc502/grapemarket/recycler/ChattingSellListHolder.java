package com.bitc502.grapemarket.recycler;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.ChattingRoomFragment;
import com.bitc502.grapemarket.MotherActivity;
import com.bitc502.grapemarket.R;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class ChattingSellListHolder extends RecyclerView.ViewHolder {
    private int chattingSellId;
    private TextView chattingInfo;
    private ImageView chattingBoardImage;
    private String chattingTitle;
    private String chattingImageUrl;
    private Context context;
    public ChattingSellListHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        chattingInfo = itemView.findViewById(R.id.chatting_list_tv);
        chattingBoardImage = itemView.findViewById(R.id.chattingListImage);
        chattingInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    MotherActivity motherActivity = (MotherActivity) context;
                    motherActivity.replaceFragment(ChattingRoomFragment.getInstance(chattingSellId+"",chattingTitle,chattingImageUrl));
                    Log.d("idtest", "chattingBuyId >>>  "+chattingSellId+"");
                }
            }
        });
    }
}
