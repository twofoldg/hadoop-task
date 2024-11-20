package com.unitask;

class DataRecord {
    private String period;
    private String units;
    private String category;
    private double dataValue;

    public DataRecord(String period, String units, String category, double dataValue) {
        this.period = period;
        this.units = units;
        this.category = category;
        this.dataValue = dataValue;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getDataValue() {
        return dataValue;
    }

    public void setDataValue(double dataValue) {
        this.dataValue = dataValue;
    }
}