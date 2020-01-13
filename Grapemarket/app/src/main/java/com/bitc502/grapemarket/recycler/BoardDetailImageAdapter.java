package com.bitc502.grapemarket.recycler;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.model.BoardForList;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class BoardDetailImageAdapter extends RecyclerView.Adapter<BoardDetailImageHolder> {
    private List<Bitmap> detailImages;

    @NonNull
    @Override
    public BoardDetailImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_image_items, parent, false);

        BoardDetailImageHolder holder = new BoardDetailImageHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull BoardDetailImageHolder holder, int position) {
        Bitmap data = detailImages.get(position);
        holder.getImage().setImageBitmap(data);

        //holder.getImage().setImageBitmap(data.getImage1());
    }

    @Override
    public int getItemCount() {
        return detailImages.size();
    }
}
