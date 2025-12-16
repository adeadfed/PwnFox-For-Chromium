package com.adeadfed.common;

import java.util.Locale;

public enum OsType {
    LINUX,
    WINDOWS,
    MACOS,
    UNKNOWN;

    public static OsType getOsType() {
        String name = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

        if (name.contains("nix") || name.contains("nux") || name.contains("aix")) {
            return LINUX;
        }
        if (name.contains("win")) {
            return WINDOWS;
        }
        if (name.contains("mac") || name.contains("darwin")) {
            return MACOS;
        }
        return UNKNOWN;
    }

    public static boolean isMacOS() {
        return getOsType() == OsType.MACOS;
    }

    public static boolean isWindows() {
        return getOsType() == OsType.WINDOWS;
    }

    public static boolean isLinux() {
        return getOsType() == OsType.LINUX;
    }
}
