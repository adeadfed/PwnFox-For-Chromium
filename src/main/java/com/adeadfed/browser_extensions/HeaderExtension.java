package com.adeadfed.browser_extensions;

import com.adeadfed.common.ProfileColors;

import java.io.IOException;

public class HeaderExtension extends ColorExtension {
    public HeaderExtension(String extensionDir, ProfileColors profileColor) {
        super(extensionDir, profileColor);
    }

    @Override
    void load() throws IOException {
        copyJarColorResource(
                "browser-extensions/header-extension-template/background.js",
                "background.js"
        );
        copyJarColorResource(
                "browser-extensions/header-extension-template/manifest.json",
                "manifest.json"
        );
    }
}
