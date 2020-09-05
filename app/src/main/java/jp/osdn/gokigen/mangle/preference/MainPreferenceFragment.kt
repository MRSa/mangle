package jp.osdn.gokigen.mangle.preference

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.mangle.IChangeScene
import jp.osdn.gokigen.mangle.R

class MainPreferenceFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener
{
    private val TAG = toString()
    private lateinit var preferences : SharedPreferences
    private lateinit var changeScene : IChangeScene

    companion object
    {
        fun newInstance() = MainPreferenceFragment().apply { }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        setPreferencesFromResource(R.xml.preference_main, rootKey)

        prepareClickListener(IPreferencePropertyAccessor.LABEL_EXIT_APPLICATION)
        prepareClickListener(IPreferencePropertyAccessor.LABEL_WIFI_SETTINGS)
    }

    fun setSceneChanger(changer : IChangeScene)
    {
        this.changeScene = changer
    }

    private fun prepareClickListener(label: String)
    {
        val settings : Preference? = findPreference(label)
        settings?.setOnPreferenceClickListener(this)
    }

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        Log.v(TAG, " onAttach() : ")

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        PreferenceInitializer().initializePreferences(preferences)
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onResume()
    {
        super.onResume()
        Log.v(TAG, " onResume() : ")
    }

    override fun onPause()
    {
        super.onPause()
        Log.v(TAG, " onPause() : ")
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?)
    {
        var value = false
        when (key)
        {
            IPreferencePropertyAccessor.PREFERENCE_NOTIFICATIONS -> value = preferences.getBoolean(key, IPreferencePropertyAccessor.PREFERENCE_NOTIFICATIONS_DEFAULT_VALUE)
            // else -> Log.v(TAG, " onSharedPreferenceChanged() : + $key ")
        }
        Log.v(TAG, " onSharedPreferenceChanged() : + $key, $value")
    }

    override fun onPreferenceClick(preference: Preference?): Boolean
    {
        var ret = true
        when (preference?.key)
        {
            IPreferencePropertyAccessor.LABEL_WIFI_SETTINGS -> activity?.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            IPreferencePropertyAccessor.LABEL_EXIT_APPLICATION -> exitApplication()
            IPreferencePropertyAccessor.LABEL_DEBUG_INFO -> changeScene.changeSceneToDebugInformation()
            else -> { Log.v(TAG, " onPreferenceClick() : " + preference?.key); ret = false; }
        }
        return (ret)
    }

    private fun exitApplication()
    {
        Log.v(TAG, " exitApplication() : ")
    }
}