package com.jeeva.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jeeva.todo.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {
    Animation top,down;
    ActivitySplashScreenBinding activitySplashScreenBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("create");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activitySplashScreenBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(activitySplashScreenBinding.getRoot());
        top = AnimationUtils.loadAnimation(this,R.anim.top_down);
        down = AnimationUtils.loadAnimation(this,R.anim.down_top);
        activitySplashScreenBinding.logo.setAnimation(top);

        new Handler(Looper.myLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this,TodoActivity.class));
            finish();
        },1000);
    }
}