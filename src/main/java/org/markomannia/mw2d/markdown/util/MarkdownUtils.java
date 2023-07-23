package org.markomannia.mw2d.markdown.util;

public class MarkdownUtils {

	public static String cleanMarkdown(final String markdown) {
		return markdown.replace(") :", "):");
	}

	public static String fixRemainingAssetLinks(final String markdown) {
		return markdown.replaceAll("\\/images\\/[a-f0-9]\\/[a-f0-9]{2}\\/", "");
	}
}
