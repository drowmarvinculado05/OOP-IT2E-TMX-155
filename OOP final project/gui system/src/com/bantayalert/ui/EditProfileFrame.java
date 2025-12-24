package com.bantayalert.ui;

import com.bantayalert.data.UserProfile;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
 * Edit Profile screen for updating user information.
 */
public class EditProfileFrame extends JFrame {

    private final UserProfile user;
    private JTextField nameField;
    private JTextField phoneField;
    private JComboBox<String> locationComboBox;
    private static final Color LIGHT_BLUE = new Color(173, 216, 230); // Light blue color for header/sidebar
    private static final Color GREEN_BUTTON = new Color(56, 142, 60); // Vibrant, highly visible green color for Save button

    public EditProfileFrame(UserProfile user) {
        super("Bantay Alert - Edit Profile");
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

        JLabel dashboardLabel = new JLabel("Edit Profile", SwingConstants.LEFT);
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
        JLabel sectionTitle = new JLabel("Edit Profile", SwingConstants.LEFT);
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        sectionTitle.setForeground(Color.BLACK);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        sectionTitle.setAlignmentX(0.0f);

        centerWrapper.add(sectionTitle);
        centerWrapper.add(Box.createVerticalStrut(10));

        // Name field
        centerWrapper.add(createInputField("👤", "Name", user.getFullName(), false));
        centerWrapper.add(Box.createVerticalStrut(20));

        // Phone field
        centerWrapper.add(createInputField("📞", "Phone Number", user.getPhone(), false));
        centerWrapper.add(Box.createVerticalStrut(20));

        // Location dropdown
        centerWrapper.add(createLocationField("📍", "Location", user.getLocation()));
        centerWrapper.add(Box.createVerticalGlue());

        // Save Changes button - vibrant green
        JButton saveButton = new JButton("Save Changes");
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(GREEN_BUTTON);
        saveButton.setOpaque(true); // Ensure background color is visible
        saveButton.setFocusPainted(false);
        saveButton.setContentAreaFilled(true); // Ensure button is filled with background color
        saveButton.setBorderPainted(false); // Remove border to show full green color
        saveButton.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        saveButton.setPreferredSize(new Dimension(600, 50));
        saveButton.setMaximumSize(new Dimension(600, 50));
        saveButton.setAlignmentX(0.0f);
        saveButton.addActionListener(e -> saveChanges());

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

    private JPanel createInputField(String icon, String label, String value, boolean readOnly) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        // Icon panel
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setPreferredSize(new Dimension(60, 50));
        iconPanel.setMaximumSize(new Dimension(60, 50));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 28));
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconPanel.add(iconLabel, BorderLayout.CENTER);

        // Text field panel
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JTextField textField = new JTextField(value);
        textField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1, false),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        textField.setEditable(!readOnly);
        textField.setBackground(readOnly ? new Color(245, 245, 245) : Color.WHITE);
        textField.setPreferredSize(new Dimension(0, 50));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        if (label.equals("Name")) {
            nameField = textField;
        } else if (label.equals("Phone Number")) {
            phoneField = textField;
        }

        fieldPanel.add(textField, BorderLayout.CENTER);

        container.add(iconPanel, BorderLayout.WEST);
        container.add(fieldPanel, BorderLayout.CENTER);

        return container;
    }

    private JPanel createLocationField(String icon, String label, String value) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        // Icon panel
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setPreferredSize(new Dimension(60, 50));
        iconPanel.setMaximumSize(new Dimension(60, 50));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 28));
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconPanel.add(iconLabel, BorderLayout.CENTER);

        // ComboBox panel
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        String[] locations = {
            "Badas", "Bobon", "Buso", "Cabuaya", "Central (City Proper)", 
            "Culian", "Dahican", "Danao", "Dawan", "Don Enrique Lopez", 
            "Don Martin Marundan", "Don Salvador Lopez, Sr.", "Langka", 
            "Lawigan", "Libudon", "Luban", "Macambol", "Mamali", "Matiao", 
            "Mayo", "Sainz", "Sanghay", "Tagabakid", "Tagbinonga", 
            "Taguibo", "Tamisan"
        };
        locationComboBox = new JComboBox<>(locations);
        locationComboBox.setFont(new Font("SansSerif", Font.PLAIN, 18));
        locationComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1, false),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        locationComboBox.setBackground(Color.WHITE);
        locationComboBox.setPreferredSize(new Dimension(0, 50));
        locationComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        // Set current value
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].equals(value)) {
                locationComboBox.setSelectedIndex(i);
                break;
            }
        }

        fieldPanel.add(locationComboBox, BorderLayout.CENTER);

        container.add(iconPanel, BorderLayout.WEST);
        container.add(fieldPanel, BorderLayout.CENTER);

        return container;
    }

    private void saveChanges() {
        String newName = nameField.getText().trim();
        String newPhone = phoneField.getText().trim();
        String newLocation = (String) locationComboBox.getSelectedItem();

        // Validate fields
        if (newName.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Name cannot be empty.",
                "Validation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPhone.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Phone number cannot be empty.",
                "Validation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update the user profile in the repository
        com.bantayalert.data.UserRepository.updateProfile(
            user.getEmail(),
            newName,
            newPhone,
            newLocation
        );

        // Get the updated profile from repository
        java.util.Optional<com.bantayalert.data.UserProfile> updatedProfileOpt = 
            com.bantayalert.data.UserRepository.findByEmail(user.getEmail());
        
        com.bantayalert.data.UserProfile updatedProfile = updatedProfileOpt.orElse(user);

        // Show success message
        javax.swing.JOptionPane.showMessageDialog(this,
            "Profile updated successfully!\n\nName: " + newName + "\nPhone: " + newPhone + "\nLocation: " + newLocation,
            "Success",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);

        // Close and return to Profile screen with updated profile
        dispose();
        new ProfileFrame(updatedProfile).setVisible(true);
    }
}

