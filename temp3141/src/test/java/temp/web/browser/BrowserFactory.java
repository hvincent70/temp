package com.sprint.iice_tests.web.browser;

import java.util.HashMap;
import java.util.Map;

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

public class BrowserFactory {

	private static int elementWaitTime = 20;

	public static int getWaitTime() {
		return elementWaitTime;
	}

	public static void setWaitTime(int waitTime) {
		elementWaitTime = waitTime;
	}	
	
	public String browserType;
	public WebDriver driver;
	
	private static Map<Long,WebDriver> concMap = new HashMap<>();
	private static Map<Long,String> browMap = new HashMap<>();
	
	public static WebDriver getDriverInstance() {
		
		System.out.println("Here is the driver instance id: " + Thread.currentThread().getId());
		return concMap.get(Thread.currentThread().getId());
	}
	
	public static String getBrowserType() {
		
		System.out.println("Here is the browser type id: " + Thread.currentThread().getId());
		return browMap.get(Thread.currentThread().getId());
	}
	
	public WebDriver getWebDriver(String browser, String puid) {
		
		try {
			switch(browser) {
			case "chrome":
				ChromeOptions options = new ChromeOptions();
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
				final String DOWNLOAD_FOLDER = "/src/test/resources/TestData";
				final String FILE_PATH = System.getProperty("user.dir") + DOWNLOAD_FOLDER;

				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("download.default_directory", FILE_PATH);
				options.setExperimentalOption("prefs", chromePrefs);

				driver = new ChromeDriver(options);
				driver.get(START_URL);
				browserType = browser;
				concMap.put(Thread.currentThread().getId(), driver);
				browMap.put(Thread.currentThread().getId(), browserType);
				
				System.out.println("Here is the instance created prior to start: " + concMap.get(Thread.currentThread().getId()));
				System.out.println("Here is the browser type created prior to start: " + browMap.get(Thread.currentThread().getId()));
				
				break;
			case "firefox":
				System.out.println("Case not setup yet.");
				throw new RuntimeException("Browser type not supported currently.");
			case "internet explorer":
				System.out.println("Case not setup yet.");
				throw new RuntimeException("Browser type not supported currently.");
			default:
				System.out.println("Case not available.");
				throw new RuntimeException("Browser type not supported.");
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return driver;
	}
	
	public static void goTo(String url) {
		getDriverInstance().get(url);
	}

	public static void maximize() {
		getDriverInstance().manage().window().maximize();
	}

	public static void quit() {
		getDriverInstance().quit();
	}
	
	public static void close() {
		getDriverInstance().close();
	}

	protected void refreshPage() {
		getDriverInstance().navigate().refresh();
	}

	public static String getTitle() {
		return getDriverInstance().getTitle();
	}

	public String getCurrentUrl() {
		return getDriverInstance().getCurrentUrl();
	}
	
}
