package com.adeadfed.profile_button;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JComponent;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

public class ButtonGridLayout {
    private Container parent;
    private GridConstraints constraints;
   

    public ButtonGridLayout(JButton button) throws Exception {
        parent = button.getParent();
        if (parent == null || !(parent.getLayout() instanceof GridLayoutManager manager)) {
            throw new Exception("Failed to initialize ButtonGridLayout object. No Parent or no GridLayoutManager");
        }
        constraints = manager.getConstraintsForComponent(button);
    }

    public void swapComponent(JComponent oldComponent, JComponent newComponent) {
        parent.remove(oldComponent);
        parent.add(newComponent, constraints);
        parent.revalidate();
        parent.repaint();
    }
}