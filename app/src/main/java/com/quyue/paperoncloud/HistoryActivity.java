package com.quyue.paperoncloud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.quyue.paperoncloud.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private final static String TAG = "HistoryActivity";
    private RecyclerView mHistoryRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mDataSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);

        initData();

        mHistoryRecyclerView = findViewById(R.id.history_recyclerView);
        mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);//这里我们使用默认的线性布局管理器,将其设为垂直显示
        mHistoryRecyclerView.setLayoutManager(mLayoutManager);//设置布局管理器
        mAdapter = new RecyclerViewAdapter(this, mDataSet);//实例化适配器
        mHistoryRecyclerView.setAdapter(mAdapter);//设置适配器


    }

    private void initData() {
        mDataSet = new ArrayList<String>();
        mDataSet.add("白雪公主");
        mDataSet.add("丑小鸭");
        mDataSet.add("皇帝的新衣");
    }

    public void callBackBtn(View view) {
        this.finish();
    }
}
