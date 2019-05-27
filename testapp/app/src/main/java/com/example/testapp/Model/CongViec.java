package com.example.testapp.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CongViec implements Serializable,Comparable<CongViec> {
    private String tieude;
    private String ghichu;
    private String email;
    private String ngaybatdau;
    private String giobatdau;
    private String gioketthuc;
    private String tennhan;
    private String trangthai;
    private String nhacnho;
    private Calendar cal;
    private Date a,b;
    public CongViec() {
    }

    public CongViec(String tieude, String ghichu, String email, String ngaybatdau, String giobatdau,
                    String gioketthuc, String tennhan, String trangthai,String nhacnho) {
        this.tieude = tieude;
        this.ghichu = ghichu;
        this.email = email;
        this.ngaybatdau = ngaybatdau;
        this.giobatdau = giobatdau;
        this.gioketthuc = gioketthuc;
        this.tennhan = tennhan;
        this.trangthai = trangthai;
        this.nhacnho=nhacnho;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getNgaybatdau() {
        return ngaybatdau;
    }

    public void setNgaybatdau(String ngaybatdau) {
        this.ngaybatdau = ngaybatdau;
    }

    public String getGiobatdau() {
        return giobatdau;
    }

    public void setGiobatdau(String giobatdau) {
        this.giobatdau = giobatdau;
    }

    public String getGioketthuc() {
        return gioketthuc;
    }

    public void setGioketthuc(String gioketthuc) {
        this.gioketthuc = gioketthuc;
    }

    public String getTennhan() {
        return tennhan;
    }

    public void setTennhan(String tennhan) {
        this.tennhan = tennhan;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getNhacnho() {
        return nhacnho;
    }

    public void setNhacnho(String nhacnho) {
        this.nhacnho = nhacnho;
    }

    /**
     * lấy định dạng ngày
     * @param d
     * @return
     */
    public String getDateFormat(Date d)
    {
        SimpleDateFormat dft=new
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dft.format(d);
    }
    /**
     * lấy định dạng giờ phút
     * @param d
     * @return
     */
    public String getHourFormat(Date d)
    {
        SimpleDateFormat dft=new
                SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dft.format(d);
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tieude", tieude);
        result.put("ghichu", ghichu);
        result.put("email", email);
        result.put("ngaybatdau", ngaybatdau);
        result.put("giobatdau", giobatdau);
        result.put("gioketthuc", gioketthuc);
        result.put("tennhan", tennhan);
        result.put("trangthai", trangthai);
        result.put("nhacnho",nhacnho);
        return result;
    }


    private void setCalander()
    {
        String strrngay[]=this.ngaybatdau.split("/");
        int d=Integer.parseInt(strrngay[0]);
        int m=Integer.parseInt(strrngay[0])-1;
        int y=Integer.parseInt(strrngay[0]);
        String strrgiobd[]=this.giobatdau.split(":");
        int giobd=Integer.parseInt(strrgiobd[0]);
        int phutbd=Integer.parseInt(strrgiobd[1]);
        cal.set(y,m,d,giobd,phutbd);
        a=cal.getTime();
    }

    @Override
    public int compareTo(CongViec o) {
        cal=Calendar.getInstance();
        setCalander();
        String strrngay[]=o.getNgaybatdau().split("/");
        int d=Integer.parseInt(strrngay[0]);
        int m=Integer.parseInt(strrngay[0])-1;
        int y=Integer.parseInt(strrngay[0]);
        String strrgiobd[]=o.getGiobatdau().split(":");
        int giobd=Integer.parseInt(strrgiobd[0]);
        int phutbd=Integer.parseInt(strrgiobd[1]);
        cal.set(y,m,d,giobd,phutbd);
        b=cal.getTime();
        int result=a.compareTo(b);
        if(result<0)
            return -1;
        else
            if (result>0)
                return 1;
            else
                return 0;
    }
}
