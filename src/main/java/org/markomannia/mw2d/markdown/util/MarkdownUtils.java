package org.markomannia.mw2d.markdown.util;

public class MarkdownUtils {

	public static String cleanMarkdown(final String markdown) {
		return markdown.replace(") :", "):");
	}
}
