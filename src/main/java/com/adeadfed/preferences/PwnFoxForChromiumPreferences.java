package com.adeadfed.preferences;

import burp.api.montoya.persistence.Preferences;

public class PwnFoxForChromiumPreferences {
    private Preferences montoyaPreferences;
    public BrowserPathPreference browserPath;
    public ProfilesDirPreference profilesDir;
    public ProfileNamesPreference profileNames;

    public PwnFoxForChromiumPreferences(Preferences montoyaPreferences) throws Exception {
        this.montoyaPreferences = montoyaPreferences;
        this.browserPath = new BrowserPathPreference();
        this.profilesDir = new ProfilesDirPreference();
        this.profileNames = new ProfileNamesPreference();

        initDefaultPreferences();
    }

    private void initDefaultPreferences() throws Exception {
        if (!preferenceExists(browserPath.getPersistentKey())) {
            set(browserPath, browserPath.getDefault());
        }
        if (!preferenceExists(profilesDir.getPersistentKey())) {
            set(profilesDir, profilesDir.getDefault());
        }
    }

    public String get(Preference preference) {
        return montoyaPreferences.getString(preference.getPersistentKey());
    }

    public void set(Preference preference, String value) {
        montoyaPreferences.setString(preference.getPersistentKey(), value);
    }

    public String getProfileName(String color) {
        String profileNameKey = profileNames.getPersistentKey(color);
        if (preferenceExists(profileNameKey)) {
            return montoyaPreferences.getString(profileNameKey);
        }
        return profileNames.getDefault(color);
    }

    public void setProfileName(String color, String value) {
        String profileNameKey = profileNames.getPersistentKey(color);
        montoyaPreferences.setString(profileNameKey, value);
    }

    private boolean preferenceExists(String settingName) {
        return montoyaPreferences.getString(settingName) != null;
    }
}
