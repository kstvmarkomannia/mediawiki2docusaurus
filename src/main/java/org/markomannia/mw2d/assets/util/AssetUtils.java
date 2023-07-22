package org.markomannia.mw2d.assets.util;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jsoup.select.Elements;
import org.markomannia.mw2d.assets.AssetRecord;
import org.markomannia.mw2d.util.UrlUtils;

public class AssetUtils {

	public static String cleanAssetFileName(final String url) {
		return url.replace("'", "").replace("´", "").replace(",", "").replace("!", "").replace("?", "")
				.replace("(", "_").replace(")", "_").replace("ü", "ue").replace("ä", "ae").replace("ö", "oe")
				.replace("Ü", "Ue").replace("Ä", "Ae").replace("Ö", "Oe").replace("ß", "ss").replace("´", "");
	}

	public static Map<String, AssetRecord> rewriteAssets(final Elements parserOutput) {
		/*
		 * assets
		 */
		final Map<String, AssetRecord> result = new HashMap<>();

		/*
		 * imgs
		 */
		final Elements assets = parserOutput.select("img");
		Objects.requireNonNull(assets);

		assets.forEach(a -> {
			final String assetRelUrl = a.attr("src");
			final String assetAbsUrl = a.absUrl("src");

			final String assetFileName = rewriteAssetUrlToFileName(assetRelUrl);
			final AssetRecord asset = new AssetRecord(assetRelUrl, assetAbsUrl, assetFileName);

			result.put(assetRelUrl, asset);
		});

		/*
		 * files
		 */
		final Elements files = parserOutput
				.select("a[href*=\".mid\"],a[href*=\".png\"],a[href*=\".jpg\"],a[href*=\".zip\"]");
		Objects.requireNonNull(files);

		files.forEach(f -> {
			final String assetRelUrl = f.attr("href");
			final String assetAbsUrl = f.absUrl("href");

			if (!assetRelUrl.contains("Datei:")) {
				final String assetFileName = rewriteAssetUrlToFileName(assetRelUrl);
				final AssetRecord asset = new AssetRecord(assetRelUrl, assetAbsUrl, assetFileName);

				result.put(assetRelUrl, asset);
			}
		});

		/*
		 * clean HTML for conversion to markdown
		 */

		// fix img URLs
		parserOutput.select("img").forEach(img -> {
			final String src = img.attr("src");
			Objects.requireNonNull(src);

			final AssetRecord asset = result.get(src);
			Objects.requireNonNull(asset);

			img.attr("src", asset.fileName());
		});

		// fix mid URLs
		parserOutput.select("a[href*=\"mid\"]").forEach(img -> {
			final String href = img.attr("href");
			Objects.requireNonNull(href);

			final AssetRecord asset = result.get(href);
			Objects.requireNonNull(asset);

			img.attr("href", asset.fileName());
		});

		return result;
	}

	public static String rewriteAssetUrlToFileName(final String url) {
		final String assetRelUrlFileName = Path.of(url).getFileName().toString();

		return cleanAssetFileName(UrlUtils.urlDecode(assetRelUrlFileName));
	}
}
