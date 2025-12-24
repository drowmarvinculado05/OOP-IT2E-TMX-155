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
import java.awt.GridLayout;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Screen showing emergency hotlines with category filters and contact cards.
 */
public class EmergencyHotlineFrame extends JFrame {

    private final UserProfile user;
    private JPanel cardsContainer;
    private JButton allButton, policeButton, fireButton, medicalButton, mdrrmoButton;
    private final List<HotlineContact> allContacts = new ArrayList<>();
    private String currentFilter = "All";
    private static final Color LIGHT_BLUE = new Color(173, 216, 230); // Light blue color for header/sidebar

    public EmergencyHotlineFrame(UserProfile user) {
        super("Bantay Alert - Emergency Hotlines");
        this.user = user;
        initializeContacts();
        buildUi();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
    }

    private void initializeContacts() {
        // Police
        allContacts.add(new HotlineContact("🛡", "Mati Police Station", "0928 422 4043", "Police"));
        
        // Fire
        allContacts.add(new HotlineContact("🔥", "Bureau of Fire Protection", "(087) 811 2072", "Fire"));
        
        // Medical
        allContacts.add(new HotlineContact("✋", "Emergency Medical Service", "0917 711 2975", "Medical"));
        allContacts.add(new HotlineContact("❤", "Red Cross Emergency", "(087) 388 4022", "Medical"));
        
        // MDRRMO / Government
        allContacts.add(new HotlineContact("🏠", "MDRRMO", "082-224-2535", "MDRRMO"));
        allContacts.add(new HotlineContact("🏠", "Barangay Captain", "09265308498", "MDRRMO"));
        allContacts.add(new HotlineContact("⚙", "Local Government Unit", "(0910) 563 9035", "MDRRMO"));
        
        // Other
        allContacts.add(new HotlineContact("⚡", "Meralco Office", "(0995) 32 98835", "Other"));
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

        JLabel dashboardLabel = new JLabel("Emergency Hotlines", SwingConstants.LEFT);
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
            filterContacts(currentFilter);
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
        sidebar.add(createSidebarItem("☎", "Emergency Hotline", true,
            e -> {}));
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
                    if (!text.equals("Emergency Hotline")) {
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

        content.add(buildFilterButtons(), BorderLayout.NORTH);
        content.add(buildHotlineCards(), BorderLayout.CENTER);

        return content;
    }

    private JPanel buildFilterButtons() {
        JPanel container = new JPanel();
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Single row, five equal-width columns
        container.setLayout(new GridLayout(1, 5, 15, 0));

        allButton = createFilterButton("≡", "All", "All");
        policeButton = createFilterButton("🛡", "Police", "Police");
        fireButton = createFilterButton("🔥", "Fire", "Fire");
        medicalButton = createFilterButton("➕", "Medical", "Medical");
        mdrrmoButton = createFilterButton("⚠", "MDRRMO", "MDRRMO");

        // Set "All" as initially selected
        updateFilterButtonHighlight(allButton, true);

        container.add(allButton);
        container.add(policeButton);
        container.add(fireButton);
        container.add(medicalButton);
        container.add(mdrrmoButton);

        return container;
    }

    private JButton createFilterButton(String icon, String text, String filterCategory) {
        JButton button = new JButton();
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, false));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 70));

        // Inner panel with GridLayout for icon + text alignment
        JPanel innerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        innerPanel.setBackground(Color.WHITE);
        innerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 32));
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);

        JLabel textLabel = new JLabel(text, SwingConstants.LEFT);
        textLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        textLabel.setVerticalAlignment(SwingConstants.CENTER);

        innerPanel.add(iconLabel);
        innerPanel.add(textLabel);

        button.setLayout(new BorderLayout());
        button.add(innerPanel, BorderLayout.CENTER);

        // Add action listener for filtering
        button.addActionListener(e -> filterContacts(filterCategory));

        return button;
    }

    private void updateFilterButtonHighlight(JButton button, boolean highlighted) {
        if (highlighted) {
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, false));
        } else {
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, false));
        }
    }

    private void filterContacts(String category) {
        currentFilter = category;
        
        // Update button highlights
        updateFilterButtonHighlight(allButton, category.equals("All"));
        updateFilterButtonHighlight(policeButton, category.equals("Police"));
        updateFilterButtonHighlight(fireButton, category.equals("Fire"));
        updateFilterButtonHighlight(medicalButton, category.equals("Medical"));
        updateFilterButtonHighlight(mdrrmoButton, category.equals("MDRRMO"));

        // Clear existing cards
        cardsContainer.removeAll();

        // Filter and add cards
        List<HotlineContact> filtered = new ArrayList<>();
        if (category.equals("All")) {
            filtered.addAll(allContacts);
        } else {
            for (HotlineContact contact : allContacts) {
                if (contact.category.equals(category)) {
                    filtered.add(contact);
                }
            }
        }

        // Calculate grid layout (2 columns, dynamic rows)
        int rows = (filtered.size() + 1) / 2; // Round up
        if (rows == 0) rows = 1;
        cardsContainer.setLayout(new GridLayout(rows, 2, 20, 15));

        // Add filtered cards
        for (HotlineContact contact : filtered) {
            cardsContainer.add(createHotlineCard(contact.icon, contact.name, contact.phone));
        }

        // Refresh the container
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    private JPanel buildHotlineCards() {
        JPanel scrollContainer = new JPanel(new BorderLayout());
        scrollContainer.setBackground(Color.WHITE);

        cardsContainer = new JPanel();
        cardsContainer.setBackground(Color.WHITE);
        // 2 columns, 4 rows for 8 cards (initial)
        cardsContainer.setLayout(new GridLayout(4, 2, 20, 15));

        // Add all contacts initially
        for (HotlineContact contact : allContacts) {
            cardsContainer.add(createHotlineCard(contact.icon, contact.name, contact.phone));
        }

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollContainer.add(scrollPane, BorderLayout.CENTER);
        return scrollContainer;
    }

    private JPanel createHotlineCard(String icon, String name, String phone) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, false));
        card.setPreferredSize(new Dimension(400, 100));
        card.setMaximumSize(new Dimension(400, 100));

        // Left: Icon (well-centered)
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        iconPanel.setPreferredSize(new Dimension(80, 100));
        
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 36));
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconPanel.add(iconLabel, BorderLayout.CENTER);

        // Center: Name and Phone (vertical stack, centered)
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.WHITE);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setAlignmentX(0.5f);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel phoneLabel = new JLabel(phone, SwingConstants.CENTER);
        phoneLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        phoneLabel.setAlignmentX(0.5f);
        phoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
        phoneLabel.setForeground(new Color(0, 100, 200)); // Blue color to indicate clickable
        phoneLabel.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        phoneLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                makePhoneCall(phone);
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                phoneLabel.setForeground(new Color(0, 150, 255)); // Lighter blue on hover
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                phoneLabel.setForeground(new Color(0, 100, 200)); // Original blue
            }
        });

        textPanel.add(Box.createVerticalGlue());
        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(6));
        textPanel.add(phoneLabel);
        textPanel.add(Box.createVerticalGlue());

        // Right: Phone call icon (well-centered)
        JPanel callIconPanel = new JPanel(new BorderLayout());
        callIconPanel.setBackground(Color.WHITE);
        callIconPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
        callIconPanel.setPreferredSize(new Dimension(70, 100));
        
        JLabel callIconLabel = new JLabel("📞", SwingConstants.CENTER);
        callIconLabel.setFont(new Font("SansSerif", Font.PLAIN, 36));
        callIconLabel.setVerticalAlignment(SwingConstants.CENTER);
        callIconLabel.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        callIconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                makePhoneCall(phone);
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                callIconLabel.setFont(new Font("SansSerif", Font.PLAIN, 40)); // Slightly larger on hover
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                callIconLabel.setFont(new Font("SansSerif", Font.PLAIN, 36)); // Original size
            }
        });
        callIconPanel.add(callIconLabel, BorderLayout.CENTER);

        card.add(iconPanel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        card.add(callIconPanel, BorderLayout.EAST);

        return card;
    }

    /**
     * Initiates a phone call using the system's default calling application.
     */
    private void makePhoneCall(String phoneNumber) {
        try {
            // Clean phone number (remove spaces, dashes, parentheses)
            String cleanNumber = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");
            
            // Create tel: URI
            URI telUri = new URI("tel:" + cleanNumber);
            
            // Check if Desktop is supported
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(telUri);
                } else {
                    // Fallback: show message
                    javax.swing.JOptionPane.showMessageDialog(this,
                        "Calling: " + phoneNumber + "\n\nNote: Your system may not support direct calling.\nPlease dial manually.",
                        "Call",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Fallback: show message
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Calling: " + phoneNumber + "\n\nPlease dial manually.",
                    "Call",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            // Show error message
            javax.swing.JOptionPane.showMessageDialog(this,
                "Unable to initiate call to: " + phoneNumber + "\n\nError: " + e.getMessage() + "\n\nPlease dial manually.",
                "Call Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Data class to represent a hotline contact with its category.
     */
    private static class HotlineContact {
        String icon;
        String name;
        String phone;
        String category;

        HotlineContact(String icon, String name, String phone, String category) {
            this.icon = icon;
            this.name = name;
            this.phone = phone;
            this.category = category;
        }
    }
}
