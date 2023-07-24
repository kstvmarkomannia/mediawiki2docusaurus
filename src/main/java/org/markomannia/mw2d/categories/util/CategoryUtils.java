package org.markomannia.mw2d.categories.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.markomannia.mw2d.client.MediaWikiCategoryRecord;
import org.markomannia.mw2d.util.UrlUtils;

public class CategoryUtils {

	public static final String CATEGORIES_EXCLUDED = "Seiten mit defekten Dateilinks";

	public static String cleanCategoryDirectoryName(final String fileName) {
		return UrlUtils.cleanFileName(fileName);
	}

	public static List<MediaWikiCategoryRecord> sortCategories(final List<MediaWikiCategoryRecord> categories) {
		final Comparator<MediaWikiCategoryRecord> comparator = Comparator
				.comparing(MediaWikiCategoryRecord::numberEntries, Comparator.reverseOrder());

		final List<MediaWikiCategoryRecord> result = new ArrayList<>(categories);
		result.sort(comparator);

		return result;
	}
}
