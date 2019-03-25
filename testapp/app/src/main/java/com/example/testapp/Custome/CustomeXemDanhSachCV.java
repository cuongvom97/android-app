package com.example.testapp.Custome;

import android.content.Context;
import android.support.v7.widget.ActivityChooserView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.testapp.Model.CongViec;
import com.example.testapp.R;

import java.util.List;

public class CustomeXemDanhSachCV extends ArrayAdapter<CongViec>{

    Context context;
    int resource;
    List<CongViec> objects;
    public CustomeXemDanhSachCV(Context context, int resource,List<CongViec> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }


    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        convertView=LayoutInflater.from(context).inflate(R.layout.mycustome_danhsachcongviec,parent,false);
        TextView tvtieude,tvbatdau,tvketthuc,tvghichu;
        tvtieude=convertView.findViewById(R.id.tvtieude);
        tvbatdau=convertView.findViewById(R.id.tvbatdau);
        tvketthuc=convertView.findViewById(R.id.tvketthuc);
        tvghichu=convertView.findViewById(R.id.tvnoidung);
        tvtieude.setText(objects.get(position).getTieude()+"");
        tvbatdau.setText(objects.get(position).getBatdau()+"");
        tvketthuc.setText(objects.get(position).getKetthuc()+"");
        tvghichu.setText(objects.get(position).getGhichu()+"");
        return convertView;
    }

}
