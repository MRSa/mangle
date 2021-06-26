package jp.osdn.gokigen.gokigenassets.constants

import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor

interface ICameraConstantConvert
{
    companion object
    {
        const val ID_STRING_CAMERA_NOT_FOUND = R.string.camera_not_found
        const val ID_STRING_CAMERA_CONNECT_RESPONSE_NG = R.string.camera_connect_response_ng

        const val ID_STRING_DIALOG_TITLE_CONNECT_FAILED = R.string.dialog_title_connect_failed
        const val ID_STRING_DIALOG_BUTTON_RETRY = R.string.dialog_button_retry
        const val ID_STRING_DIALOG_BUTTON_NETWORK_SETTINGS = R.string.dialog_button_network_settings


        const val ID_STRING_CONNECT_START = R.string.connect_start
        const val ID_STRING_CONNECT_CHECK_WIFI = R.string.connect_check_wifi
        const val ID_STRING_CONNECT_CONNECT = R.string.connect_connect
        const val ID_STRING_CONNECT_CONNECTING = R.string.connect_connecting
        const val ID_STRING_CONNECT_CONNECTED = R.string.connect_connected
        const val ID_STRING_CONNECT_CHANGE_RUN_MODE = R.string.connect_change_run_mode
        const val ID_STRING_CONNECT_CAMERA_DETECTED = R.string.connect_camera_detected
        const val ID_STRING_CONNECT_CAMERA_SEARCH_REQUEST = R.string.connect_camera_search_request
        const val ID_STRING_CONNECT_CAMERA_FOUND = R.string.connect_camera_found
        const val ID_STRING_CONNECT_WAIT_REPLY_CAMERA = R.string.connect_camera_wait_reply
        const val ID_STRING_CONNECT_CAMERA_RECEIVED_REPLY = R.string.connect_camera_received_reply
        const val ID_STRING_CONNECT_CAMERA_REJECTED = R.string.connect_camera_rejected


        const val ID_PREFERENCE_THETA_LIVEVIEW_RESOLUTION = IPreferencePropertyAccessor.THETA_LIVEVIEW_RESOLUTION
        const val ID_PREFERENCE_THETA_LIVEVIEW_RESOLUTION_DEFAULT_VALUE = IPreferencePropertyAccessor.THETA_LIVEVIEW_RESOLUTION_DEFAULT_VALUE


    }

}