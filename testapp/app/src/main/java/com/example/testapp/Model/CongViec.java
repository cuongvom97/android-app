package com.example.testapp.Model;

public class CongViec {
    private String id;
    private String tieude;
    private String ghichu;
    private String batdau;

    public CongViec() {
    }

    public CongViec(String id, String tieude, String ghichu, String batdau, String ketthuc) {
        this.id = id;
        this.tieude = tieude;
        this.ghichu = ghichu;
        this.batdau = batdau;
        this.ketthuc = ketthuc;
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

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
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

    private String ketthuc;
}
