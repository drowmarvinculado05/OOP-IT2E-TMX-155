package com.bantayalert.ui;

import javax.swing.border.AbstractBorder;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

/**
 * Invisible border that only adds padding/margins.
 */
class InsetsBorder extends AbstractBorder {
    private final Insets insets;

    InsetsBorder(Insets insets) {
        this.insets = insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        // No painting needed; acts purely as spacing.
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return (Insets) insets.clone();
    }
}

