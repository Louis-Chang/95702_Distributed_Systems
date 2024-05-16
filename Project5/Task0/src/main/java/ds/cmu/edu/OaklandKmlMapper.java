package ds.cmu.edu;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class OaklandKmlMapper extends Mapper<LongWritable, Text, Text, Text> {

    private static final double targetX = 1354326.897; // Target X coordinate
    private static final double targetY = 411447.7828; // Target Y coordinate

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] parts = line.split("\t");

        if (parts.length >= 5 && parts[4].equals("AGGRAVATED ASSAULT")) {
            double crimeX = Double.parseDouble(parts[0]);
            double crimeY = Double.parseDouble(parts[1]);

            double distance = Math.sqrt(Math.pow(crimeX - targetX, 2) + Math.pow(crimeY - targetY, 2)) * 0.3048; // Distance in meters

            if (distance <= 350.0) {
                String coordinate = parts[8] + "," + parts[7] + ",0";
                context.write(new Text("Total"), new Text(coordinate));
            }
        }
    }
}
