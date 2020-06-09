package com.sprint.iice_tests.web.pages.il_digital_paper;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.WatchKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import com.sprint.iice_tests.lib.dao.data.Constants;
import com.sprint.iice_tests.utilities.test_util.DataFileFinder;
import com.sprint.iice_tests.web.browser.Browser;

public abstract class TabBase extends PageBase {
	private Robot robot;
	final static String DOWNLOAD_FOLDER = "/src/test/resources/TestData";
	private static final By DOWNLOAD_PRODUCES_CSV = By.cssSelector(".download-link");
	protected static File dir = new File(System.getProperty("user.dir") + DOWNLOAD_FOLDER);
	By downloadButtonLoc = By.xpath("//a[contains(text(),'Download')]");
	By listOfKeysLoc = By.xpath("//div[contains(@class, 'first-col')]//div[contains(@class, 'bb-row')]");
	By appendedRowCell = By.xpath(".//div[contains(@class, 'bb-col')]");
	By firstColLoc = By.id("first-col");
	By headerSingleElementLoc = By.xpath("//div[contains(@class, 'bb-row bb-row-headers')]");
	private final By EXPAND_BUTTONS = By.cssSelector(".row.middle-xs > div > span > .ico");
	public Predicate<WebElement> buttonIsOpen = i -> getElementAttribute(i, "class").matches(".*collapse.*");
	public Function<WebElement, Boolean> buttonElementIsOpen = e -> e.getAttribute("class").contains("collapse");
	
	/*
	 * Constants associated with click and wait methods
	 */
	private static final int POLLING_MILLIS = 500;
	private static final int TIMEOUT_SECS = 120;
	private static final int QUICK_TIMEOUT_SECS = 5;
	private static final int QUICK_SLEEP_MILLIS = 1000;
	private static final int SLEEP_MILLIS = 2000;

	By specificHeaderLoc(String headerName) {
		return By.xpath(".//div[contains(@class, 'others-col')]//span[text()='" + headerName + "']");
	}

	By specificKeyLoc(String firstColKey) {
		return By.xpath("//div[contains(@class, 'first-col')]//div[contains(@class, 'bb-row')]//span[text()='"
				+ firstColKey + "']");
	}

	public String messageDisplaysString(String s) {
		s = s.length() > 30 ? s.substring(0, 30) : s; // Selector does not work for long strings, this trims down large
														// messages, with 30 being a string long enough to be unique yet
														// short enough to easily handle.
		return getMessageWithText(s) != null ? deleteLineBreaks(getText(getMessageWithText(s))) : " ";
	}

	public List<String> messagesMatchingString(String s) {
		s = s.length() > 30 ? s.substring(0, 30) : s; // Selector does not work for long strings, this trims down large
														// messages, with 30 being a string long enough to be unique yet
														// short enough to easily handle.
		return getMessagesWithText(s) != null ? turnWebElementsToTextKeepDuplicates(getMessagesWithText(s)) : null;
	}

	private List<WebElement> getMessagesWithText(String s) {
		return retryElementsUntilPresent(contains_text(s));
	}

	public WebElement downloadButton() {
		return retryElementUntilVisible(downloadButtonLoc);
	}

	public List<WebElement> downloadButtons() {
		return retryElementsUntilPresent(downloadButtonLoc);
	}

	public WebElement getMessageWithText(String s) {
		return retryElementUntilPresent(contains_text(s));
	}

	By okButtonLoc = By.xpath("//a[contains(text(),'Ok')]");

	public Boolean invokeDownload(String csv) throws Exception {
		WatchKey watchKey = DataFileFinder.setWatcher();
		DataFileFinder data = new DataFileFinder();
		return data.clickUntilFileDownloadsByDownloadButton(watchKey, csv);
	}

	public Boolean invokeDownloadAndOk(String csv) throws Exception {
		// clickDownloadButton method from tab base will not work if trying to get the
		// download
		// button from the second row. There is a list of webelements in a situation
		// with multiple
		// rows, and if not specified will always return the first one, which will never
		// be visible.
		Boolean fileDownloaded = false;
		WatchKey watchKey = DataFileFinder.setWatcher();
		resilientClick(openDownloadButton());
		// OK button does not always pop up, especially when switching and downloading
		// between radio buttons.
		long startTime = System.currentTimeMillis();
		long waitTime = (Browser.getWaitTime() * 1000);
		long endTime = (startTime + waitTime);
		if (!csvPopupDisplayed()) {
			System.out.println("POP UP NOT DISPLAYED");

			while (!csvPopupDisplayed() && (System.currentTimeMillis() < (endTime))) {
				resilientClick(openDownloadButton());
			}

		}

		if (csvPopupDisplayed()) {
			System.out.println("POP UP DISPLAYED");
			DataFileFinder data = new DataFileFinder();
			fileDownloaded = data.clickUntilFileDownloadsByOKButton(watchKey, csv);
		}
		return fileDownloaded;
	}

	/*
	 * for (int i = 0; i < 25; i++) { if (dir.listFiles(filter).length == 0 &&
	 * csvPopupDisplayed()) { resilientClick(openOkayButton()); } if
	 * (dir.listFiles(filter).length == 1) break; }
	 */

	public boolean csvPopupDisplayed() {
		try {
			driver.findElement(DOWNLOAD_PRODUCES_CSV).isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public WebElement openOkayButton() {
		int index = getIndexOfOpenRow();
		return okayButtons().get(index);
	}

	private List<WebElement> okayButtons() {
		return retryElementsUntilPresent(okButtonLoc);
	}

	public WebElement openDownloadButton() {
		int index = getIndexOfOpenRow();
		return downloadButtons().get(index);
	}

	/**
	 * Method used to avoid indexing issues between accounts, goes through expand
	 * buttons and makes sure only the one with the integer matching i is expanded
	 * 
	 * @throws InterruptedException
	 */
	public void cleanExpandButtons(List<WebElement> expandButtonList, int indexOfTarget) throws InterruptedException {
		List<WebElement> list = expandButtonList;
		int i = indexOfTarget;
		for (int j = 0; j < list.size(); j++) {
			Boolean click = list.get(j).getAttribute("class").contains("collapse") && j != i;
			click |= list.get(j).getAttribute("class").contains("expand") && j == i;
			if (click) {
				resilientClick(list.get(j));
			}
		}
	}

	private By svg_polygon(String hrefId) {
		return By.cssSelector("symbol" + hrefId + " > g > polygon");
	}

	public WebElement svgPolygon(String hrefId) {
		return driver.findElement(svg_polygon(hrefId));
	}

	public String getSvgPolygonPoints(String hrefId) {
		return svgPolygon(hrefId).getAttribute("points");
	}

	public By creditCardSymbolLoc = By.xpath("//div[@class='credit-card-block']");

	public WebElement creditCardSymbol() {
		return driver.findElement(creditCardSymbolLoc);
	}

	public Boolean creditCardSymbolIsDisplayed() {
		return creditCardSymbol().isDisplayed();
	}

	public WebElement creditCardSymbolSvgUse() {
		return creditCardSymbol().findElement(By.cssSelector(".//child::*//child::*"));
	}

	public String getCreditCardXlinkHrefId() {
		return getElementAttribute(creditCardSymbolSvgUse(), "xlink:href");
	}

	private By svg_path(String hrefId) {
		return By.cssSelector("symbol" + hrefId + " > g > path:nth-child(1)");
	}

	public WebElement svgPath(String hrefId) {
		return driver.findElement(svg_path(hrefId));
	}

	public String getSvgPath(String hrefId) {
		return svgPath(hrefId).getAttribute("path");
	}

	By barGraphLoc = By.cssSelector("svg > rect.bb-filled-overage-rect");

	public WebElement barGraph() {
		return driver.findElement(barGraphLoc);
	}

	public String barGraphColor() {
		return barGraph().getCssValue(Constants.BAR_GRAPH_COLOR_FILL_CSS_VALUE);
	}

	public String barGraphFillAmount() {
		return barGraph().getAttribute("width");
	}

	public int getIndexOfParentElement(String headingText, List<WebElement> expandButtonHeadings,
			String BUTTON_TEXT_TAIL) {
		for (WebElement element : expandButtonHeadings) {
			WebElement child = element.findElement(By.cssSelector(BUTTON_TEXT_TAIL));
			String childText = child.getText();
			if (childText.equals(headingText)) {
				return expandButtonHeadings.indexOf(element);
			}
		}
		return 0;
	}

	public void cleanExpandButtonFromParent(List<WebElement> expandButtons, int index, String expandButtonTail)
			throws InterruptedException {
		for (int j = 0; j <= expandButtons.size() - 1; j++) {
			WebElement child = testForSecondElement(expandButtons.get(j), expandButtonTail);
			String attr = child.getAttribute("class");
			Boolean click = attr.contains("collapse") && j != index && !attr.contains("hide");
			click |= attr.contains("expand") && j == index && !attr.contains("hide");
			if (click) {
				resilientClick(child);
			}
		}
	}

	private WebElement testForSecondElement(WebElement webElement, String expandButtonTail) {
		String newTail = expandButtonTail + ":nth-of-type(2)";
		try {
			if (!webElement.findElement(By.cssSelector(newTail)).getAttribute("class").contains("ng-hide")) {
				return webElement.findElement(By.cssSelector(newTail));
			} else {
				return webElement.findElement(By.cssSelector(expandButtonTail));
			}
		} catch (Exception e) {
			return webElement.findElement(By.cssSelector(expandButtonTail));
		}
	}

	public final By miniHeaderNextToReadBoxLoc(String headerName) {
		return By.xpath("//a[contains(text(),'" + headerName + "')]");
	}

	public WebElement miniHeaderNextToReadBox(String headerName) {
		return retryElementUntilVisible(miniHeaderNextToReadBoxLoc(headerName));
	}

	public Boolean miniHeaderNextToReadBoxIsDisplayed(String headerName) {
		return miniHeaderNextToReadBox(headerName).isDisplayed();
	}

	public Map<String, String> createTableOfStringsFromWebElements(List<WebElement> headerList,
			List<WebElement> rowList) {
		List<String> cleanHeaders = makeListElementsStrings(headerList);
		List<String> cleanRows = makeListElementsStrings(rowList);
		return fillTableWithStringValues(cleanHeaders, cleanRows);
	}

	public Map<String, String> fillTableWithStringValues(List<String> headerList, List<String> rowList) {
		Map<String, String> table = new HashMap<String, String>();
		for (int i = 0; i < headerList.size(); i++) {
			table.put(headerList.get(i), rowList.get(i));
		}
		return table;
	}
	
	public <T> Map<T, WebElement> fillTableWithWebElements(List<T>headerList, List<WebElement> rowList) {
		Map<T, WebElement> table =  new HashMap<T, WebElement>();
		for (int i = 0; i < headerList.size(); i++) {
			table.put( headerList.get(i), rowList.get(i));
		}
		return table;
	}

	public List<String> makeListElementsStrings(List<WebElement> list) {
		list.removeIf(Objects::isNull);
		List<String> listOfStrings = new ArrayList<>();
		for (WebElement element : list) {
			String newString = getTextOfElementWithoutLineBreaks(element);
			if (newString.length() == 0)
				newString = "XXXX";
			listOfStrings.add(newString);
		}
		return listOfStrings;
	}

	public String getTextOfElementWithoutLineBreaks(WebElement element) {
		String text = element.getText();
		String stringWithNoLineBreaks = deleteLineBreaks(text);
		return stringWithNoLineBreaks;

	}

	public List<String> makeListOfWebElementsAListOfStrings(List<WebElement> list) {
		List<String> listOfStrings = new ArrayList<>();
		for (WebElement element : list) {
			listOfStrings.add(element.getText());
			return listOfStrings;
		}
		return listOfStrings;
	}

	public List<String> cleanListOfWebElementsAListOfStrings(List<WebElement> list) {
		List<String> listOfStrings = new ArrayList<>();
		for (WebElement element : list) {
			listOfStrings.add(deleteLineBreaks(element.getText()));

		}
		return listOfStrings;
	}

	public String deleteSpaces(String element) {
		return element.replaceAll("\\s", "");
	}

	public String deleteLineBreaks(String element) {
		return element.replaceAll("\\r\\n|\\r|\\n", " ");
	}

	public String deleteBrTag(String element) {
		return element.replaceAll("<br />", "");
	}

	public Boolean listContainsElements(List<WebElement> list) {
		return list != null && list.size() > 0;
	}

	private By plusSignButtonTitle(String nameOfPlusSign) {
		return By.xpath("//span[text()='" + nameOfPlusSign + "']/parent::*/parent::*/parent::*//i");
	}

	/**
	 * This grabs the plus sign button you want to open or close.
	 * 
	 * @param nameOfPlusSign
	 *            = the name of content that is revealed or hidden by the plus sign
	 *            button
	 * @return the plus sign you want to open or close
	 */

	public WebElement desiredPlusSignButton(String nameOfPlusSign) {
		List<WebElement> possibleList = driver.findElements(plusSignButtonTitle(nameOfPlusSign));
		for (WebElement e : possibleList) {
			if (plusSignIsNotHidden(e)) {
				return e;
			} else if (plusSignIsHidden(e)) {
				possibleList.remove(e);
			}
		}
		System.out.println(possibleList);
		return possibleList.get(0);
	}

	private By anyOpenButtonLoc = By.xpath("//i[contains(@class, 'ico ico--collapse')]");

	public List<WebElement> listOfOpenPlusSignButtons() {
		return driver.findElements(anyOpenButtonLoc);
	}

	public void openDesiredButtonAndCloseUndesiredButtons(String nameOfPlusSign) throws Exception {
		collapseOpenUndesiredButtons(nameOfPlusSign);
		expandClosedDesiredButton(nameOfPlusSign);
	}

	public void expandClosedDesiredButton(String nameOfPlusSign) throws Exception {
		WebElement desiredPlusSign = desiredPlusSignButton(nameOfPlusSign);
		scrollToElement(desiredPlusSign);
		if (plusSignIsClosed(desiredPlusSign)) {
			scrollToAndClickElement(desiredPlusSign);
		} else {
			System.out.println(nameOfPlusSign + " is already open");
		}
	}

	/**
	 * This checks to see if the plus sign is closed based on an attribute that is
	 * indicative of a closed button.
	 * 
	 * @param button
	 *            = a plus sign button
	 * @return true if the button is closed
	 */

	public Boolean plusSignIsClosed(WebElement button) {
		return button.getAttribute("class").contains("ico ico--expand");
	}

	public Boolean plusSignIsOpen(String nameOfPlusSign) {
		WebElement button = desiredPlusSignButton(nameOfPlusSign);
		return !button.getAttribute("class").contains("ico ico--expand");
	}

	public void collapseOpenUndesiredButtons(String nameOfPlusSign) throws Exception {
		List<WebElement> listOfOpenPlusSigns = listOfOpenPlusSignButtons();
		WebElement desiredPlusSign = desiredPlusSignButton(nameOfPlusSign);

		if (listOfOpenPlusSigns != null) {
			for (WebElement openPlusSign : listOfOpenPlusSigns) {
				if (elementsAreNotTheSame(openPlusSign, desiredPlusSign) && plusSignIsNotHidden(openPlusSign)) {
					scrollToAndClickElement(openPlusSign);
				}
			}
		}
	}

	/**
	 * This checks to see if the plus sign is open by determining if it has an
	 * attribute that is indicative of a button hidden from view. When the plus sign
	 * is hidden, it is unable to expand content.
	 * 
	 * @param button
	 *            = a plus sign button
	 * @return true if the plus sign is not hidden
	 */

	public Boolean plusSignIsNotHidden(WebElement button) {
		return !button.getAttribute("class").contains("ico ico--collapse ng-hide");
	}

	public Boolean plusSignIsHidden(WebElement button) {
		return button.getAttribute("class").contains("ico ico--collapse ng-hide");
	}

	/**
	 * This checks to see if two elements are identical
	 * 
	 * @param element
	 *            = element you want to compare the desired element to
	 * @param desiredElement
	 *            = element you want to inspect
	 * @return true if the elements are not equal to each other
	 */

	public Boolean elementsAreNotTheSame(WebElement element, WebElement desiredElement) {
		return !element.equals(desiredElement);
	}

	public String getMsgParagraphText(String ID, String identifyingTextForMsg) {
		System.out.println("MESSAGE PARAGRAPH TEXT IS: " + getMsgParagraph(ID, identifyingTextForMsg).getText());
		return getMsgParagraph(ID, identifyingTextForMsg).getText();
	}

	public WebElement getMsgParagraph(String ID, String identifyingTextForMsg) {
		System.out.println("THE ELEMENT IS: " + getElementByTextFromAnchorParam(ID, identifyingTextForMsg));
		return getElementByTextFromAnchorParam(ID, identifyingTextForMsg);

	}

	public String getFontWeightOfMsg(String ID, String identifyingTextForMsg) {
		System.out.println("FOR TEXT: " + getMsgParagraph(ID, identifyingTextForMsg).getText()
				+ "MESSAGE PARAGRAPH FONT WEIGHT IS: "
				+ getMsgParagraph(ID, identifyingTextForMsg).getCssValue(Constants.FONT_WEIGHT_CSS_VALUE));
		return getMsgParagraph(ID, identifyingTextForMsg).getCssValue(Constants.FONT_WEIGHT_CSS_VALUE);
	}

	public String getFontWeightOfElement(WebElement e) {
		return e.getCssValue(Constants.FONT_WEIGHT_500);
	}

	public By phoneNumberHeaderElementLoc(String uniqueTextOfElement) {
		return By.xpath("//span[contains(@class, 'light no-wrap') and text() = '" + uniqueTextOfElement + "']");
	}

	public WebElement phoneNumberHeaderElement(String uniqueTextOfElement) {
		return driver.findElement(phoneNumberHeaderElementLoc(uniqueTextOfElement));
	}

	public void scrollToPhoneNumberHeaderByItsTextFont(String elementName) {
		WebElement element = phoneNumberHeaderElement(elementName);
		scrollToElement(element);
	}

	public By headerLoc(String headerName) {
		return By.xpath("//span[contains(@class, 'light') and text() = '" + headerName + "']");
	}

	public WebElement header(String headerName) {
		return driver.findElement(headerLoc(headerName));
	}

	public String headerNameText(String headerName) {
		return header(headerName).getText();
	}

	public String getLocationOfElementTextInList(String elementText, List<WebElement> list) throws Exception {
		for (WebElement orderVal : list) {
			if (orderVal.getText().equals(elementText)) {
				String orderLoc = orderVal.getLocation().toString();
				System.out.println("THE POSITION OF " + elementText + " IS " + orderLoc);
				return orderLoc;
			}
			continue;
		}
		return null;
	}

	public String getLocationOfElement(WebElement element) {
		System.out.println("THE LOCATION OF " + element + " IS: " + element.getLocation().toString());
		return element.getLocation().toString();
	}

	public int getYLocationOfElement(WebElement e) {
		return e.getLocation().getY();
	}

	public String diffInVerticalPosOfTwoElements(WebElement top, WebElement bottom) {
		return convertIntToString(diffInVerticalPosOfTwoElementsAsInt(top, bottom));
	}

	/**
	 * 
	 * @param top
	 *            = element we are verifying is above another element (bottom
	 *            element)
	 * @param bottom
	 *            = element we are verifying is below another element (top element)
	 * @return the vertical difference between two elements
	 */

	public int diffInVerticalPosOfTwoElementsAsInt(WebElement top, WebElement bottom) {
		int differenceInVerticalPosition = -(getYLocationOfElement(top) - getYLocationOfElement(bottom));
		return differenceInVerticalPosition;
	}

	/**
	 * 
	 * @param top
	 *            = element we are verifying is above another element (bottom
	 *            element)
	 * @param bottom
	 *            = element we are verifying is below another element (top element)
	 * @param range
	 *            = the acceptable error bounds for the vertical difference between
	 *            two elements
	 * @return true if the elements are in correct vertical relation to another,
	 *         within acceptable error bounds
	 */
	public boolean diffInVerticalPosOfTwoElementsIsWithinSpecifiedRange(WebElement top, WebElement bottom, int range) {
		int diff = diffInVerticalPosOfTwoElementsAsInt(top, bottom);
		boolean diffIsWithinLowerRange = (diff - range) <= (diff);
		boolean diffIsWithinUpperRange = (diff) <= (diff + range);
		return diffIsWithinLowerRange || diffIsWithinUpperRange;
	}

	public int convertStringToInt(String value) {
		return Integer.parseInt(value);
	}

	public String convertIntToString(int value) {
		return Integer.toString(value);
	}

	public By questMarkNextToTextLoc(String text) {
		return By.xpath("//span[text() = '" + text + "']/../..//i[@class = '"
				+ Constants.QUEST_MARK_NEXT_TO_TEXT_ICON_ATTRIBUTE + "']");
	}

	public WebElement questMarkNextToText(String text) {
		return driver.findElement(questMarkNextToTextLoc(text));
	}

	public WebElement popupOfQuesMarkNextToText(String text) {
		WebElement questMark = questMarkNextToText(text);
		return questMark.findElement(By.xpath(".//div/div/div/div[@class='popup-content']"));
	}

	public String popUpTextNextToText(String text) {
		return popupOfQuesMarkNextToText(text).getText();
	}

	/**
	 * DATA TABLE FOUNDATION METHODS FOR USAGE DATA TABLE AND CALLS TEXT LOG DATA
	 * TABLE
	 * 
	 * @return
	 */
	public WebElement firstCol() {
		return driver.findElement(firstColLoc);
	}

	public WebElement headerSingleElement() {
		return driver.findElement(headerSingleElementLoc);
	}

	public List<WebElement> listOfKeys() {
		return driver.findElements(listOfKeysLoc);
	}

	public WebElement specificKey(String firstColKey) {
		return driver.findElement(specificKeyLoc(firstColKey));
	}

	public Integer amountOfCalls() {
		return listOfKeys().size();
	}

	/**
	 * @return a string of the ptn without phone number annotations
	 */

	public String cleanPTNWebElement(WebElement ptn) {
		String clean = cleanPTNString(ptn.getText());
		return clean;
	}

	public String cleanPTNString(String ptn) {
		String clean = ptn.replaceAll("\\D+", "");
		return clean;
	}

	public long pntAsLong(String ptn) {
		String cleanPtn = cleanPTNString(ptn);
		long intPtn = Long.parseLong(cleanPtn);
		return intPtn;
	}

	public long convertPtnWebElementToLong(WebElement ptn) {
		String ptnAsString = cleanPTNWebElement(ptn);
		long ptnAsInt = pntAsLong(ptnAsString);
		return ptnAsInt;
	}

	/**
	 * This routine converts ptn Strings into Long values. We need ptn values as
	 * Longs so that we can later determine whether they are ascending or
	 * descending.
	 * 
	 * @param listOfPtns
	 *            = the column of ptn values
	 * @return = column of ptn values as Long values
	 */

	public List<Long> convertListOfStringPtnIntoLongs(List<String> listOfPtns) {
		List<Long> listOfPtnInts = new ArrayList<>(listOfPtns.size());
		for (String e : listOfPtns) {
			String cleanPtn = cleanPTNString(e);
			long intPtn = Long.parseLong(cleanPtn);
			listOfPtnInts.add(intPtn);
		}
		return listOfPtnInts;
	}

	/**
	 * This routine creates a list of elements that are identical ptns. We need a
	 * list of identical ptns to create a control for testing a csv column of ptn
	 * values.
	 * 
	 * @param ptn
	 *            = the ptn
	 * @param listOfElements
	 *            = the size of the column in the csv.
	 * @return = a list of identical ptn String values
	 */

	public List<String> createListOfIdenticalPtnWebElements(WebElement ptn, List<WebElement> listOfElements) {
		List<String> listOfPtnInts = new ArrayList<>(listOfElements.size());
		for (int i = 0; i < listOfElements.size(); i++) {
			listOfPtnInts.add(cleanPTNWebElement(ptn));
		}
		return listOfPtnInts;
	}

	public List<String> createListOfIdenticalAllCapNames(WebElement name, List<WebElement> listOfElements) {
		List<String> listOfPtnInts = new ArrayList<>(listOfElements.size());
		for (int i = 0; i < listOfElements.size(); i++) {
			listOfPtnInts.add(makeWebElementTextUpperCase(name));
		}
		return listOfPtnInts;
	}

	public List<Double> convertListOfStringCostsIntoDoubles(List<String> listOfCosts) {
		List<Double> costListAsDoubles = new CopyOnWriteArrayList<>();

		for (String e : listOfCosts) {
			System.out.println("Here is the cost: " + e);
			if (e.startsWith("$")) {
				e = e.substring(1, e.length());
			}
			System.out.println("Here is the cost now: " + e);
			Double newCost = Double.parseDouble(e);
			costListAsDoubles.add(newCost);
		}
		System.out.println("LIST AS DOUBLES: " + costListAsDoubles);

		return costListAsDoubles;
	}

	// costListAsDoubles.stream().map(mapper)(removeTheDollarSign);
	Function<String, String> removeTheDollarSign = cost -> cost.replace("$", "").trim();

	public List<String> removeNullCosts(List<String> listOfCosts) {
		System.out.println("BEFORE REMOVING NULLS: " + listOfCosts);
		for (String e : listOfCosts) {
			if (e.equals("-")) {
				listOfCosts.remove(e);
			}
		}
		System.out.println("AFTER REMOVING NULLS: " + listOfCosts);
		return listOfCosts;
	}

	/**
	 * This routine converts any null values denoted by "-" into "$0.00". this
	 * allows us to numerically compare the cost column values against each other
	 * e.g. see if the column has ascending or descending cost values
	 * 
	 * @param costColumn
	 *            = String values of the cost column
	 * @return
	 */
	public CopyOnWriteArrayList<String> convertNullCostsToZero(CopyOnWriteArrayList<String> costColumn) {
		String zeroCost = "$0.00";

		for (String e : costColumn) {
			if (e.equals("-")) {
				int indexOfNull = costColumn.indexOf(e);
				String zero = e.replace("-", zeroCost);
				costColumn.set(indexOfNull, zero);
			} else {
				continue;
			}
		}
		return costColumn;
	}

	public Boolean areDatesDescending(Date date, Date date2) {
		return (date2).before(date);
	}

	public Boolean areDatesAscending(java.util.Date firstDate, java.util.Date nextDate) {
		return (nextDate).after(firstDate);
	}

	public Boolean isListInAlphabeticalOrder(List<String> list) {
		boolean sorted = true;
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				if (i == i + 1) {
					continue;
				}
				if (list.get(i).compareTo(list.get(j)) > 0) {
					sorted = false;
				}
			}
		}
		return sorted;
	}

	public List<String> sortListInAlphabeticalOrder(List<String> list) {
		Collections.sort(list);
		return list;
	}

	public Boolean listIsInAlphabeticalOrder(List<String> list) {
		return list.equals(sortListInAlphabeticalOrder(list));
	}

	public Boolean isListOfDatesAscending(List<java.util.Date> list) {
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i - 1) == list.get(i)) {
				continue;
			}
			if (areDatesAscending(list.get(i), list.get(i - 1))) {
				return true;
			}
		}
		return false;
	}

	public Boolean isListOfDatesDescending(List<java.util.Date> list) {
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i - 1) == list.get(i)) {
				continue;
			}
			if (areDatesDescending(list.get(i), list.get(i - 1))) {
				return true;
			}
		}
		return false;
	}

	public boolean isListOfPtnsIncreasing(List<Long> list) {
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i - 1) == list.get(i)) {
				continue;
			}
			if (list.get(i - 1) < list.get(i)) {
				return true;
			}
		}
		return false;
	}

	public boolean isListOfPtnsDecreasing(List<Long> list) {
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i - 1) == list.get(i)) {
				continue;
			}
			if (list.get(i - 1) > list.get(i)) {
				return true;
			}
		}
		return false;
	}

	public boolean isCostIncreasing(List<Double> list) {
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i - 1) == list.get(i)) {
				continue;
			}
			if (list.get(i - 1) < list.get(i)) {
				return true;
			}
		}
		return false;
	}

	public boolean isCostDecreasing(List<Double> list) {
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i - 1) == list.get(i)) {
				continue;
			}
			if (list.get(i - 1) > list.get(i)) {
				return true;
			}
		}
		return false;
	}

	public List<Date> convertStringsIntoDates(List<String> list, String datePattern) throws java.text.ParseException {
		List<Date> listOfDates = new ArrayList<Date>();
		for (String e : list) {
			Date date = formatDateStringIntoDate(e, datePattern);
			listOfDates.add(date);
		}
		return listOfDates;
	}

	public LocalTime addUpTimes(LocalTime time1, String nextTime) {
		LocalTime time2 = LocalTime.parse(nextTime);
		LocalTime newTime = time1.plus(time2.getHour(), ChronoUnit.HOURS).plus(time2.getMinute(), ChronoUnit.MINUTES)
				.plus(time2.getSecond(), ChronoUnit.SECONDS);
		return newTime;
	}

	public LocalTime addUpListOfDatesIntoTimes(List<String> list) throws ParseException {
		LocalTime sumOfTimes = LocalTime.parse("00:00:00");
		for (String e : list) {
			System.out.println("the time being added is: " + e);
			sumOfTimes = addUpTimes(sumOfTimes, e);
			System.out.println("the sum is: " + sumOfTimes);
		}
		return sumOfTimes;
	}

	public LocalTime makeStringALocalTime(String time) {
		LocalTime newTime = LocalTime.parse(time);
		return newTime;
	}

	public double makeStringADouble(String text) {
		double stringIntoDouble = Double.valueOf(text).doubleValue();
		return stringIntoDouble;
	}

	public String convertDateToString(Date date) {
		return date.toString();
	}

	public List<String> convertDateListIntoStringListOfNewFormat(List<Date> list, String newDateFormat,
			String correctYear) {
		List<String> listOfStringDates = new ArrayList<String>();
		for (Date e : list) {
			String correctDateFormat = convertDateListIntoNewFormat(e, newDateFormat);
			String correctNewDate = replaceYearInStringDate(correctDateFormat, correctYear);

			listOfStringDates.add(correctNewDate);
		}
		return listOfStringDates;
	}

	public String replaceYearInStringDate(String dateWithInvalidYear, String correctYear) {
		String replaceYear = dateWithInvalidYear.replaceAll("\\d{4}", correctYear);
		return replaceYear;
	}

	public String convertDateListIntoNewFormat(Date date, String newDateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(newDateFormat);
		sdf.applyPattern(newDateFormat);
		return sdf.format(date);
	}

	public Date formatDateStringIntoDate(String dateAsString, String datePattern) throws java.text.ParseException {
		// TODO: simple date formatter adds extra info. DOES NOT hurt integrity of test.
		// Just adds trivial info. need to figure out why.
		// DateTimeFormatter.ofPattern(Constants.CALLS_TEXT_LOG_DATE_FORMAT);
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		// dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		dateFormat.applyPattern(datePattern);
		Date date = dateFormat.parse(dateAsString);
		return date;
	}

	public ArrayList<Date> convertStringArrayIntoDates(String[] stringArray, String datePattern) throws ParseException {
		ArrayList<Date> dateArray = new ArrayList<Date>(stringArray.length);
		for (String e : stringArray) {
			Date date = formatDateStringIntoDate(e, datePattern);
			dateArray.add(date);
		}
		return dateArray;
	}

	public String cleanWebElementCost(WebElement cost) {
		return cost.getText().replace("$", "");
	}

	public String makeWebElementTextUpperCase(WebElement e) {
		return e.getText().toUpperCase();
	}

	public void saveFileToLocation(String fileName, String path) throws InterruptedException, AWTException {
		// File FILE = new File(path + "\\" + fileName);
		// action = new Actions(Browser.getDriverInstance());
		// loop:
		// for(int i = 0; i == 10; i++) {
		jsWaitsForPageToLoad();
		initiateSave();
		passTextToApp(fileName);
		tabToFilePathFromFileName();
		passTextToAppWithReturns(path);
		// if (FILE.exists()) {
		// break loop;
		// }
		// action.click();
		// }
	}

	private void passTextToAppWithReturns(String path) throws InterruptedException, AWTException {
		Thread.sleep(3000);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(3000);
		robot.keyPress(KeyEvent.VK_DELETE);
		robot.keyRelease(KeyEvent.VK_DELETE);
		Thread.sleep(3000);
		passTextToApp(path);
		Thread.sleep(500);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(500);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(500);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(500);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(2000);
	}

	private void tabToFilePathFromFileName() throws InterruptedException, AWTException {
		Thread.sleep(4000);
		robot = new Robot();
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(500);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(500);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(500);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(500);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(500);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(2000);
	}

	private void passTextToApp(String fileName) throws InterruptedException, AWTException {
		StringSelection stringSelection = new StringSelection(fileName);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, stringSelection);
		System.out.println("Passing" + fileName);
		Thread.sleep(10000);
		robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		Thread.sleep(250);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	}

	private void initiateSave() throws InterruptedException, AWTException {
		Thread.sleep(20000);
		robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_S);
		Thread.sleep(500);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(500);
	}

	By activeYearLoc = By.xpath("//div[@class ='year-row active']");

	public WebElement activeYear() {
		return driver.findElement(activeYearLoc);
	}

	public String activeYearText() {
		return activeYear().getText();
	}

	public String getEndOFYearText() {
		return org.apache.commons.lang.StringUtils.right(activeYearText(), 2);
	}

	public int getIndexOfStringInList(String targetText, List<String> list) {
		for (String item : list) {
			if (targetText.equals(item.replaceAll("^\"|\"$", ""))) {
				return list.indexOf(item);
			}
		}
		System.out.println(targetText + "NOT FOUND IN LIST");
		return 0;
	}

	public int getIndexOfStringContainedInList(String targetText, List<String> list) {
		for (String item : list) {
			String s = item.replaceAll("^\"|\"$", "");
			if (s.contains(targetText)) {
				return list.indexOf(item);
			}
		}
		System.out.println(targetText + "NOT FOUND IN LIST");
		return 0;
	}

	public Boolean stringContainsCrucialText(String testString, String crucialText) {
		return testString.contains(crucialText);
	}

	public boolean elementIsDisplayed(WebElement e) {
		return e.isDisplayed();
	}

	public boolean elementIsNotDisplayed(WebElement e) {
		return !e.isDisplayed();
	}

	private int getIndexOfOpenRow() {
		List<WebElement> list = expandButtons();
		for (WebElement item : list) {
			if (item.getAttribute("class").contains("collapse")) {
				return list.indexOf(item);
			}
		}
		return 0;
	}

	private List<WebElement> expandButtons() {
		return retryElementsUntilPresent(EXPAND_BUTTONS);
	}

	public int getIndexOfEntryWhoseChildrenEquals(List<WebElement> tables, String arg1) {
		for (int i = 0; i < tables.size(); i++) {
			String s = getText(tables.get(i).findElement(DIV));
			if (s.length() == 0) {
				s = String.join(" ", turnWebElementsToText(tables.get(i).findElements(CHILDREN)));
			}
			if (s.equals(arg1))
				return i;
		}
		return 0;
	}

	/*
	 * Automatically orders text from left to right, regardless of order Selenium
	 * grabs them in
	 */
	public List<List<String>> createListStringFromRows(List<WebElement> rows, By by) {
		List<List<String>> sub = new ArrayList<List<String>>();
		for (WebElement e : rows) {
			List<String> s = turnWebElementsToTextOrderLeftToRight(e.findElements(by));
			if (s.size() != 0) {
				if (sub.size() == 0)
					sub.add(s);
				if (sub.size() > 0 && !sub.get(sub.size() - 1).equals(s))
					sub.add(s);
			}
		}
		return sub;
	}

	/*
	 * public List<List<String>>
	 * createListStringFromRowsOnlyDispalyedElements(List<WebElement> rows, By by) {
	 * List<List<String>> sub = new ArrayList<List<String>>(); for (WebElement e :
	 * rows) { List<String> webEntries =
	 * createListStringFromRowOnlyDispalyedElements(e.findElements(by)); if
	 * (webEntries.size() != 0) sub.add(webEntries); } return sub; }
	 */
	public List<String> createListStringFromRowOnlyDispalyedElements(List<WebElement> row) {
		return row.stream().filter(i -> i.isDisplayed()).map(i -> i.getText()).collect(Collectors.toList());
	}

	public List<List<String>> createListStringFromRowsLeftToRightKeepBlanks(List<WebElement> rows, By by) {
		List<List<String>> sub = new ArrayList<List<String>>();
		for (WebElement e : rows) {
			List<String> s = turnWebElementsToTextOrderLeftToRightKeepBlanks(e.findElements(by));
			if (s.size() != 0)
				sub.add(turnWebElementsToTextOrderLeftToRightKeepBlanks(e.findElements(by)));
		}
		return sub;
	}
/*
	public void clickUntilStringMatches(WebElement e, Predicate<String> p) {
		for (int i = 0; i < 10; i++) {
			resilientClick(e);
			ensureElementNotStale(e);
			if (p.test(e.getText())) {
				break;
			} else {
				sleep(1000);
			}
		}
	}

	public void clickUntilClassMatches(WebElement e, Predicate<String> p) {
		for (int i = 0; i < 10; i++) {
			try {
				if (p.test(getElementAttribute(e, "class"))) {
					break;
				}
				resilientClick(e);
				if (p.test(getElementAttribute(e, "class"))) {
					break;
				} else {
					sleep(1000);
				}
			} catch (Exception e1) {
				sleep(1000);
			}
		}
	}

	public void clickUntilPredicateMatches(WebElement e, Predicate<String> p, String test) {
		for (int i = 0; i < 20; i++) {
			try {
				if (p.test(test)) {
					break;
				}
				resilientClick(e);
				if (p.test(test)) {
					break;
				} else {
					sleep(2000);
				}
			} catch (Exception e1) {
				sleep(2000);
			}
		}
	}
*/
	public void clickElementsMatchingCondition(List<WebElement> e, Predicate<WebElement> p) {
		e.stream().filter(p).forEach(this::resilientClick);
	}

	public void clickAllElements(List<WebElement> e) {
		e.stream().forEach(this::resilientClick);
	}

	public void clickElementsMatchingConditionHandleStale(List<WebElement> e, Predicate<WebElement> p) {
		for (int i = 0; i < 5; i++) {
			try {
				e.stream().filter(p).forEach(this::resilientClick);
				break;
			} catch (Exception e1) {
				sleep(100);
			}
		}
	}
	
	public <T, U> boolean clickAndWait(WebElement e, BiFunction<T, U, Boolean> condition, T param1, U param2) {
		for (int i = 0; i < 2; i++) {
			try {
				resilientClick(e);
				waitUntilCondition(condition, param1, param2, POLLING_MILLIS, TIMEOUT_SECS);
				return true;
			} catch (TimeoutException t) {
				return false;
			} catch (StaleElementReferenceException s) {
				sleep(SLEEP_MILLIS);
			}
		}
		return false;
	}

	public <T> boolean clickAndWait(WebElement e, Function<T, Boolean> condition, T parameter) {
		for (int i = 0; i < 2; i++) {
			try {
				resilientClick(e);
				waitUntilCondition(condition, parameter, POLLING_MILLIS, TIMEOUT_SECS);
				return true;
			} catch (TimeoutException t) {
				return false;
			} catch (StaleElementReferenceException s) {
				sleep(SLEEP_MILLIS);
			}
		}
		return false;
	}

	public boolean clickAndWait(WebElement e, BooleanSupplier condition) {
		for (int i = 0; i < 2; i++) {
			try {
				resilientClick(e);
				waitUntilCondition(condition,  POLLING_MILLIS, TIMEOUT_SECS);
				return true;
			} catch (TimeoutException t) {
				return false;
			} catch (StaleElementReferenceException s) {
				sleep(SLEEP_MILLIS);
			}
		}
		return false;
	}
	
	public boolean clickUntilConditionIsMet(WebElement e, BooleanSupplier condition) {
		for (int i = 0; i < 10; i++) {
			try {
				resilientClick(e);
				waitUntilCondition(condition, POLLING_MILLIS, QUICK_TIMEOUT_SECS);
				return true;
			} catch (TimeoutException t) {
				sleep(QUICK_SLEEP_MILLIS);
			} catch (StaleElementReferenceException s) {
				sleep(QUICK_SLEEP_MILLIS);
			}
		}
		return false;
	}
	
	public <T> boolean clickUntilConditionIsMet(WebElement e, Function<T, Boolean> condition, T parameter) {
		for (int i = 0; i < 10; i++) {
			try {
				resilientClick(e);
				waitUntilCondition(condition, parameter, POLLING_MILLIS, QUICK_TIMEOUT_SECS);
				return true;
			} catch (TimeoutException t) {
				sleep(QUICK_SLEEP_MILLIS);
			} catch (StaleElementReferenceException s) {
				sleep(QUICK_SLEEP_MILLIS);
			} 
		}
		return false;
	}

	public WebElement getDisplayedElement(List<WebElement> e) {
		return e.stream().filter(i -> i.isDisplayed()).findFirst().orElseGet(null);
	}

	public List<String> getClassValuesOfList(List<WebElement> e) {
		return e.stream().map(i -> getElementAttribute(i, "class")).collect(Collectors.toList());
	}

	public WebElement webElementInX_Pos(List<WebElement> e, int x) {
		return e.stream().min(Comparator.comparing(i -> Math.abs(((WebElement) i).getLocation().getX() - x)))
				.orElseGet(null);
	}

}
