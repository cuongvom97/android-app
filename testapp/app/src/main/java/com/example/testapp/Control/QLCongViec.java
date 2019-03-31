package com.example.testapp.Control;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.testapp.Model.CongViec;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class QLCongViec {
    public static DatabaseReference databaseReference;
    public QLCongViec() {
    }

    //lấy danh sách Cong việc
    public static List<CongViec> getDSCongViec()
    {
        final List<CongViec> list=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("CongViec").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CongViec cv=dataSnapshot.getValue(CongViec.class);
                list.add(cv);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return list;
    }
}
