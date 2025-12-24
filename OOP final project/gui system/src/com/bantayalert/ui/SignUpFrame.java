package com.bantayalert.ui;

import com.bantayalert.data.UserProfile;
import com.bantayalert.data.UserRepository;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
 * Sign-up screen with barangay selector.
 */
public class SignUpFrame extends JFrame {

    private static final String[] MATI_BARANGAYS = {
        "Badas", "Bobon", "Buso", "Ca Buaya", "Central (City Proper/Poblacion)",
        "Culian", "Dahican", "Danao", "Dawan", "Don Enrique Lopez",
        "Don Martin Marundan", "Don Salvador Lopez, Sr.", "Langka", "Lawigan",
        "Libudon", "Luban", "Macambol", "Mamali", "Matiao", "Mayo", "Sainz",
        "Sanghay", "Tagabakid", "Tagbinonga", "Taguibo", "Tamisan"
    };

    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JComboBox<String> barangayCombo = new JComboBox<>(MATI_BARANGAYS);
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();
    private final JLabel messageLabel = new JLabel(" ", SwingConstants.CENTER);

    public SignUpFrame() {
        super("Create Account - Bantay Alert");
        buildUi();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
    }

    private void buildUi() {
        // Gradient background behind the white registration card
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

        JLabel title = new JLabel("Register", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        addRow(formPanel, gbc, row++, title);

        // Order should follow: Full Name, Email, Phone, Location, Password, Confirm Password
        addRow(formPanel, gbc, row++, createInputField("👤", nameField, "Full Name"));
        addRow(formPanel, gbc, row++, createInputField("✉", emailField, "Email Address"));
        addRow(formPanel, gbc, row++, createInputField("☎", phoneField, "Phone"));
        addRow(formPanel, gbc, row++, createComboField()); // Location / Barangay
        addRow(formPanel, gbc, row++, createPasswordField("🔒", passwordField, "Password"));
        addRow(formPanel, gbc, row++, createPasswordField("🔒", confirmPasswordField, "Confirm password"));

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setFocusPainted(false);
        createAccountButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        createAccountButton.setPreferredSize(new Dimension(230, 48));
        createAccountButton.setBackground(new Color(125, 206, 235));
        createAccountButton.setBorder(new LineBorder(new Color(41, 128, 185), 1));
        createAccountButton.addActionListener(e -> handleCreateAccount());
        addRow(formPanel, gbc, row++, createAccountButton);

        JLabel footer = new JLabel("Back to Login", SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 16));
        footer.setForeground(new Color(0x1B4F72));
        footer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        footer.addMouseListener(new SimpleLinkMouseListener(() -> {
            dispose();
            new LoginFrame().setVisible(true);
        }));
        addRow(formPanel, gbc, row, footer);

        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(0xC0392B));
        messageLabel.setBorder(new InsetsBorder(new Insets(10, 0, 10, 0)));

        // White rounded card like mockup
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(0xCCCCCC), 2, true));
        card.setPreferredSize(new Dimension(420, 580));

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

    private JPanel createInputField(String icon, JTextField field, String placeholder) {
        JPanel wrapper = createBorderedWrapper();
        field.setBorder(null);
        field.setFont(new Font("SansSerif", Font.PLAIN, 18));
        field.setForeground(Color.BLACK);
        field.addFocusListener(new PlaceholderFocusListener(field, placeholder));
        field.setText(placeholder);

        wrapper.add(createIconLabel(icon), BorderLayout.WEST);
        wrapper.add(field, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createPasswordField(String icon, JPasswordField field, String placeholder) {
        JPanel wrapper = createBorderedWrapper();
        field.setBorder(null);
        field.setFont(new Font("SansSerif", Font.PLAIN, 18));
        field.setForeground(Color.BLACK);
        field.setText(placeholder);
        field.setEchoChar((char) 0);
        field.addFocusListener(new PlaceholderFocusListener(field, placeholder));

        wrapper.add(createIconLabel(icon), BorderLayout.WEST);
        wrapper.add(field, BorderLayout.CENTER);

        PasswordVisibilityToggle toggle = new PasswordVisibilityToggle(field);
        toggle.setPreferredSize(new Dimension(50, 60));
        wrapper.add(toggle, BorderLayout.EAST);
        return wrapper;
    }

    private JPanel createComboField() {
        JPanel wrapper = createBorderedWrapper();

        barangayCombo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        barangayCombo.setBorder(null);
        barangayCombo.setBackground(Color.WHITE);
        barangayCombo.setForeground(Color.BLACK);

        wrapper.add(createIconLabel("📍"), BorderLayout.WEST);
        wrapper.add(barangayCombo, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createBorderedWrapper() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new LineBorder(new Color(41, 128, 185), 2));
        wrapper.setPreferredSize(new Dimension(360, 65));
        return wrapper;
    }

    private JLabel createIconLabel(String icon) {
        JLabel label = new JLabel(icon, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 26));
        label.setPreferredSize(new Dimension(70, 60));
        return label;
    }

    private void handleCreateAccount() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
            || "Full Name".equals(name) || "Email Address".equals(email)
            || "Phone".equals(phone) || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setForeground(new Color(0xC0392B));
            messageLabel.setText("Please fill out all fields.");
            return;
        }

        // Validate that email is Gmail only
        if (!email.toLowerCase().endsWith("@gmail.com")) {
            messageLabel.setForeground(new Color(0xC0392B));
            messageLabel.setText("Only Gmail addresses are allowed.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setForeground(new Color(0xC0392B));
            messageLabel.setText("Passwords do not match.");
            return;
        }

        String location = (String) barangayCombo.getSelectedItem();

        String status = "citizen";
        String memberSince = java.time.LocalDate.now().toString();

        UserProfile profile = new UserProfile(name, email, phone, location, password, status, memberSince);
        UserRepository.save(profile);

        // Automatically go back to login after successful account creation
        dispose();
        new LoginFrame().setVisible(true);
    }
}

