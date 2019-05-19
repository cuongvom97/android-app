package com.example.testapp.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.testapp.Activity_CaiDat;
import com.example.testapp.CustomeCalandar.Adapter_Calandar;
import com.example.testapp.CustomeCalandar.LuaChonTrongLich;
import com.example.testapp.Model.CongViec;
import com.example.testapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private DatabaseReference reference;
    public GregorianCalendar cal_month, cal_month_copy;
    private Adapter_Calandar hwAdapter;
    private TextView tv_month,tiltile_email;
    private ImageButton previous,next,dangxuat;
    private GridView gridview;
    private String _emaim_signin="";
    private String _ngay_hientai="";
    public static final String NGAYBD_THEMCV="ngay_ngay";
    public   static final String EMAIL_THEMCV="email_email";
    public final static int REQUEST_CODE_NGAY=5000;
    public final static int RESULT_CODE_NGAY=5001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reference= FirebaseDatabase.getInstance().getReference();
        layTheHien();
        loadIU();
        sukien();
    }
    private void loadIU()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        layEmail();
        View view=navigationView.getHeaderView(0);
        tiltile_email=view.findViewById(R.id.tvemail_nav);
        dangxuat=view.findViewById(R.id.imagebtndangxuat);
        tiltile_email.setText(_emaim_signin);
        //calendar
        LuaChonTrongLich.luaChonTrongLichArrayList=new ArrayList<>();
        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        hwAdapter = new Adapter_Calandar(this, cal_month,LuaChonTrongLich.luaChonTrongLichArrayList);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
        gridview.setAdapter(hwAdapter);
        loadSuKienTrenLich();
    }
    private void layTheHien()
    {
        tv_month = (TextView) findViewById(R.id.tv_month);
        previous = (ImageButton) findViewById(R.id.ib_prev);
        next = (ImageButton) findViewById(R.id.Ib_next);
        gridview = (GridView) findViewById(R.id.gv_calendar);
    }
    private void sukien()
    {
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        gridview.setOnItemClickListener(this);
        dangxuat.setOnClickListener(this);
        tv_month.setOnClickListener(this);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.menu_ngay:
                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                _ngay_hientai=df.format(calendar.getTime());
                guiDSCongViec_Ngay(_emaim_signin);
                break;
            case R.id.menu_tuan:
                loadTuan();
                break;
            case R.id.menu_goithieu:
                Intent intent1=new Intent(this, Activity_GioiThieu.class);
                startActivity(intent1);
                break;
            case R.id.menu_thongke:
                loadThongKe();
                break;
            case R.id.menu_caidat:
                loadCaiDat();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void loadCaiDat(){
        Intent intent=new Intent(this, Activity_CaiDat.class);
        startActivity(intent);
    }
    private void loadTuan() {
        Intent intent=new Intent(this, Activity_Tuan.class);
        intent.putExtra(EMAIL_THEMCV,_emaim_signin);
        startActivity(intent);
    }

    private void loadThongKe() {
        Intent intent=new Intent(this, Activity_ThongKe.class);
        intent.putExtra(EMAIL_THEMCV,_emaim_signin);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.left_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.tailai_leftmenu_main:
                reloadUI();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //Load lại giao diện
    private void reloadUI()
    {
        Intent intent=this.getIntent();
        finish();
        startActivity(intent);
    }
    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        hwAdapter.refreshDays();
        hwAdapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }
    //Lấy email vừa đăng nhập
    private void layEmail()
    {
        Intent intent=getIntent();
        _emaim_signin=intent.getStringExtra(Google_sign_in.EMAIL_SIGN)+"";
    }
    //Gửi sang activity Activity_DSCongViec_Ngay
    private void guiDSCongViec_Ngay(String mail){
        Intent intent=new Intent(this,Activity_DSCongViec_Ngay.class);
        intent.putExtra(EMAIL_THEMCV,mail);
        intent.putExtra(NGAYBD_THEMCV,_ngay_hientai);
        startActivityForResult(intent,MainActivity.REQUEST_CODE_NGAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==REQUEST_CODE_NGAY)
        {
            switch (resultCode)
            {
                case RESULT_CODE_NGAY:
                    loadIU();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ib_prev:
                setPreviousMonth();
                refreshCalendar();
                break;
            case R.id.Ib_next:
                setNextMonth();
                refreshCalendar();
                break;
            case R.id.imagebtndangxuat:
                showDialogHoi();
                break;
            case R.id.tv_month:
                showDatePickerDialog();
                break;
        }
    }
    private void loadGoogleSignin()
    {
        Intent intent=new Intent(this,Google_sign_in.class);
        startActivity(intent);
    }
    private void showDialogHoi()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn đăng xuất");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadGoogleSignin();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedGridDate = Adapter_Calandar.day_string.get(position);
        _ngay_hientai=selectedGridDate;
        guiDSCongViec_Ngay(_emaim_signin);
    }
    private void loadSuKienTrenLich()
    {
        LuaChonTrongLich.luaChonTrongLichArrayList.clear();
        hwAdapter.notifyDataSetChanged();
        reference.child("CongViec").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CongViec cv=dataSnapshot.getValue(CongViec.class);
                if(cv.getEmail().equalsIgnoreCase(_emaim_signin)&&cv.getTrangthai().equalsIgnoreCase("Chưa hoàn thành"))
                {
                    LuaChonTrongLich.luaChonTrongLichArrayList.add(new LuaChonTrongLich(cv.getNgaybatdau()+""));
                    hwAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                loadSuKienTrenLich();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                loadSuKienTrenLich();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void showDatePickerDialog()
    {
        DatePickerDialog.OnDateSetListener callback=new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                cal_month.set(year, monthOfYear, dayOfMonth);
                refreshCalendar();
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        String s=df.format(cal_month.getTime());
        String strArrtmp[]=s.split("/");
        int ngay=Integer.parseInt(strArrtmp[0]);
        int thang=Integer.parseInt(strArrtmp[1])-1;
        int nam=Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic=new DatePickerDialog(
                MainActivity.this,
                callback, nam, thang, ngay);
        pic.setTitle("Chọn ngày bạn muốn");
        pic.show();
    }
}
