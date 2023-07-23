package org.markomannia.mw2d;

import java.util.Base64;

public class Config {

	public static final String BASE_PATH = "/Users/uwol/github/kstvmarkomannia/markomannenwiki/docs";

	public static final String MEDIAWIKI_URL = "https://old.markomannenwiki.de";

	public static final String OPTIONAL_AUTH = Base64.getEncoder().encodeToString("username:password".getBytes());
}
