package com.adeadfed.preferences;

public abstract class Preference extends OptionalPreference {
    public abstract String getDefault() throws Exception;
    public abstract String getPersistentKey();
}
