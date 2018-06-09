package com.quyue.paperoncloud;

;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.quyue.paperoncloud.adapter.RecyclerViewAdapter;
import com.quyue.paperoncloud.db.data.DataBaseConstants;

import java.util.ArrayList;
import java.util.List;

public class VoiceBillActivity extends AppCompatActivity {

    private final static String TAG = "VoiceBillActivity";
    private RecyclerView mHistoryRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_bill);
        Toolbar toolbar = findViewById(R.id.voice_bill_toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);

        mHistoryRecyclerView = findViewById(R.id.voice_bill_recyclerView);
        mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);//这里我们使用默认的线性布局管理器,将其设为垂直显示
        mHistoryRecyclerView.setLayoutManager(mLayoutManager);//设置布局管理器
        mAdapter = new RecyclerViewAdapter(this, DataBaseConstants.voiceMyBillFolderList, R.layout.recyclerviewadaptervoice_bill_item, true);//实例化适配器
        mHistoryRecyclerView.setAdapter(mAdapter);//设置适配器


    }

    public void callBackBtn(View view) {
        this.finish();
    }
}
