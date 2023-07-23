package org.markomannia.mw2d;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.markomannia.mw2d.articles.ArticleRecord;
import org.markomannia.mw2d.articles.ArticleWriter;
import org.markomannia.mw2d.assets.AssetRecord;
import org.markomannia.mw2d.assets.util.AssetUtils;
import org.markomannia.mw2d.categories.CategoryWriter;
import org.markomannia.mw2d.categories.util.CategoryUtils;
import org.markomannia.mw2d.client.MediaWikiCategoryRecord;
import org.markomannia.mw2d.client.MediaWikiClient;
import org.markomannia.mw2d.client.MediaWikiPageRecord;
import org.markomannia.mw2d.document.util.DocumentUtils;
import org.markomannia.mw2d.util.UrlUtils;

public class Main {

	public static void main(final String[] args) throws IOException, InterruptedException {
		final List<MediaWikiCategoryRecord> categories = MediaWikiClient.getCategories();
		final List<MediaWikiCategoryRecord> categoriesSorted = CategoryUtils.sortCategories(categories);

		System.out.println("Found " + categories.size() + " categories");

		final List<MediaWikiPageRecord> pages = MediaWikiClient.getPages();

		final List<MediaWikiPageRecord> realPages = pages.stream().filter(p -> !p.isRedirect())
				.collect(Collectors.toList());
		final List<MediaWikiPageRecord> redirects = pages.stream().filter(p -> p.isRedirect())
				.collect(Collectors.toList());

		System.out.println("Found " + realPages.size() + " articles");
		System.out.println("Found " + redirects.size() + " redirects");

		final List<ArticleRecord> articles = new ArrayList<>();
		final Map<String, ArticleRecord> articlesByTitles = new HashMap<>();

		for (final MediaWikiPageRecord page : realPages) {
			final String url = page.url();

			final String html = MediaWikiClient.getHtml(url);
			final ArticleRecord article = onPageCreateArticle(html, url, false, categoriesSorted);

			articlesByTitles.put(article.fromTitle(), article);
			articles.add(article);

			ArticleWriter.writeArticle(article);
		}

		for (final MediaWikiPageRecord page : redirects) {
			final String url = page.url() + "&redirect=no";

			final String html = MediaWikiClient.getHtml(url);
			final ArticleRecord article = onPageCreateArticle(html, url, true, categoriesSorted);

			articlesByTitles.put(article.fromTitle(), article);
			articles.add(article);

			ArticleWriter.writeArticle(article);
		}

		final Set<MediaWikiCategoryRecord> categoriesUsed = articles.stream().map(ArticleRecord::fromCategory)
				.collect(Collectors.toSet());

		for (final MediaWikiCategoryRecord category : categoriesUsed) {
			CategoryWriter.writeCategory(category);
		}

		for (final ArticleRecord article : articles) {
			DocumentUtils.rewriteLinks(article.elements(), articlesByTitles);

			ArticleWriter.writeArticle(article);
		}
	}

	private static ArticleRecord onPageCreateArticle(final String html, final String fromUrl, final boolean isRedirect,
			final List<MediaWikiCategoryRecord> categories) {
		final Document document = Jsoup.parse(html, fromUrl);
		DocumentUtils.cleanDocument(document);

		/*
		 * title
		 */
		final Map<String, String> queryMap = UrlUtils.getQueryMapForUrl(fromUrl);

		final String fromTitle = queryMap.get("title");
		Objects.requireNonNull(fromTitle);

		/*
		 * heading
		 */
		final Elements firstHeadings = document.select("#firstHeading");
		Objects.requireNonNull(firstHeadings);

		final Element firstHeading = DocumentUtils.getFirst(firstHeadings);
		Objects.requireNonNull(firstHeading);

		final String fromFirstHeadingText = firstHeading.text();
		Objects.requireNonNull(fromFirstHeadingText);

		/*
		 * primary category
		 */
		final List<String> pageCategories = document.select(".mw-normal-catlinks ul > li > a").stream().map(c -> {
			return c.text();
		}).filter(c -> {
			return !CategoryUtils.CATEGORIES_EXCLUDED.contains(c);
		}).collect(Collectors.toList());

		final MediaWikiCategoryRecord fallback = isRedirect
				? new MediaWikiCategoryRecord("Weiterleitung", "Weiterleitung", 0)
				: new MediaWikiCategoryRecord("Allgemein", "Allgemein", 0);

		final MediaWikiCategoryRecord fromCategory = categories.stream().filter(c -> {
			return pageCategories.contains(c.text());
		}).findFirst().orElse(fallback);

		/*
		 * article body
		 */
		final Elements parserOutput = document.select(".mw-parser-output");
		Objects.requireNonNull(parserOutput);

		final Map<String, AssetRecord> assetRecords = AssetUtils.rewriteAssets(parserOutput);

		/*
		 * markdown
		 */
		return new ArticleRecord(fromTitle, fromUrl, fromFirstHeadingText, fromCategory, parserOutput,
				assetRecords.values());
	}
}
