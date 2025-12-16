package com.adeadfed.browser_extensions;


import java.io.IOException;

public class ProxyExtension extends Extension {
    public ProxyExtension(String extensionDir) {
        super(extensionDir);
    }

    @Override
    void load() throws IOException {
        copyJarResource("browser-extensions/proxy-extension/background.js", "background.js");
        copyJarResource("browser-extensions/proxy-extension/manifest.json", "manifest.json");
        copyJarResource("browser-extensions/proxy-extension/popup.html", "popup.html");
        copyJarResource("browser-extensions/proxy-extension/popup.js", "popup.js");
    }
}
