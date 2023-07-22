package org.markomannia.mw2d.document.util;

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.markomannia.mw2d.articles.ArticleRecord;
import org.markomannia.mw2d.articles.util.ArticleUtils;
import org.markomannia.mw2d.util.UrlUtils;

public class DocumentUtils {

	public static void cleanDocument(final Document document) {
		// remove toc
		document.select("#toc").remove();

		// remove Tondateibeschreibungsseite mit Lizenzangabe around files
		document.select(".mw-parser-output sup a").remove();

		// remove loudspeaker images
		document.select(".mw-parser-output .image[href*=\"Loudspeaker\"]").remove();

		// remove Vorlage_Begriffsklaerung
		document.select("#Vorlage_Begriffsklaerung").remove();

		// remove a href around images
		document.select(".mw-parser-output .image").removeAttr("href");
	}

	public static Element getFirst(final Elements elements) {
		return elements.stream().findFirst().orElse(null);
	}

	public static void rewriteLinks(final Elements elements, final Map<String, ArticleRecord> articlesByTitles) {
		// rewrite a href to /index.php?title=
		elements.select("a[href*=\"/index.php?title=\"]").forEach(a -> {
			final String url = UrlUtils.urlDecode(a.attr("href"));
			final String query = url.replace("/index.php?", "");

			final Map<String, String> queryMap = UrlUtils.getQueryMapForQuery(query);
			final String title = queryMap.get("title");

			final ArticleRecord article = articlesByTitles.get(title);

			if (article == null) {
				a.removeAttr("href");
			} else {
				final String newPath = ArticleUtils.determineArticleWithCategoryPath(article);
				a.attr("href", newPath);
			}
		});
	}
}
