package com.quyue.paperoncloud;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quyue.paperoncloud.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by arter on 2018/6/5.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mDataSet;
    private LayoutInflater mInflater;
    private int LayoutResId;

    //提供一个合适的构造方法
    public RecyclerViewAdapter(Context context, List<String> dataSet, @LayoutRes int resource) {
        this.mContext = context;
        this.mDataSet = dataSet;
        LayoutResId = resource;
        mInflater = LayoutInflater.from(context);
    }

    //提供一个合适的构造方法
    public RecyclerViewAdapter(Context context, List<String> dataSet) {
        this.mContext = context;
        this.mDataSet = dataSet;
        LayoutResId = R.layout.recyclerviewadapter_item;
        mInflater = LayoutInflater.from(context);
    }


    /**
     * 将布局转换为View并传递给自定义的MyViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(LayoutResId, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    /**
     * 建立起MyViewHolder中视图与数据的关联
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    //自定义的ViewHoder，持有item的所有控件
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.text);
        }
    }

}

