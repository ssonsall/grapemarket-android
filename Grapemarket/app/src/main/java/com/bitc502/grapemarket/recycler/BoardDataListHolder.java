package com.bitc502.grapemarket.recycler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.DetailActivity;

import com.bitc502.grapemarket.R;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

//RecyclerView에서 강제되는 사항
//현재 화면에 보여지는 아이템만 메모리에 띄움
@Setter
@Getter
@EqualsAndHashCode(callSuper=false)
public class BoardDataListHolder extends RecyclerView.ViewHolder {
    private ImageView image;
    private TextView title;
    private TextView location;
    private TextView username;
    private TextView price;
    private TextView cntComment;
    private TextView cntLike;
    private Context context;

    private int id;


    public BoardDataListHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        image = itemView.findViewById(R.id.list_image);
        title = itemView.findViewById(R.id.title);
        location = itemView.findViewById(R.id.location);
        username = itemView.findViewById(R.id.list_username);
        price = itemView.findViewById(R.id.price);
        cntComment = itemView.findViewById(R.id.cntComment);
        cntLike = itemView.findViewById(R.id.cntLike);
        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    //여기서 애초에 보드리스트 받아와서 각 보드의 모든 데이터를 세팅해서 갖고 있다가
                    //터치하면 디테일 넘어갈때 넘겨줄지
                    //아니면 id만 가지고 있다가
                    //detail 넘어가서 id로 보드 다시 셀렉트해서 가지고 올지
                    //선택!!!
                    //난 그냥 다시 재통신해서 디비 다시 셀렉하는 방식으로 하겠다.
                    //왜냐면 그게 덜 귀찮아서.
                    Log.d("idtest", "boardId >>>  "+id+"");
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra("id",id);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }
}

