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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;

/**
 * Screen for reporting incidents with incident details, photos, and videos.
 */
public class ReportIncidentFrame extends JFrame {

    private final UserProfile user;
    private JComboBox<String> incidentTypeCombo;
    private JTextArea descriptionArea;
    private File selectedPhoto;
    private File selectedVideo;
    private JLabel photoFileNameLabel;
    private JLabel videoFileNameLabel;
    private static final Color LIGHT_BLUE = new Color(173, 216, 230); // Light blue color for header/sidebar

    public ReportIncidentFrame(UserProfile user) {
        super("Bantay Alert - Report Incident");
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

        JLabel dashboardLabel = new JLabel("Report Incident", SwingConstants.LEFT);
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
            incidentTypeCombo.setSelectedIndex(0);
            descriptionArea.setText("");
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
        sidebar.add(createSidebarItem("📷", "Report Incident", true,
            e -> {}));
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
                    if (!text.equals("Report Incident")) {
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

        // Centered scrollable content
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Color.WHITE);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel sectionTitle = new JLabel("Incident Details");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        sectionTitle.setAlignmentX(0.5f);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        scrollContent.add(sectionTitle);
        scrollContent.add(Box.createVerticalStrut(10));
        
        JPanel incidentTypeField = buildIncidentTypeField();
        incidentTypeField.setAlignmentX(0.5f);
        scrollContent.add(incidentTypeField);
        
        scrollContent.add(Box.createVerticalStrut(20));
        
        JPanel descriptionField = buildDescriptionField();
        descriptionField.setAlignmentX(0.5f);
        scrollContent.add(descriptionField);
        
        scrollContent.add(Box.createVerticalStrut(25));
        
        JPanel mediaSection = buildMediaSection();
        mediaSection.setAlignmentX(0.5f);
        scrollContent.add(mediaSection);
        
        scrollContent.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.add(scrollPane, BorderLayout.CENTER);
        mainContent.add(buildSubmitButton(), BorderLayout.SOUTH);

        content.add(mainContent, BorderLayout.CENTER);
        return content;
    }

    private JPanel buildIncidentTypeField() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        labelPanel.setBackground(Color.WHITE);
        labelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        labelPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        JLabel iconLabel = new JLabel("⚠");
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        iconLabel.setAlignmentY(0.5f);

        labelPanel.add(iconLabel);
        labelPanel.add(Box.createHorizontalStrut(10));

        JLabel label = new JLabel("Incident Type*");
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        label.setAlignmentY(0.5f);

        labelPanel.add(label);
        labelPanel.add(Box.createHorizontalGlue());

        // Dropdown combo box
        String[] incidentTypes = {"Select incident type", "Fire", "Flood", "Earthquake", 
                                  "Typhoon", "Landslide", "Medical Emergency", "Crime", "Other"};
        incidentTypeCombo = new JComboBox<>(incidentTypes);
        incidentTypeCombo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        incidentTypeCombo.setBackground(Color.WHITE);
        incidentTypeCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1, false),
            BorderFactory.createEmptyBorder(12, 15, 12, 40)
        ));
        incidentTypeCombo.setPreferredSize(new Dimension(600, 50));
        incidentTypeCombo.setMaximumSize(new Dimension(600, 50));

        container.add(labelPanel);
        container.add(incidentTypeCombo);

        return container;
    }

    private JPanel buildDescriptionField() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        JLabel label = new JLabel("Description*", SwingConstants.LEFT);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        label.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        descriptionArea = new JTextArea();
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1, false),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        descriptionArea.setRows(6);
        String placeholderText = "Describe the incident in detail, include what happened, when it started and any immediate danger";
        descriptionArea.setText(placeholderText);
        descriptionArea.setForeground(new Color(150, 150, 150));
        descriptionArea.addFocusListener(new com.bantayalert.ui.PlaceholderFocusListener(
            descriptionArea, placeholderText
        ));
        descriptionArea.setPreferredSize(new Dimension(600, 150));
        descriptionArea.setMaximumSize(new Dimension(600, 150));

        container.add(label);
        container.add(descriptionArea);

        return container;
    }

    private JPanel buildMediaSection() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        JLabel titleLabel = new JLabel("Photo & Videos (Optional)", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        titleLabel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        JLabel instructionLabel = new JLabel(
            "Add media to help authorities assess the situation. Max video size: 50MB",
            SwingConstants.LEFT
        );
        instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        instructionLabel.setForeground(new Color(100, 100, 100));
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        instructionLabel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        // Photo button with file name display
        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(new BoxLayout(photoPanel, BoxLayout.Y_AXIS));
        photoPanel.setBackground(Color.WHITE);
        photoPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        
        JButton addPhotoButton = createMediaButton("📷", "Add Photo");
        addPhotoButton.setMaximumSize(new Dimension(600, 60));
        addPhotoButton.addActionListener(e -> selectPhotoFile());
        
        photoFileNameLabel = new JLabel("", SwingConstants.LEFT);
        photoFileNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        photoFileNameLabel.setForeground(new Color(100, 100, 100));
        photoFileNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        photoFileNameLabel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        
        photoPanel.add(addPhotoButton);
        photoPanel.add(photoFileNameLabel);

        // Video button with file name display
        JPanel videoPanel = new JPanel();
        videoPanel.setLayout(new BoxLayout(videoPanel, BoxLayout.Y_AXIS));
        videoPanel.setBackground(Color.WHITE);
        videoPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        
        JButton addVideoButton = createMediaButton("🎥", "Add Video");
        addVideoButton.setMaximumSize(new Dimension(600, 60));
        addVideoButton.addActionListener(e -> selectVideoFile());
        
        videoFileNameLabel = new JLabel("", SwingConstants.LEFT);
        videoFileNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        videoFileNameLabel.setForeground(new Color(100, 100, 100));
        videoFileNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        videoFileNameLabel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        
        videoPanel.add(addVideoButton);
        videoPanel.add(videoFileNameLabel);

        buttonsPanel.add(photoPanel);
        buttonsPanel.add(Box.createVerticalStrut(12));
        buttonsPanel.add(videoPanel);

        container.add(titleLabel);
        container.add(instructionLabel);
        container.add(buttonsPanel);

        return container;
    }
    
    private void selectPhotoFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Photo");
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        // Filter for image files
        javax.swing.filechooser.FileFilter imageFilter = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String name = f.getName().toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".jpeg") || 
                       name.endsWith(".png") || name.endsWith(".gif") || 
                       name.endsWith(".bmp");
            }
            
            @Override
            public String getDescription() {
                return "Image Files (*.jpg, *.jpeg, *.png, *.gif, *.bmp)";
            }
        };
        fileChooser.addChoosableFileFilter(imageFilter);
        fileChooser.setFileFilter(imageFilter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedPhoto = fileChooser.getSelectedFile();
            photoFileNameLabel.setText("Selected: " + selectedPhoto.getName());
            photoFileNameLabel.setForeground(new Color(0, 150, 0));
        }
    }
    
    private void selectVideoFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Video");
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        // Filter for video files
        javax.swing.filechooser.FileFilter videoFilter = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String name = f.getName().toLowerCase();
                return name.endsWith(".mp4") || name.endsWith(".avi") || 
                       name.endsWith(".mov") || name.endsWith(".wmv") || 
                       name.endsWith(".flv") || name.endsWith(".mkv");
            }
            
            @Override
            public String getDescription() {
                return "Video Files (*.mp4, *.avi, *.mov, *.wmv, *.flv, *.mkv)";
            }
        };
        fileChooser.addChoosableFileFilter(videoFilter);
        fileChooser.setFileFilter(videoFilter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            long fileSizeMB = selectedFile.length() / (1024 * 1024);
            
            if (fileSizeMB > 50) {
                videoFileNameLabel.setText("Error: File size exceeds 50MB limit");
                videoFileNameLabel.setForeground(new Color(200, 0, 0));
                selectedVideo = null;
            } else {
                selectedVideo = selectedFile;
                videoFileNameLabel.setText("Selected: " + selectedVideo.getName() + " (" + fileSizeMB + " MB)");
                videoFileNameLabel.setForeground(new Color(0, 150, 0));
            }
        }
    }

    private JButton createMediaButton(String icon, String text) {
        JButton button = new JButton();
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createDashedBorder(Color.BLACK, 2, 5, 5, false),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(0, 60));

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
        innerPanel.setBackground(Color.WHITE);
        innerPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        iconLabel.setAlignmentY(0.5f);

        innerPanel.add(iconLabel);
        innerPanel.add(Box.createHorizontalStrut(15));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        textLabel.setAlignmentY(0.5f);

        innerPanel.add(textLabel);
        innerPanel.add(Box.createHorizontalGlue());

        button.setLayout(new BorderLayout());
        button.add(innerPanel, BorderLayout.CENTER);

        return button;
    }

    private JPanel buildSubmitButton() {
        JPanel container = new JPanel();
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        submitButton.setBackground(Color.WHITE);
        submitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, false));
        submitButton.setFocusPainted(false);
        submitButton.setPreferredSize(new Dimension(200, 55));
        submitButton.addActionListener(e -> {
            String incidentType = (String) incidentTypeCombo.getSelectedItem();
            String description = descriptionArea.getText();
            
            if ("Select incident type".equals(incidentType)) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Please select an incident type", 
                    "Validation Error", 
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (description.trim().isEmpty() || 
                description.equals("Describe the incident in detail, include what happened, when it started and any immediate danger")) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Please provide a description", 
                    "Validation Error", 
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Get location (using user's location as default, or a placeholder)
            String location = user.getLocation() != null ? 
                "10.308813, 123.912192" : "10.308813, 123.912192"; // Default coordinates
            
            // Get file paths
            String photoPath = selectedPhoto != null ? selectedPhoto.getAbsolutePath() : null;
            String videoPath = selectedVideo != null ? selectedVideo.getAbsolutePath() : null;
            
            // Create and save the report
            com.bantayalert.data.IncidentReport report = new com.bantayalert.data.IncidentReport(
                incidentType,
                description,
                location,
                photoPath,
                videoPath,
                user.getFullName(),
                user.getEmail()
            );
            
            com.bantayalert.data.IncidentReportRepository.save(report);
            
            // Show success message
            StringBuilder reportInfo = new StringBuilder();
            reportInfo.append("Incident Report Submitted Successfully!\n\n");
            reportInfo.append("Type: ").append(incidentType).append("\n");
            reportInfo.append("Description: ").append(description).append("\n");
            
            if (selectedPhoto != null) {
                reportInfo.append("Photo: ").append(selectedPhoto.getName()).append("\n");
            }
            
            if (selectedVideo != null) {
                reportInfo.append("Video: ").append(selectedVideo.getName()).append("\n");
            }
            
            javax.swing.JOptionPane.showMessageDialog(this, 
                reportInfo.toString(), 
                "Report Submitted", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            // Reset form
            incidentTypeCombo.setSelectedIndex(0);
            descriptionArea.setText("Describe the incident in detail, include what happened, when it started and any immediate danger");
            descriptionArea.setForeground(new Color(150, 150, 150));
            selectedPhoto = null;
            selectedVideo = null;
            if (photoFileNameLabel != null) photoFileNameLabel.setText("");
            if (videoFileNameLabel != null) videoFileNameLabel.setText("");
            
            dispose();
            DashboardFrame.getOrCreateInstance(user).setVisible(true);
        });

        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.add(Box.createHorizontalGlue());
        container.add(submitButton);
        container.add(Box.createHorizontalGlue());

        return container;
    }
}
