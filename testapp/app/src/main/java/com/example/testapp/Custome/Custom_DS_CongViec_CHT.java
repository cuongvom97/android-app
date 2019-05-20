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

public class Custom_DS_CongViec_CHT extends ArrayAdapter<CongViec> {
    Context context;
    int resource;
    List<CongViec> objects;
    public Custom_DS_CongViec_CHT(Context context, int resource,List<CongViec> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(resource,parent,false);
        TextView tieude,ghichu,gio,ngay,nhan;
        LinearLayout layout=convertView.findViewById(R.id.layout_ds_cong_viec_cht);
        tieude=convertView.findViewById(R.id.custom_ds_cong_viec_cht_tieude);
        ghichu=convertView.findViewById(R.id.custome_ds_cong_viec_cht_ghichu);
        gio=convertView.findViewById(R.id.custom_ds_cong_viec_cht_gio);
        ngay=convertView.findViewById(R.id.custom_ds_cong_viec_cht_ngay);
        nhan=convertView.findViewById(R.id.custom_ds_cong_viec_cht_nhan);
        tieude.setText(objects.get(position).getTieude()+"");
        ghichu.setText(objects.get(position).getGhichu()+"");
        gio.setText(objects.get(position).getGiobatdau()+" - "+objects.get(position).getGioketthuc());
        ngay.setText(objects.get(position).getNgaybatdau()+"");
        nhan.setText(objects.get(position).getTennhan()+"");
        if(objects.get(position).getTrangthai().equals("Chưa hoàn thành"))
            layout.setBackgroundColor(Color.parseColor("#ffffcc"));
        else
            layout.setBackgroundColor(Color.parseColor("#ccff99"));
        return convertView;
    }
}
