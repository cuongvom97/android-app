package com.example.testapp.CustomeCalandar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testapp.Model.CongViec;
import com.example.testapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Adapter_Calendar_Tuan extends BaseAdapter {
    private Activity context;

    private Calendar week;
    public GregorianCalendar pweek;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int mnthlength;
    String itemvalue;
    /**
     * calendar instance for previous month for getting complete view
     */
    ArrayList<LuaChonTrongTuan> date_collection_arr;
    private GregorianCalendar selectedDate;
    private SimpleDateFormat df;
    private String curentDateString;
    public ArrayList<String> day_string;
    private int ngay,thang,nam;
    public ArrayList<CongViec> alcongviec=new ArrayList<>();
    public Adapter_Calendar_Tuan(Activity context, GregorianCalendar pweek, ArrayList<LuaChonTrongTuan> date_collection_arr) {
        this.context = context;
        this.pweek = pweek;
        this.date_collection_arr = date_collection_arr;
        Adapter_Calandar.day_string=new ArrayList<String>();
        Locale.setDefault(Locale.getDefault());
        week = pweek;
        selectedDate = (GregorianCalendar) pweek.clone();
        df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();
    }

    private void refreshDays() {

    }

    @Override
    public int getCount() {
        return day_string.size();
    }

    @Override
    public Object getItem(int position) {
        return day_string.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_calendar_tuan, null);

        }
        dayView=v.findViewById(R.id.date_tuan_ngay);
        dayView.setText(day_string.get(position));

        return v;
    }
}
