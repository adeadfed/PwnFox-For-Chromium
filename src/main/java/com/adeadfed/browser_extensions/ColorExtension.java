package com.adeadfed.browser_extensions;

import com.adeadfed.common.ProfileColors;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

abstract class ColorExtension extends Extension {
    protected ProfileColors profileColor;

    public ColorExtension(String extensionDir, ProfileColors profileColor) {
        super(extensionDir);
        this.profileColor = profileColor;
    }

    protected void copyJarColorResource(String colorResourceTemplatePath, String targetPath) throws IOException {
        try (InputStream inputStream = openJarResource(colorResourceTemplatePath)) {

            String colorResource = new String(inputStream.readAllBytes());
            colorResource = colorResource.replace("{{colorrgb}}", profileColor.getColorRGB());
            colorResource = colorResource.replace("{{colorname}}", profileColor.getName());

            Files.writeString(
                    Paths.get(extensionDir, targetPath),
                    colorResource
            );
        }
    }
}
