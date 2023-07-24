package org.markomannia.mw2d.markdown.util;

public class MarkdownUtils {

	public static String cleanMarkdown(final String markdown) {
		final String htmlEntitiesDecoded = markdown.replace("%C3%B6", "oe").replace("%C3%BC", "ue").replace("%2C", "")
				.replace("%27", "").replace("%C3%A4", "").replace("%C3%9F", "ss").replace("%C3%B6", "oe")
				.replace("%C3%84", "Ä");

		final String linkReplaced1 = htmlEntitiesDecoded.replace(
				"http://www.markomannia.org/index.php?target=kvvereindetail&verein=",
				"https://www.markomannia.org/index.php?pid=verein&id=");

		final String linkReplaced2 = linkReplaced1.replace("http://www.markomannia.org/index.php?target=kvvereine",
				"https://www.markomannia.org/index.php?pid=dachverband_vereine");

		final String linkReplaced3 = linkReplaced2.replace("http://www.markomannia.org", "https://www.markomannia.org");

		final String linkReplaced4 = linkReplaced3.replace(
				"https://www.markomannia.org/index.php?target=intranetchargierkalendershow",
				"https://www.markomannia.org/index.php?pid=intranet_chargierkalender");

		final String linkReplaced5 = linkReplaced4.replace("https://www.markomannia.org/index.php?target=zimmerangebot",
				"https://www.markomannia.org/index.php?pid=zimmer");

		final String linkReplaced6 = linkReplaced5.replace(
				"https://www.markomannia.org/index.php?target=semesterhistory",
				"https://www.markomannia.org/index.php?pid=intranet_home");

		final String linkReplaced7 = linkReplaced6.replace("http://www.markomannenwiki.de/Dokumente/", "");

		return linkReplaced7.replace(") :", "):");
	}

	public static String fixRemainingAssetLinks(final String markdown) {
		return markdown.replaceAll("\\/images\\/[a-f0-9]\\/[a-f0-9]{2}\\/", "");
	}
}
