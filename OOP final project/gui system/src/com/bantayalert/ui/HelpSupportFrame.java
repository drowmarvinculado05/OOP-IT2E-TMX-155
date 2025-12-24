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
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.net.URI;

/**
 * Help & Support screen for user assistance and contact information.
 */
public class HelpSupportFrame extends JFrame {

    private final UserProfile user;
    private static final Color LIGHT_BLUE = new Color(173, 216, 230); // Light blue color for header/sidebar
    private static final Color GREEN_BUTTON = new Color(56, 142, 60); // Vibrant, highly visible green color for buttons

    public HelpSupportFrame(UserProfile user) {
        super("Bantay Alert - Help & Support");
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

        JLabel dashboardLabel = new JLabel("Help & Support", SwingConstants.LEFT);
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
        centerWrapper.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));
        centerWrapper.setAlignmentX(0.0f);

        // Main title
        JLabel mainTitle = new JLabel("Help & Support", SwingConstants.LEFT);
        mainTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        mainTitle.setForeground(Color.BLACK);
        mainTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainTitle.setAlignmentX(0.0f);

        centerWrapper.add(mainTitle);
        centerWrapper.add(Box.createVerticalStrut(10));

        // Description text
        JLabel descriptionLabel = new JLabel(
            "For urgent issues, please call the MDRRMO office or local hotlines listed in the app.",
            SwingConstants.LEFT
        );
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        descriptionLabel.setForeground(Color.BLACK);
        descriptionLabel.setHorizontalAlignment(SwingConstants.LEFT);
        descriptionLabel.setVerticalAlignment(SwingConstants.TOP);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        descriptionLabel.setAlignmentX(0.0f);
        descriptionLabel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        centerWrapper.add(descriptionLabel);
        centerWrapper.add(Box.createVerticalStrut(20));

        // Email Support option
        centerWrapper.add(createSupportOption("✉", "Email Support"));
        centerWrapper.add(Box.createVerticalStrut(15));

        // Facebook Page option
        centerWrapper.add(createSupportOption("f", "Facebook Page"));
        centerWrapper.add(Box.createVerticalStrut(15));

        // Go Back button - vibrant green
        JButton goBackButton = new JButton("← Go Back");
        goBackButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        goBackButton.setForeground(Color.WHITE);
        goBackButton.setBackground(GREEN_BUTTON);
        goBackButton.setFocusPainted(false);
        goBackButton.setBorderPainted(false); // Remove border to show full green color
        goBackButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        goBackButton.setContentAreaFilled(true);
        goBackButton.setOpaque(true);
        goBackButton.setPreferredSize(new Dimension(700, 50));
        goBackButton.setMaximumSize(new Dimension(700, 50));
        goBackButton.setAlignmentX(0.0f);
        goBackButton.addActionListener(e -> {
            dispose();
            new ProfileFrame(user).setVisible(true);
        });

        centerWrapper.add(goBackButton);
        centerWrapper.add(Box.createVerticalGlue());

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

    private JPanel createSupportOption(String icon, String text) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));
        container.setAlignmentX(0.0f);
        container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));
        container.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

        // Icon panel - smaller size
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setPreferredSize(new Dimension(40, 50));
        iconPanel.setMaximumSize(new Dimension(40, 50));
        iconPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        if (icon.equals("f")) {
            // Facebook 'f' logo - use a bold font to make it look like the Facebook logo
            iconLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            iconLabel.setForeground(new Color(24, 119, 242)); // Facebook blue
        } else {
            iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
            iconLabel.setForeground(Color.BLACK);
        }
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconPanel.add(iconLabel, BorderLayout.CENTER);

        // Text panel
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(Color.WHITE);
        textPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 15));

        JLabel textLabel = new JLabel(text, SwingConstants.LEFT);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        textLabel.setForeground(Color.BLACK);
        textLabel.setHorizontalAlignment(SwingConstants.LEFT);
        textLabel.setVerticalAlignment(SwingConstants.CENTER);
        textPanel.add(textLabel, BorderLayout.WEST);

        container.add(iconPanel, BorderLayout.WEST);
        container.add(textPanel, BorderLayout.CENTER);

        // Add click listener
        container.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (text.equals("Email Support")) {
                    try {
                        Desktop.getDesktop().mail(new URI("mailto:support@bantayalert.com"));
                    } catch (Exception ex) {
                        javax.swing.JOptionPane.showMessageDialog(HelpSupportFrame.this,
                            "Unable to open email client. Please contact support@bantayalert.com",
                            "Email Support",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (text.equals("Facebook Page")) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://www.facebook.com/bantayalert"));
                    } catch (Exception ex) {
                        javax.swing.JOptionPane.showMessageDialog(HelpSupportFrame.this,
                            "Unable to open browser. Please visit our Facebook page manually.",
                            "Facebook Page",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        return container;
    }
}

