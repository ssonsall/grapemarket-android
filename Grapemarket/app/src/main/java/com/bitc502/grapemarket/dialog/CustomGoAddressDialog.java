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
import android.widget.LinearLayout;
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
public class CustomGoAddressDialog extends Dialog {

    private Context c;
    private String message;
    private TextView confirmMessage;
    private Button confirmOk, confirmCancel;
    private CustomGoAddressDialogListener customGoAddressDialogListener;

    public CustomGoAddressDialog(@NonNull Context context, String message,CustomGoAddressDialogListener customGoAddressDialogListener) {
        super(context);
        this.customGoAddressDialogListener = customGoAddressDialogListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        setCanceledOnTouchOutside(false);
        this.c = context;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_dialog);
        confirmMessage = findViewById(R.id.confirm_message);
        confirmOk = findViewById(R.id.confirm_ok);
        confirmCancel = findViewById(R.id.confirm_cancel);
        confirmMessage.setText(message);
        //confirmCancel.setVisibility(View.GONE);
        confirmOk.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        confirmOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customGoAddressDialogListener.clickConfirm();
                dismiss();
            }
        });

        confirmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customGoAddressDialogListener.clickCancel();
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

    public interface CustomGoAddressDialogListener{
        void clickConfirm();
        void clickCancel();
    }
}
