package com.quyue.paperoncloud;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.tts.Voice;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quyue.paperoncloud.adapter.RecyclerViewAdapter;
import com.quyue.paperoncloud.adapter.RecyclerViewClickSupport;
import com.quyue.paperoncloud.db.data.DataBaseConstants;
import com.quyue.paperoncloud.db.entity.VoiceCategory;
import com.quyue.paperoncloud.db.entity.VoiceCollection;
import com.quyue.paperoncloud.db.entity.VoiceResource;
import com.quyue.paperoncloud.fragment.TabContentFragment;
import com.quyue.paperoncloud.util.LyricsFileUtil;
import com.quyue.paperoncloud.util.Util;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import de.hdodenhof.circleimageview.CircleImageView;


public class VoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "VoiceActivity";
    private static final String NO_VOICE_IN_PLAYER_TOAST = "当前无播放歌曲";
    public static final String VOICE_ACTIVITY_INTENT_ACTION = "com.quyue.paperoncloud.playvoice";
    public static final String VOICE_ACTIVITY_INTENT_Extras_OBJ_NAME = "voiceResourceObj";
    private TabLayout mTabTl;
    private ViewPager mContentVp;
    private SlidingUpPanelLayout mLayout;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    private CircleImageView albumImageView;
    private ImageView miniAlbumImageView;
    private LinearLayout albumLayout;
    private LinearLayout lyricsLayout;
    private ImageView playImageView;
    private ImageView stopImageView;
    private ImageView miniPlayImageView;
    private ImageView miniStopImageView;
    private SeekBar voiceTimeSeekBar;
    private TextView currentTimeTextView;
    private TextView endTimeTextView;
    private TextView miniStoryTitleTextView;
    private TextView albumStoryTitleTextView;
    private TextView lyricsStoryTitleTextView;
    private TextView authorTextView;
    private TextView lyricsTextView;

    private ImageView addBillImageView;
    private ImageView collectionImageView;
    private ImageView shareImageView;
    private ImageView downloadImageView;

    private Animation albumImageViewAnimation;

    private Intent voiceServiceIntent;
    private VoiceService voiceService;
    private boolean isConnectService = false;
    private VoiceResource currentPlayVoiceResource;
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            voiceService = null;
            isConnectService = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            // 实例化audioService,通过binder来实现
            voiceService = ((VoiceService.AudioBinder) binder).getService();
            isConnectService = true;
            if (currentPlayVoiceResource != null) {
                setNewVoice(currentPlayVoiceResource);
            }
        }
    };

    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (voiceService != null) {

//                Log.d(TAG, "设置了开始时间和结束时间");
                voiceTimeSeekBar.setProgress(voiceService.getPlayer().getCurrentPosition());
                voiceTimeSeekBar.setMax(voiceService.getPlayer().getDuration());
                currentTimeTextView.setText(timeFormat(voiceService.getPlayer().getCurrentPosition()));
                endTimeTextView.setText(timeFormat(voiceService.getPlayer().getDuration()));

                // 当歌曲结束时，清除动画，切换按钮
                if (!voiceService.getPlayer().isPlaying()) {
                    albumImageView.clearAnimation();
                    playImageView.setVisibility(View.VISIBLE);
                    stopImageView.setVisibility(View.GONE);
                    miniPlayImageView.setVisibility(View.VISIBLE);
                    miniStopImageView.setVisibility(View.GONE);
                }
            }
            handler.postDelayed(runnable, 1000);
        }
    };

    private String timeFormat(int milliSeconds) {
        String result = "";
        int totalSeconds = milliSeconds / 1000;
        int min = totalSeconds / 60;
        int second = totalSeconds % 60;
        String minStr = String.valueOf(min);
        String secondStr = String.valueOf(second);
        if (minStr.length() < 2) {
            minStr = "0" + minStr;
        }
        if (secondStr.length() < 2) {
            secondStr = "0" + secondStr;
        }
        result = minStr + ":" + secondStr;
        return result;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        mTabTl = findViewById(R.id.tl_tab);
        mContentVp = findViewById(R.id.vp_content);
        initContent();
        initTab();
        initSliderLayout();

        initControlItem();
        exchangeAlbumAndLyrics();

        voiceServiceIntent = new Intent();
        voiceServiceIntent.setClass(this, VoiceService.class);
        //启动Service
        startService(voiceServiceIntent);
        if (!isConnectService) {
            bindService(voiceServiceIntent, conn, Context.BIND_AUTO_CREATE);
        }

    }


    /**
     * 初始化界面图片，按钮等控件
     */

    private void initControlItem() {
        albumImageView = findViewById(R.id.Album_ImageView);
        miniAlbumImageView = findViewById(R.id.miniAlbumImageView);
        albumLayout = findViewById(R.id.albumLayout);
        lyricsLayout = findViewById(R.id.lyricsLayout);
        miniStoryTitleTextView = findViewById(R.id.miniStoryTitleTextView);
        albumStoryTitleTextView = findViewById(R.id.albumStoryTitleTextView);
        lyricsStoryTitleTextView = findViewById(R.id.lyricsStoryTitleTextView);
        authorTextView = findViewById(R.id.authorTextView);
        lyricsTextView = findViewById(R.id.lyricsTextView);

        addBillImageView = findViewById(R.id.addBillImageView);
        collectionImageView = findViewById(R.id.collectionImageView);
        shareImageView = findViewById(R.id.shareImageView);
        downloadImageView = findViewById(R.id.downloadImageView);

        playImageView = findViewById(R.id.playImageView);
        stopImageView = findViewById(R.id.stopImageView);
        miniPlayImageView = findViewById(R.id.miniPlayImageView);
        miniStopImageView = findViewById(R.id.miniStopImageView);
        voiceTimeSeekBar = findViewById(R.id.play_time_seek_bar);
        currentTimeTextView = findViewById(R.id.current_time_tv);
        endTimeTextView = findViewById(R.id.end_time_tv);

        playImageView.setOnClickListener(this);
        stopImageView.setOnClickListener(this);
        miniPlayImageView.setOnClickListener(this);
        miniStopImageView.setOnClickListener(this);
        addBillImageView.setOnClickListener(this);
        collectionImageView.setOnClickListener(this);
        shareImageView.setOnClickListener(this);
        downloadImageView.setOnClickListener(this);

        albumImageViewAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        albumImageViewAnimation.setFillAfter(true); // 设置保持动画最后的状态
        albumImageViewAnimation.setDuration(5000); // 设置动画时间
        albumImageViewAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        albumImageViewAnimation.setInterpolator(new LinearInterpolator()); // 设置线性插入器
        voiceTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    voiceService.getPlayer().seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onResume() {
        VoiceResource voiceResource = (VoiceResource) getIntent().getSerializableExtra(VOICE_ACTIVITY_INTENT_Extras_OBJ_NAME);
        if (voiceResource != null) {
            currentPlayVoiceResource = voiceResource;
        }

        if (voiceService != null) {
            voiceTimeSeekBar.setProgress(voiceService.getPlayer().getCurrentPosition());
            voiceTimeSeekBar.setMax(voiceService.getPlayer().getDuration());
        }
        handler.post(runnable);
        super.onResume();
    }

    private void exchangeAlbumAndLyrics() {
        albumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumLayout.setVisibility(View.GONE);
                lyricsLayout.setVisibility(View.VISIBLE);
            }
        });
        lyricsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyricsLayout.setVisibility(View.GONE);
                albumLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initTab() {
        mTabTl.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabTl.setTabTextColors(ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.white));
        mTabTl.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        ViewCompat.setElevation(mTabTl, 10);
        mTabTl.setupWithViewPager(mContentVp);
    }

    private void initSliderLayout() {
        mLayout = findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            LinearLayout miniLayout = findViewById(R.id.miniLayout);

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (miniLayout.getVisibility() == View.INVISIBLE) {
                    miniLayout.setVisibility(View.VISIBLE);
                }
                miniLayout.setAlpha(1 - slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
                    miniLayout.setVisibility(View.INVISIBLE);
                }
                if (newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)) {
                    miniLayout.setVisibility(View.VISIBLE);

                }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    /**
     * 初始化RecyclerView容器
     */
    private void initContent() {
        final List<VoiceCategory> categoryList = DataBaseConstants.voiceCategoryList;
        final RecyclerViewAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = new RecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                TextView idTextView = view.findViewById(R.id.id_tv);
                int id = Integer.valueOf(idTextView.getText().toString().trim());
                VoiceResource voiceResource = (VoiceResource) Util.getEntityFromList(DataBaseConstants.voiceResourceList, id);
                setNewVoice(voiceResource);
            }

            @Override
            public boolean onItemLongClick(View view) {
                return false;
            }
        };
        tabFragments = new ArrayList<>();
        for (int i = 0; i < categoryList.size() + 1; i++) {
            TabContentFragment tmp;
            if (i == 0) {
                tmp = TabContentFragment.newInstance("全部");
                tmp.setOnLoadViewListener(new TabContentFragment.OnLoadViewListener() {
                    @Override
                    public View onLoadView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                        View view = inflater.inflate(R.layout.fragment_tab_story, null);
                        RecyclerView recyclerView = view.findViewById(R.id.voice_recyclerView_in_fragment);
                        //使用默认的线性布局管理器,将其设为垂直显示
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                                LinearLayoutManager.VERTICAL, false);
                        //设置布局管理器
                        recyclerView.setLayoutManager(layoutManager);
                        //实例化适配器
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), DataBaseConstants.voiceResourceList, R.layout.recyclerviewadaptervoice_play_bill_item, true);
                        //设置适配器
                        recyclerView.setAdapter(adapter);
                        //设置item点击回调事件
                        adapter.setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener);
                        return view;
                    }
                });
            } else {
                tmp = TabContentFragment.newInstance(categoryList.get(i - 1).getName());
                final int index = i - 1;
                tmp.setOnLoadViewListener(new TabContentFragment.OnLoadViewListener() {
                    @Override
                    public View onLoadView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                        View view = inflater.inflate(R.layout.fragment_tab_story, null);

                        RecyclerView recyclerView = view.findViewById(R.id.voice_recyclerView_in_fragment);
                        //使用默认的线性布局管理器,将其设为垂直显示
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                                LinearLayoutManager.VERTICAL, false);
                        //设置布局管理器
                        recyclerView.setLayoutManager(layoutManager);
                        // 实例化适配器
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), categoryList.get(index).getVoiceResourceList(), R.layout.recyclerviewadaptervoice_play_bill_item, true);
                        //设置适配器
                        recyclerView.setAdapter(adapter);
                        //设置item点击回调事件
                        adapter.setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener);
                        return view;
                    }
                });
            }

            tabFragments.add(tmp);
        }
        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());


        mContentVp.setAdapter(contentAdapter);
    }

    public void callBackVoiceBtn(View view) {
        super.onBackPressed();
    }


    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return DataBaseConstants.voiceCategoryList.size() + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "全部";
            } else {
                return DataBaseConstants.voiceCategoryList.get(position - 1).getName();
            }
        }
    }

    /**
     * 点Android返回键触发的函数
     */
    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playImageView:
                playImageView.setVisibility(View.GONE);
                stopImageView.setVisibility(View.VISIBLE);
                miniPlayImageView.setVisibility(View.GONE);
                miniStopImageView.setVisibility(View.VISIBLE);
                playOrPause();
                break;
            case R.id.stopImageView:
                playImageView.setVisibility(View.VISIBLE);
                stopImageView.setVisibility(View.GONE);
                miniPlayImageView.setVisibility(View.VISIBLE);
                miniStopImageView.setVisibility(View.GONE);
                playOrPause();
                break;
            case R.id.miniPlayImageView:
                miniPlayImageView.setVisibility(View.GONE);
                miniStopImageView.setVisibility(View.VISIBLE);
                playImageView.setVisibility(View.GONE);
                stopImageView.setVisibility(View.VISIBLE);
                playOrPause();
                break;
            case R.id.miniStopImageView:
                miniPlayImageView.setVisibility(View.VISIBLE);
                miniStopImageView.setVisibility(View.GONE);
                playImageView.setVisibility(View.VISIBLE);
                stopImageView.setVisibility(View.GONE);
                playOrPause();
                break;

            /* 最下面的四个按钮*/
            case R.id.addBillImageView:
                if (currentPlayVoiceResource == null) {
                    Toast.makeText(getApplicationContext(), NO_VOICE_IN_PLAYER_TOAST, Toast.LENGTH_SHORT).show();
                } else {
                    final PopupWindow voiceBillFolderPopup = new PopupWindow(getApplicationContext());
                    View voiceBillFolderPopupView = View.inflate(getApplicationContext(), R.layout.voice_bill_folder_popup_layout, null);
                    voiceBillFolderPopup.setContentView(voiceBillFolderPopupView);
                    Button button = voiceBillFolderPopupView.findViewById(R.id.close_popup_button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            voiceBillFolderPopup.dismiss();
                        }
                    });
                    {
                        RecyclerView recyclerView = voiceBillFolderPopupView.findViewById(R.id.voice_bill_folder_popup_recyclerView);
                        //使用默认的线性布局管理器,将其设为垂直显示
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                                LinearLayoutManager.VERTICAL, false);
                        //设置布局管理器
                        recyclerView.setLayoutManager(layoutManager);
                        // 实例化适配器
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), DataBaseConstants.voiceMyBillFolderList, R.layout.recyclerviewadaptervoice_bill_item, true);
                        //设置适配器
                        recyclerView.setAdapter(adapter);
                        //设置item点击回调事件
                        adapter.setOnRecyclerViewItemClickListener(new RecyclerViewAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view) {
                                //将歌曲添加到听单
                                TextView idTextView = view.findViewById(R.id.id_tv);
                                int id = Integer.valueOf(idTextView.getText().toString().trim());
                                DataBaseConstants.insert2VoiceMyBill(id, currentPlayVoiceResource);
                                //关闭voiceBillFolderPopup
                                voiceBillFolderPopup.dismiss();
                            }

                            @Override
                            public boolean onItemLongClick(View view) {
                                return false;
                            }
                        });
                    }
                    //设置新增歌单选项
                    final EditText new_bill_folder_et = voiceBillFolderPopupView.findViewById(R.id.new_bill_folder_et);
                    new_bill_folder_et.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new_bill_folder_et.setCursorVisible(true);
                        }
                    });
                    Button new_bill_folder_btn = voiceBillFolderPopupView.findViewById(R.id.new_bill_folder_btn);
                    new_bill_folder_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String newFolderText = new_bill_folder_et.getText().toString().trim();
                            if (newFolderText == null || newFolderText.equals("")) {
                                Toast.makeText(getApplicationContext(), "听单名字不能为空", Toast.LENGTH_SHORT).show();
                            } else {
                                DataBaseConstants.insert2VoiceMyBillWithNewFolder(newFolderText, currentPlayVoiceResource);
                                //关闭voiceBillFolderPopup
                                voiceBillFolderPopup.dismiss();
                            }
                        }
                    });

                    voiceBillFolderPopup.setFocusable(true);
                    voiceBillFolderPopup.setAnimationStyle(R.style.add_bill_popup_anim);
                    voiceBillFolderPopup.setWidth(mLayout.getWidth());
                    voiceBillFolderPopup.setHeight(1000);
                    voiceBillFolderPopup.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_two)));
                    voiceBillFolderPopup.showAsDropDown(v, 0, -150, Gravity.BOTTOM);
                }
                break;
            case R.id.collectionImageView:
                if (currentPlayVoiceResource == null) {
                    Toast.makeText(getApplicationContext(), NO_VOICE_IN_PLAYER_TOAST, Toast.LENGTH_SHORT).show();
                } else {
                    boolean isExist=DataBaseConstants.insertOrDel2VoiceMyCollection(currentPlayVoiceResource);;
                    if (isExist){
                        collectionImageView.setImageDrawable(getDrawable(R.drawable.collect_icon));
                    }else {
                        collectionImageView.setImageDrawable(getDrawable(R.drawable.ic_cc_heart));
                        Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            case R.id.shareImageView:
                if (currentPlayVoiceResource == null) {
                    Toast.makeText(getApplicationContext(), NO_VOICE_IN_PLAYER_TOAST, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "分享受限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.downloadImageView:
                if (currentPlayVoiceResource == null) {
                    Toast.makeText(getApplicationContext(), NO_VOICE_IN_PLAYER_TOAST, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "正在下载：" + currentPlayVoiceResource.getName(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void playOrPause() {
        if (currentPlayVoiceResource==null){
            Toast.makeText(getApplicationContext(),NO_VOICE_IN_PLAYER_TOAST,Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isConnectService) {
            Log.d(TAG, "bindService");
            bindService(voiceServiceIntent, conn, Context.BIND_AUTO_CREATE);
        }
        voiceService.playOrPause();

        voiceTimeSeekBar.setProgress(voiceService.getPlayer().getCurrentPosition());
        voiceTimeSeekBar.setMax(voiceService.getPlayer().getDuration());
        handler.post(runnable);

        // 专辑图片动画旋转判断
        if (voiceService.getPlayer().isPlaying()) {
            albumImageView.startAnimation(albumImageViewAnimation);
        } else {
//            albumImageViewAnimation.get
            albumImageView.clearAnimation();
        }
    }

    public void setNewVoice(final VoiceResource voice) {
        if (voice != null) {
            currentPlayVoiceResource = voice;
            if (!isConnectService) {
                Log.d(TAG, "bindService");
                bindService(voiceServiceIntent, conn, Context.BIND_AUTO_CREATE);
            }
            voiceService.setVoiceResource(voice.getResId());
            DataBaseConstants.insert2VoiceHistoryBill(voice);
            //ui更新
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //album专辑图片更新
                    albumImageView.setImageDrawable(getResources().getDrawable(voice.getAlbumResId()));
                    miniAlbumImageView.setImageDrawable(getResources().getDrawable(voice.getAlbumResId()));
                    // 按钮更新
                    playImageView.setVisibility(View.GONE);
                    stopImageView.setVisibility(View.VISIBLE);
                    miniPlayImageView.setVisibility(View.GONE);
                    miniStopImageView.setVisibility(View.VISIBLE);

                    //文本更新
                    miniStoryTitleTextView.setText(voice.getName());
                    albumStoryTitleTextView.setText(voice.getName());
                    lyricsStoryTitleTextView.setText(voice.getName());
                    authorTextView.setText(voice.getAuthor());
                    try {
                        String lyricsText = LyricsFileUtil.getResTxtFileString(getApplicationContext(), voice.getLyricsResId());
                        lyricsTextView.setText(lyricsText);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //判断是否为已经收藏好的
                    for (VoiceCollection c:DataBaseConstants.voiceCollectionList){
                        if(c.getVoiceResource().getId()==voice.getId()){
                            collectionImageView.setImageDrawable(getDrawable(R.drawable.ic_cc_heart));
                            break;
                        }
                    }

                }
            });
        }
    }


}
