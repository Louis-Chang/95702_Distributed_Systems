package ds.cmu.edu;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class CrimeMapper2 extends Mapper<LongWritable, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private final static double TARGET_X = 1354326.897;
    private final static double TARGET_Y = 411447.7828;
    private final static double RADIUS_FEET = 350 / 0.3048;  // Convert 350 meters to feet
    private boolean isHeader = true;  // flag to skip the header

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (isHeader) {
            isHeader = false;  // set isHeader to false after the first run
            return;
        }
        String line = value.toString();
        try {
            String[] parts = line.split("\t");
            double x = Double.parseDouble(parts[0].trim());
            double y = Double.parseDouble(parts[1].trim());
            String type = parts[4].trim().toLowerCase();
            double distance = Math.sqrt(Math.pow(x - TARGET_X, 2) + Math.pow(y - TARGET_Y, 2));

            if (type.equals("aggravated assault") && distance <= RADIUS_FEET) {
                context.write(new Text("Aggravated Assault near 3803 Forbes Ave"), one);
            }
        } catch (NumberFormatException e) {
            // Log the error or further handling
            System.err.println("NumberFormatException encountered in map task: " + e.getMessage());
        }
    }
}
