package jp.osdn.gokigen.mangle.preference

interface IPreferencePropertyAccessor
{

    companion object
    {
        val PREFERENCE_NOTIFICATIONS = "show_notifications"
        val PREFERENCE_NOTIFICATIONS_DEFAULT_VALUE : Boolean = false

        // --- SCREEN TRANSACTION LABEL ---
        val LABEL_EXIT_APPLICATION = "exit_application"
        val LABEL_WIFI_SETTINGS = "wifi_settings"
        val LABEL_INSTRUCTION_LINK = "instruction_link"
        val LABEL_PRIVACY_POLICY = "privacy_policy"
        val LABEL_DEBUG_INFO = "debug_info"
    }

}
