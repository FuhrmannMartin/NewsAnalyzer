package newsanalyzer.ctrl;

import newsapi.NewsApi;
import newsapi.NewsApiBuilder;
import newsapi.NewsApiException;
import newsapi.beans.Article;
import newsapi.beans.NewsReponse;
import newsapi.enums.Endpoint;
import newsreader.downloader.ParallelDownloader;
import newsreader.downloader.SequentialDownloader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Controller {

	public static final String APIKEY = "de1bc1c9970a4612aa0eb42e33025408";
	// public static final String APIKEY = "";
	public static final String pageSize = "20";

	public String process(String q) throws NewsApiException, IOException, InterruptedException, ExecutionException {
		System.out.println("Start process");

		//TODO implement Error handling

		//TODO load the news based on the parameters

		//TODO implement methods for analysis

		String sb = "";

		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(APIKEY)
				.setQ(q)
				.setEndPoint(Endpoint.EVERYTHING)
				.setPageSize(pageSize)
				.createNewsApi();

		List<Article> articles = getData(newsApi);

		/*
		articles = sortByTitleLength(articles);
		sb.append("Number of articles found: " + getCount(articles) + System.lineSeparator());
		sb.append("Most articles published by: " + getDominantProvider(articles) + System.lineSeparator());
		sb.append("Author with shortest name: " + getShortestAuthorName(articles) + System.lineSeparator());
		sb.append("Articles sorted by longest name first: " + System.lineSeparator());
		articles.forEach(article -> sb.append(article.getTitle() + System.lineSeparator()));
		 */

		downloadLastSearch(articles);
		System.out.println("End process");
		return sb;
	}

	protected static int getCount(List<Article> articles) {
		long count = articles.size();
		return (int) count;
	}

	protected static String getDominantProvider(List<Article> articles) {
		Map<String, Long> publishers = articles.stream().
				map(article -> article.getSource().getName())
				.collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		return getMaxEntryInMapBasedOnValue(publishers).getKey();
	}

	private static List<Article> sortByTitleLength(List<Article> articles) {
		return articles.stream().sorted( (a1, a2) -> Integer.compare(a2.getTitle().length(),a1.getTitle().length())).collect(Collectors.toList());
	}

	private static String getShortestAuthorName(List<Article> articles) {
		// removal of articles without any author defined and sort by length of authors' names
		articles = articles.stream().filter(a -> a.getAuthor() != null).sorted( (a1, a2) -> Integer.compare(a1.getAuthor().length(),a2.getAuthor().length())).collect(Collectors.toList());
		return articles.get(0).getAuthor();
	}

	// getMaxEntryInMapBasedOnValue: https://www.geeksforgeeks.org/how-to-find-the-entry-with-largest-value-in-a-java-map/
	public static <K, V extends Comparable<V> >	Map.Entry<K, V>	getMaxEntryInMapBasedOnValue(Map<K, V> map)	{

		// To store the result
		Map.Entry<K, V> entryWithMaxValue = null;

		// Iterate in the map to find the required entry
		for (Map.Entry<K, V> currentEntry :
				map.entrySet()) {

			if (
				// If this is the first entry, set result as
				// this
					entryWithMaxValue == null

							// If this entry's value is more than the
							// max value Set this entry as the max
							|| currentEntry.getValue().compareTo(
							entryWithMaxValue.getValue())
							> 0) {

				entryWithMaxValue = currentEntry;
			}
		}

		// Return the entry with highest value
		return entryWithMaxValue;
	}

	public List<Article> getData(NewsApi newsApi) throws NewsApiException, IOException {
		NewsReponse newsResponse = newsApi.getNews();
		return newsResponse.getArticles();
	}

	private static List<String> getURLs(List<Article> articles) {
		// List<String> urls = articles.stream().map(a -> a.getUrl()).collect(Collectors.toList());
		return articles.stream().map(Article::getUrl).filter(url -> !url.contains("?")).collect(Collectors.toList());
	}

	public void downloadLastSearch(List<Article> articles) throws InterruptedException, ExecutionException {
		SequentialDownloader sequentialDownloader = new SequentialDownloader();
		long startSequential = System.currentTimeMillis();
		sequentialDownloader.process(getURLs(articles));
		long finishSequential = System.currentTimeMillis();
		long timeElapsedSequential = finishSequential - startSequential;
		System.out.println("Sequential Execution took " + timeElapsedSequential + "ms");

		ParallelDownloader parallelDownloader = new ParallelDownloader();
		long startParallel = System.currentTimeMillis();
		parallelDownloader.process(getURLs(articles));
		long finishParallel = System.currentTimeMillis();
		long timeElapsedParallel = finishParallel - startParallel;
		System.out.println("Parallel Execution took " + timeElapsedParallel + "ms");
	}

}
