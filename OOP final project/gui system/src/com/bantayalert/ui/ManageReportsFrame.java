package com.bantayalert.ui;

import com.bantayalert.data.IncidentReport;
import com.bantayalert.data.IncidentReportRepository;
import com.bantayalert.data.UserProfile;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

/**
 * Frame for managing incident reports (admin only).
 */
public class ManageReportsFrame extends JFrame {
    private static final Color YELLOW_HEADER = new Color(255, 193, 7);
    private static final Color BLUE_BUTTON = new Color(0, 123, 255);
    private static final Color GREEN_BUTTON = new Color(40, 167, 69);
    private static final Color RED_BUTTON = new Color(220, 53, 69);
    
    private final UserProfile user;
    private JPanel reportsListPanel;
    private JLabel pendingCountLabel;
    private JLabel verifiedCountLabel;
    private JLabel respondedCountLabel;
    private String currentTab = "All Reports";

    public ManageReportsFrame(UserProfile user) {
        super("Bantay Alert - Manage Reports");
        this.user = user;
        buildUi();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(1200, 800));
        setLocationRelativeTo(null);
    }

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildMainContent(), BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(YELLOW_HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Left side: Back button + Title
        JPanel leftPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(YELLOW_HEADER);
        leftPanel.setOpaque(false);

        JButton backButton = new JButton("←");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        backButton.addActionListener(e -> {
            dispose();
            new AdminDashboardFrame(user).setVisible(true);
        });

        JLabel titleLabel = new JLabel("Manage Reports", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);

        leftPanel.add(backButton);
        leftPanel.add(titleLabel);

        header.add(leftPanel, BorderLayout.WEST);

        return header;
    }

    private JPanel buildMainContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Navigation Tabs
        content.add(buildTabs());
        content.add(Box.createVerticalStrut(20));

        // Summary Statistics
        content.add(buildSummaryStats());
        content.add(Box.createVerticalStrut(20));

        // Reports List
        content.add(buildReportsList());

        return content;
    }

    private JPanel buildTabs() {
        JPanel tabsPanel = new JPanel(new GridLayout(1, 4, 0, 0));
        tabsPanel.setBackground(Color.WHITE);

        String[] tabNames = {"All Reports", "Pending", "Verified", "Responded"};
        for (String tabName : tabNames) {
            JButton tab = new JButton(tabName);
            tab.setFont(new Font("SansSerif", Font.PLAIN, 14));
            tab.setFocusPainted(false);
            tab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            if (tabName.equals(currentTab)) {
                tab.setBackground(YELLOW_HEADER);
                tab.setForeground(Color.BLACK);
            } else {
                tab.setBackground(Color.WHITE);
                tab.setForeground(Color.BLACK);
            }
            
            final String tabToSelect = tabName;
            tab.addActionListener(e -> {
                currentTab = tabToSelect;
                refreshReportsList();
                // Update tab colors
                for (int i = 0; i < tabsPanel.getComponentCount(); i++) {
                    JButton t = (JButton) tabsPanel.getComponent(i);
                    if (t.getText().equals(tabToSelect)) {
                        t.setBackground(YELLOW_HEADER);
                    } else {
                        t.setBackground(Color.WHITE);
                    }
                }
            });
            
            tabsPanel.add(tab);
        }

        return tabsPanel;
    }

    private JPanel buildSummaryStats() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(new Color(245, 245, 245)); // Light gray instead of white

        pendingCountLabel = new JLabel("0 Pending", SwingConstants.CENTER);
        pendingCountLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        pendingCountLabel.setForeground(Color.BLACK);
        pendingCountLabel.setOpaque(false);
        pendingCountLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        verifiedCountLabel = new JLabel("0 Verified", SwingConstants.CENTER);
        verifiedCountLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        verifiedCountLabel.setForeground(Color.BLACK);
        verifiedCountLabel.setOpaque(false);
        verifiedCountLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        respondedCountLabel = new JLabel("0 Responded", SwingConstants.CENTER);
        respondedCountLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        respondedCountLabel.setForeground(Color.BLACK);
        respondedCountLabel.setOpaque(false);
        respondedCountLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        statsPanel.add(pendingCountLabel);
        statsPanel.add(verifiedCountLabel);
        statsPanel.add(respondedCountLabel);

        updateSummaryStats();

        return statsPanel;
    }

    private JPanel buildReportsList() {
        reportsListPanel = new JPanel();
        reportsListPanel.setLayout(new BoxLayout(reportsListPanel, BoxLayout.Y_AXIS));
        reportsListPanel.setBackground(Color.WHITE);

        refreshReportsList();

        JScrollPane scrollPane = new JScrollPane(reportsListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 400));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        
        return wrapper;
    }

    private void refreshReportsList() {
        reportsListPanel.removeAll();
        
        java.util.List<IncidentReport> reports;
        if ("All Reports".equals(currentTab)) {
            reports = IncidentReportRepository.findAll();
        } else if ("Pending".equals(currentTab)) {
            reports = IncidentReportRepository.findByStatus("PENDING");
        } else if ("Verified".equals(currentTab)) {
            reports = IncidentReportRepository.findByStatus("VERIFIED");
        } else if ("Responded".equals(currentTab)) {
            reports = IncidentReportRepository.findByStatus("RESPONDED");
        } else {
            reports = java.util.Collections.emptyList();
        }

        if (reports.isEmpty()) {
            JLabel placeholder = new JLabel("No reports found.", SwingConstants.CENTER);
            placeholder.setFont(new Font("SansSerif", Font.PLAIN, 18));
            placeholder.setForeground(new Color(120, 120, 120));
            placeholder.setAlignmentX(0.5f);
            reportsListPanel.add(Box.createVerticalGlue());
            reportsListPanel.add(placeholder);
            reportsListPanel.add(Box.createVerticalGlue());
        } else {
            for (IncidentReport report : reports) {
                reportsListPanel.add(createReportCard(report));
                reportsListPanel.add(Box.createVerticalStrut(15));
            }
        }

        reportsListPanel.revalidate();
        reportsListPanel.repaint();
        updateSummaryStats();
    }

    private JPanel createReportCard(IncidentReport report) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Left side: Icon and details
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);

        // Icon
        JLabel iconLabel;
        if ("Accident".equalsIgnoreCase(report.getType()) || "Other".equalsIgnoreCase(report.getType())) {
            String icon = "Accident".equalsIgnoreCase(report.getType()) ? "❓" : "⚠";
            iconLabel = new JLabel(icon, SwingConstants.CENTER);
            iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 40));
            iconLabel.setForeground(YELLOW_HEADER);
        } else {
            iconLabel = new JLabel("⚠", SwingConstants.CENTER);
            iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 40));
            iconLabel.setForeground(YELLOW_HEADER);
        }
        iconLabel.setPreferredSize(new Dimension(60, 60));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        // Details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);

        JLabel typeLabel = new JLabel(report.getType(), SwingConstants.LEFT);
        typeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        typeLabel.setForeground(Color.BLACK);
        detailsPanel.add(typeLabel);

        JLabel dateLabel = new JLabel(report.getDateTime(), SwingConstants.LEFT);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        dateLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        detailsPanel.add(dateLabel);

        JLabel descLabel = new JLabel("<html><body style='width: 500px'>" + report.getDescription() + "</body></html>", SwingConstants.LEFT);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descLabel.setForeground(Color.BLACK);
        descLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        detailsPanel.add(descLabel);

        JLabel locationLabel = new JLabel("📍 " + report.getLocation(), SwingConstants.LEFT);
        locationLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        locationLabel.setForeground(new Color(100, 100, 100));
        locationLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        detailsPanel.add(locationLabel);

        // Attached Photos
        if (report.getPhotoPath() != null && !report.getPhotoPath().isEmpty()) {
            JPanel photoPanel = new JPanel(new BorderLayout());
            photoPanel.setBackground(Color.WHITE);
            photoPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            
            JLabel photoLabel = new JLabel("Attached Photos:", SwingConstants.LEFT);
            photoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            photoLabel.setForeground(new Color(100, 100, 100));
            
            try {
                File photoFile = new File(report.getPhotoPath());
                if (photoFile.exists()) {
                    ImageIcon originalIcon = new ImageIcon(photoFile.getAbsolutePath());
                    Image originalImage = originalIcon.getImage();
                    
                    // Create larger thumbnail (max 300x300 for better visibility)
                    int thumbWidth = 300;
                    int thumbHeight = 300;
                    double scale = Math.min((double) thumbWidth / originalIcon.getIconWidth(),
                                          (double) thumbHeight / originalIcon.getIconHeight());
                    int scaledWidth = (int) (originalIcon.getIconWidth() * scale);
                    int scaledHeight = (int) (originalIcon.getIconHeight() * scale);
                    
                    Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                    ImageIcon thumbnailIcon = new ImageIcon(scaledImage);
                    
                    JLabel photoThumbnail = new JLabel(thumbnailIcon);
                    photoThumbnail.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));
                    photoThumbnail.setToolTipText(photoFile.getName());
                    
                    // Make thumbnail clickable to view full size
                    photoThumbnail.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
                    final File imageFile = photoFile;
                    photoThumbnail.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            ManageReportsFrame.this.showFullSizeImage(imageFile);
                        }
                    });
                    
                    photoPanel.add(photoLabel, BorderLayout.NORTH);
                    photoPanel.add(photoThumbnail, BorderLayout.CENTER);
                } else {
                    JLabel errorLabel = new JLabel("📷 Photo file not found: " + photoFile.getName(), SwingConstants.LEFT);
                    errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    errorLabel.setForeground(new Color(200, 0, 0));
                    errorLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
                    photoPanel.add(photoLabel, BorderLayout.NORTH);
                    photoPanel.add(errorLabel, BorderLayout.CENTER);
                }
            } catch (Exception e) {
                JLabel errorLabel = new JLabel("📷 Error loading photo: " + new File(report.getPhotoPath()).getName(), SwingConstants.LEFT);
                errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                errorLabel.setForeground(new Color(200, 0, 0));
                errorLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
                photoPanel.add(photoLabel, BorderLayout.NORTH);
                photoPanel.add(errorLabel, BorderLayout.CENTER);
            }
            
            detailsPanel.add(photoPanel);
        }

        // Attached Videos
        if (report.getVideoPath() != null && !report.getVideoPath().isEmpty()) {
            JPanel videoPanel = new JPanel(new BorderLayout());
            videoPanel.setBackground(Color.WHITE);
            videoPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            
            JLabel videoLabel = new JLabel("Attached Videos:", SwingConstants.LEFT);
            videoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            videoLabel.setForeground(new Color(100, 100, 100));
            
            try {
                File videoFile = new File(report.getVideoPath());
                if (videoFile.exists()) {
                    // Create a video thumbnail panel with play button overlay
                    JPanel videoThumbnailPanel = new JPanel(new BorderLayout());
                    videoThumbnailPanel.setBackground(new Color(30, 30, 30));
                    videoThumbnailPanel.setPreferredSize(new Dimension(150, 100));
                    videoThumbnailPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));
                    
                    // Video icon/thumbnail representation
                    JLabel videoIcon = new JLabel("🎥", SwingConstants.CENTER);
                    videoIcon.setFont(new Font("SansSerif", Font.PLAIN, 40));
                    videoIcon.setForeground(Color.WHITE);
                    
                    // Play button overlay
                    JLabel playButton = new JLabel("▶", SwingConstants.CENTER);
                    playButton.setFont(new Font("SansSerif", Font.PLAIN, 30));
                    playButton.setForeground(Color.WHITE);
                    playButton.setOpaque(false);
                    
                    JPanel centerPanel = new JPanel(new BorderLayout());
                    centerPanel.setOpaque(false);
                    centerPanel.add(videoIcon, BorderLayout.CENTER);
                    centerPanel.add(playButton, BorderLayout.CENTER);
                    
                    videoThumbnailPanel.add(centerPanel, BorderLayout.CENTER);
                    
                    // File name below thumbnail
                    JLabel fileNameLabel = new JLabel(videoFile.getName(), SwingConstants.CENTER);
                    fileNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
                    fileNameLabel.setForeground(new Color(100, 100, 100));
                    fileNameLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
                    
                    JPanel videoContainer = new JPanel();
                    videoContainer.setLayout(new BoxLayout(videoContainer, BoxLayout.Y_AXIS));
                    videoContainer.setBackground(Color.WHITE);
                    videoContainer.add(videoThumbnailPanel);
                    videoContainer.add(fileNameLabel);
                    
                    videoPanel.add(videoLabel, BorderLayout.NORTH);
                    videoPanel.add(videoContainer, BorderLayout.CENTER);
                } else {
                    JLabel errorLabel = new JLabel("🎥 Video file not found: " + videoFile.getName(), SwingConstants.LEFT);
                    errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    errorLabel.setForeground(new Color(200, 0, 0));
                    errorLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
                    videoPanel.add(videoLabel, BorderLayout.NORTH);
                    videoPanel.add(errorLabel, BorderLayout.CENTER);
                }
            } catch (Exception e) {
                JLabel errorLabel = new JLabel("🎥 Error loading video: " + new File(report.getVideoPath()).getName(), SwingConstants.LEFT);
                errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                errorLabel.setForeground(new Color(200, 0, 0));
                errorLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
                videoPanel.add(videoLabel, BorderLayout.NORTH);
                videoPanel.add(errorLabel, BorderLayout.CENTER);
            }
            
            detailsPanel.add(videoPanel);
        }

        JPanel iconDetailsPanel = new JPanel(new BorderLayout());
        iconDetailsPanel.setBackground(Color.WHITE);
        iconDetailsPanel.add(iconLabel, BorderLayout.WEST);
        iconDetailsPanel.add(detailsPanel, BorderLayout.CENTER);

        leftPanel.add(iconDetailsPanel, BorderLayout.CENTER);

        // Right side: Status and Action buttons
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(245, 245, 245)); // Light gray instead of white

        // Status tag
        Color statusColor;
        if ("PENDING".equals(report.getStatus())) {
            statusColor = YELLOW_HEADER;
        } else if ("VERIFIED".equals(report.getStatus())) {
            statusColor = BLUE_BUTTON;
        } else {
            statusColor = GREEN_BUTTON;
        }

        JLabel statusLabel = new JLabel(report.getStatus(), SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBackground(statusColor);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        statusLabel.setPreferredSize(new Dimension(100, 30));
        rightPanel.add(statusLabel);
        rightPanel.add(Box.createVerticalStrut(10));

        // Action buttons
        if (!"RESPONDED".equals(report.getStatus())) {
            JButton verifyButton = new JButton("✓ Verify");
            verifyButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
            verifyButton.setBackground(BLUE_BUTTON);
            verifyButton.setForeground(Color.WHITE);
            verifyButton.setOpaque(true);
            verifyButton.setFocusPainted(false);
            verifyButton.setBorderPainted(false);
            verifyButton.setContentAreaFilled(true);
            verifyButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            verifyButton.setMaximumSize(new Dimension(120, 35));
            verifyButton.addActionListener(e -> {
                IncidentReport updated = report.withStatus("VERIFIED");
                IncidentReportRepository.save(updated);
                refreshReportsList();
            });
            rightPanel.add(verifyButton);
            rightPanel.add(Box.createVerticalStrut(5));
        }

        if (!"RESPONDED".equals(report.getStatus())) {
            JButton respondButton = new JButton("✓ Respond");
            respondButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
            respondButton.setBackground(GREEN_BUTTON);
            respondButton.setForeground(Color.WHITE);
            respondButton.setOpaque(true);
            respondButton.setFocusPainted(false);
            respondButton.setBorderPainted(false);
            respondButton.setContentAreaFilled(true);
            respondButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            respondButton.setMaximumSize(new Dimension(120, 35));
            respondButton.addActionListener(e -> {
                IncidentReport updated = report.withStatus("RESPONDED");
                IncidentReportRepository.save(updated);
                refreshReportsList();
            });
            rightPanel.add(respondButton);
            rightPanel.add(Box.createVerticalStrut(5));
        }

        JButton dismissButton = new JButton("X Dismiss");
        dismissButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dismissButton.setBackground(RED_BUTTON);
        dismissButton.setForeground(Color.WHITE);
        dismissButton.setOpaque(true);
        dismissButton.setFocusPainted(false);
        dismissButton.setBorderPainted(false);
        dismissButton.setContentAreaFilled(true);
        dismissButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        dismissButton.setMaximumSize(new Dimension(120, 35));
        dismissButton.addActionListener(e -> {
            IncidentReportRepository.delete(report.getId());
            refreshReportsList();
        });
        rightPanel.add(dismissButton);

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    private void updateSummaryStats() {
        pendingCountLabel.setText(IncidentReportRepository.countPending() + " Pending");
        verifiedCountLabel.setText(IncidentReportRepository.countVerified() + " Verified");
        respondedCountLabel.setText(IncidentReportRepository.countResponded() + " Responded");
    }
    
    private void showFullSizeImage(File imageFile) {
        try {
            ImageIcon fullIcon = new ImageIcon(imageFile.getAbsolutePath());
            Image fullImage = fullIcon.getImage();
            
            // Limit max display size to screen size
            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            int maxWidth = (int) (screenSize.width * 0.8);
            int maxHeight = (int) (screenSize.height * 0.8);
            
            int imgWidth = fullIcon.getIconWidth();
            int imgHeight = fullIcon.getIconHeight();
            
            double scale = Math.min((double) maxWidth / imgWidth, (double) maxHeight / imgHeight);
            if (scale > 1.0) scale = 1.0; // Don't upscale
            
            int displayWidth = (int) (imgWidth * scale);
            int displayHeight = (int) (imgHeight * scale);
            
            Image displayImage = fullImage.getScaledInstance(displayWidth, displayHeight, Image.SCALE_SMOOTH);
            ImageIcon displayIcon = new ImageIcon(displayImage);
            
            JFrame imageFrame = new JFrame("Image: " + imageFile.getName());
            JLabel imageLabel = new JLabel(displayIcon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            
            JScrollPane scrollPane = new JScrollPane(imageLabel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            
            imageFrame.add(scrollPane);
            imageFrame.setSize(displayWidth + 50, displayHeight + 50);
            imageFrame.setLocationRelativeTo(this);
            imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            imageFrame.setVisible(true);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Error displaying image: " + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}

