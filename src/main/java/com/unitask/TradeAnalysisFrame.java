package com.unitask;

import jakarta.annotation.PostConstruct;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

@Component
public class TradeAnalysisFrame extends JFrame {
    private final HadoopService hadoopService;

    private JTextField periodField;
    private JTextField unitsField;
    private JTextField categoryField;
    private JTextArea resultArea;
    private JRadioButton totalValueRadio;
    private JRadioButton averageValueRadio;

    @Autowired
    public TradeAnalysisFrame(HadoopService hadoopService) {
        this.hadoopService = hadoopService;
        setTitle("Trade Analysis Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @PostConstruct
    private void initializeComponents() {

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Period Filter
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Period (YYYY.MM):"), gbc);

        gbc.gridx = 1;
        periodField = new JTextField(20);
        mainPanel.add(periodField, gbc);

        // Units Filter
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Units:"), gbc);

        gbc.gridx = 1;
        unitsField = new JTextField(20);
        mainPanel.add(unitsField, gbc);

        // Category Filter
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        categoryField = new JTextField(20);
        mainPanel.add(categoryField, gbc);

        // Radio buttons
        ButtonGroup buttonGroup = new ButtonGroup();
        totalValueRadio = new JRadioButton("Total Value");
        averageValueRadio = new JRadioButton("Average Value");
        buttonGroup.add(totalValueRadio);
        buttonGroup.add(averageValueRadio);
        totalValueRadio.setSelected(true);

        JPanel radioPanel = new JPanel();
        radioPanel.add(totalValueRadio);
        radioPanel.add(averageValueRadio);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(radioPanel, gbc);

        // Search button
        JButton searchButton = new JButton("Search");
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(searchButton, gbc);

        // Load File button
        JButton loadFileButton = new JButton("Load File");
        gbc.gridx = 1;
        gbc.gridy = 5;
        mainPanel.add(loadFileButton, gbc);

        // Results area
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(scrollPane, gbc);

        // Add action listener to search button
        searchButton.addActionListener(e -> performSearch());
        loadFileButton.addActionListener(e -> loadFileAndProcess());

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private void performSearch() {
        String period = periodField.getText();
        String units = unitsField.getText();
        String category = categoryField.getText();
        boolean isTotalValue = totalValueRadio.isSelected();

        try {
            // Hardcoded paths for input and output
            String inputPath = "/user/datafolder/input/data.csv"; // Replace with your actual HDFS path
            String outputPath = "";

            // Trigger the Hadoop job with hardcoded file
            hadoopService.processData(inputPath, outputPath, period, units, category, isTotalValue);

            // Fetch results from HDFS
            String result = fetchHDFSOutput(outputPath + "/part-r-00000");
            resultArea.setText(result);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void loadFileAndProcess() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                ExcelReader excelReader = new ExcelReader();
                List<DataRecord> records = excelReader.readExcelFile(selectedFile.getAbsolutePath());
                // Process records (e.g., export to CSV or pass to Hadoop)
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // Helper method to fetch output from HDFS
    private String fetchHDFSOutput(String hdfsPath) throws IOException {
        FileSystem fs = FileSystem.get(new Configuration());
        FSDataInputStream inputStream = fs.open(new Path(hdfsPath));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line).append("\n");
        }

        reader.close();
        return result.toString();
    }

}