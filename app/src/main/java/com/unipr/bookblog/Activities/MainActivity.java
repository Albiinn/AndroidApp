package com.unipr.bookblog.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.unipr.bookblog.R;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN = 5000;
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bottom_animation);

        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);
        slogan = findViewById(R.id.textView2);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(() -> {
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        }, SPLASH_SCREEN);
    }
}