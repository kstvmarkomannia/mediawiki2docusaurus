package org.markomannia.mw2d.postprocess;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.markomannia.mw2d.Config;
import org.markomannia.mw2d.util.UrlUtils;

public class PostProcessFixSpecialCharsTest {

	public static void deleteEmptyDirectories(final File dir) throws IOException {
		for (final File file : dir.listFiles()) {
			if (file.isDirectory()) {
				final boolean isEmpty = file.listFiles().length == 0;

				if (isEmpty) {
					file.delete();
				} else {
					deleteEmptyDirectories(file);
				}
			}
		}
	}

	public static void fixDirectory(final File dir, final Map<String, String> replacements) throws IOException {
		for (final File file : dir.listFiles()) {
			final Path filePath = file.toPath();

			final String filePathCleaned = UrlUtils.cleanFileName(filePath.toString());
			final String filePathCleanedIntermediateForSz = filePathCleaned + "__";

			final Path filePathCleanedIntermediateForSzAsPAth = Path.of(filePathCleanedIntermediateForSz);
			final Path filePathCleanedAsPath = Path.of(filePathCleaned);

			if (!Objects.equals(filePath, filePathCleanedAsPath)) {
				replacements.put(filePath.getFileName().toString(), filePathCleanedAsPath.getFileName().toString());

				Files.move(filePath, filePathCleanedIntermediateForSzAsPAth);
				Files.move(filePathCleanedIntermediateForSzAsPAth, filePathCleanedAsPath);
			}
		}

		for (final File file : dir.listFiles()) {
			if (file.isDirectory()) {
				fixDirectory(file, replacements);
			}
		}
	}

	public static void rewriteReferences(final File dir, final Map<String, String> replacements) throws IOException {
		for (final File file : dir.listFiles()) {
			if (file.isFile() && file.toString().endsWith(".md")) {
				final Path filePath = file.toPath();
				final String oldContent = Files.readString(filePath, StandardCharsets.UTF_8);
				String newContent = oldContent;

				for (final Entry<String, String> entry : replacements.entrySet()) {
					newContent = newContent.replace("/" + entry.getKey(), "/" + entry.getValue());
				}

				if (!Objects.equals(newContent, oldContent)) {
					Files.writeString(filePath, newContent, StandardCharsets.UTF_8);
				}
			}
		}

		for (final File file : dir.listFiles()) {
			if (file.isDirectory()) {
				rewriteReferences(file, replacements);
			}
		}
	}

	@Test
	public void test() throws Exception {
		final File baseDir = new File(Config.BASE_PATH);
		final Map<String, String> replacements = new HashMap<>();

		deleteEmptyDirectories(baseDir);
		fixDirectory(baseDir, replacements);
		rewriteReferences(baseDir, replacements);
	}
}
