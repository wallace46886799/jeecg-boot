package org.jeecg.modules.dreamlabs.gtja.okhttp3;




import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.hutool.core.text.StrSpliter;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
* 
* Cookie Helper for OkHttp3 <br>
* <br>
* <p>
* usage example:<br>
* 
* <pre>
* <code>
* String url = "https://example.com/webapi";
* 
* 		OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
* 		cookieHelper.setCookie(url, "cookie_name", "cookie_value");
* 		
* 		OkHttpClient client = new OkHttpClient.Builder()
* 				.cookieJar(cookieHelper.cookieJar())
* 				.build();
* 
* 		Request request = new Request.Builder()
* 				.url(url)
* 				.build();
* </code>
* </pre>
* 
* @author Tom Misawa (riversun.org@gmail.com)
*/
public class OkHttp3CookieHelper {

	private final Map<String, List<Cookie>> mServerCookieStore = new ConcurrentHashMap<String, List<Cookie>>();

	private Map<String, List<Cookie>> mClientCookieStore = new ConcurrentHashMap<String, List<Cookie>>();

	private final CookieJar mCookieJar = new CookieJar() {
		@Override
		public List<Cookie> loadForRequest(HttpUrl url) {

			List<Cookie> serverCookieList = mServerCookieStore.get(url.host());

			if (serverCookieList == null) {
				serverCookieList = new ArrayList<Cookie>();
			}

			final List<Cookie> clientCookieStore = mClientCookieStore.get(url.host());

			if (clientCookieStore != null) {
				serverCookieList.addAll(clientCookieStore);
			}

			return serverCookieList != null ? serverCookieList : new ArrayList<Cookie>();
		}

		@Override
		public void saveFromResponse(HttpUrl url, List<Cookie> unmodifiableCookieList) {
			// Why 'new ArrayList<Cookie>'?
			// Since 'unmodifiableCookieList' can not be changed, create a new
			// one
			mServerCookieStore.put(url.host(), new ArrayList<Cookie>(unmodifiableCookieList));

			// The persistence code should be described here if u want.
		}

	};

	/**
	 * Set cookie
	 * 
	 * @param url
	 * @param cookie
	 */
	public void setCookie(String url, Cookie cookie) {

		final String host = HttpUrl.parse(url).host();

		List<Cookie> cookieListForUrl = mClientCookieStore.get(host);
		if (cookieListForUrl == null) {
			cookieListForUrl = new ArrayList<Cookie>();
			mClientCookieStore.put(host, cookieListForUrl);
		}
		putCookie(cookieListForUrl, cookie);

	}

	/**
	 * Set cookie
	 * 
	 * @param url
	 * @param cookieName
	 * @param cookieValue
	 */
	public void setCookie(String url, String cookieName, String cookieValue) {
		final HttpUrl httpUrl = HttpUrl.parse(url);
		setCookie(url, Cookie.parse(httpUrl, cookieName + "=" + cookieValue));
	}
	
	
	/**
	 * Set cookie
	 * 
	 * @param url
	 * @param cookieName
	 * @param cookieValue
	 */
	public void setCookie(String url, String cookieStr) {
		final HttpUrl httpUrl = HttpUrl.parse(url);
		List<String> cookieStrs = StrSpliter.split(cookieStr, "; ", true, true);
		for (String cStr : cookieStrs) {
			setCookie(url, Cookie.parse(httpUrl, cStr));
		}
	}
	

	/**
	 * Set cookie
	 * 
	 * @param httpUrl
	 * @param cookieName
	 * @param cookieValue
	 */
	public void setCookie(HttpUrl httpUrl, String cookieName, String cookieValue) {
		setCookie(httpUrl.host(), Cookie.parse(httpUrl, cookieName + "=" + cookieValue));
	}

	/**
	 * Returns CookieJar
	 * 
	 * @return
	 */
	public CookieJar cookieJar() {
		return mCookieJar;
	}

	private void putCookie(List<Cookie> storedCookieList, Cookie newCookie) {

		Cookie oldCookie = null;
		for (Cookie storedCookie : storedCookieList) {

			// create key for comparison
			final String oldCookieKey = storedCookie.name() + storedCookie.path();
			final String newCookieKey = newCookie.name() + newCookie.path();

			if (oldCookieKey.equals(newCookieKey)) {
				oldCookie = storedCookie;
				break;
			}
		}
		if (oldCookie != null) {
			storedCookieList.remove(oldCookie);
		}
		storedCookieList.add(newCookie);
	}

}