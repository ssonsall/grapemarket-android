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
import androidx.recyclerview.widget.RecyclerView;

import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.currentuserinfo.Session;
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

public class ChattingSellListAdapter extends RecyclerView.Adapter<ChattingSellListHolder> {
    private ChatList chatList;
    private Context context;
    public ChattingSellListAdapter(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    public ChattingSellListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatting_list_items, parent, false);

        ChattingSellListHolder holder = new ChattingSellListHolder(view,context);
        ImageView iv = view.findViewById(R.id.chattingListImage);
        Drawable drawable = parent.getContext().getDrawable(R.drawable.round_imageview);
        iv.setBackground(drawable);
        iv.setClipToOutline(true);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingSellListHolder holder, int position) {
        Chat chat = chatList.getChatForSell().get(position);
        new AsyncTask<Void, Bitmap,Bitmap>(){
            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    Log.d("chattinglisttest", chat.getBoard().getImage1());
                    Bitmap boardImage;
                    //OKHTTP3
                    Request requestForImage = new Request.Builder()
                            .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                            .url("https://192.168.43.40:8443/upload/" + chat.getBoard().getImage1())
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
                holder.getChattingInfo().setText(chat.getBuyerId().getName() +"님과의 채팅입니다.");
                holder.setChattingSellId(chat.getId());
                holder.setChattingTitle(chat.getBoard().getTitle());
                holder.setChattingImageUrl(chat.getBoard().getImage1());
                holder.getChattingBoardImage().setImageBitmap(result);
            }
        }.execute();

    }

    @Override
    public int getItemCount() {
        return chatList.getChatForSell().size();
    }
}
