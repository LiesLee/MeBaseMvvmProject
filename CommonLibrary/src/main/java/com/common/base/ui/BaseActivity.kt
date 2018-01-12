package com.common.base.ui

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.common.MyActivityManager
import com.common.annotation.ActivityFragmentInject
import com.common.base.R

/**
 * Created by LiesLee on 2018/1/11.
 * Email: LiesLee@foxmail.com
 */
abstract class BaseActivity : AppCompatActivity() {
    /**
     * 布局的id
     */
    protected var mContentViewId: Int = 0
    /**
     * 菜单的id
     */
    private var mMenuId: Int = 0
    /**
     * Toolbar标题
     */
    private var mToolbarTitle: Int = 0
    /**
     * 默认选中的菜单项
     */
    private var mMenuDefaultCheckedItem: Int = 0
    /**
     * Toolbar左侧按钮的样式
     */
    private var mToolbarIndicator: Int = 0
    private var mToolbarTitleString: String? = null
    protected val baseActivity: BaseActivity = this

    /**
     * 标题
     */
    protected var tv_title: TextView? = null
    protected var toolbar: Toolbar? = null
    protected var default_loading_dialog: Dialog? = null
    protected var onClickBack: OnClickBack? = null
    private var ib_back: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        MyActivityManager.getInstance().addActivity(this)
        if (javaClass.isAnnotationPresent(ActivityFragmentInject::class.java)) {
            val annotation = javaClass
                    .getAnnotation(ActivityFragmentInject::class.java)
            mContentViewId = annotation.contentViewId
            mMenuId = annotation.menuId
            mToolbarTitle = annotation.toolbarTitle
            mToolbarTitleString = annotation.toolbarTitleString
            mToolbarIndicator = annotation.toolbarIndicator
            mMenuDefaultCheckedItem = annotation.menuDefaultCheckedItem
        } else {
            throw RuntimeException(
                    "Class must add annotations of ActivityFragmentInitParams.class")
        }
        initView()
        initData()
    }

    private fun initToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        //注:UI设计风格不是material design, setToolbar***等方法不符合要求
        ib_back = findViewById<ImageButton>(R.id.ib_back)
        tv_title = findViewById<TextView>(R.id.tv_title)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.setTitle(null)
            tv_title?.apply {
                if (mToolbarTitle != -1) setText(mToolbarTitle) else text = mToolbarTitleString
            }


            ib_back?.setOnClickListener {
                if (onClickBack != null) {
                    onClickBack!!.onClick()
                } else {
                    finish()
                }
            }

            if (mToolbarIndicator != -1) {
                ib_back?.setImageResource(mToolbarIndicator)
                //setToolbarIndicator(mToolbarIndicator);
            }


        }
    }

    /** 建议在onResume后再显示Dialog */
    protected abstract fun initView()

    /**
     * 初始化数据，在主线程
     * 建议在onResume后再显示Dialog
     */
    abstract fun initData()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (mMenuId == -1) {
            return false
        } else {
            menuInflater.inflate(mMenuId, menu)
            return true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home && mToolbarIndicator == -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            } else {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (onClickBack != null && keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            onClickBack!!.onClick()
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    /**
     * toast提示
     * @param msg 信息
     * @param duration 显示时长, 默认Toast.LENGTH_SHORT
     */
    fun toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        Toast.makeText(baseActivity, msg, duration).show()
    }


    override fun finish() {
        super.finish()
        MyActivityManager.getInstance().removeActivity(this)
    }




    protected fun setToolBarTitleString(str: String) {
        tv_title?.text = str
    }
    protected fun setToolBarTitleStringRes(@StringRes resId: Int) {
        tv_title?.setText(resId)
    }

}

/** 点击返回键回调  */
interface OnClickBack {
    fun onClick()
}