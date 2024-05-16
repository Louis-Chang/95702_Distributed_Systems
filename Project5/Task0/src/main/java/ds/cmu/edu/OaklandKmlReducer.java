package ds.cmu.edu;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class OaklandKmlReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        StringBuilder kml = new StringBuilder();
        kml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        kml.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
        kml.append("<Document>\n");

        int num = 0;
        for (Text value : values) {
            num++;
            kml.append("<Placemark>\n");
            kml.append("<name>AGGRAVATED ASSAULT ").append(num).append("</name>\n");
            kml.append("<Point>\n");
            kml.append("<coordinates>").append(value.toString()).append("</coordinates>\n");
            kml.append("</Point>\n");
            kml.append("</Placemark>\n");
        }

        kml.append("</Document>\n");
        kml.append("</kml>\n");

        context.write(new Text(), new Text(kml.toString()));
    }
}
