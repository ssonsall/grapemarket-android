package com.bitc502.grapemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MotherActivity extends AppCompatActivity {

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    private ListFragment listFragment = new ListFragment();
    private WriteFragment writeFragment = new WriteFragment();
    private SearchFragment searchFragment = new SearchFragment();
    private ChattingFragment chattingFragment = new ChattingFragment();
    private MySettingFragment mySettingFragment = new MySettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, listFragment).commitAllowingStateLoss();


        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_menu1: {
                        transaction.replace(R.id.main_frame, listFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu2: {
                        transaction.replace(R.id.main_frame, writeFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu3: {
                        transaction.replace(R.id.main_frame, chattingFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu4: {
                        transaction.replace(R.id.main_frame, searchFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu5: {
                        transaction.replace(R.id.main_frame, mySettingFragment).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });
    }

    //Fragment 안의 버튼 세팅
    public void btnWriteSelectImageClicked(View v) {
        writeFragment.btnWriteSelectImageClicked(v);
    }

    public void btnWriteComplete(View v) {
        writeFragment.btnWriteComplete(v);
    }

    public void btnProductSearchClicked(View v) {
        searchFragment.btnProductSearchClicked(v);
    }

    public void btnMyInfoClicked(View v) {
        mySettingFragment.btnMyInfoClicked(v);
    }

    public void btnLocationSetClicked(View v) {
        mySettingFragment.btnLocationSetClicked(v);
    }

    public void btnLocationAuthClicked(View v) {
        mySettingFragment.btnLocationAuthClicked(v);
    }

    public void btnMyTradeLogClicked(View v) {
        mySettingFragment.btnMyTradeLogClicked(v);
    }

    public void btnLogoutClicked(View v) {
        mySettingFragment.btnLogoutClicked(v);
    }
}
