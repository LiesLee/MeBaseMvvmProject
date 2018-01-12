package com.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.common.base.R;

import com.common.utils.TimeUtil;
import com.common.utils.UUID;
import com.views.util.ToastUtil;
import com.views.util.ViewUtil;

import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.extra.header.MaterialHeader;
import me.dkzwm.widget.srl.utils.PixelUtl;

/**
 * Created by LiesLee on 2016/7/8.
 * Email: LiesLee@foxmail.com
 */
public class ViewsHelper {
    /**
     * 大于这个数开启adapter加载更多
     */
    public static int OPEN_LOAD_MORE_SIZE = 5;

    public static <T extends SmoothRefreshLayout.OnRefreshListener> SmoothRefreshLayout initSmoothRefreshLayoutByMaterial
            (@NonNull SmoothRefreshLayout smoothRefreshLayout, @NonNull T listener) {
        MaterialHeader header = new MaterialHeader(smoothRefreshLayout.getContext());
        header.setPadding(0, PixelUtl.dp2px(smoothRefreshLayout.getContext(), 2), 0, PixelUtl.dp2px(smoothRefreshLayout.getContext(), 8));
        header.setColorSchemeColors(smoothRefreshLayout.getContext().getApplicationContext().getResources().getIntArray(R.array.Material_colors));
        smoothRefreshLayout.setHeaderView(header);
        smoothRefreshLayout.setDisableLoadMore(true);
        smoothRefreshLayout.setEnableNextPtrAtOnce(true);
        smoothRefreshLayout.setEnableKeepRefreshView(true);
        smoothRefreshLayout.setDisableWhenHorizontalMove(true);
        smoothRefreshLayout.setEnableCheckFingerInsideHorView(true);
        smoothRefreshLayout.setEnableOverScroll(true);//越界回弹
        smoothRefreshLayout.setOverScrollDurationRatio(0.2f);
        smoothRefreshLayout.setMaxOverScrollDuration(200);
        smoothRefreshLayout.setOnRefreshListener(listener);
        return smoothRefreshLayout;
    }

//    /**
//     * 初始化下拉刷新控件默认参数
//     * @param context
//     * @param ptrFrameLayout
//     */
//    public static void init_PTR_common_params(Context context, PtrFrameLayout ptrFrameLayout){
//        StoreHouseHeader header = new StoreHouseHeader(context);
//        header.setPadding(0, ViewUtil.dip2px(1, context), 0, 0);
//        // using string array from resource xml file
//        header.initWithStringArray(R.array.vip_time_logo);
//        if("com.shihui.userapp".equals(context.getPackageName())){
//            header.setTextColor(Color.parseColor("#bb9a55"));
//        }else{
//            header.setTextColor(Color.parseColor("#1f97f1"));
//        }
//        header.setLineWidth(ViewUtil.dip2px(1f, context));
//        ptrFrameLayout.setResistance(1.7f);
//        ptrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
//        ptrFrameLayout.setDurationToClose(200);
//        ptrFrameLayout.setDurationToCloseHeader(800);
//        ptrFrameLayout.setHeaderView(header);
//        ptrFrameLayout.addPtrUIHandler(header);
//        // default is false
//        ptrFrameLayout.setPullToRefresh(false);
//        // default is true
//        ptrFrameLayout.setKeepHeaderWhenRefresh(true);
//    }
//    /**
//     * 初始化Material风格下拉刷新控件默认参数
//     * @param context
//     * @param ptrFrameLayout
//     */
//    public static void init_PTR_Material_params(Context context, PtrFrameLayout ptrFrameLayout){
//        // header
//        MaterialHeader header = new MaterialHeader(context);
//        int[] colors = context.getResources().getIntArray(R.array.google_colors);
//        header.setColorSchemeColors(colors);
//        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
//        header.setPadding(0, ViewUtil.dp2px(context,10), 0, ViewUtil.dp2px(context,10));
//        header.setPtrFrameLayout(ptrFrameLayout);
//
//        ptrFrameLayout.setResistance(1.7f);
//        ptrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
//        ptrFrameLayout.setDurationToClose(200);
//        ptrFrameLayout.setDurationToCloseHeader(800);
//        ptrFrameLayout.setHeaderView(header);
//        ptrFrameLayout.addPtrUIHandler(header);
//        //是否不等动画返回可继续下拉刷新
//        ptrFrameLayout.setEnabledNextPtrAtOnce(false);
//        // default is false
//        ptrFrameLayout.setPullToRefresh(false);
//        // default is true
//        ptrFrameLayout.setKeepHeaderWhenRefresh(true);
//    }

//    /**
//     * 为QuickAdapter设置自动加载更多的loading布局
//     *
//     * @param context
//     * @param adapter
//     */
//    public static void initLoadMoreLayout(Context context, BaseQuickAdapter adapter) {
//        LinearLayout loadMoreLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_load_more_layout, null);
//        ViewGroup.LayoutParams layoup = loadMoreLayout.getLayoutParams();
//        if (layoup == null) {
//            layoup = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//        loadMoreLayout.setLayoutParams(layoup);
//        ProgressWheel progressWheel = (ProgressWheel) loadMoreLayout.findViewById(R.id.pw_custom_loading);
//        progressWheel.setBarWidth(ViewUtil.dip2px(4, context));
//
//        progressWheel.setBarColor(context.getResources().getColor(R.color.cus_blue));
//
//
//        progressWheel.spin();
//
//    }

    /**
     * 默认Loading小弹窗
     *
     * @param context
     * @param listener
     * @return
     */
    public static Dialog initDefaultLoadingDialog(Context context, DialogInterface.OnCancelListener listener) {

        Dialog default_loading_dialog = new Dialog(context, R.style.custom_dialog);
        default_loading_dialog.setContentView(R.layout.dialog_default_loading);

        ProgressWheel progressWheel = (ProgressWheel) default_loading_dialog.findViewById(R.id.pw_custom_loading);
        View ll_root = default_loading_dialog.findViewById(R.id.ll_root);
        TextView tv_tips = (TextView) default_loading_dialog.findViewById(R.id.tv_tips);
        progressWheel.setBarWidth(ViewUtil.dip2px(4, context));

        progressWheel.setBarColor(context.getResources().getColor(R.color.colorAccent));
        ll_root.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_default_dialog_bg_white));
        tv_tips.setTextColor(context.getResources().getColor(R.color.grey));


        progressWheel.spin();

        default_loading_dialog.setCanceledOnTouchOutside(false);
        //当dialog被取消时,取消相应被指定的网络请求
        default_loading_dialog.setOnCancelListener(listener);
        return default_loading_dialog;
    }

    /**
     * 下载Loading小弹窗
     *
     * @param context
     * @param listener
     * @return
     */
    public static Dialog downLoadingDialog(Context context, String tips, DialogInterface.OnCancelListener listener) {

        Dialog default_loading_dialog = new Dialog(context, R.style.custom_dialog);
        default_loading_dialog.setContentView(R.layout.dialog_default_loading);

        ProgressWheel progressWheel = (ProgressWheel) default_loading_dialog.findViewById(R.id.pw_custom_loading);
        View ll_root = default_loading_dialog.findViewById(R.id.ll_root);
        TextView tv_tips = (TextView) default_loading_dialog.findViewById(R.id.tv_tips);
        progressWheel.setBarWidth(ViewUtil.dip2px(4, context));

        progressWheel.setBarColor(context.getResources().getColor(R.color.colorAccent));
        ll_root.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_default_dialog_bg_white));
        tv_tips.setTextColor(context.getResources().getColor(R.color.grey));

        tv_tips.setText(tips);
        progressWheel.spin();
        default_loading_dialog.setCanceledOnTouchOutside(false);
        default_loading_dialog.setCancelable(false);
        //当dialog被取消时,取消相应被指定的网络请求
        default_loading_dialog.setOnCancelListener(listener);
        return default_loading_dialog;
    }


    public static void showShakeAnim(Context context, View view, String toast) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake_x);
        view.startAnimation(shake);
        view.requestFocus();
        if (!TextUtils.isEmpty(toast)) {
            ToastUtil.showLongToast(context, toast);
        }

    }

    public static boolean phoneNumberValid(String number) {
        // 手机号固定在5-20范围内
        if (number.length() < 5 || number.length() > 20) {
            return false;
        }

        String match = "";
        if (number.length() != 11) {
            return false;
        } else {
            // match = "^[1]{1}[0-9]{2}[0-9]{8}$";
            match = "^(1[3456789])\\d{9}$";
        }

        // 正则匹配
        if (!"".equals(match)) {
            return number.matches(match);
        }
        return true;
    }

    public static boolean matchesPassword(String password){
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        return password.matches(regex);
    }
    public static boolean matchesEmail(String email){
        String regex =  "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return email.matches(regex);
    }


}

class MyLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected int getLoadingViewId() {
        return 0;
    }

    @Override
    protected int getLoadFailViewId() {
        return 0;
    }

    @Override
    protected int getLoadEndViewId() {
        return 0;
    }
}

