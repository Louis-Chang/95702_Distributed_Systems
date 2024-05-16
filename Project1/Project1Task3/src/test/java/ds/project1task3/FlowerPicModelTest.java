package ds.project1task3;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FlowerPicModelTest {
    @Test
    void name() throws IOException {
        FlowerPicModel flowerPicModel = new FlowerPicModel();
        System.out.println(flowerPicModel.doSearch("Alabama"));
    }
}