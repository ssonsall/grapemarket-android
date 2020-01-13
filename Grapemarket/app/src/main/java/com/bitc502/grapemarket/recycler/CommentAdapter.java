package com.bitc502.grapemarket.recycler;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.model.CommentForDetail;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

    private List<CommentForDetail> commentForDetails;

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_comment_items, parent, false);

        CommentHolder holder = new CommentHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        CommentForDetail data = commentForDetails.get(position);
        holder.getCommentContent().setText(data.getContent());
        holder.getCommentLocation().setText(data.getUser().getAddress());
        holder.getCommentUpdateDate().setText(data.getUpdateDate().toString());
        holder.getCommentUsername().setText(data.getUser().getName());
        holder.getCommentUserProfile().setImageBitmap(data.getUserProfile());
    }

    @Override
    public int getItemCount() {
        return commentForDetails.size();
    }
}
