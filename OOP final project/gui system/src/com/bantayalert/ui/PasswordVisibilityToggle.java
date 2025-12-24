package com.bantayalert.ui;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Small button that toggles password visibility.
 */
class PasswordVisibilityToggle extends JButton implements ActionListener {
    private final JPasswordField passwordField;
    private boolean revealing;

    PasswordVisibilityToggle(JPasswordField passwordField) {
        super("👁");
        this.passwordField = passwordField;
        setBorder(null);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setToolTipText("Show/Hide password");
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPlaceholderActive()) {
            passwordField.setEchoChar((char) 0);
            return;
        }
        revealing = !revealing;
        passwordField.setEchoChar(revealing ? (char) 0 : '•');
    }

    private boolean isPlaceholderActive() {
        Object flag = passwordField.getClientProperty("placeholderActive");
        return flag instanceof Boolean bool && bool;
    }
}

