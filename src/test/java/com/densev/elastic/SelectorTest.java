package com.densev.elastic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created on 15.03.2017.
 */
public class SelectorTest {


    private final static Logger logger = LoggerFactory.getLogger(SelectorTest.class);

    @Test(enabled = false)
    public void test() throws IOException {
        Document doc = Jsoup.connect("http://www.pixiv.net/search.php?word=2B&s_mode=s_tag_full&order=date_d&p=110").get();
        Elements elements = doc.select(".work._work");

        for (Element e: elements){
            logger.error(e.toString());
        }
    }
}
