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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testapp.Custome.Custom_DS_CongViec_CHT;
import com.example.testapp.Model.CongViec;
import com.example.testapp.R;
import com.example.testapp.SQLiteManager.DBManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Activity_DS_Chua_HT extends AppCompatActivity {
    private DatabaseReference reference;
    private ArrayAdapter<CongViec> adapter;
    private List<CongViec> congViecList;
    private String _email;
    private String _key;
    private CongViec _cv;
    private final static int REQUEST_CODE_CAP_NHAT=9001;
    private DBManager db;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__ds__chua__ht);
        reference= FirebaseDatabase.getInstance().getReference();
        _email=getIntent().getStringExtra("email").toString();
        db=new DBManager(this);
        listView=findViewById(R.id.ds_cong_viec_cht_list);
        congViecList=new ArrayList<>();
        adapter=new Custom_DS_CongViec_CHT(this,R.layout.custom_ds_cong_viec_cht,congViecList);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        get_danh_sach_cv_chua_hoan_thanh();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                _cv=congViecList.get(position);
                reference.child("CongViec").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren())
                        {
                            String key=data.getKey();
                            CongViec cv=data.getValue(CongViec.class);
                            if(cv.getNgaybatdau().equals(_cv.getNgaybatdau())&&cv.getTieude().equals(_cv.getTieude()))
                                _key=key;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return false;
            }
        });
        Toolbar toolbar=findViewById(R.id.toolbar_ds_cv_cht);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_menu_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.right_menu_refresh:
                Intent intent=this.getIntent();
                finish();
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_CAP_NHAT)
        {
            switch (resultCode)
            {
                case RESULT_OK:
                    get_danh_sach_cv_chua_hoan_thanh();
                    break;
                case RESULT_CANCELED:
                    get_danh_sach_cv_chua_hoan_thanh();
                    break;
            }
        }
    }

    private void get_danh_sach_cv_chua_hoan_thanh()
    {
        congViecList.clear();
        final Calendar calendar=Calendar.getInstance();
        final Date datenow=calendar.getTime();
        reference.child("CongViec").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    CongViec cv=data.getValue(CongViec.class);

                    if(cv.getEmail().equals(_email))
                    {
                        String str[]=cv.getNgaybatdau().split("/");
                        int day=Integer.parseInt(str[0]);
                        int month=Integer.parseInt(str[1])-1;
                        int year=Integer.parseInt(str[2]);
                        calendar.set(year,month,day);
                        Date date=calendar.getTime();
                        int result=date.compareTo(datenow);
                        if(result<0&&cv.getTrangthai().equals("Chưa hoàn thành"))
                        {
                            congViecList.add(cv);
                        }
                    }
                }
                Collections.sort(congViecList);
                adapter.notifyDataSetChanged();
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

                showDialogHoiXoa();
                break;
            case R.id.contextmenu_hoanthanh:

                showHoiHT();
                break;
        }
        return super.onContextItemSelected(item);

    }
    private void suaCV(){
        loadCapNhatCV();

    }
    private void loadCapNhatCV()
    {

        Bundle bundle=new Bundle();
        bundle.putSerializable("cong_viec_cap_nhat",_cv);
        Intent intent=new Intent(this, CapNhatCV.class);
        intent.putExtras(bundle);
        intent.putExtra("email_cap_nhat_cv",_email);
        intent.putExtra("key_cn",_key);
        startActivityForResult(intent,REQUEST_CODE_CAP_NHAT);

    }
    private void showHoiHT()
    {
        if(_cv.getTrangthai().equals(".."))
        {
            Toast.makeText(Activity_DS_Chua_HT.this, "Công việc đã hoàn thành rồi.", Toast.LENGTH_SHORT).show();
            return;
        }
//        else {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Calendar calht = Calendar.getInstance();
            Date a = calht.getTime();
            Date b= df.parse(_cv.getNgaybatdau()+"");
            int check=a.compareTo(b);
            if(check>=0)
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(Activity_DS_Chua_HT.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    //                        @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hoanthanhCV();
                        Toast.makeText(Activity_DS_Chua_HT.this, "Thành công", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
            else
            {
                Toast.makeText(Activity_DS_Chua_HT.this,
                        "Bạn không thể hoàn thành công việc ở tương lai",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //    }
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
                        String text =  cv.getTrangthai();
                        if(text== "Hoàn thành"){
                            text= "Chưa hoàn thành";
                        }
                        else{
                            text="Hoàn thành";
                        }
                        CongViec congViec=new CongViec(cv.getTieude(),cv.getGhichu(),_email,
                                cv.getNgaybatdau(),cv.getGiobatdau(),cv.getGioketthuc(),cv.getTennhan(),text,
                                null);
                        Map<String,Object> values=congViec.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/CongViec/"+key,values);
                        reference.updateChildren(childUpdates);
                        db.Update(cv,key);
                    }
                }
                get_danh_sach_cv_chua_hoan_thanh();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void showDialogHoiXoa()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                xoaCV();
                Toast.makeText(Activity_DS_Chua_HT.this, "Thành công.", Toast.LENGTH_SHORT).show();
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
                    if(cv.getTieude().equalsIgnoreCase(_cv.getTieude())&&key.equals(_key))
                    {
                        CongViec congViec=new CongViec(null,null,null,null,
                                null,null,null,null,null);
                        Map<String,Object> values=congViec.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/CongViec/"+key,values);
                        reference.updateChildren(childUpdates);
                        db.deleteCV(key);
                    }
                }
                get_danh_sach_cv_chua_hoan_thanh();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
