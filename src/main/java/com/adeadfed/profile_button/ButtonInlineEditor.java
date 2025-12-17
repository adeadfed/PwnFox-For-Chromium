package com.adeadfed.profile_button;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ButtonInlineEditor {
    private JTextField textField;

    public ButtonInlineEditor(JButton source) throws Exception{
        textField = new JTextField(source.getText());
        textField.setFont(source.getFont());
        textField.setForeground(source.getForeground());
        textField.setBackground(source.getBackground());
        textField.setBorder(source.getBorder());
        textField.setHorizontalAlignment(SwingConstants.CENTER);

        SwingUtilities.invokeLater(() -> {
                textField.requestFocusInWindow();
                textField.selectAll();
        });
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setupCallback(JTextField editor, Runnable callback) {
        editor.addActionListener(e -> callback.run());
        editor.addFocusListener(new FocusAdapter() { @Override public void focusLost(FocusEvent e) { callback.run(); } });
    }
}
