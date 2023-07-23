package org.markomannia.mw2d.categories.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.markomannia.mw2d.client.MediaWikiCategoryRecord;

public class CategoryUtils {

	public static final String CATEGORIES_EXCLUDED = "Seiten mit defekten Dateilinks";

	public static String cleanCategoryDirectoryName(final String url) {
		return url.replace("'", "").replace("´", "").replace(",", "").replace("!", "").replace("?", "")
				.replace("(", "_").replace(")", "_").replace(" ", "_").strip();
	}

	public static List<MediaWikiCategoryRecord> sortCategories(final List<MediaWikiCategoryRecord> categories) {
		final Comparator<MediaWikiCategoryRecord> comparator = Comparator
				.comparing(MediaWikiCategoryRecord::numberEntries, Comparator.reverseOrder());

		final List<MediaWikiCategoryRecord> result = new ArrayList<>(categories);
		result.sort(comparator);

		return result;
	}
}
