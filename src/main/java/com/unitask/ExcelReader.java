package com.unitask;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public List<DataRecord> readExcelFile(String filePath) throws IOException {
        List<DataRecord> records = new ArrayList<>();
        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fis);

        Sheet sheet = workbook.getSheetAt(0); // Assumes data is in the first sheet
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // Skip header row
                continue;
            }
            String period = row.getCell(1).toString(); // Column A
            String units = row.getCell(4).toString(); // Column B
            String category = row.getCell(9).toString(); // Column C
            double dataValue = row.getCell(2).getNumericCellValue(); // Column D

            records.add(new DataRecord(period, units, category, dataValue));
        }

        workbook.close();
        fis.close();
        return records;
    }

    public class DataExporter {
        public void exportToCsv(List<DataRecord> records, String outputPath) throws IOException {
            try (FileWriter writer = new FileWriter(outputPath)) {
                writer.append("Period,Units,Category,DataValue\n");
                for (DataRecord record : records) {
                    writer.append(record.getPeriod()).append(",")
                            .append(record.getUnits()).append(",")
                            .append(record.getCategory()).append(",")
                            .append(String.valueOf(record.getDataValue())).append("\n");
                }
            }
        }
    }
}
