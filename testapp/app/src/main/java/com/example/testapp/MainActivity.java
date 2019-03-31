package com.example.testapp;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.Custome.CustomeXemDanhSachCV;
import com.example.testapp.KetNoiMang.ConnectionReceiver;
import com.example.testapp.Model.CongViec;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private List<CongViec> list;
    private TextView tvemailnav;
    public static String EMAILNAV="email";
    public static final int RESULT_CODE1=115;
    public static final int REQUEST_CODE=113;
    private String MainEmail="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Lấy header của navigation
        View view=navigationView.getHeaderView(0);
        tvemailnav= view.findViewById(R.id.tvemail_nav);
        setTexTexView();

    }
    //get Intent từ Google_sign_in
    private void setTexTexView()
    {
        Intent intent=getIntent();
        tvemailnav.setText(intent.getStringExtra(Google_sign_in.EMAIL_SIGN).toString());
        MainEmail=intent.getStringExtra(Google_sign_in.EMAIL_SIGN).toString();
    }

    //Kiêm tra kết nối mạng
    public void check() {
        boolean ret = ConnectionReceiver.isConnected();
        String msg;
        if (ret==false)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Thông báo");
            builder.setMessage("Thiết bị chưa kết nối internet");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
                    MainActivity.this.startActivities(new Intent[]{intent});
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.tailai_leftmenu:
                reloadActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    //Reload activity
    private void reloadActivity()
    {
        Intent intent=getIntent();
        finish();
        startActivity(intent);
    }
    @Override
    //Sử lý sự kiện khi ấn vào các mút trên menu phải
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId())
        {
            //Ấn trang chủ
            case R.id.menu_trangchu:
                reloadActivity();
                break;
                //Ấn quản lý công việc
            case R.id.menu_quanlycv:
                //Gọi tới hàm ChuyenActivityQlCongViec() xử lý sự kiện
                ChuyenActivityQlCongViec();
                break;
                //Ấn quản lý nhãn
            case R.id.menu_quanlynhan:break;
            //Ấn thống kê
            case R.id.menu_thongke:break;
            //Ấn cài đặt
            case R.id.menu_caidat:
                Toast.makeText(MainActivity.this, "Cài đặt", Toast.LENGTH_SHORT).show();
                break;
                //Ấn giới thiệu
            case R.id.menu_goithieu:
                Toast.makeText(MainActivity.this, "Giới thiệu", Toast.LENGTH_SHORT).show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void ChuyenActivityQlCongViec()
    {

        try{
            //Gọi activity_qlcong_viec
            Intent intent=new Intent(this,ActivityQLCongViec.class);
            intent.putExtra(EMAILNAV,MainEmail);
            startActivityForResult(intent,REQUEST_CODE);
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ""+ex, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE)
        {
            switch (requestCode)
            {
                case RESULT_CODE1:
                    reloadActivity();
                    break;
            }
        }
    }
}
