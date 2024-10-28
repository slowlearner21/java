import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class SimplePercentageCalculator extends JFrame {
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JButton importButton;
    private JButton calculateButton;
    private JButton exportButton;
    private JLabel statusLabel;

    public SimplePercentageCalculator() {
        setTitle("Student Results Processor");
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        setupUI();
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        // Initialize table with columns
        String[] columns = {
            "Student ID", "Student Name", 
            "Subject 1", "Max 1", "Subject 2", "Max 2", 
            "Subject 3", "Max 3", "Subject 4", "Max 4", 
            "Subject 5", "Max 5", "Total Obtained", 
            "Total Max", "Percentage", "Grade"
        };
        
        tableModel = new DefaultTableModel(columns, 0);
        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Set column widths
        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            resultTable.getColumnModel().getColumn(i).setPreferredWidth(100);
        }

        // Initialize buttons
        importButton = new JButton("Import CSV");
        calculateButton = new JButton("Calculate Results");
        exportButton = new JButton("Export Results");
        statusLabel = new JLabel("Ready to import data");
        
        // Add button listeners
        importButton.addActionListener(e -> importFile());
        calculateButton.addActionListener(e -> calculateResults());
        exportButton.addActionListener(e -> exportResults());
    }

    private void setupUI() {
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Table Panel
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(importButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(exportButton);
        
        // Status Panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        // Combine button and status panels
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 128, 128));
        headerPanel.setPreferredSize(new Dimension(1000, 50));
        JLabel titleLabel = new JLabel("Student Results Processor");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel);
        return headerPanel;
    }

    private void importFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
        fileChooser.setFileFilter(filter);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                importCSV(file);
                statusLabel.setText("Data imported successfully");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error importing file: " + e.getMessage(),
                    "Import Error",
                    JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("Import failed");
            }
        }
    }

    private void importCSV(File file) throws IOException {
        tableModel.setRowCount(0); // Clear existing data
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Skip header
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 12) { // Ensure minimum required columns
                    tableModel.addRow(data);
                }
            }
        }
    }

    private void calculateResults() {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            double totalObtained = 0;
            double totalMax = 0;

            // Calculate totals for each subject
            for (int subject = 0; subject < 5; subject++) {
                int obtainedCol = 2 + (subject * 2);
                int maxCol = 3 + (subject * 2);
                
                try {
                    double obtained = Double.parseDouble(tableModel.getValueAt(row, obtainedCol).toString());
                    double max = Double.parseDouble(tableModel.getValueAt(row, maxCol).toString());
                    
                    totalObtained += obtained;
                    totalMax += max;
                } catch (NumberFormatException | NullPointerException e) {
                    continue;
                }
            }

            // Calculate and set results
            if (totalMax > 0) {
                double percentage = (totalObtained / totalMax) * 100;
                String grade = calculateGrade(percentage);
                
                tableModel.setValueAt(totalObtained, row, 12);
                tableModel.setValueAt(totalMax, row, 13);
                tableModel.setValueAt(String.format("%.2f%%", percentage), row, 14);
                tableModel.setValueAt(grade, row, 15);
            }
        }
        statusLabel.setText("Results calculated successfully");
    }

    private String calculateGrade(double percentage) {
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        if (percentage >= 50) return "D";
        return "F";
    }

    private void exportResults() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            
            try (PrintWriter writer = new PrintWriter(file)) {
                // Write header
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.print(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
                
                // Write data
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        writer.print(tableModel.getValueAt(row, col));
                        if (col < tableModel.getColumnCount() - 1) {
                            writer.print(",");
                        }
                    }
                    writer.println();
                }
                statusLabel.setText("Results exported successfully");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting results: " + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("Export failed");
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            SimplePercentageCalculator calculator = new SimplePercentageCalculator();
            calculator.setVisible(true);
        });
    }
}