package jp.osdn.gokigen.mangle.preference

interface IPreferencePropertyAccessor
{

    companion object
    {
        // --- PREFERENCE KEY AND DEFAULT VALUE ---
        const val PREFERENCE_NOTIFICATIONS = "show_notifications"
        const val PREFERENCE_NOTIFICATIONS_DEFAULT_VALUE = false
        const val PREFERENCE_SAVE_LOCAL_LOCATION = "save_local_location"
        const val PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE = false
        const val PREFERENCE_USE_CAMERA_X_PREVIEW = "use_camera_x_preview"
        const val PREFERENCE_USE_CAMERA_X_PREVIEW_DEFAULT_VALUE = false

        const val CACHE_LIVE_VIEW_PICTURES = "cache_live_view_pictures"
        const val CACHE_LIVE_VIEW_PICTURES_DEFAULT_VALUE = false

        const val USE_ONLY_SINGLE_CAMERA_X = "only_one_camera_x"
        const val USE_ONLY_SINGLE_CAMERA_X_DEFAULT_VALUE = false

        const val NUMBER_OF_CACHE_PICTURES = "number_of_cache_pictures"
        const val NUMBER_OF_CACHE_PICTURES_DEFAULT_VALUE = "500"

        const val CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW = "capture_both_camera_and_live_view"
        const val CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE = false

        const val CAPTURE_ONLY_LIVE_VIEW = "capture_only_live_view"
        const val CAPTURE_ONLY_LIVE_VIEW_DEFAULT_VALUE = false

        const val CAPTURE_ONLY_EXTERNAL_CAMERA = "capture_only_external_camera"
        const val CAPTURE_ONLY_EXTERNAL_CAMERA_DEFAULT_VALUE = false

        // --- SCREEN TRANSACTION LABEL ---
        const val LABEL_EXIT_APPLICATION = "exit_application"
        const val LABEL_WIFI_SETTINGS = "wifi_settings"
        const val LABEL_INSTRUCTION_LINK = "instruction_link"
        const val LABEL_PRIVACY_POLICY = "privacy_policy"
        const val LABEL_DEBUG_INFO = "debug_info"

        // --- HIDDEN
        const val SHOW_GRID_STATUS = "show_grid"
        const val SHOW_GRID_STATUS_DEFAULT_VALUE = false
        const val EXTERNAL_STORAGE_LOCATION = "external_storage_location"
        const val EXTERNAL_STORAGE_LOCATION_DEFAULT_VALUE = ""


        // --- Camera Specific Preferences
        const val THETA_LIVEVIEW_RESOLUTION = "theta_liveview_resolution"
        const val THETA_LIVEVIEW_RESOLUTION_DEFAULT_VALUE = "{\"width\": 640, \"height\": 320, \"framerate\": 30}"


        // --- RICOH/PENTAX
        const val GR2_LCD_SLEEP = "gr2_lcd_sleep"
        const val USE_GR2_SPECIAL_COMMAND = "use_gr2_command"
        const val PREFERENCE_CONNECTION_METHOD = "connection_method"

        // --- CAMERA 1  PREFERENCES
        const val PREFERENCE_CAMERA_METHOD_1 = "camera_method1"
        const val PREFERENCE_CAMERA_METHOD_1_DEFAULT_VALUE = "camerax"
        const val PREFERENCE_CAMERA_SEQUENCE_1 = "camera_sequence1"
        const val PREFERENCE_CAMERA_SEQUENCE_1_DEFAULT_VALUE = "0"
        const val PREFERENCE_CAMERA_OPTION1_1 = "camera_option11"
        const val PREFERENCE_CAMERA_OPTION1_1_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION2_1 = "camera_option21"
        const val PREFERENCE_CAMERA_OPTION2_1_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION3_1 = "camera_option31"
        const val PREFERENCE_CAMERA_OPTION3_1_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION4_1 = "camera_option41"
        const val PREFERENCE_CAMERA_OPTION4_1_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION5_1 = "camera_option51"
        const val PREFERENCE_CAMERA_OPTION5_1_DEFAULT_VALUE = ""

        // --- CAMERA 2  PREFERENCES
        const val PREFERENCE_CAMERA_METHOD_2 = "camera_method2"
        const val PREFERENCE_CAMERA_METHOD_2_DEFAULT_VALUE = "camerax"
        const val PREFERENCE_CAMERA_SEQUENCE_2 = "camera_sequence2"
        const val PREFERENCE_CAMERA_SEQUENCE_2_DEFAULT_VALUE = "0"
        const val PREFERENCE_CAMERA_OPTION1_2 = "camera_option12"
        const val PREFERENCE_CAMERA_OPTION1_2_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION2_2 = "camera_option22"
        const val PREFERENCE_CAMERA_OPTION2_2_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION3_2 = "camera_option32"
        const val PREFERENCE_CAMERA_OPTION3_2_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION4_2 = "camera_option42"
        const val PREFERENCE_CAMERA_OPTION4_2_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION5_2 = "camera_option52"
        const val PREFERENCE_CAMERA_OPTION5_2_DEFAULT_VALUE = ""

        // --- CAMERA 3  PREFERENCES
        const val PREFERENCE_CAMERA_METHOD_3 = "camera_method3"
        const val PREFERENCE_CAMERA_METHOD_3_DEFAULT_VALUE = "camerax"
        const val PREFERENCE_CAMERA_SEQUENCE_3 = "camera_sequence3"
        const val PREFERENCE_CAMERA_SEQUENCE_3_DEFAULT_VALUE = "0"
        const val PREFERENCE_CAMERA_OPTION1_3 = "camera_option13"
        const val PREFERENCE_CAMERA_OPTION1_3_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION2_3 = "camera_option23"
        const val PREFERENCE_CAMERA_OPTION2_3_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION3_3 = "camera_option33"
        const val PREFERENCE_CAMERA_OPTION3_3_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION4_3 = "camera_option43"
        const val PREFERENCE_CAMERA_OPTION4_3_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION5_3 = "camera_option53"
        const val PREFERENCE_CAMERA_OPTION5_3_DEFAULT_VALUE = ""

        // --- CAMERA 4  PREFERENCES
        const val PREFERENCE_CAMERA_METHOD_4 = "camera_method4"
        const val PREFERENCE_CAMERA_METHOD_4_DEFAULT_VALUE = "camerax"
        const val PREFERENCE_CAMERA_SEQUENCE_4 = "camera_sequence4"
        const val PREFERENCE_CAMERA_SEQUENCE_4_DEFAULT_VALUE = "0"
        const val PREFERENCE_CAMERA_OPTION1_4 = "camera_option14"
        const val PREFERENCE_CAMERA_OPTION1_4_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION2_4 = "camera_option24"
        const val PREFERENCE_CAMERA_OPTION2_4_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION3_4 = "camera_option34"
        const val PREFERENCE_CAMERA_OPTION3_4_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION4_4 = "camera_option44"
        const val PREFERENCE_CAMERA_OPTION4_4_DEFAULT_VALUE = ""
        const val PREFERENCE_CAMERA_OPTION5_4 = "camera_option54"
        const val PREFERENCE_CAMERA_OPTION5_4_DEFAULT_VALUE = ""
    }

}
