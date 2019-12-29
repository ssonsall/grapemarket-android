package com.bitc502.grapemarket.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.model.Board;
import com.bitc502.grapemarket.model.BoardForList;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardListAdapter extends RecyclerView.Adapter<BoardDataListHolder>{

    private List<BoardForList> boardList;

    @NonNull
    @Override
    public BoardDataListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recycler_items, parent, false);

        BoardDataListHolder holder = new BoardDataListHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull BoardDataListHolder holder, int position) {
        BoardForList data = boardList.get(position);
        //이미지는 시바 하.. 또 따로 처리해야됨.
        holder.getImage().setImageBitmap(data.getImage1());
        holder.getTitle().setText(data.getTitle());
        holder.getLocation().setText(data.getUser().getAddress());
        holder.getUsername().setText(data.getUser().getName());
        holder.getPrice().setText(data.getPrice());
        holder.setId(data.getId());

    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }
}
