package com.adeadfed.browser;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Arrays;

import com.adeadfed.common.OsType;
import com.adeadfed.common.ProfileColors;
import com.adeadfed.browser_extensions.BrowserExtensionsLoader;

public class Browser {
    private String exePath;
    private String profilesPath;
    private String profileColor;
    private String profileName;

    private final String BROWSER_DATA_PREFIX = "browser-data";

    // taken from args passed to the built-in BurpSuite Chromium browser
    private final String[] PWNCHROME_DEFAULT_ARGS = {
        "--ignore-certificate-errors",
        "--disable-ipc-flooding-protection",
        "--disable-xss-auditor",
        "--disable-bundled-ppapi-flash",
        "--disable-plugins-discovery",
        "--disable-default-apps",
        "--disable-prerender-local-predictor",
        "--disable-sync",
        "--disable-breakpad",
        "--disable-crash-reporter",
        "--disable-prerender-local-predictor",
        "--disk-cache-size=0",
        "--disable-settings-window",
        "--disable-notifications",
        "--disable-speech-api",
        "--disable-file-system",
        "--disable-presentation-api",
        "--disable-permissions-api",
        "--disable-new-zip-unpacker",
        "--disable-media-session-api",
        "--no-experiments",
        "--no-events",
        "--no-first-run",
        "--no-default-browser-check",
        "--no-pings",
        "--no-service-autorun",
        "--media-cache-size=0",
        "--use-fake-device-for-media-stream",
        "--dbus-stub",
        "--disable-background-networking",
        "--disable-features=ChromeWhatsNewUI,HttpsUpgrades,ImageServiceObserveSyncDownloadStatus"
    };

    public Browser(String exePath, String profilesPath, String themeColor, String profileName) {
        this.exePath = exePath;
        this.profilesPath = profilesPath;
        this.profileColor = themeColor;
        this.profileName = profileName;
    }

    public Process start() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(getArgs());
        Process process = processBuilder.start();
        // ensure browser is closed when burp is exited
        Runtime.getRuntime().addShutdownHook(new Thread(process::destroy));
        return process;
    }

    private String[] getArgs() throws Exception {
        return concatAll(
                getExeArgs(),
                PWNCHROME_DEFAULT_ARGS,
                getProfileArgs(),
                getExtensionsArgs()
        );
    }

    private String[] getExeArgs() {
        return new String[] { exePath };
    } 

    private String[] getExtensionsArgs() throws IOException {
        BrowserExtensionsLoader browserExtensions = new BrowserExtensionsLoader(
                profilesPath,
                ProfileColors.valueOf(profileColor.toUpperCase())
        );
        return new String[] {
            "--load-extension=" + String.join(
                ",",
                browserExtensions.getHeaderExtensionDir(),
                browserExtensions.getProxyExtensionDir(),
                browserExtensions.getThemeDir()
        )};
    }

    private String[] getProfileArgs() throws InvalidPathException {
        String pwnChromeBrowserDataDir = Paths.get(
                profilesPath,
                BROWSER_DATA_PREFIX,
                profileColor.toLowerCase()
        ).toString();
        String userDataDirArg = "--user-data-dir=" + pwnChromeBrowserDataDir;
        String windowNameArg = "--window-name=" + "PwnChromium - " + profileName;
        return new String[] { userDataDirArg, windowNameArg };
    }

    // https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
    @SafeVarargs
    private static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}