package org.markomannia.mw2d.extensions.youtube;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class YoutubeRewriterTest {

	@Test
	public void test1() throws Exception {
		assertEquals("some [YouTube-Video](https://youtu.be/dVQOLiwaEgQ) test",
				YoutubeRewriter.rewriteYoutubeLinks("some {{#ev:youtube|dVQOLiwaEgQ|100|right}} test"));
	}

	@Test
	public void test2() throws Exception {
		assertEquals("some [YouTube-Video](https://youtu.be/5K6F-OznPPM) test",
				YoutubeRewriter.rewriteYoutubeLinks("some {{#ev:youtube|5K6F-OznPPM|100|right}} test"));
	}

	@Test
	public void test3() throws Exception {
		assertEquals("some [YouTube-Video](https://youtu.be/enFh_JdqMHM) test",
				YoutubeRewriter.rewriteYoutubeLinks("some {{#ev:youtube|enFh\\_JdqMHM|100|right}} test"));
	}
}
