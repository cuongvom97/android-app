package com.example.testapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testapp.Custome.CustomeXemDanhSachCV;
import com.example.testapp.Custome.CustomeXoaCongViec;
import com.example.testapp.Model.CongViec;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Activity_XoaCongViec extends AppCompatActivity implements  AdapterView.OnItemClickListener {
    private List<CongViec> list;
    private ListView lsvDs;
    private DatabaseReference md;
    private CustomeXoaCongViec customeXoaCongViec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__xoa_cong_viec);
        lsvDs=findViewById(R.id.lsvDS);
        list=new ArrayList<>();
        customeXoaCongViec=new CustomeXoaCongViec(this,R.layout.custome_layout_xoacongviec,list);
        lsvDs.setAdapter(customeXoaCongViec);
        loadListView();
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lsvDs.setOnItemClickListener(this);
        //Dăng ký contextmenu
        registerForContextMenu(lsvDs);
    }

    private void loadListView() {
        //kết nối firebase
        md= FirebaseDatabase.getInstance().getReference();
        md.child("CongViec").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CongViec cv=dataSnapshot.getValue(CongViec.class);
                list.add(cv);
                customeXoaCongViec.notifyDataSetChanged();
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

    //Tạo menu trái
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qlcongviec_xoanhieucongviec,menu);
        return super.onCreateOptionsMenu(menu);

    }
    //Xử lý sự kiện menu trái

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_qlcongviec_child_thoat:
                combackActivityQlCongViec();
                break;
            case R.id.menu_qlcongviec_child_xoacv:
                Toast.makeText(this, "Làm sao mà xóa =))", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void combackActivityQlCongViec() {
        Intent intent=getIntent();
        setResult(ActivityQLCongViec.REQUEST_CODE1,intent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Không biết làm thế nào", Toast.LENGTH_SHORT).show();
    }
}
