package com.bitc502.grapemarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.dialog.CustomAnimationDialog;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText password, passwordConfirm;
    private Context changPasswordContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        changPasswordContext = getApplicationContext();
        password = findViewById(R.id.change_password_new);
        passwordConfirm = findViewById(R.id.change_password_new_confirm);
    }

    public void btnChangePasswordComplete(View v) {
        String newPassword = password.getText().toString();
        String newPasswordConfirm = passwordConfirm.getText().toString();
        if (newPassword.equals(newPasswordConfirm)) {
            new AsyncTask<Void, Boolean, Boolean>() {
                CustomAnimationDialog podoLoading = new CustomAnimationDialog(ChangePasswordActivity.this);
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    podoLoading.show();
                }

                @Override
                protected Boolean doInBackground(Void... voids) {
                    return Connect2Server.changePassword(newPassword);
                }

                @Override
                protected void onProgressUpdate(Boolean... values) {
                    super.onProgressUpdate(values);
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    super.onPostExecute(result);
                    podoLoading.dismiss();
                    if(result) {
                        Intent intent = new Intent(changPasswordContext,MyProfileActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(changPasswordContext, "비밀번호 변경에 실패했습니다.", Toast.LENGTH_LONG).show();
                    }
                }


            }.execute();
        } else {
            Toast.makeText(changPasswordContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
        }
    }
}
