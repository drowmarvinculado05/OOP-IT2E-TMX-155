package com.bantayalert.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Lightweight mouse listener that executes a runnable when clicked.
 */
class SimpleLinkMouseListener extends MouseAdapter {
    private final Runnable onClick;

    SimpleLinkMouseListener(Runnable onClick) {
        this.onClick = onClick;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (onClick != null) {
            onClick.run();
        }
    }
}

