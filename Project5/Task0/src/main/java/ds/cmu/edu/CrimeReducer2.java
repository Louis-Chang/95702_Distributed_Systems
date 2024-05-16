package ds.cmu.edu;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class CrimeReducer2 extends Reducer<Text, Text, NullWritable, Text> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        context.write(NullWritable.get(), new Text("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        context.write(NullWritable.get(), new Text("<kml xmlns=\"http://www.opengis.net/kml/2.2\">"));
        context.write(NullWritable.get(), new Text("<Document>"));
    }

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for (Text value : values) {
            context.write(NullWritable.get(), new Text("<Placemark>"));
            context.write(NullWritable.get(), new Text("<name>" + key.toString() + "</name>"));
            context.write(NullWritable.get(), value); // value should contain the KML formatted Point
            context.write(NullWritable.get(), new Text("</Placemark>"));
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        context.write(NullWritable.get(), new Text("</Document>"));
        context.write(NullWritable.get(), new Text("</kml>"));
    }
}

