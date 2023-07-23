package org.markomannia.mw2d.extensions.youtube;

public class YoutubeRewriter {

	public static String rewriteYoutubeLinks(final String markdown) {
		return markdown.replaceAll("\\{\\{#ev:youtube\\|([A-Za-z0-9-\\\\_]+)(\\|[0-9]+)?(\\|(right|left))?\\}\\}",
				"[YouTube-Video](https://youtu.be/$1)").replace("\\_", "_");
	}
}
