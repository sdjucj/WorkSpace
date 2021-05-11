package com.cj.framework.constant

import android.Manifest.permission
import android.os.Build
import androidx.annotation.StringDef

/**
 * 权限
 *
 * @author CJ
 * @date 2021/4/26 19:23
 */
object PermissionConstants {

    const val CALENDAR = "CALENDAR"//日历
    const val CAMERA = "CAMERA"//相机
    const val CONTACTS = "CONTACTS"//联系人
    const val LOCATION = "LOCATION"//位置
    const val MICROPHONE = "MICROPHONE"//音频
    const val PHONE = "PHONE"//电话
    const val SENSORS = "SENSORS"//传感器
    const val SMS = "SMS"//短信
    const val STORAGE = "STORAGE"//存储
    const val ACTIVITY_RECOGNITION = "ACTIVITY_RECOGNITION"//行为认知

    private val GROUP_CALENDAR = arrayOf(
        permission.READ_CALENDAR, permission.WRITE_CALENDAR
    )

    private val GROUP_CAMERA = arrayOf(
        permission.CAMERA
    )

    private val GROUP_CONTACTS = arrayOf(
        permission.READ_CONTACTS, permission.WRITE_CONTACTS, permission.GET_ACCOUNTS
    )

    private val GROUP_LOCATION =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(
                permission.ACCESS_FINE_LOCATION,
                permission.ACCESS_COARSE_LOCATION,
                permission.ACCESS_BACKGROUND_LOCATION
            )
        } else {
            arrayOf(
                permission.ACCESS_FINE_LOCATION,
                permission.ACCESS_COARSE_LOCATION
            )
        }

    private val GROUP_MICROPHONE = arrayOf(
        permission.RECORD_AUDIO
    )

    private val GROUP_PHONE =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            arrayOf(
                permission.READ_PHONE_STATE, permission.READ_PHONE_NUMBERS, permission.CALL_PHONE,
                permission.READ_CALL_LOG, permission.WRITE_CALL_LOG, permission.ADD_VOICEMAIL,
                permission.USE_SIP, permission.PROCESS_OUTGOING_CALLS, permission.ANSWER_PHONE_CALLS
            )
        } else {
            arrayOf(
                permission.READ_PHONE_STATE, permission.CALL_PHONE,
                permission.READ_CALL_LOG, permission.WRITE_CALL_LOG, permission.ADD_VOICEMAIL,
                permission.USE_SIP, permission.PROCESS_OUTGOING_CALLS
            )
        }

    private val GROUP_SENSORS = arrayOf(
        permission.BODY_SENSORS
    )

    private val GROUP_SMS = arrayOf(
        permission.SEND_SMS, permission.RECEIVE_SMS, permission.READ_SMS,
        permission.RECEIVE_WAP_PUSH, permission.RECEIVE_MMS
    )
    private val GROUP_STORAGE = arrayOf(
        permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE
    )
    private val GROUP_ACTIVITY_RECOGNITION =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(
                permission.ACTIVITY_RECOGNITION
            )
        } else {
            emptyArray()
        }

    @StringDef(
        CALENDAR,
        CAMERA,
        CONTACTS,
        LOCATION,
        MICROPHONE,
        PHONE,
        SENSORS,
        SMS,
        STORAGE,
        ACTIVITY_RECOGNITION
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class PermissionGroup

    fun getPermissions(@PermissionGroup permission: String): Array<String> {
        when (permission) {
            CALENDAR -> return GROUP_CALENDAR
            CAMERA -> return GROUP_CAMERA
            CONTACTS -> return GROUP_CONTACTS
            LOCATION -> return GROUP_LOCATION
            MICROPHONE -> return GROUP_MICROPHONE
            PHONE -> return GROUP_PHONE
            SENSORS -> return GROUP_SENSORS
            SMS -> return GROUP_SMS
            STORAGE -> return GROUP_STORAGE
            ACTIVITY_RECOGNITION -> return GROUP_ACTIVITY_RECOGNITION
        }
        return arrayOf(permission)
    }

}