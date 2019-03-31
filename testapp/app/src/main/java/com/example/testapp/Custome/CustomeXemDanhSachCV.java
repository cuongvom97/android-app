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
        TextView tvtieude,tvngaybd,tvngaykt,tvtrangthai,tvgiobd,tvgiokt;
        tvtieude=convertView.findViewById(R.id.tvmain_tieude);
        tvtrangthai=convertView.findViewById(R.id.tvmain_trangthai);
        tvngaybd=convertView.findViewById(R.id.tvmain_ngaybatdau);
        tvgiobd=convertView.findViewById(R.id.tvmain_giobatdau);
        tvngaykt=convertView.findViewById(R.id.tvmain_ngayketthuc);
        tvgiokt=convertView.findViewById(R.id.tvmain_gioketthuc);

        tvtieude.setText(objects.get(position).getTieude()+"");
        tvngaybd.setText(objects.get(position).getNgaybatdau()+"");
        tvgiobd.setText(objects.get(position).getGiobatdau()+"");
        tvngaykt.setText(objects.get(position).getNgayhoanthanh()+"");
        tvgiokt.setText(objects.get(position).getGioketthuc()+"");
        tvtrangthai.setText(objects.get(position).getTrangthai()+"");
        return convertView;
    }

}
