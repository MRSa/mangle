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

        // --- CONNECTION METHOD
        const val PREFERENCE_CAMERA_METHOD_NONE = "none"
        const val PREFERENCE_CONNECTION_METHOD = "connection_method"
        const val PREFERENCE_CAMERA_METHOD_1 = "camera_method1"
        const val PREFERENCE_CAMERA_METHOD_1_DEFAULT_VALUE = "camerax"

        const val PREFERENCE_CAMERA_METHOD_2 = "camera_method2"
        const val PREFERENCE_CAMERA_METHOD_2_DEFAULT_VALUE = "camerax"

        const val PREFERENCE_CAMERA_METHOD_3 = "camera_method3"
        const val PREFERENCE_CAMERA_METHOD_3_DEFAULT_VALUE = "camerax"

        const val PREFERENCE_CAMERA_METHOD_4 = "camera_method4"
        const val PREFERENCE_CAMERA_METHOD_4_DEFAULT_VALUE = "camerax"

    }

}
