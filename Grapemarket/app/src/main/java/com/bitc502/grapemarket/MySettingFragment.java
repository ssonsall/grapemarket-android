package com.bitc502.grapemarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MySettingFragment extends Fragment {
    private Context mysettingContext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_my_setting, container, false);
        mysettingContext = getContext();
        return v;
    }

    //내 정보보기
    public void btnMyInfoClicked(View v){
        Intent intent = new Intent(mysettingContext,MyInfoActivity.class);
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
