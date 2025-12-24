package com.bantayalert.ui;

import com.bantayalert.data.UserRepository;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

/**
 * Login frame inspired by the supplied mock-up.
 */
public class LoginFrame extends JFrame {

    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JLabel messageLabel = new JLabel(" ", SwingConstants.CENTER);

    public LoginFrame() {
        super("Bantay Alert Mati City");
        buildUi();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
    }

    private void buildUi() {
        // Gradient background like mockup
        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color top = new Color(196, 234, 248);
                Color bottom = new Color(73, 163, 199);
                GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setOpaque(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        int row = 0;

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        addRow(formPanel, gbc, row++, title);

        JPanel emailFieldPanel = createInputField("✉", emailField, "Email Address");
        addRow(formPanel, gbc, row++, emailFieldPanel);

        JPanel passwordFieldPanel = createInputField("🔒", passwordField, "Password");
        addRow(formPanel, gbc, row++, passwordFieldPanel);

        JButton signInButton = new JButton("Login");
        signInButton.setFocusPainted(false);
        signInButton.setPreferredSize(new Dimension(230, 48));
        signInButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        signInButton.setBackground(new Color(125, 206, 235));
        signInButton.setBorder(new LineBorder(new Color(41, 128, 185), 1));
        signInButton.addActionListener(e -> handleSignIn());
        addRow(formPanel, gbc, row++, signInButton);

        JLabel footer = new JLabel("Don't have an account? Sign Up", SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 16));
        footer.setForeground(new Color(0x1B4F72));
        footer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        footer.addMouseListener(new SimpleLinkMouseListener(() -> {
            dispose();
            new SignUpFrame().setVisible(true);
        }));
        footer.setFont(new Font("SansSerif", Font.PLAIN, 16));
        addRow(formPanel, gbc, row, footer);

        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(0xD35400));
        messageLabel.setBorder(new InsetsBorder(new Insets(10, 0, 10, 0)));

        // White card centered on gradient background
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(0xCCCCCC), 2, true));
        card.setPreferredSize(new Dimension(420, 520));

        card.add(formPanel, BorderLayout.CENTER);
        card.add(messageLabel, BorderLayout.SOUTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(card);

        root.add(centerWrapper, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, java.awt.Component component) {
        gbc.gridy = row;
        panel.add(component, gbc);
    }

    private JPanel createInputField(String iconText, javax.swing.JComponent field, String placeholder) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new LineBorder(new Color(41, 128, 185), 2));

        JLabel iconLabel = new JLabel(iconText, SwingConstants.CENTER);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        iconLabel.setPreferredSize(new Dimension(60, 60));

        field.setBorder(null);
        field.setFont(new Font("SansSerif", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(300, 60));

        if (field instanceof JTextField textField) {
            textField.setText(placeholder);
            textField.addFocusListener(new PlaceholderFocusListener(textField, placeholder));
        } else if (field instanceof JPasswordField password) {
            password.setEchoChar((char) 0);
            password.setText(placeholder);
            password.addFocusListener(new PlaceholderFocusListener(password, placeholder));
            PasswordVisibilityToggle toggle = new PasswordVisibilityToggle(password);
            toggle.setPreferredSize(new Dimension(50, 60));
            wrapper.add(toggle, BorderLayout.EAST);
        }

        wrapper.add(iconLabel, BorderLayout.WEST);
        wrapper.add(field, BorderLayout.CENTER);
        return wrapper;
    }

    private void handleSignIn() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()
            || "Email Address".equals(email) || "Password".equals(password)) {
            messageLabel.setText("Please enter your email and password.");
            return;
        }

        UserRepository.authenticate(email, password).ifPresentOrElse(profile -> {
            messageLabel.setForeground(new Color(0x27AE60));
            messageLabel.setText("Welcome back, " + profile.getFullName() + "!");
            dispose();
            // Check if user is admin
            if ("admin".equals(profile.getCommunityStatus())) {
                new AdminDashboardFrame(profile).setVisible(true);
            } else {
                new EmergencyAlertFrame(profile).setVisible(true);
            }
        }, () -> {
            messageLabel.setForeground(new Color(0xC0392B));
            messageLabel.setText("Invalid credentials. Please try again.");
        });
    }
}

