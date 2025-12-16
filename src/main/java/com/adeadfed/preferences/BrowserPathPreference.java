package com.adeadfed.preferences;

import java.nio.file.Files;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

public class BrowserPathPreference extends Preference {
    private final String persistentKey = "PWNCHROMIUM_BROWSER_PATH";

    @Override
    public String getPersistentKey() {
        return persistentKey;
    }

    @Override
    public String getDefault() throws Exception {
        try {
            String chromiumRegex = "regex:[Cc]hrom(e|ium)(\\.exe)?";

            Path userDirPath = Paths.get(
                    System.getProperty("user.dir"));

            FileSystem fs = FileSystems.getDefault();
            PathMatcher matcher = fs.getPathMatcher(chromiumRegex);

            Path chromiumPath = Files.walk(userDirPath, 10)
                    .filter(Files::isRegularFile)
                    .filter(Files::isExecutable)
                    .filter(path -> matcher.matches(path.getFileName()))
                    .findFirst()
                    .orElse(null);
            return chromiumPath == null ? null : chromiumPath.toAbsolutePath().toString();
        } catch (Exception e) {
            throw new Exception("PwnFox For Chromium - Error getting default browser path", e);
        }
    }
}
