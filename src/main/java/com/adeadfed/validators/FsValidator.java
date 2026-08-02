package com.adeadfed.validators;

import com.adeadfed.common.OsType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Locale;

public class FsValidator {
    public static boolean isDirValid(String directory) {
        return directory.length() != 0 && Files.isDirectory(Paths.get(directory));
    }

    public static boolean isChromiumExecutableValid(String pwnChromeExePath) {
        try {
            final var executablePath = Paths.get(pwnChromeExePath);
            if (!Files.isRegularFile(executablePath) || !Files.isExecutable(executablePath)) {
                return false;
            }

            String executableName = executablePath.getFileName().toString().toLowerCase(Locale.ROOT);
            if (!executableName.contains("chrom") && !executableName.contains("burp browser")) {
                return false;
            }

            // Check that the executable identifies itself as a supported browser.
            if (OsType.isWindows()) {
                return true;
            }

            ProcessBuilder processBuilder = new ProcessBuilder(executablePath.toString(), "--version")
                    .redirectErrorStream(true);
            Process process = processBuilder.start();
            String output = new String(
                    process.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            ).toLowerCase(Locale.ROOT);
            return output.contains("chrom") || output.contains("burp browser");
        } catch (IOException | InvalidPathException e) {
            return false;
        }
    }
}
