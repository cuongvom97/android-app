package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.testapp.Custome.CustomeSpinner;
import com.example.testapp.Model.CongViec;
import com.example.testapp.Model.Nhan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Update_CongViec extends AppCompatActivity implements View.OnClickListener {
    private Spinner nhanspinner;
    private List<Nhan> listnhan;
    private ArrayAdapter<Nhan> apSpinner;
    private DatabaseReference databaseReference;
    private EditText tieude,ghichu;
    private Button huy,update,ngaybatdau,ngayhoanthanh,giobatdau,giohoanthanh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_congviec);
        layTheHien();
        loadUI();
        sukien();


    }

    private void sukien() {
        update.setOnClickListener(this);
        huy.setOnClickListener(this);
    }

    private void loadUI() {
        loadDataSpinner();
        layData();
    }

    private void layTheHien() {
        databaseReference= FirebaseDatabase.getInstance().getReference();
        tieude=findViewById(R.id.ed_updatecv_tieude);
        ghichu=findViewById(R.id.ed_updatecv_ghichu);
        ngaybatdau=findViewById(R.id.btn_updatecv_ngaybatdau);
        ngayhoanthanh=findViewById(R.id.btn_updatecv_ngayhoanthanh);
        giobatdau=findViewById(R.id.btn_updatecv_giobatdau);
        giohoanthanh=findViewById(R.id.btn_updatecv_giohoanthanh);
        nhanspinner=findViewById(R.id.pinner_updatecv_nhan);
        update=findViewById(R.id.btn_updatecv_update);
        huy=findViewById(R.id.btn_updatecv_huy);
        listnhan=new ArrayList<>();
        apSpinner=new CustomeSpinner(this,R.layout.custome_layout_spinner,listnhan);
        apSpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        nhanspinner.setAdapter(apSpinner);
    }
    //Load data lên spinner
    private void loadDataSpinner()
    {
        databaseReference.child("Nhan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Nhan nhan=dataSnapshot.getValue(Nhan.class);
                listnhan.add(nhan);
                apSpinner.notifyDataSetChanged();
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
    //Lấy dữ liệu từ Main gửi qua
    public void layData()
    {
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra(MainActivity.BUNDLE);
        CongViec congViec= (CongViec) bundle.getSerializable(MainActivity.CONGVIEC);
        tieude.setText(congViec.getTieude());
        ghichu.setText(congViec.getGhichu());
        ngaybatdau.setText(congViec.getNgaybatdau());
        ngayhoanthanh.setText(congViec.getNgayhoanthanh());
        giobatdau.setText(congViec.getGiobatdau());
        giohoanthanh.setText(congViec.getGioketthuc());

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_updatecv_huy:
                sendToMain(MainActivity.RESULT_CODE2);
                break;
            case R.id.btn_updatecv_update:

                break;
        }

    }
    //Trả kết quả về ActivityMain
    private void sendToMain(int resultcode)
    {
        Intent intent=getIntent();
        setResult(resultcode,intent);
        finish();
    }
}
