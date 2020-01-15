package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.currentuserinfo.Session;
import com.bitc502.grapemarket.model.UserLocationSetting;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyLocationSetting extends AppCompatActivity {
    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅
    private TextView address, addressX, addressY;
    private Context myLocationSettingContext;
    private UserLocationSetting userLocationSetting;
    private Boolean flag;
    private Button btnSettingUserAddress, btnSaveUserAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location_setting);
        flag = false;
        myLocationSettingContext = getApplicationContext();
        mWebView = findViewById(R.id.webView);
        address = findViewById(R.id.myLocationSetAddress);
        addressX = findViewById(R.id.myLocationSetLatitude);
        addressY = findViewById(R.id.myLocationSetLongitude);
        btnSettingUserAddress = findViewById(R.id.btnAddressSetting);
        btnSaveUserAddress = findViewById(R.id.btnAddressSave);
    }

    //동네설정 버튼
    public void btnAddressSettingClicked(View v) {
        try {
            mWebView.setWebViewClient(new MyWebViewClient());
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
            mWebView.loadUrl("https://192.168.43.40:8443/map/android/popup"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
        } catch (Exception e) {
            Log.d("addressTest", e.toString());
        }
    }

    //저장 버튼
    public void btnAddressSaveClicked(View v) {
        new AsyncTask<Void, Boolean,Boolean>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                return Connect2Server.saveUserAddressSetting(address.getText().toString(),addressX.getText().toString(),addressY.getText().toString(), Session.currentUserInfo.getUser().getId()+"");
            }

            @Override
            protected void onProgressUpdate(Boolean... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if(aBoolean){
                    Session.currentUserInfo.getUser().setAddress(address.getText().toString());
                    Session.currentUserInfo.getUser().setAddressX(Double.parseDouble(addressX.getText().toString()));
                    Session.currentUserInfo.getUser().setAddressY(Double.parseDouble(addressY.getText().toString()));
                    Toast.makeText(myLocationSettingContext,"동네 설정 성공",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(myLocationSettingContext,"동네 설정 실패",Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    private class MyWebViewClient extends WebViewClient {
        //Override 된 함수들에 대한 공부 필요.
        //일단 원하는대로 동작은 하는데 잘 이해가 안됨.
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();  //SSL 에러가 발생해도 계속 진행!
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (flag) {
                new AsyncTask<Void, Boolean, Boolean>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        try {

                            Request okRequest = new Request.Builder()
                                    .url(request.getUrl().toString())
                                    .get()
                                    .build();

                            OkHttpClient client = Connect2Server.getUnsafeOkHttpClient();
                            Response response = client.newCall(okRequest).execute();
                            String res = response.body().string();
                            userLocationSetting = new Gson().fromJson(res,UserLocationSetting.class);
                            return true;
                        } catch (Exception e) {
                            Log.d("please", e.toString());
                            return false;
                        }
                    }

                    @Override
                    protected void onProgressUpdate(Boolean... values) {
                        super.onProgressUpdate(values);
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        if(aBoolean){
                            address.setText(userLocationSetting.getAddress());
                            addressX.setText(userLocationSetting.getAddressX());
                            addressY.setText(userLocationSetting.getAddressY());
                        }
                    }

                }.execute();
            }
           return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            flag = true;
        }
    }
}
