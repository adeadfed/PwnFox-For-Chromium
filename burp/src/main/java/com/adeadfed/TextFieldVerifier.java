package com.adeadfed;
import java.util.function.Predicate;

import javax.swing.*;
import java.awt.Color;


public class TextFieldVerifier extends InputVerifier {
    private Predicate<String> validator;
    private final Color defaultBackground = new Color(153, 255, 153);
    private final Color invalidBackground = new Color(255, 102, 102);

    public TextFieldVerifier(Predicate<String> validator) {
        this.validator = validator;
    }

    @Override
    public boolean verify(JComponent input) {
        boolean result = this.validator.test(((JTextField) input).getText());
        input.setBackground(result ? defaultBackground : invalidBackground);
        return result;
    }
}