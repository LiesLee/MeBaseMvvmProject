package com.common.base.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import com.common.base.R;
import com.views.ViewsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter 基类,带自动加载更多、拖动、删除等等功能
 * 文档: https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/%E9%A6%96%E9%A1%B5
 * 领取奖励  特殊 adapter
 */
public abstract class BaseAdapterForMultiItem<T extends MultiItemEntity, E extends BaseViewHolder> extends BaseMultiItemQuickAdapter<T, E> {


    private int mCurrentCounter = 0;

    public BaseAdapterForMultiItem(Context ctx, List<T> data) {
        super(data);
        setEnableLoadMore(data!=null && data.size() > ViewsHelper.OPEN_LOAD_MORE_SIZE);
        mCurrentCounter = getData().size();
    }

    /*
     * 设置数据
     * @param data
     */
    public void setData(List<T> data){
        setNewData(data == null ? new ArrayList<T>() : data);
        mCurrentCounter = getData().size();
        setEnableLoadMore(getData().size() > ViewsHelper.OPEN_LOAD_MORE_SIZE);

    }

    /**
     * 在尾部添加数据
     * @param data
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