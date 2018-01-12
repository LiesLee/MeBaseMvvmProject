package com.common.utils

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

/**
 * Created by LiesLee on 2017/9/30.
 * Email: LiesLee@foxmail.com
 */
class DPermissionsUtil{

    /**
     *
    日历读写
    READ_CALENDAR
    WRITE_CALENDAR

    相机
    CAMERA

    通信录
    READ_CONTACTS
    WRITE_CONTACTS
    GET_ACCOUNTS

    定位
    ACCESS_FINE_LOCATION
    ACCESS_COARSE_LOCATION
    MICROPHONE
    RECORD_AUDIO

    电话（打电话，电话日志等等）
    READ_PHONE_STATE
    CALL_PHONE
    READ_CALL_LOG
    WRITE_CALL_LOG
    ADD_VOICEMAIL
    USE_SIP
    PROCESS_OUTGOING_CALLS
    SENSORS
    BODY_SENSORS

    信息
    SEND_SMS
    RECEIVE_SMS
    READ_SMS
    RECEIVE_WAP_PUSH
    RECEIVE_MMS

    存储卡读写
    READ_EXTERNAL_STORAGE
    WRITE_EXTERNAL_STORAGE
     *
     * */

    companion object {
        /**
         * 检查权限
         * example: Manifest.permission.READ_PHONE_STATE
         *  */
        @JvmStatic
        fun checkPermissions(vararg permissions: String, callback: ()-> Unit){
            Dexter.checkPermissions(object : MultiplePermissionsListener{
                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if(report.deniedPermissionResponses == null || report.deniedPermissionResponses.isEmpty()){
                        callback()
                    }
                }

            }, permissions.toList())
        }

        /**
         * 检查权限
         * example: Manifest.permission.READ_PHONE_STATE
         *  */
        @JvmStatic
        fun checkPermissionsWithResult(vararg permissions: String, callback: (hasPermissions: Boolean)-> Unit){
            Dexter.checkPermissions(object : MultiplePermissionsListener{

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if(report.deniedPermissionResponses == null || report.deniedPermissionResponses.isEmpty()){
                        callback(true)
                    } else {
                        callback(false)
                    }
                }
            }, permissions.toList())
        }
    }
}