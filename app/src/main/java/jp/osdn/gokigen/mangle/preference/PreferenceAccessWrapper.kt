package jp.osdn.gokigen.mangle.preference

import android.content.Context
import androidx.preference.PreferenceManager

class PreferenceAccessWrapper(context : Context) : IPreferenceAccessWrapper
{
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun getString(key : String, defaultValue : String) : String
    {
        try
        {
            val value = preferences.getString(key, defaultValue)
            if (value != null)
            {
                return (value)
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (defaultValue)
    }

    override fun getBoolean(key : String, defaultValue : Boolean) : Boolean
    {
        try
        {
            return (preferences.getBoolean(key, defaultValue))
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (defaultValue)
    }

}
