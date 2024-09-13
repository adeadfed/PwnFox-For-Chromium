package com.adeadfed;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Locale;

public class PwnFoxForChromiumUI {
    private JPanel ui;
    private final PwnFoxForChromium pwnChromiumExtension;
    private JTextField pwnChromeExePath;
    private JTextField pwnChromeExtensionsPath;
    private JTextField pwnChromeProfilesPath;
    private JButton chooseExeButton;
    private JButton chooseExtDirButton;
    private JButton chooseProfilesDirButton;
    private JPanel settingsPanel;
    private JPanel buttonsPanel;
    private JButton blueButton;
    private JButton cyanButton;
    private JButton greenButton;
    private JButton yellowButton;
    private JButton redButton;
    private JButton orangeButton;
    private JButton pinkButton;
    private JButton magentaButton;

    public JPanel getUI() {
        return this.ui;
    }

    private void uiChoosePath(JTextField uiPath, String settingsKey, int pathMode) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(pathMode);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            this.pwnChromiumExtension.api.persistence().preferences().setString(settingsKey, path);
            uiPath.setText(path);
        } else {
            JOptionPane.showMessageDialog(null, "Nothing selected!");
        }
    }

    private boolean areSettingsValid() {
        return pwnChromeExePath.getInputVerifier().verify(pwnChromeExePath) &&
                pwnChromeExtensionsPath.getInputVerifier().verify(pwnChromeExtensionsPath) &&
                pwnChromeProfilesPath.getInputVerifier().verify(pwnChromeProfilesPath);
    }

    private void uiStartDetachedPwnChromium(String themeColor) {
        // validate settings before the launch one final time
        if (areSettingsValid()) {
            String chromiumExePath = pwnChromeExePath.getText();
            String chromiumExtensionsPath = pwnChromeExtensionsPath.getText();
            String chromiumProfilesPath = pwnChromeProfilesPath.getText();
            boolean result = this.pwnChromiumExtension.startDetachedPwnChromium(chromiumExePath, chromiumExtensionsPath, chromiumProfilesPath, themeColor);
            if (!result) {
                JOptionPane.showMessageDialog(null, "An error launching PwnChromium has occurred. Check the extension logs");
            }
        }
    }

    private void setupSettingsButton(JButton button, JTextField uiPath, String settingsKey, int pathMode) {
        // setup settings buttons to pick the required Chrome exe and extension dir using Swing GUI
        String path = this.pwnChromiumExtension.api.persistence().preferences().getString(settingsKey);
        if (path != null) {
            uiPath.setText(path);
        }
        button.addActionListener(e -> uiChoosePath(uiPath, settingsKey, pathMode));
    }

    private void setupProfileButtons() {
        JButton[] profileButtons = {
                blueButton, cyanButton, greenButton, yellowButton,
                redButton, orangeButton, pinkButton, magentaButton
        };

        ActionListener profileActionListener = e -> {
            String themeColor = e.getActionCommand();
            uiStartDetachedPwnChromium(themeColor);
        };

        for (JButton b : profileButtons) {
            b.addActionListener(profileActionListener);
        }
    }

    public PwnFoxForChromiumUI(PwnFoxForChromium pwnChromiumExtension) {
        // setup Swing GUI
        this.pwnChromiumExtension = pwnChromiumExtension;
        this.pwnChromeExePath.setInputVerifier(new TextFieldVerifier(this.pwnChromiumExtension::isChromiumExecutableValid));
        this.pwnChromeExtensionsPath.setInputVerifier(new TextFieldVerifier(this.pwnChromiumExtension::isExtensionsDirectoryValid));
        this.pwnChromeProfilesPath.setInputVerifier(new TextFieldVerifier(this.pwnChromiumExtension::isDirectoryValid));
        setupSettingsButton(chooseExeButton, pwnChromeExePath, this.pwnChromiumExtension.PERSISTENT_CHROME_EXE, JFileChooser.FILES_ONLY);
        setupSettingsButton(chooseExtDirButton, pwnChromeExtensionsPath, this.pwnChromiumExtension.PERSISTENT_EXTENSIONS_DIR, JFileChooser.DIRECTORIES_ONLY);
        setupSettingsButton(chooseProfilesDirButton, pwnChromeProfilesPath, this.pwnChromiumExtension.PERSISTENT_PROFILES_DIR, JFileChooser.DIRECTORIES_ONLY);
        setupProfileButtons();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        ui = new JPanel();
        ui.setLayout(new GridLayoutManager(4, 1, new Insets(30, 30, 30, 30), -1, -1));
        settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayoutManager(8, 3, new Insets(20, 0, 0, 0), -1, -1));
        ui.add(settingsPanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Path to the Chromium executable");
        settingsPanel.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pwnChromeExePath = new JTextField();
        pwnChromeExePath.setEditable(true);
        pwnChromeExePath.setEnabled(true);
        settingsPanel.add(pwnChromeExePath, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Path to the PwnFox For Chromium extensions directory");
        settingsPanel.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pwnChromeExtensionsPath = new JTextField();
        pwnChromeExtensionsPath.setEditable(true);
        pwnChromeExtensionsPath.setEnabled(true);
        settingsPanel.add(pwnChromeExtensionsPath, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        chooseExtDirButton = new JButton();
        chooseExtDirButton.setText("Choose...");
        settingsPanel.add(chooseExtDirButton, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chooseExeButton = new JButton();
        chooseExeButton.setText("Choose...");
        settingsPanel.add(chooseExeButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        settingsPanel.add(spacer1, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        settingsPanel.add(spacer2, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, 22, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Settings");
        settingsPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Path to the PwnFox For Chromium profile data directory");
        settingsPanel.add(label4, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pwnChromeProfilesPath = new JTextField();
        pwnChromeProfilesPath.setEditable(true);
        pwnChromeProfilesPath.setEnabled(true);
        settingsPanel.add(pwnChromeProfilesPath, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        chooseProfilesDirButton = new JButton();
        chooseProfilesDirButton.setText("Choose...");
        settingsPanel.add(chooseProfilesDirButton, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        ui.add(buttonsPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        blueButton = new JButton();
        blueButton.setBackground(new Color(-12866049));
        blueButton.setBorderPainted(true);
        blueButton.setContentAreaFilled(true);
        blueButton.setFocusPainted(false);
        Font blueButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, blueButton.getFont());
        if (blueButtonFont != null) blueButton.setFont(blueButtonFont);
        blueButton.setForeground(new Color(-1));
        blueButton.setName("Blue");
        blueButton.setText("Blue");
        buttonsPanel.add(blueButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(100, 100), null, 0, false));
        cyanButton = new JButton();
        cyanButton.setBackground(new Color(-13973606));
        Font cyanButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, cyanButton.getFont());
        if (cyanButtonFont != null) cyanButton.setFont(cyanButtonFont);
        cyanButton.setForeground(new Color(-1));
        cyanButton.setName("Cyan");
        cyanButton.setText("Cyan");
        buttonsPanel.add(cyanButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        greenButton = new JButton();
        greenButton.setBackground(new Color(-11416320));
        Font greenButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, greenButton.getFont());
        if (greenButtonFont != null) greenButton.setFont(greenButtonFont);
        greenButton.setForeground(new Color(-1));
        greenButton.setName("Green");
        greenButton.setText("Green");
        buttonsPanel.add(greenButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        yellowButton = new JButton();
        yellowButton.setBackground(new Color(-210176));
        Font yellowButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, yellowButton.getFont());
        if (yellowButtonFont != null) yellowButton.setFont(yellowButtonFont);
        yellowButton.setForeground(new Color(-1));
        yellowButton.setLabel("Yellow");
        yellowButton.setName("Yellow");
        yellowButton.setText("Yellow");
        buttonsPanel.add(yellowButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        orangeButton = new JButton();
        orangeButton.setBackground(new Color(-24551));
        Font orangeButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, orangeButton.getFont());
        if (orangeButtonFont != null) orangeButton.setFont(orangeButtonFont);
        orangeButton.setForeground(new Color(-1));
        orangeButton.setName("Orange");
        orangeButton.setText("Orange");
        buttonsPanel.add(orangeButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        redButton = new JButton();
        redButton.setBackground(new Color(-40369));
        redButton.setBorderPainted(true);
        Font redButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, redButton.getFont());
        if (redButtonFont != null) redButton.setFont(redButtonFont);
        redButton.setForeground(new Color(-1));
        redButton.setName("Red");
        redButton.setText("Red");
        buttonsPanel.add(redButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        pinkButton = new JButton();
        pinkButton.setBackground(new Color(-46118));
        Font pinkButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, pinkButton.getFont());
        if (pinkButtonFont != null) pinkButton.setFont(pinkButtonFont);
        pinkButton.setForeground(new Color(-1));
        pinkButton.setName("Pink");
        pinkButton.setText("Pink");
        buttonsPanel.add(pinkButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        magentaButton = new JButton();
        magentaButton.setBackground(new Color(-5213199));
        Font magentaButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, magentaButton.getFont());
        if (magentaButtonFont != null) magentaButton.setFont(magentaButtonFont);
        magentaButton.setForeground(new Color(-1));
        magentaButton.setName("Magenta");
        magentaButton.setText("Magenta");
        buttonsPanel.add(magentaButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, 22, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("PwnChromium Profiles");
        ui.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        ui.add(spacer3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        label1.setLabelFor(pwnChromeExePath);
        label2.setLabelFor(pwnChromeExtensionsPath);
        label4.setLabelFor(pwnChromeProfilesPath);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return ui;
    }

}