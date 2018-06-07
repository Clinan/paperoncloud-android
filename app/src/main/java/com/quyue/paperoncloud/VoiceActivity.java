package com.quyue.paperoncloud;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.quyue.paperoncloud.fragment.TabContentFragment;
import com.quyue.paperoncloud.R;
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
    }

    private void initControlItem() {
        albumImageView = findViewById(R.id.Album_ImageView);

        albumLayout = findViewById(R.id.albumLayout);
        lyricsLayout = findViewById(R.id.lyricsLayout);

        playImageView = findViewById(R.id.playImageView);
        stopImageView = findViewById(R.id.stopImageView);
        miniPlayImageView = findViewById(R.id.miniPlayImageView);
        miniStopImageView = findViewById(R.id.miniStopImageView);


        playImageView.setOnClickListener(this);
        stopImageView.setOnClickListener(this);
        miniPlayImageView.setOnClickListener(this);
        miniStopImageView.setOnClickListener(this);

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

    public void addTextViewToParentView(ViewGroup parentView, int marginTop, String text) {
        TextView textView = new TextView(getApplicationContext());
        textView.setWidth(681);
        textView.setHeight(150);
        textView.setPadding(180, 0, 0, 0);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textView.setTextColor(Color.BLACK);
        textView.setBackground(getDrawable(R.drawable.voice_item_bg));

        // 设置margin参数
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(681, 150);
        lp.setMargins(0, marginTop, 0, 0);
        textView.setLayoutParams(lp);
        textView.setText(text);
        parentView.addView(textView);
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
                break;
            case R.id.stopImageView:

                playImageView.setVisibility(View.VISIBLE);
                stopImageView.setVisibility(View.GONE);
                break;
            case R.id.miniPlayImageView:
                miniPlayImageView.setVisibility(View.GONE);
                miniStopImageView.setVisibility(View.VISIBLE);
                break;
            case R.id.miniStopImageView:
                miniPlayImageView.setVisibility(View.VISIBLE);
                miniStopImageView.setVisibility(View.GONE);
                break;
        }
    }
}
