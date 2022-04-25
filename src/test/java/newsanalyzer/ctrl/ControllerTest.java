package newsanalyzer.ctrl;

import newsapi.beans.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Test
    void getCount2_szenario1(){
        List<Article> articles = new ArrayList<>();
        Article article = new Article(null, "author","title", " description", " url", " urlToImage", " publishedAt", " content");
        articles.add(article);
        int actual = Controller.getCount(articles);
        int expected = 1;
        assertEquals(expected,actual);
    }
    @Test
    void getCount2_szenario2(){
        List<Article> articles = new ArrayList<>();
        Article article = new Article(null, "author","title", " description", " url", " urlToImage", " publishedAt", " content");
        articles.add(article);
        article = new Article(null, "author2","title", " description", " url", " urlToImage", " publishedAt", " content");
        articles.add(article);

        int actual = Controller.getCount(articles);
        int expected = 2;
        assertEquals(expected,actual);
    }

    /*
    @Test
    void getCount2_szenario_null(){
        int actual = Controller.getCount(null);
        int expected = 2;
        assertEquals(expected,actual);
    }
     */

}