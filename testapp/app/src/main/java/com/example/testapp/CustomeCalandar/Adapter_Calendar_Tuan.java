package com.example.testapp.CustomeCalandar;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testapp.Custome.CustomeXemDanhSachCV;
import com.example.testapp.Model.CongViec;
import com.example.testapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Adapter_Calendar_Tuan extends BaseAdapter {
    private Activity context;
    private Calendar cal;
    private String _email="";
    private ArrayList<String> ngaytrongtuan;
    private List<CongViec> viecList;
    private ArrayAdapter<CongViec> adapter;
    private DatabaseReference reference;
    public Adapter_Calendar_Tuan(Activity context, Calendar cal, ArrayList<String> ngaytrongtuan,String email) {
        this.context = context;
        this.cal=cal;
        this.ngaytrongtuan=ngaytrongtuan;
        this._email=email;
        reference=FirebaseDatabase.getInstance().getReference();
        refreshDays();
    }

    private void refreshDays() {

    }

    @Override
    public int getCount() {
        return ngaytrongtuan.size();
    }

    @Override
    public Object getItem(int position) {
        return ngaytrongtuan.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        ListView dscv;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_calendar_tuan, null);
        }
        dayView=v.findViewById(R.id.tieude_tuan_ngay);
        dscv=v.findViewById(R.id.dscv_tuan);
        viecList=new ArrayList<>();
        dayView.setText(ngaytrongtuan.get(position).toString());
        adapter=new CustomeXemDanhSachCV(context,R.layout.mycustome_danhsachcongviec,viecList);
        dscv.setAdapter(adapter);
        getAllCV(position);
        return v;
    }
    private void getAllCV(final int vt)
    {
        reference.child("CongViec").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    CongViec cv=data.getValue(CongViec.class);
                    if(cv.getEmail().equals(_email)&&cv.getNgaybatdau().equals(ngaytrongtuan.get(vt))){
                        viecList.add(cv);
                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
