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
        if (!items.containsKey(IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW))
        {
            editor.putBoolean(IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW, IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW_DEFAULT_VALUE)
        }
        if (!items.containsKey(IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION))
        {
            editor.putBoolean(IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION, IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE)
        }
        if (!items.containsKey(IPreferencePropertyAccessor.SHOW_GRID_STATUS))
        {
            editor.putBoolean(IPreferencePropertyAccessor.SHOW_GRID_STATUS, IPreferencePropertyAccessor.SHOW_GRID_STATUS_DEFAULT_VALUE)
        }
        if (!items.containsKey(IPreferencePropertyAccessor.CACHE_LIVEVIEW_PICTURES))
        {
            editor.putBoolean(IPreferencePropertyAccessor.CACHE_LIVEVIEW_PICTURES, IPreferencePropertyAccessor.CACHE_LIVEVIEW_PICTURES_DEFAULT_VALUE)
        }
        editor.apply()
    }

}
