package com.example.testapp;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.Control.QLCongViec;
import com.example.testapp.Custome.CustomeSpinner;
import com.example.testapp.Custome.CustomeXemDanhSachCV;
import com.example.testapp.KetNoiMang.ConnectionReceiver;
import com.example.testapp.Model.CongViec;
import com.example.testapp.Model.Nhan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
    private List<CongViec> list;
    private ListView lsvDS;
    private TextView tvemailnav;
    public static String EMAILNAV="email";
    public static String CONGVIEC="congviec";
    public static String BUNDLE="bun";
    public static final int RESULT_CODE=116;
    public static final int RESULT_CODE1=115;
    public static final int REQUEST_CODE1=114;
    public static final int REQUEST_CODE=113;
    public static final int RESULT_CODE2=117;
    public static final int REQUEST_CODE2=118;
    private String MainEmail="";
    private DatabaseReference databaseReference;
    private CustomeXemDanhSachCV adapterrr;
    private static final String TAG = "MainActivity";
    private String _tieude="";
    private String _key="";
    private int _vitrixoasua=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layTheHien();
        loadUI();
        sukien();


    }
    private void sukien()
    {
        //Sự kiện click vào item trong listview
        lsvDS.setOnItemClickListener(this);
        lsvDS.setOnItemLongClickListener(this);
        registerForContextMenu(lsvDS);
    }
    private void loadUI()
    {
        //Đổ dữ liệu vào listview
        list=new ArrayList<>();
        adapterrr=new CustomeXemDanhSachCV(this,R.layout.mycustome_danhsachcongviec,list);
        lsvDS.setAdapter(adapterrr);
        /////////
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
        //Set dữ liệu vào textview email
        setTexTexView();
        loadDataListView();
    }
    private void layTheHien()
    {

        databaseReference= FirebaseDatabase.getInstance().getReference();
        //Lấy thể hiện
        lsvDS=findViewById(R.id.lsv_main_dsCongViec);
    }
    //Đưa dữ liệu vào listview hiển thị ở màng hình chính
    private void loadDataListView()
    {
        adapterrr.clear();
        Query query=databaseReference.child("CongViec").orderByChild("email");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CongViec cv=dataSnapshot.getValue(CongViec.class);
                if(cv.getEmail().equals("cuongvo077@gmail.com")&&cv.getTrangthai().equalsIgnoreCase("Chưa hoàn thành"))
                {
                    list.add(cv);
                    adapterrr.notifyDataSetChanged();
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
    //get Intent từ Google_sign_in
    private void setTexTexView()
    {
        Intent intent=getIntent();
        tvemailnav.setText(intent.getStringExtra(Google_sign_in.EMAIL_SIGN).toString());
        MainEmail=intent.getStringExtra(Google_sign_in.EMAIL_SIGN).toString();
    }
    //Sử lý sự kiện giữ lâu 1 item trong listview

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.contextmenu_qlcongviec_sua:
                //suaCongViec();
                break;
            case R.id.contextmenu_qlcongviec_xoa:
                showAlertDialogHoi();
                break;
        }
        return super.onContextItemSelected(item);
    }
    //Cập nhật công việc
    private void suaCongViec() {
        ChuyenUpdateCongViec();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu_qlcongviec,menu);
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
    //Load activuty Activity_ThemCongViec
    private void loadActivityThemCV()
    {
        Intent intent=new Intent(this,Activity_ThemCongViec.class);
        startActivityForResult(intent,REQUEST_CODE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.tailai_leftmenu_main:
                reloadActivity();
                break;
            case R.id.them_leftmenu_main:
                loadActivityThemCV();
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
    //Mở ActivityQlCongViec
    public void ChuyenActivityQlCongViec()
    {

        try{
            //Gọi activity_qlcong_viec
            Intent intent=new Intent(this,ActivityQLCongViec.class);
            intent.putExtra(EMAILNAV,MainEmail);
            startActivityForResult(intent,REQUEST_CODE1);
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ""+ex, Toast.LENGTH_SHORT).show();
        }
    }
    //Mở UpdateCongViec
    public  void ChuyenUpdateCongViec()
    {
        try{
            Intent intent=new Intent(this,Update_CongViec.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable(CONGVIEC,list.get(_vitrixoasua));
            intent.putExtra(BUNDLE,bundle);
            startActivityForResult(intent,REQUEST_CODE2);
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ""+ex, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //quản lý
        if(requestCode==REQUEST_CODE1)
        {
            switch (requestCode)
            {
                case RESULT_CODE1:
                    reloadActivity();
                    break;
            }
        }
        //thêm công việc
        if(requestCode==REQUEST_CODE)
        {
            switch (requestCode)
            {
                case RESULT_CODE:
                    reloadActivity();
                    break;
            }
        }
        //Cập nhật công việc
        if(requestCode==REQUEST_CODE2)
        {
            switch (requestCode)
            {
                case RESULT_CODE2:
                    reloadActivity();
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CongViec cv=list.get(position);
        showAlertDialog(cv);
    }

    //Show dialog hiển thị chi tiết công việc khi ấn vào 1 công việc nào đó
    private void showAlertDialog(CongViec congViec) {
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_chitiet_congviec,null);
        TextView tieude,ngaybatdau,ngayketthuc,giobatdau,gioketthuc,ghichu,nhan,trangthai;
        tieude=view.findViewById(R.id.tvtieude_chitiet);
        nhan=view.findViewById(R.id.tvnhan_chitiet);
        ngaybatdau=view.findViewById(R.id.tvngaybatdau_chitiet);
        giobatdau=view.findViewById(R.id.tvgiobatdau_chitiet);
        ngayketthuc=view.findViewById(R.id.tvngayhoanthanh_chitiet);
        gioketthuc=view.findViewById(R.id.tvgiohoanthanh_chitiet);
        trangthai=view.findViewById(R.id.tvtrangthai_chitiet);
        ghichu=view.findViewById(R.id.tvghichu_chitiet);
        tieude.setText(congViec.getTieude());
        ngaybatdau.setText(congViec.getNgaybatdau());
        ngayketthuc.setText(congViec.getNgayhoanthanh());
        giobatdau.setText(congViec.getGiobatdau());
        gioketthuc.setText(congViec.getGioketthuc());
        ghichu.setText(congViec.getGhichu());
        nhan.setText(congViec.getTennhan());
        trangthai.setText(congViec.getTrangthai());

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
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
    private void xoaCongViec(final String tieude)
    {
        try {
            databaseReference.child("CongViec").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    CongViec cv=dataSnapshot.getValue(CongViec.class);
                    if(cv.getTieude().equals(tieude))
                    {
                        _key=dataSnapshot.getKey();
                        databaseReference.child("CongViec").child(_key).child("tieude").setValue(null);
                        databaseReference.child("CongViec").child(_key).child("ghichu").setValue(null);
                        databaseReference.child("CongViec").child(_key).child("email").setValue(null);
                        databaseReference.child("CongViec").child(_key).child("giobatdau").setValue(null);
                        databaseReference.child("CongViec").child(_key).child("gioketthuc").setValue(null);
                        databaseReference.child("CongViec").child(_key).child("ngaybatdau").setValue(null);
                        databaseReference.child("CongViec").child(_key).child("ngayhoanthanh").setValue(null);
                        databaseReference.child("CongViec").child(_key).child("tennhan").setValue(null);
                        databaseReference.child("CongViec").child(_key).child("trangthai").setValue(null);
                        list.remove(_vitrixoasua);
                    }
                    adapterrr.notifyDataSetChanged();
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
            Toast.makeText(this, "Xóa thành công.", Toast.LENGTH_SHORT).show();

        }catch (Exception ex)
        {
            Toast.makeText(this, "Không xóa được.", Toast.LENGTH_SHORT).show();
        }

    }

    private void showAlertDialogHoi() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn xóa ?");
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                xoaCongViec(_tieude);
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        _tieude=list.get(position).getTieude();
        _vitrixoasua=position;
        return false;
    }

}
