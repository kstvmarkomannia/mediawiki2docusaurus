package org.markomannia.mw2d.extensions.youtube;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class YoutubeRewriterTest {

	@Test
	public void test() throws Exception {
		assertEquals("some [YouTube-Video](https://youtu.be/dVQOLiwaEgQ) test",
				YoutubeRewriter.rewriteYoutubeLinks("some {{#ev:youtube|dVQOLiwaEgQ|100|right}} test"));
	}
}
