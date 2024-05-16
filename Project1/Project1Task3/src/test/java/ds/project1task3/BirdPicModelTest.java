package ds.project1task3;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

class BirdPicModelTest {

    BirdPicModel birdPicModel = new BirdPicModel();

    @Test
    void scrapingTest() throws IOException {
        String[] states = {"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
        for (String state : states) {
            Map<String, String> bird = birdPicModel.doSearch(state);
            System.out.println(state);
            for (Map.Entry<String, String> entry : bird.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("--------");
        }
//        String state = "Pennsylvania";
//        Map<String, String> bird = birdPicModel.doSearch(state);
//        System.out.println(state);
//        for (Map.Entry<String, String> entry : bird.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
    }

}