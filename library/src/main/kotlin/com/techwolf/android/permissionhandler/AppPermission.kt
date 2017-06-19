package com.techwolf.android.permissionhandler

import android.Manifest

sealed class AppPermission(val permissionName: String,
                           val requestCode: Int,
                           val deniedMessageId: Int,
                           val explanationMessageId: Int) {
    companion object {
        val permissions: List<AppPermission> by lazy {
            listOf(
                    READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE,
                    ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION,
                    CAMERA, CALL_PHONE,
                    SEND_SMS, READ_SMS, RECEIVE_SMS, RECEIVE_MMS,
                    READ_CALENDAR, WRITE_CALENDAR,
                    READ_CONTACTS, WRITE_CONTACTS, GET_ACCOUNTS,
                    RECORD_AUDIO, READ_CALL_LOG,
                    READ_PHONE_STATE, ADD_VOICEMAIL, WRITE_CALL_LOG,
                    USE_SIP, PROCESS_OUTGOING_CALLS, BODY_SENSORS,
                    RECEIVE_MMS, RECEIVE_WAP_PUSH
            )
        }
    }

    /**CAMERA PERMISSIONS**/
    object CAMERA : AppPermission(Manifest.permission.CAMERA, 1, R.string.permission_camera_denied, R.string.permission_camera_explanation)

    /**CALENDAR PERMISSIONS**/
    object READ_CALENDAR : AppPermission(Manifest.permission.READ_CALENDAR, 2, R.string.permission_read_calendar_denied, R.string.permission_read_calendar_explanation)

    object WRITE_CALENDAR : AppPermission(Manifest.permission.WRITE_CALENDAR, 3, R.string.permission_write_calendar_denied, R.string.permission_write_calendar_explanation)

    /**CONTACTS PERMISSIONS**/
    object READ_CONTACTS : AppPermission(Manifest.permission.READ_CONTACTS, 4, R.string.permission_read_contacts_denied, R.string.permission_read_contacts_explanation)

    object WRITE_CONTACTS : AppPermission(Manifest.permission.WRITE_CONTACTS, 5, R.string.permission_write_contacts_denied, R.string.permission_write_contacts_explanation)
    object GET_ACCOUNTS : AppPermission(Manifest.permission.GET_ACCOUNTS, 6, R.string.permission_get_account_denied, R.string.permission_get_account_explanation)

    /**LOCATION PERMISSIONS**/
    object ACCESS_FINE_LOCATION : AppPermission(Manifest.permission.ACCESS_FINE_LOCATION, 7, R.string.permission_camera_denied, R.string.permission_camera_explanation)

    object ACCESS_COARSE_LOCATION : AppPermission(Manifest.permission.ACCESS_COARSE_LOCATION, 8, R.string.permission_camera_denied, R.string.permission_camera_explanation)

    /**MICROPHONE PERMISSIONS**/
    object RECORD_AUDIO : AppPermission(Manifest.permission.RECORD_AUDIO, 9, R.string.permission_record_audio_denied, R.string.permission_record_audio_explanation)

    /**PHONE PERMISSIONS**/
    object READ_PHONE_STATE : AppPermission(Manifest.permission.READ_PHONE_STATE, 10, R.string.permission_read_phone_state_denied, R.string.permission_read_phone_state_explanation)

    object CALL_PHONE : AppPermission(Manifest.permission.CALL_PHONE, 11, R.string.permission_call_phone_denied, R.string.permission_call_phone_explanation)
    object READ_CALL_LOG : AppPermission(Manifest.permission.READ_CALL_LOG, 12, R.string.permission_read_call_log_denied, R.string.permission_read_call_log_explanation)
    object WRITE_CALL_LOG : AppPermission(Manifest.permission.WRITE_CALL_LOG, 13, R.string.permission_write_call_log_denied, R.string.permission_write_call_log_explanation)
    object ADD_VOICEMAIL : AppPermission(Manifest.permission.ADD_VOICEMAIL, 14, R.string.permission_add_voicemail_denied, R.string.permission_add_voicemail_explanation)
    object USE_SIP : AppPermission(Manifest.permission.USE_SIP, 15, R.string.permission_use_sip_denied, R.string.permission_use_sip_explanation)
    object PROCESS_OUTGOING_CALLS : AppPermission(Manifest.permission.PROCESS_OUTGOING_CALLS, 16, R.string.permission_process_outgoing_calls_denied, R.string.permission_process_outgoing_calls_explanation)

    /**SENSOR PERMISSIONS**/
    object BODY_SENSORS : AppPermission(Manifest.permission.BODY_SENSORS, 17, R.string.permission_body_sensor_denied, R.string.permission_body_sensor_explanation)

    /**SMS PERMISSIONS**/
    object SEND_SMS : AppPermission(Manifest.permission.SEND_SMS, 18, R.string.permission_send_sms_denied, R.string.permission_send_sms_explanation)

    object RECEIVE_SMS : AppPermission(Manifest.permission.RECEIVE_SMS, 19, R.string.permission_receive_sms_denied, R.string.permission_receive_sms_explanation)
    object READ_SMS : AppPermission(Manifest.permission.READ_SMS, 20, R.string.permission_read_sms_denied, R.string.permission_read_sms_explanation)
    object RECEIVE_WAP_PUSH : AppPermission(Manifest.permission.RECEIVE_WAP_PUSH, 21, R.string.permission_receive_wap_push_denied, R.string.permission_receive_wap_push_explanation)
    object RECEIVE_MMS : AppPermission(Manifest.permission.RECEIVE_MMS, 22, R.string.permission_receive_mms_denied, R.string.permission_receive_mms_explanation)

    /**READ/WRITE TO STORAGE PERMISSIONS**/
    object READ_EXTERNAL_STORAGE : AppPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 23, R.string.permission_read_ext_storage_denied, R.string.permission_read_ext_storage_explanation)

    object WRITE_EXTERNAL_STORAGE : AppPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 24, R.string.permission_write_ext_storage_denied, R.string.permission_write_ext_storage_explanation)
}