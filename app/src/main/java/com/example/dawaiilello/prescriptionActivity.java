package com.example.dawaiilello;

import static com.example.dawaiilello.RegisterActivity.setSignUpFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class prescriptionActivity extends AppCompatActivity {
    private Button upload;
    Dialog signInDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        upload = findViewById(R.id.uploadBtn);
        signInDialog = new Dialog(prescriptionActivity.this);
        signInDialog.setContentView(R.layout.signin_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button signInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button signupBtn = signInDialog.findViewById(R.id.sign_up_btn);
        Intent registerIntent = new Intent(prescriptionActivity.this, RegisterActivity.class);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });
        if(!(FirebaseAuth.getInstance().getCurrentUser() ==null)) {


            FirebaseFirestore.getInstance().collection("prescription").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {


                        if (task.getResult().get("status").toString().equals("ordered")) {
                            upload.setVisibility(View.GONE);
                        } else if (task.getResult().get("status").toString().equals("confirmed")) {
                            upload.setVisibility(View.VISIBLE);


                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(prescriptionActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()){
                    if (!(FirebaseAuth.getInstance().getCurrentUser() == null)) {
                        Intent uploadIntent = new Intent(prescriptionActivity.this, PostActivity.class);
                        startActivity(uploadIntent);
                    } else {
                        signInDialog.show();

                    }
            }else{
                    Toast.makeText(prescriptionActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    upload.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent mainIN = new Intent(prescriptionActivity.this,MainActivity.class);
         startActivity(mainIN);
        finish();

    }
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        /* NetworkInfo is deprecated in API 29 so we have to check separately for higher API Levels */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Network network = cm.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(network);
            if (networkCapabilities == null) {
                return false;
            }
            boolean isInternetSuspended = !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED);
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    && !isInternetSuspended;
        } else {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }
}