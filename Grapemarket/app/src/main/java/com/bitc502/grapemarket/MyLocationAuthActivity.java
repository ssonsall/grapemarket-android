package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.model.CurrentUserInfo;
import com.bitc502.grapemarket.model.UserLocationSetting;

public class MyLocationAuthActivity extends AppCompatActivity {

    private TextView savedAddressX,savedAddressY,currentAddressX,currentAddressY;
    private CurrentUserInfo currentUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location_auth);
        savedAddressX = findViewById(R.id.savedAddressX);
        savedAddressY = findViewById(R.id.savedAddressY);
        currentAddressX = findViewById(R.id.currentAddressX);
        currentAddressY = findViewById(R.id.currentAddressY);
        currentUserInfo = CurrentUserInfo.getInstance();
    }

    public void btnAddressAuthClicked(View V){
        new AsyncTask<Void, UserLocationSetting, UserLocationSetting>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected UserLocationSetting doInBackground(Void... voids) {
                return Connect2Server.getSavedAddress(currentUserInfo+"");
            }

            @Override
            protected void onProgressUpdate(UserLocationSetting... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(UserLocationSetting userLocationSetting) {
                super.onPostExecute(userLocationSetting);
            }
        }.execute();
    }
}
