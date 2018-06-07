package com.quyue.paperoncloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.quyue.paperoncloud.R;

public class UserCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
    }

    public void myStarBtn(View view) {

        Intent intent = new Intent(getApplicationContext(), MyStarActivity.class);
        startActivity(intent);
    }

    public void voiceBillBtn(View view) {

        Intent intent = new Intent(getApplicationContext(), VoiceBillActivity.class);
        startActivity(intent);
    }

    public void historyBillBtn(View view) {

        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
        startActivity(intent);
    }
}
