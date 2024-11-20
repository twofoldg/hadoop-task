package com.unitask;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.springframework.stereotype.Service;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.conf.Configuration;

@Service
public class HadoopService {

//    public void processData(String inputPath, String outputPath, String period, String units, String category, boolean isTotalValue) {
//        try {
//            Configuration conf = new Configuration();
//            conf.set("period", period);
//            conf.set("units", units);
//            conf.set("category", category);
//            conf.set("calculationType", isTotalValue ? "total" : "average");
//
//            Job job = Job.getInstance(conf, "Trade Analysis");
//            job.setJarByClass(getClass());
//            job.setMapperClass(TradeMapper.class);
//            job.setReducerClass(TradeReducer.class);
//
//            job.setOutputKeyClass(Text.class);
//            job.setOutputValueClass(Text.class);
//
////            FileInputFormat.addInputPath(job, new Path(inputPath));  // Hardcoded input file
//            FileInputFormat.addInputPath(job, new Path("hdfs://localhost:9000/user/datafolder/input/data.csv"));
//
//            FileSystem fs = FileSystem.get(conf);
//            Path outputDir = new Path("/user/datafolder/output");
//            if (fs.exists(outputDir)) {
//                System.out.println("Output path exists. Deleting: " + outputPath);
//                fs.delete(outputDir, true); // Recursive delete
//            }
//
//            FileOutputFormat.setOutputPath(job, new Path("hdfs://localhost:9000/user/datafolder/output"));
//
//            job.waitForCompletion(true);
//        } catch (Exception ex) {
//            throw new RuntimeException("Error processing Hadoop job", ex);
//        }
//    }

public void processData(String inputPath, String outputPath, String period, String units, String category, boolean isTotalValue) {
    try {
        Configuration conf = new Configuration();
        conf.set("period", period);
        conf.set("units", units);
        conf.set("category", category);

        FileSystem fs = FileSystem.get(conf);
        Path outputDir = new Path("/user/datafolder/output");

        // Delete output directory if it exists
        if (fs.exists(outputDir)) {
            System.out.println("Output path exists. Deleting: " + outputPath);
            fs.delete(outputDir, true); // Recursive delete
        }

        Job job = Job.getInstance(conf, "Trade Analysis");
        job.setJarByClass(getClass());
        job.setMapperClass(TradeMapper.class);
        job.setReducerClass(TradeReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path("hdfs://localhost:9000/user/datafolder/input/data.csv"));
        FileOutputFormat.setOutputPath(job, new Path("hdfs://localhost:9000/user/datafolder/output"));

        if (!job.waitForCompletion(true)) {
            throw new RuntimeException("Hadoop job failed!");
        }

    } catch (Exception ex) {
        throw new RuntimeException("Error processing Hadoop job: " + ex.getMessage(), ex);
    }
}
}
