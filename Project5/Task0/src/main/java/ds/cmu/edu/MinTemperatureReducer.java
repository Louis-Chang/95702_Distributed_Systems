package ds.cmu.edu;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer; import org.apache.hadoop.mapred.Reporter;

public class MinTemperatureReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
    public void reduce(Text key, java.util.Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
        int minValue = Integer.MAX_VALUE;
        while (values.hasNext()) {
            minValue = Math.min(minValue, values.next().get());
        }
        output.collect(key, new IntWritable(minValue));
    }
}
