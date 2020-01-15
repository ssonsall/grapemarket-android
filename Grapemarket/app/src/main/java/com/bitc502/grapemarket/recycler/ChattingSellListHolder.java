package com.bitc502.grapemarket.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.R;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class ChattingSellListHolder extends RecyclerView.ViewHolder {
    private TextView chattingInfo;
    public ChattingSellListHolder(@NonNull View itemView) {
        super(itemView);
        chattingInfo = itemView.findViewById(R.id.chatting_list_tv);
    }
}
