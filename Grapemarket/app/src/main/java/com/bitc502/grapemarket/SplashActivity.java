package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler() {

            @Override

            public void handleMessage(Message msg) {

                finish(); //현재 액티비티 즉 SplashActivity 종료



                //페이드 인 페이드 아웃 효과 res/anim/fadein, fadeout xml을 만들어 줘야 합니다.

                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }

        };

        handler.sendEmptyMessageDelayed(0,2000); //2000 시간 설정 1000->1초

    }

    @Override
    public void onBackPressed() {}
}
