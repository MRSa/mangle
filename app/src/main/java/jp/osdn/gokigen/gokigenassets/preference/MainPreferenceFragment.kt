package jp.osdn.gokigen.gokigenassets.preference

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.preference.*
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_LAYOUT_PREFERENCE
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_ARRAY_CAMERA_METHOD
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_ARRAY_CAMERA_METHOD_VALUE
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_ARRAY_CAMERA_SEQUENCE
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_ARRAY_CAMERA_SEQUENCE_VALUE
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAMERA_METHOD_1
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAMERA_METHOD_2
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAMERA_METHOD_3
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAMERA_METHOD_4
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAMERA_SEQUENCE_1
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAMERA_SEQUENCE_2
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAMERA_SEQUENCE_3
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAMERA_SEQUENCE_4
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

        setMethodSummary(ID_PREFERENCE_CAMERA_METHOD_1, ID_PREFERENCE_ARRAY_CAMERA_METHOD, ID_PREFERENCE_ARRAY_CAMERA_METHOD_VALUE)
        setMethodSummary(ID_PREFERENCE_CAMERA_METHOD_2, ID_PREFERENCE_ARRAY_CAMERA_METHOD, ID_PREFERENCE_ARRAY_CAMERA_METHOD_VALUE)
        setMethodSummary(ID_PREFERENCE_CAMERA_METHOD_3, ID_PREFERENCE_ARRAY_CAMERA_METHOD, ID_PREFERENCE_ARRAY_CAMERA_METHOD_VALUE)
        setMethodSummary(ID_PREFERENCE_CAMERA_METHOD_4, ID_PREFERENCE_ARRAY_CAMERA_METHOD, ID_PREFERENCE_ARRAY_CAMERA_METHOD_VALUE)

        setMethodSummary(ID_PREFERENCE_CAMERA_SEQUENCE_1, ID_PREFERENCE_ARRAY_CAMERA_SEQUENCE, ID_PREFERENCE_ARRAY_CAMERA_SEQUENCE_VALUE)
        setMethodSummary(ID_PREFERENCE_CAMERA_SEQUENCE_2, ID_PREFERENCE_ARRAY_CAMERA_SEQUENCE, ID_PREFERENCE_ARRAY_CAMERA_SEQUENCE_VALUE)
        setMethodSummary(ID_PREFERENCE_CAMERA_SEQUENCE_3, ID_PREFERENCE_ARRAY_CAMERA_SEQUENCE, ID_PREFERENCE_ARRAY_CAMERA_SEQUENCE_VALUE)
        setMethodSummary(ID_PREFERENCE_CAMERA_SEQUENCE_4, ID_PREFERENCE_ARRAY_CAMERA_SEQUENCE, ID_PREFERENCE_ARRAY_CAMERA_SEQUENCE_VALUE)
    }

    private fun setMethodSummary(itemKey : String, listArray : Int, listArrayValue : Int)
    {
        try
        {
            val preferenceItem = findPreference<DropDownPreference>(itemKey)
            if (preferenceItem != null)
            {
                preferenceItem.summary = preferenceItem.entry
                preferenceItem.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                    try
                    {
                        val listArrayValues = context?.resources!!.getStringArray(listArray)
                        val listArrays = context?.resources!!.getStringArray(listArrayValue)
                        val item = newValue.toString()
                        var summaryIsSet = false
                        var targetIndex = -1
                        for (checkItem in listArrays)
                        {
                            targetIndex++
                            Log.v(TAG, " $checkItem : $item $targetIndex ${listArrayValues[targetIndex]}")
                            if (item == checkItem)
                            {
                                preferenceItem.summary = listArrayValues[targetIndex]
                                summaryIsSet = true
                                break
                            }
                        }
                        if (!summaryIsSet)
                        {
                            preferenceItem.summary = (newValue  as CharSequence)
                        }
                    }
                    catch (e : Exception)
                    {
                        e.printStackTrace()
                        preferenceItem.summary = (newValue  as CharSequence) // 例外発生時には、とりあえず番号を表示した
                    }
                    true
                }
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
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
