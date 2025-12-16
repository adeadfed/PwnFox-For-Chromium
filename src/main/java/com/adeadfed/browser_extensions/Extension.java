package com.adeadfed.browser_extensions;

import com.adeadfed.validators.FsValidator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

abstract class Extension {
    protected String extensionDir;

    public Extension(String extensionDir) {
        this.extensionDir = extensionDir;
    }

    public boolean isLoaded() {
        return FsValidator.isDirValid(this.extensionDir);
    }

    public String getExtensionDir() {
        return extensionDir;
    }

    protected InputStream openJarResource(String resourcePath) {
        return getClass().getClassLoader().getResourceAsStream(resourcePath);
    }

    protected void copyJarResource(String resourcePath, String targetPath) throws IOException {
        Files.copy(
                openJarResource(resourcePath),
                Paths.get(extensionDir, targetPath),
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    abstract void load() throws IOException;
}
