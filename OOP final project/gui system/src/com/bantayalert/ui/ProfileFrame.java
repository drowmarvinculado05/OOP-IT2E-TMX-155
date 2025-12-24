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
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Profile / account screen showing user info and settings.
 */
public class ProfileFrame extends JFrame {

    private final UserProfile user;
    private static final Color LIGHT_BLUE = new Color(173, 216, 230); // Light blue color for header/sidebar

    public ProfileFrame(UserProfile user) {
        super("Bantay Alert - Profile");
        // Always get the latest profile from repository to ensure we show updated data
        this.user = com.bantayalert.data.UserRepository.findByEmail(user.getEmail())
            .orElse(user);
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
            DashboardFrame.getOrCreateInstance(user).setVisible(true);
        });

        JLabel dashboardLabel = new JLabel("Profile", SwingConstants.LEFT);
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
        sidebar.add(createSidebarItem("👤", "Profile", true,
            e -> {}));

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
                    if (!text.equals("Profile")) {
                        dispose();
                    }
                }
            });
        }

        return item;
    }

    private JPanel buildMainContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));

        // Scrollable content
        JPanel scrollContent = new JPanel();
        scrollContent.setBackground(Color.WHITE);
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));

        scrollContent.add(buildUserHeader());
        scrollContent.add(Box.createVerticalStrut(20));

        // Side-by-side layout (Account Information and Settings)
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.X_AXIS));
        centerWrapper.setBackground(Color.WHITE);

        JPanel mainSections = buildMainSections();
        mainSections.setMaximumSize(new Dimension(860, Integer.MAX_VALUE));

        centerWrapper.add(Box.createHorizontalGlue());
        centerWrapper.add(mainSections);
        centerWrapper.add(Box.createHorizontalGlue());

        scrollContent.add(centerWrapper);
        scrollContent.add(Box.createVerticalStrut(30));
        scrollContent.add(buildLogoutButton());
        scrollContent.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        content.add(scrollPane, BorderLayout.CENTER);
        return content;
    }

    private JPanel buildUserHeader() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(Color.WHITE);
        inner.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Avatar circle placeholder with person icon
        JLabel avatar = new JLabel("👤", SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(80, 80));
        avatar.setOpaque(true);
        avatar.setBackground(new Color(230, 230, 230));
        avatar.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 2, false));
        avatar.setFont(new Font("SansSerif", Font.PLAIN, 40));

        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.WHITE);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));

        JLabel nameLabel = new JLabel(user.getFullName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel emailLabel = new JLabel(user.getEmail());
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        emailLabel.setForeground(new Color(90, 90, 90));

        JLabel statusLabel = new JLabel(user.getCommunityStatus());
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        statusLabel.setForeground(new Color(90, 90, 90));

        textPanel.add(nameLabel);
        textPanel.add(emailLabel);
        textPanel.add(statusLabel);

        inner.add(avatar, BorderLayout.WEST);
        inner.add(textPanel, BorderLayout.CENTER);

        // Horizontal line below profile section
        JLabel line = new JLabel();
        line.setOpaque(true);
        line.setBackground(new Color(220, 220, 220));
        line.setPreferredSize(new Dimension(0, 2));

        container.add(inner, BorderLayout.CENTER);
        container.add(line, BorderLayout.SOUTH);

        return container;
    }

    private JPanel buildMainSections() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.setBackground(Color.WHITE);

        container.add(buildAccountInfo());
        container.add(Box.createHorizontalStrut(40));
        container.add(buildSettings());

        return container;
    }

    private JPanel buildAccountInfo() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel titleWrapper = new JPanel();
        titleWrapper.setLayout(new BoxLayout(titleWrapper, BoxLayout.X_AXIS));
        titleWrapper.setBackground(Color.WHITE);
        titleWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        titleWrapper.add(Box.createHorizontalGlue());
        
        JLabel title = new JLabel("Account information", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setAlignmentX(0.5f);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        titleWrapper.add(title);
        titleWrapper.add(Box.createHorizontalGlue());

        panel.add(titleWrapper);
        panel.add(Box.createVerticalStrut(10));

        // Center the boxes below the title
        JPanel boxesWrapper = new JPanel();
        boxesWrapper.setLayout(new BoxLayout(boxesWrapper, BoxLayout.Y_AXIS));
        boxesWrapper.setBackground(Color.WHITE);
        boxesWrapper.setAlignmentX(0.5f); // Center horizontally
        
        boxesWrapper.add(createInfoRow("☎", "Contact Number", user.getPhone()));
        boxesWrapper.add(Box.createVerticalStrut(12));
        boxesWrapper.add(createInfoRow("📍", "Barangay", user.getLocation()));
        boxesWrapper.add(Box.createVerticalStrut(12));
        boxesWrapper.add(createInfoRow("🪪", "Member since", user.getMemberSince()));
        
        panel.add(Box.createHorizontalGlue());
        panel.add(boxesWrapper);
        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    private JPanel createInfoRow(String icon, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, false)); // Gridline border
        card.setPreferredSize(new Dimension(340, 75));
        card.setMaximumSize(new Dimension(340, 75));
        card.setMinimumSize(new Dimension(340, 75));

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        // Icon panel with consistent size
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setPreferredSize(new Dimension(65, 75));
        iconPanel.setMaximumSize(new Dimension(65, 75));
        iconPanel.setMinimumSize(new Dimension(65, 75));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 36));
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconPanel.add(iconLabel, BorderLayout.CENTER);

        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.WHITE);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel labelLabel = new JLabel(label, SwingConstants.LEFT);
        labelLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        labelLabel.setAlignmentX(0.0f);
        labelLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel valueLabel = new JLabel(value != null ? value : "-", SwingConstants.LEFT);
        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        valueLabel.setAlignmentX(0.0f);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);

        textPanel.add(Box.createVerticalGlue());
        textPanel.add(labelLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(valueLabel);
        textPanel.add(Box.createVerticalGlue());

        content.add(iconPanel, BorderLayout.WEST);
        content.add(textPanel, BorderLayout.CENTER);

        card.add(content, BorderLayout.CENTER);

        JPanel alignWrapper = new JPanel();
        alignWrapper.setBackground(Color.WHITE);
        alignWrapper.setLayout(new BorderLayout());
        alignWrapper.add(card, BorderLayout.WEST);

        row.add(alignWrapper, BorderLayout.CENTER);

        return row;
    }

    private JPanel buildSettings() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel titleWrapper = new JPanel();
        titleWrapper.setLayout(new BoxLayout(titleWrapper, BoxLayout.X_AXIS));
        titleWrapper.setBackground(Color.WHITE);
        titleWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        titleWrapper.add(Box.createHorizontalGlue());
        
        JLabel title = new JLabel("Settings", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setAlignmentX(0.5f);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        titleWrapper.add(title);
        titleWrapper.add(Box.createHorizontalGlue());

        panel.add(titleWrapper);
        panel.add(Box.createVerticalStrut(10));

        // Center the boxes below the title
        JPanel boxesWrapper = new JPanel();
        boxesWrapper.setLayout(new BoxLayout(boxesWrapper, BoxLayout.Y_AXIS));
        boxesWrapper.setBackground(Color.WHITE);
        boxesWrapper.setAlignmentX(0.5f); // Center horizontally
        
        boxesWrapper.add(createSettingsRow("👤", "Edit Profile", "Update your personal information", 
            () -> new EditProfileFrame(user).setVisible(true)));
        boxesWrapper.add(Box.createVerticalStrut(12));
        boxesWrapper.add(createSettingsRow("🔔", "Notification & Settings", "Manage alert properties", 
            () -> new NotificationSettingsFrame(user).setVisible(true)));
        boxesWrapper.add(Box.createVerticalStrut(12));
        boxesWrapper.add(createSettingsRow("🔒", "Privacy & Security", "Manage your privacy settings", 
            () -> new PrivacySecurityFrame(user).setVisible(true)));
        boxesWrapper.add(Box.createVerticalStrut(12));
        boxesWrapper.add(createSettingsRow("❓", "Help & Support", "Get help and contact", 
            () -> new HelpSupportFrame(user).setVisible(true)));
        boxesWrapper.add(Box.createVerticalStrut(12));
        boxesWrapper.add(createSettingsRow("ℹ", "About BantayAlert", "Learn more about BantayAlert", 
            () -> new AboutBantayAlertFrame(user).setVisible(true)));
        
        panel.add(Box.createHorizontalGlue());
        panel.add(boxesWrapper);
        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    private JPanel createSettingsRow(String icon, String title, String subtitle, Runnable onClick) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, false)); // Gridline border
        card.setPreferredSize(new Dimension(340, 75));
        card.setMaximumSize(new Dimension(340, 75));
        card.setMinimumSize(new Dimension(340, 75));
        
        if (onClick != null) {
            card.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    onClick.run();
                }
            });
        }

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        // Icon panel with consistent size (same as info rows)
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setPreferredSize(new Dimension(65, 75));
        iconPanel.setMaximumSize(new Dimension(65, 75));
        iconPanel.setMinimumSize(new Dimension(65, 75));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 36));
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconPanel.add(iconLabel, BorderLayout.CENTER);

        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.WHITE);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel titleLabel = new JLabel(title, SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setAlignmentX(0.0f);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel subtitleLabel = new JLabel(subtitle, SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setAlignmentX(0.0f);
        subtitleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        textPanel.add(Box.createVerticalGlue());
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(subtitleLabel);
        textPanel.add(Box.createVerticalGlue());

        content.add(iconPanel, BorderLayout.WEST);
        content.add(textPanel, BorderLayout.CENTER);

        card.add(content, BorderLayout.CENTER);

        JPanel alignWrapper = new JPanel();
        alignWrapper.setBackground(Color.WHITE);
        alignWrapper.setLayout(new BorderLayout());
        alignWrapper.add(card, BorderLayout.WEST);

        row.add(alignWrapper, BorderLayout.CENTER);

        return row;
    }

    private JPanel buildLogoutButton() {
        JPanel container = new JPanel();
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, false));
        logoutButton.setFocusPainted(false);
        logoutButton.setPreferredSize(new Dimension(220, 55));
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.add(Box.createHorizontalGlue());
        container.add(logoutButton);
        container.add(Box.createHorizontalGlue());

        return container;
    }
}
