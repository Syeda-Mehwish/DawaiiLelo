package com.example.dawaiilello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroActivity extends AppCompatActivity {
    TextView welcome, title;
    private  static  int Splash_timeout = 5000;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        firebaseAuth = FirebaseAuth.getInstance();
        welcome = findViewById(R.id.textView);
        title = findViewById(R.id.textView2);

        Animation myanimation = AnimationUtils.loadAnimation(IntroActivity.this,R.anim.animation2);
        welcome.startAnimation(myanimation);
        Animation myanimation2 = AnimationUtils.loadAnimation(IntroActivity.this,R.anim.animation1);
        title.startAnimation(myanimation2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent splashintent = new Intent(IntroActivity.this,RegisterActivity.class);
                    startActivity(splashintent);
                    finish();
                }
            },Splash_timeout);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainintent = new Intent(IntroActivity.this,MainActivity.class);
                    startActivity(mainintent);
                    finish();
                }
            },Splash_timeout);
        }
    }
}