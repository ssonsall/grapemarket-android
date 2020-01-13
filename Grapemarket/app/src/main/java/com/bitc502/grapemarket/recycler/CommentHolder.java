package com.bitc502.grapemarket.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.R;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class CommentHolder extends RecyclerView.ViewHolder {

    private ImageView commentUserProfile;
    private TextView commentUsername;
    private TextView commentLocation;
    private TextView commentUpdateDate;
    private TextView commentContent;

    public CommentHolder (View itemView){
        super(itemView);
        commentUserProfile = itemView.findViewById(R.id.comment_userProfile);
        commentUsername = itemView.findViewById(R.id.comment_username);
        commentLocation = itemView.findViewById(R.id.comment_location);
        commentUpdateDate = itemView.findViewById(R.id.comment_updateDate);
        commentContent = itemView.findViewById(R.id.comment_content);
    }
}
