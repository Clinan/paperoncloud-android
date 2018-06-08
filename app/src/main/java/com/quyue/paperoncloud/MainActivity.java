package com.quyue.paperoncloud;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.quyue.paperoncloud.R;

public class MainActivity extends AppCompatActivity {

    private ImageView balloonImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int[] position = {150, 279};
        balloonImageView = findViewById(R.id.balloon);

        Path path = new Path();
        path.reset();
        path.moveTo(position[0], position[1]);
        Log.d("TAG", position[0] + "" + position[1]);
        path.lineTo(position[0] + 30, position[1] + 30);
        ObjectAnimator animator = ObjectAnimator.ofFloat(balloonImageView, View.X, View.Y, path);
        animator.setDuration(3000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.start();



    }

    // 个人中心跳转
    public void userCenterBtn(View view) {
        Intent intent = new Intent(getApplicationContext(), UserCenterActivity.class);
        startActivity(intent);

    }

    public void voiceBtn(View view) {

        Intent intent = new Intent(getApplicationContext(), VoiceActivity.class);
        startActivity(intent);
    }
}
