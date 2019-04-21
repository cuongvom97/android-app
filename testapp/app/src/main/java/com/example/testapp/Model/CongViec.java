package com.example.testapp.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CongViec implements Serializable {
    private String tieude,ghichu,email, ngaybatdau,giobatdau,gioketthuc,tennhan,trangthai;

    public CongViec() {
    }

    public CongViec(String tieude, String ghichu, String email, String ngaybatdau, String giobatdau, String gioketthuc, String tennhan, String trangthai) {
        this.tieude = tieude;
        this.ghichu = ghichu;
        this.email = email;
        this.ngaybatdau = ngaybatdau;
        this.giobatdau = giobatdau;
        this.gioketthuc = gioketthuc;
        this.tennhan = tennhan;
        this.trangthai = trangthai;
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
        return result;
    }


}
