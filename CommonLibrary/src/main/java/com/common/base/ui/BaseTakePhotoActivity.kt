package com.common.base.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import com.common.base.R
import com.common.base.presenter.BasePresenter
import com.common.base.presenter.BasePresenterImpl
import com.jph.takephoto.app.TakePhoto
import com.jph.takephoto.app.TakePhotoImpl
import com.jph.takephoto.compress.CompressConfig
import com.jph.takephoto.compress.CompressImage
import com.jph.takephoto.compress.CompressImageImpl
import com.jph.takephoto.model.*
import com.jph.takephoto.permission.InvokeListener
import com.jph.takephoto.permission.PermissionManager
import com.jph.takephoto.permission.TakePhotoInvocationHandler
import com.socks.library.KLog
import com.views.util.ToastUtil
import me.shaohui.advancedluban.Luban
import org.jetbrains.anko.selector
import java.io.File

/**
 * Created by LiesLee on 2017/9/30.
 * Email: LiesLee@foxmail.com
 */
abstract class BaseTakePhotoActivity: BaseActivity() , TakePhoto.TakeResultListener, InvokeListener {
    private var takePhoto: TakePhoto? = null
    private var invokeParam: InvokeParam? = null

    /**
     * 获取TakePhoto实例
     * @return
     */
    fun getTakePhoto(): TakePhoto {
        if (takePhoto == null) {
            takePhoto = TakePhotoInvocationHandler.of(this).bind(TakePhotoImpl(this@BaseTakePhotoActivity, this@BaseTakePhotoActivity)) as TakePhoto?
        }
        return takePhoto as TakePhoto
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this)
    }

    override fun invoke(invokeParam: InvokeParam?): PermissionManager.TPermissionType {
        val type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam!!.method)
        if (PermissionManager.TPermissionType.WAIT == type) {
            this.invokeParam = invokeParam
        }
        return type
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        getTakePhoto().onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        getTakePhoto().onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    /** 最大选择的图片数量 */
    protected abstract fun getMaxCount():Int
    /** 当前已经选择的数量 */
    protected abstract fun currentCount(): Int


    protected fun showSelectePhotoItems(){
        if(getMaxCount() > currentCount()){
            val countries = listOf("相册", "相机")
            selector(null, countries, { dialogInterface, i ->
                setConfig()
                when(i){
                    0 -> selectPhotos(getMaxCount() - currentCount())
                    1 -> takeAphoto()
                }
            })
        }else{
            ToastUtil.showShortToast(baseActivity, "最多选择${getMaxCount()}张图片")
        }

    }

    private fun takeAphoto(){
        val file = File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg")
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        val imageUri = Uri.fromFile(file)
        takePhoto?.onPickFromCapture(imageUri)
    }

    private fun selectPhotos(count: Int){
        takePhoto?.onPickMultiple(count)
    }

    protected fun setConfig(): TakePhoto{
        getTakePhoto()
        val builder = TakePhotoOptions.Builder()
        //用控件自带
            builder.setWithOwnGallery(true)

        //是否矫正旋转角度
            builder.setCorrectImage(true)

        takePhoto?.apply {
            setTakePhotoOptions(builder.create())
            //压缩后是否保存原图
            val enableRawFile = true
            val config: CompressConfig
            //鲁班压缩
            val option = LubanOptions.Builder()
                    .setMaxSize(1024 * 500)
                    .create()
            config = CompressConfig.ofLuban(option)
            config.enableReserveRaw(enableRawFile)
//
            onEnableCompress(config, true)
        }
        return takePhoto!!
    }
    /** 图片压缩（使用鲁班）使用完成以后记得清空鲁班缓存 File(cacheDir, "/luban_disk_cache") */
    protected fun compressImages(maxSize : Int = 1024 * 500, list : ArrayList<TImage>, trans: (isCompressSuccess:Boolean,results: Collection<String>?, msg: String) -> Unit){
        //压缩后是否保存原图
        val enableRawFile = true
        val config: CompressConfig
        //鲁班压缩
        val option = LubanOptions.Builder()
                .setMaxSize(maxSize)
                .create()
        config = CompressConfig.ofLuban(option)
        config.enableReserveRaw(enableRawFile)

        CompressImageImpl.of(baseContext, config, list, object : CompressImage.CompressListener {
            override fun onCompressSuccess(images: ArrayList<TImage>) {
                runOnUiThread {
                    trans(true, images.map { it.compressPath }, "压缩成功")
                }

            }

            override fun onCompressFailed(images: ArrayList<TImage>, msg: String) {
                runOnUiThread {
                    trans(false, if (images!=null && images.size>0) images.map { it.compressPath } else null, msg)
                }
            }
        }).compress()


    }

    override fun takeFail(result: TResult?, msg: String?) {
        toast(msg)
    }

    override fun takeCancel() {

    }

    override fun onDestroy() {
        clearCache()
        super.onDestroy()
    }
    private fun getPhotoCacheDir(cacheName: String): File? {
        val cacheDir = cacheDir
        if (cacheDir != null) {
            val result = File(cacheDir, cacheName)
            return if (!result.mkdirs() && (!result.exists() || !result.isDirectory)) {
                // File wasn't able to create a directory, or the result exists but not a directory
                null
            } else result
        }
        return null
    }

    /**
     * 清空Luban所产生的缓存
     * Clears the cache generated by Luban
     * @return
     */
    fun clearCache() {
        if(getPhotoCacheDir("luban_disk_cache")!=null){
            deleteFile(getPhotoCacheDir("luban_disk_cache")!!)
        }
    }

    /**
     * 清空目标文件或文件夹
     * Empty the target file or folder
     */
    private fun deleteFile(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory) {
            for (file in fileOrDirectory.listFiles()!!) {
                deleteFile(file)
            }
        }
        fileOrDirectory.delete()
    }
}

