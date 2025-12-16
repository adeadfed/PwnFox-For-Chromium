package com.adeadfed.profile_button;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JComponent;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

public class ButtonGridLayout {
    final Container parent;
    final GridLayoutManager manager;
    final GridConstraints constraints;
    
    ButtonGridLayout(Container p, GridLayoutManager l, GridConstraints c) {
        parent = p; manager = l; constraints = c;
    }

    public static ButtonGridLayout layoutFrom(JButton button) throws Exception {
        Container parent = button.getParent();
        if (parent == null || !(parent.getLayout() instanceof GridLayoutManager g)) {
            throw new Exception("Failed to initialize ButtonGridLayout object. No Parent or no GridLayoutManager");
        }
        GridConstraints gc = g.getConstraintsForComponent(button);
        return new ButtonGridLayout(parent, g, gc);
    }

    public void swapComponent(JComponent oldComponent, JComponent newComponent) {
        parent.remove(oldComponent);
        parent.add(newComponent, constraints);
        parent.revalidate();
        parent.repaint();
    }
}