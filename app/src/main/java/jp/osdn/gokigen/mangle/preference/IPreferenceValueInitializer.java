package jp.osdn.gokigen.mangle.preference;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

public interface IPreferenceValueInitializer
{
    void initializePreferences(@NonNull Context context);

    // external Storageを使用するための設定
    void initializeStorageLocationPreferences(@NonNull Context context);
    void storeStorageLocationPreference(@NonNull Context context, @NonNull Uri uri);
}
