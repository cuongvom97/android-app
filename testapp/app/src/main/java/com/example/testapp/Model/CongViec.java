package com.example.testapp.Model;

public class CongViec {
    private String tieude,ghichu,email, ngaybatdau,ngayhoanthanh,giobatdau,gioketthuc,tennhan,trangthai;

    public CongViec() {
    }

    public CongViec(String tieude, String ghichu, String email, String ngaybatdau, String ngayhoanthanh, String giobatdau, String gioketthuc, String tennhan, String trangthai) {
        this.tieude = tieude;
        this.ghichu = ghichu;
        this.email = email;
        this.ngaybatdau = ngaybatdau;
        this.ngayhoanthanh = ngayhoanthanh;
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

    public String getNgayhoanthanh() {
        return ngayhoanthanh;
    }

    public void setNgayhoanthanh(String ngayhoanthanh) {
        this.ngayhoanthanh = ngayhoanthanh;
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
}
