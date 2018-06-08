package com.quyue.paperoncloud;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
import android.view.LayoutInflater;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.quyue.paperoncloud.fragment.TabContentFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import de.hdodenhof.circleimageview.CircleImageView;


public class VoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "VoiceActivity";
    private TabLayout mTabTl;
    private ViewPager mContentVp;
    private SlidingUpPanelLayout mLayout;
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    private CircleImageView albumImageView;
    private LinearLayout albumLayout;
    private LinearLayout lyricsLayout;
    private ImageView playImageView;
    private ImageView stopImageView;
    private ImageView miniPlayImageView;
    private ImageView miniStopImageView;
    private SeekBar voiceTimeSeekBar;
    private TextView currentTimeTextView;
    private TextView endTimeTextView;
    private Animation albumImageViewAnimation;

    private Intent voiceServiceIntent;
    private VoiceService voiceService;
    private boolean isConnectService = false;
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

        Intent voiceServiceInten = new Intent();
        voiceServiceInten.setClass(this, VoiceService.class);
        //启动Service
        startService(voiceServiceInten);
        if (!isConnectService) {
            bindService(voiceServiceInten, conn, Context.BIND_AUTO_CREATE);
        }

    }


    /**
     * 初始化界面图片，按钮等控件
     */

    private void initControlItem() {
        albumImageView = findViewById(R.id.Album_ImageView);

        albumLayout = findViewById(R.id.albumLayout);
        lyricsLayout = findViewById(R.id.lyricsLayout);

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

        albumImageViewAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        albumImageViewAnimation.setFillAfter(true); // 设置保持动画最后的状态
        albumImageViewAnimation.setDuration(5000); // 设置动画时间
        albumImageViewAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        albumImageViewAnimation.setInterpolator(new LinearInterpolator()); // 设置线性插入器
        albumImageViewAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                albumImageViewAnimation
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                if (miniLayout.getVisibility() == View.INVISIBLE) {
                    miniLayout.setVisibility(View.VISIBLE);
                }
                miniLayout.setAlpha(1 - slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
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

    private void initContent() {
        tabIndicators = new ArrayList<>();
        String[] tabs = {"全部", "童话", "科教", "自然", "安全"};
        tabIndicators.addAll(Arrays.asList(tabs));

        tabFragments = new ArrayList<>();
        for (int i = 0; i < tabIndicators.size(); i++) {
            TabContentFragment tmp = TabContentFragment.newInstance(tabs[i]);
            switch (i) {
                // 全部界面
                case 0: {
                    final String[] storyList = {"白雪公主", "卖火柴的小女孩", "灰姑娘", "丑小鸭", "皇帝的新装", "拇指姑娘", "卖火柴的小女孩", "灰姑娘", "丑小鸭", "皇帝的新装", "拇指姑娘"};
                    tmp.setOnLoadViewListener(new TabContentFragment.OnLoadViewListener() {
                        @Override
                        public View onLoadView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                            View view = inflater.inflate(R.layout.fragment_tab_story, null);
                            LinearLayout linearLayout = view.findViewById(R.id.linearLayout_fragment_tab_story);

//                            for (int i = 0; i < storyList.length; i++) {
//                                addTextViewToParentView(linearLayout, 60, storyList[i]);
//                            }
                            RecyclerView recyclerView = view.findViewById(R.id.voice_recyclerView_in_fragment);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                                    LinearLayoutManager.VERTICAL, false);//这里我们使用默认的线性布局管理器,将其设为垂直显示
                            recyclerView.setLayoutManager(layoutManager);//设置布局管理器
                            List<String> list = new ArrayList<String>();
                            list.addAll(Arrays.asList(storyList));
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), list, R.layout.recyclerviewadaptervoice_play_bill_item);//实例化适配器
                            recyclerView.setAdapter(adapter);//设置适配器
                            return view;
                        }
                    });
                }
                break;
                // 童话界面
                case 1: {
                    final String[] storyList = {"白雪公主", "卖火柴的小女孩", "灰姑娘", "丑小鸭", "皇帝的新装", "拇指姑娘"};
                    tmp.setOnLoadViewListener(new TabContentFragment.OnLoadViewListener() {
                        @Override
                        public View onLoadView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                            View view = inflater.inflate(R.layout.fragment_tab_story, null);
                            LinearLayout linearLayout = view.findViewById(R.id.linearLayout_fragment_tab_story);

                            RecyclerView recyclerView = view.findViewById(R.id.voice_recyclerView_in_fragment);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                                    LinearLayoutManager.VERTICAL, false);//这里我们使用默认的线性布局管理器,将其设为垂直显示
                            recyclerView.setLayoutManager(layoutManager);//设置布局管理器
                            List<String> list = new ArrayList<String>();
                            list.addAll(Arrays.asList(storyList));
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), list, R.layout.recyclerviewadaptervoice_play_bill_item);//实例化适配器
                            recyclerView.setAdapter(adapter);//设置适配器
                            return view;
                        }
                    });
                }

                break;
                // 科教界面
                case 2: {
                    final String[] storyList = {"白雪公主", "卖火柴的小女孩", "灰姑娘", "丑小鸭", "皇帝的新装", "拇指姑娘"};
                    tmp.setOnLoadViewListener(new TabContentFragment.OnLoadViewListener() {
                        @Override
                        public View onLoadView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                            View view = inflater.inflate(R.layout.fragment_tab_story, null);
                            LinearLayout linearLayout = view.findViewById(R.id.linearLayout_fragment_tab_story);

//                            for (int i = 0; i < storyList.length; i++) {
//                                addTextViewToParentView(linearLayout, 60, storyList[i]);
//                            }
                            RecyclerView recyclerView = view.findViewById(R.id.voice_recyclerView_in_fragment);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                                    LinearLayoutManager.VERTICAL, false);//这里我们使用默认的线性布局管理器,将其设为垂直显示
                            recyclerView.setLayoutManager(layoutManager);//设置布局管理器
                            List<String> list = new ArrayList<String>();
                            list.addAll(Arrays.asList(storyList));
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), list, R.layout.recyclerviewadaptervoice_play_bill_item);//实例化适配器
                            recyclerView.setAdapter(adapter);//设置适配器
                            return view;
                        }
                    });
                }
                break;
                // 自然界面
                case 3: {
                    final String[] storyList = {"白雪公主", "卖火柴的小女孩", "灰姑娘", "丑小鸭", "皇帝的新装", "拇指姑娘"};
                    tmp.setOnLoadViewListener(new TabContentFragment.OnLoadViewListener() {
                        @Override
                        public View onLoadView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                            View view = inflater.inflate(R.layout.fragment_tab_story, null);
                            LinearLayout linearLayout = view.findViewById(R.id.linearLayout_fragment_tab_story);

//                            for (int i = 0; i < storyList.length; i++) {
//                                addTextViewToParentView(linearLayout, 60, storyList[i]);
//                            }
                            RecyclerView recyclerView = view.findViewById(R.id.voice_recyclerView_in_fragment);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                                    LinearLayoutManager.VERTICAL, false);//这里我们使用默认的线性布局管理器,将其设为垂直显示
                            recyclerView.setLayoutManager(layoutManager);//设置布局管理器
                            List<String> list = new ArrayList<String>();
                            list.addAll(Arrays.asList(storyList));
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), list, R.layout.recyclerviewadaptervoice_play_bill_item);//实例化适配器
                            recyclerView.setAdapter(adapter);//设置适配器
                            return view;
                        }
                    });
                }
                break;
                // 安全界面
                case 4: {
                    final String[] storyList = {"白雪公主", "卖火柴的小女孩", "灰姑娘", "丑小鸭", "皇帝的新装", "拇指姑娘"};
                    tmp.setOnLoadViewListener(new TabContentFragment.OnLoadViewListener() {
                        @Override
                        public View onLoadView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                            View view = inflater.inflate(R.layout.fragment_tab_story, null);
                            LinearLayout linearLayout = view.findViewById(R.id.linearLayout_fragment_tab_story);

//                            for (int i = 0; i < storyList.length; i++) {
//                                addTextViewToParentView(linearLayout, 60, storyList[i]);
//                            }
                            RecyclerView recyclerView = view.findViewById(R.id.voice_recyclerView_in_fragment);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                                    LinearLayoutManager.VERTICAL, false);//这里我们使用默认的线性布局管理器,将其设为垂直显示
                            recyclerView.setLayoutManager(layoutManager);//设置布局管理器
                            List<String> list = new ArrayList<String>();
                            list.addAll(Arrays.asList(storyList));
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), list, R.layout.recyclerviewadaptervoice_play_bill_item);//实例化适配器
                            recyclerView.setAdapter(adapter);//设置适配器
                            return view;
                        }
                    });
                }
                break;

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
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
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
        }
    }

    public void playOrPause() {
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

}
