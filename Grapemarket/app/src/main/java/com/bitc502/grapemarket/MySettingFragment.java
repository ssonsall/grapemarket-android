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
import android.widget.TextView;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.singleton.Session;

public class MySettingFragment extends Fragment {
    private Context mysettingContext;
    private TextView rangeSet;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_my_setting, container, false);
        mysettingContext = getContext();
        rangeSet = getActivity().findViewById(R.id.toolbar_range_set);
        rangeSet.setVisibility(View.INVISIBLE);
        return v;
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
        Intent intent = new Intent(mysettingContext,TradeLogActivity.class);
        startActivity(intent);
    }
    //로그아웃
    public void btnLogoutClicked(View v){
        Session.currentUserInfo.setJSessionId("");
        Session.currentUserInfo.setUser(null);
        Intent intent = new Intent(getContext(),MainActivity.class);
        startActivity(intent);
    }
}
