package com.example.testapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testapp.Model.CongViec;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Testfirebase extends AppCompatActivity {
    DatabaseReference databaseReference;
    List<CongViec> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testfirebase);
        Button test=findViewById(R.id.btntest);
        final EditText truoc,sau;
        truoc=findViewById(R.id.truoc);
        sau=findViewById(R.id.sau);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        list=new ArrayList<>();
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(truoc.getText().toString().equalsIgnoreCase(sau.getText().toString()))
                    Toast.makeText(Testfirebase.this, "Trùng rồi", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Testfirebase.this, "Méo trùng", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getListData() {

    }
}
