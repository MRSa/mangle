package jp.osdn.gokigen.gokigenassets.preference

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_LAYOUT_PREFERENCE
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_LABEL_DEBUG_INFO
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_LABEL_EXIT_APPLICATION
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_LABEL_SELECT_CAMERA_CONNECTION_METHOD
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_LABEL_WIFI_SETTINGS

class MainPreferenceFragment : PreferenceFragmentCompat()
{
    private lateinit var actionReceiver : IActionReceiver
    private lateinit var valueInitializer : IPreferenceValueInitializer

    private fun setActionReceiver(receiver : IActionReceiver)
    {
        actionReceiver = receiver
    }

    private fun setValueInitializer(initializer : IPreferenceValueInitializer)
    {
        valueInitializer = initializer
    }

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        Log.v(TAG, " onAttach() : ")

        prepareChangeListener(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        setPreferencesFromResource(ID_LAYOUT_PREFERENCE, rootKey)

        prepareClickListener(ID_PREFERENCE_LABEL_EXIT_APPLICATION)
        prepareClickListener(ID_PREFERENCE_LABEL_WIFI_SETTINGS)
        prepareClickListener(ID_PREFERENCE_LABEL_DEBUG_INFO)
        prepareClickListener(ID_PREFERENCE_LABEL_SELECT_CAMERA_CONNECTION_METHOD)
    }

    private fun prepareChangeListener(context: Context)
    {
        try
        {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            valueInitializer.initializePreferences(context)
            preferences.registerOnSharedPreferenceChangeListener(actionReceiver.getPreferenceChangeListener())
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    private fun prepareClickListener(label: String)
    {
        try
        {
            val settings : Preference? = findPreference(label)
            settings?.onPreferenceClickListener = actionReceiver.getPreferenceClickListener()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        fun newInstance(receiver : IActionReceiver, initializer : IPreferenceValueInitializer) = MainPreferenceFragment().apply {
            setActionReceiver(receiver)
            setValueInitializer(initializer)
        }
        private val TAG = MainPreferenceFragment::class.java.simpleName
    }
}
