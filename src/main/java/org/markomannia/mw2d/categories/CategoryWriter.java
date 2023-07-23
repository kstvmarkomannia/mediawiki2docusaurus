package org.markomannia.mw2d.categories;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.markomannia.mw2d.Config;
import org.markomannia.mw2d.categories.util.CategoryUtils;
import org.markomannia.mw2d.client.MediaWikiCategoryRecord;

public class CategoryWriter {

	private static String createMarkdown(final MediaWikiCategoryRecord category) {
		return "# " + category.text();
	}

	private static String determineDirectoryPath(final MediaWikiCategoryRecord category) {
		return Config.BASE_PATH + "/" + CategoryUtils.cleanCategoryDirectoryName(category.text());
	}

	private static Path determineFilePath(final MediaWikiCategoryRecord category) {
		final String directoryPath = determineDirectoryPath(category);
		return Paths.get(directoryPath, "index.md");
	}

	public static void writeCategory(final MediaWikiCategoryRecord category) throws IOException, InterruptedException {
		final String markdown = createMarkdown(category);

		final String directoryPath = determineDirectoryPath(category);
		new File(directoryPath).mkdirs();

		final byte[] markdownBytes = markdown.getBytes();

		final Path path = determineFilePath(category);

		System.out.println("Writing category " + path);

		Files.write(path, markdownBytes);
	}
}
