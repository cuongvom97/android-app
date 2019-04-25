package com.example.testapp.CustomeCalandar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testapp.Model.CongViec;
import com.example.testapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Adapter_Calandar extends BaseAdapter {
    private Activity context;

    private java.util.Calendar month;
    public GregorianCalendar pmonth;
    /**
     * calendar instance for previous month for getting complete view
     */
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;

    private ArrayList<String> items;
    public static List<String> day_string;
    public ArrayList<LuaChonTrongLich>  date_collection_arr;
    private String gridvalue;
    private ListView listTeachers;
    private ArrayList<CongViec> alCustom=new ArrayList<CongViec>();

    public Adapter_Calandar(Activity context, GregorianCalendar pmonths, ArrayList<LuaChonTrongLich> date_collection_arr) {
        this.context = context;
        this.pmonth = pmonths;
        this.date_collection_arr = date_collection_arr;
        Adapter_Calandar.day_string=new ArrayList<String>();
        Locale.setDefault(Locale.getDefault());
        month = pmonths;
        selectedDate = (GregorianCalendar) pmonths.clone();
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        this.items=new ArrayList<String>();
        df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();
    }

    public void refreshDays() {
        // clear items
        items.clear();
        day_string.clear();
        Locale.setDefault(Locale.getDefault());
        pmonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
        pmonthmaxset = (GregorianCalendar) pmonth.clone();

        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);


        for (int n = 0; n < mnthlength; n++) {

            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            day_string.add(itemvalue);

        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
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
            v = vi.inflate(R.layout.item_calandar, null);

        }


        dayView = (TextView) v.findViewById(R.id.date);
        String[] separatedTime = day_string.get(position).split("/");


        gridvalue = separatedTime[0].replaceFirst("^0*", "");
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            dayView.setTextColor(Color.parseColor("#A9A9A9"));
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(Color.parseColor("#A9A9A9"));
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else {
            // setting curent month's days in blue color.
            dayView.setTextColor(Color.parseColor("#696969"));
        }


        if (day_string.get(position).equals(curentDateString)) {

            v.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            v.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        dayView.setText(gridvalue);
        // create date string for comparison
        String date = day_string.get(position);

        if (date.length() <= 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() <= 1) {
            monthStr = "0" + monthStr;
        }

        setEventView(v, position,dayView);

        return v;
    }

    private void setEventView(View v, int position, TextView dayView) {
        int len=LuaChonTrongLich.luaChonTrongLichArrayList.size();
        for (int i = 0; i < len; i++) {
            LuaChonTrongLich cal_obj=LuaChonTrongLich.luaChonTrongLichArrayList.get(i);
            String date=cal_obj.ngaybd;
            int len1=day_string.size();
            if (len1>position) {

                if (day_string.get(position).equals(date)) {
                    if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {

                    } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {

                    } else {
                        v.setBackgroundColor(Color.parseColor("#343434"));
                        v.setBackgroundResource(R.drawable.rounded_calender);
                        dayView.setTextColor(Color.parseColor("#696969"));
                    }

                }
            }}
    }
}
