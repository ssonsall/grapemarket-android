package com.bitc502.grapemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother);
        Log.d("crazy", "MotherActivity !!!!");

        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, listFragment).commitAllowingStateLoss();

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_menu1: {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_frame, listFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu2: {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_frame, writeFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu3: {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_frame, chattingFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu4: {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_frame, searchFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu5: {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
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
    }

    @Override
    public void onBackPressed() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.isVisible()) {
                if (fragment instanceof ListFragment) {
                    //현재 ListFragment이면 종료
                    finishAffinity();
                    System.runFinalization();
                    System.exit(0);
                } else {
                    //다른 Fragment에 있으면 ListFragment로 이동
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_frame, listFragment).commitAllowingStateLoss();
                    //이동만 하면 내비게이션뷰에 Active상태가 안바뀐다. 그래서 바꿔줌.
                    bottomNavigationView.setSelectedItemId(R.id.navigation_menu1);
                }
            }
        }
    }

    //Fragment -> Fragment 전환시 사용할 메서드
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment).commitAllowingStateLoss();
    }

    //Fragment 안의 버튼 세팅
    public void btnRangeSetClicked(View v) {
        listFragment.btnRangeSetClicked(v);
    }

    public void btnGoAddressSetting(View v) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.isVisible()) {
                if (fragment instanceof ListFragment) {
                    listFragment.btnGoAddressSetting(v);
                } else if (fragment instanceof WriteFragment) {
                    writeFragment.btnGoAddressSetting(v);
                } else if (fragment instanceof SearchFragment) {
                    searchFragment.btnGoAddressSetting(v);
                } else if (fragment instanceof MySettingFragment) {
                    mySettingFragment.btnGoAddressSetting(v);
                } else if (fragment instanceof ChattingFragment) {
                    chattingFragment.btnGoAddressSetting(v);
                }
            }
        }
    }

    public void btnToolbarBack(View v) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.isVisible()) {
                if (fragment instanceof ListFragment) {
                    //현재 ListFragment이면 종료
                    finishAffinity();
                    System.runFinalization();
                    System.exit(0);
                } else {
                    //다른 Fragment에 있으면 ListFragment로 이동
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_frame, listFragment).commitAllowingStateLoss();
                    //이동만 하면 내비게이션뷰에 Active상태가 안바뀐다. 그래서 바꿔줌.
                    bottomNavigationView.setSelectedItemId(R.id.navigation_menu1);
                }
            }
        }
    }

    public void btnWriteSelectImageClicked(View v) {
        writeFragment.btnWriteSelectImageClicked(v);
    }

    public void btnWriteComplete(View v) {
        writeFragment.btnWriteComplete(v);
    }

    public void btnImgDelete1(View v) {
        writeFragment.btnImgDelete1(v);
    }

    public void btnImgDelete2(View v) {
        writeFragment.btnImgDelete2(v);
    }

    public void btnImgDelete3(View v) {
        writeFragment.btnImgDelete3(v);
    }

    public void btnImgDelete4(View v) {
        writeFragment.btnImgDelete4(v);
    }

    public void btnImgDelete5(View v) {
        writeFragment.btnImgDelete5(v);
    }

    public void write_spinner_arrow_btn_clicked(View v) {
        writeFragment.write_spinner_arrow_btn_clicked(v);
    }

    public void btnProductSearchClicked(View v) {
        searchFragment.btnProductSearchClicked(v);
    }

    public void search_spinner_arrow_btn_clicked(View v) {
        searchFragment.search_spinner_arrow_btn_clicked(v);
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
