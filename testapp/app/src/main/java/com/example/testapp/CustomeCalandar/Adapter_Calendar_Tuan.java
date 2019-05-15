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
    private String _email;
    public static ArrayList<String> ngaytrongtuan;
    private List<CongViec> viecList;
    private ArrayAdapter<CongViec> adapter;
    private DatabaseReference reference;
    private int _dem=0;
    public Adapter_Calendar_Tuan(Activity context, ArrayList<String> ngaytrongtuan,String email) {
        this.context = context;
        this.ngaytrongtuan=ngaytrongtuan;
        this._email=email;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView,tong;
        //List<CongViec> listtam=new ArrayList<>();
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_calendar_tuan, null);
        }
        dayView=v.findViewById(R.id.tieude_tuan_ngay);
        tong=v.findViewById(R.id.tongcv_ngay_tuan);
        dayView.setText(ngaytrongtuan.get(position).toString());
        getAllCV(position,tong);
        return v;
    }
    private void getAllCV(final int vt, final TextView t)
    {
        reference=FirebaseDatabase.getInstance().getReference();
        reference.child("CongViec").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    CongViec cv=data.getValue(CongViec.class);
                    if(cv.getEmail().equals(_email)&&cv.getNgaybatdau().equals(ngaytrongtuan.get(vt).toString())){
                        _dem++;
                    }
                }
                t.setText(_dem+"");
                _dem=0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
