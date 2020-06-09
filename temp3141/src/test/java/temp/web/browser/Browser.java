package com.sprint.iice_tests.web.browser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.activation.MimetypesFileTypeMap;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.pdfbox.pdmodel.PDPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import com.sprint.iice_tests.lib.dao.data.Constants;
import com.sprint.iice_tests.lib.dao.vo.Account;
import com.sprint.iice_tests.utilities.test_util.CustomListener;
import com.sprint.iice_tests.utilities.test_util.DataFileFinder;
import com.sprint.iice_tests.web.pages.il_digital_paper.HomePage;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

public class Browser {
	public final static String fs = System.getProperty("file.separator");
	public final static String FOLDER = fs+"src"+fs+"test"+fs+"resources"+fs+"TestData";
	public final static String SYSTEM_DOWNLOAD_FOLDER = fs + "src" + fs + "test" + fs + "resources" + fs + "TestData";
	public final static String FILE_PATH = System.getProperty("user.dir") + SYSTEM_DOWNLOAD_FOLDER;
	public final static String SYSTEM_FILE_PATH = System.getProperty("user.dir") + SYSTEM_DOWNLOAD_FOLDER;
	public final static String FIREFOX_PATH = System.getProperty("user.dir") + FOLDER;
	public static String site = System.getProperty("site");
	public static String uat1 = "https://tvmkf352.test.sprint.com/platform/#/en-us/";
	public static String uat2 = "https://tvmkf353.test.sprint.com/platform/";
	public static String uat039 = "https://tvmkg039.test.sprint.com/platform";
	public static String uat037 = "https://tvmkg037.test.sprint.com/platform/";
	private static String OS = System.getProperty("os.name").toLowerCase();
	private String pathToChromeBinaryForUnix="/data/uyv/test0/chrome/opt/google/chrome/chrome";

	public static String locationOfDrivers = (System.getProperty("user.dir") + fs + "src" + fs + "test" + fs
			+ "resources" + fs + "drivers");
	public static String ps = System.getProperty("path.separator");

	public static String retrieveDrLocForOS() {
		String newDrLoc = System.getenv("SystemDrive") + fs + "ProgramData";
		if (isUnix()) {
			newDrLoc =   "/home/britebil/google-chrome";
		}
		return newDrLoc;
	}

	static String execChromeDriverLoc = retrieveDrLocForOS() + fs + (isWindows()?("chromedriver.exe"):("chromedriver"));
	public static File execChromeDriver = new File(execChromeDriverLoc);

	private static int elementWaitTime = 120;

	public static int getWaitTime() {
		return elementWaitTime;
	}

	public static void setWaitTime(int waitTime) {
		elementWaitTime = waitTime;
	}

	public String browserType;
	public WebDriver driver;
	public WebDriverWait wbWait;
	public BrowserMobProxy proxy;
	public Boolean remoteDriverEnabled;

	// Might need to change the concMap, browMap, and proxyMap back to private
	public static Map<Long, WebDriver> concMap = new ConcurrentHashMap<>();
	public static Map<Long, String> browMap = new ConcurrentHashMap<>();
	public static Map<Long, BrowserMobProxy> proxyMap = new ConcurrentHashMap<>();
	public static Map<Long, SessionId> sessionMap = new ConcurrentHashMap<>();
	public static Map<String, String> concStringMap = new ConcurrentHashMap<>();
	public static Map<WebElement, WebElement> concWebElementMap = new ConcurrentHashMap<>();
	public static Map<Long, String> banMap = new ConcurrentHashMap<>();
	public static Map<Long, String> classMap = new ConcurrentHashMap<>();
	public static Map<Long, File> pdfMap = new ConcurrentHashMap<>();
	public static Map<Long, Map<String, List<File>>> csvMap = new ConcurrentHashMap<>();
	public static Map<Long, Account> accountMap = new ConcurrentHashMap<>();
	public static Map<Long, Map<PDPage, Integer>> mapOfPdfPages = new ConcurrentHashMap<>();
	public static Queue<WebDriver> stack = new ArrayDeque<WebDriver>();
	public final static String newLine = System.getProperty("line.separator");

	public static Queue<WebDriver> getStack() {
		return stack;
	}

	public static WebDriver getDriverInstance() {
		return concMap.get(Thread.currentThread().getId());
	}

	public static String getBrowserType() {

		return browMap.get(Thread.currentThread().getId());
	}

	public static BrowserMobProxy getBrowserProxy() {
		return proxyMap.get(Thread.currentThread().getId());
	}

	public static List<Integer> getPdfPages() {
		return Browser.mapOfPdfPages.get(Thread.currentThread().getId()).values().stream().collect(Collectors.toList());
	}

	public synchronized WebDriver getWebDriver(String browser, String puid, String site, Boolean remoteDriverEnabled,
			Boolean runHeadless) throws IOException {

		final String HEADER_KEY = "PUID";
		final String HEADER_VAL = puid;

		HashMap<String, Object> prefs = new HashMap<String, Object>();
		long threadId = Thread.currentThread().getId();

		try {
			switch (browser) {
			case "chrome":
				if (remoteDriverEnabled) {

					setEnVarPathForDrivers();

				}
				
				ChromeOptions options = new ChromeOptions();
				if (runHeadless) {
					options.addArguments("--headless");
					options.addArguments("--window-size=1325x744");
				}
				
//				options.setBinary("/data/uyv/test0/jenkins/src/test/resources/drivers");
				
				// options.addArguments("--disable-gpu");
				// options.addArguments("start-maximized");
				// options.addArguments("--screenshot");
				// options.setExperimentalOption("useAutomationExtension", false);
				// options.addArguments("--window-size=1200x600");
				// options.addArguments("--ignore-certificate-errors");
				// options.addArguments("--no-sandbox");
				// options.addArguments("--disable-dev-shm-usage");
				// options.addArguments("--enable-logging");

				// options.addArguments("--disable-setuid-sandbox");
				// options.setHeadless(true);

				BrowserMobProxy proxy = getProxyServer();

				// Trying this out to block issue libraries
				proxy.blacklistRequests("https?://www\\.idsync.rlcdn\\.com/.*", 410);
				proxy.blacklistRequests("https://idsync.rlcdn.com", 410);
				proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT); // MM
				proxy.addRequestFilter(new RequestFilter() {
					@Override
					public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents,
							HttpMessageInfo messageInfo) {
						request.headers().add(HEADER_KEY, HEADER_VAL);
						return null;
					}

				});
				Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

				// TODO: Trying this to stop hanging. Also added to emulation case.
				options.addArguments("--no-sandbox");
				options.addArguments("--verbose");
				options.addArguments("--disable-ipv6");
				options.addArguments("--disable-async-dns");
				options.addArguments("--disable-dev-shm-usage");
				options.addArguments("--disable-gpu");
				options.addArguments("--disable-extensions");
				
				options.addArguments("--always-authorize-plugins");
				options.addArguments("--disable-browser-side-navigation");
				options.addArguments("--dns-prefetch-disable");
				options.addArguments("--start-maximized");
				options.addArguments("--ignore-certificate-errors");
				
				// options.addArguments("--disable-popup-blocking");
				// options.addArguments("chrome.switches","--disable-extensions");
				options.setAcceptInsecureCerts(true);
				// options.addArguments("--incognito");
				options.addArguments("test-type");
				// options.addArguments("no-default-browser-check");
				// options.addArguments("--disable-extensions");
				options.setCapability(CapabilityType.PROXY, seleniumProxy);
				options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				options.setCapability("acceptInsecureCerts", true);
				
				// options.setCapability(CapabilityType.TAKES_SCREENSHOT, seleniumProxy);
				// options.setCapability(CapabilityType.SUPPORTS_ALERTS, seleniumProxy);
				// options.setCapability("chrome.switches",
				// Arrays.asList("--disable-local-storage"));
				prefs.put("download.default_directory", FILE_PATH);

				// *******automatically downloads pdf to test download directory, Test
				// Data.*****
				// it is commented out below so it does not disrupt the current pdf download
				// process
				prefs.put("plugins.always_open_pdf_externally", true);
				prefs.put("pdfjs.disabled", true);
				if(isUnix()) {
					//options.setBinary("/data/uyv/test0/chrome/chromedriver");
					//we need to set the binary for driver to find Chrome in non-default location
					options.setBinary(pathToChromeBinaryForUnix);
					options.setCapability("chrome.binary", pathToChromeBinaryForUnix);
//					options.setBinary("/data/uyv/test0/chrome/opt/google/chrome");
//					options.addExtensions(new File("/data/uyv/test0/chrome/chromedriver"));
				}
				options.setExperimentalOption("prefs", prefs);
				
				options.setExperimentalOption("useAutomationExtension", false);
				
				options.setPageLoadStrategy(PageLoadStrategy.NONE);
//				options.addArguments("port=9515");
				driver = new ChromeDriver(options);

				browserType = browser;
				concMap.put(threadId, driver);
				browMap.put(threadId, browserType);
				proxyMap.put(threadId, proxy);
				waitForLoad(driver);

				try {
					SessionId session = ((ChromeDriver) driver).getSessionId();
					sessionMap.put(threadId, session);
					try {
						System.out.println("Getting url.");
						driver.get(site);
					} catch (Exception nsd) {
						if (!isSessionEmpty(session)) {
							System.out.println("THIS SESSION IS NOT EMPTY!!!!");
							driver.close();
							driver = getWebDriver(browser, puid, site, remoteDriverEnabled, runHeadless);
							System.out.println("Getting url.");
							driver.get(site);
						} else {
							System.out.println("THIS SESSION IS EMPTY!!!");
							driver.close();
							driver = getWebDriver(browser, puid, site, remoteDriverEnabled, runHeadless);
							System.out.println("Getting url.");
							driver.get(site);
						}
					}
				} catch (Exception e) {
					System.out.println("Killing thread.");
					System.out.println(e.getMessage());
					driver.close();
					Thread.currentThread().interrupt();
				}
				System.out.println("Here is the thread state: " + Thread.currentThread().getState());
				System.out.println(
						"Here is the instance created prior to start: " + concMap.get(threadId));
				System.out.println("Here is the browser type created prior to start: "
						+ browMap.get(threadId));
				System.out.println("Here is the proxy instance: " + proxyMap.get(threadId));
				returnProcessList("chromedriver.exe");
				break;
			case "firefox":
				FirefoxOptions firefoxOptions = new FirefoxOptions();

				proxy = getProxyServer();
				// proxy.blacklistRequests("https?://www\\.idsync.rlcdn\\.com/.*", 410);
				// proxy.blacklistRequests("https://idsync.rlcdn.com", 410);
				proxy.addRequestFilter(new RequestFilter() {
					@Override
					public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents,
							HttpMessageInfo messageInfo) {
						request.headers().add(HEADER_KEY, HEADER_VAL);
						return null;
					}
				});
				seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

				// To determine MIME types, refer to the firefox profile and look to the
				// handlers.json document. The server can actually distort the MIME type of the
				// downloaded file.
				firefoxOptions.addPreference("browser.helperApps.neverAsk.saveToDisk", Constants.MIME_TYPES);

				firefoxOptions.addPreference("browser.helperApps.alwaysAsk.force", false);
				firefoxOptions.addPreference("browser.download.folderList", 2);
				firefoxOptions.addPreference("browser.download.manager.showWhenStarting", false);
				firefoxOptions.addPreference("browser.download.manager.alertOnEXEOpen", false);
				firefoxOptions.addPreference("browser.download.manager.focusWhenStarting", false);
				firefoxOptions.addPreference("browser.download.manager.useWindow", false);
				firefoxOptions.addPreference("browser.download.panel.shown", false);
				firefoxOptions.addPreference("browser.download.pluginOverrideTypes ", true);
				firefoxOptions.addPreference("browser.download.dir", FIREFOX_PATH);
				firefoxOptions.addPreference("browser.download.downloadDir", FIREFOX_PATH);
				firefoxOptions.addPreference("browser.download.defaultFolder", FIREFOX_PATH);
				firefoxOptions.addPreference("browser. download. show_plugins_in_list", false);

				// firefoxOptions.addArguments("--always-authorize-plugins");
				// firefoxOptions.addArguments("--disable-browser-side-navigation");
				// options.addArguments("--disable-gpu");
				// options.addArguments("--headless");
				// firefoxOptions.addArguments("--dns-prefetch-disable");
				// firefoxOptions.addArguments("--start-maximized");
				// firefoxOptions.addArguments("--no-sandbox");
				// firefoxOptions.addArguments("--ignore-certificate-errors");
				// firefoxOptions.setAcceptInsecureCerts(true);
				// firefoxOptions.addArguments("--incognito");
				// firefoxOptions.addArguments("test-type");
				// options.addArguments("no-default-browser-check");
				// options.addArguments("--disable-extensions");

				firefoxOptions.setCapability(CapabilityType.PROXY, seleniumProxy);
				firefoxOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

				firefoxOptions.setCapability("marionette", true);

				// prefs.put("download.default_directory", FILE_PATH);
				// firefoxOptions.setCapability("prefs", prefs);
				if (remoteDriverEnabled) {
					// System.setProperty("webdriver.gecko.driver", DRIVER_PATH +
					// "geckodriver.exe");
					setEnVarPathForDrivers();
				}
				driver = new FirefoxDriver(firefoxOptions);
				browserType = browser;
				driver.get(site);
				concMap.put(threadId, driver);
				browMap.put(threadId, browserType);
				proxyMap.put(threadId, proxy);

				waitForLoad(driver);
				try {
					SessionId session = ((FirefoxDriver) driver).getSessionId();
					sessionMap.put(threadId, session);
					try {
						System.out.println("Getting url.");
						driver.get(site);
						// driver.navigate().to(START_URL);
					} catch (Exception nsd) {
						if (!isSessionEmpty(session)) {
							System.out.println("THIS SESSION IS NOT EMPTY!!!!");
							driver.close();
							driver = getWebDriver(browser, puid, site, remoteDriverEnabled, runHeadless);
							System.out.println("Getting url.");
							driver.get(site);
							// driver.navigate().to(START_URL);
						} else {
							System.out.println("THIS SESSION IS EMPTY!!!");
							driver.close();
							driver = getWebDriver(browser, puid, site, remoteDriverEnabled, runHeadless);
							System.out.println("Getting url.");
							driver.get(site);
							// driver.navigate().to(START_URL);
						}
					}
				} catch (Exception e) {
					System.out.println("Killing thread.");
					System.out.println(e.getMessage());
					driver.close();
					Thread.currentThread().interrupt();
				}
				System.out.println("Here is the thread state: " + Thread.currentThread().getState());
				System.out.println(
						"Here is the instance created prior to start: " + concMap.get(threadId));
				System.out.println("Here is the browser type created prior to start: "
						+ browMap.get(threadId));
				System.out.println("Here is the proxy instance: " + proxyMap.get(threadId));
				returnProcessList("geckodriver.exe");
				maximize();
				break;
			case "ie":
				InternetExplorerOptions ieOptions = new InternetExplorerOptions();

				proxy = getProxyServer();
				proxy.addRequestFilter(new RequestFilter() {
					@Override
					public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents,
							HttpMessageInfo messageInfo) {
						request.headers().add(HEADER_KEY, HEADER_VAL);
						return null;
					}
				});
				seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

				ieOptions.setCapability(CapabilityType.PROXY, seleniumProxy);
				ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

				prefs.put("download.default_directory", FILE_PATH);
				ieOptions.setCapability("prefs", prefs);
				if (remoteDriverEnabled) {
					// System.setProperty("webdriver.ie.driver", DRIVER_PATH +
					// "IEDriverServer.exe");
					setEnVarPathForDrivers();
				}
				driver = new InternetExplorerDriver(ieOptions);
				driver.get(site);
				browserType = browser;
				concMap.put(threadId, driver);
				browMap.put(threadId, browserType);
				proxyMap.put(threadId, proxy);

				waitForLoad(driver);
				try {
					SessionId session = ((InternetExplorerDriver) driver).getSessionId();
					sessionMap.put(threadId, session);
					try {
						System.out.println("Getting url.");
						driver.get(site);
						// driver.navigate().to(START_URL);
					} catch (Exception nsd) {
						if (!isSessionEmpty(session)) {
							System.out.println("THIS SESSION IS NOT EMPTY!!!!");
							driver.close();
							driver = getWebDriver(browser, puid, site, remoteDriverEnabled, runHeadless);
							System.out.println("Getting url.");
							driver.get(site);
							// driver.navigate().to(START_URL);
						} else {
							System.out.println("THIS SESSION IS EMPTY!!!");
							driver.close();
							driver = getWebDriver(browser, puid, site, remoteDriverEnabled, runHeadless);
							System.out.println("Getting url.");
							driver.get(site);
							// driver.navigate().to(START_URL);
						}
					}
				} catch (Exception e) {
					System.out.println("Killing thread.");
					System.out.println(e.getMessage());
					driver.close();
					Thread.currentThread().interrupt();
				}
				System.out.println("Here is the thread state: " + Thread.currentThread().getState());
				System.out.println(
						"Here is the instance created prior to start: " + concMap.get(threadId));
				System.out.println("Here is the browser type created prior to start: "
						+ browMap.get(threadId));
				System.out.println("Here is the proxy instance: " + proxyMap.get(threadId));
				maximize();
				break;
			case "edge":
				EdgeOptions edgeOptions = new EdgeOptions();

				proxy = getProxyServer();
				proxy.addRequestFilter(new RequestFilter() {
					@Override
					public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents,
							HttpMessageInfo messageInfo) {
						request.headers().add(HEADER_KEY, HEADER_VAL);
						return null;
					}
				});
				seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

				edgeOptions.setCapability(CapabilityType.PROXY, seleniumProxy);
				edgeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				edgeOptions.setCapability("acceptInsecureCerts", true);

				prefs.put("download.default_directory", FILE_PATH);
				edgeOptions.setCapability("prefs", prefs);
				if (remoteDriverEnabled) {
					// System.setProperty("webdriver.ie.driver", DRIVER_PATH +
					// "IEDriverServer.exe");
					setEnVarPathForDrivers();
				}
				driver = new EdgeDriver(edgeOptions);
				driver.get(site);
				browserType = browser;
				concMap.put(threadId, driver);
				browMap.put(threadId, browserType);
				proxyMap.put(threadId, proxy);

				waitForLoad(driver);
				try {
					SessionId session = ((EdgeDriver) driver).getSessionId();
					sessionMap.put(threadId, session);
					try {
						System.out.println("Getting url.");
						driver.get(site);
						// driver.navigate().to(START_URL);
					} catch (Exception nsd) {
						if (!isSessionEmpty(session)) {
							System.out.println("THIS SESSION IS NOT EMPTY!!!!");
							driver.close();
							driver = getWebDriver(browser, puid, site, remoteDriverEnabled, runHeadless);
							System.out.println("Getting url.");
							driver.get(site);
							// driver.navigate().to(START_URL);
						} else {
							System.out.println("THIS SESSION IS EMPTY!!!");
							driver.close();
							driver = getWebDriver(browser, puid, site, remoteDriverEnabled, runHeadless);
							System.out.println("Getting url.");
							driver.get(site);
							// driver.navigate().to(START_URL);
						}
					}
				} catch (Exception e) {
					System.out.println("Killing thread.");
					System.out.println(e.getMessage());
					driver.close();
					Thread.currentThread().interrupt();
				}
				System.out.println("Here is the thread state: " + Thread.currentThread().getState());
				System.out.println(
						"Here is the instance created prior to start: " + concMap.get(threadId));
				System.out.println("Here is the browser type created prior to start: "
						+ browMap.get(threadId));
				System.out.println("Here is the proxy instance: " + proxyMap.get(threadId));
				maximize();
				break;
			default:
				System.out.println("Case not available.");
				throw new RuntimeException("Browser type not supported.");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return driver;
	}

	public synchronized WebDriver getWebDriver(String browser, String puid, String deviceName, String site,
			Boolean remoteDriverEnabled, Boolean runHeadless) {

		final String HEADER_KEY = "PUID";
		final String HEADER_VAL = puid;

		final String DOWNLOAD_FOLDER = "/src/test/resources/TestData";
		final String FILE_PATH = System.getProperty("user.dir") + DOWNLOAD_FOLDER;
		long threadId = Thread.currentThread().getId();

		HashMap<String, Object> prefs = new HashMap<String, Object>();

		try {
			switch (browser) {
			case "mobileChrome":

				if (remoteDriverEnabled) {

					// System.setProperty("webdriver.chrome.driver", DRIVER_PATH +
					// "chromedriver.exe");
					setEnVarPathForDrivers();
				}
				
				ChromeOptions mobileChromeOptions = new ChromeOptions();
				BrowserMobProxy proxy = getProxyServer();

				// Testing this out
				proxy.blacklistRequests("https?://www\\.idsync.rlcdn\\.com/.*", 410);
				proxy.blacklistRequests("https://idsync.rlcdn.com", 410);
				proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT); // MM
				proxy.addRequestFilter(new RequestFilter() {
					@Override
					public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents,
							HttpMessageInfo messageInfo) {
						request.headers().add(HEADER_KEY, HEADER_VAL);
						return null;
					}
				});
				if (runHeadless) {
					mobileChromeOptions.addArguments("--headless");
					mobileChromeOptions.addArguments("--window-size=1325x744");
				}
				Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

				seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

				Map<String, String> mobileEmulation = new ConcurrentHashMap<String, String>();
				mobileEmulation.put("deviceName", deviceName);

				// TODO: Trying this out for chrome hangin
				mobileChromeOptions.addArguments("--always-authorize-plugins");
				mobileChromeOptions.addArguments("--disable-browser-side-navigation");
				// options.addArguments("--disable-gpu");
				// options.addArguments("--headless");
				mobileChromeOptions.addArguments("--dns-prefetch-disable");
				mobileChromeOptions.addArguments("--start-maximized");
				mobileChromeOptions.addArguments("--no-sandbox");
				mobileChromeOptions.addArguments("--ignore-certificate-errors");
				// options.addArguments("--disable-popup-blocking");
				// options.addArguments("chrome.switches","--disable-extensions");
				mobileChromeOptions.setAcceptInsecureCerts(true);
				// options.addArguments("--incognito");
				mobileChromeOptions.addArguments("test-type");
				// options.addArguments("no-default-browser-check");
				// options.addArguments("--disable-extensions");

				mobileChromeOptions.setCapability(CapabilityType.PROXY, seleniumProxy);
				mobileChromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				mobileChromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
				// options.setCapability(CapabilityType.TAKES_SCREENSHOT, seleniumProxy);
				// options.setCapability(CapabilityType.SUPPORTS_ALERTS, seleniumProxy);
				// options.setCapability("chrome.switches",
				// Arrays.asList("--disable-local-storage"));
				prefs.put("download.default_directory", FILE_PATH);

				// *******automatically downloads pdf to test download directory, Test
				// Data.*****
				// it is commented out below so it does not disrupt the current pdf download
				// process
				prefs.put("plugins.always_open_pdf_externally", true);
				prefs.put("pdfjs.disabled", true);

				mobileChromeOptions.setExperimentalOption("prefs", prefs);
				mobileChromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

				driver = new ChromeDriver(mobileChromeOptions);
				browserType = browser;
				concMap.put(threadId, driver);
				browMap.put(threadId, browserType);
				proxyMap.put(threadId, proxy);
				waitForLoad(driver);

				try {
					SessionId session = ((ChromeDriver) driver).getSessionId();
					sessionMap.put(threadId, session);
					try {
						System.out.println("Getting url.");
						driver.get(site);
					} catch (Exception nsd) {
						if (!isSessionEmpty(session)) {
							System.out.println("THIS SESSION IS NOT EMPTY!!!!");
							driver.close();
							driver = getWebDriver(browser, puid, deviceName, site, remoteDriverEnabled, runHeadless);
							System.out.println("Getting url.");
							driver.get(site);
						} else {
							System.out.println("THIS SESSION IS EMPTY!!!");
							driver.close();
							driver = getWebDriver(browser, puid, deviceName, site, remoteDriverEnabled, runHeadless);
							System.out.println("Getting url.");
							driver.get(site);
						}
					}
				} catch (Exception e) {
					System.out.println("Killing thread.");
					System.out.println(e.getMessage());
					driver.close();
					Thread.currentThread().interrupt();
				}
				System.out.println("Here is the thread state: " + Thread.currentThread().getState());
				System.out.println(
						"Here is the instance created prior to start: " + concMap.get(threadId));
				System.out.println("Here is the browser type created prior to start: "
						+ browMap.get(threadId));
				System.out.println("Here is the proxy instance: " + proxyMap.get(threadId));
				returnProcessList("chromedriver.exe");
				break;
			default:
				getWebDriver(browser, puid, site, remoteDriverEnabled, runHeadless);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return driver;
	}

	public static ChromeOptions optionsForChromeHeadless() {
		long threadId = Thread.currentThread().getId();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--window-size=1325x744");
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--always-authorize-plugins");
		options.addArguments("--safebrowsing-disable-download-protection");
		options.addArguments("--allow-running-insecure-content");
		options.addArguments("--disable-background-networking");

		BrowserMobProxy proxy = getProxyServer();

		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

		options.setCapability(CapabilityType.PROXY, seleniumProxy);
		options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, seleniumProxy);
		proxyMap.put(threadId, proxy);
		System.out.println("THIS IS THE SSL PROXY: " + seleniumProxy.getSslProxy());

		HashMap<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.default_directory", FILE_PATH);
		prefs.put("plugins.always_open_pdf_externally", true);
		prefs.put("pdfjs.disabled", true);
		options.setExperimentalOption("prefs", prefs);

		options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
		return options;

	}

	public synchronized WebDriver getWebDriver(String ban, Class<?> clazz, String site, Boolean remoteDriverEnabled,
			Boolean runHeadless) throws Exception {
		ChromeOptions options = new ChromeOptions();
		long threadId = Thread.currentThread().getId();
		if (runHeadless) {
			options.addArguments("--headless");
			options.addArguments("--window-size=1325x744");
			options.addArguments("--ignore-certificate-errors");
			options.addArguments("--always-authorize-plugins");
			options.addArguments("--safebrowsing-disable-download-protection");
			options.addArguments("--allow-running-insecure-content");
			options.addArguments("--disable-background-networking");

		}
		// options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		options.addArguments("--start-maximized");
		// options.addArguments("--disable-popup-blocking");
		// options.addArguments("chrome.switches","--disable-extensions");
		HashMap<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.default_directory", FILE_PATH);
		prefs.put("plugins.always_open_pdf_externally", true);
		prefs.put("pdfjs.disabled", true);
		// prefs.put("browser","set_download_behavior:
		// {"+jsonStringOfDownloadBehavior()+"}");
		options.setExperimentalOption("prefs", prefs);

		options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
		if (remoteDriverEnabled) {
			setEnVarPathForDrivers();
		}
		if (runHeadless) {
			// driver = new ChromeDriver(options);
			setDownloadSettings(options);
		}
		if (!runHeadless) {
			driver = new ChromeDriver(options);
			driver.get(site);
		}
		
		System.out.println("1 HERE");
		concMap.put(threadId, driver);
		browMap.put(threadId, "chrome");
		banMap.put(threadId, ban);
		classMap.put(threadId, clazz.getClass().getName());
		boolean offer = stack.offer(driver);
		System.out.println(offer);
		return driver;
	}

	public static void allowSSLHandshake() {
		System.setProperty("com.sun.net.ssl.checkRevocation", "false");
		System.setProperty("javax.net.debug", "all");
		System.setProperty("com.sun.net.ssl.checkRevocation", "false");
		System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		System.setProperty("javax.net.ssl.trustStore", "C:"+fs+"Program Files (x86)"+fs+"Java"+fs+"jdk1.8.0_201"+fs+"jre"+fs+"lib"+fs+"security"+fs+"cacerts.ts");
	}
	public void setDownloadSettings(ChromeOptions options) throws Exception {
		ChromeDriverService driverService = ChromeDriverService.createDefaultService();
		driver = new ChromeDriver(driverService, options);
System.setProperty("com.sun.net.ssl.checkRevocation", "false");
		HttpClient httpClient = HttpClientBuilder.create().build();
		// System.out.println("CHROME_DRIVER_EXE_PROPERTY:
		// "+driverService.CHROME_DRIVER_EXE_PROPERTY.toString());
		// String url = driver.getCurrentUrl();
		String url = driverService.getUrl().toString();
		driverService.start();
		driver.get(site);
		System.out.println("THIS IS THE DRIVER SERVICE URL: " + url);
		String u = url;
		if (url.endsWith("/")) {
			u = url + "session/" + ((ChromeDriver) driver).getSessionId() + "/chromium/send_command";
		} else {
			u = url + "/session/" + ((ChromeDriver) driver).getSessionId() + "/chromium/send_command";
		}
		System.out.println("THIS IS THE REQUEST: " + u);
		HttpPost request = new HttpPost(u);
		request.addHeader("Content-Type", "text/plain");
		
	
		request.setEntity(new StringEntity(jsonStringOfDownloadCmd()));
		org.apache.http.HttpResponse resp = httpClient.execute(request);
		System.out.println("THIS IS THE RESPONSE: " + resp);
	}
	
	public static void setDownloadSettings() {
		JavascriptExecutor js = (JavascriptExecutor) getDriverInstance();
			String cookies =  js.executeScript(" var allcookies = document.cookie;\n" + 
					"            document.write (\"All Cookies : \" + allcookies );;").toString();
			System.out.println("Cookies "+cookies);
	}
	
	public static void cookiesThroughRest() {
		Response resp =  RestAssured.get(Browser.getDriverInstance().getCurrentUrl());
		resp.getCookies();
		System.out.print("These are the cookies: "+resp.getCookies());
	}

	public synchronized WebDriver getWebDriver(String site, Boolean remoteDriverEnabled, Boolean runHeadless)
			throws Exception {
		ChromeOptions options = new ChromeOptions();
		long threadId = Thread.currentThread().getId();
		if (runHeadless) {
			options.addArguments("--headless");
			options.addArguments("--window-size=1325x744");

		}

		options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		options.addArguments("--start-maximized");
		// options.addArguments("--disable-popup-blocking");
		// options.addArguments("chrome.switches","--disable-extensions");
		HashMap<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.default_directory", FILE_PATH);
		prefs.put("plugins.always_open_pdf_externally", true);
		prefs.put("pdfjs.disabled", true);
		options.setExperimentalOption("prefs", prefs);
		if (remoteDriverEnabled) {
			// System.setProperty("webdriver.chrome.driver", DRIVER_PATH +
			// "chromedriver.exe");
			setEnVarPathForDrivers();
		}
		
		driver = new ChromeDriver(options);
		driver.get(site);
		concMap.put(threadId, driver);
		browMap.put(threadId, "chrome");
		// banMap.put(Thread.currentThread().getId(), ban);
		return driver;
	}

	public void goTo(String url) {
		getDriverInstance().get(url);

	}

	public void maximize() {
		getDriverInstance().manage().window().maximize();
	}

	public void quit() {
		getDriverInstance().quit();
	}

	public void close() {
		getDriverInstance().close();
	}

	public void refreshPage() {
		getDriverInstance().navigate().refresh();
	}

	public String getTitle() {
		return getDriverInstance().getTitle();
	}

	public static String getCurrentUrl() {
		return getDriverInstance().getCurrentUrl();
	}

	protected void waitForLoad(WebDriver driver) throws InterruptedException {
		// driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				// return ((JavascriptExecutor)driver).executeScript("return
				// document.readyState").equals("complete");
				return true;
			}
		};

		try {
			System.out.println("Here is the thread state: " + Thread.currentThread().getState());
			wbWait = new WebDriverWait(driver, 120);
			System.out.println("Waiting on the page load");
			wbWait.until(pageLoadCondition);
		} catch (Exception error) {

		}
	}

	public void execKill() throws InterruptedException, IOException {
		String driverName = System.getProperty("browser");
		DataFileFinder dataFile = new DataFileFinder();
		synchronized (this.getClass()) {
			if (driverName != null) {

				String line = "TASKKILL /F /IM ";
				switch (driverName) {
				case "chrome":
					line = "";
					line = killChromeDrByPID();
					break;
				case "firefox":
					line = line.concat(BrowserType.FIREFOX.getBrowserTypeDriverName());
					System.out.println("Here is the exec kill: " + line);
					break;
				case "mobileChrome":
					line = line.concat(BrowserType.CHROME.getBrowserTypeDriverName());
					break;
				case "ie":
					line = line.concat(BrowserType.IE.getBrowserTypeDriverName());
					break;
				default:
					System.out.println("The browser stated is not available yet.");
				}

				// System.out.println("Here is the task kill: " + line);
				
				Runtime.getRuntime().exec(line);

				dataFile.deleteDriversFromNewLoc();

			}
		}
	}

	public String killChromeDrByPID() throws IOException {
		String pid = pidOfChromeDriver();
		String cmdToKill =null;
		if(Browser.isWindows()) {
		cmdToKill = "TASKKILL /PID " + pid + " /F";}
		else if (Browser.isUnix()) {
			cmdToKill = "kill -9 "+pid;}
		return cmdToKill;
	}

	public String pidOfChromeDriver() throws IOException {
		ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
		int port = chromeDriverService.getUrl().getPort();
		System.out.println("THIS IS THE CHROMEDRIVER PORT: " + port);
		String pid = null;
		
		String cmdToGetPid = cmdToGetPID(port);

		pid = CustomListener.processBuildForWindowsAndUnix(System.getProperty("user.dir"), cmdToGetPid);
		
		System.out.println("This is the pid: " + pid);
		return pid;
	}

	public String cmdToGetPID(int port) {
		String cmdToGetPID = null;
		if(Browser.isWindows()) {
			cmdToGetPID ="netstat -aon | findstr LISTENING | findstr [" + port + "]";
		}else if(Browser.isUnix()) {
			cmdToGetPID ="netstat -anp | grep LISTEN | grep [" + port + "]";
		}
		
		return cmdToGetPID;
	}

	protected void returnProcessList(String processName) {
		try {
			String line;
			List<String> lineList = new ArrayList<>();
			Process p = Runtime.getRuntime().exec("tasklist /fi \"imagename eq " + processName + "\"");
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				lineList.add(line);
			}
			lineList.remove(0);
			lineList.remove(1);
			for (String text : lineList) {
				System.out.println(text);
			}
			input.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static BrowserMobProxy getProxyServer() {
		BrowserMobProxy proxy = new BrowserMobProxyServer();
		// proxy.setTrustAllServers(true);
		proxy.start(0);
		return proxy;
	}

	private static Boolean isSessionEmpty(SessionId session) {
		Boolean isEmpty = false;

		if (session == null) {
			isEmpty = true;
		} else {
			isEmpty = false;
		}
		return isEmpty;
	}

	/**
	 * Though this routine is supposed to return the MIME type of the file you send
	 * it, the server can distort the actual MIME type. Refer to the firefox
	 * profile's handlers.json document
	 * 
	 * @param fileName
	 * @return
	 */
	public static String identifyFileTypeUsingMimetypesFileTypeMap(final String fileName) {
		final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
		System.out.println("the MIME type of the file is: " + fileTypeMap.getContentType(fileName));
		return fileTypeMap.getContentType(fileName);
	}

	public static boolean siteIsConstant(String site, String url) {
		return site.equals(url) || site.startsWith("data:");
	}

	/*
	 * This routine polls for the BAN account number and asserts that within the
	 * elapsed Browser.getWaitTime(), the ban is displayed. If the ban is displayed
	 * within the elapsed Browser.getWaitTime(),it exits the while loop and results
	 * in a hard assert pass. If the page is loaded and the ban is not displayed
	 * within the elapsed Browser.getWaitTime(), it exits the while loop and results
	 * in a hard assert fail. If the ban is not displayed after the elapsed
	 * Browser.getWaitTime(), it exits the while loop and results in a hard assert
	 * fail.
	 */

	public static Boolean quitTestIfBanIsDown(String site) throws IOException {
		boolean urlIsTestSite = true;
		long threadId = Thread.currentThread().getId();
		if (!Browser.browMap.get(threadId)
				.equals(BrowserType.MOBILE_CHROME.getBrowserTypeName())) {

			long startTime = System.currentTimeMillis();
			long waitTime = (Browser.getWaitTime() * 1000);
			long endTime = (startTime + waitTime);

			String url = site;
			 String msg = "\nTHE URL IS " + Browser.getCurrentUrl() + " AND EXPECTED TEST SITE " + site+"\nand they are equal: "+site.equals(Browser.getCurrentUrl());
			while (urlIsTestSite && (startsTrueTurnsFalseIfPageReadyOrAfterEndTime(endTime))) {
				url = Browser.getCurrentUrl();
				urlIsTestSite = siteIsConstant(site, url);
				// System.out.println(msg);
			}
			/*
			 * System.out.println("THE URL DID NOT NAVIGATE AWAY FROM TEST SITE: " +
			 * siteIsConstant(site, url) + "\nAND THE BAN IS DISPLAYED: " + banDisplayed());
			 */

			// if (!(siteIsConstant(site, url) && banDisplayed())) {
			// throw new SkipException(
			// "BAN NUMBER: " + Browser.banMap.get(Thread.currentThread().getId()) + " IS
			// DOWN");
		//	System.out.println(msg);
			Assert.assertTrue(siteIsConstant(site, url) && banDisplayed(),
					"BAN NUMBER: " + Browser.banMap.get(threadId) + " IS DOWN");
			// }
		}
		return urlIsTestSite;
	}

	public static String jsGetBan() {
		String banText = null;
		JavascriptExecutor js = (JavascriptExecutor) getDriverInstance();

		if (Browser.site.contains("consumer")) {
			String banTextIL = js.executeScript("return document.getElementsByClassName('account-number light');")
					.toString();
			banText = banTextIL;
		} else if (Browser.site.contains("corporate")) {
			String banTextCL = js
					.executeScript("return document.getElementsByClassName('account-number light ng-binding');")
					.toString();
			banText = banTextCL;
		}
		return banText;
	}

	public static boolean startsTrueTurnsFalseIfPageReadyOrAfterEndTime(long endTime) {
		boolean dontTimeOut = true;
		while ((pageLoadedWithBanDisplayed() || (System.currentTimeMillis() > endTime) || banDisplayed())
				&& dontTimeOut) {
			System.out.println("The polling for ban routine timed out: " + (System.currentTimeMillis() > endTime));
			dontTimeOut = false;
		}
		return dontTimeOut;
	}

	public static boolean pageLoadedWithBanDisplayed() {
		boolean pageLoaded = pageIsLoaded();
		boolean banDisplayed = banDisplayed();
		// System.out.println("the page is loaded: "+pageLoaded+" and the ban is
		// displayed "+banDisplayed);
		return (pageLoaded && banDisplayed);
	}

	public static Boolean banIsDown(String site) throws IOException {
		// System.out.println("The ban is displayed: "+banDisplayed());
		return (!urlContainsSprintUrl(site) || !banDisplayed());
	}

	public static boolean banDisplayed() {
		String ban = null;
		HomePage header = new HomePage();
		// //String line = sc.nextLine();
		// ban = header.getBanText();
		// if (ban.length() == 0) {
		// ban = jsGetBan();
		// }

		if (!Browser.browMap.get(Thread.currentThread().getId())
				.equals(BrowserType.MOBILE_CHROME.getBrowserTypeName())) {
			ban = jsGetBan();
		} else {
			ban = header.getBanText();
		}

		boolean isNull = ban.equals(null);
		int legnthOfEmptyBanByJS = 2;
		boolean isLengthZero = ban.length() <= legnthOfEmptyBanByJS;
		boolean isEmpty = ban.isEmpty();

		/*
		 * System.out.println("the ban is null: " + isNull +
		 * " the ban is length 2	(empty in js): " + isLengthZero +
		 * " the ban is empty " + isEmpty + "\nthe ban length is " + ban.length());
		 */

		boolean banDisplayed = (!isNull && !isLengthZero && !isEmpty);
		// System.out.println("the ban: " + ban + " is not null or empty " +
		// banDisplayed);
		return banDisplayed;
	}

	public static Boolean urlContainsSprintUrl(String site) throws IOException {
		String id = ".com:8080";
		int testEnviron = site.indexOf(id) + id.length();
		String importantIDOfTestEnviron = site.substring(0, testEnviron);
		String currentURL = Browser.getCurrentUrl();
		boolean testEnvironMatchesCurrentURL = false;

		if (currentURL.contains(importantIDOfTestEnviron) && (!currentURL.contains("error"))) {
			String importantIDOfCurrentURL = currentURL.substring(0, testEnviron);
			testEnvironMatchesCurrentURL = importantIDOfTestEnviron.equals(importantIDOfCurrentURL);

		}
		System.out.println("current url is: " + Browser.getCurrentUrl() + " and the environment home page is " + site
				+ "\nthese URLS are on the test environment: " + testEnvironMatchesCurrentURL);
		return testEnvironMatchesCurrentURL;
	}

	public static Boolean pageIsLoaded() {
		boolean pageLoaded = ((JavascriptExecutor) getDriverInstance()).executeScript("return document.readyState")
				.equals("complete");
		// System.out.println("PAGE IS LOADED: " + pageLoaded);
		return pageLoaded;
	}

	public static void getWindowHandles() throws InterruptedException, IOException, ScriptException, InstantiationException, IllegalAccessException {
		JavascriptExecutor js = (JavascriptExecutor) getDriverInstance();
		// js.executeScript("window.open()");
		Thread.sleep(5000);
		System.out.println("THESE ARE THE WINDOW HANDLES: " + getDriverInstance().getWindowHandles());
		String newWindow = getDriverInstance().getWindowHandles().stream()
				.filter(win -> (!win.equals(getDriverInstance().getWindowHandle()))).findFirst().get();
		System.out.println("This is the new window: " + newWindow);
		System.out.println("BEFORE MOVING TO TO NEW WINDOW");
		getDriverInstance().switchTo().window(newWindow);
	System.out.println("MOVED TO NEW WINDOW: ");
//		String newUrl = getDriverInstance().switchTo().window(newWindow).getPageSource();
//		Thread.sleep(5000);
//		System.out.println("newUrl PageSource" + newUrl);
//		getDriverInstance().switchTo().window(newWindow);
		getDriverInstance().getWindowHandle();
		System.out.println("JUST ASKED FOR WINDOW HANDLE");
//		System.out.println("This is page source after navigation: "+Browser.getDriverInstance().getPageSource());
//		String blobPageSource = getDriverInstance().switchTo().window(newWindow).getPageSource();
//		System.out.println("blobPageSource " + blobPageSource);
		String firstPartOfCmd = "window.URL = window.URL || window.webkitURL;";
		String jsCmd = "var blob = new Blob([" + firstPartOfCmd
				+ "],{type:\"application/pdf\"});var link = document.createElement('link');link.rel = 'stylesheet';return link.href = window.URL.createObjectURL(blob);";

		InputStream is = new ByteArrayInputStream(jsCmd.getBytes());
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("Nashorn");
		Reader red = new InputStreamReader(is);

		engine.eval(red);

		String blobUrl = (String) js.executeScript(jsCmd);
		System.out.println("This is the blobUrl: " + blobUrl);
		getDriverInstance().switchTo().window(newWindow);

		// FileUtils.copyFile(realUrl.getContent(), new
		// File(Browser.FILE_PATH+Browser.fs+"479967232-2018-06-01.pdf"));

	}

	public static void printSystemEnvVar() {
		printMapValueSingleString(System.getenv());
	}

	public static void printSystemProps() {
		java.util.Properties props = System.getProperties();
		System.out.println("The system properties are:\n");
		for (String p : props.stringPropertyNames()) {
			System.out.println(p + "=" + System.getProperty(p));
		}
	}

	public static void printMapValueSingleString(Map<String, String> map) {
		map.forEach((K, Y) -> {
			System.out.println(K + "=" + Y);
		});
	}

	public static String convertClassToJSON(Class<?> class1) {
		return class1.getSimpleName() + ".json";
	}

	public static boolean runningOnUAT1() {
		return Browser.site.startsWith(Browser.uat1) || Browser.site.equals(Browser.uat1);
	}

	public static boolean runningOnUAT2() {
		return Browser.site.startsWith(Browser.uat2) || Browser.site.equals(Browser.uat2);
	}

	public static boolean runningOn039() {
		return Browser.site.startsWith(Browser.uat039) || Browser.site.equals(Browser.uat039)
				|| Browser.site.contains("tvmkg039.test.sprint.com");
	}
	
	public static boolean runningOn037() {
		return Browser.site.startsWith(Browser.uat037) || Browser.site.equals(Browser.uat037)
				|| Browser.site.contains("tvmkg037.test.sprint.com");
	}

	/*
	 * The following routine copies drivers within this project to the folder,
	 * C://ProgramData on Windows environments or the home folder on Linux
	 * environments. It then adds that folder location to the JVM's memory of the
	 * environment variables and java.library.path.
	 * 
	 * We need to copy the drivers outside the project because:
	 * 
	 * The project is unable to execute a driver from a folder within itself.
	 *
	 * We work with the env variable of the JVM's memory because:
	 * 
	 * We cannot change and utilize updated env. variables within the same session
	 * so that the Path includes the newDrLoc.
	 * 
	 * 
	 * 
	 */

	public static void setEnVarPathForDrivers() throws Exception {
		if (System.getProperty("remoteDriverEnabled").equals("true")) {

			{
				DataFileFinder.copyDriversToLocOutsideProject();
				String appendedDrLoc = ps + retrieveDrLocForOS() + ps + ps;
				String envPathForDriver = System.getenv("PATH") + appendedDrLoc;
				String jvmPathForDrivers = System.getProperty("java.library.path") + appendedDrLoc;

				Boolean enVarContainsPathToDrivers = System.getenv("PATH").contains(appendedDrLoc);
				Boolean jvmContainsPathToDrivers = System.getProperty("java.library.path").contains(appendedDrLoc);

				if (!enVarContainsPathToDrivers) {
					Map<String, String> envs = new HashMap<String, String>();
					envs.put("PATH", envPathForDriver);
					setEnv(envs);
				}

				if (!jvmContainsPathToDrivers) {
					System.setProperty("java.library.path", jvmPathForDrivers);
				}
//
//				 System.out.println("DRIVER LOCATION: " + newDrLoc);
				 System.out.println("env path: " + System.getenv("PATH"));
				 System.out.println("sys path: " + System.getProperty("java.library.path"));
				 System.setProperty("webdriver.chrome.driver", execChromeDriverLoc);
				 
			}
		}
	}

	/*
	 * The following routine will change the environment variables throughout the
	 * JVM, and it will not alter the system environment.
	 * 
	 * It only changes the environment variable in memory, the copy that the JVM
	 * creates when it starts the program.
	 */

	protected static void setEnv(Map<String, String> newenv) throws Exception {
		try {
			Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
			Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
			theEnvironmentField.setAccessible(true);
			Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
			env.putAll(newenv);
			Field theCaseInsensitiveEnvironmentField = processEnvironmentClass
					.getDeclaredField("theCaseInsensitiveEnvironment");
			theCaseInsensitiveEnvironmentField.setAccessible(true);
			Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
			cienv.putAll(newenv);
		} catch (NoSuchFieldException e) {
			Class[] classes = Collections.class.getDeclaredClasses();
			Map<String, String> env = System.getenv();
			for (Class cl : classes) {
				if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
					Field field = cl.getDeclaredField("m");
					field.setAccessible(true);
					Object obj = field.get(env);
					Map<String, String> map = (Map<String, String>) obj;
					map.clear();
					map.putAll(newenv);
				}
			}
		}
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	public static boolean isWindows() {
		return (OS.startsWith("win"));
	}

	public static String grabTestRunnerType() {
		String nameOfRun = null;
		if (!System.getProperties().containsKey("package")) {
			nameOfRun = System.getProperty("class");
		} else {
			nameOfRun = System.getProperty("package");
		}
		// System.out.println("This is the name of the run: " + nameOfRun);
		return nameOfRun;
	}

	public static Boolean runIsDigital() {
		return grabTestRunnerType().contains("_digital");
	}
	
	public static Boolean runIsAPI() {
		return grabTestRunnerType().contains("API");
	}

	public static Boolean isILPaperRun() {
		// System.out.println("This is an il paper test: " +
		// grabTestRunnerType().contains("il_paper"));
		return grabTestRunnerType().contains("il_paper");
	}

	public static void checkIfBanIsDown() {
		long threadId = Thread.currentThread().getId();
		if (Browser.getDriverInstance().findElement(By.cssSelector(".widget span")).isDisplayed()) {
			throw new SkipException("BAN NUMBER: " + Browser.banMap.get(threadId) + " IS DOWN");
		}
		try {
			if (!Browser.getDriverInstance().findElement(By.cssSelector(".light.title")).isDisplayed()) {
				throw new SkipException(
						"BAN NUMBER: " + Browser.banMap.get(threadId) + " IS DOWN");
			}
		} catch (Exception e) {
			throw new SkipException("BAN NUMBER: " + Browser.banMap.get(threadId) + " IS DOWN");
		}
	}

	public static Map<String, Object> mapOfCommands() {
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("cmd", "Page.setDownloadBehavior");
		paramsMap.put("params", mapOfDownloadBehavior());
		return paramsMap;
	}

	public static Map<String, String> mapOfDownloadBehavior() {
		Map<String, String> cmdParamsMap = new HashMap<>();
		cmdParamsMap.put("behavior", "allow");
		cmdParamsMap.put("downloadPath", FILE_PATH);
		return cmdParamsMap;
	}

	public static String jsonStringOfDownloadBehavior() {
		Json json = new Json();
		String content = json.toJson(mapOfDownloadBehavior());
		return content;
	}

	public static String jsonStringOfDownloadCmd() {
		Json json = new Json();
		String content = json.toJson(mapOfCommands());
		return content;
	}

	public static String hostURL() {
		String id = "sprint.com";
		int lengthOfID = id.length();
		int testEnvironPos = Browser.site.indexOf(id) + lengthOfID;
		// int portNum = Browser.getBrowserProxy().getPort();
		// System.out.println("THIS IS THE PORT NUM: "+portNum);;
		// String port = Integer.toString(portNum);
		String host = ":8443";
		// String host = ":8080";
		String importantIDOfTestEnviron = Browser.site.substring(0, testEnvironPos);
		System.out.println("importantIDOfTestEnviron: " + importantIDOfTestEnviron);
		return importantIDOfTestEnviron;
	}

	public static boolean offerToStack(WebDriver driverInstance) {
		return stack.offer(driverInstance);
	}

}