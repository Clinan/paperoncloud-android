package com.quyue.paperoncloud.fragment;

/**
 * Created by arter on 2018/6/6.
 */

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quyue.paperoncloud.R;


/**
 * Created by yifeng on 16/8/3.
 */
public class TabContentFragment extends Fragment {

    private static final String EXTRA_CONTENT = "content";
    private static final String VIEW_ID = "ViewId";
    private View contentView;
    private OnLoadViewListener onLoadViewListener;

    public static TabContentFragment newInstance(String content, @LayoutRes int ViewId) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_CONTENT, content);
        arguments.putInt(VIEW_ID, ViewId);
        TabContentFragment tabContentFragment = new TabContentFragment();
        tabContentFragment.setArguments(arguments);
        return tabContentFragment;
    }

    public static TabContentFragment newInstance(String content) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_CONTENT, content);
        arguments.putInt(VIEW_ID, R.layout.fragment_tab_content);
        TabContentFragment tabContentFragment = new TabContentFragment();
        tabContentFragment.setArguments(arguments);
        return tabContentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        contentView = inflater.inflate(getArguments().getInt(VIEW_ID), null);
        if (onLoadViewListener != null) {
            contentView = onLoadViewListener.onLoadView(inflater, container, savedInstanceState);
        }
//        ((TextView) contentView.findViewById(R.id.tv_content)).setText("我");
        return contentView;
    }

    public void setOnLoadViewListener(OnLoadViewListener viewListener) {
        onLoadViewListener = viewListener;
    }

    public interface OnLoadViewListener {
        View onLoadView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    }
}