package com.example.testapp.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.testapp.Model.CongViec;
import com.example.testapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class Activity_ThongKe extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, OnChartValueSelectedListener {
    private PieChart pieChart;
    private LineChartView lineChart;
    private TextView tieude,tongcv,loikhen;
    private ImageButton next,pre;
    private Spinner spinner;
    private static String arrthongke[];
    private Calendar cal;
    private Date _date;
    private DatabaseReference databaseReference;
    private List<CongViec> listcvngay,listcvtuan;
    private List<CongViec> listcvthang;
    private int _day,_month,_year,_demht=0,_demcht=0;
    private String _dateselected="";
    private String _selectedspinner="Ngày",_ngaybatdautuan="",_ngaketthuctuan="",_email="";
    private LinearLayout linechart,piechart;
    private List<String> ngaytrongtuan;
    private Calendar calwstart=Calendar.getInstance();
    private Calendar calwsend=Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__thong_ke);
        databaseReference= FirebaseDatabase.getInstance().getReference();
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
        tongcv=findViewById(R.id.thongke_tongcv);
        loikhen=findViewById(R.id.thongke_loikhen);
        linechart=findViewById(R.id.layout_thongke_thang);
        piechart=findViewById(R.id.layout_thongke_ngay);
        pieChart = (PieChart) findViewById(R.id.piechart);
        lineChart=findViewById(R.id.thongke_linechart);
    }
    private void loadUI()
    {
        listcvngay=new ArrayList<>();
        listcvtuan=new ArrayList<>();
        listcvthang=new ArrayList<>();
        ngaytrongtuan=new ArrayList<>();
        arrthongke=getResources().getStringArray(R.array.dsthongke);
        ArrayAdapter arrayAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arrthongke);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);
        //Piechart
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(35f);
        pieChart.setDescription(new Description());
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("% Công việc");
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(true);
        //Linechart

        setDefaultInfor();
        getdayStartEndinWeek();
        layemail();
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
            linechart.setVisibility(View.GONE);
            piechart.setVisibility(View.VISIBLE);
            tieude.setText(_dateselected);
            getListCV();
        }
        else
            if(_selectedspinner.equals(arrthongke[2]))
            {
                linechart.setVisibility(View.VISIBLE);
                piechart.setVisibility(View.GONE);
                tieude.setText(_month+"/"+_year);
                getListCV();
            }
            else
            {
                linechart.setVisibility(View.GONE);
                piechart.setVisibility(View.VISIBLE);
                tieude.setText("Tuần "+cal.get(Calendar.WEEK_OF_MONTH)+" Tháng "+_month+"/"+_year);
                getListCV();
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void getListCV()
    {
        _demht=0;
        _demcht=0;
        databaseReference.child("CongViec").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(_selectedspinner.equals(arrthongke[0]))
                {
                    listcvngay.clear();
                    for(DataSnapshot data:dataSnapshot.getChildren())
                    {
                        CongViec cv=data.getValue(CongViec.class);
                        if(cv.getNgaybatdau().equals(_dateselected)
                                &&cv.getEmail().equals(_email))
                        {
                            listcvngay.add(cv);
                            if(cv.getTrangthai().equals("Hoàn thành"))
                                _demht++;
                            else
                                _demcht++;
                        }
                    }
                    addDataSetPiechart(pieChart);
                }
                else
                    if(_selectedspinner.equals(arrthongke[2]))
                    {
                        listcvthang.clear();
                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            CongViec cv=data.getValue(CongViec.class);
                            if(_month==stringSplitMonth(cv.getNgaybatdau())
                                    &&_year==stringSplitYear(cv.getNgaybatdau())
                                    &&cv.getEmail().equals(_email))
                            {
                                listcvthang.add(cv);
                            }
                        }
                        addDataSetLinechart(lineChart);
                    }
                    else
                    {
                        listcvtuan.clear();
                        setListNgaytrongtuan();
                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            CongViec cv=data.getValue(CongViec.class);
                            for(String x:ngaytrongtuan)
                            {
                                if(cv.getNgaybatdau().equals(x)&&cv.getEmail().equals(_email))
                                {
                                    listcvtuan.add(cv);
                                    if(cv.getTrangthai().equals("Chưa hoàn thành"))
                                        _demcht++;
                                    else
                                        _demht++;
                                }

                            }
                        }
                        addDataSetPiechart(pieChart);
                    }
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
            setValuesfromcalendar();
            tieude.setText(_dateselected);
            getListCV();
        }
        else
            if(_selectedspinner.equals(arrthongke[2]))
            {
                cal.add(Calendar.MONTH,1);
                setValuesfromcalendar();
                tieude.setText(_month+"/"+_year);
                getListCV();
            }
            else
            {
                cal.add(Calendar.WEEK_OF_MONTH,1);
                setValuesfromcalendar();
                getdayStartEndinWeek();
                tieude.setText("Tuần "+cal.get(Calendar.WEEK_OF_MONTH)+" Tháng "+_month+"/"+_year);
                getListCV();

            }
    }
    private void clickPre()
    {
        if(_selectedspinner.equals(arrthongke[0]))
        {
            cal.add(Calendar.DATE,-1);
            setValuesfromcalendar();
            tieude.setText(_dateselected);
            getListCV();
        }
        else
        if(_selectedspinner.equals(arrthongke[2]))
        {
            cal.add(Calendar.MONTH,-1);
            setValuesfromcalendar();
            tieude.setText(_month+"/"+_year);
            getListCV();
        }
        else
        {
            cal.add(Calendar.WEEK_OF_MONTH,-1);
            setValuesfromcalendar();
            getdayStartEndinWeek();
            tieude.setText("Tuần "+cal.get(Calendar.WEEK_OF_MONTH)+" Tháng "+_month+"/"+_year);
            getListCV();
        }
    }
    private void setValuesfromcalendar()
    {
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
    }
    private void addDataSetPiechart(PieChart pieChart) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        float[] yData = { 100, 0 };
        if(_selectedspinner.equals(arrthongke[0])){
            int tong=listcvngay.size();
            if(tong!=0)
            {
                float tyleht=0,tylecht = 0;
                tyleht=(float) _demht*100/tong;
                tylecht=100-tyleht;
                yData[0]=tyleht;
                yData[1]=tylecht;
                tongcv.setText("Có "+tong+" công việc trong ngày.");
                if(tyleht<100)
                {
                    loikhen.setText("Bạn còn công việc cần làm. Cố lên.");
                }
                else
                    loikhen.setText("Tuyệt vời.");
            }
            else
            {
                tongcv.setText("Có "+tong+" công việc trong ngày.");
                loikhen.setText("Làm cho mình thành người bận rộn đi nào.");
            }
        }
        else
        {
            int tong=listcvtuan.size();
            if(tong!=0)
            {
                float tyleht=0,tylecht = 0;
                tyleht=(float) _demht*100/tong;
                tylecht=100-tyleht;
                yData[0]=tyleht;
                yData[1]=tylecht;
                tongcv.setText("Có "+tong+" công việc trong tuần.");
                if(tyleht<100)
                {
                    loikhen.setText("Bạn còn công việc cần làm. Cố lên.");
                }
                else
                    loikhen.setText("Tuyệt vời.");
            }
            else
            {
                tongcv.setText("Có "+tong+" công việc trong tuần.");
                loikhen.setText("Làm cho mình thành người bận rộn đi nào.");
            }
        }

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
        colors.add(Color.parseColor("#ccff99"));
        colors.add(Color.parseColor("#ffffcc"));
        pieDataSet.setColors(colors);

        Legend legend=pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
    private void addDataSetLinechart(LineChartView line)
    {
        int maxday=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int ngaytam=1;
        float tyle[]=new float[maxday];
        List tylevalue=new ArrayList();
        int ngay[]=new int[maxday];
        List ngayvalue=new ArrayList<>();
        List<CongViec> cvtrongngay=new ArrayList<>();
        for (int i=0;i<maxday;i++)
        {
            tyle[i]= (float) 0.0;
            ngay[i]=ngaytam;
            ngaytam++;
        }

        for (int i=0;i<maxday;i++)
        {
            for (CongViec cv:listcvthang)
            {
                if(ngay[i]==stringSplitDay(cv.getNgaybatdau()))
                {
                    cvtrongngay.add(cv);
                }
            }
            for (CongViec cv:cvtrongngay)
            {
                if(cv.getTrangthai().equals("Hoàn thành"))
                    _demht++;
                else
                    _demcht++;
            }
            if(cvtrongngay.size()!=0)
            {
                int tong=cvtrongngay.size();
                float tlht=(float) _demht*100/tong;
                float tlcht=100-tlht;
                tyle[i]=tlht;
                _demht=0;
                _demcht=0;
                cvtrongngay.clear();
            }
        }
        Line lline = new Line(tylevalue).setColor(Color.parseColor("#ccff99"));
        for (int i=0;i<ngay.length;i++)
        {
            ngayvalue.add(i, new AxisValue(i).setLabel(ngay[i]+""));
        }
        for(int i=0;i<tyle.length;i++)
        {
            tylevalue.add(new PointValue(i, tyle[i]));
        }
        List lines = new ArrayList();
        lines.add(lline);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(ngayvalue);
        axis.setTextSize(13);
        axis.setTextColor(Color.parseColor("#37474F"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("% công việc hoàn thành");
        yAxis.setTextColor(Color.parseColor("#37474F"));
        yAxis.setTextSize(13);
        data.setAxisYLeft(yAxis);

        line.setLineChartData(data);
        Viewport viewport = new Viewport(line.getMaximumViewport());
        viewport.top = 100;
        line.setMaximumViewport(viewport);
        line.setCurrentViewport(viewport);
    }
    //Sư kiện click vào biểu đồ
    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

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
        ngaytrongtuan.clear();
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
            ngaytrongtuan.add(ngay);
            calwstart.add(Calendar.DATE,1);
            datestart=calwstart.getTime();
            sosanh=datestart.compareTo(dateend);
        }
    }
    private void layemail()
    {
        _email=getIntent().getStringExtra(MainActivity.EMAIL_THEMCV);
    }
}
