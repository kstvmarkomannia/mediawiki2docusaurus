package org.markomannia.mw2d.markdown.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MarkdownUtilsTest {

	@Test
	public void test1() throws Exception {
		assertEquals("some [Aushang](Kommersablauf.pdf \"Kommersablauf.pdf\") test", MarkdownUtils
				.fixRemainingAssetLinks("some [Aushang](/images/3/35/Kommersablauf.pdf \"Kommersablauf.pdf\") test"));
	}
}
