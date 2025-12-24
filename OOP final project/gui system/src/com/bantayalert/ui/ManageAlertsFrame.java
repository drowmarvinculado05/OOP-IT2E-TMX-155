package com.bantayalert.ui;

import com.bantayalert.data.Alert;
import com.bantayalert.data.AlertRepository;
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
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Frame for managing emergency alerts (admin only).
 */
public class ManageAlertsFrame extends JFrame {
    private static final Color RED_HEADER = new Color(220, 53, 69);
    private static final Color GREEN_BUTTON = new Color(40, 167, 69);
    
    private final UserProfile user;
    private JTextField titleField;
    private JTextArea messageArea;
    private JComboBox<String> typeComboBox;
    private JComboBox<String> severityComboBox;
    private JTextField barangaysField;
    private JPanel alertsListPanel;
    private JLabel activeCountLabel;
    private JLabel criticalCountLabel;
    private JLabel disastersCountLabel;

    public ManageAlertsFrame(UserProfile user) {
        super("Bantay Alert - Manage Alerts");
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
        header.setBackground(RED_HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Left side: Back button + Title
        JPanel leftPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(RED_HEADER);
        leftPanel.setOpaque(false);

        JButton backButton = new JButton("←");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        backButton.addActionListener(e -> {
            dispose();
            new AdminDashboardFrame(user).setVisible(true);
        });

        JLabel titleLabel = new JLabel("Manage Alerts", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        leftPanel.add(backButton);
        leftPanel.add(titleLabel);

        // Right side: Add button
        JButton addButton = new JButton("+");
        addButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        addButton.addActionListener(e -> clearForm());

        header.add(leftPanel, BorderLayout.WEST);
        header.add(addButton, BorderLayout.EAST);

        return header;
    }

    private JPanel buildMainContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Create Alert Section
        content.add(buildCreateAlertSection());
        content.add(Box.createVerticalStrut(20));

        // Summary Statistics
        content.add(buildSummaryStats());
        content.add(Box.createVerticalStrut(20));

        // Current Alerts Section
        content.add(buildCurrentAlertsSection());

        return content;
    }

    private JPanel buildCreateAlertSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel sectionTitle = new JLabel("Create Alert", SwingConstants.LEFT);
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        sectionTitle.setForeground(Color.BLACK);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        section.add(sectionTitle);

        // Title field
        section.add(createFieldLabel("Title:"));
        titleField = new JTextField();
        titleField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        titleField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        titleField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        section.add(titleField);
        section.add(Box.createVerticalStrut(10));

        // Message field
        section.add(createFieldLabel("Message:"));
        messageArea = new JTextArea(3, 0);
        messageArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        section.add(messageArea);
        section.add(Box.createVerticalStrut(10));

        // Type and Severity
        JPanel typeSeverityPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        typeSeverityPanel.setBackground(Color.WHITE);

        JPanel typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));
        typePanel.setBackground(Color.WHITE);
        typePanel.add(createFieldLabel("Type:"));
        typeComboBox = new JComboBox<>(new String[]{"disaster", "announcement", "warning"});
        typeComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        typeComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        typeComboBox.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        typePanel.add(typeComboBox);

        JPanel severityPanel = new JPanel();
        severityPanel.setLayout(new BoxLayout(severityPanel, BoxLayout.Y_AXIS));
        severityPanel.setBackground(Color.WHITE);
        severityPanel.add(createFieldLabel("Severity:"));
        severityComboBox = new JComboBox<>(new String[]{"low", "medium", "high", "critical"});
        severityComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        severityComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        severityComboBox.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        severityPanel.add(severityComboBox);

        typeSeverityPanel.add(typePanel);
        typeSeverityPanel.add(severityPanel);
        section.add(typeSeverityPanel);
        section.add(Box.createVerticalStrut(10));

        // Affected Barangays
        section.add(createFieldLabel("Affected Barangays (comma-separated):"));
        barangaysField = new JTextField();
        barangaysField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        barangaysField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        barangaysField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        section.add(barangaysField);
        section.add(Box.createVerticalStrut(15));

        // Buttons
        JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cancelButton.setBackground(new Color(200, 200, 200));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.addActionListener(e -> clearForm());

        JButton saveButton = new JButton("Save Alert");
        saveButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        saveButton.setBackground(GREEN_BUTTON);
        saveButton.setForeground(Color.WHITE);
        saveButton.setOpaque(true);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setContentAreaFilled(true);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.addActionListener(e -> saveAlert());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        section.add(buttonPanel);

        return section;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(Color.BLACK);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        return label;
    }

    private JPanel buildSummaryStats() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(Color.WHITE);

        activeCountLabel = new JLabel("0 Active Alerts", SwingConstants.CENTER);
        activeCountLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        activeCountLabel.setForeground(Color.BLACK);
        activeCountLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        criticalCountLabel = new JLabel("0 Critical", SwingConstants.CENTER);
        criticalCountLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        criticalCountLabel.setForeground(Color.BLACK);
        criticalCountLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        disastersCountLabel = new JLabel("0 Disasters", SwingConstants.CENTER);
        disastersCountLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        disastersCountLabel.setForeground(Color.BLACK);
        disastersCountLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        statsPanel.add(activeCountLabel);
        statsPanel.add(criticalCountLabel);
        statsPanel.add(disastersCountLabel);

        updateSummaryStats();

        return statsPanel;
    }

    private JPanel buildCurrentAlertsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(new Color(245, 245, 245)); // Light gray instead of white
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel sectionTitle = new JLabel("Current Alerts", SwingConstants.LEFT);
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        sectionTitle.setForeground(Color.BLACK);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        section.add(sectionTitle);

        alertsListPanel = new JPanel();
        alertsListPanel.setLayout(new BoxLayout(alertsListPanel, BoxLayout.Y_AXIS));
        alertsListPanel.setBackground(new Color(245, 245, 245)); // Light gray instead of white

        refreshAlertsList();

        JScrollPane scrollPane = new JScrollPane(alertsListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 300));

        section.add(scrollPane);

        return section;
    }

    private void refreshAlertsList() {
        alertsListPanel.removeAll();
        
        java.util.List<Alert> alerts = AlertRepository.findAll();
        
        if (alerts.isEmpty()) {
            JLabel placeholder = new JLabel("No alerts yet.", SwingConstants.CENTER);
            placeholder.setFont(new Font("SansSerif", Font.PLAIN, 16));
            placeholder.setForeground(new Color(120, 120, 120));
            placeholder.setAlignmentX(0.5f);
            alertsListPanel.add(Box.createVerticalGlue());
            alertsListPanel.add(placeholder);
            alertsListPanel.add(Box.createVerticalGlue());
        } else {
            for (Alert alert : alerts) {
                alertsListPanel.add(createAlertCard(alert));
                alertsListPanel.add(Box.createVerticalStrut(10));
            }
        }

        alertsListPanel.revalidate();
        alertsListPanel.repaint();
        updateSummaryStats();
    }

    private JPanel createAlertCard(Alert alert) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(245, 245, 245)); // Light gray instead of white
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Left side: Alert details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(245, 245, 245)); // Light gray instead of white

        JLabel titleLabel = new JLabel(alert.getTitle(), SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLACK);
        detailsPanel.add(titleLabel);

        JLabel messageLabel = new JLabel(alert.getMessage(), SwingConstants.LEFT);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        detailsPanel.add(messageLabel);

        JLabel severityLabel = new JLabel("Severity: " + alert.getSeverity().toUpperCase(), SwingConstants.LEFT);
        severityLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        severityLabel.setForeground(Color.BLACK);
        detailsPanel.add(severityLabel);

        JLabel areasLabel = new JLabel("Areas: " + alert.getAffectedBarangays(), SwingConstants.LEFT);
        areasLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        areasLabel.setForeground(Color.BLACK);
        detailsPanel.add(areasLabel);

        JLabel dateLabel = new JLabel(alert.getDateCreated(), SwingConstants.LEFT);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(120, 120, 120));
        dateLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        detailsPanel.add(dateLabel);

        // Right side: Status and Delete button
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(new Color(245, 245, 245)); // Light gray instead of white

        JButton statusButton = new JButton(alert.getStatus());
        statusButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusButton.setBackground(alert.getStatus().equals("ACTIVE") ? GREEN_BUTTON : new Color(200, 200, 200));
        statusButton.setForeground(alert.getStatus().equals("ACTIVE") ? Color.WHITE : Color.BLACK);
        statusButton.setOpaque(true);
        statusButton.setFocusPainted(false);
        statusButton.setBorderPainted(false);
        statusButton.setContentAreaFilled(true);
        statusButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        statusButton.setMaximumSize(new Dimension(100, 30));
        statusButton.addActionListener(e -> {
            String newStatus = alert.getStatus().equals("ACTIVE") ? "INACTIVE" : "ACTIVE";
            Alert updated = alert.withStatus(newStatus);
            AlertRepository.save(updated);
            refreshAlertsList();
        });

        JButton deleteButton = new JButton("✕");
        deleteButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        deleteButton.setBackground(RED_HEADER);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setOpaque(true);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(true);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        deleteButton.setMaximumSize(new Dimension(40, 30));
        deleteButton.addActionListener(e -> {
            AlertRepository.delete(alert.getId());
            refreshAlertsList();
        });

        actionPanel.add(statusButton);
        actionPanel.add(Box.createVerticalStrut(5));
        actionPanel.add(deleteButton);

        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.EAST);

        return card;
    }

    private void clearForm() {
        titleField.setText("");
        messageArea.setText("");
        typeComboBox.setSelectedIndex(0);
        severityComboBox.setSelectedIndex(0);
        barangaysField.setText("");
    }

    private void saveAlert() {
        String title = titleField.getText().trim();
        String message = messageArea.getText().trim();
        String type = (String) typeComboBox.getSelectedItem();
        String severity = (String) severityComboBox.getSelectedItem();
        String barangays = barangaysField.getText().trim();

        if (title.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Title cannot be empty.",
                "Validation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (message.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Message cannot be empty.",
                "Validation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        Alert alert = new Alert(title, message, type, severity, barangays);
        AlertRepository.save(alert);

        clearForm();
        refreshAlertsList();

        javax.swing.JOptionPane.showMessageDialog(this,
            "Alert saved successfully!",
            "Success",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateSummaryStats() {
        activeCountLabel.setText(AlertRepository.countActive() + " Active Alerts");
        criticalCountLabel.setText(AlertRepository.countCritical() + " Critical");
        disastersCountLabel.setText(AlertRepository.countDisasters() + " Disasters");
    }
}

