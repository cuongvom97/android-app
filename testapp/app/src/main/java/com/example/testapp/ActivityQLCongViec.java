package com.example.testapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.Custome.CustomeXemDanhSachCV;
import com.example.testapp.Model.CongViec;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class ActivityQLCongViec extends AppCompatActivity implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private List<CongViec> list;
    private ListView lsvDs;
    private DatabaseReference mDatabase;
    private CustomeXemDanhSachCV customeXemDanhSachCV;
    private TextView tvemailnav;
    public static final int REQUEST_CODE1=113;
    public static final int REQUEST_CODE2=114;
    public static final int RESULT_CODE=115;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlcong_viec);
        //Listview
        lsvDs=findViewById(R.id.lsvDS);
        list=new ArrayList<>();
        customeXemDanhSachCV=new CustomeXemDanhSachCV(this,R.layout.mycustome_danhsachcongviec,list);
        lsvDs.setAdapter(customeXemDanhSachCV);
        loadListView();
        //menu
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View v=navigationView.getHeaderView(0);
        tvemailnav=v.findViewById(R.id.tvemail_nav);
        setTexTexView();
        lsvDs.setOnItemClickListener(this);
        //Dăng ký contextmenu
        registerForContextMenu(lsvDs);
    }
    private void setTexTexView()
    {
        Intent intent=getIntent();
        tvemailnav.setText(intent.getStringExtra(MainActivity.EMAILNAV).toString());
    }
    //Lấy danh sách cong việc từ firebase vào listview
    private void loadListView() {
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("CongViec").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CongViec cv=dataSnapshot.getValue(CongViec.class);
                list.add(cv);
                customeXemDanhSachCV.notifyDataSetChanged();

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

    //Xử lý sự kiện ấn giữ lâu 1 item trong listview
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu_qlcongviec,menu);
    }
    //Xử lý xự kiêm ấn vào item trong contextmenu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            //Sự kiện sửa
            case R.id.contextmenu_qlcongviec_sua:
                Toast.makeText(this, "Sửa đi", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qlcongviec,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Sử lý suwj kiện click vào 1 item trong menu trái

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_qlcongviec_thoat:
                //Gọi hàm quay về trang chủ
                sendToMain(MainActivity.RESULT_CODE1);
                break;
            case R.id.menu_qlcongviec_them_congviec:
                loadActivityThemCV();
                break;
            case R.id.menu_qlcongviec_xoa_congviec:
                loadActivityXoaCV();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //Load activuty Activity_ThemCongViec
    private void loadActivityThemCV()
    {
        Intent intent=new Intent(this,Activity_ThemCongViec.class);
        startActivityForResult(intent,REQUEST_CODE1);
    }
    //Load activity ActivityXoaCongViec
    private void loadActivityXoaCV()
    {
        Intent intent=new Intent(this,Activity_XoaCongViec.class);
        startActivityForResult(intent,REQUEST_CODE2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE1)
        {
            switch (resultCode)
            {
                case RESULT_CODE:
                    ReloadActivity();
                    break;

            }
        }
        if(requestCode==REQUEST_CODE2)
        {
            switch (resultCode)
            {
                case RESULT_CODE:
                    ReloadActivity();
                    break;

            }
        }
    }
    //Sự kiện click vào 1 item trong listview
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CongViec cv=list.get(position);
        showAlertDialog(cv);
    }
    //Show dialog hiển thị chi tiết công việc khi ấn vào 1 công việc nào đó
    private void showAlertDialog(CongViec congViec) {
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_chitiet_congviec,null);
        TextView tieude,batdau,ketthuc,ghichu;
        tieude=view.findViewById(R.id.tvtieude_chitiet);
        batdau=view.findViewById(R.id.tvbatdau_chitiet);
        ketthuc=view.findViewById(R.id.tvketthuc_chitiet);
        ghichu=view.findViewById(R.id.tvghichu_chitiet);
        tieude.setText(congViec.getTieude()+"");
        batdau.setText(congViec.getBatdau()+"");
        ketthuc.setText(congViec.getKetthuc()+"");
        ghichu.setText(congViec.getGhichu()+"");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chi tiết công việc");
        builder.setView(view);
        builder.setCancelable(false);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            //Ấn trang chủ
            case R.id.menu_trangchu:
                sendToMain(MainActivity.RESULT_CODE1);
                break;
            //Ấn quản lý công việc
            case R.id.menu_quanlycv:
                ReloadActivity();
                break;
            //Ấn quản lý nhãn
            case R.id.menu_quanlynhan:break;
            //Ấn thống kê
            case R.id.menu_thongke:break;
            //Ấn cài đặt
            case R.id.menu_caidat:
                Toast.makeText(this, "Cài đặt", Toast.LENGTH_SHORT).show();
                break;
            //Ấn giới thiệu
            case R.id.menu_goithieu:
                Toast.makeText(this, "Giới thiệu", Toast.LENGTH_SHORT).show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //Reload lại activity
    private void ReloadActivity()
    {
        Intent intent=getIntent();
        finish();
        startActivity(intent);
    }
    public void sendToMain(int resultcode)
    {
        Intent intent=getIntent();
        setResult(resultcode, intent);
        finish();
    }
}
