package ds.project1task3;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/05/2024
 */
public class BirdPicModel extends PicModel {

    // Create a URL for the page to be screen scraped
    final String webURL = "https://en.wikipedia.org/wiki/List_of_U.S._state_birds";

    /**
     * scrape data on Wikipedia using the searchTag, which is state, that user chose
     * @param searchTag The tag of the photo to be searched for.
     * photo requested.
     */
    @Override
    public Map<String, String> doSearch(String searchTag) throws IOException {
        /*
         * URL encode the searchTag, e.g. to encode spaces as %20
         *
         * There is no reason that UTF-8 would be unsupported.  It is the
         * standard encoding today.  So if it is not supported, we have
         * big problems, so don't catch the exception.
         */
        Map<String, String> result = new HashMap<>();

        // Read the html content using Jsoup
        Document doc = Jsoup.connect(webURL).get();
        Elements rows = doc.select("tr:has(th:contains(" + searchTag + "))");
        for (Element row : rows) {
            Elements tds = row.select("td");
            for (int i=0; i<tds.size(); i++) {
                Element td = tds.get(i);
                // Process each <td> as needed
                switch (i) {
                    // the first column is the bird's name, the second column is its scientific name, and the last column is the year
                    case 0:
                        result.put("Name", td.text());
                        break;
                    case 1:
                        result.put("sciName", td.text());
                        break;
                    case 3:
                        if (td.text().length() >= 4) {
                            result.put("year", td.text().substring(0, 4));
                        } else {
                            result.put("year", td.text());
                        }
                        break;
                }
                // If the <td> contains an <img>, print its 'src' attribute
                Elements images = td.select("img");
                for (Element img : images) {
                    // the third column, also the only column contains <img>, is the image of the state bird
                    String src = img.attr("src");
                    result.put("imgUrl", "https:"+src);
                }
            }
        }

        return result;
    }

}
