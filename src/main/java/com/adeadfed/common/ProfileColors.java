package com.adeadfed.common;

import java.awt.*;

public enum ProfileColors {
    BLUE("blue", new Color(59, 173, 255)),
    CYAN("cyan", new Color(42, 199, 154)),
    GREEN("green", new Color(81, 205, 0)),
    MAGENTA("magenta", new Color(176, 115, 241)),
    ORANGE("orange", new Color(255, 160, 25)),
    PINK("pink", new Color(255, 75, 218)),
    RED("red", new Color(255, 98, 79)),
    YELLOW("yellow", new Color(252, 203, 0));

    private final String name;
    private final Color color;

    private ProfileColors(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getColorRGB() {
        return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}
