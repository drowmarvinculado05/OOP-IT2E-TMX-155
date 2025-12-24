package com.bantayalert.ui;

import com.bantayalert.data.EvacuationCenter;
import com.bantayalert.data.EvacuationCenterRepository;
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
 * Frame for managing evacuation centers (admin only).
 */
public class ManageEvacuationCentersFrame extends JFrame {
    private static final Color GREEN_HEADER = new Color(40, 167, 69);
    private static final Color GREEN_BUTTON = new Color(40, 167, 69);
    
    private final UserProfile user;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField barangayField;
    private JTextField capacityField;
    private JTextField facilitiesField;
    private JTextField contactPersonField;
    private JTextField contactNumberField;
    private JPanel centersListPanel;
    private JLabel openCountLabel;
    private JLabel fullCountLabel;
    private JLabel totalEvacueesLabel;
    private JLabel totalCapacityLabel;
    private JPanel createFormPanel;

    public ManageEvacuationCentersFrame(UserProfile user) {
        super("Bantay Alert - Evacuation Centers");
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
        header.setBackground(GREEN_HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Left side: Back button + Title
        JPanel leftPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(GREEN_HEADER);
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

        JLabel titleLabel = new JLabel("Evacuation Centers", SwingConstants.LEFT);
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
        addButton.addActionListener(e -> toggleCreateForm());

        header.add(leftPanel, BorderLayout.WEST);
        header.add(addButton, BorderLayout.EAST);

        return header;
    }

    private JPanel buildMainContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Summary Statistics
        content.add(buildSummaryStats());
        content.add(Box.createVerticalStrut(20));

        // Create Form (initially hidden)
        createFormPanel = buildCreateForm();
        createFormPanel.setVisible(false);
        content.add(createFormPanel);
        content.add(Box.createVerticalStrut(20));

        // Manage Centers Section
        content.add(buildManageCentersSection());

        return content;
    }

    private JPanel buildSummaryStats() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(Color.WHITE);

        openCountLabel = new JLabel("0 Open", SwingConstants.CENTER);
        openCountLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        openCountLabel.setForeground(Color.BLACK);
        openCountLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        fullCountLabel = new JLabel("0 Full", SwingConstants.CENTER);
        fullCountLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        fullCountLabel.setForeground(Color.BLACK);
        fullCountLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        totalEvacueesLabel = new JLabel("0 Total Evacuees", SwingConstants.CENTER);
        totalEvacueesLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        totalEvacueesLabel.setForeground(Color.BLACK);
        totalEvacueesLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        totalCapacityLabel = new JLabel("0 Total Capacity", SwingConstants.CENTER);
        totalCapacityLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        totalCapacityLabel.setForeground(Color.BLACK);
        totalCapacityLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        statsPanel.add(openCountLabel);
        statsPanel.add(fullCountLabel);
        statsPanel.add(totalEvacueesLabel);
        statsPanel.add(totalCapacityLabel);

        updateSummaryStats();

        return statsPanel;
    }

    private JPanel buildCreateForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel formTitle = new JLabel("Add Evacuation Center", SwingConstants.LEFT);
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        formTitle.setForeground(Color.BLACK);
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        form.add(formTitle);

        // Name
        form.add(createFieldLabel("Name:"));
        nameField = new JTextField();
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        nameField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        form.add(nameField);
        form.add(Box.createVerticalStrut(10));

        // Address
        form.add(createFieldLabel("Address:"));
        addressField = new JTextField();
        addressField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        addressField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        addressField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        form.add(addressField);
        form.add(Box.createVerticalStrut(10));

        // Barangay
        form.add(createFieldLabel("Barangay:"));
        barangayField = new JTextField();
        barangayField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        barangayField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        barangayField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        form.add(barangayField);
        form.add(Box.createVerticalStrut(10));

        // Capacity
        form.add(createFieldLabel("Capacity:"));
        capacityField = new JTextField();
        capacityField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        capacityField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        capacityField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        form.add(capacityField);
        form.add(Box.createVerticalStrut(10));

        // Facilities
        form.add(createFieldLabel("Facilities (comma separated):"));
        facilitiesField = new JTextField();
        facilitiesField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        facilitiesField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        facilitiesField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        form.add(facilitiesField);
        form.add(Box.createVerticalStrut(10));

        // Contact Person
        form.add(createFieldLabel("Contact Person:"));
        contactPersonField = new JTextField();
        contactPersonField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contactPersonField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        contactPersonField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        form.add(contactPersonField);
        form.add(Box.createVerticalStrut(10));

        // Contact Number
        form.add(createFieldLabel("Contact Number:"));
        contactNumberField = new JTextField();
        contactNumberField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contactNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        contactNumberField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        form.add(contactNumberField);
        form.add(Box.createVerticalStrut(15));

        // Buttons
        JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cancelButton.setBackground(new Color(200, 200, 200));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.addActionListener(e -> {
            clearForm();
            toggleCreateForm();
        });

        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        saveButton.setBackground(GREEN_BUTTON);
        saveButton.setForeground(Color.WHITE);
        saveButton.setOpaque(true);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setContentAreaFilled(true);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.addActionListener(e -> saveCenter());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        form.add(buttonPanel);

        return form;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(Color.BLACK);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        return label;
    }

    private JPanel buildManageCentersSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel sectionTitle = new JLabel("Manage Centers", SwingConstants.LEFT);
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        sectionTitle.setForeground(Color.BLACK);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        section.add(sectionTitle);

        centersListPanel = new JPanel();
        centersListPanel.setLayout(new BoxLayout(centersListPanel, BoxLayout.Y_AXIS));
        centersListPanel.setBackground(Color.WHITE);

        refreshCentersList();

        JScrollPane scrollPane = new JScrollPane(centersListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 300));

        section.add(scrollPane);

        return section;
    }

    private void refreshCentersList() {
        centersListPanel.removeAll();
        
        java.util.List<EvacuationCenter> centers = EvacuationCenterRepository.findAll();
        
        if (centers.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(Color.WHITE);
            
            JLabel houseIcon = new JLabel("🏠", SwingConstants.CENTER);
            houseIcon.setFont(new Font("SansSerif", Font.PLAIN, 80));
            houseIcon.setForeground(new Color(200, 200, 200));
            
            JLabel messageLabel = new JLabel("No centers found. Tap + to add one.", SwingConstants.CENTER);
            messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            messageLabel.setForeground(new Color(120, 120, 120));
            messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
            centerPanel.setBackground(Color.WHITE);
            centerPanel.add(Box.createVerticalGlue());
            centerPanel.add(houseIcon);
            centerPanel.add(messageLabel);
            centerPanel.add(Box.createVerticalGlue());
            
            emptyPanel.add(centerPanel, BorderLayout.CENTER);
            centersListPanel.add(emptyPanel);
        } else {
            for (EvacuationCenter center : centers) {
                centersListPanel.add(createCenterCard(center));
                centersListPanel.add(Box.createVerticalStrut(10));
            }
        }

        centersListPanel.revalidate();
        centersListPanel.repaint();
        updateSummaryStats();
    }

    private JPanel createCenterCard(EvacuationCenter center) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Left side: Center details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(center.getName(), SwingConstants.LEFT);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setForeground(Color.BLACK);
        detailsPanel.add(nameLabel);

        JLabel addressLabel = new JLabel("📍 " + center.getAddress(), SwingConstants.LEFT);
        addressLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        addressLabel.setForeground(Color.BLACK);
        addressLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        detailsPanel.add(addressLabel);

        JLabel barangayLabel = new JLabel("🏘️ Barangay: " + center.getBarangay(), SwingConstants.LEFT);
        barangayLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        barangayLabel.setForeground(Color.BLACK);
        detailsPanel.add(barangayLabel);

        JLabel capacityLabel = new JLabel("👥 Capacity: " + center.getCurrentEvacuees() + "/" + center.getCapacity(), SwingConstants.LEFT);
        capacityLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        capacityLabel.setForeground(Color.BLACK);
        detailsPanel.add(capacityLabel);

        if (center.getFacilities() != null && !center.getFacilities().trim().isEmpty()) {
            JLabel facilitiesLabel = new JLabel("🏢 Facilities: " + center.getFacilities(), SwingConstants.LEFT);
            facilitiesLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            facilitiesLabel.setForeground(Color.BLACK);
            detailsPanel.add(facilitiesLabel);
        }

        JLabel contactLabel = new JLabel("📞 " + center.getContactPerson() + " - " + center.getContactNumber(), SwingConstants.LEFT);
        contactLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        contactLabel.setForeground(Color.BLACK);
        contactLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        detailsPanel.add(contactLabel);

        // Right side: Status and Delete button
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(Color.WHITE);

        JButton statusButton = new JButton(center.getStatus());
        statusButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusButton.setBackground(center.getStatus().equals("OPEN") ? GREEN_BUTTON : new Color(255, 193, 7));
        statusButton.setForeground(Color.WHITE);
        statusButton.setFocusPainted(false);
        statusButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        statusButton.setMaximumSize(new Dimension(100, 30));
        statusButton.addActionListener(e -> {
            String newStatus = center.getStatus().equals("OPEN") ? "FULL" : "OPEN";
            EvacuationCenter updated = center.withStatus(newStatus);
            EvacuationCenterRepository.save(updated);
            refreshCentersList();
        });

        JButton deleteButton = new JButton("✕");
        deleteButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        deleteButton.setMaximumSize(new Dimension(40, 30));
        deleteButton.addActionListener(e -> {
            EvacuationCenterRepository.delete(center.getId());
            refreshCentersList();
        });

        actionPanel.add(statusButton);
        actionPanel.add(Box.createVerticalStrut(5));
        actionPanel.add(deleteButton);

        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.EAST);

        return card;
    }

    private void toggleCreateForm() {
        createFormPanel.setVisible(!createFormPanel.isVisible());
        if (!createFormPanel.isVisible()) {
            clearForm();
        }
    }

    private void clearForm() {
        nameField.setText("");
        addressField.setText("");
        barangayField.setText("");
        capacityField.setText("");
        facilitiesField.setText("");
        contactPersonField.setText("");
        contactNumberField.setText("");
    }

    private void saveCenter() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String barangay = barangayField.getText().trim();
        String capacityStr = capacityField.getText().trim();
        String facilities = facilitiesField.getText().trim();
        String contactPerson = contactPersonField.getText().trim();
        String contactNumber = contactNumberField.getText().trim();

        if (name.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Name cannot be empty.",
                "Validation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (address.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Address cannot be empty.",
                "Validation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (barangay.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Barangay cannot be empty.",
                "Validation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
            if (capacity <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Capacity must be a positive number.",
                "Validation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (contactPerson.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Contact Person cannot be empty.",
                "Validation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (contactNumber.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Contact Number cannot be empty.",
                "Validation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        EvacuationCenter center = new EvacuationCenter(name, address, barangay, capacity, 
                                                       facilities, contactPerson, contactNumber);
        EvacuationCenterRepository.save(center);

        clearForm();
        toggleCreateForm();
        refreshCentersList();

        javax.swing.JOptionPane.showMessageDialog(this,
            "Evacuation center saved successfully!",
            "Success",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateSummaryStats() {
        openCountLabel.setText(EvacuationCenterRepository.countOpen() + " Open");
        fullCountLabel.setText(EvacuationCenterRepository.countFull() + " Full");
        totalEvacueesLabel.setText(EvacuationCenterRepository.totalEvacuees() + " Total Evacuees");
        totalCapacityLabel.setText(EvacuationCenterRepository.totalCapacity() + " Total Capacity");
    }
}

