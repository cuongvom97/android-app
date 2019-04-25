package com.example.testapp.CustomeCalandar;

import java.util.ArrayList;

public class LuaChonTrongTuan {
    public String giobd,gioht,tieude,ngay,trangthai;
    public static ArrayList<LuaChonTrongTuan> luaChonTrongTuans;

    public LuaChonTrongTuan(String giobd, String gioht, String tieude, String ngay,String trangthai) {
        this.giobd = giobd;
        this.gioht = gioht;
        this.tieude = tieude;
        this.ngay = ngay;
        this.trangthai=trangthai;
    }
}
