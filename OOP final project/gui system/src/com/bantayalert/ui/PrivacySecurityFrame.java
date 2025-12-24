package com.bantayalert.ui;

import com.bantayalert.data.UserProfile;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Privacy & Security screen for managing privacy settings.
 */
public class PrivacySecurityFrame extends JFrame {

    private final UserProfile user;
    private JToggleButton shareLocation;
    private JToggleButton showNameOnReports;
    private static final Color LIGHT_BLUE = new Color(173, 216, 230); // Light blue color for header/sidebar
    private static final Color GREEN_BUTTON = new Color(76, 175, 80); // Green color for Save button

    public PrivacySecurityFrame(UserProfile user) {
        super("Bantay Alert - Privacy & Security");
        this.user = user;
        buildUi();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
    }

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        root.add(buildTopHeader(), BorderLayout.NORTH);
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMainContent(), BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel buildTopHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(LIGHT_BLUE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Left side: Back button + Title
        JPanel leftPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(LIGHT_BLUE);
        leftPanel.setOpaque(false);

        JButton backButton = new JButton("←");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        backButton.addActionListener(e -> {
            dispose();
            new ProfileFrame(user).setVisible(true);
        });

        JLabel dashboardLabel = new JLabel("Privacy & Security", SwingConstants.LEFT);
        dashboardLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        dashboardLabel.setForeground(Color.BLACK);

        leftPanel.add(backButton);
        leftPanel.add(dashboardLabel);

        // Right side: Search field + icons
        JPanel rightPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(LIGHT_BLUE);
        rightPanel.setOpaque(false);

        JTextField searchField = new JTextField("search....");
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setForeground(Color.GRAY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 35)
        ));
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.addFocusListener(new com.bantayalert.ui.PlaceholderFocusListener(
            searchField, "search...."
        ));

        JLabel searchIcon = new JLabel("🔍", SwingConstants.CENTER);
        searchIcon.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JLabel profileIcon = new JLabel("👤", SwingConstants.CENTER);
        profileIcon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        profileIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        profileIcon.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        profileIcon.addMouseListener(new com.bantayalert.ui.SimpleLinkMouseListener(() -> {
            new ProfileFrame(user).setVisible(true);
        }));

        rightPanel.add(searchField);
        rightPanel.add(searchIcon);
        rightPanel.add(profileIcon);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(LIGHT_BLUE);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.setPreferredSize(new Dimension(240, 0));

        // Quick Actions section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(LIGHT_BLUE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 20, 10));
        
        JLabel sectionTitle = new JLabel("QUICK ACTIONS");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        sectionTitle.setForeground(Color.BLACK);
        sectionTitle.setHorizontalAlignment(SwingConstants.LEFT);
        sectionTitle.setVerticalAlignment(SwingConstants.CENTER);
        
        titlePanel.add(sectionTitle, BorderLayout.WEST);
        sidebar.add(titlePanel);

        sidebar.add(createSidebarItem("⚠", "Emergency Alerts", false,
            e -> new EmergencyAlertFrame(user).setVisible(true)));
        sidebar.add(createSidebarItem("☎", "Emergency Hotline", false,
            e -> new EmergencyHotlineFrame(user).setVisible(true)));
        sidebar.add(createSidebarItem("📷", "Report Incident", false,
            e -> new ReportIncidentFrame(user).setVisible(true)));
        sidebar.add(createSidebarItem("🚪", "Evacuation Center", false,
            e -> new EvacuationCenterFrame(user).setVisible(true)));
        sidebar.add(createSidebarItem("👤", "Profile", false,
            e -> new ProfileFrame(user).setVisible(true)));

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JPanel createSidebarItem(String icon, String text, boolean selected, 
                                     java.awt.event.ActionListener listener) {
        JPanel item = new JPanel(new BorderLayout());
        Color selectedColor = new Color(135, 206, 235); // Slightly darker blue for selected
        item.setBackground(selected ? selectedColor : LIGHT_BLUE);
        item.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 10));
        item.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        item.setPreferredSize(new Dimension(240, 50));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        content.setBackground(selected ? selectedColor : LIGHT_BLUE);
        content.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        iconLabel.setForeground(Color.BLACK);
        iconLabel.setAlignmentY(0.5f);

        content.add(iconLabel);
        content.add(Box.createHorizontalStrut(15));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        textLabel.setForeground(Color.BLACK);
        textLabel.setAlignmentY(0.5f);

        content.add(textLabel);
        content.add(Box.createHorizontalGlue());

        item.add(content, BorderLayout.CENTER);
        if (listener != null) {
            item.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    listener.actionPerformed(new java.awt.event.ActionEvent(item, 0, ""));
                    dispose();
                }
            });
        }

        return item;
    }

    private JPanel buildMainContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Scrollable content
        JPanel scrollContent = new JPanel();
        scrollContent.setBackground(Color.WHITE);
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));

        // Center wrapper
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        // Section title
        JLabel sectionTitle = new JLabel("Privacy & Security", SwingConstants.LEFT);
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        sectionTitle.setForeground(Color.BLACK);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        sectionTitle.setAlignmentX(0.0f);

        centerWrapper.add(sectionTitle);
        centerWrapper.add(Box.createVerticalStrut(10));

        // Privacy options
        centerWrapper.add(createPrivacyOption("Share Location (for reports)", true));
        centerWrapper.add(Box.createVerticalStrut(10)); // Reduced spacing

        centerWrapper.add(createPrivacyOption("Show Name on Reports", true));
        centerWrapper.add(Box.createVerticalGlue());

        // Save button with custom styling
        JButton saveButton = new JButton("Save") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw button background with rounded corners
                int width = getWidth();
                int height = getHeight();
                int arc = 8; // Rounded corner radius
                
                // Main green background
                g2.setColor(GREEN_BUTTON);
                g2.fillRoundRect(0, 0, width, height, arc, arc);
                
                // Subtle diagonal stripe texture
                g2.setColor(new Color(GREEN_BUTTON.getRed() - 10, GREEN_BUTTON.getGreen() - 10, GREEN_BUTTON.getBlue() - 10));
                for (int i = -height; i < width + height; i += 4) {
                    g2.drawLine(i, 0, i + height, height);
                }
                
                // Top highlight (lighter green)
                g2.setColor(new Color(GREEN_BUTTON.getRed() + 20, GREEN_BUTTON.getGreen() + 20, GREEN_BUTTON.getBlue() + 20));
                g2.setStroke(new java.awt.BasicStroke(1));
                g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);
                
                // Bottom shadow (darker green)
                g2.setColor(new Color(GREEN_BUTTON.getRed() - 20, GREEN_BUTTON.getGreen() - 20, GREEN_BUTTON.getBlue() - 20));
                g2.drawLine(2, height - 1, width - 2, height - 1);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        saveButton.setForeground(new Color(240, 240, 240)); // Light grey/off-white text
        saveButton.setOpaque(false); // Use custom painting
        saveButton.setFocusPainted(false);
        saveButton.setContentAreaFilled(false); // Use custom painting
        saveButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        saveButton.setPreferredSize(new Dimension(600, 50));
        saveButton.setMaximumSize(new Dimension(600, 50));
        saveButton.setAlignmentX(0.0f);
        saveButton.addActionListener(e -> saveSettings());

        centerWrapper.add(Box.createVerticalStrut(30));
        centerWrapper.add(saveButton);

        scrollContent.add(Box.createVerticalGlue());
        scrollContent.add(Box.createHorizontalGlue());
        scrollContent.add(centerWrapper);
        scrollContent.add(Box.createHorizontalGlue());
        scrollContent.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        content.add(scrollPane, BorderLayout.CENTER);
        return content;
    }

    private JPanel createPrivacyOption(String label, boolean initialState) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        container.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Reduced padding for closer buttons

        // Label
        JLabel labelComponent = new JLabel(label, SwingConstants.LEFT);
        labelComponent.setFont(new Font("SansSerif", Font.PLAIN, 18));
        labelComponent.setForeground(Color.BLACK);

        // Toggle switch (custom styled toggle button)
        JToggleButton toggle = new JToggleButton() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = 50;
                int height = 28;
                int radius = height / 2;
                int x = (getWidth() - width) / 2;
                int y = (getHeight() - height) / 2;
                
                // Draw track
                if (isSelected()) {
                    g2.setColor(GREEN_BUTTON);
                } else {
                    g2.setColor(new Color(200, 200, 200));
                }
                g2.fillRoundRect(x, y, width, height, radius, radius);
                
                // Draw circle
                int circleSize = height - 4;
                int circleX = isSelected() ? x + width - circleSize - 2 : x + 2;
                int circleY = y + 2;
                
                g2.setColor(Color.WHITE);
                g2.fillOval(circleX, circleY, circleSize, circleSize);
                
                g2.dispose();
            }
        };
        toggle.setSelected(initialState);
        toggle.setFocusPainted(false);
        toggle.setBackground(Color.WHITE);
        toggle.setPreferredSize(new Dimension(50, 30));
        toggle.setMaximumSize(new Dimension(50, 30));
        toggle.setBorderPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setOpaque(false);
        toggle.setText(""); // No text

        container.add(labelComponent, BorderLayout.WEST);
        container.add(toggle, BorderLayout.EAST);

        // Store reference
        if (label.equals("Share Location (for reports)")) {
            shareLocation = toggle;
        } else if (label.equals("Show Name on Reports")) {
            showNameOnReports = toggle;
        }

        return container;
    }

    private void saveSettings() {
        boolean shareLoc = shareLocation.isSelected();
        boolean showName = showNameOnReports.isSelected();

        // Show success message
        javax.swing.JOptionPane.showMessageDialog(this,
            "Privacy settings saved successfully!\n\n" +
            "Share Location (for reports): " + (shareLoc ? "On" : "Off") + "\n" +
            "Show Name on Reports: " + (showName ? "On" : "Off"),
            "Success",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);

        // Close and return to Profile screen
        dispose();
        new ProfileFrame(user).setVisible(true);
    }
}

