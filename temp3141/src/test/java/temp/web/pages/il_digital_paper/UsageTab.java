package com.sprint.iice_tests.web.pages.il_digital_paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.google.common.collect.ImmutableMap;

public class UsageTab extends TabBase implements TabActions {

	private static final By EXPAND_BUTTON_HEADINGS = By.cssSelector("a > span.title");
	private static final By RIGHT_ARROW_ROW_SHIFTING = By.cssSelector(".ico--chevron-right");
	ChargesTab chargesTab;
	HomePage navBarAndViewBill;
	final String POPUP_GRAPH_TITLE = ".trends-popup-header > span:nth-child(%d)";
	final String VIEW_DETAILS_LINKS = "div:nth-child(%d) > .others-col > .bb-row > .bb-col > .historic-usage > .open-call-filters";
	final String MOBILE_GRAPH = "div:nth-child(%d) > .others-col > .bb-row > .bb-col > div > div > .bb-usage-progress-bar > .bb-filled-rect";
	final String TRIANGLE_POINTS_ATTRIBUTE = "points";
	final String GRAPH_ELEMENTS = "[data*='historicUsage'] > svg > g > .bb";
	final String DATA_TABLE_PATH = "div:nth-child(%d) > .others-col > .bb-row > .bb-col:nth-child(%d) > span";
	final String DATA_TABLE_ALLOWED_PATH = "div:nth-child(%d) > .others-col > .bb-row > .bb-col:nth-child(%d) > span > span";
	final String DATA_ROW_PATH = "div:nth-of-type(%d) > .others-col> .bb-row > .bb-col > [ng-bind='data.amount']";
	private final By DATA_HEADERS = By.cssSelector(".bb-row-headers > .bb-col > div:nth-child(1)");

	By everythingDataButtonLoc = By.id("LTD1007");
	By cust = By.cssSelector(".first-col > div > div > div > div > div:nth-child(2) > span");
	By usageTabLoc = By.id("lnkUsageNav");
	By triangleSymbolLoc = By.cssSelector(
			"#PAD0R4 > div.usage-table > div.row.row--spacing-narrow.messages-row > div.col-xs-3.col-sm-1");
	By plusSignForFreeAndClearAddAPhoneLoc = By.cssSelector("#PAD0R4 > div > div.col-xs-3.col-sm-1 > span > i");
	By poolingMsgLoc = By
			.cssSelector("#PAD0R4 > div.usage-table > div.row.row--spacing-narrow.messages-row > div.col-xs-20 > span");
	By bottomGridLoc = By.cssSelector("#PAD0R4 > div.usage-table > div.row.row--spacing-narrow.messages-row");
	By usageTableLoc = By.cssSelector(".usage-table");
	By getDesiredPTNLoc = By.cssSelector(".first-col");
	By poolingMsgAndSymbolLoc = By.cssSelector("#PAD0R4 > div.usage-table > div.row.row--spacing-narrow.messages-row");
	By triangleCoordLoc = By.cssSelector("polygon[points='20.58 19.704 1.314 19.704 10.947 0.438 20.58 19.704']");
	By triangleHost = By.cssSelector(
			"#PAD0R4 > div.usage-table > div.row.row--spacing-narrow.messages-row > div.col-xs-3.col-sm-1");
	final By SUBSCRIBER_DESCR_BLOCK = By.cssSelector(".subscriber-desc-block > div > span");

	// By usageTableHeaderLoc = By.cssSelector("#PAD0R4 > div.row.middle-xs >
	// div.col-xs-21.col-sm-23 > a > span");
	By usageTableHeadersLoc = By.cssSelector(".bb-col");
	private final By EXPAND_BUTTONS = By.cssSelector("span > .ico");
//	private ImmutableMap<String, Integer> VIEW_TRENDS_HEADERS = ImmutableMap.of("DATA", 0, "DATA ROAMING", 1, "MOBILE HOTSPOT", 2);
	private ImmutableMap<String, Integer> VIEW_TRENDS_HEADERS = ImmutableMap.of("MOBILE HOTSPOT", 0);
	By usageHeaderList = By.xpath(".//div[contains(@class, 'bb-col')]");
	By usageRowFromSpecificKeyLoc = By.xpath("./../../../../../../following-sibling::div[@class = 'others-col']");

	public WebElement poolingMsgAndSymbol() {
		return wait.until(ExpectedConditions.visibilityOf(driver.findElement(poolingMsgAndSymbolLoc)));
	}

	private By graph_properties(String tail) {
		return By.cssSelector(formatPath(GRAPH_ELEMENTS, null, tail));
	}

	/*
	 * Series of methods for finding data inside the table based on phone number and
	 * column heading
	 */
	private By data_table_phone_and_column(Object[] args) {
		return By.cssSelector(formatPath(DATA_TABLE_PATH, args));
	}
	
	private By data_table_allowed_phone_and_column(Object[] args) {
		return By.cssSelector(formatPath(DATA_TABLE_ALLOWED_PATH, args));
	}

	private By trend_info_popup_links(int row) {
		Object[] args = { row + 1 };
		return By.cssSelector(formatPath(VIEW_DETAILS_LINKS, args));
	}

	private By mobile_graph(int row) {
		Object[] args = { row + 2 };
		return By.cssSelector(formatPath(MOBILE_GRAPH, args));
	}

	public List<WebElement> subscriberDescriptions() {
		return retryElementsUntilPresent(SUBSCRIBER_DESCR_BLOCK);

	}

	public List<WebElement> subscriberNames() {
		List<WebElement> subscriberName = new ArrayList<WebElement>();
		for (int i = 0; i < subscriberDescriptions().size(); i += 2) {
			subscriberName.add(subscriberDescriptions().get(i));
		}
		return subscriberName;
	}

	public List<WebElement> subscriberPhoneNumber() {
		List<WebElement> subscriberPhoneNumber = new ArrayList<WebElement>();

		int size = subscriberDescriptions().size();
		for (int i = 1; i < size; i += 2) {

			subscriberPhoneNumber.add(subscriberDescriptions().get(i));
		}
		return subscriberPhoneNumber;
	}

	public WebElement triangleSymbol() {
		return driver.findElement(triangleSymbolLoc);
	}

	public WebElement triangleSymbolSvgUse() {
		return triangleSymbol().findElement(By.cssSelector("svg > use"));
	}

	public WebElement plusSignForFreeAndClearAddAPhone() {
		return wait.until(ExpectedConditions.visibilityOf(driver.findElement(plusSignForFreeAndClearAddAPhoneLoc)));
	}

	public WebElement plusSignForeverythingData() {
		return wait.until(ExpectedConditions.visibilityOf(driver.findElement(everythingDataButtonLoc)));
	}

	public WebElement poolingMsg() {
		return wait.until(ExpectedConditions.visibilityOf(driver.findElement(poolingMsgLoc)));
	}

	public WebElement bottomGrid() {
		return wait.until(ExpectedConditions.visibilityOf(driver.findElement(bottomGridLoc)));
	}

	public WebElement usageTable() {
		return wait.until(ExpectedConditions.visibilityOf(driver.findElement(usageTableLoc)));
	}

	public List<WebElement> getExpandButtons() {
		return retryElementsUntilPresent(EXPAND_BUTTONS);
	}
	
	public List<WebElement> usageTables() {
		return retryElementsUntilPresent(By.xpath("//div[@class='usage-table']//div[@class='bb-row']//div[@class='bb-col']"));
	}

	public List<List<String>> getRowsLeftToRighOfTableWithUsage() {
		List<WebElement> rows = usageTables();
		return createListStringFromRowsLeftToRightKeepBlanks(rows, SPAN);
	}

	/**
	 * Returns a list of equipment graphs from under the Equipmnent and Plans expand
	 * button number of graphs is variable, pass an int (zero-based) to get a
	 * particular graph 0 will always return something
	 */
	public List<WebElement> getTrendInfoPopupLinks(int row) {
		return retryElementsUntilPresent(trend_info_popup_links(row));
	}

	public WebElement getTrendInfoPopupLinksFromColumn(String columnHeading, Integer row) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("DATA", 0);
		map.put("DATA ROAMING", 1);
		map.put("MOBILE HOTSPOT", 2);
		return getTrendInfoPopupLinks(row).get(map.get(columnHeading));
	}

	/**
	 * Return the title text of the graph popups under view trend location varies
	 * between the mobile link and the other two; thus the boolean checks to see if
	 * the return is for the mobile check
	 */

	private By trend_info_popup_headings(int spanIndex) {
		return By.cssSelector(String.format(POPUP_GRAPH_TITLE, spanIndex));
	}

	public List<WebElement> trendInfoPopupHeadings(int spanIndex) throws InterruptedException {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(trend_info_popup_headings(spanIndex)));
	}

	public Boolean doesTrendInfoPopupHeadingEqual(int row, String targetHeading) throws InterruptedException {
		return doesTrendInfoPopupHeadingEqual(row, targetHeading, false);
	}

	public Boolean doesTrendInfoPopupHeadingEqual(int row, String targetHeading, Boolean isMobile)
			throws InterruptedException {
		if (isMobile) {
			String headingText = trendInfoPopupHeadings(3).get(row - 1).getText();
			return headingText.contains(targetHeading);
		}
		String headingText = trendInfoPopupHeadings(4).get(row - 1).getText();
		return headingText.contains(targetHeading);
	}

	/**
	 * Gets a List of WebElements representing each bill period in the graphs pop-up
	 */
	public List<WebElement> getChargeGraphs() {
		return retryElementsUntilPresent(graph_properties("-filled-rect"));
	}

	/**
	 * Gets a List of WebElements representing the dates in the graphs pop-up
	 */
	public List<WebElement> getGraphDate() {
		return retryElementsUntilPresent(graph_properties("-graph-date"));
	}

	/**
	 * Gets a List of WebElements representing the dollar amounts for each period
	 * period in the graphs pop-up
	 */
	public List<WebElement> getGraphAmount() {
		return retryElementsUntilPresent(graph_properties("-graph-amount > .amount"));
	}

	/**
	 * Returns a list of equipment graphs from under the Equipmnent and Plans expand
	 * button number of graphs is variable, pass an int to get a particular graph 0
	 * will always return something
	 */
	public WebElement getMobileGraph(int order) {
		return retryElementUntilPresent(mobile_graph(order));
	}
	
	public Boolean triangleSymbolIsDisplayed() {
		// looks for triangle symbol and puts results in a boolean variable
		return triangleSymbol().isDisplayed();
	}

	/**
	 * freeAndClearAddAPhonePoolingMsg() clicks on the + sign to expand the Free &
	 * Clear Add a Phone contents which should display a message at the bottom of
	 * the page.
	 * 
	 * @return the text of the pooling message displayed at the bottom of the Usage
	 *         grid
	 */

	public String freeAndClearAddAPhonePoolingMsg() {
		// gets the text of a pooling message within expanded content of Free & Clear
		// Add a Phone
		String poolMsg = poolingMsg().getText();

		// returns text for the Pooling Message "Free & Clear Add a Phone"
		return poolMsg;
	}

	public String getTriangleXlinkHrefId() {
		return getElementAttribute(triangleSymbolSvgUse(), "xlink:href");
	}

	By sliderButtonLoc(String leftORRight) {
		return By.xpath("//div[contains(@class, 'nav " + leftORRight + "-navigation')]");
	}

	public WebElement sliderButton(String leftORRight) {
		return driver.findElement(sliderButtonLoc(leftORRight));
	}

	public void clickSliderButton(String leftORRight) throws Exception {
		scrollToAndClickElement(sliderButton(leftORRight));
	}

	public void clickSliderButtonACertainNumberOfTimes(String leftORRight, int totalNumOfClicks) throws Exception {
		for (int click = 0; click < totalNumOfClicks; click++) {
			clickSliderButton(leftORRight);
		}
	}

	@Override
	public void openExpandButtons(String headingText) throws InterruptedException {
		List<WebElement> headings = expandButtonHeadings();
		int index = getIndexOfElementWithTextFromList(headingText, headings);
		cleanExpandButtons(getExpandButtons(), index);
	}

	private List<WebElement> expandButtonHeadings() {
		return retryElementsUntilPresent(EXPAND_BUTTON_HEADINGS);
	}

	public WebElement getShiftDataTable() {
		try {
			return retryElementsUntilPresent(RIGHT_ARROW_ROW_SHIFTING).get(1);
		}catch(Exception e) {
			return retryElementsUntilPresent(RIGHT_ARROW_ROW_SHIFTING).get(0);
		}
	}

	public List<String> dataRow(int i) {
		List<WebElement> rowElements = retryElementsUntilPresent(data_row(i));
		List<String> string = new ArrayList<String>();
		for (WebElement element : rowElements) {
			string.add(element.getText());
		}
		return string;
	}

	private By data_row(int i) {
		String data = String.format(DATA_ROW_PATH, i + 1);
		return By.cssSelector(data);
	}

	public String getInternationalTextListFromRow(int i) {
		int j = dataRow(i).size() - 1;
		return dataRow(i).get(j);

	}

	public WebElement getViewTrendsLinkByNumberAndColumn(String phoneNum, String columnHeading) {
		int row = getIndexOfElementWithTextFromList(phoneNum, subscriberPhoneNumber()) + 1;
		return getTrendInfoPopupLinks(row).get(VIEW_TRENDS_HEADERS.get(columnHeading));
	}

	public String valueFromPhoneNumberAndColumnHeading(String phoneNum, String column) {
		int row = getIndexOfElementWithTextFromList(phoneNum, subscriberPhoneNumber()) + 2;
		int col_index = getIndexOfElementWithTextFromList(column, getColumnHeadings()) + 1;
		Object[] args = {row, col_index};
		return retryElementUntilPresent(data_table_phone_and_column(args)).getText();
	}
	
	public String valueAllowedFromPhoneNumberAndColumnHeading(String phoneNum, String column) {
		int row = getIndexOfElementWithTextFromList(phoneNum, subscriberPhoneNumber()) + 2;
		int col_index = getIndexOfElementWithTextFromList(column, getColumnHeadings()) + 1;
		Object[] args = {row, col_index};
		return retryElementUntilPresent(data_table_allowed_phone_and_column(args)).getText();
	}

	private List<WebElement> getColumnHeadings() {
		return retryElementsUntilPresent(DATA_HEADERS);
	}

	public void goToColumn(String column) throws Exception {
		for(int i = 0; i < 15; i++) {
			resilientClick(getShiftDataTable());
			int col_index = getIndexOfElementWithTextFromList(column, getColumnHeadings());
			if(col_index != 0) {
				return;
			}
		}
	}

	public WebElement getMobileGraphUnderPhoneNumber(String phoneNum) {
		int row = getIndexOfElementWithTextFromList(phoneNum, subscriberPhoneNumber());
		return getMobileGraph(row);
	}
	
	/**
	 * USAGE DATA TABLE CONTENT
	 */
	
	/**
	 * @param firstColKey = user specified ptn. think of this as the x coordinate
	 * @param desiredHeader = think of this as the y coordinate
	 * @return the value given the ptn and header
	 */
	
	public String usageDataTable(String firstColKey, String desiredHeader) {
		Map<String, String> table = createTableOfStringsFromWebElements(usageHeaderListForDataTable(),
				usageRowCellsFromSpecificKey(firstColKey));
		System.out.println("THE USAGE TABLE IS: " + table);
		String chargeAmount = table.get(desiredHeader);
		return chargeAmount;
	}
	public WebElement usageDataTableOfWebElements(String firstColKey, String desiredHeader) {
		Map<String, WebElement> table = fillTableWithWebElements(makeListElementsStrings(usageHeaderListForDataTable()),usageRowCellsFromSpecificKey(firstColKey));
		System.out.println("THE USAGE TABLE IS: " + table);
		WebElement chargeAmount = table.get(desiredHeader);
		return chargeAmount;
	}
	
	public WebElement progressBarFromUsageCell(String firstColKey, String desiredHeader) {
		WebElement cell =usageDataTableOfWebElements(firstColKey, desiredHeader);
		WebElement progressBar = tryWebElement(cell, usageProgressBar);
		return progressBar;
		
	}
	public WebElement usageRowFromSpecificKey(String firstColKey) {
		return specificKey(firstColKey).findElement(usageRowFromSpecificKeyLoc);
	}
	
	public WebElement specificHeaderListForDataTable(String headerName) {
		return headerSingleElement().findElement(specificHeaderLoc(headerName));
	}
	
	
	
	public List<WebElement> usageHeaderListForDataTable() {
		return headerSingleElement().findElements(usageHeaderList);
	}
	
	By usageProgressBar = By.xpath(".//div[@value='data.amount']/*");

	
	public List<WebElement> usageRowCellsFromSpecificKey(String firstColKey) {
		return usageRowFromSpecificKey(firstColKey).findElements(appendedRowCell);
	}
}