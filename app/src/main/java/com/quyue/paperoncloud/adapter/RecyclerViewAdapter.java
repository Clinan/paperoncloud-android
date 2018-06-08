package com.quyue.paperoncloud.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quyue.paperoncloud.R;
import com.quyue.paperoncloud.db.entity.BaseEntity;

import java.util.List;

/**
 * RecyclerView适配器
 * Created by arter on 2018/6/5.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mDataSet;
    private List<? extends BaseEntity> entityList;
    private LayoutInflater mInflater;
    private int LayoutResId;
    private boolean isEntity = false;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public RecyclerViewAdapter(Context context, List<String> dataSet, @LayoutRes int resource) {
        this.mContext = context;
        this.mDataSet = dataSet;
        LayoutResId = resource;
        mInflater = LayoutInflater.from(context);
    }

    public RecyclerViewAdapter(Context context, List<? extends BaseEntity> entityList, @LayoutRes int resource, boolean isEntity) {
        this.isEntity = true;
        this.entityList = entityList;
        LayoutResId = resource;
        mInflater = LayoutInflater.from(context);
    }


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
        if (onRecyclerViewItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewItemClickListener.onItemClick(v);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onRecyclerViewItemClickListener.onItemLongClick(v);
                }
            });
        }
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
        if (isEntity && entityList != null) {
            holder.mTextView.setText(entityList.get(position).getName());
            holder.idTextView.setText(String.valueOf(entityList.get(position).getId()));
        } else {
            holder.mTextView.setText(mDataSet.get(position));
            holder.idTextView.setText("-1");
        }

    }

    @Override
    public int getItemCount() {
        if (isEntity && entityList != null) {
            return entityList.size();
        } else {
            return mDataSet.size();
        }
    }

    public OnRecyclerViewItemClickListener getOnRecyclerViewItemClickListener() {
        return onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    //自定义的ViewHoder，持有item的所有控件
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        protected TextView idTextView;

        public ViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.text);
            idTextView = view.findViewById(R.id.id_tv);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);

        boolean onItemLongClick(View view);
    }

}

