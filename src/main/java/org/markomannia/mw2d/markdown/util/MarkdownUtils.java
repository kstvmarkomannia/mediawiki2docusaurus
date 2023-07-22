package org.markomannia.mw2d.markdown.util;

public class MarkdownUtils {

	public static String rewriteMarkdown(final String markdown) {
		return markdown.replace(") :", "):");
	}
}
