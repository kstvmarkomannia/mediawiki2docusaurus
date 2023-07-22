package org.markomannia.mw2d.articles.util;

import org.markomannia.mw2d.articles.ArticleRecord;

public class ArticleUtils {

	public static String cleanArticleFileName(final String url) {
		return url.replace("'", "").replace("´", "").replace(",", "").replace("!", "").replace("?", "")
				.replace("(", "_").replace(")", "_");
	}

	public static String determineArticleWithCategoryPath(final ArticleRecord article) {
		final String categoryDirectory = article.fromCategory() == null ? "" : "/" + article.fromCategory().text();
		return categoryDirectory + "/" + cleanArticleFileName(article.fromTitle());
	}
}
