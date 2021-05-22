package jp.osdn.gokigen.mangle.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.gokigenassets.preference.IPreferenceValueInitializer

class PreferenceValueInitializer() : IPreferenceValueInitializer
{
    override fun initializePreferences(context : Context)
    {
        try
        {
            initializeApplicationPreferences(context)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    private fun initializeApplicationPreferences(context : Context)
    {
        try
        {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context) ?: return
            val items : Map<String, *> = preferences.all
            val editor : SharedPreferences.Editor = preferences.edit()

            if (!items.containsKey(IPreferencePropertyAccessor.EXTERNAL_STORAGE_LOCATION))
            {
                editor.putString(
                    IPreferencePropertyAccessor.EXTERNAL_STORAGE_LOCATION,
                    IPreferencePropertyAccessor.EXTERNAL_STORAGE_LOCATION_DEFAULT_VALUE
                )
            }
            if (!items.containsKey(IPreferencePropertyAccessor.PREFERENCE_NOTIFICATIONS))
            {
                editor.putBoolean(
                    IPreferencePropertyAccessor.PREFERENCE_NOTIFICATIONS,
                    IPreferencePropertyAccessor.PREFERENCE_NOTIFICATIONS_DEFAULT_VALUE
                )
            }
            if (!items.containsKey(IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW))
            {
                editor.putBoolean(
                    IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW,
                    IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW_DEFAULT_VALUE
                )
            }
            if (!items.containsKey(IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION))
            {
                editor.putBoolean(
                    IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION,
                    IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE
                )
            }
            if (!items.containsKey(IPreferencePropertyAccessor.SHOW_GRID_STATUS))
            {
                editor.putBoolean(
                    IPreferencePropertyAccessor.SHOW_GRID_STATUS,
                    IPreferencePropertyAccessor.SHOW_GRID_STATUS_DEFAULT_VALUE
                )
            }
            if (!items.containsKey(IPreferencePropertyAccessor.CACHE_LIVE_VIEW_PICTURES))
            {
                editor.putBoolean(
                    IPreferencePropertyAccessor.CACHE_LIVE_VIEW_PICTURES,
                    IPreferencePropertyAccessor.CACHE_LIVE_VIEW_PICTURES_DEFAULT_VALUE
                )
            }
            if (!items.containsKey(IPreferencePropertyAccessor.NUMBER_OF_CACHE_PICTURES))
            {
                editor.putString(
                    IPreferencePropertyAccessor.NUMBER_OF_CACHE_PICTURES,
                    IPreferencePropertyAccessor.NUMBER_OF_CACHE_PICTURES_DEFAULT_VALUE
                )
            }
            if (!items.containsKey(IPreferencePropertyAccessor.CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW))
            {
                editor.putBoolean(
                    IPreferencePropertyAccessor.CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW,
                    IPreferencePropertyAccessor.CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE
                )
            }
            if (!items.containsKey(IPreferencePropertyAccessor.CAPTURE_ONLY_LIVE_VIEW))
            {
                editor.putBoolean(
                    IPreferencePropertyAccessor.CAPTURE_ONLY_LIVE_VIEW,
                    IPreferencePropertyAccessor.CAPTURE_ONLY_LIVE_VIEW_DEFAULT_VALUE
                )
            }
            if (!items.containsKey(IPreferencePropertyAccessor.CAPTURE_ONLY_EXTERNAL_CAMERA))
            {
                editor.putBoolean(
                    IPreferencePropertyAccessor.CAPTURE_ONLY_EXTERNAL_CAMERA,
                    IPreferencePropertyAccessor.CAPTURE_ONLY_EXTERNAL_CAMERA_DEFAULT_VALUE
                )
            }
            if (!items.containsKey(IPreferencePropertyAccessor.EXTERNAL_STORAGE_LOCATION))
            {
                editor.putString(IPreferencePropertyAccessor.EXTERNAL_STORAGE_LOCATION, IPreferencePropertyAccessor.EXTERNAL_STORAGE_LOCATION_DEFAULT_VALUE)
            }
            if (!items.containsKey(IPreferencePropertyAccessor.THETA_LIVEVIEW_RESOLUTION))
            {
                editor.putString(IPreferencePropertyAccessor.THETA_LIVEVIEW_RESOLUTION, IPreferencePropertyAccessor.THETA_LIVEVIEW_RESOLUTION_DEFAULT_VALUE)
            }
            if (!items.containsKey(IPreferencePropertyAccessor.PREFERENCE_CAMERA_METHOD_1))
            {
                editor.putString(IPreferencePropertyAccessor.PREFERENCE_CAMERA_METHOD_1, IPreferencePropertyAccessor.PREFERENCE_CAMERA_METHOD_1_DEFAULT_VALUE)
            }
            if (!items.containsKey(IPreferencePropertyAccessor.PREFERENCE_CAMERA_METHOD_2))
            {
                editor.putString(IPreferencePropertyAccessor.PREFERENCE_CAMERA_METHOD_2, IPreferencePropertyAccessor.PREFERENCE_CAMERA_METHOD_2_DEFAULT_VALUE)
            }
            if (!items.containsKey(IPreferencePropertyAccessor.PREFERENCE_CAMERA_METHOD_3))
            {
                editor.putString(IPreferencePropertyAccessor.PREFERENCE_CAMERA_METHOD_3, IPreferencePropertyAccessor.PREFERENCE_CAMERA_METHOD_3_DEFAULT_VALUE)
            }
            if (!items.containsKey(IPreferencePropertyAccessor.PREFERENCE_CAMERA_METHOD_4))
            {
                editor.putString(IPreferencePropertyAccessor.PREFERENCE_CAMERA_METHOD_4, IPreferencePropertyAccessor.PREFERENCE_CAMERA_METHOD_4_DEFAULT_VALUE)
            }
            editor.apply()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }
}
