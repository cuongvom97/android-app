package com.example.testapp.Custome;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testapp.Model.CongViec;
import com.example.testapp.R;

import java.util.List;

public class CustomeXemDanhSachCV extends ArrayAdapter<CongViec>{

    Context context;
    int resource;
    List<CongViec> objects;
    public CustomeXemDanhSachCV(Context context, int resource, List<CongViec> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }


    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        convertView=LayoutInflater.from(context).inflate(R.layout.mycustome_danhsachcongviec,parent,false);
        TextView tvtieude,bd,kt,ghichu,nhan;
        LinearLayout trangthai;
        tvtieude=convertView.findViewById(R.id.custom_dscv_tieude);
        trangthai=convertView.findViewById(R.id.layout_dscongviec);
        bd=convertView.findViewById(R.id.custom_dscv_thoigianbd);
        ghichu=convertView.findViewById(R.id.custome_dscv_ghichu);
        kt=convertView.findViewById(R.id.custom_dscv_thoigianht);
        nhan=convertView.findViewById(R.id.custom_dscv_nhan);
        nhan.setText(objects.get(position).getTennhan());
        bd.setText(objects.get(position).getGiobatdau()+"");
        kt.setText(objects.get(position).getGioketthuc()+"");
        ghichu.setText(objects.get(position).getGhichu()+"");
        tvtieude.setText(objects.get(position).getTieude()+"");
        String tt=objects.get(position).getTrangthai()+"";
        if(tt.equals("Chưa hoàn thành"))
            trangthai.setBackgroundColor(Color.parseColor("#ffffcc"));
        else
            trangthai.setBackgroundColor(Color.parseColor("#ccff99"));
        return convertView;
    }

}
