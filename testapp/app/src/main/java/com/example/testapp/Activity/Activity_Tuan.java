package com.example.testapp.Activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.Custome.CustomeXemDanhSachCV;
import com.example.testapp.CustomeCalandar.Adapter_Calendar_Tuan;
import com.example.testapp.CustomeCalandar.LuaChonTrongTuan;
import com.example.testapp.Model.CongViec;
import com.example.testapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Activity_Tuan extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView tieude,ngay_start_end;
    private ImageButton next,pre;
    private GridView gridView;
    private ArrayList<String> dsngaytrongtuan;
    private DatabaseReference reference;
    private Calendar cal;
    private Calendar calwstart=Calendar.getInstance();
    private Calendar calwsend=Calendar.getInstance();
    private String _ngaybatdautuan="",_ngaketthuctuan="",_dateselected="",itemselected="",_email="";
    private Adapter_Calendar_Tuan adapter_calendar_tuan;
    private int _day,_month,_year;
    private List<CongViec> listcv_inday;
    private ArrayAdapter<CongViec> adapter_dayselected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__tuan);
        reference=FirebaseDatabase.getInstance().getReference();
        laytheHien();
        loadUI();
        sukien();

    }

    private void laytheHien()
    {
        tieude=findViewById(R.id.tv_tuan);
        ngay_start_end=findViewById(R.id.ngaybdkt_tuan);
        gridView=findViewById(R.id.listcv_tuan);
        pre=findViewById(R.id.ib_prev_tuan);
        next=findViewById(R.id.Ib_next_tuan);
    }

    private void loadUI()
    {
        layEmailtuMain();
        dsngaytrongtuan=new ArrayList<>();
        cal=Calendar.getInstance();
        calwsend=Calendar.getInstance();
        calwstart=Calendar.getInstance();
        setValuesfromcalendar();
        getdayStartEndinWeek();
        adapter_calendar_tuan=new Adapter_Calendar_Tuan(this,dsngaytrongtuan,_email);
        gridView.setAdapter(adapter_calendar_tuan);
        setListNgaytrongtuan();
        tieude.setText("Tuần "+cal.get(Calendar.WEEK_OF_MONTH)+" Tháng "+_month+"/"+_year);
        ngay_start_end.setText(_ngaybatdautuan+"-"+_ngaketthuctuan);
    }
    private void layEmailtuMain()
    {
        _email=getIntent().getStringExtra(MainActivity.EMAIL_THEMCV).toString();
    }
    private void sukien()
    {
        gridView.setOnItemClickListener(this);
        next.setOnClickListener(this);
        pre.setOnClickListener(this);

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
            adapter_calendar_tuan.notifyDataSetChanged();
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ib_prev_tuan:
                clickPre();
                break;
            case R.id.Ib_next_tuan:
                clickNext();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        itemselected=Adapter_Calendar_Tuan.ngaytrongtuan.get(position).toString();
        showDialogDSCV();
    }
    private void clickNext()
    {
        dsngaytrongtuan.clear();
        cal.add(Calendar.WEEK_OF_MONTH,1);
        setValuesfromcalendar();
        getdayStartEndinWeek();
        tieude.setText("Tuần "+cal.get(Calendar.WEEK_OF_MONTH)+" Tháng "+_month+"/"+_year);
        ngay_start_end.setText(_ngaybatdautuan+"-"+_ngaketthuctuan);
        setListNgaytrongtuan();
        adapter_calendar_tuan.notifyDataSetChanged();
    }
    private void clickPre()
    {
        dsngaytrongtuan.clear();
        cal.add(Calendar.WEEK_OF_MONTH,-1);
        setValuesfromcalendar();
        getdayStartEndinWeek();
        tieude.setText("Tuần "+cal.get(Calendar.WEEK_OF_MONTH)+" Tháng "+_month+"/"+_year);
        ngay_start_end.setText(_ngaybatdautuan+"-"+_ngaketthuctuan);
        setListNgaytrongtuan();
        adapter_calendar_tuan.notifyDataSetChanged();
    }
    private void setValuesfromcalendar()
    {
        _day=cal.get(Calendar.DATE);
        String day=_day+"";
        if(day.length()<=1)
            day="0"+day;
        _month=cal.get(Calendar.MONTH)+1;
        String month=_month+"";
        if(month.length()<=1)
            month="0"+month;
        _year=cal.get(Calendar.YEAR);
        _dateselected=day+"/"+month+"/"+_year;
    }
    private void showDialogDSCV()
    {
        listcv_inday=new ArrayList<>();
        LayoutInflater inflater=getLayoutInflater();
        View v=inflater.inflate(R.layout.dialog_dscv_tuan,null);
        ListView listView=v.findViewById(R.id.dialog_dscv_tuan);
        adapter_dayselected=new CustomeXemDanhSachCV(this,R.layout.mycustome_danhsachcongviec,listcv_inday);
        listView.setAdapter(adapter_dayselected);
        getCVinDay();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    private void getCVinDay()
    {
        listcv_inday.clear();
        Query query=reference.child("CongViec").orderByChild("ngabatdauy");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    CongViec cv=data.getValue(CongViec.class);
                    if(cv.getEmail().equals(_email)&&cv.getNgaybatdau().equals(itemselected))
                    {
                        listcv_inday.add(cv);
                        adapter_dayselected.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
