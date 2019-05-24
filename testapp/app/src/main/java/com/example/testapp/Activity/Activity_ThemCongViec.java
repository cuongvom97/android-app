package com.example.testapp.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.testapp.ReceiverManager.BaoReceiver;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Activity_ThemCongViec extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private TextView ngaybatdau,giobatdau,giohoanthanh,thongbao,laplai;
    private EditText tieude,ghichu;
    private Spinner nhanspinner;
    private Button themcv,thoat;
    private DatabaseReference databaseReference;
    private ArrayAdapter<Nhan> stringArrayAdapterpinner;
    private  List<Nhan> list;
    private List<CongViec> congViecList;
    private String[] arrlap;
    private String[] arrthongbao;
    private String _nhan="",_email="",_ngaybd="",_ngayktlap="",_tieude="",_key="";
    private int _vitrilap,_vitrithongbao;
    Calendar cal,calnow,callap,calthongbao;
    Date dateStart,hourStart,hourFinish,hourthongbao,datefinish;
    Date datenow,hournow;
    //Báo thức
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
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
    private void layEmailTuMain()
    {
        Intent intent=getIntent();
        _email=intent.getStringExtra(Activity_DSCongViec_Ngay.EMAIL_THEMCV_NGAY).toString();
        _ngaybd=intent.getStringExtra(Activity_DSCongViec_Ngay.NGAYBD_THEMCV_NGAY).toString();
        ngaybatdau.setText(_ngaybd+"");
    }
    private void loadUI()
    {
        Toolbar toolbar=findViewById(R.id.toolbar_themcv);
        setSupportActionBar(toolbar);
        //Spinner nhãn
        list=new ArrayList<>();
        stringArrayAdapterpinner=new CustomeSpinner(this,R.layout.custome_layout_spinner,list);
        stringArrayAdapterpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        nhanspinner.setAdapter(stringArrayAdapterpinner);
        arrlap=getResources().getStringArray(R.array.dslapcv);
        arrthongbao=getResources().getStringArray(R.array.dsthongbaocv);
        calnow=Calendar.getInstance();
        datenow=calnow.getTime();
        hournow=calnow.getTime();
        loadDataSpinner();
        layEmailTuMain();
        getDefaultInfor();
        congViecList=new ArrayList<>();
        dsCongViec();
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
    //Gán sự kiện
    private void sukien()
    {
        nhanspinner.setOnItemSelectedListener(this);
        themcv.setOnClickListener(this);
        thoat.setOnClickListener(this);
        ngaybatdau.setOnClickListener(this);
        giobatdau.setOnClickListener(this);
        giohoanthanh.setOnClickListener(this);
        thongbao.setOnClickListener(this);
        laplai.setOnClickListener(this);
    }
    //lấy thể hiện các view từ layout
    private void layTheHien()
    {
        tieude=findViewById(R.id.themcv_tieude);
        ghichu=findViewById(R.id.themcv_ghichu);
        ngaybatdau=findViewById(R.id.tv_themcv_ngaybatdau);
        giobatdau=findViewById(R.id.tv_themcv_giobatdau);
        giohoanthanh=findViewById(R.id.tv_themcv_gioht);
        thoat=findViewById(R.id.themcv_btnthoat);
        themcv=findViewById(R.id.themcv_btnthem);
        laplai=findViewById(R.id.themcv_tvlap);
        thongbao=findViewById(R.id.themcv_tvthongbao);
        nhanspinner=findViewById(R.id.themcv_spinner_nhan);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.themcv_btnthem:
                themCongViec();
                guiActivity_DSCongViec_Ngay(Activity_DSCongViec_Ngay.RESULT_CODETHEM);
                break;
            case R.id.themcv_btnthoat:
                guiActivity_DSCongViec_Ngay(Activity_DSCongViec_Ngay.RESULT_CANCELED);
                break;
            case R.id.tv_themcv_ngaybatdau:
                //showDatePickerDialogStart();
                break;
            case R.id.tv_themcv_giobatdau:
                showTimePickerDialogStart();
                break;
            case R.id.tv_themcv_gioht:
                showTimePickerDialogFinish();
                break;
            case R.id.themcv_tvlap:
                showDialogLap();
            break;
            case R.id.themcv_tvthongbao:
                showDialogThongBao();
                break;

        }

    }

    private void showDialogThongBao() {
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.dialogthongbao,null);
        final RadioButton s1,s2,s3,s4,s5,s6,s7,s8;
        s1=view.findViewById(R.id.themcv_thongbao0);
        s1.setChecked(true);
        s2=view.findViewById(R.id.themcv_thongbao5p);
        s3=view.findViewById(R.id.themcv_thongbao10p);
        s4=view.findViewById(R.id.themcv_thongbao30p);
        s5=view.findViewById(R.id.themcv_thongbao60p);
        s6=view.findViewById(R.id.themcv_thongbao90p);
        s7=view.findViewById(R.id.themcv_thongbao120p);
        s8=view.findViewById(R.id.themcv_thongbaokhac);
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

    private void showDialogLap() {
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_laplai,null);
        final RadioButton s1,s2,s3,s4,s5,s6;
        s1=view.findViewById(R.id.themcv_laplai0);
        s1.setChecked(true);
        s2=view.findViewById(R.id.themcv_laplaingay);
        s3=view.findViewById(R.id.themcv_laplaituan);
        s4=view.findViewById(R.id.themcv_laplai2tuan1lan);
        s5=view.findViewById(R.id.themcv_laplaithang);
        s6=view.findViewById(R.id.themcv_lap_lai_moi_nam);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(s1.isChecked()) {
                    laplai.setText(arrlap[0]);
                    _vitrilap=0;
                }
                if(s2.isChecked()) {
                    laplai.setText(arrlap[1]);
                    showDatePickerDialogLap();
                    _vitrilap=1;
                }
                if(s3.isChecked()) {
                    laplai.setText(arrlap[2]);
                    showDatePickerDialogLap();
                    _vitrilap=2;
                }
                if(s4.isChecked()) {
                    laplai.setText(arrlap[3]);
                    showDatePickerDialogLap();
                    _vitrilap=3;
                }
                if(s5.isChecked()) {
                    laplai.setText(arrlap[4]);
                    showDatePickerDialogLap();
                    _vitrilap=4;
                }
                if (s6.isChecked())
                {
                    laplai.setText(arrlap[5]);
                    showDatePickerDialogLap();
                    _vitrilap=5;
                }
                dialog.cancel();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
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

    //Thêm công việc mới.
    private void themCongViec() {
        try {

                if(checkThoiGianHT()==false)
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Giờ không hợp lệ");
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
                    if(_vitrilap!=0)
                    {

                        if(_vitrilap==1)
                        {
                            long khoangcach=datefinish.getTime()-dateStart.getTime();
                            long kc=TimeUnit.MILLISECONDS.toDays(khoangcach);
                            if (_vitrithongbao!=0)
                            {
                                while (kc>=0)
                                {
                                    themCVCSDL(dateStart);
                                    setchuongBao(hourthongbao);
                                    callap.add(Calendar.DATE,1);
                                    dateStart=callap.getTime();
                                    calthongbao.add(Calendar.DATE,1);
                                    hourthongbao=calthongbao.getTime();
                                    kc--;
                                }
                            }
                            else
                            {
                                while (kc>=0)
                                {
                                    themCVCSDL(dateStart);
                                    callap.add(Calendar.DATE,1);
                                    dateStart=callap.getTime();
                                    kc--;
                                }
                            }
                        }
                        if(_vitrilap==2)
                        {
                            long khoangcach=datefinish.getTime()-dateStart.getTime();
                            long kc=khoangcach/(7*24*60*60*1000);
                            if(kc<0){
                                Toast.makeText(this, "Tuần được chọn phải sau tuần hiện tại", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else{
                                if (_vitrithongbao!=0)
                                {
                                    while (kc>=0)
                                    {
                                        themCVCSDL(dateStart);
                                        setchuongBao(hourthongbao);
                                        callap.add(Calendar.DATE,7);
                                        dateStart=callap.getTime();
                                        calthongbao.add(Calendar.DATE,7);
                                        hourthongbao=calthongbao.getTime();
                                        kc--;
                                    }
                                }
                                else
                                {
                                    while (kc>=0)
                                    {
                                        themCVCSDL(dateStart);
                                        callap.add(Calendar.DATE,7);
                                        dateStart=callap.getTime();
                                        kc--;
                                    }
                                }
                            }
                        }
                        if(_vitrilap==3)
                        {
                            long khoangcach=datefinish.getTime()-dateStart.getTime();
                            long kc=khoangcach/(7*24*60*60*1000);
                            if(kc<0) {
                                Toast.makeText(this, "Tuần được chọn phải sau tuần hiện tại", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else
                            {
                                if (_vitrithongbao!=0)
                                {
                                    while (kc>=0)
                                    {
                                        themCVCSDL(dateStart);
                                        setchuongBao(hourthongbao);
                                        callap.add(Calendar.DATE,21);
                                        dateStart=callap.getTime();
                                        calthongbao.add(Calendar.DATE,21);
                                        hourthongbao=calthongbao.getTime();
                                        kc--;
                                    }
                                }
                                else
                                {
                                    while (kc>=0)
                                    {
                                        themCVCSDL(dateStart);
                                        callap.add(Calendar.DATE,21);
                                        dateStart=callap.getTime();
                                        kc--;
                                    }
                                }
                            }
                        }
                        if(_vitrilap==4)
                        {
                            long khoangcach=datefinish.getTime()-dateStart.getTime();
                            long kc=khoangcach/(4*7*24*60*60*1000);
                            if (kc<0)
                            {
                                Toast.makeText(this, "Tháng được chọn phải sau tháng hiện tại", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else
                            {
                                if (_vitrithongbao!=0)
                                {
                                    while (kc>=0)
                                    {
                                        themCVCSDL(dateStart);
                                        setchuongBao(hourthongbao);
                                        callap.add(Calendar.MONTH,1);
                                        dateStart=callap.getTime();
                                        calthongbao.add(Calendar.MONTH,1);
                                        hourthongbao=calthongbao.getTime();
                                        kc--;
                                    }
                                }
                                else
                                {
                                    while (kc>=0)
                                    {
                                        themCVCSDL(dateStart);
                                        callap.add(Calendar.MONTH,1);
                                        dateStart=callap.getTime();
                                        kc--;
                                    }
                                }
                            }
                        }
                        if(_vitrilap==5)
                        {
                            long khoangcach=datefinish.getTime()-dateStart.getTime();
                            long kc=TimeUnit.MILLISECONDS.toDays(khoangcach);
                            long lap=kc/365;
                            if (kc<0||kc<365)
                            {
                                Toast.makeText(this, "Năm được chọn phải sau năm hiện tại", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else
                            {
                                if (_vitrithongbao!=0)
                                {
                                    while (lap>=0)
                                    {
                                        themCVCSDL(dateStart);
                                        setchuongBao(hourthongbao);
                                        callap.add(Calendar.YEAR,1);
                                        dateStart=callap.getTime();
                                        calthongbao.add(Calendar.YEAR,1);
                                        hourthongbao=calthongbao.getTime();
                                        lap--;
                                    }
                                }
                                else
                                {
                                    while (lap>=0)
                                    {
                                        themCVCSDL(dateStart);
                                        callap.add(Calendar.YEAR,1);
                                        dateStart=callap.getTime();
                                        lap--;
                                    }
                                }
                            }
                        }

                    }
                    else
                    {
                        if(_vitrithongbao!=0)
                        {
                            themCVCSDL(dateStart);
                            setchuongBao(hourthongbao);
                        }
                        else
                        {
                            themCVCSDL(dateStart);
                        }
                    }
                }


        }catch (Exception ex)
        {
            Toast.makeText(this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
        }
    }
    private void setchuongBao(Date date) {
        Intent intent=new Intent(this,BaoReceiver.class);
        pendingIntent=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }

    //Thêm công việc vào csdl
    private void themCVCSDL(Date ngabd)
    {
        try {
            final String td,gc,nbd,gbt,ght,n;
            SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
            nbd=df.format(ngabd);
            td=tieude.getText()+"";
            _tieude=td;
            gc=ghichu.getText()+"";
            gbt=giobatdau.getText()+"";
            ght=giohoanthanh.getText()+"";
            n=_nhan;
            for (int i=0;i<congViecList.size();i++)
            {
                if(congViecList.get(i).getTieude().equals(td)&&congViecList.get(i).getNgaybatdau().equals(nbd))
                {
                    Toast.makeText(this, "Tiêu đề trong cùng một ngày không được trùng nhau", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            CongViec cv=new CongViec(td,gc,_email,nbd,gbt,ght,n,"Chưa hoàn thành",arrthongbao[_vitrithongbao]);
            databaseReference.child("CongViec").push().setValue(cv);
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
    // giờ bắt đầu sẽ sau thời gian hiện tại 10p, giờ kết thúc sau giờ bắt đầu 15p
    public void getDefaultInfor()
    {
        //lấy ngày hiện tại của hệ thống
        cal=Calendar.getInstance();
        callap=Calendar.getInstance();
        calthongbao=Calendar.getInstance();
        String strr[]=_ngaybd.split("/");
        int d=Integer.parseInt(strr[0]);
        int m=Integer.parseInt(strr[1])-1;
        int y=Integer.parseInt(strr[2]);
        cal.set(y,m,d);
        callap.set(y,m,d);
        SimpleDateFormat dft=null;
        //Định dạng giờ phút am/pm
        dft=new SimpleDateFormat("HH:mm",Locale.getDefault());
        cal.add(Calendar.MINUTE,10);
        String strTime=dft.format(cal.getTime());
        hourStart=cal.getTime();
        //đưa lên giao diện
        giobatdau.setText(strTime);
        cal.add(Calendar.MINUTE,15);
        hourFinish=cal.getTime();
        strTime=dft.format(cal.getTime());
        giohoanthanh.setText(strTime);
        //Định dạng ngày / tháng /năm
        dft=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate=dft.format(cal.getTime());
        dateStart=cal.getTime();
        datefinish=cal.getTime();
        //hiển thị lên giao diện
        ngaybatdau.setText(strDate);
        _ngayktlap=strDate;
        //lấy giờ theo 24 để lập trình theo Tag
        dft=new SimpleDateFormat("HH:mm",Locale.getDefault());
        giobatdau.setTag(dft.format(cal.getTime()));
        giohoanthanh.setTag(dft.format(cal.getTime()));
        //gán cal.getTime() cho ngày hoàn thành và giờ hoàn thành


    }
    public void showDatePickerDialogLap()
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
                _ngayktlap=dayOfMonth+"/"+monthOfYear+"/"+year;
                //Lưu vết lại biến ngày hoàn thành
                cal.set(year, monthOfYear, dayOfMonth);
                datefinish=cal.getTime();
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s=_ngayktlap;
        String strArrtmp[]=s.split("/");
        int ngay=Integer.parseInt(strArrtmp[0]);
        int thang=Integer.parseInt(strArrtmp[1])-1;
        int nam=Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic=new DatePickerDialog(
                Activity_ThemCongViec.this,
                callback, nam, thang, ngay);
        pic.setTitle("Chọn ngày kết thúc");
        pic.show();
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
                cal.add(Calendar.MINUTE,10);
                hourStart=cal.getTime();
                SimpleDateFormat df=new SimpleDateFormat("HH:mm",Locale.getDefault());
                giobatdau.setText(df.format(cal.getTime())+"");
                cal.add(Calendar.MINUTE,15);
                hourFinish=cal.getTime();
                giohoanthanh.setText(df.format(cal.getTime())+"");
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

    /**
     * Hàm hiển thị TimePickerDialog
     */
    public void showTimePickerDialogThongbao()
    {
        OnTimeSetListener callback=new OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {
                //Xử lý lưu giờ và AM,PM
                String s=hourOfDay +":"+minute;
                String giotam=hourOfDay+"";
                if(giotam.length()<=1)
                    giotam="0"+giotam;
                String phuttam=minute+"";
                if(phuttam.length()<=1)
                    phuttam="0"+phuttam;
                thongbao.setText(giotam +":"+phuttam);
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
                Activity_ThemCongViec.this,
                callback, gio, phut, true);
        time.setTitle("Chọn giờ bắt đầu");
        time.show();
    }
    public void showTimePickerDialogStart()
    {
        OnTimeSetListener callback=new OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {
                //Xử lý lưu giờ và AM,PM
                String s=hourOfDay +":"+minute;
                String giotam=hourOfDay+"";
                if(giotam.length()<=1)
                    giotam="0"+giotam;
                String phuttam=minute+"";
                if(phuttam.length()<=1)
                    phuttam="0"+phuttam;
                giobatdau.setText(giotam +":"+phuttam);
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
                //Xử lý lưu giờ và AM,PM
                String s=hourOfDay +":"+minute;
                String giotam=hourOfDay+"";
                if(giotam.length()<=1)
                    giotam="0"+giotam;
                String phuttam=minute+"";
                if(phuttam.length()<=1)
                    phuttam="0"+phuttam;
                giohoanthanh.setText(giotam +":"+phuttam);
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
        databaseReference.child("Nhan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    Nhan nhan=data.getValue(Nhan.class);
                    list.add(nhan);
                    stringArrayAdapterpinner.notifyDataSetChanged();
                }
                Nhan nhan=new Nhan("Khác");
                list.add(nhan);
                stringArrayAdapterpinner.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        _nhan=parent.getItemAtPosition(position).toString();
        if (_nhan.equals("Khác"))
        {
            showDialogThem_Nhan();
        }
    }
    private void showDialogThem_Nhan()
    {
        LayoutInflater inflater=getLayoutInflater();
        View v=inflater.inflate(R.layout.dialog_them_nhan,null);
        final EditText them_nhan=v.findViewById(R.id.ed_them_nhan);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Thêm nhãn");
        builder.setView(v);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _nhan=them_nhan.getText()+"";
                Toast.makeText(Activity_ThemCongViec.this, "Thêm nhãn "+_nhan+" thành công", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        
    }
    private void guiActivity_DSCongViec_Ngay(int resultcode)
    {
        Intent intent=new Intent();
        String ngay=ngaybatdau.getText()+"";
        intent.putExtra("ngay_duoc_them",ngay);
        setResult(resultcode,intent);
        finish();
    }
    private void dsCongViec()
    {
        congViecList.clear();
        databaseReference.child("CongViec").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CongViec cv=dataSnapshot.getValue(CongViec.class);
                congViecList.add(cv);
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
}
