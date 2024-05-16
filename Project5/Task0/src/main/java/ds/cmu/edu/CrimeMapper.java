package ds.cmu.edu;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CrimeMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private Text crimeType = new Text();

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] parts = value.toString().split("\t");  // Split line into parts
        if (parts.length > 4) {
            String type = parts[4].toLowerCase();
            if (type.equals("aggravated assault") || type.equals("robbery")) {
                crimeType.set(type);
                context.write(crimeType, one);
            }
        }
    }
}
