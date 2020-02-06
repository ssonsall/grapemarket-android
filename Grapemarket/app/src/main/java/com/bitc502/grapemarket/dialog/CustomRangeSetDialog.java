package com.bitc502.grapemarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bitc502.grapemarket.R;
import com.bitc502.grapemarket.singleton.CurrentRangeForListFragment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomRangeSetDialog extends Dialog {

    private Context c;
    private SeekBar rangeSeekbar;
    private TextView rangeSeekbarCurrentValue;
    private Button btnSave, btnCancel;
    private String currentRange;

    public CustomRangeSetDialog(Context context, String currentRange) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        setCanceledOnTouchOutside(false);
        this.c = context;
        this.currentRange = currentRange;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.range_set);
        rangeSeekbar = findViewById(R.id.range_seekbar);
        btnSave = findViewById(R.id.btnRangeSetSave);
        btnCancel = findViewById(R.id.btnRangeSetCancel);
        rangeSeekbarCurrentValue = findViewById(R.id.range_seekbar_currentValue);
        //rangeSeekbarCurrentValue.setText(CurrentRangeForListFragment.getInstance().getCurrentRange());
        if (currentRange.equals("5km ▼")) {
            rangeSeekbar.setProgress(0);
            rangeSeekbarCurrentValue.setText("현재 : 5km");
        } else if (currentRange.equals("10km ▼")) {
            rangeSeekbar.setProgress(1);
            rangeSeekbarCurrentValue.setText("현재 : 10km");
        } else if (currentRange.equals("15km ▼")) {
            rangeSeekbar.setProgress(2);
            rangeSeekbarCurrentValue.setText("현재 : 15km");
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rangeSeekbarCurrentValue.getText().toString().equals("현재 : 5km")) {
                    CurrentRangeForListFragment.getInstance().setCurrentRange("5km ▼");
                    CurrentRangeForListFragment.getInstance().setCurrentRangeInteger(5);
                } else if (rangeSeekbarCurrentValue.getText().toString().equals("현재 : 10km")) {
                    CurrentRangeForListFragment.getInstance().setCurrentRange("10km ▼");
                    CurrentRangeForListFragment.getInstance().setCurrentRangeInteger(10);
                } else if (rangeSeekbarCurrentValue.getText().toString().equals("현재 : 15km")) {
                    CurrentRangeForListFragment.getInstance().setCurrentRange("15km ▼");
                    CurrentRangeForListFragment.getInstance().setCurrentRangeInteger(15);
                }
                CurrentRangeForListFragment.getInstance().setIsSaveAction(true);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentRangeForListFragment.getInstance().setIsSaveAction(false);
                dismiss();
            }
        });

        rangeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Integer currentValue = 5 + (seekBar.getProgress() * 5);
                rangeSeekbarCurrentValue.setText("현재 : " + currentValue.toString() + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Integer currentValue = 5 + (seekBar.getProgress() * 5);
                rangeSeekbarCurrentValue.setText("현재 : " + currentValue.toString() + "km");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Integer currentValue = 5 + (seekBar.getProgress() * 5);
                rangeSeekbarCurrentValue.setText("현재 : " + currentValue.toString() + "km");
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
}
