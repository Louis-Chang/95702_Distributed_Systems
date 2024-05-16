import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class WordCounter {

    private static void wordCount(String fileName) {

        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("Extended Word Counter");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        // Read the file into an RDD
        JavaRDD<String> inputFile = sparkContext.textFile(fileName);

        // Count the number of lines
        long numberOfLines = inputFile.count();

        // Flatten the lines into words
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split("[^a-zA-Z]+")));
        JavaRDD<String> filteredWords = wordsFromFile.filter(s -> !s.isEmpty());

        // Count the number of words
        long numberOfWords = filteredWords.count();

        // Create a PairRDD with each word and count each word
        JavaPairRDD<String, Integer> wordCounts = filteredWords.mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey(Integer::sum);

        // Count the number of distinct words
        long numberOfDistinctWords = wordCounts.keys().distinct().count();

        // Flatten the lines into characters
        JavaRDD<String> symbolsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split("")));

        // Count the number of symbols
        long numberOfSymbols = symbolsFromFile.count();

        // Count distinct symbols
        long numberOfDistinctSymbols = symbolsFromFile.distinct().count();

        // Filter only letters and count distinct letters
        JavaRDD<String> lettersFromFile = symbolsFromFile.filter(s -> s.matches("[a-zA-Z]"));
        long numberOfDistinctLetters = lettersFromFile.distinct().count();

        // Output the results to the console or write them back to a file system
        System.out.println("Number of lines: " + numberOfLines);
        System.out.println("Number of words: " + numberOfWords);
        System.out.println("Number of distinct words: " + numberOfDistinctWords);
        System.out.println("Number of symbols: " + numberOfSymbols);
        System.out.println("Number of distinct symbols: " + numberOfDistinctSymbols);
        System.out.println("Number of distinct letters: " + numberOfDistinctLetters);

    }

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No files provided.");
            System.exit(0);
        }

        wordCount(args[0]);
    }
}
