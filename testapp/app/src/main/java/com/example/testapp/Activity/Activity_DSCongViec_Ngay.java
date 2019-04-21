package com.example.testapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_DSCongViec_Ngay extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView dscongviec;
    private TextView ngay;
    private ArrayAdapter<CongViec> arrayAdapter;
    private ArrayList<CongViec> arrayList;
    private DatabaseReference reference;
    public static final int REQUEST_CODE=4000;
    public static  final int RESULT_CODETHEM=4001;
    public static  final int RESULT_CODECAPNHAT=4002;
    public static String EMAIL_THEMCV_NGAY="emailthemcv";
    public static String NGAYBD_THEMCV_NGAY="ngaybdd";
    public static String KEYCAPNHAT="keycapnhat";
    private String _ngay="",_email="";
    private static String _tieude="",_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__dscong_viec__ngay);
        reference= FirebaseDatabase.getInstance().getReference();
        layTheHien();
        loadUI();
        sukien();
        loadDataListView();
    }
    private void layTheHien()
    {
        dscongviec=findViewById(R.id.dscongviec_ngay);
        ngay=findViewById(R.id.ngay_ngayhientai);
    }

    private void loadUI()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dsngay);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        arrayList=new ArrayList<>();
        arrayAdapter=new CustomeXemDanhSachCV(this,R.layout.activity__dscong_viec__ngay,arrayList);
        dscongviec.setAdapter(arrayAdapter);
        Intent intent=getIntent();
        _ngay=intent.getStringExtra(MainActivity.NGAYBD_THEMCV);
        _email=intent.getStringExtra(MainActivity.EMAIL_THEMCV);
        ngay.setText(_ngay);
    }
    private void sukien()
    {
        registerForContextMenu(dscongviec);
        dscongviec.setOnItemClickListener(this);
        dscongviec.setOnItemLongClickListener(this);
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
            case R.id.home:
                Intent intent=getIntent();
                setResult(MainActivity.RESULT_CODE_NGAY);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDataListView()
    {
        Query query=reference.child("CongViec").orderByChild("ngaybatdau");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    CongViec cv=dataSnapshot.getValue(CongViec.class);
                    if(cv.getNgaybatdau().equalsIgnoreCase(_ngay)&&cv.getEmail().equals(_email))
                    {
                        arrayList.add(cv);
                        arrayAdapter.notifyDataSetChanged();

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
        }
        return super.onContextItemSelected(item);
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
                reloadActivity();
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
        reference.child("CongViec").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try
                {
                    CongViec cv=dataSnapshot.getValue(CongViec.class);
                if(cv.getTieude().equalsIgnoreCase(_tieude))
                {
                    String key=dataSnapshot.getKey();
                    CongViec congViec=new CongViec(null,null,null,null,null,null,null,null);
                    Map<String,Object> values=congViec.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/CongViec/"+key,values);
                    reference.updateChildren(childUpdates);
                    Toast.makeText(Activity_DSCongViec_Ngay.this, "Thành công.", Toast.LENGTH_SHORT).show();
                }
                }catch (Exception ex)
                {
                    Toast.makeText(Activity_DSCongViec_Ngay.this, "Lỗi hệ thống", Toast.LENGTH_SHORT).show();
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

    private void themCV() {
        loadActivityThemCV(_email,_ngay);
    }
    private void suaCV(){
        ladCapNhatCV();

    }
    private void ladCapNhatCV()
    {
        Intent intent=new Intent(this,CapNhatCV.class);
        intent.putExtra(KEYCAPNHAT,_key);
        startActivityForResult(intent,REQUEST_CODE);
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
                    reloadActivity();
                    break;
                case RESULT_CODECAPNHAT:
                    reloadActivity();
                    break;
            }
        }
    }
    private  void reloadActivity()
    {
        Intent intent=getIntent();
        finish();
        startActivity(intent);

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CongViec congViec=arrayList.get(position);
        _tieude=congViec.getTieude();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        CongViec congViec=arrayList.get(position);
        _tieude=congViec.getTieude();
        reference.child("CongViec").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CongViec cv=dataSnapshot.getValue(CongViec.class);
                if(cv.getTieude().equalsIgnoreCase(_tieude))
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
        return false;
    }
}
