package jp.osdn.gokigen.mangle.preference;

import androidx.annotation.NonNull;

public interface IPreferenceAccessWrapper
{
    @NonNull String getString(@NonNull String key, @NonNull String defaultValue);
    boolean getBoolean(@NonNull String key, boolean defaultValue);
}
