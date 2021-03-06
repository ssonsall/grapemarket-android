package com.bitc502.grapemarket;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitc502.grapemarket.Util.InternetConnection;
import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.model.NullCheckState;
import com.bitc502.grapemarket.permission.PermissionsActivity;
import com.bitc502.grapemarket.permission.PermissionsChecker;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


public class JoinActivity extends AppCompatActivity {

    private final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    PermissionsChecker checker;

    private EditText username;
    private EditText password;
    private EditText passwordCheck;
    private EditText name;
    private EditText email;
    private EditText phone;
    private String imagePath;
    private Context joinContext;
    private CircleImageView showSelectedImageView;
    private NullCheckState nullCheckState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        nullCheckState = new NullCheckState();
        joinContext = getApplicationContext();
        checker = new PermissionsChecker(this);
        showSelectedImageView = findViewById(R.id.imageSelected);

        username = findViewById(R.id.join_username);
        password = findViewById(R.id.join_password);
        passwordCheck = findViewById(R.id.passwordCheck);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
    }

    //회원가입 버튼
    public void btnJoinClicked(View v) {
        if (!TextUtils.isEmpty(imagePath)) {
            if (InternetConnection.checkConnection(joinContext)) {
                new AsyncTask<Void, Integer, Integer>() {
                    CustomAnimationDialog podoLoading = new CustomAnimationDialog(JoinActivity.this);

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        checkNullBlank();
                        podoLoading.show();
                    }

                    @Override
                    protected Integer doInBackground(Void... voids) {
                        try {

                            if (!nullCheckState.getIsValidate()) {
                                return -3;
                            }

                            //이미지 파일 포함 회원가입 정보 서버로 전송
                            if (password.getText().toString().equals(passwordCheck.getText().toString())) {
                                return Connect2Server.
                                        sendJoinInfoToServer(imagePath, username.getText().toString(), password.getText().toString(),
                                                name.getText().toString(), email.getText().toString(), phone.getText().toString());
                            } else {
                                return 0;
                            }
                        } catch (Exception e) {
                            Log.d("myerror", e.toString());
                            return -1;
                        }
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                    }

                    @Override
                    protected void onPostExecute(Integer integer) {
                        super.onPostExecute(integer);
                        podoLoading.dismiss();
                        if (integer == 1) {
                            Log.d("myerror", "회원가입 성공");
                            showSelectedImageView.setVisibility(View.INVISIBLE);
                            imagePath = "";
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (integer == -1) { // Connect2Server.java에서 에러
                            Log.d("myerror", "회원가입 실패");
                            Toast.makeText(joinContext, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                        } else if (integer == 0) { // 비밀번호 불일치
                            Toast.makeText(joinContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                        } else if (integer == 2) { // 아이디 중복확인 처리

                        } else if (integer == -3) { // 정보부족
                            Toast.makeText(joinContext, nullCheckState.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }.execute();
            } else {
                //인터넷 연결 안되었을때
                Toast.makeText(joinContext, "인터넷 연결을 확인해주세요.", Toast.LENGTH_LONG).show();
            }
        } else {
            //사진 선택안했을때 (나중에 선택안해도 되도록)
            Toast.makeText(joinContext, "사진을 선택해주세요.", Toast.LENGTH_LONG).show();
        }
    }

    //Image 선택버튼
    public void btnImageSelectClicked(View v) {
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

                    Picasso.with(joinContext).load(new File(imagePath)).into(showSelectedImageView);
                    cursor.close();
                    showSelectedImageView.setVisibility(View.VISIBLE);
                } else {

                }
            } catch (Exception e) {
                Log.d("myerror", e.toString());
            }

        }
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }

    private NullCheckState checkNullBlank() {

        if (TextUtils.isEmpty(username.getText()) || username.getText().toString().equals("")) {
            nullCheckState.setIsValidate(false);
            nullCheckState.setMessage("아이디를 입력하세요.");
            return nullCheckState;
        } else if (TextUtils.isEmpty(password.getText()) || password.getText().toString().equals("")) {
            nullCheckState.setIsValidate(false);
            nullCheckState.setMessage("비밀번호를 입력하세요.");
            return nullCheckState;
        } else if (TextUtils.isEmpty(passwordCheck.getText()) || passwordCheck.getText().toString().equals("")) {
            nullCheckState.setIsValidate(false);
            nullCheckState.setMessage("비밀번호 확인을 입력하세요.");
            return nullCheckState;
        } else if (TextUtils.isEmpty(name.getText()) || name.getText().toString().equals("")) {
            nullCheckState.setIsValidate(false);
            nullCheckState.setMessage("닉네임을 입력하세요.");
            return nullCheckState;
        } else if (TextUtils.isEmpty(email.getText()) || email.getText().toString().equals("")) {
            nullCheckState.setIsValidate(false);
            nullCheckState.setMessage("이메일을 입력하세요.");
            return nullCheckState;
        } else if (TextUtils.isEmpty(phone.getText()) || phone.getText().toString().equals("")) {
            nullCheckState.setIsValidate(false);
            nullCheckState.setMessage("전화번호를 입력하세요.");
            return nullCheckState;
        }
        nullCheckState.setIsValidate(true);
        nullCheckState.setMessage("ok");
        return nullCheckState;
    }
}
