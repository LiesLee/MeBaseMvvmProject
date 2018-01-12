package com.common.base.ui;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.views.ViewsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiesLee on 2017/10/30.
 * Email: LiesLee@foxmail.com
 */

public abstract class BaseAdapterForDrag<T, E extends BaseViewHolder>  extends BaseItemDraggableAdapter<T, E > {
    /** 当前列表的总数量 */
    public int mCurrentCounter = 0;
    public BaseAdapterForDrag(int layoutResId, List<T> data) {
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

    /**
     * 添加更多数据
     * @param data  数据
     * @param totalCounter  服务器最大数
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
