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
import java.util.ArrayList;
import java.util.List;

/**
 * Main dashboard shown after successful login.
 */
public class DashboardFrame extends JFrame {

    private final UserProfile user;
    private static final Color LIGHT_BLUE = new Color(173, 216, 230); // Light blue color for header/sidebar
    private JPanel sidebar;
    private JTextField searchField;
    private final List<SidebarItemData> sidebarItems = new ArrayList<>();
    private static DashboardFrame instance; // Static reference to the dashboard instance
    
    private static class SidebarItemData {
        String icon;
        String text;
        java.awt.event.ActionListener listener;
        
        SidebarItemData(String icon, String text, java.awt.event.ActionListener listener) {
            this.icon = icon;
            this.text = text;
            this.listener = listener;
        }
    }

    public DashboardFrame(UserProfile user) {
        super("Bantay Alert - Dashboard");
        this.user = user;
        buildUi();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
        
        // Disable maximize and minimize functionality
        setResizable(false);
        setExtendedState(JFrame.NORMAL); // Ensure window is in normal state
        
        // Store this instance
        instance = this;
    }
    
    /**
     * Get or create the dashboard instance. Reuses existing instance if available.
     */
    public static DashboardFrame getOrCreateInstance(UserProfile user) {
        if (instance != null && instance.isDisplayable()) {
            // Bring existing dashboard to front
            instance.toFront();
            instance.requestFocus();
            return instance;
        }
        // Create new instance if none exists or was disposed
        return new DashboardFrame(user);
    }
    
    /**
     * Clear the static instance when dashboard is disposed.
     */
    @Override
    public void dispose() {
        if (instance == this) {
            instance = null;
        }
        super.dispose();
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

        // Left side: Hamburger menu + Dashboard text
        JPanel leftPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(LIGHT_BLUE);
        leftPanel.setOpaque(false);

        JLabel menuIcon = new JLabel("☰", SwingConstants.CENTER);
        menuIcon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        menuIcon.setForeground(Color.BLACK);

        JLabel dashboardLabel = new JLabel("Dashboard", SwingConstants.LEFT);
        dashboardLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        dashboardLabel.setForeground(Color.BLACK);

        leftPanel.add(menuIcon);
        leftPanel.add(dashboardLabel);

        // Right side: Search field + icons
        JPanel rightPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(LIGHT_BLUE);
        rightPanel.setOpaque(false);

        searchField = new JTextField("search....");
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
        searchField.addActionListener(e -> filterQuickActions());
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterQuickActions();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterQuickActions();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterQuickActions();
            }
        });

        JLabel searchIcon = new JLabel("🔍", SwingConstants.CENTER);
        searchIcon.setFont(new Font("SansSerif", Font.PLAIN, 18));
        searchIcon.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        searchIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                filterQuickActions();
            }
        });
        
        JButton refreshButton = new JButton("⟳");
        refreshButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setFocusPainted(false);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        refreshButton.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> refreshDashboard());

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
        sidebar = new JPanel();
        sidebar.setBackground(LIGHT_BLUE);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.setPreferredSize(new Dimension(240, 0));

        // Initialize sidebar items data
        sidebarItems.clear();
        sidebarItems.add(new SidebarItemData("⚠", "Emergency Alerts",
            e -> new EmergencyAlertFrame(user).setVisible(true)));
        sidebarItems.add(new SidebarItemData("☎", "Emergency Hotline",
            e -> new EmergencyHotlineFrame(user).setVisible(true)));
        sidebarItems.add(new SidebarItemData("📷", "Report Incident",
            e -> new ReportIncidentFrame(user).setVisible(true)));
        sidebarItems.add(new SidebarItemData("🚪", "Evacuation Center",
            e -> new EvacuationCenterFrame(user).setVisible(true)));
        sidebarItems.add(new SidebarItemData("👤", "Profile",
            e -> new ProfileFrame(user).setVisible(true)));

        refreshSidebar();

        return sidebar;
    }
    
    private void refreshSidebar() {
        sidebar.removeAll();
        
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

        // Add all sidebar items
        for (int i = 0; i < sidebarItems.size(); i++) {
            SidebarItemData itemData = sidebarItems.get(i);
            sidebar.add(createSidebarItem(itemData.icon, itemData.text, i == 0, itemData.listener));
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.revalidate();
        sidebar.repaint();
    }
    
    private void filterQuickActions() {
        String searchText = searchField.getText().trim().toLowerCase();
        
        // If search field has placeholder or is empty, show all items
        if (searchText.isEmpty() || searchText.equals("search....")) {
            refreshSidebar();
            return;
        }
        
        sidebar.removeAll();
        
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

        // Filter and add matching items
        boolean foundAny = false;
        for (int i = 0; i < sidebarItems.size(); i++) {
            SidebarItemData itemData = sidebarItems.get(i);
            String itemText = itemData.text.toLowerCase();
            
            if (itemText.contains(searchText)) {
                sidebar.add(createSidebarItem(itemData.icon, itemData.text, i == 0 && !foundAny, itemData.listener));
                foundAny = true;
            }
        }
        
        // If no matches found, show message
        if (!foundAny) {
            JLabel noResultsLabel = new JLabel("No results found", SwingConstants.CENTER);
            noResultsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            noResultsLabel.setForeground(new Color(120, 120, 120));
            noResultsLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            sidebar.add(noResultsLabel);
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.revalidate();
        sidebar.repaint();
    }
    
    private void refreshDashboard() {
        // Clear search field
        searchField.setText("search....");
        searchField.setForeground(Color.GRAY);
        
        // Refresh sidebar to show all items
        refreshSidebar();
    }

    private JPanel createSidebarItem(String icon, String text, boolean selected, 
                                     java.awt.event.ActionListener listener) {
        JPanel item = new JPanel(new BorderLayout());
        Color selectedColor = new Color(135, 206, 235); // Slightly darker blue for selected
        item.setBackground(selected ? selectedColor : LIGHT_BLUE);
        item.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 10));
        item.setPreferredSize(new Dimension(240, 50));
        item.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

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
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                listener.actionPerformed(new java.awt.event.ActionEvent(item, 0, ""));
                dispose();
            }
        });

        return item;
    }

    private JPanel buildMainContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        return content;
    }
}
