package jp.osdn.gokigen.mangle.preference

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.mangle.R

class MainPreferenceFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener
{
    private val TAG = toString()
    private lateinit var preferences : SharedPreferences

    companion object
    {
        fun newInstance() = MainPreferenceFragment.apply { }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        setPreferencesFromResource(R.xml.preference_main, rootKey)
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
        TODO("Not yet implemented")
    }


}