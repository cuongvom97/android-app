package com.example.testapp.Custome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.testapp.Model.Nhan;
import com.example.testapp.R;

import java.util.List;

public class CustomeSpinner extends ArrayAdapter<Nhan> {
    Context context;
    int resource;
    List<Nhan> objects;
    public CustomeSpinner(Context context, int resource,List<Nhan> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(resource,parent,false);
        TextView tvnhan=convertView.findViewById(R.id.tv_spinner_nhan);
        tvnhan.setText(objects.get(position).getTennhan()+"");
        return convertView;
    }
}
