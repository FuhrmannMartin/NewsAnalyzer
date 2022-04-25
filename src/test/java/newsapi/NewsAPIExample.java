package newsapi;

import newsapi.beans.Article;
import newsapi.beans.NewsReponse;
import newsapi.enums.Category;
import newsapi.enums.Country;
import newsapi.enums.Endpoint;

import java.io.IOException;
import java.util.List;

public class NewsAPIExample {

    public static final String APIKEY = "myAPIKey";

    public static void main(String[] args){

        NewsApi newsApi = new NewsApiBuilder()
                .setApiKey(APIKEY)
                .setQ("corona")
                .setEndPoint(Endpoint.TOP_HEADLINES)
                .setSourceCountry(Country.at)
                .setSourceCategory(Category.health)
                .createNewsApi();

            try {
                NewsReponse newsResponse = newsApi.getNews();
                if(newsResponse != null){
                    List<Article> articles = newsResponse.getArticles();
                    articles.forEach(article -> System.out.println(article.toString()));
                }
            } catch (NewsApiException | IOException exception) {
                System.out.println("Error: " + exception.getMessage());
            }

    }
}
