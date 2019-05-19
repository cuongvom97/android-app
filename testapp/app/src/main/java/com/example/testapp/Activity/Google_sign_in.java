package com.example.testapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.testapp.KetNoiMang.ConnectionReceiver;
import com.example.testapp.Model.Nhan;
import com.example.testapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Google_sign_in extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    public static String EMAIL_SIGN ="email" ;
    public  String email=null;
    private static final String TAG = "Google_sign_in";
    private GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN=9001;
    public static FirebaseAuth myAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(myAuth.getCurrentUser()!=null){
            email=myAuth.getCurrentUser().getEmail();
            System.out.println("-------------------"+email);
        }
        if(email!=null){
            System.out.println("co current user"+myAuth.getCurrentUser());
            Intent intent=new Intent(this, MainActivity.class);
            intent.putExtra(EMAIL_SIGN,email);
            startActivity(intent);
            System.out.println("hien tai co user la "+email);
            finish();
        }
        else{
            System.out.println("User khong duoc luu : "+email);
            setContentView(R.layout.activity_google_sign_in);
            scopeGoogleSignin();
            signIn();
        }

    }
    //Lấy phạm vi kết nối
    private void scopeGoogleSignin()
    {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        account = GoogleSignIn.getLastSignedInAccount(this);
//        email=account.getEmail().toString();
//        sendToMain(email);
//    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleSignInClient.revokeAccess();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.sign_in_button:signIn();break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            email = account.getEmail().toString();
            String fireemail= email.trim();
            String firepass= email.trim();
//            myAuth.signInWithEmailAndPassword(fireemail,firepass);
//            System.out.println("dang nhap thanh cong");
            if(myAuth.signInWithEmailAndPassword(fireemail,firepass).isSuccessful()){
                sendToMain(email);
                System.out.println("dang nhap thanh cong");
            }
            else{
                System.out.println("khongt hanh cong");
                myAuth.createUserWithEmailAndPassword(firepass,firepass);
                sendToMain(email);
            }
            //sendToMain(email);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Lỗi hệ thống.", Toast.LENGTH_SHORT).show();
        }
    }
    //gửi email cho ActivityMain
    private void sendToMain(String email)
    {
        if(email!=null)
        {
            //email=account.getEmail().toString();
            Intent intent=new Intent(Google_sign_in.this, MainActivity.class);
            intent.putExtra(EMAIL_SIGN,email);
            startActivity(intent);
            //myAuth.getCurrentUser()=x;
            //System.out.println("hien tai co user la "+email);
            finish();
        }

    }
}
