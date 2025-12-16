package com.adeadfed;

import com.adeadfed.preferences.Preference;

import com.adeadfed.common.ProfileColors;
import com.adeadfed.browser.Browser;
import com.adeadfed.validators.FsValidator;

import com.adeadfed.validators.TextFieldVerifier;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.util.Locale;

public class PwnFoxForChromiumUI {
    private JPanel ui;
    private PwnFoxForChromium pwnChromiumExtension;
    private JTextField pwnChromeExePath;
    private JTextField pwnChromeProfilesPath;
    private JButton chooseExeButton;
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
    private JLabel helpLabel;

    public JPanel getUI() {
        return ui;
    }

    private void uiChoosePath(Preference preference, JTextField uiPath, int pathMode) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(pathMode);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            pwnChromiumExtension.pwnChromiumPreferences.set(preference, path);
            uiPath.setText(path);
        } else {
            JOptionPane.showMessageDialog(null, "Nothing selected!");
        }
    }

    private boolean areSettingsValid() {
        return pwnChromeExePath.getInputVerifier().verify(pwnChromeExePath) &&
                pwnChromeProfilesPath.getInputVerifier().verify(pwnChromeProfilesPath);
    }

    private void uiStartDetachedPwnChromium(String themeColor) {
        if (areSettingsValid()) {
            String chromiumExePath = pwnChromeExePath.getText();
            String chromiumProfilesPath = pwnChromeProfilesPath.getText();
            Browser browser = new Browser(chromiumExePath, chromiumProfilesPath, themeColor);
            try {
                Process process = browser.start();
                pwnChromiumExtension.montoyaApi.logging().logToOutput(
                        String.format("PwnChromium %s started with PID: %d", themeColor, process.pid())
                );
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An error launching PwnChromium has occurred. Check the extension logs");
                pwnChromiumExtension.montoyaApi.logging().logToError(e);
            }
        }
    }

    private void setupPreferenceButton(Preference preference, JButton button, JTextField uiPath, int pathMode) {
        String path = pwnChromiumExtension.pwnChromiumPreferences.get(preference);
        if (path != null) {
            uiPath.setText(path);
        }
        button.addActionListener(e -> uiChoosePath(preference, uiPath, pathMode));
    }

    private void setupProfileButtons() {
        JButton[] profileButtons = {
                blueButton, cyanButton, greenButton, yellowButton,
                redButton, orangeButton, pinkButton, magentaButton
        };

        ActionListener profileActionListener = e -> {
            JButton buttonPressed = (JButton) e.getSource();

            // Check if Ctrl/Command key was pressed
            boolean ctrlDown = (e.getModifiers() & InputEvent.CTRL_MASK) != 0
                    || (e.getModifiers() & InputEvent.META_MASK) != 0; // Command key

            if (ctrlDown) {
                // Ctrl/Command + Click: rename button
                pwnChromiumExtension.montoyaApi.logging().logToOutput("About to start inline edit");
                startInlineEdit(buttonPressed);
            } else {
                // Regular click: launch Chromium
                String themeColor = buttonPressed.getName();
                pwnChromiumExtension.montoyaApi.logging().logToOutput("About to start chromium: " + themeColor);
                uiStartDetachedPwnChromium(themeColor);
            }
        };

        for (JButton b : profileButtons) {
            b.addActionListener(profileActionListener);
        }
    }

    /**
     * Replace a JButton with a temporary text field so the label can be edited.
     */
    private void startInlineEdit(JButton button) {
        Container parent = button.getParent();
        if (parent == null) return;

        // Grab the same layout constraints IntelliJ uses
        GridLayoutManager layout = null;
        GridConstraints constraints = null;
        if (parent.getLayout() instanceof GridLayoutManager gLayout) {
            layout = gLayout;
            constraints = layout.getConstraintsForComponent(button);
        }

        JTextField editor = new JTextField(button.getText());
        editor.setFont(button.getFont());
        editor.setForeground(button.getForeground());
        editor.setBackground(button.getBackground());
        editor.setBorder(button.getBorder());
        editor.setOpaque(true);
        editor.setHorizontalAlignment(SwingConstants.CENTER);

        // replace within container
        parent.remove(button);
        if (layout != null && constraints != null) {
            parent.add(editor, constraints);   // preserve grid cell and sizing
        } else {
            parent.add(editor);  // fallback for non‑designer layout
        }
        parent.revalidate();
        parent.repaint();

        // focus after layout
        SwingUtilities.invokeLater(() -> {
            editor.requestFocusInWindow();
            editor.selectAll();
        });

        // Inline commit handler
        final GridLayoutManager layoutFinal = layout;
        final GridConstraints constraintsFinal = constraints;
        Runnable commit = () -> {
            String newText = editor.getText().trim();
            if (!newText.isEmpty()) {
                button.setText(newText);
            }
            parent.remove(editor);
            if (layoutFinal != null && constraintsFinal != null) {
                parent.add(button, constraintsFinal);
            } else {
                parent.add(button);
            }
            parent.revalidate();
            parent.repaint();
        };

        // save when Enter is pressed
        editor.addActionListener(e -> commit.run());

        // save when focus is lost
        editor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                commit.run();
            }
        });
    }

    private void setPwnChromiumExtension(PwnFoxForChromium extension) {
        this.pwnChromiumExtension = extension;
    }

    public PwnFoxForChromiumUI(PwnFoxForChromium pwnChromiumExtension) {
        setPwnChromiumExtension(pwnChromiumExtension);

        pwnChromeExePath.setInputVerifier(new TextFieldVerifier(FsValidator::isChromiumExecutableValid));
        pwnChromeProfilesPath.setInputVerifier(new TextFieldVerifier(FsValidator::isDirValid));
       
        setupPreferenceButton(
            pwnChromiumExtension.pwnChromiumPreferences.browserPath,
            chooseExeButton,
            pwnChromeExePath, 
            JFileChooser.FILES_ONLY
        );

        setupPreferenceButton(
            pwnChromiumExtension.pwnChromiumPreferences.profilesDir,
            chooseProfilesDirButton,
            pwnChromeProfilesPath,
            JFileChooser.DIRECTORIES_ONLY
        );

        setupProfileButtons();

        // Detect platform and set label text
        String modifierKey = System.getProperty("os.name").toLowerCase().contains("mac")
                ? "⌘" : "Ctrl";
        helpLabel.setText(modifierKey + " + Click to edit button names");
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
        Color white = new Color(-1);
        ui = new JPanel();
        ui.setLayout(new GridLayoutManager(4, 2, new Insets(30, 30, 30, 30), -1, -1));
        settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayoutManager(6, 3, new Insets(20, 0, 0, 0), -1, -1));
        ui.add(settingsPanel, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Path to the Chromium executable");
        settingsPanel.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pwnChromeExePath = new JTextField();
        pwnChromeExePath.setEditable(true);
        pwnChromeExePath.setEnabled(true);
        settingsPanel.add(pwnChromeExePath, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        chooseExeButton = new JButton();
        chooseExeButton.setText("Choose...");
        settingsPanel.add(chooseExeButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        settingsPanel.add(spacer1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        settingsPanel.add(spacer2, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, 22, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Settings");
        settingsPanel.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Path to the PwnFox For Chromium profile data directory");
        settingsPanel.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pwnChromeProfilesPath = new JTextField();
        pwnChromeProfilesPath.setEditable(true);
        pwnChromeProfilesPath.setEnabled(true);
        settingsPanel.add(pwnChromeProfilesPath, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        chooseProfilesDirButton = new JButton();
        chooseProfilesDirButton.setText("Choose...");
        settingsPanel.add(chooseProfilesDirButton, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        ui.add(buttonsPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        blueButton = new JButton();
        blueButton.setBackground(ProfileColors.BLUE.getColor());
        blueButton.setBorderPainted(true);
        blueButton.setContentAreaFilled(true);
        blueButton.setFocusPainted(false);
        Font blueButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, blueButton.getFont());
        if (blueButtonFont != null) blueButton.setFont(blueButtonFont);
        blueButton.setForeground(white);
        blueButton.setName("Blue");
        blueButton.setText("Blue");
        buttonsPanel.add(blueButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(100, 100), null, 0, false));
        cyanButton = new JButton();
        cyanButton.setBackground(ProfileColors.CYAN.getColor());
        Font cyanButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, cyanButton.getFont());
        if (cyanButtonFont != null) cyanButton.setFont(cyanButtonFont);
        cyanButton.setForeground(white);
        cyanButton.setName("Cyan");
        cyanButton.setText("Cyan");
        buttonsPanel.add(cyanButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        greenButton = new JButton();
        greenButton.setBackground(ProfileColors.GREEN.getColor());
        Font greenButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, greenButton.getFont());
        if (greenButtonFont != null) greenButton.setFont(greenButtonFont);
        greenButton.setForeground(white);
        greenButton.setName("Green");
        greenButton.setText("Green");
        buttonsPanel.add(greenButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        yellowButton = new JButton();
        yellowButton.setBackground(ProfileColors.YELLOW.getColor());
        Font yellowButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, yellowButton.getFont());
        if (yellowButtonFont != null) yellowButton.setFont(yellowButtonFont);
        yellowButton.setForeground(white);
        yellowButton.setName("Yellow");
        yellowButton.setText("Yellow");
        buttonsPanel.add(yellowButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        orangeButton = new JButton();
        orangeButton.setBackground(ProfileColors.ORANGE.getColor());
        Font orangeButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, orangeButton.getFont());
        if (orangeButtonFont != null) orangeButton.setFont(orangeButtonFont);
        orangeButton.setForeground(white);
        orangeButton.setName("Orange");
        orangeButton.setText("Orange");
        buttonsPanel.add(orangeButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        redButton = new JButton();
        redButton.setBackground(ProfileColors.RED.getColor());
        redButton.setBorderPainted(true);
        Font redButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, redButton.getFont());
        if (redButtonFont != null) redButton.setFont(redButtonFont);
        redButton.setForeground(white);
        redButton.setName("Red");
        redButton.setText("Red");
        buttonsPanel.add(redButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        pinkButton = new JButton();
        pinkButton.setBackground(ProfileColors.PINK.getColor());
        Font pinkButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, pinkButton.getFont());
        if (pinkButtonFont != null) pinkButton.setFont(pinkButtonFont);
        pinkButton.setForeground(white);
        pinkButton.setName("Pink");
        pinkButton.setText("Pink");
        buttonsPanel.add(pinkButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        magentaButton = new JButton();
        magentaButton.setBackground(ProfileColors.MAGENTA.getColor());
        Font magentaButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, magentaButton.getFont());
        if (magentaButtonFont != null) magentaButton.setFont(magentaButtonFont);
        magentaButton.setForeground(white);
        magentaButton.setName("Magenta");
        magentaButton.setText("Magenta");
        buttonsPanel.add(magentaButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, 22, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("PwnChromium Profiles");
        ui.add(label4, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        ui.add(spacer3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        helpLabel = new JLabel();
        helpLabel.setText("Ctrl+Click to edit button names");
        ui.add(helpLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        label1.setLabelFor(pwnChromeExePath);
        label3.setLabelFor(pwnChromeProfilesPath);
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
