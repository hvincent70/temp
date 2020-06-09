package com.sprint.iice_tests.web.browser;

import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import com.sprint.iice_tests.config.Config;
import com.sprint.iice_tests.config.Config.PROPERTIES;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

public class Browser_old {

	private static int elementWaitTime = 20;

	public static int getWaitTime() {
		return elementWaitTime;
	}

	public static void setWaitTime(int waitTime) {
		elementWaitTime = waitTime;
	}

	private static WebDriver driver;

	public static WebDriver getDriver() {
		return driver;
	}

	public static void initDriver() {
		ChromeOptions options = new ChromeOptions();
		driver = new ChromeDriver(options);
	}

	public static void initDigitalExperience(String puid) throws IOException {
		ChromeOptions options = new ChromeOptions();

		/**
		 * Configure Browser Mob Proxy to use an appended header on load
		 */
		final String START_URL = Config.getProperty(PROPERTIES.URL);
		final String HEADER_KEY = "PUID";
		final String HEADER_VAL = puid;

		BrowserMobProxy proxy = new BrowserMobProxyServer();
		proxy.start(0);
		proxy.addRequestFilter(new RequestFilter() {
			@Override
			public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents,
					HttpMessageInfo messageInfo) {
				request.headers().add(HEADER_KEY, HEADER_VAL);
				return null;
			}
		});
		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
		options.setCapability(CapabilityType.PROXY, seleniumProxy);
		/**
		 * Configure the location for files downloaded from the browser
		 */
		final String DOWNLOAD_FOLDER = "/src/test/resources/TestData";
		final String FILE_PATH = System.getProperty("user.dir") + DOWNLOAD_FOLDER;

		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("download.default_directory", FILE_PATH);
		options.setExperimentalOption("prefs", chromePrefs);

		driver = new ChromeDriver(options);
		driver.get(START_URL);

	}

	public static void goTo(String url) {
		driver.get(url);
	}

	public static void maximize() {
		driver.manage().window().maximize();
	}

	public static void quit() {
		driver.quit();
	}

	public static void close() {
		driver.close();
	}

	protected void refreshPage() {
		driver.navigate().refresh();
	}

	public static String getTitle() {
		return driver.getTitle();
	}

	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

}
