package com.example.testapp.Custome;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testapp.Model.CongViec;
import com.example.testapp.R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.ArrayList;
import java.util.List;

public class Custome_RecyclerView extends RecyclerView.Adapter<Custome_RecyclerView.RecyclerViewHolder> implements Filterable {
    private List<CongViec> list;
    private List<CongViec> listfull;

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_recyclerview,viewGroup,false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
        CongViec cv=list.get(i);
        recyclerViewHolder.tieude.setText(cv.getTieude()+"");
        recyclerViewHolder.ghichu.setText(cv.getGhichu()+"");
        recyclerViewHolder.nhan.setText(cv.getTennhan()+"");
        recyclerViewHolder.gio.setText(cv.getGiobatdau()+" - "+cv.getGioketthuc());
        recyclerViewHolder.ngay.setText(cv.getNgaybatdau());
        if(cv.getTrangthai().equals("Hoàn thành"))
            recyclerViewHolder.layout.setBackgroundColor(Color.parseColor("#ccff99"));
        else
            recyclerViewHolder.layout.setBackgroundColor(Color.parseColor("#ffffcc"));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CongViec> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CongViec item : listfull) {
                    if (item.getTieude().toLowerCase().contains(filterPattern)||item.getTennhan().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView tieude,ghichu,nhan,gio,ngay;
        LinearLayout layout;
        RecyclerViewHolder(View view)
        {
            super(view);
            tieude=view.findViewById(R.id.custom_recyclerview_tieude);
            ghichu=view.findViewById(R.id.custome_recyclerview_ghichu);
            nhan=view.findViewById(R.id.custom_recyclerview_nhan);
            gio=view.findViewById(R.id.custom_recyclerview_gio);
            ngay=view.findViewById(R.id.custom_recyclerview_ngay);
            layout=view.findViewById(R.id.layout_recyclerview);

        }
    }
    public Custome_RecyclerView(List<CongViec> congViecList)
    {
        this.list=congViecList;
        listfull=new ArrayList<>(congViecList);
    }
}
