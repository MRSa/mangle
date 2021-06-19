package jp.osdn.gokigen.gokigenassets.constants

import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.CACHE_LIVE_VIEW_PICTURES
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.CAPTURE_ONLY_LIVE_VIEW
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.CAPTURE_ONLY_LIVE_VIEW_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.EXTERNAL_STORAGE_LOCATION
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.EXTERNAL_STORAGE_LOCATION_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.GR2_LCD_SLEEP
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.LABEL_DEBUG_INFO
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.LABEL_EXIT_APPLICATION
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.LABEL_WIFI_SETTINGS
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.NUMBER_OF_CACHE_PICTURES
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.NUMBER_OF_CACHE_PICTURES_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION1_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION1_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION1_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION1_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION2_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION2_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION2_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION2_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION3_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION3_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION3_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION3_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION4_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION4_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION4_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION4_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION5_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION5_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION5_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION5_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CONNECTION_METHOD
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_SAVE_LOCAL_LOCATION
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.USE_GR2_SPECIAL_COMMAND

/**
     アプリケーションの依存
*/
interface IApplicationConstantConvert
{
    companion object {
        const val ID_MAIN_ACTIVITY_LAYOUT = R.id.main_layout
        const val ID_AREA_MESSAGE = R.id.message

        const val ID_DRAWABLE_SPLASH_IMAGE = R.drawable.a01e1
        const val ID_DRAWABLE_BACKGROUND_IMAGE = R.drawable.background

        const val ID_LABEL_APP_NAME = R.string.app_name
        const val ID_LABEL_APP_LOCATION = R.string.app_location

        const val ID_LAYOUT_CAMERA_CAPTURE = R.layout.camera_capture
        const val ID_LAYOUT_PREFERENCE = R.xml.preference_main

        const val ID_DIALOG_TITLE_CONFIRMATION = R.string.dialog_title_confirmation
        const val ID_DIALOG_EXIT_APPLICATION = R.string.dialog_message_exit_application
        const val ID_DIALOG_EXIT_POWER_OFF = R.string.dialog_message_power_off

        const val ID_DIALOG_BUTTON_LABEL_POSITIVE = R.string.dialog_positive_execute
        const val ID_DIALOG_BUTTON_LABEL_NEGATIVE = R.string.dialog_negative_cancel

        const val ID_MESSAGE_LABEL_CAPTURE_SUCCESS = R.string.capture_success

        const val ID_LIVE_VIEW_LAYOUT_DEFAULT = R.layout.liveimage_view
        const val ID_VIEW_FINDER_0 = R.id.liveViewFinder0
        const val ID_VIEW_FINDER_1 = R.id.liveViewFinder1
        const val ID_VIEW_FINDER_2 = R.id.liveViewFinder2
        const val ID_VIEW_FINDER_3 = R.id.liveViewFinder3
        const val ID_VIEW_UPPER_AREA = R.id.liveview_upper_area
        const val ID_VIEW_LOWER_AREA = R.id.liveview_lower_area
        const val ID_VIEW_AREA_0 = R.id.panelLayout0
        const val ID_VIEW_AREA_1 = R.id.panelLayout1
        const val ID_VIEW_AREA_2 = R.id.panelLayout2
        const val ID_VIEW_AREA_3 = R.id.panelLayout3

        const val ID_CACHE_SEEKBAR_0 = R.id.liveview_cache_seekbar0
        const val ID_CACHE_SEEKBAR_1 = R.id.liveview_cache_seekbar1
        const val ID_CACHE_SEEKBAR_2 = R.id.liveview_cache_seekbar2
        const val ID_CACHE_SEEKBAR_3 = R.id.liveview_cache_seekbar3

        const val MAX_VALUE_SEEKBAR = 1000

        const val ID_BUTTON_SHUTTER = R.id.button_camera
        const val ID_PREVIEW_VIEW_BUTTON_SHUTTER = R.id.camera_capture_button

        const val ID_CAMERA_X_PREVIEW_LAYOUT = R.id.viewFinder

        const val ID_MENU_LAYOUT_DEBUG_VIEW = R.menu.debug_view
        const val ID_MENU_ACTION_REFRESH = R.id.action_refresh
        const val ID_LABEL_FINISHED_REFRESH = R.string.finish_refresh
        const val ID_LABEL_TITLE_CONFIRMATION_FOR_EXPORT_LOG = R.string.dialog_confirm_title_output_log
        const val ID_LABEL_MESSAGE_CONFIRMATION_FOR_EXPORT_LOG = R.string.dialog_confirm_message_output_log

        const val ID_PREFERENCE_LABEL_EXIT_APPLICATION = LABEL_EXIT_APPLICATION
        const val ID_PREFERENCE_LABEL_WIFI_SETTINGS = LABEL_WIFI_SETTINGS
        const val ID_PREFERENCE_LABEL_DEBUG_INFO = LABEL_DEBUG_INFO

        const val ID_PREFERENCE_SAVE_LOCAL_LOCATION = PREFERENCE_SAVE_LOCAL_LOCATION
        const val ID_PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE = PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE

        const val ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW = CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW
        const val ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE = CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE

        const val ID_PREFERENCE_CAPTURE_ONLY_LIVEVIEW_IMAGE = CAPTURE_ONLY_LIVE_VIEW
        const val ID_PREFERENCE_CAPTURE_ONLY_LIVEVIEW_IMAGE_DEFAULT_VALUE = CAPTURE_ONLY_LIVE_VIEW_DEFAULT_VALUE

        const val ID_PREFERENCE_EXTERNAL_STORAGE_LOCATION = EXTERNAL_STORAGE_LOCATION
        const val ID_PREFERENCE_EXTERNAL_STORAGE_LOCATION_DEFAULT_VALUE = EXTERNAL_STORAGE_LOCATION_DEFAULT_VALUE

        const val ID_PREFERENCE_CACHE_LIVE_VIEW_PICTURES = CACHE_LIVE_VIEW_PICTURES
        const val ID_PREFERENCE_NUMBER_OF_CACHE_PICTURES = NUMBER_OF_CACHE_PICTURES
        const val ID_PREFERENCE_NUMBER_OF_CACHE_PICTURES_DEFAULT_VALUE = NUMBER_OF_CACHE_PICTURES_DEFAULT_VALUE

        const val ID_PREFERENCE_RICOH_GR2_LCD_SLEEP = GR2_LCD_SLEEP
        const val ID_PREFERENCE_USE_GR2_SPECIAL_COMMAND = USE_GR2_SPECIAL_COMMAND

        const val ID_PREFERENCE_LABEL_SELECT_CAMERA_CONNECTION_METHOD = PREFERENCE_CONNECTION_METHOD

        const val ID_PREFERENCE_ARRAY_CAMERA_METHOD = R.array.connection_method
        const val ID_PREFERENCE_ARRAY_CAMERA_SEQUENCE = R.array.connection_sequence
        const val ID_PREFERENCE_ARRAY_CAMERA_METHOD_VALUE = R.array.connection_method_value
        const val ID_PREFERENCE_ARRAY_CAMERA_SEQUENCE_VALUE = R.array.connection_sequence_value

        const val ID_PREFERENCE_CAMERA_METHOD_1 = PREFERENCE_CAMERA_METHOD_1
        const val ID_PREFERENCE_CAMERA_SEQUENCE_1 = PREFERENCE_CAMERA_SEQUENCE_1
        const val ID_PREFERENCE_CAMERA_OPTION1_1 = PREFERENCE_CAMERA_OPTION1_1
        const val ID_PREFERENCE_CAMERA_OPTION2_1 = PREFERENCE_CAMERA_OPTION2_1
        const val ID_PREFERENCE_CAMERA_OPTION3_1 = PREFERENCE_CAMERA_OPTION3_1
        const val ID_PREFERENCE_CAMERA_OPTION4_1 = PREFERENCE_CAMERA_OPTION4_1
        const val ID_PREFERENCE_CAMERA_OPTION5_1 = PREFERENCE_CAMERA_OPTION5_1

        const val ID_PREFERENCE_CAMERA_METHOD_2 = PREFERENCE_CAMERA_METHOD_2
        const val ID_PREFERENCE_CAMERA_SEQUENCE_2 = PREFERENCE_CAMERA_SEQUENCE_2
        const val ID_PREFERENCE_CAMERA_OPTION1_2 = PREFERENCE_CAMERA_OPTION1_2
        const val ID_PREFERENCE_CAMERA_OPTION2_2 = PREFERENCE_CAMERA_OPTION2_2
        const val ID_PREFERENCE_CAMERA_OPTION3_2 = PREFERENCE_CAMERA_OPTION3_2
        const val ID_PREFERENCE_CAMERA_OPTION4_2 = PREFERENCE_CAMERA_OPTION4_2
        const val ID_PREFERENCE_CAMERA_OPTION5_2 = PREFERENCE_CAMERA_OPTION5_2

        const val ID_PREFERENCE_CAMERA_METHOD_3 = PREFERENCE_CAMERA_METHOD_3
        const val ID_PREFERENCE_CAMERA_SEQUENCE_3 = PREFERENCE_CAMERA_SEQUENCE_3
        const val ID_PREFERENCE_CAMERA_OPTION1_3 = PREFERENCE_CAMERA_OPTION1_3
        const val ID_PREFERENCE_CAMERA_OPTION2_3 = PREFERENCE_CAMERA_OPTION2_3
        const val ID_PREFERENCE_CAMERA_OPTION3_3 = PREFERENCE_CAMERA_OPTION3_3
        const val ID_PREFERENCE_CAMERA_OPTION4_3 = PREFERENCE_CAMERA_OPTION4_3
        const val ID_PREFERENCE_CAMERA_OPTION5_3 = PREFERENCE_CAMERA_OPTION5_3

        const val ID_PREFERENCE_CAMERA_METHOD_4 = PREFERENCE_CAMERA_METHOD_4
        const val ID_PREFERENCE_CAMERA_SEQUENCE_4 = PREFERENCE_CAMERA_SEQUENCE_4
        const val ID_PREFERENCE_CAMERA_OPTION1_4 = PREFERENCE_CAMERA_OPTION1_4
        const val ID_PREFERENCE_CAMERA_OPTION2_4 = PREFERENCE_CAMERA_OPTION2_4
        const val ID_PREFERENCE_CAMERA_OPTION3_4 = PREFERENCE_CAMERA_OPTION3_4
        const val ID_PREFERENCE_CAMERA_OPTION4_4 = PREFERENCE_CAMERA_OPTION4_4
        const val ID_PREFERENCE_CAMERA_OPTION5_4 = PREFERENCE_CAMERA_OPTION5_4

    }

}
