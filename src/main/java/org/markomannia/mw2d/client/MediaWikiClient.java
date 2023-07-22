package org.markomannia.mw2d.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.markomannia.mw2d.categories.util.CategoryUtils;
import org.markomannia.mw2d.client.util.HttpClientUtils;
import org.markomannia.mw2d.document.util.DocumentUtils;

public class MediaWikiClient {

	private static final String _BASE_URL = "https://old.markomannenwiki.de";

	private static final String ALL_CATEGORIES_URL = _BASE_URL + "/index.php?title=Spezial:Kategorien";

	private static final String ALL_PAGES_START_URL = _BASE_URL + "/index.php?title=Spezial:Alle_Seiten";

	private static final String BASIC_AUTH = Base64.getEncoder().encodeToString("".getBytes());

	public static byte[] getAsset(final String url) throws IOException, InterruptedException {
		final HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url))
				.timeout(Duration.ofMillis(10 * 1000)).header("Authorization", "Basic " + BASIC_AUTH).build();

		try {
			final HttpResponse<byte[]> response = HttpClientUtils.sendOrRetry(httpRequest, BodyHandlers.ofByteArray());

			if (response.statusCode() < 200 || response.statusCode() >= 300) {
				return null;
			} else {
				return response.body();
			}
		} catch (final IOException e) {
			return null;
		}
	}

	public static List<MediaWikiCategoryRecord> getCategories() throws IOException, InterruptedException {
		return getCategories(ALL_CATEGORIES_URL);
	}

	private static List<MediaWikiCategoryRecord> getCategories(final String url)
			throws IOException, InterruptedException {
		System.out.println("Fetching " + url);

		final HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url))
				.timeout(Duration.ofMillis(10 * 1000)).header("Authorization", "Basic " + BASIC_AUTH).build();

		final HttpResponse<String> response = HttpClientUtils.sendOrRetry(httpRequest, BodyHandlers.ofString());

		final List<MediaWikiCategoryRecord> result = new ArrayList<>();

		if (response.statusCode() < 200 || response.statusCode() >= 300) {
			throw new RuntimeException();
		} else {
			final String body = response.body();
			final Document document = Jsoup.parse(body, _BASE_URL);

			document.select("ul > li").forEach(li -> {
				final Element href = DocumentUtils.getFirst(li.select("> a[href]"));
				final String title = href.attr("title");
				final String text = href.text();

				if (List.of(CategoryUtils.CATEGORIES_EXCLUDED).contains(text)) {
					System.out.println("Ignoring category " + text);
				} else if (title.contains("Kategorie:")) {
					final Matcher m = Pattern.compile("\\(([0-9\\.]+) Eintr").matcher(li.text());
					final int numberEntries = m.find() ? Integer.parseInt(m.group(1).replace(".", "")) : null;

					result.add(new MediaWikiCategoryRecord(title, text, numberEntries));
				}
			});

			final String nextUrl = document.select("a[href]").stream().filter(link -> {
				return link.text().contains("nächste");
			}).map(link -> {
				return link.absUrl("href");
			}).findFirst().orElse(null);

			if (nextUrl != null && !nextUrl.isBlank()) {
				result.addAll(getCategories(nextUrl));
			}
		}

		return result;
	}

	public static String getHtml(final String url) throws IOException, InterruptedException {
		final HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url))
				.timeout(Duration.ofMillis(10 * 1000)).header("Authorization", "Basic " + BASIC_AUTH).build();

		final HttpResponse<String> response = HttpClientUtils.sendOrRetry(httpRequest, BodyHandlers.ofString());

		if (response.statusCode() < 200 || response.statusCode() >= 300) {
			throw new RuntimeException();
		} else {
			return response.body();
		}
	}

	public static List<MediaWikiPageRecord> getPages() throws IOException, InterruptedException {
		return getPages(ALL_PAGES_START_URL);
	}

	private static List<MediaWikiPageRecord> getPages(final String url) throws IOException, InterruptedException {
		System.out.println("Fetching " + url);

		final HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url))
				.timeout(Duration.ofMillis(10 * 1000)).header("Authorization", "Basic " + BASIC_AUTH).build();

		final HttpResponse<String> response = HttpClientUtils.sendOrRetry(httpRequest, BodyHandlers.ofString());

		final List<MediaWikiPageRecord> result = new ArrayList<>();

		if (response.statusCode() < 200 || response.statusCode() >= 300) {
			throw new RuntimeException();
		} else {
			final String body = response.body();
			final Document document = Jsoup.parse(body, _BASE_URL);

			document.select("ul.mw-allpages-chunk > li > a[href]").forEach(link -> {
				final String absUrl = link.absUrl("href");
				final String classNames = link.attr("class");
				final boolean isRedirect = classNames.contains("mw-redirect");

				result.add(new MediaWikiPageRecord(absUrl, isRedirect));
			});

			final String nextUrl = document.select(".mw-allpages-nav > a[href]").stream().filter(link -> {
				return link.text().contains("Nächste Seite");
			}).map(link -> {
				return link.absUrl("href");
			}).findFirst().orElse(null);

			if (nextUrl != null && !nextUrl.isBlank()) {
				result.addAll(getPages(nextUrl));
			}
		}

		return result;
	}
}
