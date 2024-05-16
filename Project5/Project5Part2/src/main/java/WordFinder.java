import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.List;
import java.util.Scanner;

public class WordFinder {

    private static void findLinesWithWord(String fileName) {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("Line Finder");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        // Read the file into an RDD
        JavaRDD<String> inputFile = sparkContext.textFile(fileName);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a word:");
        String word = scanner.nextLine();
        scanner.close();

        // Filter lines that contain the specified word
        JavaRDD<String> linesWithWord = inputFile.filter(line -> line.contains(word));

        // Collect and print lines
        List<String> resultLines = linesWithWord.collect();
        if (resultLines.isEmpty()) {
            System.out.println("No lines found containing the word: " + word);
        } else {
            System.out.println("Lines containing the word '" + word + "':");
            resultLines.forEach(System.out::println);
        }

    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No file provided.");
            System.exit(0);
        }

        findLinesWithWord(args[0]);
    }
}
