package com.example.testapp.CustomeCalandar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.testapp.Model.CongViec;
import com.example.testapp.R;

import java.util.ArrayList;

public class Custome_ChiTietCV_Thang extends BaseAdapter {
    Activity activity;

    private Activity context;
    private ArrayList<CongViec> alCustom;
    private String sturl;

    public Custome_ChiTietCV_Thang(Activity context, ArrayList<CongViec> alCustom) {
        this.context = context;
        this.alCustom = alCustom;
    }

    @Override
    public int getCount() {
        return alCustom.size();
    }

    @Override
    public Object getItem(int position) {
        return alCustom.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.custome_chitetcv_thang,null,true);
        TextView td,gc,ngay,nhan;
        td=convertView.findViewById(R.id.chitiet_tieude_thang);
        gc=convertView.findViewById(R.id.chitiet_ghichu_thang);
        ngay=convertView.findViewById(R.id.chitiet_ngaybd_thang);
        nhan=convertView.findViewById(R.id.chitiet_nhan_thang);

        td.setText("Tiêu đề: "+alCustom.get(position).getTieude()+"");
        gc.setText("Ghi chú: "+alCustom.get(position).getGhichu()+"");
        ngay.setText("Ngày bắt đầu: "+alCustom.get(position).getNgaybatdau());
        nhan.setText("Nhãn: "+alCustom.get(position).getTennhan());
        return convertView;
    }
}
