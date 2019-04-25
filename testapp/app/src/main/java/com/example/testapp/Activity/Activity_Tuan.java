package com.example.testapp.Activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.example.testapp.CustomeCalandar.Adapter_Calendar_Tuan;
import com.example.testapp.CustomeCalandar.LuaChonTrongTuan;
import com.example.testapp.Model.CongViec;
import com.example.testapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Activity_Tuan extends AppCompatActivity {
    public GregorianCalendar cal_week, cal_week_copy;
    private TextView tuan,ngay;
    private GridView gridAM,gridPM;
    private DatabaseReference reference;
    private ArrayList<LuaChonTrongTuan> luaChonTrongTuanAM;
    private ArrayList<LuaChonTrongTuan> luaChonTrongTuanPM;
    private Adapter_Calendar_Tuan adapter_calendar_tuanAM;
    private Adapter_Calendar_Tuan adapter_calendar_tuanPM;
    private Calendar cal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__tuan);
        reference= FirebaseDatabase.getInstance().getReference();
        laytheHien();
        loadUI();
        sukien();

    }
    private void laytheHien()
    {
        tuan=findViewById(R.id.tv_tuan);
        ngay=findViewById(R.id.ngaybdkt_tuan);
        gridAM=findViewById(R.id.itemdata_AM);
        gridPM=findViewById(R.id.itemdata_PM);
    }
    private void loadUI()
    {

        cal=Calendar.getInstance();
        int thang=cal.get(Calendar.MONTH)+1;
        tuan.setText("Tuần "+cal.get(Calendar.DAY_OF_WEEK_IN_MONTH)+" tháng "+thang+"/"+cal.get(Calendar.YEAR));
    }

    private void loadDuLieu() {
        reference.child("CongViec").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CongViec cv=dataSnapshot.getValue(CongViec.class);
                String[] ngaybd=cv.getGiobatdau().split(" ");
                String gio=ngaybd[1].toString();
                if(gio.equals("AM"))
                {
                    luaChonTrongTuanAM.add(new LuaChonTrongTuan(cv.getGiobatdau(),cv.getGioketthuc(),cv.getTieude(),cv.getNgaybatdau(),cv.getTrangthai()));
                    adapter_calendar_tuanAM.notifyDataSetChanged();
                }
                else
                {
                    luaChonTrongTuanPM.add(new LuaChonTrongTuan(cv.getGiobatdau(),cv.getGioketthuc(),cv.getTieude(),cv.getNgaybatdau(),cv.getTrangthai()));
                    adapter_calendar_tuanPM.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sukien()
    {

    }
}
