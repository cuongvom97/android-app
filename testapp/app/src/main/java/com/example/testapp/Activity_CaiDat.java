package com.example.testapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class Activity_CaiDat extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout layout_caidat;
    private RadioButton dark,light;
    private Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__cai_dat);
        laythehien();
        loadUI();
        sukien();
    }
    private void laythehien()
    {
        layout_caidat=findViewById(R.id.layout_caidat);
        dark=findViewById(R.id.caidat_rad_dark);
        light=findViewById(R.id.caidat_rad_light);
        ok=findViewById(R.id.caidat_btn_ok);
    }
    private  void loadUI()
    {

    }
    private  void sukien()
    {
        ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.caidat_btn_ok:
                if(dark.isChecked())
                {
                    layout_caidat.setBackgroundColor(Color.parseColor("#607D8B"));
                }
                else
                    layout_caidat.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }
}
