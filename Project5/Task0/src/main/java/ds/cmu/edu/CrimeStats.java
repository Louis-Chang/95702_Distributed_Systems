package ds.cmu.edu;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.*;

// Define the Driver
public class CrimeStats extends Configured implements Tool {
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: CrimeStats <input path> <output path>");
            System.exit(-1);
        }

        Job job = new Job(getConf());
        job.setJobName("Crime Stats");
        job.setJarByClass(CrimeStats.class);

        job.setMapperClass(CrimeMapper2.class);
        job.setCombinerClass(CrimeReducer2.class);
        job.setReducerClass(CrimeReducer2.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new CrimeStats(), args);
        System.exit(res);
    }
}