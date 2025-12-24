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
import java.awt.GridLayout;

/**
 * Screen showing emergency alerts with category filters.
 */
public class EmergencyAlertFrame extends JFrame {

    private final UserProfile user;
    private JPanel alertArea;
    private String currentCategory = "All Alerts"; // Track current selected category
    private static final Color LIGHT_BLUE = new Color(173, 216, 230); // Light blue color for header/sidebar
    // Alerts will be populated from admin dashboard in future

    public EmergencyAlertFrame(UserProfile user) {
        super("Bantay Alert - Emergency Alerts");
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
            DashboardFrame.getOrCreateInstance(user).setVisible(true);
        });

        JLabel dashboardLabel = new JLabel("Emergency Alert", SwingConstants.LEFT);
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

        JButton refreshButton = new JButton("⟳");
        refreshButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setFocusPainted(false);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        refreshButton.addActionListener(e -> {
            refreshAlertArea();
        });

        JLabel profileIcon = new JLabel("👤", SwingConstants.CENTER);
        profileIcon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        profileIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        profileIcon.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        profileIcon.addMouseListener(new com.bantayalert.ui.SimpleLinkMouseListener(() -> {
            new ProfileFrame(user).setVisible(true);
        }));

        rightPanel.add(searchField);
        rightPanel.add(searchIcon);
        rightPanel.add(refreshButton);
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

        sidebar.add(createSidebarItem("⚠", "Emergency Alerts", true,
            e -> {}));
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
                    if (!text.equals("Emergency Alerts")) {
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
        content.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        content.add(buildCategoryBar(), BorderLayout.NORTH);
        alertArea = buildAlertArea();
        content.add(alertArea, BorderLayout.CENTER);

        return content;
    }

    private JPanel buildCategoryBar() {
        JPanel container = new JPanel();
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        // Single row, three equal-width columns
        container.setLayout(new GridLayout(1, 3, 40, 0));

        JButton allAlertsBtn = createCategoryCard("≡", "All Alerts");
        allAlertsBtn.addActionListener(e -> filterAlerts("All Alerts"));
        
        JButton disastersBtn = createCategoryCard("⛈", "Disasters");
        disastersBtn.addActionListener(e -> filterAlerts("Disasters"));
        
        JButton announcementBtn = createCategoryCard("📣", "Announcement");
        announcementBtn.addActionListener(e -> filterAlerts("Announcement"));

        container.add(allAlertsBtn);
        container.add(disastersBtn);
        container.add(announcementBtn);

        return container;
    }

    private JButton createCategoryCard(String icon, String text) {
        JButton card = new JButton();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, false));
        card.setFocusPainted(false);
        card.setPreferredSize(new Dimension(280, 90));

        // icon + label aligned horizontally using GridLayout
        JPanel inner = new JPanel(new GridLayout(1, 2, 16, 0));
        inner.setBackground(Color.WHITE);
        inner.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 38));
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);

        JLabel textLabel = new JLabel(text, SwingConstants.LEFT);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 22));
        textLabel.setVerticalAlignment(SwingConstants.CENTER);

        inner.add(iconLabel);
        inner.add(textLabel);

        card.setLayout(new BorderLayout());
        card.add(inner, BorderLayout.CENTER);

        return card;
    }

    /**
     * Placeholder panel where emergency alerts / announcements
     * from the admin will be displayed.
     */
    private JPanel buildAlertArea() {
        JPanel area = new JPanel();
        area.setLayout(new BoxLayout(area, BoxLayout.Y_AXIS));
        area.setBackground(Color.WHITE);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false)
        ));

        displayAlerts(area, "All Alerts");
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        
        return wrapper;
    }

    /**
     * Refreshes the alert area by removing all components and rebuilding.
     */
    private void refreshAlertArea() {
        JScrollPane scrollPane = (JScrollPane) alertArea.getComponent(0);
        JPanel scrollViewport = (JPanel) scrollPane.getViewport().getView();
        scrollViewport.removeAll();
        displayAlerts(scrollViewport, currentCategory);
        scrollViewport.revalidate();
        scrollViewport.repaint();
    }

    /**
     * Filters and displays alerts based on the selected category.
     */
    private void filterAlerts(String category) {
        currentCategory = category;
        JScrollPane scrollPane = (JScrollPane) alertArea.getComponent(0);
        JPanel scrollViewport = (JPanel) scrollPane.getViewport().getView();
        scrollViewport.removeAll();
        displayAlerts(scrollViewport, category);
        scrollViewport.revalidate();
        scrollViewport.repaint();
    }

    /**
     * Displays alerts in the given panel based on category filter.
     */
    private void displayAlerts(JPanel panel, String category) {
        java.util.List<com.bantayalert.data.Alert> alerts;
        
        if ("All Alerts".equals(category)) {
            alerts = com.bantayalert.data.AlertRepository.findActive();
        } else if ("Disasters".equals(category)) {
            alerts = com.bantayalert.data.AlertRepository.findByType("disaster")
                .stream()
                .filter(a -> "ACTIVE".equals(a.getStatus()))
                .collect(java.util.stream.Collectors.toList());
        } else if ("Announcement".equals(category)) {
            alerts = com.bantayalert.data.AlertRepository.findByType("announcement")
                .stream()
                .filter(a -> "ACTIVE".equals(a.getStatus()))
                .collect(java.util.stream.Collectors.toList());
        } else {
            alerts = java.util.Collections.emptyList();
        }

        if (alerts.isEmpty()) {
            JLabel placeholder = new JLabel("No " + category.toLowerCase() + " yet.", SwingConstants.CENTER);
            placeholder.setFont(new Font("SansSerif", Font.PLAIN, 18));
            placeholder.setForeground(new Color(120, 120, 120));
            placeholder.setAlignmentX(0.5f);
            panel.add(Box.createVerticalGlue());
            panel.add(placeholder);
            panel.add(Box.createVerticalGlue());
        } else {
            for (com.bantayalert.data.Alert alert : alerts) {
                panel.add(createAlertCard(alert));
                panel.add(Box.createVerticalStrut(15));
            }
        }
    }

    /**
     * Creates a card component for displaying an alert.
     */
    private JPanel createAlertCard(com.bantayalert.data.Alert alert) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Determine color based on severity
        Color severityColor;
        switch (alert.getSeverity().toLowerCase()) {
            case "critical":
                severityColor = new Color(220, 53, 69); // Red
                break;
            case "high":
                severityColor = new Color(255, 193, 7); // Yellow
                break;
            case "medium":
                severityColor = new Color(255, 152, 0); // Orange
                break;
            default:
                severityColor = new Color(40, 167, 69); // Green
        }

        // Left side: Alert content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Title with severity indicator
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(alert.getTitle(), SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLACK);
        
        JLabel severityLabel = new JLabel(alert.getSeverity().toUpperCase(), SwingConstants.RIGHT);
        severityLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        severityLabel.setForeground(severityColor);
        severityLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(severityLabel, BorderLayout.EAST);
        contentPanel.add(titlePanel);
        contentPanel.add(Box.createVerticalStrut(10));

        // Message
        JLabel messageLabel = new JLabel("<html><body style='width: 600px'>" + alert.getMessage() + "</body></html>", SwingConstants.LEFT);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setAlignmentX(0.0f);
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        // Affected areas
        if (alert.getAffectedBarangays() != null && !alert.getAffectedBarangays().trim().isEmpty()) {
            JLabel areasLabel = new JLabel("📍 Affected Areas: " + alert.getAffectedBarangays(), SwingConstants.LEFT);
            areasLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            areasLabel.setForeground(new Color(100, 100, 100));
            areasLabel.setAlignmentX(0.0f);
            contentPanel.add(areasLabel);
            contentPanel.add(Box.createVerticalStrut(5));
        }

        // Date
        JLabel dateLabel = new JLabel("📅 " + alert.getDateCreated(), SwingConstants.LEFT);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(120, 120, 120));
        dateLabel.setAlignmentX(0.0f);
        contentPanel.add(dateLabel);

        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

}
