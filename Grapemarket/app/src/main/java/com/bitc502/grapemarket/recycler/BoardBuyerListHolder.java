package com.bitc502.grapemarket.recycler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.BoardBuyerListActivity;
import com.bitc502.grapemarket.DetailActivity;
import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.dialog.CustomConfirmDialog;
import com.bitc502.grapemarket.dialog.CustomRangeSetDialog;
import com.bitc502.grapemarket.singleton.CurrentRangeForListFragment;

import de.hdodenhof.circleimageview.CircleImageView;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
public class BoardBuyerListHolder extends RecyclerView.ViewHolder {

    private CircleImageView buyerListUserprofile;
    private TextView buyerListName;
    private ImageView buyerListCheckGreen, buyerListXRed;
    private Context context;
    private Integer boardId, buyerId;

    public BoardBuyerListHolder(@NonNull View itemView, Context context, Integer boardId) {
        super(itemView);
        this.context = context;
        this.boardId = boardId;
        buyerListUserprofile = itemView.findViewById(R.id.board_buyer_list_profile);
        buyerListName = itemView.findViewById(R.id.board_buyer_list_name);
        buyerListCheckGreen = itemView.findViewById(R.id.board_buyer_list_check);
        buyerListXRed = itemView.findViewById(R.id.board_buyer_list_uncheck);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    //누르면 판매완료로 바꿈 (다이얼로그 띄워서 확인하고)
                    String message = buyerListName.getText().toString() + "님을\n구매자로 하시겠습니까?";
                    Log.d("tradeidtest", "holder에서 >> "+buyerId+"");
                    CustomConfirmDialog confirmDialog = new CustomConfirmDialog(context, message, boardId, buyerId, new CustomConfirmDialog.CustomConfirmDialogListener() {
                        @Override
                        public void clickConfirm() {
                            Intent intent = new Intent(context,DetailActivity.class);
                            intent.putExtra("id",boardId);
                            context.startActivity(intent);
                        }

                        @Override
                        public void clickCancel() {

                        }
                    });
                    confirmDialog.show();
                }
            }
        });
    }
}
