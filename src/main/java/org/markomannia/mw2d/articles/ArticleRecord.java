package org.markomannia.mw2d.articles;

import java.util.Collection;

import org.jsoup.select.Elements;
import org.markomannia.mw2d.assets.AssetRecord;
import org.markomannia.mw2d.client.MediaWikiCategoryRecord;

public record ArticleRecord(String fromTitle, String fromUrl, String fromHeading, MediaWikiCategoryRecord fromCategory,
		Elements elements, Collection<AssetRecord> assets) {
}
