package com.sprint.iice_tests.web.pages.il_digital_paper;

import java.io.File;
import java.io.FilenameFilter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sprint.iice_tests.web.browser.Browser;
import com.sprint.iice_tests.web.browser.BrowserType;

public abstract class PageBase{

	protected WebDriver driver;
	protected WebDriverWait wait;
	protected static final String ELEMENT_TEXT = "//*[contains(text(),'%s')]";
	private static final String EQUALS_TEXT = "//*[text()='%s']";
	public final String CHILD_CONTAINS_TEXT = ".//*[text()='%s']";
	final static String DOWNLOAD_FOLDER = "/src/test/resources/TestData";
	protected static File dir = new File(System.getProperty("user.dir") + DOWNLOAD_FOLDER);
	public final By SPAN = By.cssSelector(" span");
	public final By DIV = By.cssSelector(" div");
	public final By DIV_SPAN = By.cssSelector(" div span");
	public final By CHILDREN = By.xpath("./child::*");
	public final By SPAN_OR_DIV = By.xpath("./div/span | ./div/div");
	public final By FOLLOWING_SIBLING_DIV = By.xpath("./following-sibling::div");
	public final By FOLLOWING_SIBLING = By.xpath(".//following-sibling::*");
	
	public final long threadId = Thread.currentThread().getId();

	protected PageBase() {
		this.driver = Browser.getDriverInstance();
		this.wait = new WebDriverWait(driver, Browser.getWaitTime());
	}

	By equipIDLoc(String ID) {
		return By.xpath("//*[@id='" + ID + "']");
	}

	public WebElement labelContainsText(String text) {
		return retryElementUntilPresent(By.xpath("//label[contains(text(),'" + text + "')]"));
	}

	public WebElement elementWithID(String ID) {
		return retryElementUntilPresent(equipIDLoc(ID));
	}

	public By contains_text(String text) {
		return By.xpath(String.format(ELEMENT_TEXT, text));
	}

	public WebElement elementWithText(String text) {
		return retryElementUntilPresent(contains_text(text));
	}

	public By equals_text(String text) {
		return By.xpath(String.format(EQUALS_TEXT, text));
	}

	public String jsGetTextOfWebElement(WebElement e) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String text = (String) js.executeScript("return arguments[0].innerHTML;", e);
		return text;
	}
	
	public long getPageOffset() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (long) js.executeScript("return window.pageYOffset");
	}
	
	public Double getPageOffsetY() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (Double) js.executeScript("return window.pageYOffset");
	}

	public WebElement elementByClassName(String name) {
		JavascriptExecutor js = (JavascriptExecutor) Browser.getDriverInstance();
		WebElement el = (WebElement) js.executeScript("return document.getElementsByClassName('" + name + "');");
		return el;
	}

	public String jsGetsElementByCssSelector(String macAccountMsg) {
		JavascriptExecutor js = (JavascriptExecutor) Browser.getDriverInstance();
		String el = js.executeScript("return document.querySelector('" + macAccountMsg + "');").toString();
		return el;
	}

	public void clickElementByClassName(String name) throws Exception {
		WebElement el = elementByClassName(name);
		scrollToAndClickElement(el);
	}

	public void jsWaitsForPageToLoad() {
		try {
			wait.until(webDriver -> ((JavascriptExecutor) driver).executeScript("return document.readyState")
					.equals("complete"));
		} catch (Exception e) {
			return;
		}
	}

	public List<String> turnWebElementsToText(List<WebElement> e) {
		try {
			List<String> s = e.stream().map(i -> i.getText()).filter(i -> i.length() > 0).collect(Collectors.toList());
			return deleteDuplicatesAndBreaks(s);
		} catch(Exception NullPointerException) {
			return null;
		}
	}

	public List<String> turnWebElementsToTextKeepDuplicates(List<WebElement> e) {
		return e.stream().map(i -> i.getText()).filter(i -> i.length() > 0).collect(Collectors.toList());
	}

	public List<String> turnWebElementsToTextKeepBlanks(List<WebElement> e) {
		List<String> s = e.stream().map(i -> i.getText().length() > 0 ? i.getText() : " ").collect(Collectors.toList());
		return deleteDuplicatesAndBreaks(s);
	}

	private List<String> deleteDuplicatesAndBreaks(List<String> s) {
		List<String> dd = new ArrayList<String>();
		if (s.size() > 0) {
			splitAndAddStringWithLineBreaks(dd, s.get(0));
			for (int i = 1; i < s.size(); i++) {
				if (!s.get(i).contains("\n")) {
					if (!dd.get(dd.size() - 1).equals(s.get(i)))
						splitAndAddStringWithLineBreaks(dd, s.get(i));
					if (s.get(i).equals(" ")) {
						dd.add(s.get(i));
					}
				}
			}
		}
		return dd;
	}

	private void splitAndAddStringWithLineBreaks(List<String> list, String string) {
		String[] arr = string.split("\n");
		for (String s : arr) {
			list.add(s);
		}
	}

	public Boolean pageisNotReady() {
		Boolean pageIsNOTReady = !((JavascriptExecutor) driver).executeScript("return document.readyState")
				.equals("complete");
		return pageIsNOTReady;
	}

	public Boolean pageIsLoaded() {
		return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
	}

	public void refreshUnreadyPage() throws InterruptedException {

		if (pageisNotReady()) {
			System.out.println("PAGE NEEDED TO BE REFRESHED");
			Browser browser = new Browser();
			browser.refreshPage();
		}
		System.out.println("PAGE DID NOT NEED TO BE REFRESHED");
	}

	protected <T> T retryStaleElements(Callable<T> element) throws Exception {
		String errorMsg = null;
		long startTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - startTime) < (Browser.getWaitTime() * 1000)) {
			try {
				System.out.println(System.currentTimeMillis());
				return element.call();
			} catch (StaleElementReferenceException e) {
				errorMsg = e.getMessage();
			} catch (Exception e) {
				throw e;
			}
		}
		throw new StaleElementReferenceException(errorMsg);
	}

	protected WebElement retryWebElementUntilVisible(WebElement element) {
		WebElement e = wait.until(ExpectedConditions.visibilityOf(element));
		scrollToElement(e);
		return wait.until(ExpectedConditions.visibilityOf(element));
	}

	protected List<WebElement> retryAllWebElementsUntilVisible(List<WebElement> elements) {
		return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
	}

	protected WebElement retryElementUntilVisible(By locator) {
		WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		scrollToElement(e);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	protected List<WebElement> retryAllElementsUntilVisible(By locator) {
		List<WebElement> e = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
		scrollToElement(e.get(0));
		return e;
	}
	

	protected List<WebElement> retryAllElementsUntilDisplayed(By i) {
		wait.until((ExpectedCondition<Boolean>) driver -> driver.findElement(i).isDisplayed());
		List<WebElement> e = driver.findElements(i);
		scrollToElement(e.get(0));
		return e;
	}

	protected WebElement retryElementUntilClickable(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	protected void ensureElementNotStale(WebElement e) {
		wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(e)));
	}

	public WebElement retryElementUntilPresent(By locator) {
		try {
			return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		}catch (Exception ex) {
			return null;
		}
	}
	
	public WebElement tryWebElement(WebElement element, By locator) {
		WebElement e =element;
		try {
			e=e.findElement(locator);
		} catch (NoSuchElementException ex) {
			return e;
		}catch (NullPointerException ex) {
			return e;
		}catch (Exception ex) {
			return e;
		}
		if(!(e==null)) {
			scrollToElement(e);}
		return e;
	}

	protected WebElement findChildOfParent(WebElement parent, By child) {
		try {
			return parent.findElement(child);
		} catch (Exception e) {
			return null;
		}
	}

	protected List<WebElement> findChildrenOfParent(WebElement parent, By child) {
		try {
			return parent.findElements(child);
		} catch (Exception e) {
			return null;
		}
	}

	public String getText(WebElement element) {
		try {
			return element.getText();
		} catch (Exception e) {
			return null;
		}
	}

	public boolean stringEquals(String string, String match) {
		try {
			return string.equals(match);
		} catch (Exception e) {
			return false;
		}
	}

	public WebElement retryElementRefreshFromBy(By e) throws Exception {
		WebElement element = retryElementUntilPresent(e);
		// wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(element)));
		Callable<WebElement> freshElement = () -> (element);
		return retryStaleElements(freshElement);
	}

	protected List<WebElement> retryElementsUntilPresent(By locator) {
		try {
			List<WebElement> e = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
			scrollToElement(e.get(0));
			return e;
		} catch (Exception e) {
			return null;
		}
	}

	protected List<WebElement> retryElementsUntilPresentOnlyDisplayedValues(By locator) {
		try {
			return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator)).stream()
					.filter(i -> i.isDisplayed()).collect(Collectors.toList());
		} catch (Exception e) {
			return null;
		}
	}

	protected List<WebElement> resilientRetry(By locator) {
		for (int i = 0; i < 20; i++) {
			try {
				return retryElementsUntilPresent(locator);
			} catch (Exception e) {
				sleep(100);
			}
		}
		System.out.println("Element is stale");
		return null;
	}

	public Boolean retryUntilTitleContains(String title) {
		return wait.until(ExpectedConditions.titleContains(title));
	}

	public String getElementCssValue(WebElement element, String attribute) {
		try {
			return element.getCssValue(attribute);
		} catch (Exception NullPointerException) {
			return "";
		}
	}

	public String getElementAttribute(WebElement element, String attribute) {
		return element.getAttribute(attribute);
	}

	/**
	 * Method that handles issues where an element might not be immediately
	 * clickable
	 */
	public void resilientClick(WebElement e) {
		for (int i = 0; i < 5; i++) {
			try {
				e.click();
				return;
			} catch (Exception a) {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				try {
					js.executeScript("arguments[0].click()", e);
					return;
				} catch (Exception e1) {
					sleep(100);
				}
			}
		}
	}

	public void clickOrReload(WebElement e) {
		for (int i = 0; i < 10; i++) {
			if (e != null) {
				System.out.println("Element is fine");
				resilientClick(e);
				break;
			}
			System.out.println("Element is null");
			Browser.getDriverInstance().navigate().refresh();
		}
	}

	protected void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	protected String formatPath(String formatBase, Object[] args, String tail) {
		return String.format(formatBase, args) + tail;
	}

	protected String formatPath(String formatBase, Object[] args) {
		return formatPath(formatBase, args, "");
	}

	/**
	 * move to and hover over an element
	 * 
	 * @param element
	 *            WebElement you are targeting
	 */
	public void moveToElement(WebElement element) {
		scrollToElement(element);
		Actions actions = new Actions(driver);
		actions.moveToElement(element).build().perform();
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public void scrollUp() {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0, -250)", "");
	}

	public void hoverOverElementAndWaitForPopup(WebElement hoverItem) {
		try {
			if(!(hoverItem==null)) {
		scrollToElement(hoverItem);
		Actions actions = new Actions(driver);
		scrollUp();
		actions.moveToElement(hoverItem);
		// wait.until(ExpectedConditions.visibilityOf(hoverItem));
		actions.build().perform();
		}}
		catch(NullPointerException ex) {
			
		}catch(NoSuchElementException ex) {
			
		}
	}

	/**
	 * scroll to and click an element
	 * 
	 * @param element
	 *            WebElement you are targeting
	 * @throws Exception
	 */
	public void scrollToAndClickElement(WebElement element) throws Exception {
		scrollToElement(element);
		resilientClick(element);
	}
	
	public void scrollToAndClickAction(WebElement e) {
		Actions ob = new Actions(driver);
		ob.moveToElement(e);
		ob.click(e);
		 ob.build();
		ob.perform();
	}

	public void scrollToElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void scrollToElementInMiddleOfPage(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,250);", element);
	}

	/**
	 * iterate a list of elements and return a match for the getText() method
	 * 
	 * @param targetText
	 *            desired value to match
	 * @param list
	 *            list of elements to iterate
	 */
	public WebElement getElementWithTextFromList(String targetText, List<WebElement> list) {
		for (WebElement element : list) {
			if (element.getText().equals(targetText)) {
				return element;
			}
		}
		System.out.println(targetText + " NOT FOUND IN LIST");
		return null;
	}

	/**
	 * iterate a list of elements and return the list index of a match for the
	 * getText() method
	 * 
	 * @param targetText
	 *            desired value to match
	 * @param list
	 *            list of elements to iterate
	 */
	public int getIndexOfElementWithTextFromList(String targetText, List<WebElement> list) {
		for (WebElement element : list) {
			if (element.getText().equals(targetText)) {
				return list.indexOf(element);
			}
		}
		System.out.println(targetText + "NOT FOUND IN LIST");
		return 0;
	}

	public int getIndexOfStringInList(String targetText, List<String> list) {
		for (String item : list) {
			if (item.equals(targetText)) {
				return list.indexOf(item);
			}
		}
		System.out.println(targetText + "NOT FOUND IN LIST");
		return 0;
	}

	/**
	 * iterate a list of elements and return the list index of containing text from
	 * the getText() method. Usefull for row descriptions that are mutually
	 * exclusive but similar, i.e contracts row
	 * 
	 * @param targetText
	 *            desired value to match
	 * @param list
	 *            list of elements to iterate
	 */
	public int getIndexOfElementContainingTextFromList(String targetText, List<WebElement> list) {
		for (WebElement element : list) {
			if (element.getText().contains(targetText)) {
				return list.indexOf(element);
			}
		}
		System.out.println(targetText + " NOT FOUND IN LIST");
		return 0;
	}

	public By locationOfDirectSiblingByCss(String nextDirectSiblingAnchor) {
		return By.cssSelector(" + " + nextDirectSiblingAnchor + "");
	}

	public WebElement elementWithinElement(String uniqueParentElementText, String uniqueNestedElementText) {
		return retryElementUntilVisible(locationOfElementBySpanText(uniqueParentElementText))
				.findElement(locationOfElementBySpanText(uniqueNestedElementText));
	}

	public WebElement directSiblingOfElement(String uniqueElementText, String nextDirectSiblingAnchor) {
		return retryElementUntilVisible(locationOfElementBySpanText(uniqueElementText))
				.findElement(locationOfDirectSiblingByCss(nextDirectSiblingAnchor));
	}

	public String textOfSiblingElement(String uniqueElementText, String nextDirectSiblingAnchor) {
		return directSiblingOfElement(uniqueElementText, nextDirectSiblingAnchor).getText();
	}

	public By locationOfElementBySpanText(String uniqueTextOfElement) {
		return By.xpath("//span[contains(text(),'" + uniqueTextOfElement + "')]");
	}

	public boolean elementIsPresent(WebElement webElement) {
		return webElement != null;
	}

	public By locationOfElementByAnchorAndText(String uniqueTextOfElement) {
		return By.xpath(".//*[contains(text(),'" + uniqueTextOfElement + "')]");
	}

	By msgByBAnchorLoc(String uniqueTextOfElement) {
		return By.xpath("//b[contains(text(),'" + uniqueTextOfElement + "')]");
	}

	public WebElement getElementByTextFromAnchorParam(String ID, String uniqueTextOfElement) {
		return elementWithID(ID).findElement(locationOfElementByAnchorAndText(uniqueTextOfElement));
	}

	public String textOfElement(WebElement element) {
		String t=null;
		try{
			t=element.getText();}
	catch(NoSuchElementException ex) {
	}catch(NullPointerException ex) {
	}
	return t;
	}

	public void printTextOFElementInListNewLine(List<String> list) {
		for (String e : list) {
			System.out.println("THE ELEMENT TEXT IS_" + e + "_");
		}
	}

	public String getJSElementAsString(String executeText) {
		jsWaitsForPageToLoad();
		JavascriptExecutor executor = (JavascriptExecutor) Browser.getDriverInstance();
		return (String) executor.executeScript(executeText);
	}

	public String getJSElementAsString(String executeText, WebElement element) {
		jsWaitsForPageToLoad();
		JavascriptExecutor executor = (JavascriptExecutor) Browser.getDriverInstance();
		return (String) executor.executeScript(executeText, element);

	}

	public Boolean doesTheElementExist(By e) {
		List<WebElement> list = driver.findElements(e);
		return list != null && list.size() != 0;
	}

	public boolean browserIsChrome() {
		System.out.println("Here is my browser: " + Browser.getBrowserType());
		return Browser.getBrowserType().equals(BrowserType.CHROME.getBrowserTypeName());
	}

	public boolean browserIsFirefox() {
		System.out.println("Here is my browser: " + Browser.getBrowserType());
		return Browser.getBrowserType().equals(BrowserType.FIREFOX.getBrowserTypeName());
	}

	public void sendTextToElement(WebElement e, String s) {
		resilientClick(e);
		e.sendKeys(s);
		e.sendKeys(Keys.RETURN);
	}

	public List<String> turnWebElementsToTextOrderLeftToRight(List<WebElement> web) {
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		List<String> list = new ArrayList<String>();
		for (WebElement e : web) {
			String s = e.getText();
			int x = Integer.valueOf(e.getLocation().getX()) + Integer.valueOf(e.getLocation().getY());
			map.put(x, s);
		}
		for (String s : map.values()) {
			if (s.length() != 0)
				list.add(s);
		}
		return deleteDuplicatesAndBreaks(list);
	}

	public List<String> turnWebElementsToTextOrderLeftToRightKeepBlanks(List<WebElement> web) {
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		List<String> list = new ArrayList<String>();
		for (WebElement e : web) {
			String s = e.getText();
			if (s.length() > 0) {
				map.put(Integer.valueOf(e.getLocation().getX()), s);
			} else {
				map.put(Integer.valueOf(e.getLocation().getX()), " ");
			}
		}
		for (String s : map.values()) {
			list.add(s);
		}
		return deleteDuplicatesAndBreaks(list);
	}

	public List<String> turnWebElementsToTextOrderLeftToRightKeepDuplicates(List<WebElement> web) {
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		List<String> list = new ArrayList<String>();
		for (WebElement e : web) {
			String s = e.getText();
			if (s.length() > 0) {
				map.put(Integer.valueOf(e.getLocation().getX()), s);
			}
		}
		for (String s : map.values()) {
			if (s != " ")
				list.add(s);
		}
		return list;
	}

	public String retryElementAndGetText(By by) {
		try {
			wait.until((ExpectedCondition<Boolean>) driver -> driver.findElement(by).getText().length() != 0);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			return " ";
		}
	}

	public boolean pdfDownloads() {
		waitForPDFToDownload();
		return dir.listFiles(pdfFilter).length == 1;
	}

	public void waitForPDFToDownload() {
		wait.until((ExpectedCondition<Boolean>) driver -> dir.listFiles(pdfFilter).length == 1);
	}
	
	public void waitForCSVToDownload(int fileNum) {
		wait.until((ExpectedCondition<Boolean>) driver -> dir.list(filter).length == fileNum + 1);
	}

	public void waitForElementClass(WebElement buttonWithText, String value) {
		wait.until(ExpectedConditions.attributeContains(buttonWithText, "class", value));
	}

	FilenameFilter pdfFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".pdf");
		}
	};
	
	FilenameFilter filter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".csv");
		}
	};
	
	public void waitForInput() {
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
		sc.close();
	}
	
    public <T, U> boolean waitUntilCondition(BiFunction<T, U, Boolean> condition, T param1, U param2, int pollMillis, int timeOutSecs){
		FluentWait<WebDriver> quickWait = new FluentWait<WebDriver>(driver)
				.pollingEvery(Duration.ofMillis(pollMillis))
				.withTimeout(Duration.ofSeconds(timeOutSecs));

        quickWait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    return condition.apply(param1, param2);
                } catch (NullPointerException e) {
                    return false;
                } catch(Exception e) {
                	System.out.println("Error at Wait");
                }
                return false;
            }
        });
        return false;
    }
	
    public <T> boolean waitUntilCondition(Function<T, Boolean> condition, T parameter, int pollMillis, int timeOutSecs){
		FluentWait<WebDriver> quickWait = new FluentWait<WebDriver>(driver)
				.pollingEvery(Duration.ofMillis(pollMillis))
				.withTimeout(Duration.ofSeconds(timeOutSecs));

        quickWait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    return condition.apply(parameter);
                } catch (NullPointerException e) {
                    return false;
                } catch(Exception e) {
                	System.out.println("Error at Wait");
                }
                return false;
            }
        });
        return false;
    }
    
    public boolean waitUntilCondition(BooleanSupplier condition, int pollMillis, int timeOutSecs){
		FluentWait<WebDriver> quickWait = new FluentWait<WebDriver>(driver)
				.pollingEvery(Duration.ofMillis(pollMillis))
				.withTimeout(Duration.ofSeconds(timeOutSecs));

        quickWait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    return condition.getAsBoolean();
                } catch (NullPointerException e) {
                    return false;
                } catch(Exception e) {
                	System.out.println("Error at Wait");
                }
                return false;
            }
        });
        return false;
    }

}