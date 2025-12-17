package com.adeadfed.profile_button;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ButtonInlineEditor {
    private JButton button;
    private JTextField textField;
    private ButtonGridLayout layout;

    public ButtonInlineEditor(JButton button) throws Exception {
        this.button = button;
        layout = new ButtonGridLayout(button);

        textField = new JTextField(button.getText());
        textField.setFont(button.getFont());
        textField.setForeground(button.getForeground());
        textField.setBackground(button.getBackground());
        textField.setBorder(button.getBorder());
        textField.setHorizontalAlignment(SwingConstants.CENTER);

        SwingUtilities.invokeLater(() -> {
                textField.requestFocusInWindow();
                textField.selectAll();
        });
    }

    public String getText() {
        return textField.getText().trim();
    }

    public void setupCallback(Runnable callback) {
        textField.addActionListener(e -> callback.run());
        textField.addFocusListener(new FocusAdapter() { @Override public void focusLost(FocusEvent e) { callback.run(); } });
    }

    public void startEdit() {
        layout.swapComponent(button, textField);
    }

    public void stopEdit() {
        layout.swapComponent(textField, button);
    }
}
