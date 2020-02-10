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
public class CustomOutChattingDialog extends Dialog {

    private Context c;
    private String message, buyerOrSeller;
    private Integer roomId;
    private TextView confirmMessage;
    private Button confirmOk, confirmCancel;
    private CustomOutChattingDialogListener customOutChattingDialogListener;

    public CustomOutChattingDialog(@NonNull Context context, Integer roomId, String buyerOrSeller, String message,CustomOutChattingDialogListener customOutChattingDialogListener) {
        super(context);
        this.customOutChattingDialogListener = customOutChattingDialogListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        setCanceledOnTouchOutside(false);
        this.c = context;
        this.message = message;
        this.roomId = roomId;
        this.buyerOrSeller = buyerOrSeller;
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
                        return Connect2Server.outChatting(roomId,buyerOrSeller);
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
                            Toast.makeText(c,"채팅방 나가기 성공",Toast.LENGTH_LONG).show();
                            customOutChattingDialogListener.clickConfirm();
                            dismiss();
                        }else{
                            Toast.makeText(c,"채팅방 나가기 실패",Toast.LENGTH_LONG).show();
                            customOutChattingDialogListener.clickCancel();
                            dismiss();
                        }
                    }
                }.execute();
            }
        });

        confirmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customOutChattingDialogListener.clickCancel();
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

    public interface CustomOutChattingDialogListener{
        void clickConfirm();
        void clickCancel();
    }
}
