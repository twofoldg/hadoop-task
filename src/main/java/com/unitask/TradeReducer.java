package com.unitask;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class TradeReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        double total = 0;
        int count = 0;

        for (Text value : values) {
            total += Double.parseDouble(value.toString());
            count++;
        }

        double average = count == 0 ? 0 : total / count;

        context.write(new Text("Total"), new Text(String.valueOf(total)));
        context.write(new Text("Average"), new Text(String.valueOf(average)));
    }
}

