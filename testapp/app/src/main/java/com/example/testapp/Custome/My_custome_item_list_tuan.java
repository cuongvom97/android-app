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

public class My_custome_item_list_tuan extends ArrayAdapter<CongViec> {
    Context context;
    int resource;
    List<CongViec> objects;
    public My_custome_item_list_tuan(Context context, int resource,List<CongViec> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(resource,parent,false);
        TextView tieude,gio;
        LinearLayout layout=convertView.findViewById(R.id.layout_item_list_tuan);
        tieude=convertView.findViewById(R.id.tieu_de_item_list_tuan);
        gio=convertView.findViewById(R.id.gio_item_list_tuan);
        tieude.setText(objects.get(position).getTieude());
        gio.setText(objects.get(position).getGiobatdau()+"-"+objects.get(position).getGioketthuc());
        String tt=objects.get(position).getTrangthai()+"";
        if(tt.equals("Chưa hoàn thành"))
            layout.setBackgroundColor(Color.parseColor("#ffffcc"));
        else
            layout.setBackgroundColor(Color.parseColor("#ccff99"));
        return convertView;
    }
}
