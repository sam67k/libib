package com.example.libib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.logoimage);
        Glide.with(this).load(R.drawable.logoimage).into(imageView);

        ImageView img = (ImageView)findViewById(R.id.logoimage);
        Animation imganiFadein = AnimationUtils.loadAnimation(this,R.anim.splashfadeinanim);

        ImageView txt = (ImageView)findViewById(R.id.logotext);
        Animation textaniFadein = AnimationUtils.loadAnimation(this,R.anim.splashfadeinanim);

        imganiFadein.reset();
        img.clearAnimation();
        img.startAnimation(imganiFadein);

        textaniFadein.reset();
        txt.clearAnimation();
        txt.startAnimation(textaniFadein);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}