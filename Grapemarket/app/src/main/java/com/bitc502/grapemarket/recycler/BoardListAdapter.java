package com.bitc502.grapemarket.recycler;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.model.Board;
import com.bitc502.grapemarket.model.BoardForList;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class BoardListAdapter extends RecyclerView.Adapter<BoardDataListHolder>{

    private List<BoardForList> boardList;

    @NonNull
    @Override
    public BoardDataListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recycler_items, parent, false);
        ImageView iv = view.findViewById(R.id.list_image);
        Drawable drawable = parent.getContext().getDrawable(R.drawable.round_imageview);
        iv.setBackground(drawable);
        iv.setClipToOutline(true);
        BoardDataListHolder holder = new BoardDataListHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull BoardDataListHolder holder, int position) {
        BoardForList data = boardList.get(position);
        holder.getImage().setImageBitmap(data.getImage1());
        holder.getTitle().setText(data.getTitle());
        holder.getLocation().setText(data.getUser().getAddress());
        holder.getUsername().setText(data.getUser().getName());
        holder.getPrice().setText(new DecimalFormat("###,###").format(Integer.parseInt(data.getPrice()))+"원");
        holder.getCntComment().setText(" "+new DecimalFormat("###,###").format(data.getComment().size()));
        holder.getCntLike().setText(" "+new DecimalFormat("###,###").format(data.getLike().size()));
        holder.setId(data.getId());
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }
}
