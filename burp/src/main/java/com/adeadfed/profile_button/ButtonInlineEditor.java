package com.adeadfed.profile_button;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ButtonInlineEditor {

    public static JTextField fromButton(JButton source) {
        JTextField editor = new JTextField(source.getText());
        editor.setFont(source.getFont());
        editor.setForeground(source.getForeground());
        editor.setBackground(source.getBackground());
        editor.setBorder(source.getBorder());
        editor.setHorizontalAlignment(SwingConstants.CENTER);

        SwingUtilities.invokeLater(() -> {
                editor.requestFocusInWindow();
                editor.selectAll();
        });
        
        return editor;
    }

    public static void setupCallback(JTextField editor, Runnable callback) {
        editor.addActionListener(e -> callback.run());
        editor.addFocusListener(new FocusAdapter() { @Override public void focusLost(FocusEvent e) { callback.run(); } });
    }
}
