package jp.osdn.gokigen.mangle.preference

import android.content.SharedPreferences

class PreferenceValueInitializer
{
    fun initializePreferences(preferences : SharedPreferences)
    {
        val items : Map<String, *> = preferences.all
        val editor : SharedPreferences.Editor = preferences.edit()

        if (!items.containsKey(IPreferencePropertyAccessor.PREFERENCE_NOTIFICATIONS))
        {
            editor.putBoolean(IPreferencePropertyAccessor.PREFERENCE_NOTIFICATIONS, IPreferencePropertyAccessor.PREFERENCE_NOTIFICATIONS_DEFAULT_VALUE)
        }
        if (!items.containsKey(IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION))
        {
            editor.putBoolean(IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION, IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE)
        }
        editor.apply()
    }

}
