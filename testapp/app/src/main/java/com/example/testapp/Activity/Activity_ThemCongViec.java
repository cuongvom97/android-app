package com.example.testapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;

import com.example.testapp.Custome.CustomeSpinner;
import com.example.testapp.Model.CongViec;
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

public class Activity_ThemCongViec extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private TextView ngaybatdau,ngayhoanthanh,giobatdau,giohoanthanh;
    private EditText tieude,ghichu;
    private ImageButton thongbao;
    private Spinner nhanspinner;
    private Button themcv,thoat;
    private CheckBox chklap;
    private RadioButton radngay,radthang,radtuan;
    private DatabaseReference databaseReference;
    private ArrayAdapter<Nhan> stringArrayAdapterpinner;
    private  List<Nhan> list;
    private String _nhan="";
    Calendar cal,calnow;
    Date dateStart,hourStart,dateFinish,hourFinish;
    Date datenow,hournow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__them_cong_viec);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        layTheHien();
        loadUI();
        //Xử lý ự kiện
        sukien();
    }
    private String _email="",_ngaybd="";
    private void layEmailTuMain()
    {
        Intent intent=getIntent();
        _email=intent.getStringExtra(Activity_DSCongViec_Ngay.EMAIL_THEMCV_NGAY).toString();
        _ngaybd=intent.getStringExtra(Activity_DSCongViec_Ngay.NGAYBD_THEMCV_NGAY).toString();
        ngaybatdau.setText(_ngaybd+"");
    }
    private void loadUI()
    {
        radngay.setEnabled(false);
        radtuan.setEnabled(false);
        radthang.setEnabled(false);
        loadDataSpinner();
        getDefaultInfor();
        //layEmailTuMain();
    }
    private void checkCheckBox()
    {
        radngay.setEnabled(true);
        radtuan.setEnabled(true);
        radthang.setEnabled(true);

    }
    //Gán sự kiện
    private void sukien()
    {
        nhanspinner.setOnItemSelectedListener(this);
        themcv.setOnClickListener(this);
        thoat.setOnClickListener(this);
        ngaybatdau.setOnClickListener(this);
        giobatdau.setOnClickListener(this);
        ngayhoanthanh.setOnClickListener(this);
        giohoanthanh.setOnClickListener(this);
        chklap.setOnClickListener(this);
        radngay.setOnClickListener(this);
        radtuan.setOnClickListener(this);
        radthang.setOnClickListener(this);
        thongbao.setOnClickListener(this);
    }
    //lấy thể hiện các view từ layout
    private void layTheHien()
    {
        tieude=findViewById(R.id.themcv_tieude);
        ghichu=findViewById(R.id.themcv_ghichu);
        ngaybatdau=findViewById(R.id.tv_themcv_ngaybatdau);
        giobatdau=findViewById(R.id.tv_themcv_giobatdau);
        ngayhoanthanh=findViewById(R.id.tv_themcv_ngayht);
        giohoanthanh=findViewById(R.id.tv_themcv_gioht);
        thoat=findViewById(R.id.themcv_btnthoat);
        themcv=findViewById(R.id.themcv_btnthem);
        chklap=findViewById(R.id.themcv_lap);
        radngay=findViewById(R.id.themcv_lap_ngay);
        radthang=findViewById(R.id.themcv_lap_thang);
        radtuan=findViewById(R.id.themcv_lap_tuan);
        thongbao=findViewById(R.id.themcv_btnthongbao);
        nhanspinner=findViewById(R.id.themcv_spinner_nhan);
        //Dưa dữ liệu vào spinner
        list=new ArrayList<>();
        stringArrayAdapterpinner=new CustomeSpinner(this,R.layout.custome_layout_spinner,list);
        stringArrayAdapterpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        nhanspinner.setAdapter(stringArrayAdapterpinner);
        calnow=Calendar.getInstance();
        datenow=calnow.getTime();
        hournow=calnow.getTime();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.themcv_btnthem:
                themCongViec();
                break;
            case R.id.themcv_btnthoat:
                guiActivity_DSCongViec_Ngay(Activity_DSCongViec_Ngay.RESULT_CODETHEM);
                break;
            case R.id.tv_themcv_ngaybatdau:
                showDatePickerDialogStart();break;
            case R.id.tv_themcv_giobatdau:
                showTimePickerDialogStart();break;
            case R.id.tv_themcv_ngayht:
                showDatePickerDialogFinish();break;
            case R.id.tv_themcv_gioht:
                showTimePickerDialogFinish();break;
            case R.id.themcv_lap:
                if(chklap.isChecked())
                {
                    checkCheckBox();

                }
                else
                {
                    radngay.setEnabled(false);
                    radtuan.setEnabled(false);
                    radthang.setEnabled(false);
                    radngay.setChecked(false);
                    radtuan.setChecked(false);
                    radthang.setChecked(false);
                }
                break;
        }


    }
    //Kiểm tra thời gian
    private boolean checkThoiGianHT(){
        int trungngay=dateStart.compareTo(dateFinish);
        int giobatdausovoithoihientai=hourStart.compareTo(hournow);
        int giohttrunggiobt=hourFinish.compareTo(hourStart);
        boolean giohtsovoigiobatdau=hourFinish.after(hourStart);
        if(trungngay==0){
                if(giobatdausovoithoihientai<0)
                    return false;
                else
                {
                    if (giohtsovoigiobatdau==false)
                        return false;
                    return true;
                }

            }
            else
        {
            if(trungngay<0)
            {
                if(giobatdausovoithoihientai<0)
                    return false;
                else {
                    return true;
                }
            }
            else {
                if(trungngay>0)
                    return false;
        }
        }
        return  true;
    }

    //Thêm công việc mới.
    private void themCongViec() {
        try {

            String td,gc,nbt,gbt,nht,ght,n,email;
            td=tieude.getText()+"";
            gc=ghichu.getText()+"";
            nbt=ngaybatdau.getText()+"";
            gbt=giobatdau.getText()+"";
            nht=ngayhoanthanh.getText()+"";
            ght=giohoanthanh.getText()+"";
            n=_nhan;
                if(checkThoiGianHT()==false)
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Thời gian hoàn thành phải sau thời gian bắt đầu.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog=builder.create();
                    dialog.show();
                }
                else
                {
                    CongViec cv=new CongViec(td,gc,_email,nbt,nht,gbt,ght,n,"Chưa hoàn thành");
                    databaseReference.child("CongViec").push().setValue(cv);
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    resetForm();
                }

        }catch (Exception ex)
        {
            Toast.makeText(this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetForm() {
        tieude.setText("");
        tieude.requestFocus();
        ghichu.setText("");
        getDefaultInfor();
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
        ngayhoanthanh.setText(strDate);
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
        dateFinish=cal.getTime();
        hourFinish=cal.getTime();
        dateStart=cal.getTime();
        hourStart=cal.getTime();
    }
    /**
     * Hàm hiển thị DatePicker dialog
     */
    public void showDatePickerDialogStart()
    {
        OnDateSetListener callback=new OnDateSetListener() {
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
                Activity_ThemCongViec.this,
                callback, nam, thang, ngay);
        pic.setTitle("Chọn ngày bắt đầu");
        pic.show();
    }
    public void showDatePickerDialogFinish()
    {
        OnDateSetListener callback=new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                //Mỗi lần thay đổi ngày tháng năm thì cập nhật lại TextView Date
                String thang=(monthOfYear+1)+"";
                if(thang.length()<=1)
                {
                    thang="0"+thang;
                }
                ngayhoanthanh.setText(
                        (dayOfMonth) +"/"+thang+"/"+year);
                //Lưu vết lại biến ngày hoàn thành
                cal.set(year, monthOfYear, dayOfMonth);
                dateFinish=cal.getTime();
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s=ngayhoanthanh.getText()+"";
        String strArrtmp[]=s.split("/");
        int ngay=Integer.parseInt(strArrtmp[0]);
        int thang=Integer.parseInt(strArrtmp[1])-1;
        int nam=Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic=new DatePickerDialog(
                Activity_ThemCongViec.this,
                callback, nam, thang, ngay);
        pic.setTitle("Chọn ngày hoàn thành");
        pic.show();
    }
    /**
     * Hàm hiển thị TimePickerDialog
     */
    public void showTimePickerDialogStart()
    {
        OnTimeSetListener callback=new OnTimeSetListener() {
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
                Activity_ThemCongViec.this,
                callback, gio, phut, true);
        time.setTitle("Chọn giờ bắt đầu");
        time.show();
    }
    public void showTimePickerDialogFinish()
    {
        OnTimeSetListener callback=new OnTimeSetListener() {
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
                Activity_ThemCongViec.this,
                callback, gio, phut, true);
        time.setTitle("Chọn giờ hoàn thành");
        time.show();
    }
    public void loadDataSpinner()
    {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        this._nhan=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        
    }
    private void guiActivity_DSCongViec_Ngay(int resultcode)
    {
        Intent intent=getIntent();
        setResult(resultcode,intent);
        finish();
    }
}
