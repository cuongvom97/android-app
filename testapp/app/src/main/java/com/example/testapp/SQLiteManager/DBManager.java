package com.example.testapp.SQLiteManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.testapp.Model.CongViec;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {
    public static final String DATABASE_NAME ="calendar_manager";
    private static final String TABLE_NAME ="cong_viec";
    private static final String TIEUDE ="tieude";
    private static final String GHICHU ="ghichu";
    private static final String EMAIL ="email";
    private static final String NHAN ="nhan";
    private static final String NGAYBATDAU ="ngaybatdau";
    private static final String GIOBATDAU ="giobatdau";
    private static final String GIOKETTHUC ="gioketthuc";
    private static final String NHACNHO ="nhacnho";
    private static final String TRANGTHAI ="trangthai";
    private static final String ID ="id";

    private Context context;
    public DBManager(Context context) {
        super(context,DATABASE_NAME,null,1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE "+TABLE_NAME +" (" +
                ID +" integer primary key, "+
                TIEUDE + " TEXT, "+
                EMAIL +" TEXT, "+
                GHICHU+" TEXT," +
                GIOBATDAU +" TEXT," +
                NGAYBATDAU+" TEXT," +
                NHACNHO+" TEXT," +
                TRANGTHAI+" TEXT," +
                NHAN+" TEXT," +
                GIOKETTHUC+" TEXT)";
        db.execSQL(sqlQuery);
        System.out.println("Tạo thành công");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        System.out.println("Xóa thành công");
    }
    //thêm công việc
    public void addCV(CongViec cv,int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIEUDE, cv.getTieude());
        values.put(GHICHU, cv.getGhichu());
        values.put(NGAYBATDAU, cv.getNgaybatdau());
        values.put(GIOBATDAU, cv.getGiobatdau());
        values.put(GIOKETTHUC, cv.getGioketthuc());
        values.put(NHAN, cv.getTennhan());
        values.put(TRANGTHAI, cv.getTrangthai());
        values.put(NHACNHO, cv.getNhacnho());
        values.put(ID, id);
        //Neu de null thi khi value bang null thi loi

        db.insert(TABLE_NAME,null,values);

        db.close();
    }
    public void deleteTable(String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { TIEUDE,
                        GHICHU, EMAIL,NHAN,NGAYBATDAU,GIOKETTHUC,GIOBATDAU,TRANGTHAI,NHACNHO },null,
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                db.delete(TABLE_NAME,ID + " = ?",
                        new String[] { String.valueOf(1) });
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
    //lấy danh sách
    public List<CongViec> getALLCV(){
        List<CongViec> list=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { TIEUDE,
                        GHICHU, EMAIL,NHAN,NGAYBATDAU,GIOKETTHUC,GIOBATDAU,TRANGTHAI,NHACNHO },null,
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                CongViec cv = new CongViec();
                cv.setTieude(cursor.getString(0));
                cv.setGhichu(cursor.getString(1));
                cv.setEmail(cursor.getString(2));
                cv.setTennhan(cursor.getString(3));
                cv.setNgaybatdau(cursor.getString(4));
                cv.setGioketthuc(cursor.getString(5));
                cv.setGiobatdau(cursor.getString(6));
                cv.setTrangthai(cursor.getString(7));
                cv.setNhacnho(cursor.getString(8));
                list.add(cv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}
