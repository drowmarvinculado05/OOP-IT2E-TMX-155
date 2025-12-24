package com.bantayalert.ui;

import com.bantayalert.data.IncidentReportRepository;
import com.bantayalert.data.UserProfile;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;

/**
 * Frame for analytics and reports (admin only).
 */
public class AnalyticsReportsFrame extends JFrame {
    private static final Color BLUE_HEADER = new Color(0, 123, 255);
    private static final Color GREEN_BUTTON = new Color(40, 167, 69);
    private static final Color RED_CARD = new Color(220, 53, 69);
    private static final Color BLUE_CARD = new Color(0, 123, 255);
    private static final Color GREEN_CARD = new Color(40, 167, 69);
    private static final Color ORANGE = new Color(255, 152, 0);
    
    private final UserProfile user;
    private String selectedPeriod = "Month";

    public AnalyticsReportsFrame(UserProfile user) {
        super("Bantay Alert - Analytics & Reports");
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
        header.setBackground(BLUE_HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Left side: Back button + Title
        JPanel leftPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(BLUE_HEADER);
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

        JLabel titleLabel = new JLabel("Analytics & Reports", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        leftPanel.add(backButton);
        leftPanel.add(titleLabel);

        // Right side: Export as Excel button
        JButton exportExcelButton = new JButton();
        exportExcelButton.setLayout(new BorderLayout());
        exportExcelButton.setBackground(GREEN_BUTTON);
        exportExcelButton.setForeground(Color.WHITE);
        exportExcelButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        exportExcelButton.setOpaque(true);
        exportExcelButton.setFocusPainted(false);
        exportExcelButton.setBorderPainted(false);
        exportExcelButton.setContentAreaFilled(true);
        exportExcelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exportExcelButton.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        exportExcelButton.addActionListener(e -> exportAsExcel());

        JPanel buttonLeftPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        buttonLeftPanel.setBackground(GREEN_BUTTON);
        buttonLeftPanel.setOpaque(false);

        JLabel excelIcon = new JLabel("📊", SwingConstants.LEFT);
        excelIcon.setFont(new Font("SansSerif", Font.PLAIN, 18));
        excelIcon.setForeground(Color.WHITE);

        JLabel buttonText = new JLabel("Export as Excel!", SwingConstants.LEFT);
        buttonText.setFont(new Font("SansSerif", Font.BOLD, 14));
        buttonText.setForeground(Color.WHITE);

        buttonLeftPanel.add(excelIcon);
        buttonLeftPanel.add(buttonText);

        JLabel downloadIcon = new JLabel("↓", SwingConstants.RIGHT);
        downloadIcon.setFont(new Font("SansSerif", Font.PLAIN, 18));
        downloadIcon.setForeground(Color.WHITE);

        exportExcelButton.add(buttonLeftPanel, BorderLayout.WEST);
        exportExcelButton.add(downloadIcon, BorderLayout.EAST);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(exportExcelButton, BorderLayout.EAST);

        return header;
    }

    private JPanel buildMainContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Time Period Selection
        content.add(buildTimePeriodSelector());
        content.add(Box.createVerticalStrut(20));

        // Key Metrics
        content.add(buildKeyMetrics());
        content.add(Box.createVerticalStrut(20));

        // Report Status Overview
        content.add(buildReportStatusOverview());
        content.add(Box.createVerticalStrut(20));

        // Reports Trend
        content.add(buildReportsTrend());
        content.add(Box.createVerticalStrut(20));

        // Export Reports
        content.add(buildExportSection());

        return content;
    }

    private JPanel buildTimePeriodSelector() {
        JPanel selector = new JPanel(new java.awt.GridLayout(1, 3, 10, 0));
        selector.setBackground(Color.WHITE);

        String[] periods = {"Week", "Month", "Year"};
        for (String period : periods) {
            JButton periodButton = new JButton(period);
            periodButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
            periodButton.setFocusPainted(false);
            periodButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            
            if (period.equals(selectedPeriod)) {
                periodButton.setBackground(BLUE_HEADER);
                periodButton.setForeground(Color.WHITE);
            } else {
                periodButton.setBackground(Color.WHITE);
                periodButton.setForeground(Color.BLACK);
                periodButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 1, false),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
            
            final String periodToSelect = period;
            periodButton.addActionListener(e -> {
                selectedPeriod = periodToSelect;
                // Refresh the view
                JPanel root = (JPanel) getContentPane();
                root.remove(1);
                root.add(buildMainContent(), BorderLayout.CENTER);
                root.revalidate();
                root.repaint();
            });
            
            selector.add(periodButton);
        }

        return selector;
    }

    private JPanel buildKeyMetrics() {
        JPanel metrics = new JPanel(new java.awt.GridLayout(1, 3, 15, 0));
        metrics.setBackground(Color.WHITE);

        int totalReports = IncidentReportRepository.findAll().size();
        int respondedReports = IncidentReportRepository.countResponded();
        double resolutionRate = totalReports > 0 ? (double) respondedReports / totalReports * 100 : 0;

        metrics.add(createMetricCard("⚠", String.valueOf(totalReports), "Total Reports", RED_CARD));
        metrics.add(createMetricCard("✓", String.valueOf(respondedReports), "Responded", BLUE_CARD));
        metrics.add(createMetricCard("✓", String.format("%.0f%%", resolutionRate), "Resolution Rate", GREEN_CARD));

        return metrics;
    }

    private JPanel createMetricCard(String icon, String value, String label, Color cardColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 36));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(0.5f);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(0.5f);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        JLabel labelLabel = new JLabel(label, SwingConstants.CENTER);
        labelLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        labelLabel.setForeground(Color.WHITE);
        labelLabel.setAlignmentX(0.5f);

        card.add(iconLabel);
        card.add(valueLabel);
        card.add(labelLabel);

        return card;
    }

    private JPanel buildReportStatusOverview() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Report Status Overview", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        section.add(titleLabel);

        // Legend
        JPanel legendPanel = new JPanel(new java.awt.GridLayout(1, 4, 20, 0));
        legendPanel.setBackground(Color.WHITE);

        legendPanel.add(createLegendItem(GREEN_CARD, "Responded"));
        legendPanel.add(createLegendItem(ORANGE, "Pending"));
        legendPanel.add(createLegendItem(BLUE_CARD, "Verified"));
        legendPanel.add(createLegendItem(RED_CARD, "Dismissed"));

        section.add(legendPanel);
        section.add(Box.createVerticalStrut(15));

        // Pie Chart
        JPanel pieChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int size = Math.min(width, height) - 40;
                int x = (width - size) / 2;
                int y = (height - size) / 2;

                int totalReports = IncidentReportRepository.findAll().size();
                int responded = IncidentReportRepository.countResponded();
                int pending = IncidentReportRepository.countPending();
                int verified = IncidentReportRepository.countVerified();
                int dismissed = totalReports - responded - pending - verified;

                double startAngle = 0;
                
                // Responded (Green)
                if (responded > 0) {
                    double angle = (double) responded / totalReports * 360;
                    g2.setColor(GREEN_CARD);
                    g2.fillArc(x, y, size, size, (int) startAngle, (int) angle);
                    startAngle += angle;
                }
                
                // Pending (Orange)
                if (pending > 0) {
                    double angle = (double) pending / totalReports * 360;
                    g2.setColor(ORANGE);
                    g2.fillArc(x, y, size, size, (int) startAngle, (int) angle);
                    startAngle += angle;
                }
                
                // Verified (Blue)
                if (verified > 0) {
                    double angle = (double) verified / totalReports * 360;
                    g2.setColor(BLUE_CARD);
                    g2.fillArc(x, y, size, size, (int) startAngle, (int) angle);
                    startAngle += angle;
                }
                
                // Dismissed (Red)
                if (dismissed > 0) {
                    double angle = (double) dismissed / totalReports * 360;
                    g2.setColor(RED_CARD);
                    g2.fillArc(x, y, size, size, (int) startAngle, (int) angle);
                }

                g2.dispose();
            }
        };
        pieChartPanel.setBackground(Color.WHITE);
        pieChartPanel.setPreferredSize(new Dimension(300, 300));
        pieChartPanel.setMinimumSize(new Dimension(300, 300));

        section.add(pieChartPanel);

        return section;
    }

    private JPanel createLegendItem(Color color, String label) {
        JPanel item = new JPanel();
        item.setLayout(new BoxLayout(item, BoxLayout.X_AXIS));
        item.setBackground(Color.WHITE);

        JLabel colorBox = new JLabel();
        colorBox.setBackground(color);
        colorBox.setOpaque(true);
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setMaximumSize(new Dimension(20, 20));
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));

        JLabel labelText = new JLabel(label, SwingConstants.LEFT);
        labelText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelText.setForeground(Color.BLACK);
        labelText.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        item.add(colorBox);
        item.add(labelText);

        return item;
    }

    private JPanel buildReportsTrend() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Reports Trend", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        section.add(titleLabel);

        // Line Chart
        JPanel lineChartPanel = new JPanel() {
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

                // Get reports data (simplified - using current month data)
                List<com.bantayalert.data.IncidentReport> allReports = IncidentReportRepository.findAll();
                int[] monthlyCounts = new int[6]; // Apr, May, Jun, Jul, Aug, Sep
                
                // Count reports by month (simplified - using current date)
                for (com.bantayalert.data.IncidentReport report : allReports) {
                    String dateTime = report.getDateTime();
                    // Simple check - if contains "9/" it's September
                    if (dateTime.contains("9/")) {
                        monthlyCounts[5]++;
                    } else if (dateTime.contains("8/")) {
                        monthlyCounts[4]++;
                    } else if (dateTime.contains("7/")) {
                        monthlyCounts[3]++;
                    } else if (dateTime.contains("6/")) {
                        monthlyCounts[2]++;
                    } else if (dateTime.contains("5/")) {
                        monthlyCounts[1]++;
                    } else if (dateTime.contains("4/")) {
                        monthlyCounts[0]++;
                    }
                }

                // Find max value for scaling
                int maxValue = 0;
                for (int count : monthlyCounts) {
                    if (count > maxValue) maxValue = count;
                }
                if (maxValue == 0) maxValue = 4; // Default max

                // Draw line graph
                g2.setColor(new Color(255, 100, 100)); // Light red
                g2.setStroke(new java.awt.BasicStroke(2));
                
                int[] xPoints = new int[6];
                int[] yPoints = new int[6];
                for (int i = 0; i < 6; i++) {
                    xPoints[i] = margin + 15 + (i * (chartWidth - 30) / 5);
                    yPoints[i] = margin + chartHeight - (int)((double) monthlyCounts[i] / maxValue * chartHeight);
                }
                
                for (int i = 0; i < xPoints.length - 1; i++) {
                    g2.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
                    // Draw data points
                    g2.fillOval(xPoints[i] - 3, yPoints[i] - 3, 6, 6);
                }
                g2.fillOval(xPoints[5] - 3, yPoints[5] - 3, 6, 6);

                // Draw legend
                g2.setColor(new Color(255, 100, 100));
                g2.fillRect(margin + chartWidth - 80, margin, 15, 15);
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.drawString("Reports", margin + chartWidth - 60, margin + 12);

                // X-axis labels
                String[] months = {"Apr", "May", "Jun", "Jul", "Aug", "Sep"};
                for (int i = 0; i < months.length; i++) {
                    g2.drawString(months[i], xPoints[i] - 10, margin + chartHeight + 20);
                }

                // Y-axis labels
                for (int i = 0; i <= 4; i++) {
                    int y = margin + chartHeight - (i * chartHeight / 4);
                    String label = String.format("%.1f", (double) i * maxValue / 4);
                    g2.drawString(label, margin - 30, y + 5);
                }

                g2.dispose();
            }
        };
        lineChartPanel.setBackground(Color.WHITE);
        lineChartPanel.setPreferredSize(new Dimension(600, 300));
        lineChartPanel.setMinimumSize(new Dimension(600, 300));

        section.add(lineChartPanel);

        return section;
    }

    private JPanel buildExportSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, false),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Export Reports", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        section.add(titleLabel);

        // Export buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(Color.WHITE);

        JButton pdfButton = createExportButton("📄", "Export as PDF", BLUE_HEADER);
        pdfButton.addActionListener(e -> exportAsPDF());
        buttonsPanel.add(pdfButton);
        buttonsPanel.add(Box.createVerticalStrut(10));

        JButton excelButton = createExportButton("📊", "Export as Excel", GREEN_BUTTON);
        excelButton.addActionListener(e -> exportAsExcel());
        buttonsPanel.add(excelButton);

        section.add(buttonsPanel);

        return section;
    }

    private JButton createExportButton(String icon, String text, Color bgColor) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));

        JPanel leftPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(bgColor);
        leftPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon, SwingConstants.LEFT);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        iconLabel.setForeground(Color.WHITE);

        JLabel textLabel = new JLabel(text, SwingConstants.LEFT);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textLabel.setForeground(Color.WHITE);

        leftPanel.add(iconLabel);
        leftPanel.add(textLabel);

        JLabel downloadIcon = new JLabel("↓", SwingConstants.RIGHT);
        downloadIcon.setFont(new Font("SansSerif", Font.PLAIN, 18));
        downloadIcon.setForeground(Color.WHITE);

        button.add(leftPanel, BorderLayout.WEST);
        button.add(downloadIcon, BorderLayout.EAST);

        return button;
    }

    private void exportAsPDF() {
        // Simplified export - show message
        javax.swing.JOptionPane.showMessageDialog(this,
            "PDF export functionality would be implemented here.\n" +
            "This would generate a PDF report with all analytics data.",
            "Export as PDF",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportAsExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export as Excel");
        fileChooser.setSelectedFile(new File("Analytics_Report_" + 
            java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")) + ".csv"));
        
        // Set file filter for CSV/Excel files
        javax.swing.filechooser.FileFilter excelFilter = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String name = f.getName().toLowerCase();
                return name.endsWith(".csv") || name.endsWith(".xlsx") || name.endsWith(".xls");
            }
            
            @Override
            public String getDescription() {
                return "Excel Files (*.csv, *.xlsx, *.xls)";
            }
        };
        fileChooser.addChoosableFileFilter(excelFilter);
        fileChooser.setFileFilter(excelFilter);
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            
            // Ensure .csv extension
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
                file = new File(filePath);
            }
            
            try {
                exportToCSV(file);
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Report exported successfully to:\n" + filePath,
                    "Export Successful",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Error exporting report:\n" + e.getMessage(),
                    "Export Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportToCSV(File file) throws IOException {
        List<com.bantayalert.data.IncidentReport> allReports = IncidentReportRepository.findAll();
        
        try (FileWriter writer = new FileWriter(file)) {
            // Write BOM for Excel UTF-8 compatibility
            writer.write('\ufeff');
            
            // Write header
            writer.write("Report ID,Type,Description,Location,Status,Date/Time,Reporter Name,Reporter Email,Photo,Video\n");
            
            // Write data rows
            for (com.bantayalert.data.IncidentReport report : allReports) {
                writer.write(escapeCSV(report.getId()) + ",");
                writer.write(escapeCSV(report.getType()) + ",");
                writer.write(escapeCSV(report.getDescription()) + ",");
                writer.write(escapeCSV(report.getLocation()) + ",");
                writer.write(escapeCSV(report.getStatus()) + ",");
                writer.write(escapeCSV(report.getDateTime()) + ",");
                writer.write(escapeCSV(report.getReporterName()) + ",");
                writer.write(escapeCSV(report.getReporterEmail()) + ",");
                writer.write(escapeCSV(report.getPhotoPath() != null ? report.getPhotoPath() : "") + ",");
                writer.write(escapeCSV(report.getVideoPath() != null ? report.getVideoPath() : ""));
                writer.write("\n");
            }
            
            // Write summary statistics
            writer.write("\n");
            writer.write("SUMMARY STATISTICS\n");
            writer.write("Total Reports," + allReports.size() + "\n");
            writer.write("Pending," + IncidentReportRepository.countPending() + "\n");
            writer.write("Verified," + IncidentReportRepository.countVerified() + "\n");
            writer.write("Responded," + IncidentReportRepository.countResponded() + "\n");
            writer.write("Dismissed," + (allReports.size() - IncidentReportRepository.countPending() - 
                                         IncidentReportRepository.countVerified() - 
                                         IncidentReportRepository.countResponded()) + "\n");
            
            double resolutionRate = allReports.size() > 0 ? 
                (double) IncidentReportRepository.countResponded() / allReports.size() * 100 : 0;
            writer.write("Resolution Rate," + String.format("%.2f%%", resolutionRate) + "\n");
            
            // Write Reports Graph Data (Dashboard)
            writer.write("\n");
            writer.write("REPORTS GRAPH DATA (DASHBOARD)\n");
            writer.write("Status,Count\n");
            writer.write("Pending," + IncidentReportRepository.countPending() + "\n");
            writer.write("Verified," + IncidentReportRepository.countVerified() + "\n");
            writer.write("Responded," + IncidentReportRepository.countResponded() + "\n");
            
            // Write Incident Types Graph Data (Dashboard)
            writer.write("\n");
            writer.write("INCIDENT TYPES GRAPH DATA (DASHBOARD)\n");
            writer.write("Incident Type,Count\n");
            
            // Count reports by type
            java.util.Map<String, Integer> typeCounts = new java.util.HashMap<>();
            String[] incidentTypes = {"Fire", "Flood", "Earthquake", "Typhoon", "Landslide", 
                                     "Medical Emergency", "Crime", "Other"};
            
            for (com.bantayalert.data.IncidentReport report : allReports) {
                String type = report.getType();
                typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
            }
            
            // Write counts for each type
            for (String type : incidentTypes) {
                int count = typeCounts.getOrDefault(type, 0);
                if (count > 0) {
                    writer.write(type + "," + count + "\n");
                }
            }
            
            // Write Monthly Reports Data (if needed)
            writer.write("\n");
            writer.write("MONTHLY REPORTS DATA\n");
            writer.write("Month,Count\n");
            
            java.util.Map<String, Integer> monthlyCounts = new java.util.HashMap<>();
            String[] months = {"January", "February", "March", "April", "May", "June", 
                              "July", "August", "September", "October", "November", "December"};
            
            for (com.bantayalert.data.IncidentReport report : allReports) {
                String dateTime = report.getDateTime();
                // Extract month from date string (assuming format like "MM/dd/yyyy HH:mm")
                try {
                    String[] parts = dateTime.split("/");
                    if (parts.length >= 2) {
                        int monthNum = Integer.parseInt(parts[0]);
                        if (monthNum >= 1 && monthNum <= 12) {
                            String month = months[monthNum - 1];
                            monthlyCounts.put(month, monthlyCounts.getOrDefault(month, 0) + 1);
                        }
                    }
                } catch (Exception e) {
                    // Skip if date parsing fails
                }
            }
            
            // Write monthly counts
            for (String month : months) {
                int count = monthlyCounts.getOrDefault(month, 0);
                writer.write(month + "," + count + "\n");
            }
        }
    }
    
    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        // Escape quotes and wrap in quotes if contains comma, quote, or newline
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}

