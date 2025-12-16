package com.adeadfed.browser_extensions;

import com.adeadfed.common.ProfileColors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BrowserExtensionsLoader {

    private final String HEADER_EXTENSION_DIR = "header-extension";
    private final String PROXY_EXTENSION_DIR = "proxy-extension";
    private final String THEME_DIR = "themes";

    private static String BROWSER_EXTENSIONS_PREFIX = "browser-extensions";

    private HeaderExtension headerExtension;
    private ProxyExtension proxyExtension;
    private ThemeExtension themeExtension;

    public BrowserExtensionsLoader(String pwnChromeProfileDir, ProfileColors profileColor) throws IOException {
        String browserExtensionsDir = Paths.get(
                pwnChromeProfileDir,
                BROWSER_EXTENSIONS_PREFIX,
                profileColor.getName()
        ).toString();

        headerExtension = new HeaderExtension(
                Paths.get(browserExtensionsDir, HEADER_EXTENSION_DIR).toString(),
                profileColor
        );

        themeExtension = new ThemeExtension(
                Paths.get(browserExtensionsDir, THEME_DIR).toString(),
                profileColor
        );

        proxyExtension = new ProxyExtension(
                Paths.get(browserExtensionsDir, PROXY_EXTENSION_DIR).toString()
        );

        if (!areBrowserExtensionsLoaded()) {
            createExtensionDirectories();
            loadExtensions();
        }
    }

    private boolean areBrowserExtensionsLoaded() {
        return headerExtension.isLoaded() &&
                proxyExtension.isLoaded() &&
                themeExtension.isLoaded();
    }

    private void createExtensionDirectories() throws IOException {
        Files.createDirectories(Paths.get(headerExtension.getExtensionDir()));
        Files.createDirectories(Paths.get(proxyExtension.getExtensionDir()));
        Files.createDirectories(Paths.get(themeExtension.getExtensionDir()));
    }

    private void loadExtensions() throws IOException {
        proxyExtension.load();
        themeExtension.load();
        headerExtension.load();
    }

    public String getHeaderExtensionDir() {
        return headerExtension.getExtensionDir();
    }

    public String getProxyExtensionDir() {
        return proxyExtension.getExtensionDir();
    }

    public String getThemeDir() {
        return themeExtension.getExtensionDir();
    }
}
