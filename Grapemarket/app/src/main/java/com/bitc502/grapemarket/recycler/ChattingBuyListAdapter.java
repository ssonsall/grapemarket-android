package com.bitc502.grapemarket.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.singleton.Session;
import com.bitc502.grapemarket.model.Chat;
import com.bitc502.grapemarket.model.ChatList;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ChattingBuyListAdapter extends RecyclerView.Adapter<ChattingBuyListHolder> {
    private ChatList chatList;
    private Context context;
    private Fragment parentFragment;

    public ChattingBuyListAdapter(Context context, Fragment parentFragment){
        this.context = context;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public ChattingBuyListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatting_list_items, parent, false);

        ChattingBuyListHolder holder = new ChattingBuyListHolder(view, context, parentFragment);
        ImageView iv = view.findViewById(R.id.chattingListImage);
        Drawable drawable = parent.getContext().getDrawable(R.drawable.round_imageview);
        iv.setBackground(drawable);
        iv.setClipToOutline(true);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingBuyListHolder holder, int position) {
        //나중에 채팅리스트에 보드이미지 추가하면서
        //기존 패턴대로 하려니깐
        //모델 만들고 뭐 할게 너무 많아져서
        //이렇게 처리함.
        Chat chat = chatList.getChatForBuy().get(position);
        new AsyncTask<Void,Bitmap,Bitmap>(){
            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    Log.d("chattinglisttest", chat.getBoard().getImage1());
                    Bitmap boardImage;
                    //OKHTTP3
                    Request requestForImage = new Request.Builder()
                            .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                            .url(Connect2Server.getIpAddress()+"/upload/" + chat.getBoard().getImage1())
                            .get()
                            .build();
                    OkHttpClient clientForImage = Connect2Server.getUnsafeOkHttpClient();
                    Response responseForImage = clientForImage.newCall(requestForImage).execute();
                    InputStream inputStream = responseForImage.body().byteStream();
                    boardImage = BitmapFactory.decodeStream(inputStream);
                    return boardImage;
                }catch (Exception e){
                    Log.d("chattinglisttest", e.toString());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                holder.getChattingInfo().setText(chat.getSellerId().getName() +"님과의 채팅입니다.");
                holder.setChattingBuyId(chat.getId());
                holder.setChattingTitle(chat.getBoard().getTitle());
                holder.setChattingImageUrl(chat.getBoard().getImage1());
                holder.getChattingBoardImage().setImageBitmap(result);
            }
        }.execute();


    }

    @Override
    public int getItemCount() {
        return chatList.getChatForBuy().size();
    }
}
