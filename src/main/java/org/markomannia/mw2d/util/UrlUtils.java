package org.markomannia.mw2d.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {

	public static Map<String, String> getQueryMapForQuery(final String query) {
		final Pattern pat = Pattern.compile("([^&=]+)=([^&]*)");
		final Matcher matcher = pat.matcher(query);
		final Map<String, String> result = new HashMap<>();

		while (matcher.find()) {
			result.put(matcher.group(1), urlDecode(matcher.group(2)));
		}

		return result;
	}

	public static Map<String, String> getQueryMapForUrl(final String urlString) {
		final URL url = parse(urlString);
		final String query = url.getQuery();

		return getQueryMapForQuery(query);
	}

	public static URL parse(final String url) {
		try {
			return new URL(url);
		} catch (final MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static String urlDecode(final String url) {
		try {
			return URLDecoder.decode(url, StandardCharsets.UTF_8.name());
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
