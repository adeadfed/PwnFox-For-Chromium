package com.adeadfed;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class PwnFoxForChromium implements BurpExtension
{
    public MontoyaApi api;
    // names of extension settings to be stored in the persistent Montoya's Persistence API
    public final String PERSISTENT_CHROME_EXE = "PwnChromeExePath";
    public final String PERSISTENT_EXTENSIONS_DIR = "PwnChromeExtensionsPath";
    public final String PERSISTENT_PROFILES_DIR = "PwnChromeProfilesPath";
    // hard-wired names of required directories in the PwnChrome extensions directory
    // need this to validate that the user gave us a valid path to the directory with
    // all required Chrome extensions they downloaded from GitHub
    private final String THEMES_DIR = "themes";
    private final String HEADER_EXTENSIONS_DIR = "header-extensions";
    private final String PROXY_EXTENSION_DIR = "proxy-extension";
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

    // https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
    // sorry if this causes heap pollution ¯\_(ツ)_/¯
    // guess it's your usual BurpSuite experience then
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

    private String getPwnChromiumLoadExtensionArgs(String pwnChromeDirPath, String themeColor) {
        // build --load-extension argument passed to the Chromium to hot-load proxy, theme and header extensions
        String headerExtensionDir = Paths.get(pwnChromeDirPath, HEADER_EXTENSIONS_DIR, themeColor.toLowerCase()).toString();
        String proxyExtensionDir = Paths.get(pwnChromeDirPath, PROXY_EXTENSION_DIR).toString();
        String themeDir = Paths.get(pwnChromeDirPath, THEMES_DIR, themeColor.toLowerCase()).toString();
        return "--load-extension=" + String.join(",", headerExtensionDir, proxyExtensionDir, themeDir);
    }

    private String getPwnChromiumProfileArgs(String pwnChromeProfilesDir, String themeColor) {
        String pwnChromeProfileDir = Paths.get(pwnChromeProfilesDir, themeColor.toLowerCase()).toString();
        return "--user-data-dir="+pwnChromeProfileDir;
    }

    private void populateDefaults() {
        // populate default options for the extension
        // only Chromium profiles directory is supported for now
        String persistentProfilesDir = this.api.persistence().preferences().getString(PERSISTENT_PROFILES_DIR);
        // set the profiles directory to ~/.PwnChromiumData if nothing was declared
        // bail out otherwards
        if (persistentProfilesDir == null) {
            String homeDir = System.getProperty("user.home");
            Path profileDirPath = Paths.get(homeDir, ".PwnChromiumData");
            try {
                if (!Files.exists(profileDirPath)) {
                    Files.createDirectory(profileDirPath);
                    this.api.persistence().preferences().setString(PERSISTENT_PROFILES_DIR, profileDirPath.toString());
                    this.api.logging().logToOutput("Created ~/.PwnChromiumData directory...");
                }
            } catch (IOException e) {
                this.api.logging().logToError(e);
            }
        }
    }

    public boolean startDetachedPwnChromium(String pwnChromeExePath, String pwnChromeExtensionsPath, String pwnChromeProfilesDir, String themeColor) {
        // start Chromium with needed args and a correct profile
        try {
            String[] pwnChromiumArgs = concatAll(
                    // I just want my list expansion from Python >:(
                    new String[] { pwnChromeExePath },
                    this.PWNCHROME_DEFAULT_ARGS,
                    new String[] { getPwnChromiumLoadExtensionArgs(pwnChromeExtensionsPath, themeColor) },
                    new String[] { getPwnChromiumProfileArgs(pwnChromeProfilesDir, themeColor) }
            );
            ProcessBuilder processBuilder = new ProcessBuilder(pwnChromiumArgs);
            processBuilder.start();
            return true;
        } catch (IOException e) {
            this.api.logging().logToError(e);
            return false;
        }
    }

    public boolean isChromiumExecutableValid(String pwnChromeExePath) {
        // check if executable is named Chrome or Chromium
        String chromeExeName = Paths.get(pwnChromeExePath).getFileName().toString().toLowerCase();
        if (!(chromeExeName.contains("chrom"))) {
            return false;
        }
        // check if the --version flag of the chromium executable returns Chrome or Chromium in the output
        // a more robust check if this is actually a Chromium exe
        if (System.getProperty("os.name").startsWith("Windows")) {
            // skip this check on Windows cause apparently Chrome.exe does not have --version flag
            // why?
            // cause fuck you, that's why
            return true;
        }
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(pwnChromeExePath, "--version");
            Process process = processBuilder.start();
            String output = new String(process.getInputStream().readAllBytes());
            // check for a substring of "Chrom" in either "Chrome" or "Chromium"
            // if that's the case, then we should be good
            return output.contains("Chrom");
        } catch (IOException e) {
            this.api.logging().logToError(e);
            return false;
        }
    }

    public boolean isDirectoryValid(String directory) {
        return Files.isDirectory(Paths.get(directory));
    }

    public boolean isExtensionsDirectoryValid(String directory) {
        return isDirectoryValid(Paths.get(directory, THEMES_DIR).toString()) &&
                isDirectoryValid(Paths.get(directory, HEADER_EXTENSIONS_DIR).toString()) &&
                isDirectoryValid(Paths.get(directory, PROXY_EXTENSION_DIR).toString());
    }

    @Override
    public void initialize(MontoyaApi api)
    {
        this.api = api;
        this.api.extension().setName("PwnFox For Chromium");
        populateDefaults();
        this.api.userInterface().registerSuiteTab("PwnFox For Chromium", new PwnFoxForChromiumUI(this).getUI());
    }
}