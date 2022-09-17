package com.epidemicmanagement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author XUE
 */
@SpringBootTest
public class JsoupTest {

    @Test
    public void JsoupUseTest() {
        String html = "<h1>测试</h1>" +
                "<h3>测试12</h3>" +
                "<h3>彩色</h3>\n" +
                "<p>乘势而上撒饿饭阿瑟阿斯弗阿瑟feasible阿塞飞</p>";
        Document doc = Jsoup.parse(html);
        String text = doc.text();
        System.out.println(text);
    }

}
