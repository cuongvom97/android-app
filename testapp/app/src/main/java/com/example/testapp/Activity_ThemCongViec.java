package com.example.testapp;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.testapp.Model.CongViec;
import com.example.testapp.Model.Nhan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Activity_ThemCongViec extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private EditText tieude,ghichu,ngaybatdau,ngayhoanthanh,giobatdau,giohoanthanh;
    private ImageButton themnhan;
    private Button huy,them;
    private Spinner nhanspinner;
    private List<String> nhanList;
    private DatabaseReference databaseReference;
    private ArrayAdapter<String> stringArrayAdapterpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__them_cong_viec);
        tieude=findViewById(R.id.ed_themcv_tieude);
        ghichu=findViewById(R.id.ed_themcv_ghichu);
        ngaybatdau=findViewById(R.id.ed_themcv_ngaybatdau);
        giobatdau=findViewById(R.id.ed_themcv_giobatdau);
        ngayhoanthanh=findViewById(R.id.ed_themcv_ngayhoanthanh);
        giohoanthanh=findViewById(R.id.ed_themcv_giohoanthanh);
        huy=findViewById(R.id.btn_themcv_huy);
        them=findViewById(R.id.btn_themcv_them);
        themnhan=findViewById(R.id.btn_themcv_themnhan);
        nhanspinner=findViewById(R.id.pinner_themcv_nhan);
        nhanList=new ArrayList<>();
        //Lấy danh sách nhãn từ firebase
        loadDataPinner();
        //Dưa dữ liệu vào spinner
        stringArrayAdapterpinner=new ArrayAdapter(this, android.R.layout.simple_spinner_item,nhanList);
        stringArrayAdapterpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        nhanspinner.setAdapter(stringArrayAdapterpinner);
        //Xử lý ự kiện
        nhanspinner.setOnItemSelectedListener(this);
        them.setOnClickListener(this);
        huy.setOnClickListener(this);
        themnhan.setOnClickListener(this);
    }

    private void loadDataPinner() {
        nhanList.clear();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Nhan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Nhan nhan1=dataSnapshot.getValue(Nhan.class);
                nhanList.add(nhan1.getTennhan());
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_themcv_them:break;
            case R.id.btn_themcv_huy:
                this.finish();
                break;
            case R.id.btn_themcv_themnhan:
                showDialogThemNhan();
                break;
        }

    }

    private void showDialogThemNhan() {
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_qlnhan_themnhan,null);
        final EditText edthemnahn= view.findViewById(R.id.ed_themnhan);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Thêm nhãn");
        builder.setView(view);
        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                themNhan(edthemnahn.getText()+"");
                Toast.makeText(Activity_ThemCongViec.this, "Thêm thành công.", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    private void themNhan(String s) {
        databaseReference=FirebaseDatabase.getInstance().getReference();
        Nhan n=new Nhan(s);
        databaseReference.child("Nhan").push().setValue(n);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, ""+nhanspinner.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        
    }
}
