package com.bitc502.grapemarket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.model.CurrentUserInfoForProfile;
import com.bitc502.grapemarket.model.User;
import com.bitc502.grapemarket.permission.PermissionsActivity;
import com.bitc502.grapemarket.permission.PermissionsChecker;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    private ConstraintLayout progressBarLayout;
    private TextView currentUsername,currentName,currentAddress,currentAddressAuth;
    private EditText currentEmail,currentPhone;
    private CircleImageView currentProfilePhoto;
    private Context myProfileContext;
    private Button btnChangeEmail, btnChangePhone;
    private User user;

    PermissionsChecker checker;
    private final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        user = new User();
        myProfileContext = getApplicationContext();
        checker = new PermissionsChecker(myProfileContext);
        progressBarLayout = findViewById(R.id.progressBarLayout);
        btnChangeEmail = findViewById(R.id.btn_myprofile_change_email);
        btnChangePhone = findViewById(R.id.btn_myprofile_change_phone);
        currentUsername = findViewById(R.id.myprofile_username);
        currentName = findViewById(R.id.myprofile_name);
        currentEmail = findViewById(R.id.myprofile_email);
        currentPhone = findViewById(R.id.myprofile_phone);
        currentAddress = findViewById(R.id.myprofile_address);
        currentAddressAuth = findViewById(R.id.myprofile_address_auth);
        currentProfilePhoto = findViewById(R.id.myprofile_proflie_photo);
        setMyProfileInfo();
    }

    //프로필 사진 변경
    public void btnChangeProfileImageClicked(View v){
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            getImageFromUserDevice();
        }
    }

    public void getImageFromUserDevice() {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.setPackage("com.google.android.apps.photos");
        final Intent chooserIntent = Intent.createChooser(intent, "사진을 선택하세요.");
        startActivityForResult(chooserIntent, 1010);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1010) {
            try {
                if (data == null) {
                    return;
                }
                Uri selectedImageUri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imagePath = cursor.getString(columnIndex);

                    Picasso.with(myProfileContext).load(new File(imagePath)).into(currentProfilePhoto);
                    cursor.close();
                    user.setUserProfile(imagePath);
                } else {

                }
            }catch (Exception e){
                Log.d("myerror", e.toString());
            }

        }
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }

    //비밀번호 변경 액티비티로 이동
    public void btnChangePassword(View v){
        Intent intent = new Intent(myProfileContext,ChangePasswordActivity.class);
        startActivity(intent);
    }

    //이메일 주소 수정
    public void btnChangeEmailClicked(View v){
        if(currentEmail.isEnabled()){
            currentEmail.setEnabled(false);
            btnChangeEmail.setText("수정");
            user.setEmail(currentEmail.getText().toString());
        }else{
            currentEmail.setEnabled(true);
            currentEmail.requestFocus();
            //키보드 불러내기
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
            btnChangeEmail.setText("완료");
        }

    }

    //전화번호 수정
    public void btnChangePhoneClicked(View v){
        if(currentPhone.isEnabled()){
            currentPhone.setEnabled(false);
            btnChangePhone.setText("수정");
            user.setPhone(currentPhone.getText().toString());
        }else{
            currentPhone.setEnabled(true);
            currentPhone.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
            btnChangePhone.setText("완료");
        }

    }

    //프로필 수정완료
    public void btnChangeProfileCompleteClicked(View v){
        new AsyncTask<Void, Boolean, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarLayout.setVisibility(View.VISIBLE);
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                return Connect2Server.updateProfile(user);
            }

            @Override
            protected void onProgressUpdate(Boolean... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                progressBarLayout.setVisibility(View.GONE);
            }

        }.execute();
    }

    //프로필 액티비티 진입시 현재 유저정보 가져와서 세팅하기
    public void setMyProfileInfo() {
        new AsyncTask<Void, Boolean, CurrentUserInfoForProfile>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarLayout.setVisibility(View.VISIBLE);
            }

            @Override
            protected CurrentUserInfoForProfile doInBackground(Void... voids) {
                return Connect2Server.getCurrentProfileInfo();
            }

            @Override
            protected void onProgressUpdate(Boolean... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(CurrentUserInfoForProfile currentUserInfo) {
                super.onPostExecute(currentUserInfo);
                currentUsername.setText(currentUserInfo.getUsername());
                currentName.setText(currentUserInfo.getName());
                currentEmail.setText(currentUserInfo.getEmail());
                currentPhone.setText(currentUserInfo.getPhone());
                currentProfilePhoto.setImageBitmap(currentUserInfo.getUserProfilePhoto());
                currentAddress.setText(currentUserInfo.getAddress());
                if(currentUserInfo.getAddressAuth() == 1){
                    currentAddressAuth.setText("인증");
                }else{
                    currentAddressAuth.setText("인증 안됨");
                }
                progressBarLayout.setVisibility(View.GONE);

                user.setId(currentUserInfo.getId());
                user.setEmail(currentUserInfo.getEmail());
                user.setPhone(currentUserInfo.getPhone());
                user.setUserProfile(currentUserInfo.getUserProfile());
            }

        }.execute();
    }
}
