package com.bitc502.grapemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bitc502.grapemarket.model.TradeLogList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TradeLogActivity extends AppCompatActivity {
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView tradeLogNavgationView;
    private Fragment tradeLogBuyFragment = new TradeLogBuyFragment();
    private Fragment tradeLogSellFragment = new TradeLogSellFragment();
    private Boolean isLoaded;
    private TradeLogList tradeLogListGlobal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_log);
        tradeLogNavgationView = findViewById(R.id.tradeLogNavationView);
        isLoaded = false;
        tradeLogListGlobal = new TradeLogList();

        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.tradLogMainFrame, tradeLogBuyFragment).commitAllowingStateLoss();

        // tradeLogNavgationView의 아이템이 선택될 때 호출될 리스너 등록
        tradeLogNavgationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_menu1: {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.tradLogMainFrame, tradeLogBuyFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu2: {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.tradLogMainFrame, tradeLogSellFragment).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void btnToolbarBack(View v) {
        super.onBackPressed();
    }

    public void setTradeLogListGlobal(TradeLogList tradeLogList) {
        this.tradeLogListGlobal.getTradeLogBuyList().clear();
        this.tradeLogListGlobal.getTradeLogSellList().clear();
        isLoaded = true;
        this.tradeLogListGlobal = tradeLogList;
    }

    public TradeLogList getTradeLogListGlobal() {
        return this.tradeLogListGlobal;
    }

    public Boolean getIsLoaded() {
        return this.isLoaded;
    }
}
