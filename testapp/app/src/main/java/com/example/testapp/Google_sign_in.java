package com.example.testapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testapp.KetNoiMang.ConnectionReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Google_sign_in extends AppCompatActivity implements View.OnClickListener {
    public static final String EMAIL_SIGN ="email" ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Google_sign_in";
    private EditText taikhoan,matkhau;
    private Button dangnhap,dangky;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Đăng nhập
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // Đăng xuất
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        setContentView(R.layout.activity_google_sign_in);
        taikhoan=findViewById(R.id.ed_dangnhap_email);
        matkhau=findViewById(R.id.ed_dangnhap_matkhau);
        dangnhap=findViewById(R.id.btn_dangnhap_dangnhap);
        dangky=findViewById(R.id.btn_dangnhap_dangky);
        //Kiểm tra mạng
        check();
        dangnhap.setOnClickListener(this);
        dangky.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        check();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //Kiêm rtra kết nối mạng
    public void check() {
        boolean ret = ConnectionReceiver.isConnected();
        String msg;
        if (ret==false)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Thông báo");
            builder.setMessage("Thiết bị chưa kết nối internet");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
                    Google_sign_in.this.startActivities(new Intent[]{intent});
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
    }
    //Đăng ký tài khoản
    private void dangKyTK() {
        final String email = taikhoan.getText().toString();
        String password = matkhau.getText().toString();
        boolean result = checkInput(email,password);
        if (result == true)
        {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // Nếu không đăng kí được, thì cho hiện Toast thông báo.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Đăng ký không thành công.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Nếu đăng nhập được, thì thực hiện các thao tác khác.
                                    Toast.makeText(Google_sign_in.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    sendMainACtivity(email);
                                }

                            }
                        });
            }

    }
    private void dangNhapTK()
    {
        final String email=taikhoan.getText().toString();
        String password=matkhau.getText().toString();
        boolean result=checkInput(email,password);
        if(result==true)
        {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                // Nếu không đăng nhập được, thì cho hiện Toast thông báo.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(getApplicationContext(), "Tài khoản hoặc mật khẩu không chính xác.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // Nếu đăng nhập được, thì thực hiện các thao tác khác.
                                    sendMainACtivity(email);
                                }
                            }
                        });
            }
    }
    private void sendMainACtivity(String email)
    {
        Intent  intent=new Intent(  Google_sign_in.this,MainActivity.class);
        intent.putExtra(EMAIL_SIGN,email);
        startActivity(intent);
        fileList();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_dangnhap_dangnhap:
                dangNhapTK();
                break;
            case R.id.btn_dangnhap_dangky:
                dangKyTK();
                break;
        }
    }
    //Kiểm tra input
    private boolean checkInput(String email,String mk)
    {
        boolean result=true;
        if(email=="")
        {
            Toast.makeText(this, "Không để trống", Toast.LENGTH_SHORT).show();
            taikhoan.requestFocus();
            return result=false;
        }
        if(checkEmail(email)==false)
        {
            Toast.makeText(this, "Định dạng không đúng.", Toast.LENGTH_SHORT).show();
            taikhoan.requestFocus();
            return result=false;
        }
        if(mk=="")
        {
            Toast.makeText(this, "Không để trống.", Toast.LENGTH_SHORT).show();
            matkhau.requestFocus();
            return result=false;
        }
        if(mk.length()<8)
        {
            Toast.makeText(this, "Mật khẩu phải >=8 ký tự.", Toast.LENGTH_SHORT).show();
            matkhau.requestFocus();
            return result=false;
        }
        return result;
    }
    private static boolean checkEmail(String email) {
        boolean result=true;
        String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern regex = Pattern.compile(emailPattern);
        Matcher matcher = regex.matcher(email);
        if (matcher.find()) {
            return result;
        } else {
            return result=false;
        }
    }
}
