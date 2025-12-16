package com.adeadfed.preferences;

public class ProfileNamesPreference extends OptionalPreference {
    private final String persistentKey = "PWNCHROMIUM_PROFILE_NAMES";

    public String getPersistentKey(String profileColor) {
        return persistentKey + "_" + profileColor.toUpperCase();
    }

    public String getDefault(String profileColor) {
        return profileColor;
    }
}
