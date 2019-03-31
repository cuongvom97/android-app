package com.example.testapp.Model;

public class CongViec {
    private String id,tieude,batdau,ketthuc,ghichu;
    public CongViec() {
    }

    public CongViec(String id, String tieude, String batdau, String ketthuc, String ghichu) {
        this.id = id;
        this.tieude = tieude;
        this.batdau = batdau;
        this.ketthuc = ketthuc;
        this.ghichu = ghichu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getBatdau() {
        return batdau;
    }

    public void setBatdau(String batdau) {
        this.batdau = batdau;
    }

    public String getKetthuc() {
        return ketthuc;
    }

    public void setKetthuc(String ketthuc) {
        this.ketthuc = ketthuc;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }
}
