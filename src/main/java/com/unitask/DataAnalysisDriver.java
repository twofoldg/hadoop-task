package com.unitask;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DataAnalysisDriver {

    public static void main(String[] args) throws Exception {
        if (args.length != 5) {
            System.err.println("Usage: DataAnalysisDriver <input path> <output path> <period filter> <units filter> <category filter>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        conf.set("filter.period", args[2]);
        conf.set("filter.units", args[3]);
        conf.set("filter.category", args[4]);

        Job job = Job.getInstance(conf, "Data Analysis");
        job.setJarByClass(DataAnalysisDriver.class);
        job.setMapperClass(TradeMapper.class);
        job.setReducerClass(TradeReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
