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


        const val ID_PREFERENCE_THETA_LIVEVIEW_RESOLUTION = IPreferencePropertyAccessor.THETA_LIVEVIEW_RESOLUTION
        const val ID_PREFERENCE_THETA_LIVEVIEW_RESOLUTION_DEFAULT_VALUE = IPreferencePropertyAccessor.THETA_LIVEVIEW_RESOLUTION_DEFAULT_VALUE


    }

}