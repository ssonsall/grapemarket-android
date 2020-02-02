package com.bitc502.grapemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
    private ChattingRoomFragment chattingRoomFragment = new ChattingRoomFragment();
    private MySettingFragment mySettingFragment = new MySettingFragment();
    private View systemSoftKey;
    final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            |View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            |View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
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

    @Override
    protected void onResume() {
        super.onResume();
        systemSoftKey = getWindow().getDecorView();
        systemSoftKey.setSystemUiVisibility(uiOptions);
    }

    //Fragment -> Fragment 전환시 사용할 메서드
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame ,fragment).commitAllowingStateLoss();
    }

    //Fragment 안의 버튼 세팅
    public void btnWriteSelectImageClicked(View v) {
        writeFragment.btnWriteSelectImageClicked(v);
    }

    public void btnWriteComplete(View v) {
        writeFragment.btnWriteComplete(v);
    }

    public void btnImgDelete1(View v){writeFragment.btnImgDelete1(v);}

    public void btnImgDelete2(View v){writeFragment.btnImgDelete2(v);}

    public void btnImgDelete3(View v){writeFragment.btnImgDelete3(v);}

    public void btnImgDelete4(View v){writeFragment.btnImgDelete4(v);}

    public void btnImgDelete5(View v){writeFragment.btnImgDelete5(v);}

    public void btnProductSearchClicked(View v) {
        searchFragment.btnProductSearchClicked(v);
    }

    public void btnMyProfileClicked(View v) {
        mySettingFragment.btnMyProfileClicked(v);
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
