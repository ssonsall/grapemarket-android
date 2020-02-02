package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MySettingActivity extends AppCompatActivity {
    private Context mysettingContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        mysettingContext = getApplicationContext();
    }

    //내 정보보기
    public void btnMyProfileClicked(View v){
        Intent intent = new Intent(mysettingContext,MyProfileActivity.class);
        startActivity(intent);
    }
    //동네설정
    public void btnLocationSetClicked(View v){
        Intent intent = new Intent(mysettingContext,MyLocationSetting.class);
        startActivity(intent);
    }
    //동네인증
    public void btnLocationAuthClicked(View v){
        Intent intent = new Intent(mysettingContext,MyLocationAuthActivity.class);
        startActivity(intent);
    }
    //거래내역
    public void btnMyTradeLogClicked(View v){

    }
    //로그아웃
    public void btnLogoutClicked(View v){

    }


}
