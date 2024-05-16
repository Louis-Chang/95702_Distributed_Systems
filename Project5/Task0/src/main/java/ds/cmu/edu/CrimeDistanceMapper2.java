package ds.cmu.edu;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class CrimeDistanceMapper2 extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private final static double TARGET_X = 1354326.897; // X coordinate of 3803 Forbes Avenue
    private final static double TARGET_Y = 411447.7828; // Y coordinate of 3803 Forbes Avenue
    private final static double RADIUS = 350 / 3.28084; // Convert 350 meters to feet

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] parts = value.toString().split(",");  // Adjust delimiter based on your data format
        if (parts.length > 5) {
            String type = parts[4].toLowerCase();
            try {
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                double distance = Math.sqrt(Math.pow(x - TARGET_X, 2) + Math.pow(y - TARGET_Y, 2));
                if (type.equals("aggravated assault") && distance <= RADIUS) {
                    context.write(new Text("Aggravated Assault near 3803 Forbes Ave"), one);
                }
            } catch (NumberFormatException e) {
                // Handle parse error or log if necessary
            }
        }
    }
}
