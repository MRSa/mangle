package jp.osdn.gokigen.mangle.preference

import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.gokigenassets.preference.IActionReceiver
import jp.osdn.gokigen.gokigenassets.preference.IPreferenceViewUpdater
import jp.osdn.gokigen.gokigenassets.scene.IChangeSceneBasic
import jp.osdn.gokigen.mangle.scene.IChangeScene

class PreferenceChanger(private val activity : AppCompatActivity, private val changeSceneBasic : IChangeSceneBasic, private val changeScene : IChangeScene) : SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener, IActionReceiver
{
    private var preferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
    private lateinit var preferenceViewUpdater : IPreferenceViewUpdater

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?)
    {
        var value = false
        when (key)
        {
            IPreferencePropertyAccessor.PREFERENCE_NOTIFICATIONS -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.PREFERENCE_NOTIFICATIONS_DEFAULT_VALUE)
            IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW_DEFAULT_VALUE)
            IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE)
            IPreferencePropertyAccessor.CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE)
            IPreferencePropertyAccessor.CACHE_LIVE_VIEW_PICTURES -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.CACHE_LIVE_VIEW_PICTURES_DEFAULT_VALUE)
            IPreferencePropertyAccessor.CAPTURE_ONLY_LIVE_VIEW -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.CAPTURE_ONLY_LIVE_VIEW_DEFAULT_VALUE)
            // else -> Log.v(TAG, " onSharedPreferenceChanged() : + $key ")
        }
        Log.v(TAG, " onSharedPreferenceChanged() : + $key, $value")
        try
        {
            if (key != null)
            {
                if (::preferenceViewUpdater.isInitialized)
                {
                    preferenceViewUpdater.onPreferenceUpdated(key)
                }
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onPreferenceClick(preference: Preference?): Boolean
    {
        var ret = true
        when (preference?.key)
        {
            IPreferencePropertyAccessor.LABEL_WIFI_SETTINGS -> activity.startActivity(
                Intent(
                    Settings.ACTION_WIFI_SETTINGS)
            )
            IPreferencePropertyAccessor.LABEL_EXIT_APPLICATION -> changeSceneBasic.exitApplication()
            IPreferencePropertyAccessor.LABEL_DEBUG_INFO -> changeSceneBasic.changeToDebugInformation()
            IPreferencePropertyAccessor.PREFERENCE_CONNECTION_METHOD -> changeScene.selectConnectionMethod()
            else -> { Log.v(TAG, " onPreferenceClick() : " + preference?.key); ret = false; }
        }
        return (ret)
    }

    override fun getPreferenceChangeListener() : SharedPreferences.OnSharedPreferenceChangeListener
    {
        return (this)
    }

    override fun getPreferenceClickListener() : Preference.OnPreferenceClickListener
    {
        return (this)
    }

    override fun setPreferenceViewUpdater(updater: IPreferenceViewUpdater)
    {
        preferenceViewUpdater = updater
    }

    companion object
    {
        private val TAG = PreferenceChanger::class.java.simpleName
    }
}