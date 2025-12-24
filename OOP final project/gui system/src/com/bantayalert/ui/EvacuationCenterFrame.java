package com.bantayalert.ui;

import com.bantayalert.data.UserProfile;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Screen showing evacuation centers with category filters.
 */
public class EvacuationCenterFrame extends JFrame {

    private final UserProfile user;
    private JPanel contentArea;
    private static final Color LIGHT_BLUE = new Color(173, 216, 230); // Light blue color for header/sidebar

    public EvacuationCenterFrame(UserProfile user) {
        super("Bantay Alert - Evacuation Center");
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

        JLabel dashboardLabel = new JLabel("Evacuation Center", SwingConstants.LEFT);
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
            refreshContentArea();
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

        sidebar.add(createSidebarItem("⚠", "Emergency Alerts", false,
            e -> new EmergencyAlertFrame(user).setVisible(true)));
        sidebar.add(createSidebarItem("☎", "Emergency Hotline", false,
            e -> new EmergencyHotlineFrame(user).setVisible(true)));
        sidebar.add(createSidebarItem("📷", "Report Incident", false,
            e -> new ReportIncidentFrame(user).setVisible(true)));
        sidebar.add(createSidebarItem("🚪", "Evacuation Center", true,
            e -> {}));
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
                    if (!text.equals("Evacuation Center")) {
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

        content.add(buildCategoryButtons(), BorderLayout.NORTH);
        content.add(buildContentArea(), BorderLayout.CENTER);

        return content;
    }

    private JPanel buildCategoryButtons() {
        JPanel container = new JPanel();
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        // Single row, three equal-width columns
        container.setLayout(new GridLayout(1, 3, 40, 0));

        container.add(createCategoryButton("≡", "All"));
        container.add(createCategoryButton("🚪", "Evacuation"));
        container.add(createCategoryButton("📣", "Announcement"));

        return container;
    }

    private JButton createCategoryButton(String icon, String text) {
        JButton button = new JButton();
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, false));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(280, 90));

        // Inner panel with GridLayout for icon + text alignment
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

        button.setLayout(new BorderLayout());
        button.add(inner, BorderLayout.CENTER);

        return button;
    }

    private JPanel buildContentArea() {
        contentArea = new JPanel();
        contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.Y_AXIS));
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 40, 20),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false)
        ));

        displayCenters();
        
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(contentArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        
        return wrapper;
    }

    /**
     * Displays evacuation centers in the content area.
     */
    private void displayCenters() {
        contentArea.removeAll();
        
        java.util.List<com.bantayalert.data.EvacuationCenter> centers = 
            com.bantayalert.data.EvacuationCenterRepository.findAll();
        
        if (centers.isEmpty()) {
            JLabel placeholder = new JLabel("No evacuation centers available yet.", SwingConstants.CENTER);
            placeholder.setFont(new Font("SansSerif", Font.PLAIN, 18));
            placeholder.setForeground(new Color(120, 120, 120));
            placeholder.setAlignmentX(0.5f);
            contentArea.add(Box.createVerticalGlue());
            contentArea.add(placeholder);
            contentArea.add(Box.createVerticalGlue());
        } else {
            for (com.bantayalert.data.EvacuationCenter center : centers) {
                contentArea.add(createCenterCard(center));
                contentArea.add(Box.createVerticalStrut(15));
            }
        }
        
        contentArea.revalidate();
        contentArea.repaint();
    }

    /**
     * Creates a card component for displaying an evacuation center.
     */
    private JPanel createCenterCard(com.bantayalert.data.EvacuationCenter center) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Left side: Center content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Name with status indicator
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(center.getName(), SwingConstants.LEFT);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setForeground(Color.BLACK);
        
        Color statusColor = center.getStatus().equals("OPEN") ? 
            new Color(40, 167, 69) : new Color(255, 193, 7);
        JLabel statusLabel = new JLabel(center.getStatus(), SwingConstants.RIGHT);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusLabel.setForeground(statusColor);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        titlePanel.add(nameLabel, BorderLayout.WEST);
        titlePanel.add(statusLabel, BorderLayout.EAST);
        contentPanel.add(titlePanel);
        contentPanel.add(Box.createVerticalStrut(10));

        // Address
        JLabel addressLabel = new JLabel("📍 " + center.getAddress(), SwingConstants.LEFT);
        addressLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        addressLabel.setForeground(Color.BLACK);
        addressLabel.setAlignmentX(0.0f);
        contentPanel.add(addressLabel);
        contentPanel.add(Box.createVerticalStrut(5));

        // Barangay
        JLabel barangayLabel = new JLabel("🏘️ Barangay: " + center.getBarangay(), SwingConstants.LEFT);
        barangayLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        barangayLabel.setForeground(new Color(100, 100, 100));
        barangayLabel.setAlignmentX(0.0f);
        contentPanel.add(barangayLabel);
        contentPanel.add(Box.createVerticalStrut(5));

        // Capacity
        JLabel capacityLabel = new JLabel("👥 Capacity: " + center.getCurrentEvacuees() + "/" + center.getCapacity(), SwingConstants.LEFT);
        capacityLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        capacityLabel.setForeground(new Color(100, 100, 100));
        capacityLabel.setAlignmentX(0.0f);
        contentPanel.add(capacityLabel);
        contentPanel.add(Box.createVerticalStrut(5));

        // Facilities
        if (center.getFacilities() != null && !center.getFacilities().trim().isEmpty()) {
            JLabel facilitiesLabel = new JLabel("🏢 Facilities: " + center.getFacilities(), SwingConstants.LEFT);
            facilitiesLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            facilitiesLabel.setForeground(new Color(100, 100, 100));
            facilitiesLabel.setAlignmentX(0.0f);
            contentPanel.add(facilitiesLabel);
            contentPanel.add(Box.createVerticalStrut(5));
        }

        // Contact
        JLabel contactLabel = new JLabel("📞 " + center.getContactPerson() + " - " + center.getContactNumber(), SwingConstants.LEFT);
        contactLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        contactLabel.setForeground(new Color(100, 100, 100));
        contactLabel.setAlignmentX(0.0f);
        contactLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        contentPanel.add(contactLabel);

        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Refreshes the content area by removing all components and rebuilding.
     */
    private void refreshContentArea() {
        displayCenters();
    }
}
