package com.quyue.paperoncloud;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class WelcomeActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private ImageView lg1;
    private ImageView lg2;
    private ImageView lg3;
    private ImageView lg4;
    private ImageView lg5;

    private ImageView logoImageView;
    private boolean jumpAnimationFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        handler.sendEmptyMessageDelayed(0, 6000);
        findById();
        animation();

    }

    private void findById() {
        lg1 = findViewById(R.id.lg1);
        lg2 = findViewById(R.id.lg2);
        lg3 = findViewById(R.id.lg3);
        lg4 = findViewById(R.id.lg4);
        lg5 = findViewById(R.id.lg5);
        logoImageView = findViewById(R.id.logoImageView);
    }

    private void animation() {
        /*final Path path = new Path();
        path.reset();
        //M0,0 C500,30 500,300 400,500 Q300,700 100,500 Q10,400 0,300
        path.moveTo(0, 0);
        path.cubicTo(500, 30, 500, 300, 400, 500);
        path.quadTo(300, 700, 100, 500);
        path.quadTo(10, 400, 0, 300);

        {
            path.reset();
            //M0,0 C500,30 500,300 400,500 Q300,700 100,500 Q10,400 0,300
            path.moveTo(0, 0);
            path.cubicTo(500, 30, 500, 300, 400, 500);
            path.quadTo(300, 700, 100, 500);
            path.quadTo(10, 400, 0, 300);
            ObjectAnimator animator = ObjectAnimator.ofFloat(lg1, View.X, View.Y, path);
            animator.setDuration(2000);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.start();
        }
        {
            path.reset();
            //M0,0 C500,30 500,300 400,500 Q300,700 100,500 Q10,400 0,300
            path.moveTo(0, 0);
            path.cubicTo(-200, 30, -200, 300, -150, 500);
            path.quadTo(0, 800, 200, 700);
            path.quadTo(500, 500, 0, 300);
            ObjectAnimator animator = ObjectAnimator.ofFloat(lg2, View.X, View.Y, path);
            animator.setDuration(2000);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.start();
        }
        {
            path.reset();
            path.cubicTo(200, 1000, 1000, 20, 0, 300);
            path.quadTo(-700, 800, 0, 300);
            ObjectAnimator animator = ObjectAnimator.ofFloat(lg3, View.X, View.Y, path);
            animator.setDuration(2000);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.start();
        }
        {
            path.reset();
            path.cubicTo(700, 1000, 1000, 0, 300, 100);
            path.quadTo(200, 120, 0, 300);
            ObjectAnimator animator = ObjectAnimator.ofFloat(lg4, View.X, View.Y, path);
            animator.setDuration(2000);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.start();
        }
        {
            path.reset();
            path.cubicTo(-700, 1000, -1000, 0, -300, 100);
            path.quadTo(-200, 120, 0, 300);
            ObjectAnimator animator = ObjectAnimator.ofFloat(lg5, View.X, View.Y, path);
            animator.setDuration(2000);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.start();
        }
        {
            path.reset();
            path.cubicTo(-800, 300, -600, 700, -400, 700);
            path.quadTo(0, 700, -400, 300);
            path.quadTo(-600, 30, 0, 300);
            ObjectAnimator animator = ObjectAnimator.ofFloat(lg6, View.X, View.Y, path);
            animator.setDuration(2000);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.start();
        }
        {
            path.reset();
            *//**
         * C-900, 0 -800,700 -400,700
         * C0, 0 -400,-600 -100,450
         * Q0,630 0,300
         *//*
            path.cubicTo(-900, 0, -800, 700, -400, 700);
            path.cubicTo(0, 0, -400, -100, -100, 450);
            path.quadTo(0, 630, 0, 300);
            ObjectAnimator animator = ObjectAnimator.ofFloat(lg7, View.X, View.Y, path);
            animator.setDuration(2000);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.start();
        }*/


        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg1, "rotationX", 90, 0);
            rotation.setDuration(2000);
            rotation.start();
        }
        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg2, "rotationX", 90, 0);
            rotation.setDuration(2000);
            rotation.start();
        }
        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg4, "rotationY", -90, 0);
            rotation.setDuration(2000);
            rotation.start();
        }
        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg5, "rotationY", -90, 0);
            rotation.setDuration(2000);
            rotation.start();
        }
        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg3, "rotationY", 90, 0);
            rotation.setDuration(2000);
            rotation.start();
        }

        //缩放

        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg1, "scaleX", 0, 1);
            rotation.setDuration(2000);
            rotation.start();
        }
        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg2, "scaleX", 0, 1);
            rotation.setDuration(2000);
            rotation.start();
        }
        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg4, "scaleX", 0, 1);
            rotation.setDuration(2000);
            rotation.start();
        }
        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg5, "scaleX", 0, 1);
            rotation.setDuration(2000);
            rotation.start();
        }
        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg3, "scaleX", 0, 1);
            rotation.setDuration(2000);
            rotation.start();
        }

//透明度
        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg1, "alpha", 0, 0.5f, 1.0f, 1.0f, 1.0f, 0);
            rotation.setDuration(3000);
            rotation.start();
        }
        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg2, "alpha", 0, 0.5f, 1.0f, 1.0f, 1.0f, 0);
            rotation.setDuration(3000);
            rotation.start();
        }
        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg4, "alpha", 0, 0.5f, 1.0f, 1.0f, 1.0f, 0);
            rotation.setDuration(3000);
            rotation.start();
        }
        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg5, "alpha", 0, 0.5f, 1.0f, 1.0f, 1.0f, 0);
            rotation.setDuration(3000);
            rotation.start();
        }
        {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(lg3, "alpha", 0, 0.5f, 1.0f, 1.0f, 1.0f, 0);
            rotation.setDuration(3000);
            rotation.start();
        }

        {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(logoImageView, "alpha", 0, 1.0f);
            alpha.setDuration(2000);
            alpha.setStartDelay(3000);
            alpha.start();
        }


    }


    private Handler handler = new Handler() {
        @Override
        public String getMessageName(Message message) {
            return super.getMessageName(message);
        }

        @Override
        public void handleMessage(Message msg) {
            getHome();
            super.handleMessage(msg);
        }
    };

    public void getHome() {
        if (!jumpAnimationFlag) {
            jumpAnimationFlag = true;
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void jumpAnimation(View view) {
        getHome();
    }
}
