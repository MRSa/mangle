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

        const val CACHE_LIVEVIEW_PICTURES = "cache_liveview_pictures"
        const val CACHE_LIVEVIEW_PICTURES_DEFAULT_VALUE = false

        const val NUMBER_OF_CACHE_PICTURES = "nof_cache_pictures"
        const val NUMBER_OF_CACHE_PICTURES_DEFAULT_VALUE = "500"

        // --- SCREEN TRANSACTION LABEL ---
        const val LABEL_EXIT_APPLICATION = "exit_application"
        const val LABEL_WIFI_SETTINGS = "wifi_settings"
        const val LABEL_INSTRUCTION_LINK = "instruction_link"
        const val LABEL_PRIVACY_POLICY = "privacy_policy"
        const val LABEL_DEBUG_INFO = "debug_info"


        // --- HIDDEN
        const val SHOW_GRID_STATUS = "show_grid"
        const val SHOW_GRID_STATUS_DEFAULT_VALUE = false

    }

}
