package com.bantayalert.ui;

import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Simple focus listener to emulate placeholders for text components.
 */
class PlaceholderFocusListener extends FocusAdapter {
    private final javax.swing.text.JTextComponent component;
    private final String placeholder;
    private final Color placeholderColor = new Color(150, 150, 150);
    private final Color textColor = Color.BLACK;

    PlaceholderFocusListener(javax.swing.text.JTextComponent component, String placeholder) {
        this.component = component;
        this.placeholder = placeholder;
        component.setForeground(placeholderColor);
        setPlaceholderActive(true);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (placeholder.equals(component.getText())) {
            component.setText("");
            component.setForeground(textColor);
            setPlaceholderActive(false);
            if (component instanceof JPasswordField passwordField) {
                passwordField.setEchoChar('•');
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (component.getText().isEmpty()) {
            component.setText(placeholder);
            component.setForeground(placeholderColor);
            setPlaceholderActive(true);
            if (component instanceof JPasswordField passwordField) {
                passwordField.setEchoChar((char) 0);
            }
        }
    }

    private void setPlaceholderActive(boolean active) {
        component.putClientProperty("placeholderActive", active);
    }
}

