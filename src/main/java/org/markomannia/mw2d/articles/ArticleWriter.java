package org.markomannia.mw2d.articles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.markomannia.mw2d.Config;
import org.markomannia.mw2d.articles.util.ArticleUtils;
import org.markomannia.mw2d.assets.AssetRecord;
import org.markomannia.mw2d.client.MediaWikiClient;
import org.markomannia.mw2d.extensions.youtube.YoutubeRewriter;
import org.markomannia.mw2d.markdown.util.MarkdownUtils;

import io.github.furstenheim.CopyDown;

public class ArticleWriter {

	private static String createMarkdown(final ArticleRecord article) {
		final String html = article.elements().html();
		final String parserOutputMarkdown = new CopyDown().convert(html);
		final String parserOutputMarkdownWithoutHtml = parserOutputMarkdown.replaceAll("<([^>]+)>", "$1");

		final String markdown = "# " + article.fromHeading() + "\n\n" + parserOutputMarkdownWithoutHtml;
		final String markdownWithYoutube = YoutubeRewriter.rewriteYoutubeLinks(markdown);

		return MarkdownUtils.cleanMarkdown(markdownWithYoutube);
	}

	public static String determineDirectoryPath(final ArticleRecord article) {
		return Config.BASE_PATH + ArticleUtils.determineArticleWithCategoryPath(article);
	}

	public static Path determineFilePath(final ArticleRecord article) {
		final String directoryPath = determineDirectoryPath(article);
		return Paths.get(directoryPath, "index.md");
	}

	public static void writeArticle(final ArticleRecord article) throws IOException, InterruptedException {
		final String markdown = createMarkdown(article);
		final Collection<AssetRecord> assets = article.assets();

		final String directoryPath = determineDirectoryPath(article);
		new File(directoryPath).mkdirs();

		final byte[] markdownBytes = markdown.getBytes();

		for (final AssetRecord asset : assets) {
			final String assetAbsUrl = asset.absUrl();
			final String assetFileName = asset.fileName();
			final Path assetFilePath = Path.of(directoryPath, assetFileName);

			if (!assetFilePath.toFile().exists()) {
				final byte[] assetBytes = MediaWikiClient.getAsset(assetAbsUrl);

				if (assetBytes == null) {
				} else {
					System.out.println("Writing asset " + assetFilePath);

					Files.write(assetFilePath, assetBytes);
				}
			}
		}

		final Path path = determineFilePath(article);

		System.out.println("Writing article " + path);

		Files.write(path, markdownBytes);
	}
}
