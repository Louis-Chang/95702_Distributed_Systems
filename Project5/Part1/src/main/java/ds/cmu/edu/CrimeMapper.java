package ds.cmu.edu;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CrimeMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private static final IntWritable one = new IntWritable(1);
    private Text offenseType = new Text();

    // Coordinates for 3803 Forbes Avenue in Oakland
    private static final double targetX = 1354326.897;
    private static final double targetY = 411447.7828;
    private static final double thresholdDistance = 1148.294; // 350 meters in feet

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        if (fields.length > 4 && key.get() > 0) { // Skip the header and ensure data integrity
            String type = fields[4];
            double x = Double.parseDouble(fields[0]);
            double y = Double.parseDouble(fields[1]);

            // Calculate the distance using the Pythagorean theorem
            double distance = Math.sqrt(Math.pow(x - targetX, 2) + Math.pow(y - targetY, 2));

            if (type.equals("AGGRAVATED ASSAULT") && distance <= thresholdDistance) {
                offenseType.set("AGGRAVATED ASSAULT within 350m");
                context.write(offenseType, one);
            }
        }
    }
}

