package com.common.base.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.common.annotation.ActivityFragmentInject

/**
 * Created by LiesLee on 2018/1/11.
 * Email: LiesLee@foxmail.com
 */
abstract class BaseFragment : Fragment(){
    protected val baseFragment: BaseFragment? = this
    protected val baseActivity: BaseActivity = this.activity as BaseActivity
    protected var fragmentRootView: View? = null
    protected var mContentViewId: Int = 0


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (null == fragmentRootView) {
            if (javaClass.isAnnotationPresent(ActivityFragmentInject::class.java)) {
                val annotation = javaClass
                        .getAnnotation(ActivityFragmentInject::class.java)
                mContentViewId = annotation.contentViewId
            } else {
                throw RuntimeException(
                        "Class must add annotations of ActivityFragmentInitParams.class")
            }
            fragmentRootView = inflater!!.inflate(mContentViewId, container, false)
        }

        return fragmentRootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initData()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        fragmentRootView?.apply {
            (parent as ViewGroup).removeView(this)
        }
    }

    protected abstract fun initView(fragmentRootView: View?)
    abstract fun initData()
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

    protected fun <T : View?> findViewById(id: Int): T? {
        return if (fragmentRootView == null) null else fragmentRootView!!.findViewById<T>(id)
    }
}