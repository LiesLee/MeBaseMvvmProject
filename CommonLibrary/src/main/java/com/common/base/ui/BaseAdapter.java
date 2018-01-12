package com.common.base.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.base.R;
import com.views.ViewsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter 基类,带自动加载更多、拖动、删除等等功能
 * 文档: https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/%E9%A6%96%E9%A1%B5
 * Created by LiesLee on 16/9/19.
 */
public abstract class BaseAdapter<T, E extends BaseViewHolder> extends BaseQuickAdapter<T, E> {
    /** 当前列表的总条数 */
    public int mCurrentCounter = 0;


    public BaseAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
        //ViewsHelper.initLoadMoreLayout(ctx, this);
       setEnableLoadMore(data!=null && data.size() > ViewsHelper.OPEN_LOAD_MORE_SIZE);
        mCurrentCounter = getData().size();
    }

    /**
     * 设置数据
     * @param data
     */
    public void setData(List<T> data){
        setNewData(data == null ? new ArrayList<T>() : data);
        mCurrentCounter = getData().size();
        setEnableLoadMore(getData().size() > ViewsHelper.OPEN_LOAD_MORE_SIZE);

    }

    @Override
    public void setNewData(@Nullable List<T> data) {
        super.setNewData(data == null ? new ArrayList<T>() : data);
        mCurrentCounter = getData().size();
        setEnableLoadMore(getData().size() > ViewsHelper.OPEN_LOAD_MORE_SIZE);
    }

    /**
     * 添加更多数据
     * @param data  数据
     * @param totalCounter  服务器最大条数
     */
    public void addNewData(List<T> data, int totalCounter){
        addData(data == null ? new ArrayList<T>() : data);
        //加载完毕
        loadMoreComplete();
        mCurrentCounter = getData().size();
        if(mCurrentCounter >= totalCounter){
            loadMoreEnd(getData().size() < ViewsHelper.OPEN_LOAD_MORE_SIZE);
        }
    }

    @Override
    public void setOnLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener, RecyclerView recyclerView) {
        if(getData().size() >= ViewsHelper.OPEN_LOAD_MORE_SIZE){
            super.setOnLoadMoreListener(requestLoadMoreListener, recyclerView);
        }

    }

    /**
     * 获取当前position在数据列表的位置,不包括header
     * @param baseViewHolder
     * @return
     */
    public int getFinalPositionOnList(BaseViewHolder baseViewHolder){
        return baseViewHolder.getLayoutPosition() - getHeaderLayoutCount();
    }

    /**
     * 获取当前position在adapter的位置,包括header在内
     * @param baseViewHolder
     * @return
     */
    public int getFinalPositionOnAdapter(BaseViewHolder baseViewHolder){
        return baseViewHolder.getLayoutPosition() + getHeaderLayoutCount();
    }

    /**
     * 获取当前position在adapter的位置,包括header在内
     * @param positionOnList
     * @return
     */
    public int getFinalPositionOnAdapter(int positionOnList){
        return positionOnList + getHeaderLayoutCount();
    }




}