package com.unitask;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TradeDataService {
    private final List<DataRecord> allData;
    private final Set<String> availableUnits;
    private final Set<String> availableCategories;

    public TradeDataService() {
        this.allData = new ArrayList<>();
        this.availableUnits = new HashSet<>();
        this.availableCategories = new HashSet<>();
    }

    public List<DataRecord> filterData(String period, String units, String category) {
        return allData.stream()
                .filter(data -> period.isEmpty() || data.getPeriod().equals(period))
                .filter(data -> units.isEmpty() || data.getUnits().equals(units))
//                .filter(data -> category.isEmpty() || data.getSeriesTitle2().equals(category))
                .collect(Collectors.toList());
    }

    public double calculateTotalValue(String period, String units, String category) {
        return filterData(period, units, category).stream()
                .mapToDouble(DataRecord::getDataValue)
                .sum();
    }

    public double calculateAverageValue(String period, String units, String category) {
        List<DataRecord> filteredData = filterData(period, units, category);
        if (filteredData.isEmpty()) return 0.0;

        return filteredData.stream()
                .mapToDouble(DataRecord::getDataValue)
                .average()
                .orElse(0.0);
    }

}

