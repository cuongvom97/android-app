package com.example.testapp.Custome;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testapp.Model.CongViec;
import com.example.testapp.R;

public class My_ViewHolder extends ViewHolder {
    View mView;
    public My_ViewHolder(@NonNull View view) {
        super(view);
        mView=view;
        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });
    }
    public void setDetails(Context ctx, CongViec cv){
        TextView tieude,ghichu,nhan,gio,ngay;
        LinearLayout layout;
        tieude=mView.findViewById(R.id.custom_recyclerview_tieude);
        ghichu=mView.findViewById(R.id.custome_recyclerview_ghichu);
        nhan=mView.findViewById(R.id.custom_recyclerview_nhan);
        gio=mView.findViewById(R.id.custom_recyclerview_gio);
        ngay=mView.findViewById(R.id.custom_recyclerview_ngay);
        layout=mView.findViewById(R.id.layout_recyclerview);
        tieude.setText(cv.getTieude()+"");
        ghichu.setText(cv.getGhichu()+"");
        nhan.setText(cv.getTennhan()+"");
        gio.setText(cv.getGiobatdau()+" - "+cv.getGioketthuc());
        ngay.setText(cv.getNgaybatdau());
        if(cv.getTrangthai().equals("Hoàn thành"))
            layout.setBackgroundColor(Color.parseColor("#ccff99"));
        else
            layout.setBackgroundColor(Color.parseColor("#ffffcc"));
    }
    private My_ViewHolder.ClickListener mClickListener;
    //interface to send callbacks
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View  view, int position);
    }

    public void setOnClickListener(My_ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }

}
