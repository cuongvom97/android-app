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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Activity_ThemCongViec extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private TextView ngaybatdau,giobatdau,giohoanthanh,tgthongbao;
    private EditText tieude,ghichu;
    private ImageButton thongbao;
    private Spinner nhanspinner;
    private Button themcv,thoat;
    private CheckBox chklap,chkthongbao;
    private RadioButton radngay,radthang,radtuan;
    private DatabaseReference databaseReference;
    private ArrayAdapter<Nhan> stringArrayAdapterpinner;
    private  List<Nhan> list;
    private String _nhan="";
    private int _thongbao,_cheklaplai;
    Calendar cal,calnow;
    Date dateStart,hourStart,hourFinish;
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
        Toolbar toolbar=findViewById(R.id.toolbar_themcv);
        setSupportActionBar(toolbar);
        list=new ArrayList<>();
        stringArrayAdapterpinner=new CustomeSpinner(this,R.layout.custome_layout_spinner,list);
        stringArrayAdapterpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        nhanspinner.setAdapter(stringArrayAdapterpinner);
        calnow=Calendar.getInstance();
        datenow=calnow.getTime();
        hournow=calnow.getTime();
        radngay.setEnabled(false);
        radtuan.setEnabled(false);
        radthang.setEnabled(false);
        loadDataSpinner();
        getDefaultInfor();
        layEmailTuMain();
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
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
        giohoanthanh.setOnClickListener(this);
        chklap.setOnClickListener(this);
        radngay.setOnClickListener(this);
        radtuan.setOnClickListener(this);
        radthang.setOnClickListener(this);
        thongbao.setOnClickListener(this);
        chkthongbao.setOnClickListener(this);
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
        chklap=findViewById(R.id.themcv_lap);
        radngay=findViewById(R.id.themcv_lap_ngay);
        radthang=findViewById(R.id.themcv_lap_thang);
        radtuan=findViewById(R.id.themcv_lap_tuan);
        thongbao=findViewById(R.id.themcv_btnthongbao);
        tgthongbao=findViewById(R.id.tv_themcv_tgthongbao);
        nhanspinner=findViewById(R.id.themcv_spinner_nhan);
        chkthongbao=findViewById(R.id.themcv_checkthongbao);
        //Dưa dữ liệu vào spinner

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
            case R.id.tv_themcv_gioht:
                showTimePickerDialogFinish();break;
            case R.id.themcv_btnthongbao:
                showDialogTGThongBao();
                break;

        }
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
        if(chkthongbao.isChecked())
            _thongbao=1;
        else
            _thongbao=0;
        if(radngay.isChecked())
            _cheklaplai=0;
        else
            _cheklaplai=-1;
        if(radtuan.isChecked())
            _cheklaplai=1;
        else
            _cheklaplai=-1;
        if(radthang.isChecked())
            _cheklaplai=2;
        else
            _cheklaplai=-1;

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
                    if(_thongbao==1)
                    {
                        //Lặp theo tuần
                        if(_cheklaplai==1){
                            setchuongBao();
                            themCVCSDL();
                        }
                        setchuongBao();
                        themCVCSDL();
                    }
                    else
                    {
                        themCVCSDL();
                    }

                }

        }catch (Exception ex)
        {
            Toast.makeText(this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
        }
    }

    private void setchuongBao() {
        Intent intent=new Intent(this,BaoReceiver.class);
        pendingIntent=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(hourStart);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }

    //Thêm công việc vào csdl
    private void themCVCSDL()
    {
        try {
            String td,gc,nbt,gbt,ght,n;
            td=tieude.getText()+"";
            gc=ghichu.getText()+"";
            nbt=ngaybatdau.getText()+"";
            gbt=giobatdau.getText()+"";
            ght=giohoanthanh.getText()+"";
            n=_nhan;
            CongViec cv=new CongViec(td,gc,_email,nbt,gbt,ght,n,"Chưa hoàn thành");
            databaseReference.child("CongViec").push().setValue(cv);
            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            resetForm();
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
        dateStart=cal.getTime();
        //hiển thị lên giao diện
        ngaybatdau.setText(strDate);
        //Định dạng giờ phút am/pm
        dft=new SimpleDateFormat("hh:mm a",Locale.getDefault());
        cal.add(Calendar.MINUTE,60);
        String strTime=dft.format(cal.getTime());
        hourStart=cal.getTime();
        //đưa lên giao diện
        giobatdau.setText(strTime);
        cal.roll(Calendar.HOUR,1);
        hourFinish=cal.getTime();
        strTime=dft.format(cal.getTime());
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
    private void showDialogTGThongBao()
    {
        LayoutInflater inflater=this.getLayoutInflater();
        View view=inflater.inflate(R.layout.dialogthongbao,null);
        final RadioButton p10,p30,p1h;
        RadioGroup group=view.findViewById(R.id.grthongbao);
        p10= view.findViewById(R.id.themcv_thongbao10p);
        p30= view.findViewById(R.id.themcv_thongbao30p);
        p1h= view.findViewById(R.id.themcv_thongbao1h);
        p10.setChecked(true);
        final String thongbaoxx="";
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Chọn");
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(p10.isChecked())
                {
                    tgthongbao.setText("10 phút");
                }
                if(p30.isChecked())
                {
                    tgthongbao.setText("30 phút");
                }
                if(p1h.isChecked())
                {
                    tgthongbao.setText("60 phút");
                }
                dialog.cancel();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

}
