package com.bitc502.grapemarket.recycler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.ChattingFragment;
import com.bitc502.grapemarket.ChattingRoomActivity;
import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.dialog.CustomOutChattingDialog;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ChattingSellListHolder extends RecyclerView.ViewHolder {
    private int chattingSellId;
    private TextView chattingInfo;
    private ImageView chattingBoardImage;
    private String chattingTitle;
    private String chattingImageUrl;
    private Context context;
    private Fragment parentFragment;
    public ChattingSellListHolder(@NonNull View itemView, Context context, Fragment parentFragment) {
        super(itemView);
        this.context = context;
        chattingInfo = itemView.findViewById(R.id.chatting_list_tv);
        chattingBoardImage = itemView.findViewById(R.id.chattingListImage);
        this.parentFragment = parentFragment;

        chattingInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String message = "채팅방을 나가시겠습니까?";
                CustomOutChattingDialog confirmDialog = new CustomOutChattingDialog(context, chattingSellId, "seller", message, new CustomOutChattingDialog.CustomOutChattingDialogListener() {
                    @Override
                    public void clickConfirm() {
                        FragmentManager fm = parentFragment.getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ChattingFragment chattingFragment = new ChattingFragment();
                        ft.replace(R.id.main_frame, chattingFragment);
                        ft.commitAllowingStateLoss();
                    }

                    @Override
                    public void clickCancel() {
                    }
                });
                confirmDialog.show();
                return true;
            }
        });


        chattingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(v.getContext(), ChattingRoomActivity.class);
                    intent.putExtra("chattingRoomId", chattingSellId + "");
                    intent.putExtra("chattingTitle", chattingTitle);
                    intent.putExtra("chattingImageUrl", chattingImageUrl);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }
}
