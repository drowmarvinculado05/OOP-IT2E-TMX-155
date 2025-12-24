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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Admin Dashboard for managing alerts, reports, evacuation centers, and analytics.
 */
public class AdminDashboardFrame extends JFrame {

    private static final Color LIGHT_BLUE = new Color(173, 216, 230); // Light blue color for header/sidebar
    private static final Color RED_CARD = new Color(220, 53, 69);
    private static final Color YELLOW_CARD = new Color(255, 193, 7);
    private static final Color GREEN_CARD = new Color(40, 167, 69);
    private static final Color BLUE_CARD = new Color(0, 123, 255);

    private final UserProfile user;

    private JPanel mainContentPanel;

    public AdminDashboardFrame(UserProfile user) {
        super("Bantay Alert - Admin Dashboard");
        this.user = user;
        buildUi();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
        
        // Refresh counts when window becomes visible or activated
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                refreshDashboard();
            }
            
            @Override
            public void windowActivated(java.awt.event.WindowEvent e) {
                refreshDashboard();
            }
        });
    }
    
    private void refreshDashboard() {
        if (mainContentPanel != null) {
            mainContentPanel.removeAll();
            mainContentPanel.add(buildMainContent(), BorderLayout.CENTER);
            mainContentPanel.revalidate();
            mainContentPanel.repaint();
        }
    }

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        root.add(buildTopHeader(), BorderLayout.NORTH);
        root.add(buildSidebar(), BorderLayout.WEST);
        mainContentPanel = buildMainContent();
        root.add(mainContentPanel, BorderLayout.CENTER);

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
            new LoginFrame().setVisible(true);
        });

        JLabel dashboardLabel = new JLabel("Admin Dashboard", SwingConstants.LEFT);
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
        refreshButton.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> refreshDashboard());

        rightPanel.add(searchField);
        rightPanel.add(searchIcon);
        rightPanel.add(refreshButton);

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
        sidebar.setMinimumSize(new Dimension(240, 0));
        sidebar.setMaximumSize(new Dimension(240, Integer.MAX_VALUE));

        // Management Tools section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(LIGHT_BLUE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 20, 10));
        
        JLabel sectionTitle = new JLabel("Management Tools");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        sectionTitle.setForeground(Color.BLACK);
        sectionTitle.setHorizontalAlignment(SwingConstants.LEFT);
        sectionTitle.setVerticalAlignment(SwingConstants.CENTER);
        
        titlePanel.add(sectionTitle, BorderLayout.WEST);
        sidebar.add(titlePanel);

        sidebar.add(createSidebarItem("⚠", "Manage Alerts", false,
            e -> {
                dispose();
                new ManageAlertsFrame(user).setVisible(true);
            }));
        sidebar.add(createSidebarItem("📄", "Manage Reports", false,
            e -> {
                dispose();
                new ManageReportsFrame(user).setVisible(true);
            }));
        sidebar.add(createSidebarItem("🏠", "Evacuation Centers", false,
            e -> {
                dispose();
                new ManageEvacuationCentersFrame(user).setVisible(true);
            }));
        sidebar.add(createSidebarItem("📊", "Analytics & Reports", false,
            e -> {
                dispose();
                new AnalyticsReportsFrame(user).setVisible(true);
            }));

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
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
        
        // Fixed content panel - no scrolling
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Summary Cards - ensure all 4 cards are visible including Total Evacuees
        mainContent.add(buildSummaryCards());
        mainContent.add(Box.createVerticalStrut(20));

        // Reports and Incident Types
        JPanel bottomRow = new JPanel();
        bottomRow.setLayout(new java.awt.GridLayout(1, 2, 20, 0));
        bottomRow.setBackground(Color.WHITE);
        bottomRow.setAlignmentX(0.0f);
        bottomRow.setAlignmentY(0.0f);
        bottomRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel reportsPanel = buildReportsOverview();
        reportsPanel.setAlignmentX(0.0f);
        reportsPanel.setAlignmentY(0.0f);
        
        JPanel incidentPanel = buildIncidentTypes();
        incidentPanel.setAlignmentX(0.0f);
        incidentPanel.setAlignmentY(0.0f);

        bottomRow.add(reportsPanel);
        bottomRow.add(incidentPanel);

        mainContent.add(bottomRow);

        content.add(mainContent, BorderLayout.CENTER);
        return content;
    }

    private JPanel buildSummaryCards() {
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new java.awt.GridLayout(1, 4, 15, 0));
        cardsPanel.setBackground(Color.WHITE);
        cardsPanel.setAlignmentX(0.0f);
        cardsPanel.setAlignmentY(0.0f);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Get actual counts from repositories
        int activeAlertsCount = com.bantayalert.data.AlertRepository.countActive();
        int pendingReportsCount = com.bantayalert.data.IncidentReportRepository.countPending();
        int evacuationCentersCount = com.bantayalert.data.EvacuationCenterRepository.findAll().size();
        int totalEvacueesCount = com.bantayalert.data.EvacuationCenterRepository.totalEvacuees();
        int openCentersCount = com.bantayalert.data.EvacuationCenterRepository.countOpen();
        int fullCentersCount = com.bantayalert.data.EvacuationCenterRepository.countFull();
        
        cardsPanel.add(createSummaryCard("⚠", "Active Alerts", 
            String.valueOf(activeAlertsCount), 
            activeAlertsCount + " active", RED_CARD));
        cardsPanel.add(createSummaryCard("📄", "Pending Reports", 
            String.valueOf(pendingReportsCount), 
            "Awaiting review", YELLOW_CARD));
        cardsPanel.add(createSummaryCard("🏠", "Evacuation Centers", 
            String.valueOf(evacuationCentersCount), 
            openCentersCount + " open, " + fullCentersCount + " full", GREEN_CARD));
        cardsPanel.add(createSummaryCard("👥", "Total Evacuees", 
            String.valueOf(totalEvacueesCount), 
            "Across all centers", BLUE_CARD));
        
        return cardsPanel;
    }

    private JPanel createSummaryCard(String icon, String title, String value, String subtitle, Color cardColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25)); // Equal padding on all sides
        card.setPreferredSize(new Dimension(Integer.MAX_VALUE, 140));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        card.setMinimumSize(new Dimension(200, 140));
        card.setAlignmentX(0.0f);
        card.setAlignmentY(0.0f);
        card.setOpaque(true);

        // Icon
        JLabel iconLabel = new JLabel(icon, SwingConstants.LEFT);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 36));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        iconLabel.setAlignmentX(0.0f);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(cardColor);
        contentPanel.setOpaque(false);
        contentPanel.setAlignmentX(0.0f);

        JLabel titleLabel = new JLabel(title, SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(0.0f);

        JLabel valueLabel = new JLabel(value, SwingConstants.LEFT);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 42)); // Larger, more prominent number
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        valueLabel.setAlignmentX(0.0f);
        valueLabel.setOpaque(false);

        JLabel subtitleLabel = new JLabel(subtitle, SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(255, 255, 255, 230)); // Slightly transparent for hierarchy
        subtitleLabel.setAlignmentX(0.0f);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(valueLabel);
        contentPanel.add(Box.createVerticalStrut(3));
        contentPanel.add(subtitleLabel);

        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(cardColor);
        iconPanel.setOpaque(false);
        iconPanel.add(iconLabel, BorderLayout.WEST);

        card.add(iconPanel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel buildReportsOverview() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        panel.setPreferredSize(new Dimension(450, 380));
        panel.setMaximumSize(new Dimension(450, 380));
        panel.setMinimumSize(new Dimension(450, 380));
        panel.setAlignmentX(0.0f);
        panel.setAlignmentY(0.0f);

        // Title
        JLabel titleLabel = new JLabel("Reports", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        titleLabel.setAlignmentX(0.0f);

        panel.add(titleLabel);

        // Reports Chart - showing Pending, Verified, Responded
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int margin = 40;
                int chartWidth = width - 2 * margin;
                int chartHeight = height - 2 * margin;

                // Draw axes
                g2.setColor(Color.BLACK);
                g2.drawLine(margin, margin, margin, margin + chartHeight);
                g2.drawLine(margin, margin + chartHeight, margin + chartWidth, margin + chartHeight);

                // Get actual reports data and count by status
                int pendingCount = com.bantayalert.data.IncidentReportRepository.countPending();
                int verifiedCount = com.bantayalert.data.IncidentReportRepository.countVerified();
                int respondedCount = com.bantayalert.data.IncidentReportRepository.countResponded();
                
                // Fixed max value of 5.0 for Y-axis
                int maxValue = 5;
                
                // Draw bars
                Color barColor = new Color(173, 216, 230); // Light blue
                g2.setColor(barColor);
                
                int barWidth = 60;
                int barSpacing = 40;
                int startX = margin + 20;
                
                // Pending bar
                int pendingBarHeight = (int)((double) Math.min(pendingCount, maxValue) / maxValue * chartHeight);
                int pendingX = startX;
                int pendingY = margin + chartHeight - pendingBarHeight;
                g2.fillRect(pendingX, pendingY, barWidth, pendingBarHeight);
                
                // Verified bar
                int verifiedBarHeight = (int)((double) Math.min(verifiedCount, maxValue) / maxValue * chartHeight);
                int verifiedX = startX + barWidth + barSpacing;
                int verifiedY = margin + chartHeight - verifiedBarHeight;
                g2.fillRect(verifiedX, verifiedY, barWidth, verifiedBarHeight);
                
                // Responded bar
                int respondedBarHeight = (int)((double) Math.min(respondedCount, maxValue) / maxValue * chartHeight);
                int respondedX = startX + 2 * (barWidth + barSpacing);
                int respondedY = margin + chartHeight - respondedBarHeight;
                g2.fillRect(respondedX, respondedY, barWidth, respondedBarHeight);

                // Draw legend
                g2.setColor(barColor);
                g2.fillRect(margin + chartWidth - 80, margin, 15, 15);
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.drawString("Reports", margin + chartWidth - 60, margin + 12);

                // X-axis labels
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.drawString("Pending", pendingX + 5, margin + chartHeight + 20);
                g2.drawString("Verified", verifiedX + 5, margin + chartHeight + 20);
                g2.drawString("Responded", respondedX - 5, margin + chartHeight + 20);

                // Y-axis labels: 0.0, 1.0, 2.0, 3.0, 4.0, 5.0
                String[] yLabels = {"0.0", "1.0", "2.0", "3.0", "4.0", "5.0"};
                for (int i = 0; i < yLabels.length; i++) {
                    int y = margin + chartHeight - (i * chartHeight / 5);
                    g2.drawString(yLabels[i], margin - 30, y + 5);
                }

                g2.dispose();
            }
        };
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        chartPanel.setMaximumSize(new Dimension(400, 300));
        chartPanel.setMinimumSize(new Dimension(400, 300));
        chartPanel.setAlignmentX(0.0f);

        panel.add(chartPanel);

        return panel;
    }

    private JPanel buildIncidentTypes() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        panel.setPreferredSize(new Dimension(450, 380));
        panel.setMaximumSize(new Dimension(450, 380));
        panel.setMinimumSize(new Dimension(450, 380));
        panel.setAlignmentX(0.0f);
        panel.setAlignmentY(0.0f);

        // Title
        JLabel titleLabel = new JLabel("Incident Types", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        titleLabel.setAlignmentX(0.0f);

        panel.add(titleLabel);

        // Bar Chart (simplified representation)
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int margin = 40;
                int chartWidth = width - 2 * margin;
                int chartHeight = height - 2 * margin;

                // Draw axes
                g2.setColor(Color.BLACK);
                g2.drawLine(margin, margin, margin, margin + chartHeight);
                g2.drawLine(margin, margin + chartHeight, margin + chartWidth, margin + chartHeight);

                // Get actual reports data and count by type
                java.util.List<com.bantayalert.data.IncidentReport> allReports = 
                    com.bantayalert.data.IncidentReportRepository.findAll();
                
                // Define all possible incident types
                String[] incidentTypes = {"Fire", "Flood", "Earthquake", "Typhoon", "Landslide", 
                                         "Medical Emergency", "Crime", "Other"};
                
                // Count reports by type
                java.util.Map<String, Integer> typeCounts = new java.util.HashMap<>();
                for (com.bantayalert.data.IncidentReport report : allReports) {
                    String type = report.getType();
                    typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
                }
                
                // Get counts for each type
                int[] counts = new int[incidentTypes.length];
                for (int i = 0; i < incidentTypes.length; i++) {
                    counts[i] = typeCounts.getOrDefault(incidentTypes[i], 0);
                }
                
                // Fixed max value of 5.0 for Y-axis
                int maxValue = 5;
                
                // Draw bars
                Color barColor = new Color(173, 216, 230); // Light blue
                g2.setColor(barColor);
                
                int barWidth = 50;
                int barSpacing = 15;
                int startX = margin + 20;
                int maxBars = Math.min(incidentTypes.length, 8); // Show max 8 bars
                
                // Draw bars for each type that has data
                int visibleBars = 0;
                for (int i = 0; i < maxBars; i++) {
                    if (counts[i] > 0) {
                        // Calculate bar height: 1 report = 1.0 on scale, 5 reports = 5.0
                        int actualCount = Math.min(counts[i], maxValue); // Cap at 5.0
                        int barHeight = (int)((double) actualCount / maxValue * chartHeight);
                        int x = startX + visibleBars * (barWidth + barSpacing);
                        int y = margin + chartHeight - barHeight;
                        g2.fillRect(x, y, barWidth, barHeight);
                        
                        // Draw label below bar
                        String label = incidentTypes[i].length() > 8 ? 
                            incidentTypes[i].substring(0, 7) + ".." : incidentTypes[i];
                        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                        g2.drawString(label, x - 5, margin + chartHeight + 15);
                        
                        visibleBars++;
                    }
                }
                
                // If no data, show placeholder
                if (visibleBars == 0) {
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    g2.setColor(new Color(150, 150, 150));
                    g2.drawString("No incident reports yet", margin + chartWidth / 2 - 80, margin + chartHeight / 2);
                }

                // Draw legend
                g2.setColor(barColor);
                g2.fillRect(margin + chartWidth - 80, margin, 15, 15);
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.drawString("Incidents", margin + chartWidth - 60, margin + 12);

                // Y-axis labels: 0.0, 1.0, 2.0, 3.0, 4.0, 5.0
                String[] yLabels = {"0.0", "1.0", "2.0", "3.0", "4.0", "5.0"};
                for (int i = 0; i < yLabels.length; i++) {
                    int y = margin + chartHeight - (i * chartHeight / 5);
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    g2.drawString(yLabels[i], margin - 30, y + 5);
                }

                g2.dispose();
            }
        };
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        chartPanel.setMaximumSize(new Dimension(400, 300));
        chartPanel.setMinimumSize(new Dimension(400, 300));
        chartPanel.setAlignmentX(0.0f);

        panel.add(chartPanel);

        return panel;
    }

}

