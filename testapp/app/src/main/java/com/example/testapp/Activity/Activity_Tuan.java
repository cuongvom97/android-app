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
import java.util.List;
import java.util.Locale;

public class Activity_Tuan extends AppCompatActivity {
    public GregorianCalendar cal_week, cal_week_copy;
    private TextView tieude;
    private GridView gridView;
    private ArrayList<String> dsngaytrongtuan;
    private DatabaseReference reference;
    private Calendar cal;
    private Calendar calwstart=Calendar.getInstance();
    private Calendar calwsend=Calendar.getInstance();
    private String _ngaybatdautuan="",_ngaketthuctuan="";
    private Adapter_Calendar_Tuan adapter_calendar_tuan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__tuan);
        reference=FirebaseDatabase.getInstance().getReference();
        reference= FirebaseDatabase.getInstance().getReference();
        laytheHien();
        loadUI();
        sukien();

    }

    private void laytheHien()
    {

    }

    private void loadUI()
    {
        dsngaytrongtuan=new ArrayList<>();
        cal=Calendar.getInstance();
        getdayStartEndinWeek();
        setListNgaytrongtuan();
        adapter_calendar_tuan=new Adapter_Calendar_Tuan(this,cal,dsngaytrongtuan,"cuongvo077@gmail.com");
        gridView.setAdapter(adapter_calendar_tuan);
    }

    private void sukien()
    {
        tieude=findViewById(R.id.tieude_tuan_ngay);
        gridView=findViewById(R.id.listcv_tuan);

    }
    private void getdayStartEndinWeek()
    {
        calwstart.setTime(cal.getTime());
        calwsend.setTime(cal.getTime());
        int daynow=calwstart.get(Calendar.DAY_OF_WEEK);
        SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        _ngaybatdautuan=df.format(calwstart.getTime());
        _ngaketthuctuan=df.format(calwsend.getTime());
        switch (daynow)
        {
            case Calendar.MONDAY:
                calwsend.add(Calendar.DATE,5);
                _ngaketthuctuan=df.format(calwsend.getTime());
                calwstart.add(Calendar.DATE,-1);
                _ngaybatdautuan=df.format(calwstart.getTime());
                break;
            case Calendar.TUESDAY:
                calwsend.add(Calendar.DATE,4);
                _ngaketthuctuan=df.format(calwsend.getTime());
                calwstart.add(Calendar.DATE,-2);
                _ngaybatdautuan=df.format(calwstart.getTime());
                break;
            case Calendar.WEDNESDAY:
                calwsend.add(Calendar.DATE,3);
                _ngaketthuctuan=df.format(calwsend.getTime());
                calwstart.add(Calendar.DATE,-3);
                _ngaybatdautuan=df.format(calwstart.getTime());
                break;
            case Calendar.THURSDAY:
                calwsend.add(Calendar.DATE,2);
                _ngaketthuctuan=df.format(calwsend.getTime());
                calwstart.add(Calendar.DATE,-4);
                _ngaybatdautuan=df.format(calwstart.getTime());
                break;
            case Calendar.FRIDAY:
                calwsend.add(Calendar.DATE,1);
                _ngaketthuctuan=df.format(calwsend.getTime());
                calwstart.add(Calendar.DATE,-5);
                _ngaybatdautuan=df.format(calwstart.getTime());
                break;
            case Calendar.SATURDAY:
                calwstart.add(Calendar.DATE,-6);
                _ngaybatdautuan=df.format(calwstart.getTime());
                break;
            case Calendar.SUNDAY:
                calwsend.add(Calendar.DATE,6);
                _ngaketthuctuan=df.format(calwsend.getTime());
                break;
        }
    }
    private void setListNgaytrongtuan()
    {
        dsngaytrongtuan.clear();
        SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        String ngay;
        int d=stringSplitDay(_ngaybatdautuan);
        int m=stringSplitMonth(_ngaybatdautuan)-1;
        int y=stringSplitYear(_ngaybatdautuan);
        calwstart.set(y,m,d);
        Date datestart=calwstart.getTime();
        Date dateend=calwsend.getTime();
        int sosanh=datestart.compareTo(dateend);
        while (sosanh<=0)
        {
            ngay=df.format(calwstart.getTime());
            dsngaytrongtuan.add(ngay);
            calwstart.add(Calendar.DATE,1);
            datestart=calwstart.getTime();
            sosanh=datestart.compareTo(dateend);
        }
    }
    private int stringSplitDay(String value)
    {
        String arsplit[]=value.split("/");
        int day=Integer.parseInt(arsplit[0]);
        return day;
    }
    private int stringSplitMonth(String value)
    {
        String arsplit[]=value.split("/");
        int month=Integer.parseInt(arsplit[1]);
        return month;
    }
    private int stringSplitYear(String value)
    {
        String arsplit[]=value.split("/");
        int year=Integer.parseInt(arsplit[2]);
        return year;
    }
}
