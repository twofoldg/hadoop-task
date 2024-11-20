package com.unitask;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TradeMapper extends Mapper<Object, Text, Text, Text> {
    private String periodFilter;
    private String unitsFilter;
    private String categoryFilter;

    @Override
    protected void setup(Context context) {
        Configuration conf = context.getConfiguration();
        periodFilter = conf.get("period", "");
        unitsFilter = conf.get("units", "");
        categoryFilter = conf.get("category", "");
    }

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split(",");
        if (fields.length < 10) return;

        String period = fields[1].trim();
        String units = fields[4].trim();
        String category = fields[8].trim();
        String dataValue = fields[2].trim();

        boolean matches = (periodFilter.isEmpty() || period.contains(periodFilter)) &&
                (unitsFilter.isEmpty() || units.equalsIgnoreCase(unitsFilter)) &&
                (categoryFilter.isEmpty() || category.equalsIgnoreCase(categoryFilter));

        if (matches) {
            context.write(new Text("key"), new Text(dataValue));
        }
    }
}
