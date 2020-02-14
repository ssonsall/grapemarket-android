package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;
import com.bitc502.grapemarket.model.NullCheckState;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText password, passwordConfirm;
    private Context changPasswordContext;
    private NullCheckState nullCheckState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        changPasswordContext = getApplicationContext();
        password = findViewById(R.id.change_password_new);
        passwordConfirm = findViewById(R.id.change_password_new_confirm);
        nullCheckState = new NullCheckState();
    }

    public void btnChangePasswordComplete(View v) {
        String newPassword = password.getText().toString();
        String newPasswordConfirm = passwordConfirm.getText().toString();
        if (newPassword.equals(newPasswordConfirm)) {
            new AsyncTask<Void, Boolean, Integer>() {
                CustomAnimationDialog podoLoading = new CustomAnimationDialog(ChangePasswordActivity.this);

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    podoLoading.show();
                }

                @Override
                protected Integer doInBackground(Void... voids) {
                    checkNullBlank();
                    if(!nullCheckState.getIsValidate()){
                        return -3;
                    }
                    return Connect2Server.changePassword(newPassword);
                }

                @Override
                protected void onProgressUpdate(Boolean... values) {
                    super.onProgressUpdate(values);
                }

                @Override
                protected void onPostExecute(Integer result) {
                    super.onPostExecute(result);
                    podoLoading.dismiss();
                    if (result == 1) {
                        Intent intent = new Intent(changPasswordContext, MyProfileActivity.class);
                        startActivity(intent);
                    } else if (result == -3) {
                        Toast.makeText(changPasswordContext,nullCheckState.getMessage(),Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(changPasswordContext, "비밀번호 변경에 실패했습니다.", Toast.LENGTH_LONG).show();
                    }
                }


            }.execute();
        } else {
            Toast.makeText(changPasswordContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
        }
    }


    private NullCheckState checkNullBlank() {
        if (TextUtils.isEmpty(password.getText()) || password.getText().toString().equals("")) {
            nullCheckState.setIsValidate(false);
            nullCheckState.setMessage("비밀번호를 입력하세요.");
            return nullCheckState;
        } else if (TextUtils.isEmpty(passwordConfirm.getText()) || passwordConfirm.getText().toString().equals("")) {
            nullCheckState.setIsValidate(false);
            nullCheckState.setMessage("비밀번호 확인을 입력하세요.");
            return nullCheckState;
        }
        nullCheckState.setIsValidate(true);
        nullCheckState.setMessage("ok");
        return nullCheckState;
    }
}
