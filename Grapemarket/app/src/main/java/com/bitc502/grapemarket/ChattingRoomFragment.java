package com.bitc502.grapemarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.currentuserinfo.Session;

import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChattingRoomFragment extends Fragment {

    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅
    private ImageView chattingRoomImageView;
    private TextView chattingRoomTitle;
    private ConstraintLayout prgressbarLayout;
    private boolean flag;

    public static ChattingRoomFragment getInstance(String chattingRoomId, String boardTitle ,String boardImageUrl) {
        ChattingRoomFragment chattingRoomFragment = new ChattingRoomFragment();
        Bundle args = new Bundle();
        args.putString("chattingRoomId", chattingRoomId);
        args.putString("chattingTitle", boardTitle);
        args.putString("chattingImageUrl",boardImageUrl);
        chattingRoomFragment.setArguments(args);
        return chattingRoomFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_chattingroom, container, false);
        flag = false;
        mWebView = v.findViewById(R.id.chattingRoomWebView);
        prgressbarLayout = v.findViewById(R.id.progressBarLayout);
        prgressbarLayout.setVisibility(View.VISIBLE);
        chattingRoomTitle = v.findViewById(R.id.chattingRoomTitle);
        chattingRoomImageView = v.findViewById(R.id.chattingRoomImageView);
        Drawable drawable = getContext().getDrawable(R.drawable.round_imageview);
        chattingRoomImageView.setBackground(drawable);
        chattingRoomImageView.setClipToOutline(true);
        try {
            String chattingRoomId = getArguments().getString("chattingRoomId");
            String chattingTitle = getArguments().getString("chattingTitle");
            String chattingImageUrl = getArguments().getString("chattingImageUrl");

            new AsyncTask<Void, Bitmap,Bitmap>(){
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    try {
                        Bitmap boardImage;
                        //OKHTTP3
                        Request requestForImage = new Request.Builder()
                                .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                                .url("https://192.168.43.40:8443/upload/" + chattingImageUrl)
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
                    chattingRoomTitle.setText(chattingTitle);
                    chattingRoomImageView.setImageBitmap(result);
                }
            }.execute();

            //chatting webview 시작
            mWebView.setWebViewClient(new MyChattingRoomWebViewClient());
            mWebView.setHorizontalScrollBarEnabled(false);
            mWebSettings = mWebView.getSettings(); //세부 세팅 등록
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
            mWebSettings.setSupportMultipleWindows(true); // 새창 띄우기 허용 여부
            mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
            mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
            mWebSettings.setUseWideViewPort(false); // 화면 사이즈 맞추기 허용 여부
            mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
            mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
            mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
            mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
            mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie("https://192.168.43.40:8443/chat/room/enter/" + chattingRoomId, Session.currentUserInfo.getJSessionId());
            mWebView.loadUrl("https://192.168.43.40:8443/chat/room/enter/" + chattingRoomId);


            //192.168.43.40:8443
            //WebSocket Test
//            StompClient client = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://192.168.43.40:8443/ws-stomp/websocket", null, Connect2Server.getUnsafeOkHttpClient());
//            client.setHeartbeat(10000);
//            client.connect();
//
//            client.lifecycle().subscribe(lifecycleEvent -> {
//                switch (lifecycleEvent.getType()) {
//                    case OPENED:
//                        Log.d("mywebsocket", "Stomp connection opened");
//                        break;
//                    case CLOSED:
//                        Log.d("mywebsocket", "Stomp connection closed");
//                        break;
//                    case ERROR:
//                        Log.d("mywebsocket", "Stomp connection error", lifecycleEvent.getException());
//                        break;
//                }
//            });
//
//
//            client.topic("/sub/chat/room/" + chattingRoomId).subscribe(message -> {
//                Log.d("mywebsocket", "Received message: " + message.getPayload());
//            });
//
//            client.send("/pub/chat/message", "{'temp':'2', 'sender':'s', 'message':'HelloHAHAHA'}").subscribe(
//                    () -> Log.d("mywebsocket", "Sent data!"),
//                    error -> Log.d("mywebsocket", "Encountered error while sending data!", error)
//            );

        } catch (Exception e) {
            Log.d("mywebsocket", e.toString());
        }
        return v;
    }

    private class MyChattingRoomWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();  //SSL 에러가 발생해도 계속 진행!
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            prgressbarLayout.setVisibility(View.GONE);
        }
    }
}
