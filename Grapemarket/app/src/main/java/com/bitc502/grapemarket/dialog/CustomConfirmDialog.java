package com.bitc502.grapemarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bitc502.grapemarket.BoardBuyerListActivity;
import com.bitc502.grapemarket.DetailActivity;
import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.singleton.CurrentRangeForListFragment;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomConfirmDialog extends Dialog {

    private Context c;
    private String message;
    private Integer boardId, buyerId;
    private TextView confirmMessage;
    private Button confirmOk, confirmCancel;
    private CustomConfirmDialogListener customConfirmDialogListener;

    public CustomConfirmDialog(@NonNull Context context, String message, Integer boardId, Integer buyerId, CustomConfirmDialogListener customConfirmDialogListener) {
        super(context);
        this.customConfirmDialogListener = customConfirmDialogListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        setCanceledOnTouchOutside(false);
        this.c = context;
        this.message = message;
        this.boardId = boardId;
        this.buyerId = buyerId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_dialog);
        confirmMessage = findViewById(R.id.confirm_message);
        confirmOk = findViewById(R.id.confirm_ok);
        confirmCancel = findViewById(R.id.confirm_cancel);

        confirmMessage.setText(message);

        confirmOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Boolean, Boolean>() {
                    CustomAnimationDialog podoLoading = new CustomAnimationDialog(c);
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        podoLoading.show();
                    }

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        Log.d("tradeidtest", boardId + " .... " + buyerId);
                        return Connect2Server.updateTradeComplete(boardId,buyerId);
                    }

                    @Override
                    protected void onProgressUpdate(Boolean... values) {
                        super.onProgressUpdate(values);
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        super.onPostExecute(result);
                        if (result) {
                            customConfirmDialogListener.clickConfirm();
                            podoLoading.dismiss();
                            dismiss();
                            Toast.makeText(c, "판매상태 업데이트에 성공했습니다.", Toast.LENGTH_LONG).show();
                        } else {
                            podoLoading.dismiss();
                            Toast.makeText(c,"판매완료 업데이트 실패",Toast.LENGTH_LONG).show();
                            customConfirmDialogListener.clickCancel();
                            dismiss();
                        }
                    }
                }.execute();
            }
        });

        confirmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customConfirmDialogListener.clickCancel();
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface CustomConfirmDialogListener{
        void clickConfirm();
        void clickCancel();
    }
}
