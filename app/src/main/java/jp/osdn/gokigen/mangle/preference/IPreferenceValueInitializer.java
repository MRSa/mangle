package jp.osdn.gokigen.mangle.preference;

import android.content.Context;

import androidx.annotation.NonNull;

public interface IPreferenceValueInitializer
{
    void initializePreferences(@NonNull Context context);
}
