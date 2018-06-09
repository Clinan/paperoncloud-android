package com.quyue.paperoncloud;

;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.quyue.paperoncloud.adapter.RecyclerViewAdapter;
import com.quyue.paperoncloud.db.data.DataBaseConstants;
import com.quyue.paperoncloud.db.entity.VoiceHistory;
import com.quyue.paperoncloud.db.entity.VoiceMyBill;
import com.quyue.paperoncloud.db.entity.VoiceMyBillFolder;
import com.quyue.paperoncloud.util.Util;

import java.util.ArrayList;
import java.util.List;

public class VoiceBillActivity extends AppCompatActivity {

    private final static String TAG = "VoiceBillActivityTAG";
    private RecyclerView mHistoryRecyclerView;

    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_bill);
        final Toolbar toolbar = findViewById(R.id.voice_bill_toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);

        mHistoryRecyclerView = findViewById(R.id.voice_bill_recyclerView);
        mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);//这里我们使用默认的线性布局管理器,将其设为垂直显示
        mHistoryRecyclerView.setLayoutManager(mLayoutManager);//设置布局管理器
        mAdapter = new RecyclerViewAdapter(this, DataBaseConstants.voiceMyBillFolderList, R.layout.recyclerviewadaptervoice_bill_item, true);//实例化适配器
        mHistoryRecyclerView.setAdapter(mAdapter);//设置适配器
        mAdapter.setOnRecyclerViewItemClickListener(new RecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                {
                    TextView idTextView = view.findViewById(R.id.id_tv);
                    final int id = Integer.valueOf(idTextView.getText().toString().trim());


                    final PopupWindow voiceBillFolderPopup = new PopupWindow(getApplicationContext());
                    View voiceBillFolderPopupView = View.inflate(getApplicationContext(), R.layout.voice_bill_detail_folder_popup_layout, null);
                    voiceBillFolderPopup.setContentView(voiceBillFolderPopupView);
                    Button button = voiceBillFolderPopupView.findViewById(R.id.close_popup_button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            voiceBillFolderPopup.dismiss();
                        }
                    });
                    {
                        RecyclerView recyclerView = voiceBillFolderPopupView.findViewById(R.id.voice_bill_bill_folder_popup_recyclerView);
                        //使用默认的线性布局管理器,将其设为垂直显示
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        //设置布局管理器
                        recyclerView.setLayoutManager(layoutManager);
                        VoiceMyBillFolder voiceMyBillFolder = (VoiceMyBillFolder) Util.getEntityFromList(DataBaseConstants.voiceMyBillFolderList, id);
                        Log.d(TAG, TAG);
                        final List<VoiceMyBill> voiceMyBillList = voiceMyBillFolder.getVoiceMyBillList();
                        long[] ids = new long[voiceMyBillList.size()];
                        for (int i = 0; i < voiceMyBillList.size(); i++) {
                            ids[i] = voiceMyBillList.get(i).getId();
                        }
                        final List<VoiceMyBill> containtResMyBillList = DataBaseConstants.getVoiceRourceByVoiceMyBillListIds(ids);

                        // 实例化适配器
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), containtResMyBillList, R.layout.recyclerviewadapter_item, true);
                        //设置适配器
                        recyclerView.setAdapter(adapter);
                        //设置item点击回调事件
                        adapter.setOnRecyclerViewItemClickListener(new RecyclerViewAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view) {
                                TextView idTextView = view.findViewById(R.id.id_tv);
                                int id = Integer.valueOf(idTextView.getText().toString().trim());
                                Intent intent = new Intent();
                                intent.setAction(VoiceActivity.VOICE_ACTIVITY_INTENT_ACTION);
                                Bundle bundle = new Bundle();
                                VoiceMyBill voiceMyBill = (VoiceMyBill) Util.getEntityFromList(containtResMyBillList, id);
                                bundle.putSerializable(VoiceActivity.VOICE_ACTIVITY_INTENT_Extras_OBJ_NAME, voiceMyBill.getVoiceResource());
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }

                            @Override
                            public boolean onItemLongClick(View view) {
                                return false;
                            }
                        });
                    }


                    voiceBillFolderPopup.setFocusable(true);
                    voiceBillFolderPopup.setAnimationStyle(R.style.add_bill_popup_anim);
                    voiceBillFolderPopup.setWidth(toolbar.getWidth());
                    voiceBillFolderPopup.setHeight(1000);
                    voiceBillFolderPopup.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#eeeeee")));
                    voiceBillFolderPopup.showAsDropDown(view, 0, 470, Gravity.BOTTOM);
                }
            }

            @Override
            public boolean onItemLongClick(View view) {
                return false;
            }
        });


    }

    public void callBackBtn(View view) {
        this.finish();
    }
}
