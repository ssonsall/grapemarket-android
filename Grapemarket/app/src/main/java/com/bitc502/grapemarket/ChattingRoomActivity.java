package com.bitc502.grapemarket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.singleton.Session;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChattingRoomActivity extends AppCompatActivity {
    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅
    private ImageView chattingRoomImageView;
    private TextView chattingRoomTitle;
    private String chattingRoomId, chattingTitle, chattingImageUrl;
    private boolean flag;
    private ScrollView webViewScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);

        flag = false;
        mWebView = findViewById(R.id.chattingRoomWebView);
        chattingRoomTitle = findViewById(R.id.chattingRoomTitle);
        chattingRoomImageView = findViewById(R.id.chattingRoomImageView);
        webViewScrollView = findViewById(R.id.webView_scrollView);
        Drawable drawable = getDrawable(R.drawable.round_imageview);
        chattingRoomImageView.setBackground(drawable);
        chattingRoomImageView.setClipToOutline(true);
        chattingRoomId = getIntent().getExtras().getString("chattingRoomId");
        chattingTitle = getIntent().getExtras().getString("chattingTitle");
        chattingImageUrl = getIntent().getExtras().getString("chattingImageUrl");

        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        //isOpen == true : Keyboard 올라왔을때
                        if (isOpen) {
                            webViewScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }
                });

        try {
            new AsyncTask<Void, Bitmap, Bitmap>() {
                CustomAnimationDialog podoLoading = new CustomAnimationDialog(ChattingRoomActivity.this);

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    podoLoading.show();
                }

                @Override
                protected Bitmap doInBackground(Void... voids) {
                    try {
                        Bitmap boardImage;
                        //OKHTTP3
                        Request requestForImage = new Request.Builder()
                                .addHeader("Cookie", Session.currentUserInfo.getJSessionId())
                                .url(Connect2Server.getIpAddress() + "/upload/" + chattingImageUrl)
                                .get()
                                .build();
                        OkHttpClient clientForImage = Connect2Server.getUnsafeOkHttpClient();
                        Response responseForImage = clientForImage.newCall(requestForImage).execute();
                        InputStream inputStream = responseForImage.body().byteStream();
                        boardImage = BitmapFactory.decodeStream(inputStream);
                        return boardImage;
                    } catch (Exception e) {
                        Log.d("chattinglisttest", e.toString());
                        return null;
                    }
                }

                @Override
                protected void onProgressUpdate(Bitmap... values) {
                    super.onProgressUpdate(values);
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    chattingRoomTitle.setText(chattingTitle);
                    chattingRoomImageView.setImageBitmap(result);
                    podoLoading.dismiss();
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
            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onCreateWindow(WebView view, boolean isDialog,
                                              boolean isUserGesture, Message resultMsg) {
                    WebView newWebView = new WebView(ChattingRoomActivity.this);
                    newWebView.getSettings().setJavaScriptEnabled(true);
                    newWebView.getSettings().setSupportZoom(false);
                    newWebView.getSettings().setBuiltInZoomControls(false);
                    newWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                    newWebView.getSettings().setSupportMultipleWindows(true);
                    view.addView(newWebView);
                    WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                    transport.setWebView(newWebView);
                    resultMsg.sendToTarget();

                    CookieManager cookieManagerReport = CookieManager.getInstance();
                    newWebView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                            handler.proceed();  //SSL 에러가 발생해도 계속 진행!
                        }

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            cookieManagerReport.setCookie(url, Session.currentUserInfo.getJSessionId());
                            view.loadUrl(url);
                            return true;
                        }
                    });

                    return true;
                }
            });
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);

            cookieManager.setCookie(Connect2Server.getIpAddress() + "/chat/android/room/enter/" + chattingRoomId, Session.currentUserInfo.getJSessionId());
            mWebView.loadUrl(Connect2Server.getIpAddress() + "/chat/android/room/enter/" + chattingRoomId);
        } catch (Exception e) {
            Log.d("chattingroom", e.toString());
        }
    }

    public void btnToolbarBack(View v) {
        super.onBackPressed();
    }

    private class MyChattingRoomWebViewClient extends WebViewClient {
        CustomAnimationDialog podoLoading = new CustomAnimationDialog(ChattingRoomActivity.this);

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            podoLoading.show();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();  //SSL 에러가 발생해도 계속 진행!
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            podoLoading.dismiss();
        }
    }
}
