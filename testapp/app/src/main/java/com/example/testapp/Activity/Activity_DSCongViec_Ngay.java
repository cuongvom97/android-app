package com.example.testapp.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.Custome.CustomeXemDanhSachCV;
import com.example.testapp.Model.CongViec;
import com.example.testapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Activity_DSCongViec_Ngay extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ListView dscongviec;
    private Spinner dsloc;
    private TextView ngay;
    private ImageButton next,pre;
    private ArrayAdapter<CongViec> arrayAdapter;
    private ArrayList<CongViec> arrayList;
    private DatabaseReference reference;
    public static final int REQUEST_CODE=4000;
    public static  final int RESULT_CODETHEM=4001;
    public static  final int RESULT_CODECAPNHAT=4002;
    public static String EMAIL_THEMCV_NGAY="emailthemcv";
    public static String EMAIL_CAPNHATCV_NGAY="cncv";
    public static String NGAYBD_THEMCV_NGAY="ngaybdd";
    public  static String KEYCN="keycn";
    public static String CVCAPNHAT="capnhat";
    private String _ngay="",_email="",_tt="",_tts="";
    private static String _tieude="",_key="";
    private static CongViec _cv=null;
    private String[] arrdsloc;
    private Calendar calendar;
    private Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__dscong_viec__ngay);
        reference= FirebaseDatabase.getInstance().getReference();
        layTheHien();
        loadUI();
        sukien();
    }
    private void layTheHien()
    {
        dscongviec=findViewById(R.id.dscongviec_ngay);
        ngay=findViewById(R.id.ngay_ngayhientai);
        next=findViewById(R.id.ib_next_ngay);
        pre=findViewById(R.id.ib_prev_ngay);
        dsloc=findViewById(R.id.dscongviec_locspinner);
    }

    private void loadUI()
    {
        Toolbar toolbar=findViewById(R.id.toolbar_dsngay);
        setSupportActionBar(toolbar);
        arrayList=new ArrayList<>();
        arrayAdapter=new CustomeXemDanhSachCV(this,R.layout.activity__dscong_viec__ngay,arrayList);
        dscongviec.setAdapter(arrayAdapter);
        //Nhận ngay và email từ main;
        Intent intent=getIntent();
        _ngay=intent.getStringExtra(MainActivity.NGAYBD_THEMCV);
        _email=intent.getStringExtra(MainActivity.EMAIL_THEMCV);
        ngay.setText(_ngay);
        //Đưa dữ liệu vào spinner lọc
        arrdsloc=getResources().getStringArray(R.array.dsloc);
        ArrayAdapter arrayAdapterloc=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arrdsloc);
        arrayAdapterloc.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        dsloc.setAdapter(arrayAdapterloc);
        dsloc.setSelection(0);
        loadDefaulInfo();
    }
    private void sukien()
    {
        registerForContextMenu(dscongviec);
        dscongviec.setOnItemClickListener(this);
        dscongviec.setOnItemLongClickListener(this);
        next.setOnClickListener(this);
        pre.setOnClickListener(this);
        dsloc.setOnItemSelectedListener(this);
        ngay.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ngay_right,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_ngay_right_them:
                themCV();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDataListView(String kieu)
    {
        if(kieu.equals("Công việc chưa hoàn thành"))
        {
            loadDSCVCHT();
        }
        else
            if (kieu.equals("Công việc đã hoàn thành"))
                loadDSCVHT();
            else
                loadDSCV();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contectmenu_them_xoa_sua,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.contextmenu_sua:
                suaCV();
                break;
            case R.id.contextmenu_xoa:
                showDialogHoi();
                break;
            case R.id.contextmenu_hoanthanh:
                showHoiHT();
                break;
        }
        return super.onContextItemSelected(item);
    }
    private void showHoiHT()
    {
        if(_cv.getTrangthai().equals("Hoàn thành"))
        {
            Toast.makeText(Activity_DSCongViec_Ngay.this, "Công việc đã hoàn thành rồi.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            AlertDialog.Builder builder=new AlertDialog.Builder(Activity_DSCongViec_Ngay.this);
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn có chắc?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    hoanthanhCV();
                    Toast.makeText(Activity_DSCongViec_Ngay.this, "Thành công", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }

    }
    private void hoanthanhCV() {
        reference.child("CongViec").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren())
                {
                    CongViec cv=data.getValue(CongViec.class);
                    String key=data.getKey();
                    if(cv.getEmail().equals(_email)&&key.equals(_key))
                    {
                        CongViec congViec=new CongViec(cv.getTieude(),cv.getGhichu(),_email,
                                cv.getNgaybatdau(),cv.getGiobatdau(),cv.getGioketthuc(),cv.getTennhan(),"Hoàn thành",
                                null);
                        Map<String,Object> values=congViec.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/CongViec/"+key,values);
                        reference.updateChildren(childUpdates);
                    }
                }
                loadDataListView(_tts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showDialogHoi()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                xoaCV();
                Toast.makeText(Activity_DSCongViec_Ngay.this, "Thành công.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    private void xoaCV() {
        reference.child("CongViec").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    CongViec cv=data.getValue(CongViec.class);
                    String key=data.getKey();
                    if(cv.getTieude().equalsIgnoreCase(_tieude)&&key.equals(_key))
                    {
                        CongViec congViec=new CongViec(null,null,null,null,
                                null,null,null,null,null);
                        Map<String,Object> values=congViec.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/CongViec/"+key,values);
                        reference.updateChildren(childUpdates);
                    }
                }
                loadDataListView(_tts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void themCV() {
        loadActivityThemCV(_email,_ngay);
    }
    private void suaCV(){
        loadCapNhatCV();

    }
    private void loadCapNhatCV()
    {
        if(_cv.getTrangthai().equals("Hoàn thành"))
        {
            Toast.makeText(this, "Công việc đã hoàn thành không thể cập nhật", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            Bundle bundle=new Bundle();
            bundle.putSerializable(CVCAPNHAT,_cv);
            Intent intent=new Intent(this,CapNhatCV.class);
            intent.putExtras(bundle);
            intent.putExtra(EMAIL_CAPNHATCV_NGAY,_email);
            intent.putExtra(KEYCN,_key);
            startActivityForResult(intent,REQUEST_CODE);
        }
    }
    //Load activuty Activity_ThemCongViec
    private void loadActivityThemCV(String email,String ngaybd)
    {
        Intent intent=new Intent(this, Activity_ThemCongViec.class);
        intent.putExtra(EMAIL_THEMCV_NGAY,email);
        intent.putExtra(NGAYBD_THEMCV_NGAY,ngaybd);
        startActivityForResult(intent,REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE)
        {
            switch (resultCode)
            {
                case RESULT_CODETHEM:
                    _ngay=data.getStringExtra("ngay_duoc_chon");
                    reloadActivity();
                    break;
                case RESULT_CODECAPNHAT:
                    _ngay=data.getStringExtra("ngay_duoc_cap_nhat");
                    reloadActivity();
                    Toast.makeText(this, "Thành công", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    private  void reloadActivity()
    {
        Intent intent=getIntent();
        finish();
        startActivity(intent);
        loadDataListView(_tts);

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        _cv=arrayList.get(position);
        _tieude=_cv.getTieude();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        _cv=arrayList.get(position);
        _tieude=_cv.getTieude();
        try {
            reference.child("CongViec").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    CongViec cv=dataSnapshot.getValue(CongViec.class);
                    if(cv.getTieude().equals(_tieude)&&cv.getNgaybatdau().equals(_cv.getNgaybatdau()))
                    {
                        _key=dataSnapshot.getKey();
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

        }catch (Exception ex)
        {
            Toast.makeText(this, "Lỗi hệ thống.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ib_next_ngay:
                try {
                    loadNextNgay();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ib_prev_ngay:
                try {
                    loadPreNgay();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ngay_ngayhientai:
                showDatePickerDialog();
                break;
        }
    }
    private void loadDefaulInfo(){
        calendar=Calendar.getInstance();
    }
    private void loadNextNgay() throws ParseException {
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        date=dateFormat.parse(_ngay);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        _ngay=dateFormat.format(calendar.getTime());
        ngay.setText(_ngay);
        loadDataListView(_tts);
    }
    private void loadPreNgay() throws ParseException {
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        date=dateFormat.parse(_ngay);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        _ngay=dateFormat.format(calendar.getTime());
        ngay.setText(_ngay);
        loadDataListView(_tts);
    }
    private void loadDSCVCHT()
    {
        arrayList.clear();
        arrayAdapter.notifyDataSetChanged();
        Query q=reference.child("CongViec").orderByChild("trangthai").startAt("Chưa hoàn thành");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    CongViec cv=data.getValue(CongViec.class);
                    if(cv.getNgaybatdau().equalsIgnoreCase(_ngay)&&cv.getEmail().equals(_email)&&cv.getTrangthai().equals("Chưa hoàn thành"))
                    {
                        arrayList.add(cv);
                        arrayAdapter.notifyDataSetChanged();
                    }

                }
                Collections.sort(arrayList);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void loadDSCVHT()
    {
        arrayList.clear();
        arrayAdapter.notifyDataSetChanged();
        Query q=reference.child("CongViec").orderByChild("trangthai").startAt("Hoàn thành");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    CongViec cv=data.getValue(CongViec.class);
                    if(cv.getNgaybatdau().equalsIgnoreCase(_ngay)&&cv.getEmail().equals(_email)&&cv.getTrangthai().equals("Hoàn thành"))
                    {
                        arrayList.add(cv);
                        arrayAdapter.notifyDataSetChanged();
                    }

                }
                Collections.sort(arrayList);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void loadDSCV()
    {
        arrayList.clear();
        arrayAdapter.notifyDataSetChanged();
        reference.child("CongViec").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    CongViec cv=data.getValue(CongViec.class);
                    if(cv.getNgaybatdau().equalsIgnoreCase(_ngay)&&cv.getEmail().equals(_email))
                    {
                        arrayList.add(cv);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
                Collections.sort(arrayList);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        _tt=parent.getItemAtPosition(position).toString();
        _tts=_tt;
        loadDataListView(_tt);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                String n=dayOfMonth+"";
                if(n.length()<=1)
                {
                    n="0"+n;
                }
                ngay.setText(
                        n+"/"+thang+"/"+year);
                //Lưu vết lại biến ngày hoàn thành
                calendar.set(year, monthOfYear, dayOfMonth);
                date=calendar.getTime();
                SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
                _ngay=df.format(calendar.getTime());
                loadDataListView(_tts);
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s=ngay.getText()+"";
        String strArrtmp[]=s.split("/");
        int ngay=Integer.parseInt(strArrtmp[0]);
        int thang=Integer.parseInt(strArrtmp[1])-1;
        int nam=Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic=new DatePickerDialog(
                Activity_DSCongViec_Ngay.this,
                callback, nam, thang, ngay);
        pic.setTitle("Chọn ngày bạn muốn");
        pic.show();
    }
}
