package com.bitc502.grapemarket.recycler;



import android.view.View;
import android.widget.ImageView;


import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.R;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class BoardDetailImageHolder extends RecyclerView.ViewHolder {
    private ImageView image;

    public BoardDetailImageHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.detail_image);
    }

}
