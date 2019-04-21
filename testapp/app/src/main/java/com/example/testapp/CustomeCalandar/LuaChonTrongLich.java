package com.example.testapp.CustomeCalandar;

import java.util.ArrayList;

public class LuaChonTrongLich {
    public String tieude="";
    public String nhan="";
    public String ngaybd="";
    public String ghichu="";
    public static ArrayList<LuaChonTrongLich> luaChonTrongLichArrayList;

    public LuaChonTrongLich(String tieude, String nhan, String ngaybd, String ghichu) {
        this.tieude = tieude;
        this.nhan = nhan;
        this.ngaybd = ngaybd;
        this.ghichu = ghichu;
    }
}
