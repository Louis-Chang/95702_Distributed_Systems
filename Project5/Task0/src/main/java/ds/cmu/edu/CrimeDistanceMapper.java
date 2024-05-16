package ds.cmu.edu;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

// Define the Mapper
public class CrimeDistanceMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private final static double TARGET_X = 1354326.897;
    private final static double TARGET_Y = 411447.7828;
    private final static double RADIUS = 350 / 3.28084; // Convert 350 meters to feet

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] parts = value.toString().split("\t");
        if (parts.length > 6) {
            String type = parts[4].toLowerCase();
            double x = Double.parseDouble(parts[0]);
            double y = Double.parseDouble(parts[1]);
            double distance = Math.sqrt(Math.pow(x - TARGET_X, 2) + Math.pow(y - TARGET_Y, 2));
            if (type.equals("aggravated assault") && distance <= RADIUS) {
                context.write(new Text("aggravated assault near 3803 Forbes Ave"), one);
            }
        }
    }
}
