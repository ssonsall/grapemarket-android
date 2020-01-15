package com.bitc502.grapemarket.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.model.Chat;
import com.bitc502.grapemarket.model.ChatList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)

public class ChattingSellListAdapter extends RecyclerView.Adapter<ChattingSellListHolder> {
    private ChatList chatList;

    @NonNull
    @Override
    public ChattingSellListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatting_list_items, parent, false);

        ChattingSellListHolder holder = new ChattingSellListHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingSellListHolder holder, int position) {
        Chat chat = chatList.getChatForSell().get(position);
        holder.getChattingInfo().setText(chat.getBuyerId().getName() +"님과의 채팅입니다.");
    }

    @Override
    public int getItemCount() {
        return chatList.getChatForSell().size();
    }
}
