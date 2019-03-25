package com.example.testapp.Custome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.testapp.Model.CongViec;
import com.example.testapp.R;

import java.util.List;

public class CustomeXoaCongViec extends ArrayAdapter<CongViec> {
    Context context;
    int resource;
    List<CongViec> objects;
    public CustomeXoaCongViec(Context context, int resource,List<CongViec> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.custome_layout_xoacongviec,parent,false);
        TextView tvtieude,tvbatdau,tvketthuc,tvghichu;
        tvtieude=convertView.findViewById(R.id.tvtieudexoa);
        tvbatdau=convertView.findViewById(R.id.tvbatdauxoa);
        tvketthuc=convertView.findViewById(R.id.tvketthucxoa);
        tvghichu=convertView.findViewById(R.id.tvnoidungxoa);
        tvtieude.setText(objects.get(position).getTieude()+"");
        tvbatdau.setText(objects.get(position).getBatdau()+"");
        tvketthuc.setText(objects.get(position).getKetthuc()+"");
        tvghichu.setText(objects.get(position).getGhichu()+"");
        return convertView;
    }
}
