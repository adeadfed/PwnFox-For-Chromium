package com.adeadfed.preferences;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProfilesDirPreference extends Preference {
    private final String persistentKey = "PWNCHROMIUM_PROFILES_DIR";

    @Override
    public String getPersistentKey() {
        return persistentKey;
    }

    @Override
    public String getDefault() throws Exception {
        try {
            Path profileDirPath = Paths.get(
                    System.getProperty("user.home"),
                    ".PwnChromiumData");
        
            if (!Files.exists(profileDirPath)) {
                Files.createDirectory(profileDirPath);
                return profileDirPath.toString();
            }
            return null;
        } catch (Exception e) {
            throw new Exception("PwnFox For Chromium - Error getting default profiles directory", e);
        }
    }
}
