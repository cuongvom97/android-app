package com.example.testapp.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.testapp.Custome.CustomeSpinner;
import com.example.testapp.Model.Nhan;
import com.example.testapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CapNhatCV extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private TextView ngaybatdau,giobatdau,giohoanthanh,tgthongbao;
    private EditText tieude,ghichu;
    private ImageButton thongbao;
    private Spinner nhanspinner;
    private Button themcv,thoat;
    private CheckBox chklap,chkthongbao;
    private RadioButton radngay,radthang,radtuan;
    private static String _key="";
    private DatabaseReference databaseReference;
    private ArrayAdapter<Nhan> stringArrayAdapterpinner;
    private List<Nhan> list;
    Calendar cal,calnow;
    Date dateStart,hourStart,hourFinish;
    Date datenow,hournow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_cv);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        layTheHien();
        loadUI();
        sukien();
    }
    private void layTheHien()
    {
        tieude=findViewById(R.id.capnhatcv_tieude);
        ghichu=findViewById(R.id.capnhatcv_ghichu);
        ngaybatdau=findViewById(R.id.tv_capnhatcv_ngaybatdau);
        giobatdau=findViewById(R.id.tv_capnhatcv_giobatdau);
        giohoanthanh=findViewById(R.id.tv_capnhatcv_gioht);
        thoat=findViewById(R.id.capnhatcv_btnthoat);
        themcv=findViewById(R.id.capnhatcv_btncapnhat);
        chklap=findViewById(R.id.capnhatcv_lap);
        radngay=findViewById(R.id.capnhatcv_lap_ngay);
        radthang=findViewById(R.id.capnhatcv_lap_thang);
        radtuan=findViewById(R.id.capnhatcv_lap_tuan);
        thongbao=findViewById(R.id.capnhatcv_btnthongbao);
        tgthongbao=findViewById(R.id.tv_capnhatcv_tgthongbao);
        nhanspinner=findViewById(R.id.capnhatcv_spinner_nhan);
        chkthongbao=findViewById(R.id.capnhatcv_checkthongbao);
    }
    private void loadUI(){
        layKeyCapNhat();
        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_capnhatcv);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list=new ArrayList<>();
        stringArrayAdapterpinner=new CustomeSpinner(this,R.layout.custome_layout_spinner,list);
        stringArrayAdapterpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        nhanspinner.setAdapter(stringArrayAdapterpinner);
        calnow= Calendar.getInstance();
        datenow=calnow.getTime();
        hournow=calnow.getTime();
        radngay.setEnabled(false);
        radtuan.setEnabled(false);
        radthang.setEnabled(false);
        loadDataSpinner();
    }

    private void loadDataSpinner() {
        databaseReference.child("Nhan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Nhan nhan=dataSnapshot.getValue(Nhan.class);
                list.add(nhan);
                stringArrayAdapterpinner.notifyDataSetChanged();
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
        })    ;
    }

    private void sukien(){
        nhanspinner.setOnItemSelectedListener(this);
        themcv.setOnClickListener(this);
        thoat.setOnClickListener(this);
        ngaybatdau.setOnClickListener(this);
        giobatdau.setOnClickListener(this);
        giohoanthanh.setOnClickListener(this);
        chklap.setOnClickListener(this);
        radngay.setOnClickListener(this);
        radtuan.setOnClickListener(this);
        radthang.setOnClickListener(this);
        thongbao.setOnClickListener(this);
        chkthongbao.setOnClickListener(this);
    }
    private void layKeyCapNhat()
    {
        Intent intent=getIntent();
        _key=intent.getStringExtra(Activity_DSCongViec_Ngay.KEYCAPNHAT);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    /**
     * Hàm lấy các thông số mặc định khi lần đầu tiền chạy ứng dụng
     */
    public void getDefaultInfor()
    {
        //lấy ngày hiện tại của hệ thống
        cal=Calendar.getInstance();
        SimpleDateFormat dft=null;
        //Định dạng ngày / tháng /năm
        dft=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate=dft.format(cal.getTime());
        //hiển thị lên giao diện
        ngaybatdau.setText(strDate);
        //Định dạng giờ phút am/pm
        dft=new SimpleDateFormat("hh:mm a",Locale.getDefault());
        String strTime=dft.format(cal.getTime());
        //đưa lên giao diện
        giobatdau.setText(strTime);
        giohoanthanh.setText(strTime);
        //lấy giờ theo 24 để lập trình theo Tag
        dft=new SimpleDateFormat("HH:mm",Locale.getDefault());
        giobatdau.setTag(dft.format(cal.getTime()));
        giohoanthanh.setTag(dft.format(cal.getTime()));
        //gán cal.getTime() cho ngày hoàn thành và giờ hoàn thành
        hourFinish=cal.getTime();
        dateStart=cal.getTime();
        hourStart=cal.getTime();
    }
    /**
     * Hàm hiển thị DatePicker dialog
     */
    public void showDatePickerDialogStart()
    {
        DatePickerDialog.OnDateSetListener callback=new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                //Mỗi lần thay đổi ngày tháng năm thì cập nhật lại TextView Date
                String thang=(monthOfYear+1)+"";
                if(thang.length()<=1)
                {
                    thang="0"+thang;
                }
                ngaybatdau.setText(
                        (dayOfMonth) +"/"+thang+"/"+year);
                //Lưu vết lại biến ngày hoàn thành
                cal.set(year, monthOfYear, dayOfMonth);
                dateStart=cal.getTime();
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s=ngaybatdau.getText()+"";
        String strArrtmp[]=s.split("/");
        int ngay=Integer.parseInt(strArrtmp[0]);
        int thang=Integer.parseInt(strArrtmp[1])-1;
        int nam=Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic=new DatePickerDialog(
                CapNhatCV.this,
                callback, nam, thang, ngay);
        pic.setTitle("Chọn ngày bắt đầu");
        pic.show();
    }
    /**
     * Hàm hiển thị TimePickerDialog
     */
    public void showTimePickerDialogStart()
    {
        TimePickerDialog.OnTimeSetListener callback=new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {
                //Xử lý lưu giờ và AM,PM
                String s=hourOfDay +":"+minute;
                int hourTam=hourOfDay;
                if(hourTam>12)
                    hourTam=hourTam-12;
                giobatdau.setText
                        (hourTam +":"+minute +(hourOfDay>12?" PM":" AM"));
                //lưu giờ thực vào tag
                giobatdau.setTag(s);
                //lưu vết lại giờ vào hourFinish
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                hourStart=cal.getTime();
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong TimePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s=giobatdau.getTag()+"";
        String strArr[]=s.split(":");
        int gio=Integer.parseInt(strArr[0]);
        int phut=Integer.parseInt(strArr[1]);
        TimePickerDialog time=new TimePickerDialog(
                CapNhatCV.this,
                callback, gio, phut, true);
        time.setTitle("Chọn giờ bắt đầu");
        time.show();
    }
    public void showTimePickerDialogFinish()
    {
        TimePickerDialog.OnTimeSetListener callback=new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {
                //Xử lý lưu giờ và AM,PM
                String s=hourOfDay +":"+minute;
                int hourTam=hourOfDay;
                if(hourTam>12)
                    hourTam=hourTam-12;
                giohoanthanh.setText
                        (hourTam +":"+minute +(hourOfDay>12?" PM":" AM"));
                //lưu giờ thực vào tag
                giohoanthanh.setTag(s);
                //lưu vết lại giờ vào hourFinish
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                hourFinish=cal.getTime();
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong TimePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s=giobatdau.getTag()+"";
        String strArr[]=s.split(":");
        int gio=Integer.parseInt(strArr[0]);
        int phut=Integer.parseInt(strArr[1]);
        TimePickerDialog time=new TimePickerDialog(
                CapNhatCV.this,
                callback, gio, phut, true);
        time.setTitle("Chọn giờ hoàn thành");
        time.show();
    }
    //Kiểm tra thời gian
    private boolean checkThoiGianHT(){
        int giobatdausovoithoihientai=hourStart.compareTo(hournow);
        boolean giohtsovoigiobatdau=hourFinish.after(hourStart);
        int ngayhtvoingatbat=dateStart.compareTo(datenow);
        if(ngayhtvoingatbat<0)
            return false;
        if(giohtsovoigiobatdau==false)
            return false;
        if(giobatdausovoithoihientai<=0)
            return false;
        return  true;
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
