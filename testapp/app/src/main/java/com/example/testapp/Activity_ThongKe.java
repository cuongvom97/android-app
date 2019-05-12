package com.example.testapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.Activity.Activity_ThemCongViec;
import com.example.testapp.Model.CongViec;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Activity_ThongKe extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, OnChartValueSelectedListener {
    private PieChart pieChart;
    private TextView tieude;
    private ImageButton next,pre;
    private Spinner spinner;
    private static String arrthongke[];
    private Calendar cal;
    private Date _date;
    private DatabaseReference databaseReference;
    private List<CongViec> listcvngay;
    private List<CongViec> listcvthang;
    private int _day,_month,_year,_demht,_demcht;
    private static String _dateselected="";
    private String _selectedspinner="Ngày";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity__thong_ke);
        layTheHien();
        loadUI();
        sukien();
    }
    @SuppressLint("WrongViewCast")
    private void layTheHien()
    {
        pieChart=(PieChart) findViewById(R.id.piechart);
        spinner=findViewById(R.id.thongke_spinner);
        tieude=findViewById(R.id.ngay_ngayhientai_thongke);
        next=findViewById(R.id.ib_next_thongke);
        pre=findViewById(R.id.ib_prev_thongke);
    }
    private void loadUI()
    {
        listcvngay=new ArrayList<>();
        listcvthang=new ArrayList<>();
        arrthongke=getResources().getStringArray(R.array.dsthongke);
        ArrayAdapter arrayAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arrthongke);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);
        //Piechart
        pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setRotationEnabled(true);
        pieChart.setDescription(new Description());
        pieChart.setHoleRadius(35f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("% Công việc");
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(true);
        setDefaultInfor();
    }
    private void sukien()
    {
        spinner.setOnItemSelectedListener(this);
        next.setOnClickListener(this);
        pre.setOnClickListener(this);
        pieChart.setOnChartValueSelectedListener(this);
    }
    private void setDefaultInfor()
    {
        cal=Calendar.getInstance();
        _date=cal.getTime();
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
        tieude.setText(_dateselected);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        _selectedspinner=parent.getItemAtPosition(position).toString();
        if(_selectedspinner.equals(arrthongke[0]))
        {
            tieude.setText(_dateselected);
            resetPiechart();
        }
        else
            if(_selectedspinner.equals(arrthongke[2]))
            {
                tieude.setText(_month+"/"+_year);
                resetPiechart();
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void getListCV()
    {
            databaseReference.child("CongViec").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    CongViec cv=dataSnapshot.getValue(CongViec.class);
                    if(_selectedspinner.equals(arrthongke[0]))
                    {
                        if(cv.getNgaybatdau().equals(_dateselected)&&cv.getEmail().equals("cuongvo077@gmail.com"))
                        {
                            listcvngay.add(cv);
                            if(cv.getTrangthai().equals("Hoàn thành"))
                                _demht++;
                            else
                                _demcht++;
                            Toast.makeText(Activity_ThongKe.this, ""+cv.getTieude(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        if (_selectedspinner.equals(arrthongke[2]))
                        {
                            int month=stringSplitMonth(_dateselected);
                            int year=stringSplitYear(_dateselected);
                            if(month==stringSplitMonth(cv.getNgaybatdau())&&year==stringSplitYear(cv.getNgaybatdau())&&cv.getEmail().equals("cuongvo077@gmail.com"))
                            {
                                listcvthang.add(cv);
                                if(cv.getTrangthai().equals("Hoàn thành"))
                                    _demht++;
                                else
                                    _demcht++;
                                Toast.makeText(Activity_ThongKe.this, ""+cv.getTieude(), Toast.LENGTH_SHORT).show();
                            }
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
    public void showDatePickerDialog()
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
                _dateselected=(dayOfMonth) +"/"+thang+"/"+year;
                //Lưu vết lại biến ngày hoàn thành
                cal.set(year, monthOfYear, dayOfMonth);
                _date=cal.getTime();
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s=_dateselected;
        String strArrtmp[]=s.split("/");
        int ngay=Integer.parseInt(strArrtmp[0]);
        int thang=Integer.parseInt(strArrtmp[1])-1;
        int nam=Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic=new DatePickerDialog(
                Activity_ThongKe.this,
                callback, nam, thang, ngay);
        pic.setTitle("Chọn thời gian");
        pic.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ib_next_thongke:
                clickNext();
                break;
            case R.id.ib_prev_thongke:
                clickPre();
                break;

        }
    }
    private void clickNext()
    {
        if(_selectedspinner.equals(arrthongke[0]))
        {
            cal.add(Calendar.DATE,1);
            _date=cal.getTime();
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
            tieude.setText(_dateselected);
            resetPiechart();
        }
        else
            if(_selectedspinner.equals(arrthongke[2]))
            {
                cal.add(Calendar.MONTH,1);
                _date=cal.getTime();
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
                tieude.setText(_month+"/"+_year);
                resetPiechart();
            }
            else
            {

            }
    }
    private void clickPre()
    {
        if(_selectedspinner.equals(arrthongke[0]))
        {
            cal.add(Calendar.DATE,-1);
            _date=cal.getTime();
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
            tieude.setText(_dateselected);
            resetPiechart();
        }
        else
        if(_selectedspinner.equals(arrthongke[2]))
        {
            cal.add(Calendar.MONTH,-1);
            _date=cal.getTime();
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
            tieude.setText(_month+"/"+_year);
            resetPiechart();
        }
        else
        {

        }
    }
    private void addDataSet(PieChart pieChart) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        float tyleht=0,tylecht = 0;

        if(_selectedspinner.equals(arrthongke[0]))
        {
            if(listcvngay.size()!=0)
            {
                int tongcv=_demht+_demcht;
                tyleht=(float)(_demht/tongcv)*100;
                tylecht=(float)(_demcht/tongcv)*100;
            }
        }

        float[] yData = { tylecht,tyleht };
        String[] xData = { "Hoàn thành","Chưa hoàn thành" };

        for (int i = 0; i < yData.length;i++){
            yEntrys.add(new PieEntry(yData[i],i));
        }
        for (int i = 0; i < xData.length;i++){
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet=new PieDataSet(yEntrys,"Trạng thái công việc");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors=new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        pieDataSet.setColors(colors);

        Legend legend=pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
    private void resetPiechart()
    {
        _demcht=0;
        _demht=0;
        listcvthang.clear();
        listcvngay.clear();
        getListCV();
        addDataSet(pieChart);
    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
