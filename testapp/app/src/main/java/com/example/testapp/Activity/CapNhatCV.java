package com.example.testapp.Activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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

import com.example.testapp.Custome.CustomeSpinner;
import com.example.testapp.Model.CongViec;
import com.example.testapp.Model.Nhan;
import com.example.testapp.R;
import com.example.testapp.ReceiverManager.BaoReceiver;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CapNhatCV extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private TextView ngaybatdau,giobatdau,giohoanthanh,trangthai,thongbao;
    private EditText tieude,ghichu;
    private RadioButton s1,s2,s3,s4,s5,s6,s7,s8;
    private View view;
    private Spinner nhanspinner;
    private Button capnhatcv,thoat;
    private int _vitrithongbao;
    private String[] arrthongbao;
    private static String _key="",_nhan="",_email="",_gio="";
    private DatabaseReference databaseReference;
    private ArrayAdapter<Nhan> stringArrayAdapterpinner;
    private List<Nhan> list;
    Calendar cal,calnow,callap,calthongbao,_calgiobd,_calgioht;
    Date dateStart,hourStart,hourFinish,hourthongbao;
    Date datenow,hournow;
    //Báo thức
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
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
        capnhatcv=findViewById(R.id.capnhatcv_btncapnhat);
        nhanspinner=findViewById(R.id.capnhatcv_spinner_nhan);
        trangthai=findViewById(R.id.tv_capnhatcv_trangthai);
        thongbao=findViewById(R.id.capnhatcv_tvthongbao);
        LayoutInflater inflater=getLayoutInflater();
        view=inflater.inflate(R.layout.dialogthongbao,null);
        s1=view.findViewById(R.id.themcv_thongbao0);
        s1.setChecked(true);
        s2=view.findViewById(R.id.themcv_thongbao5p);
        s3=view.findViewById(R.id.themcv_thongbao10p);
        s4=view.findViewById(R.id.themcv_thongbao30p);
        s5=view.findViewById(R.id.themcv_thongbao60p);
        s6=view.findViewById(R.id.themcv_thongbao90p);
        s7=view.findViewById(R.id.themcv_thongbao120p);
        s8=view.findViewById(R.id.themcv_thongbaokhac);
    }
    private void loadUI(){
        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_capnhatcv);
        setSupportActionBar(toolbar);
        list=new ArrayList<>();
        stringArrayAdapterpinner=new CustomeSpinner(this,R.layout.custome_layout_spinner,list);
        stringArrayAdapterpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        nhanspinner.setAdapter(stringArrayAdapterpinner);
        calnow= Calendar.getInstance();
        datenow=calnow.getTime();
        hournow=calnow.getTime();
        arrthongbao=getResources().getStringArray(R.array.dsthongbaocv);
        loadDataSpinner();
        layKeyCapNhat();
        getDefaultInfor();
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        gangiatriNhacNho();
    }
    private void gangiatriNhacNho()
    {
        for(int i=0;i<arrthongbao.length;i++)
        {
            if(arrthongbao[i].equals(_gio))
                _vitrithongbao=i;
        }
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
        });
    }

    private void sukien(){
        nhanspinner.setOnItemSelectedListener(this);
        capnhatcv.setOnClickListener(this);
        thoat.setOnClickListener(this);
        ngaybatdau.setOnClickListener(this);
        giobatdau.setOnClickListener(this);
        giohoanthanh.setOnClickListener(this);
        trangthai.setOnClickListener(this);
        thongbao.setOnClickListener(this);

    }
    private void layKeyCapNhat()
    {
        try{
            _email=getIntent().getStringExtra(Activity_DSCongViec_Ngay.EMAIL_CAPNHATCV_NGAY).toString();
            _key=getIntent().getStringExtra(Activity_DSCongViec_Ngay.KEYCN).toString();
            Bundle bundle=getIntent().getExtras();
            CongViec cv= (CongViec) bundle.getSerializable(Activity_DSCongViec_Ngay.CVCAPNHAT);
            tieude.setText(cv.getTieude()+"");
            trangthai.setText(cv.getTrangthai()+"");
            ghichu.setText(cv.getGhichu()+"");
            ngaybatdau.setText(cv.getNgaybatdau()+"");
            giobatdau.setText(cv.getGiobatdau()+"");
            giohoanthanh.setText(cv.getGioketthuc()+"");
            thongbao.setText(cv.getNhacnho());
            _gio=thongbao.getText()+"";
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Lỗi hệ thống.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_capnhatcv_trangthai:
                String tt=trangthai.getText()+"";
                if(tt.equals("Chưa hoàn thành"))
                {
                    trangthai.setText("Hoàn thành");
                }
                else
                    trangthai.setText("Chưa hoàn thành");
                break;
            case R.id.tv_capnhatcv_ngaybatdau:
                showDatePickerDialogStart();
                break;
            case R.id.tv_capnhatcv_giobatdau:
                showTimePickerDialogStart();
                break;
            case R.id.tv_capnhatcv_gioht:
                showTimePickerDialogFinish();
                break;
            case R.id.capnhatcv_btncapnhat:
                capnhatCV();
                break;
            case R.id.capnhatcv_tvthongbao:
                showDialogThongBao();
                break;
            case R.id.capnhatcv_btnthoat:
                finish();
                break;
        }

    }

    private void showDialogThongBao() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(s1.isChecked())
                {
                    thongbao.setText(arrthongbao[0]);
                    calthongbao.setTime(hourStart);
                    hourthongbao=calthongbao.getTime();
                    _vitrithongbao=0;
                }
                if(s2.isChecked())
                {
                    thongbao.setText(arrthongbao[1]);
                    calthongbao.setTime(hourStart);
                    calthongbao.add(Calendar.MINUTE,-5);
                    hourthongbao=calthongbao.getTime();
                    _vitrithongbao=1;
                }
                if(s3.isChecked())
                {
                    thongbao.setText(arrthongbao[2]);
                    calthongbao.setTime(hourStart);
                    calthongbao.add(Calendar.MINUTE,-10);
                    hourthongbao=calthongbao.getTime();
                    _vitrithongbao=2;
                }
                if(s4.isChecked())
                {
                    thongbao.setText(arrthongbao[3]);
                    calthongbao.setTime(hourStart);
                    calthongbao.add(Calendar.MINUTE,-30);
                    hourthongbao=calthongbao.getTime();
                    _vitrithongbao=3;
                }
                if(s5.isChecked())
                {
                    thongbao.setText(arrthongbao[4]);
                    calthongbao.setTime(hourStart);
                    calthongbao.add(Calendar.MINUTE,-60);
                    hourthongbao=calthongbao.getTime();
                    _vitrithongbao=4;
                }
                if(s6.isChecked())
                {
                    thongbao.setText(arrthongbao[5]);
                    calthongbao.setTime(hourStart);
                    calthongbao.add(Calendar.MINUTE,-90);
                    hourthongbao=calthongbao.getTime();
                    _vitrithongbao=5;
                }
                if(s7.isChecked())
                {
                    thongbao.setText(arrthongbao[6]);
                    calthongbao.setTime(hourStart);
                    calthongbao.add(Calendar.MINUTE,-120);
                    hourthongbao=calthongbao.getTime();
                    _vitrithongbao=6;
                }
                if(s8.isChecked())
                {
                    calthongbao.setTime(hourStart);
                    showTimePickerDialogThongbao();
                    _vitrithongbao=7;
                }
                dialog.cancel();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    private void capnhatCV() {
        try{
            if(checkThoiGianHT()==false)
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Thông báo");
                builder.setMessage("Giờ không hợp lệ.");
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
                if(_vitrithongbao!=0)
                {
                    capnhatCVCSDL(dateStart);
                    setchuongBao(hourthongbao);
                    finish();
                }
                else
                {
                    capnhatCVCSDL(dateStart);
                    finish();
                }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Lỗi hệ thống", Toast.LENGTH_SHORT).show();
        }
    }
    private void setchuongBao(Date tb) {
        Intent intent=new Intent(this,BaoReceiver.class);
        pendingIntent=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(tb);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }
    private void capnhatCVCSDL(Date n) {
        final String td,gc,nbd,gbd,ght,tt;
        SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        nbd=df.format(n);
        td=tieude.getText()+"";
        gc=ghichu.getText()+"";
        gbd=giobatdau.getText()+"";
        ght=giohoanthanh.getText()+"";
        tt=trangthai.getText()+"";
        databaseReference.child("CongViec").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    CongViec cv=dataSnapshot.getValue(CongViec.class);
                    String key=dataSnapshot.getKey();
                    if(key.equals(_key)){
                        CongViec congViec=new CongViec(td,gc,_email,nbd,gbd,ght,_nhan,tt,arrthongbao[_vitrithongbao]);
                        Map<String,Object> values=congViec.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/CongViec/"+key,values);
                        databaseReference.updateChildren(childUpdates);
                        Toast.makeText(CapNhatCV.this, "Thành công.", Toast.LENGTH_SHORT).show();
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
        setResult(Activity_DSCongViec_Ngay.RESULT_CODECAPNHAT);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this._nhan=parent.getItemAtPosition(position).toString();
    }
    /**
     * Hàm lấy các thông số mặc định khi lần đầu tiền chạy ứng dụng
     */
    public void getDefaultInfor()
    {
        //lấy ngày hiện tại của hệ thống
        cal=Calendar.getInstance();
        callap=Calendar.getInstance();
        calthongbao=Calendar.getInstance();
        _calgioht=Calendar.getInstance();
        _calgiobd=Calendar.getInstance();
        String s=ngaybatdau.getText()+"";
        String strr[]=s.split("/");
        int d=Integer.parseInt(strr[0]);
        int m=Integer.parseInt(strr[1])-1;
        int y=Integer.parseInt(strr[2]);
        SimpleDateFormat dft=null;
        //Định dạng ngày / tháng /năm
        dft=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate=dft.format(cal.getTime());
        //Định dạng giờ phút am/pm
        dft=new SimpleDateFormat("hh:mm a",Locale.getDefault());
        String strgiobd=giobatdau.getText()+"";
        String strrgiobd[]=strgiobd.split(":");
        int hbd=Integer.parseInt(strrgiobd[0]);
        String tach=strrgiobd[1];
        String ppbd[]=tach.split(" ");
        int pbd=Integer.parseInt(ppbd[0]);
        cal.set(y,m,d,hbd,pbd);
        _calgiobd.setTime(cal.getTime());
        String muigiobd=ppbd[1];
        if(muigiobd.equals("PM"))
            hbd=hbd+12;
        _calgiobd.set(y,m,d,hbd,pbd);
        String strTime=dft.format(_calgiobd.getTime());
        giobatdau.setText(strTime);
        dateStart=_calgiobd.getTime();
        hourStart=_calgiobd.getTime();
        String strgioht=giohoanthanh.getText()+"";
        String strrgioht[]=strgioht.split(":");
        int hht=Integer.parseInt(strrgioht[0]);
        String tach1=strrgioht[1];
        String ppbd1[]=tach1.split(" ");
        int pht=Integer.parseInt(ppbd1[0]);
        cal.set(y,m,d,hht,pht);
        _calgioht.setTime(cal.getTime());
        String muigioht=ppbd1[1];
        if(muigioht.equals("PM"))
            hht=hht+12;
        _calgioht.set(y,m,d,hht,pht);
        hourFinish=_calgioht.getTime();
        strTime=dft.format(_calgioht.getTime());
        giohoanthanh.setText(strTime);
        //lấy giờ theo 24 để lập trình theo Tag
        dft=new SimpleDateFormat("HH:mm",Locale.getDefault());
        giobatdau.setTag(dft.format(cal.getTime()));
        giohoanthanh.setTag(dft.format(cal.getTime()));
        //gán cal.getTime() cho ngày hoàn thành và giờ hoàn thành



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
    public void showTimePickerDialogThongbao()
    {
        TimePickerDialog.OnTimeSetListener callback=new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {
                //Xử lý lưu giờ và AM,PM
                String s=hourOfDay +":"+minute;
                int hourTam=hourOfDay;
                if(hourTam>12)
                    hourTam=hourTam-12;
                String giotam=hourTam+"";
                if(giotam.length()<=1)
                    giotam="0"+giotam;
                String phutam=minute+"";
                if(phutam.length()<=1)
                    phutam="0"+phutam;
                thongbao.setText
                        (giotam +":"+minute +(hourOfDay>12?" PM":" AM"));
                //lưu giờ thực vào tag
                thongbao.setTag(s);
                //lưu vết lại giờ vào hourFinish
                calthongbao.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calthongbao.set(Calendar.MINUTE, minute);
                hourthongbao=cal.getTime();
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
                String giotam=hourTam+"";
                if(giotam.length()<=1)
                    giotam="0"+giotam;
                String phutam=minute+"";
                if(phutam.length()<=1)
                    phutam="0"+phutam;
                giobatdau.setText
                        (giotam +":"+phutam +(hourOfDay>12?" PM":" AM"));
                //lưu giờ thực vào tag
                giobatdau.setTag(s);
                //lưu vết lại giờ vào hourFinish
                _calgiobd.set(Calendar.HOUR_OF_DAY, hourOfDay);
                _calgiobd.set(Calendar.MINUTE, minute);
                hourStart=_calgiobd.getTime();
                dateStart=_calgiobd.getTime();
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
                String giotam=hourTam+"";
                if(giotam.length()<=1)
                    giotam="0"+giotam;
                String phutam=minute+"";
                if(phutam.length()<=1)
                    phutam="0"+phutam;
                giohoanthanh.setText
                        (giotam +":"+phutam +(hourOfDay>12?" PM":" AM"));
                //lưu giờ thực vào tag
                giohoanthanh.setTag(s);
                //lưu vết lại giờ vào hourFinish
                _calgioht.set(Calendar.HOUR_OF_DAY, hourOfDay);
                _calgioht.set(Calendar.MINUTE, minute);
                hourFinish=_calgioht.getTime();
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
