package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.currentuserinfo.Session;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView main_username;
    private TextView main_password;
    private Context mainContext;
    private String testUsername,testPassword;
    private View systemSoftKey,mainCenterView;
    private ProgressBar progressBar;
    final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            |View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            |View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_username = findViewById(R.id.main_username);
        main_password = findViewById(R.id.main_password);
        mainContext = getApplicationContext();
        progressBar = findViewById(R.id.main_progress_bar);
        mainCenterView = findViewById(R.id.main_center_space);

        //TestLogin Setting
        testUsername = "han";
        testPassword = "1";
    }

    @Override
    protected void onResume() {
        super.onResume();
        systemSoftKey = getWindow().getDecorView();
        systemSoftKey.setSystemUiVisibility(uiOptions);
    }

    //로그인 버튼
    public void btnLoginClicked(View view){

        new AsyncTask<Void,Boolean,Boolean>(){
            Intent intent;
            String username = main_username.getText().toString();
            String password = main_password.getText().toString();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mainCenterView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                if(Connect2Server.sendLoginInfoToServer(username,password)){
                    Session.currentUserInfo.setUser(Connect2Server.getLoginUserInfo(username));
                    Log.d("jsessiontest", Session.currentUserInfo.getJSessionId());
                    return true;
                }else {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean loginResult) {
                super.onPostExecute(loginResult);
                mainCenterView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                if(loginResult) {
                    intent = new Intent(getApplicationContext(), MotherActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(mainContext,"로그인에 실패했습니다.",Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    //회원가입 버튼
    public void btnJoinClicked(View view){
        Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
        startActivity(intent);
    }




    //테스트 로그인
    public void btnTestLoginClicked(View v){
        new AsyncTask<Void,Boolean,Boolean>(){
            Intent intent;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mainCenterView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                if(Connect2Server.sendLoginInfoToServer(testUsername,testPassword)){
                    Session.currentUserInfo.setUser(Connect2Server.getLoginUserInfo(testUsername));
                    Log.d("jsessiontest", Session.currentUserInfo.getJSessionId());
                    return true;
                }else {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean loginResult) {
                super.onPostExecute(loginResult);
                mainCenterView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                if(loginResult) {
                    intent = new Intent(getApplicationContext(), MotherActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(mainContext,"로그인에 실패했습니다.",Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

}
