package org.markomannia.mw2d.client.util;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientUtils {

	private static HttpClient httpClient;

	public static HttpClient httpClient() {
		if (httpClient == null) {
			synchronized (HttpClientUtils.class) {
				if (httpClient == null) {
					httpClient = HttpClient.newBuilder().followRedirects(Redirect.NORMAL).build();
				}
			}
		}

		return httpClient;
	}

	/**
	 * Tries to send the request, and retries several times.
	 *
	 * DO NOT USE Thread.sleep in this method, this will lead to saturation and cost
	 * in servers!
	 */
	public static <T extends Object> HttpResponse<T> sendOrRetry(final HttpRequest request,
			final HttpResponse.BodyHandler<T> responseBodyHandler) throws IOException, InterruptedException {
		HttpResponse<T> result;

		try {
			result = httpClient().send(request, responseBodyHandler);
		} catch (final IOException | InterruptedException firstException) {
			try {
				result = httpClient().send(request, responseBodyHandler);
			} catch (final IOException | InterruptedException secondException) {
				try {
					result = httpClient().send(request, responseBodyHandler);
				} catch (final IOException | InterruptedException thirdException) {
					try {
						result = httpClient().send(request, responseBodyHandler);
					} catch (final IOException | InterruptedException fourthException) {
						throw fourthException;
					}
				}
			}
		}

		return result;
	}
}