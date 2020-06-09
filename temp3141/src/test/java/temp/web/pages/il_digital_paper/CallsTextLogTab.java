package com.sprint.iice_tests.web.pages.il_digital_paper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sprint.iice_tests.lib.dao.data.Colors;
import com.sprint.iice_tests.lib.dao.data.Constants;
import com.sprint.iice_tests.web.browser.Browser;

/**
 * Page object for elements visible when the Calls/Text Log tab is selected
 * 
 * @Filter - elements are only clickable if the filter pop-up menu, invoked by
 *         clicking the filter links block
 */
public class CallsTextLogTab extends TabBase implements TabActions {

	private static final By COLUMN_HEADINGS = By.cssSelector(".bb-row-headers span, .bb-row-headers b");
	private static final By BUTTON_NUMBER = By.cssSelector(".calls-collection > .light:nth-child(1)");
	private static final String PATH_TO_TABLE_VALUE = ".table-content > %s > .bb-row .bb-col:nth-child(%d) > span";
	private static final By LIST_MESSAGES = By.cssSelector(".call-message > span");
	private static final String RADIO_BUTTON_TEXT = "#%s + label";
	private static final String BUTTON_WITH_NUMBER = "//span[contains(text(),\"%s\")]/parent::*/parent::*/div/span/i";
	private static final By ROAMING_MESSAGES = By.cssSelector(".load-more-text > span");
	private static final String COLUMN_WITH_HEADING = "//*[contains(@class,'bb-col')]//*[contains(text(),'%s')]";
	private static final String DOWN_ARROW_NEXT_TO_COLUMN = "//*[contains(text(),'ON')]/following-sibling::i[contains(@class,'down')]";
	private final String className = Browser.classMap.get(threadId).replaceAll(".json", "");
	private static final String path = System.getProperty("user.dir") + "\\" + Constants.TEST_DATA_PATH + "\\";
	// By's for methods
	private final By HEADER = By.cssSelector(".calls > .row.title-row > div > span.light");
	private final By PRINT = By.cssSelector(".print-page-link");
	private final By PDF = By.cssSelector(".view-pdf-block");
	private final By RADIO = By.cssSelector("[type='radio'] > .bb-input-btn");
	private final By FILTER = By.cssSelector(".filter-links-block");
	private final By CHECKBOX = By.cssSelector("input[type='checkbox']");
	private final By UPDATE_FILTER = By.cssSelector(".update-filter-button");
	private final By FILTER_EXPAND_BUTTONS = By.cssSelector(".popup-content > div > div > i");
	private final By PHONE_INPUT_FIELD = By.cssSelector(".sprint-input > input");
	private final By PHONE_FILTER = By.cssSelector(".add-label.open-call-filters");
	private final By EXPAND_BUTTONS = By.cssSelector(".row.middle-xs > div > span > .ico");
	private final By SUBSCRIBERS = By.cssSelector(".subscriber-name");
	private final By PHONE_NUMBERS = By.cssSelector(".call-info.call-ptn > span");
	private final By DOWNLOAD_PDF = By.cssSelector(".row.middle-xs > .download-pdf-block");
	private final By FILTER_CLOSE = By.cssSelector(".filter-close");
	private final By TOTAL_MIN = By.cssSelector(".total-min > span:nth-of-type(2)");
	private final By TOTAL_COST = By.cssSelector(".total-cost> span:nth-of-type(2)");
	private final By MORE_BUTTON = By.cssSelector(".load-more-btn");
	private BufferedReader br;
	public Function<String, Boolean> desiredRadioButtonIsSelected = x -> getElementAttribute(inputRadioButtonWithID(x),
			"aria-checked").equals("true");

	private By DAYS(int cal) {
		final String SELECTOR = ".calendar:nth-of-type(%d) > .day-cell > span";
		return By.cssSelector(String.format(SELECTOR, cal));
	}

	private WebElement inputRadioButtonWithID(String x) {
		return retryElementUntilPresent(input_radio_button_with_id(x));
	}

	private By input_radio_button_with_id(String x) {
		return By.id(x.replaceAll(" ", "") + "Id");
	}

	private By TABLE_ROW(int row) {
		final String SELECTOR = ".table-content:nth-of-type(%d) > .first-col > div > div > span";
		return By.cssSelector(String.format(SELECTOR, row));
	}

	private By TABLE_COL(int row, int col) {
		final String SELECTOR = ".table-content:nth-of-type(%d) > .others-col > div > div:nth-of-type(%d) > span";
		return By.cssSelector(String.format(SELECTOR, row, col));
	}

	By voiceButtonLoc = By.id("StandardVoiceCalls");
	By roamingButtonLoc = By.id("RoamingVoiceCalls");
	By internationalButtonLoc = By.id("IntDirectConnect");
	By textButtonLoc = By.id("Text");
	By filterButtonLoc = By.xpath("//i[@class = 'filter-img ico ico--close-circle ng-hide']/../*/child::*");

	By callsTextPlusSignButtonsLoc(String plusSignName) {
		return By.xpath("//span[text()='" + plusSignName + "']/../..//i[contains(@class, 'ico ico--expand')]");
	}

	By buttonColorLoc = By.xpath("./following-sibling::label/span/span");
	By totalMinLoc = By.xpath("//div[@class= 'total-min']");
	By totalCostLoc = By.xpath("//div[@class= 'total-cost']");
	By callMsgLoc = By.xpath("//div[@class = 'call-message']");
	By emailLoc = By.xpath("//div[@class = 'calls-collection']");
	By loadMoreTextInfoLoc = By.xpath("//div[@class = 'load-more-text'][1]");
	By showMoreTextInfoLoc = By.xpath("//div[@class = 'load-more-text'][2]");
	private By loadMoreButtonLoc = By.xpath("//a[@class = 'load-more-btn']");
	By inputTextBoxLoc = By.xpath("//div[@class= 'sprint-input']//input");
	By labelTextBoxLoc = By.xpath("//div[@class= 'sprint-input']//label");
	By updateFilterButtonLoc = By.xpath("//a[text()='Update filter']");

	By sortedHeaderTextLoc(String headerName) {
		return By.xpath(
				"//div[@class='bb-row bb-row-headers']//div[@class='bb-col']//b[contains(@class,'sorted') and text()='"
						+ headerName + "']");
	}

	By allTheValsFromCallTextLogLoc(int positionOfHeaderText) {
		return By.xpath(
				"//div[contains(@class, 'table-content')]//div[@class= 'others-col']//div[@class = 'bb-row']/div[@class='bb-col']["
						+ positionOfHeaderText + "]");
	}

	By generalIconLoc = By.xpath(".//i[contains(@class , 'ico ico--arrow')]");
	By callsTextLogsRowFromSpecificKeyLoc = By.xpath("./../../../following-sibling::div");

	By iconLoc(String attributeColorProp) {
		return By.xpath(".//i[@class = '" + attributeColorProp + "']");
	}

	By iconLocByName(String arrowName) {
		return By.xpath("//b[text()='" + arrowName + "']/..//i");
	}

	By desiredCalendarMonthLoc(String monthAndYearText) {
		return By.xpath("//span[text()= '" + monthAndYearText + "']/../..");
	}

	By desiredDateLoc(String date) {
		return By.xpath(".//span[text()= '" + date + "']/..");
	}

	By callsTextLogHeaderListLoc = By.xpath(".//div[contains(@class, 'others-col')]");
	By ptnFilterButtonLoc = By.xpath("//i[@class= 'ico bb-svg-icon-close ico--close filter-close']");
	By ptnFilterButtonTextLoc = By.xpath("./../span");

	public List<String> allTheDatesFromCallTextLog() {
		return makeListElementsStrings(listOfKeys());
	}

	public WebElement callsTextPlusSignButtons(String plusSignName) {
		return retryElementUntilPresent(callsTextPlusSignButtonsLoc(plusSignName));
	}

	public WebElement voiceButton() {
		return retryElementUntilPresent(voiceButtonLoc);
	}

	public WebElement roamingButton() {
		return retryElementUntilPresent(roamingButtonLoc);
	}

	public WebElement textbutton() {
		return retryElementUntilPresent(textButtonLoc);
	}

	public WebElement internationalDirectConnectButton() {
		return retryElementUntilPresent(internationalButtonLoc);
	}

	public WebElement filterButton() {
		return retryElementUntilPresent(filterButtonLoc);
	}

	public String getColorOfButton(WebElement button) {
		WebElement buttonColorElement = button.findElement(buttonColorLoc);
		return buttonColorElement.getCssValue(Constants.BUTTON_COLOR_CSS_VALUE);

	}

	public WebElement totalMin() {
		return retryElementUntilPresent(totalMinLoc);
	}

	public WebElement totalCost() {
		return retryElementUntilPresent(totalCostLoc);
	}

	// example: returns "TOTAL COST: $3.80"
	public String totalCostValue() {
		System.out.println("the total cost from website: " + totalCost().getText());
		return totalCost().getText();
	}

	public String actualTotalCostValue() {
		return totalCostValue().replace("TOTAL COST: $", "");
	}

	public double totalCostValueAsDouble() {
		System.out
				.println("the total cost from website rounded as double: " + makeStringADouble(actualTotalCostValue()));
		return makeStringADouble(actualTotalCostValue());
	}

	public String totalMinValue() {
		return totalMin().getText();
	}

	public String actualTotalMinValue() {
		return addHoursToTimeIfTimeDoesntHaveHours(totalMinValue().replace("TOTAL MINS: ", ""));
	}

	public LocalTime totalMinValueAsLocalTime() {
		return makeStringALocalTime(actualTotalMinValue());
	}

	public static String addHoursToTimeIfTimeDoesntHaveHours(String time) {
		if (time.contains(":") && time.indexOf(":") == time.lastIndexOf(":")) {
			String addHours = "00:" + time;
			return addHours;
		}
		return time;
	}

	public WebElement callMsg() {
		return retryElementUntilPresent(callMsgLoc);
	}

	public String callMsgText() {
		return callMsg().getText();
	}

	public WebElement email() {
		return retryElementUntilPresent(emailLoc);
	}

	public String emailText() {
		return email().getText();
	}

	public WebElement loadMoreTextInfo() {
		return retryElementUntilPresent(loadMoreTextInfoLoc);
	}

	public String loadMoreTextInfoText() {
		return loadMoreTextInfo().getText();
	}

	public WebElement showMoreTextInfo() {
		return retryElementUntilPresent(showMoreTextInfoLoc);
	}

	public WebElement loadMoreButtonLoc() {
		return retryElementUntilPresent(loadMoreButtonLoc);
	}

	/**
	 * Main Header, text is "Calls/Texts Logs"
	 */
	public WebElement header() {
		return retryElementUntilVisible(HEADER);
	}

	public WebElement printPageLink() {
		return retryElementUntilVisible(PRINT);
	}

	public WebElement viewPDFLink() {
		return retryElementUntilVisible(PDF);
	}

	/**
	 * List of the radio buttons to choose either "Voice" (choose by .get(0) or
	 * "Text" choose by .get(1)) .get(0) is for Voice .get(1) is for Text
	 */
	public List<WebElement> radioButtons() {
		return retryAllElementsUntilVisible(RADIO);
	}

	/**
	 * Clicking this element causes the filter pop-up area to appear. Clicking again
	 * makes the pop-up disappear
	 */
	public WebElement filterLinksBlock() {
		return retryElementUntilVisible(FILTER);
	}

	/**
	 * @Filter
	 */
	public WebElement chargeableCheckbox() {
		return retryElementUntilPresent(CHECKBOX);
	}

	/**
	 * There are two identical filter buttons, choose between them with .get(0) or
	 * .get(1)
	 * 
	 * @Filter
	 */
	public List<WebElement> updateFilterButtons() {
		return retryAllElementsUntilVisible(UPDATE_FILTER);
	}

	/**
	 * There are two expand buttons in the filter pop-up, get(0) is for Phone Number
	 * get(1) is for selecting by date
	 * 
	 * @Filter
	 */
	public List<WebElement> filterExpandButtons() {
		return retryAllElementsUntilVisible(FILTER_EXPAND_BUTTONS);
	}

	/**
	 * Input field for filtering by a given phone number
	 * 
	 * @Filter @PhoneNumber
	 */
	public WebElement phoneNumberInputField() {
		return retryElementUntilVisible(PHONE_INPUT_FIELD);
	}

	/**
	 * Button to add inputed phone number to filter
	 * 
	 * @Filter @PhoneNumber
	 */
	public WebElement addPhoneFilter() {
		return retryElementUntilVisible(PHONE_FILTER);
	}

	/**
	 * Returns a list of expand buttons for each phone number. An individual number
	 * can be selected based on the ordinal position using a List's .get(int)
	 * method, and can be selected based on phonenumber or subscriber name using
	 * this object's phonePosition or subscriber position ex.)
	 * getExpandButtons().get(phonePosition("(169) 736-6983"))
	 */
	public List<WebElement> expandButtons() {
		return retryAllElementsUntilVisible(EXPAND_BUTTONS);
	}

	/**
	 * Webelements indicating filters; clicking on them removes the filter
	 */
	public List<WebElement> closeFilter() {
		return retryAllElementsUntilVisible(FILTER_CLOSE);
	}

	public List<WebElement> subscribers() {
		return retryAllElementsUntilVisible(SUBSCRIBERS);
	}

	/**
	 * Returns a list of expand buttons for each phone number. An individual number
	 * can be selected based on the ordinal position using a List's .get(int)
	 * method, and can be selected based on phonenumber using this object's
	 * phonePosition ex.) getExpandButtons().get(phonePosition("(169) 736-6983"))
	 */
	public List<WebElement> phoneNumbers() {
		return retryAllElementsUntilVisible(PHONE_NUMBERS);
	}

	public List<WebElement> dayFilters(int cal) {
		return retryAllElementsUntilVisible(DAYS(cal));
	}

	/**
	 * Gets a day to click on by finding the matching day with the text. There are
	 * days that are not clickable, errors might result from this.
	 * 
	 * @Filter @PhoneNumber
	 */
	public WebElement getDayFilter(int cal, int day_num) {
		List<WebElement> days = dayFilters(cal);
		for (WebElement day : days) {
			if (day.getText().equals(String.valueOf(day_num))) {
				return day;
			}
		}
		return days.get(0);
	}

	/**
	 * Returns a list of expand buttons for each phone number. An individual number
	 * can be selected based on the ordinal position using a List's .get(int)
	 * method, and can be selected based on phonenumber using this object's
	 * phonePosition ex.) getExpandButtons().get(phonePosition("(169) 736-6983"))
	 */
	public int getPhonePosition(String phoneNumber) {
		List<WebElement> entries = phoneNumbers();
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).getText().equals(phoneNumber)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Returns a list of expand buttons for each phone number. An individual number
	 * can be selected based on the ordinal position using a List's .get(int)
	 * method, and can be selected based on phonenumber using this object's
	 * phonePosition ex.) getExpandButtons().get(phonePosition("(169) 736-6983"))
	 */
	public int getSubscriberPosiition(String subscribers) {
		List<WebElement> entries = subscribers();
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).getText().replaceAll(" ", "").equals(subscribers)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Button to add inputed phone number to filter
	 * 
	 * @throws InterruptedException
	 * 
	 * @Filter @PhoneNumber
	 */
	public WebElement getDownloadPDF(int order) throws InterruptedException {
		cleanExpandButtons(expandButtons(), order);
		return retryElementUntilVisible(DOWNLOAD_PDF);
	}

	/**
	 * WebElement with the total minutes for a phone number button chooses the
	 * expand button the user wants selected
	 * 
	 * @throws InterruptedException
	 */
	public String getTotalMinutes() throws InterruptedException {
		return retryElementUntilVisible(TOTAL_MIN).getText();
	}

	/**
	 * WebElement with the total minutes for a phone number button chooses the
	 * expand button the user wants selected
	 * 
	 * @throws InterruptedException
	 */
	public String getTotalCosts() throws InterruptedException {
		return retryElementUntilVisible(TOTAL_COST).getText();
	}

	/**
	 * WebElement to load more entries button chooses the expand button the user
	 * wants selected
	 * 
	 * @throws InterruptedException
	 */
	public WebElement getLoadMoreButton(int button) throws InterruptedException {
		cleanExpandButtons(expandButtons(), button);
		return retryElementUntilVisible(MORE_BUTTON);
	}

	/**
	 * Webelement with table information, can be used to construct entire table.
	 * First int is for the information's row, the second is the column. With six
	 * columns (zero based) vary the column between 0 to 5 to reconstruct row, etc.
	 * 
	 * @throws InterruptedException
	 */
	public WebElement getTableData(int table, int row, int col) throws InterruptedException {
		cleanExpandButtons(expandButtons(), table);
		if (col == 0) {
			return retryElementUntilVisible(TABLE_ROW(row));
		} else {
			return retryElementUntilVisible(TABLE_COL(row, col));
		}
	}

	/*
	 * public String getTypeText() { int column =
	 * getIndexOfElementContainingTextFromList(TYPE_HEADING , getColumnTitles());
	 * return getTableData(0, 1, column).getText(); }
	 */

	@Override
	public void openExpandButtons(String expandText) throws InterruptedException {
		// TODO Auto-generated method stub

	}

	public String getButtonNumber() {
		return retryElementUntilPresent(BUTTON_NUMBER).getText();
	}

	public List<String> getColumnTitles() {
		return turnWebElementsToText(columnHeadings());
	}

	private List<WebElement> columnHeadings() {
		return retryElementsUntilPresent(COLUMN_HEADINGS);
	}

	public List<Long> listOfCallsTxtPtnsAsInts(String headerName) {
		System.out.println(convertListOfStringPtnIntoLongs(columnOfSpecificHeaderInCallsTxtLog(headerName)));
		return convertListOfStringPtnIntoLongs(columnOfSpecificHeaderInCallsTxtLog(headerName));
	}

	/**
	 * This routine converts the "ON" column into Dates so that we can use the Date
	 * library to see whether the column is ascending or descending.
	 * 
	 * @return the "ON" column in the Date format "MMM dd hh:mm a"
	 * @throws java.text.ParseException
	 */

	public List<java.util.Date> allDatesFromCallsTextLogAsDates() throws java.text.ParseException {
		return allDatesFromCallsTextLogAsDatesInSpecifiedFormat(Constants.CALLS_TEXT_LOG_DATE_FORMAT);
	}

	public List<java.util.Date> allDatesFromCallsTextLogAsDatesInSpecifiedFormat(String format)
			throws java.text.ParseException {
		return convertStringsIntoDates(allTheDatesFromCallTextLog(), format);
	}

	/**
	 * This routine converts the Calls Text Log "ON" column into a List of Strings
	 * of specified Date format with a parameterized year. This creates a control,
	 * which will test against a csv column of "ON" date values (of String type).
	 * The Calls Text Log Column does not include the year, but the year is present
	 * at another section on the page (the drop down billing period). Within the
	 * test the tester will need to pass in the year from WebSite content.
	 * 
	 * @param newDateFormat
	 *            = see Constant class for Date formats
	 * @param correctYear
	 *            = year that needs to be invoked into each String within the "ON"
	 *            column
	 * @return
	 * @throws ParseException
	 */

	public List<String> columnOfCallsTextLogDatesAsStringsWithSpecifiedFormat(String newDateFormat, String correctYear)
			throws ParseException {
		return convertDateListIntoStringListOfNewFormat(allDatesFromCallsTextLogAsDates(), newDateFormat, correctYear);
	}

	/**
	 * This routine grabs a user specified Calls Text Log column of Strings by its
	 * header name
	 * 
	 * @param headerName
	 *            = the header of a Calls Text Log column
	 * @return = a column within the Calls Text Log data table as a List of Strings
	 */

	public CopyOnWriteArrayList<String> columnOfSpecificHeaderInCallsTxtLog(String headerName) {
		CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>();
		list.addAll(makeListElementsStrings(webElColumnOfSpecificHeaderInCallsTxtLog(headerName)));
		return list;
	}

	public String getSpecificRowOfColumn(String headerName, int index) {
		CopyOnWriteArrayList<String> list = columnOfSpecificHeaderInCallsTxtLog(headerName);
		// System.out.println("row " + row + " from the " + headerName + " column has a
		// value of:_"
		// + IntStream.range(0, list.size()).filter(i -> list.get(i).length()
		// >0).mapToObj(i -> list.get(i))
		// .collect(Collectors.toList()).get(row));
		return convertListToIntStream(list).get(index);
	}

	public List<String> convertListToIntStream(List<String> desiredList) {
		return IntStream.range(0, desiredList.size()).filter(i -> desiredList.get(i).length() > 0)
				.mapToObj(i -> desiredList.get(i)).collect(Collectors.toList());
	}

	public List<String> convert2DMatrixToIntStream(List<List<String>> list, int indexOfDesiredListWithinMatrix) {
		return IntStream.range(0, list.size()).filter(i -> list.get(i).size() > 0).mapToObj(i -> list.get(i))
				.collect(Collectors.toList()).get(indexOfDesiredListWithinMatrix);
	}

	public String getElementWithin2DMatrix(List<List<String>> list, int indexOfDesiredListWithinMatrix,
			int indexOfDesiredElementInList) {
		List<String> desiredList = convert2DMatrixToIntStream(list, indexOfDesiredListWithinMatrix);
		// consumer.accept(convertListToIntStream(desiredList).get(indexOfDesiredElementInList));
		// predicateIndex(convertListToIntStream(desiredList),indexOfDesiredElementInList);
		return convertListToIntStream(desiredList).get(indexOfDesiredElementInList);
	}

	public static Predicate<String> predicateIndex(List<String> list, int row) {
		return (x -> list.indexOf(x) == (row));
	}

	public static Consumer<String> consumer = (x -> System.out.println(x));

	/**
	 * This routine grabs a user specified Calls Text Log column of WebElements by
	 * its header name
	 * 
	 * @param headerName
	 *            = the header of a Calls Text Log column
	 * @return = a column within the Calls Text Log data table as a List of
	 *         WebElements
	 */

	public List<WebElement> webElColumnOfSpecificHeaderInCallsTxtLog(String headerName) {
		int indexOfDesiredHeader = (getIndexOfSpecificHeaderForCallsText(headerName));
		return driver.findElements(allTheValsFromCallTextLogLoc(indexOfDesiredHeader));
	}

	/**
	 * This routine converts the "MINS" column into dates so that we can use the
	 * Date library to see whether the column is ascending or descending.
	 * 
	 * @return = a list of Dates with format "hh:mm"
	 * @throws java.text.ParseException
	 */

	public List<java.util.Date> allMinsFromCallsTextLogAsDates() throws java.text.ParseException {
		return convertStringsIntoDates(columnOfSpecificHeaderInCallsTxtLog("MINS"),
				Constants.CALLS_TEXT_LOG_MINS_FORMAT);
	}

	/**
	 * This routine converts the Calls Text Log "COST" column of Strings into
	 * doubles. We need to do this so that we can determine whether the COST column
	 * is increasing or decreasing
	 * 
	 * @return ="COST" column of Double type
	 */

	public List<Double> allCostsFromCallsTextLogAsDoubles() {
		return convertListOfStringCostsIntoDoubles(callsTextLogCostsWithOutNulls());
	}

	/**
	 * This routine removes "-" values from the "COST" column
	 * 
	 * @return = a List of Strings of the format "$00.00"
	 */

	public List<String> callsTextLogCostsWithOutNulls() {
		return removeNullCosts(columnOfSpecificHeaderInCallsTxtLog("COST"));
	}

	/**
	 * This routine converts "-" values from the "COST" column into $0.00
	 * 
	 * @return = a List of Strings of the format "$00.00"
	 */

	public List<String> callsTextLogCostsWithNullsEqualToZero() {
		return convertNullCostsToZero(columnOfSpecificHeaderInCallsTxtLog("COST"));
	}

	/**
	 * This routine gets List of row cells of Type WebElement from the Calls Text
	 * Log Table, by a user specified ptn.
	 * 
	 * @param firstColKey
	 *            = the ptn
	 * @return = a ptn record, a row within the Calls Text Log Data Table
	 */

	public List<WebElement> callsTextLogRowCellsFromSpecificKey(String firstColKey) {
		List<WebElement> a = callsTextLogRowFromSpecificKey(firstColKey).findElements(appendedRowCell);
		a.add(0, specificKey(firstColKey));
		return a;
	}

	/**
	 * This routine gets an entire row from the Calls Text Log Data Table as a
	 * single WebElement, without including the first column ptn value.
	 * 
	 * @param firstColKey
	 *            = the ptn
	 * @return = row from Calls Text Log Data Table
	 */
	public WebElement callsTextLogRowFromSpecificKey(String firstColKey) {
		return specificKey(firstColKey).findElement(callsTextLogsRowFromSpecificKeyLoc);
	}

	public WebElement defaultOnArrow() {
		return getElementWithTextFromList("ON", callsTextLogHeaderListForDataTable())
				.findElement(iconLoc(Constants.GREEN_DOWN_ARROW_ICON_ATTRIBUTE));
	}

	public WebElement getSpecificIconFromCallsTextLogHeader(String targetText, String attributeColorProp) {
		WebElement e = getElementWithTextFromList(targetText, callsTextLogHeaderListForDataTable());
		System.out.println(e.toString());
		WebElement icon = e.findElement(iconLoc(attributeColorProp));
		return icon;
	}

	public Boolean arrowIsNotHidden(WebElement arrow) {
		return !arrow.getAttribute("class").contains("ng-hide");
	}

	public WebElement getIconFromCallsTextLogHeader(String targetText) {
		WebElement e = getElementWithTextFromList(targetText, callsTextLogHeaderListForDataTable());
		WebElement icon = e.findElement(generalIconLoc);
		return icon;
	}

	public Boolean arrowIsHidden(WebElement arrow) {
		return arrow.getAttribute("class").contains("ng-hide");
	}

	public String iconColorFromCallsTextLog(String targetText, String attributeColorProp) {
		return getSpecificIconFromCallsTextLogHeader(targetText, attributeColorProp)
				.getCssValue(Constants.TEXT_COLOR_CSS_VALUE);
	}

	public Boolean callstTextLogArrowIconPointsCorrectly(String targetText, String attributeDirection) {
		String direction = getSpecificIconFromCallsTextLogHeader(targetText, attributeDirection).getAttribute("class");
		return direction.equals(attributeDirection);
	}

	public WebElement sortedHeaderText(String headerName) {
		return driver.findElement(sortedHeaderTextLoc(headerName));
	}

	public String callsTextLogHeaderTextColor(String headerName) {
		WebElement e = getElementWithTextFromList(headerName, callsTextLogHeaderListForDataTable());
		return e.getCssValue(Constants.TEXT_COLOR_CSS_VALUE);
	}

	public String callsTextLogSortedHeaderTextColor(String headerName) {
		WebElement e = sortedHeaderText(headerName);
		return e.getCssValue(Constants.TEXT_COLOR_CSS_VALUE);
	}

	public List<WebElement> listOfIcons(String arrowName) {
		return retryElementsUntilPresent(iconLocByName(arrowName));
	}

	public void clickForDesiredArrowDirection(String arrowName, Integer totalNumberOfClicks, String upOrDown)
			throws Exception {
		List<WebElement> list = listOfIcons(arrowName);
		for (WebElement e : list) {
			if (upOrDown.equalsIgnoreCase("up")) {
				if (arrowIsUp(e) && arrowIsNotHidden(e)) {
					clickWebElementACertainNumberOfTimes(e, totalNumberOfClicks);
				}
			} else if (upOrDown.equalsIgnoreCase("down")) {
				if (arrowIsUp(e) && arrowIsNotHidden(e)) {
					clickWebElementACertainNumberOfTimes(e, totalNumberOfClicks);
				}
			}
		}
	}

	public void clickIconFromCallsTextLogHeaderACertainNumberOfTimes(String targetText, int totalNumOfClicks)
			throws Exception {
		for (int click = 0; click < totalNumOfClicks; click++) {
			clickIconFromCallsTextLog(targetText);
		}
	}

	public void clickIconFromCallsTextLog(String targetText) throws Exception {
		scrollToAndClickElement(getIconFromCallsTextLogHeader(targetText));
	}

	/**
	 * This routine gets a "coordinate" within the Calls Text Log Data Table. This
	 * coordinate is defined by user specified ptn and the column header.
	 * 
	 * @param firstColKey
	 *            = the ptn
	 * @param desiredHeader
	 *            = the column header within the Calls Text Log Data Table
	 * @return = String of a ptn's specific record value
	 */

	public String callsTextLogDataTable(String firstColKey, String desiredHeader) {
		Map<String, String> table = createTableOfStringsFromWebElements(callsTextLogHeaderListForDataTable(),
				callsTextLogRowCellsFromSpecificKey(firstColKey));
		String chargeAmount = table.get(desiredHeader);
		return chargeAmount;
	}

	public WebElement inputTextBox() {
		return driver.findElement(inputTextBoxLoc);
	}

	public void clickInputTextBox() throws Exception {
		scrollToAndClickElement(inputTextBox());
	}

	public WebElement updateFilterButton() {
		return driver.findElement(updateFilterButtonLoc);
	}

	public void clickWebElementACertainNumberOfTimes(WebElement e, int totalNumOfClicks) throws Exception {
		for (int click = 0; click < totalNumOfClicks; click++) {
			scrollToAndClickElement(e);
		}
	}

	public void clickUpdateFilterButton() throws Exception {
		scrollToAndClickElement(updateFilterButton());
	}

	public void sendValuesToInputTextBox(String userInputText) {
		inputTextBox().sendKeys(userInputText);
	}

	public void clearInputTextBox() {
		inputTextBox().clear();
	}

	public Boolean arrowIsBlueDown(WebElement e) {
		return e.getAttribute("class").contains(Constants.BLUE_DOWN_ARROW_ICON_ATTRIBUTE);
	}

	public Boolean arrowIsUp(WebElement e) {
		return e.getAttribute("class").contains(Constants.BLUE_UP_ARROW_ICON_ATTRIBUTE);
	}

	public Boolean arrowIsNotHiddenAndIsAscending(String arrowName) {
		return arrowIsUp(getSpecificIconFromCallsTextLogHeader(arrowName, "color--blue"));
	}

	public WebElement desiredCalendarMonth(String monthAndYearText) {
		return driver.findElement(desiredCalendarMonthLoc(monthAndYearText));
	}

	public WebElement desiredDateAndMonth(String monthAndYearText, String date) {
		return desiredCalendarMonth(monthAndYearText).findElement(desiredDateLoc(date));
	}

	public String desiredDateAndMonthBorderColor(String monthAndYearText, String date, String partOfBoxToText) {
		return desiredDateAndMonth(monthAndYearText, date).getCssValue(partOfBoxToText);
	}

	public void clickDesiredDateAndMonth(String monthAndYearText, String date) throws Exception {
		scrollToAndClickElement(desiredDateAndMonth(monthAndYearText, date));
	}

	public WebElement ptnFilterButton() {
		return driver.findElement(ptnFilterButtonLoc);
	}

	public WebElement ptnFilterButtonText() {
		return ptnFilterButton().findElement(ptnFilterButtonTextLoc);
	}

	public String textOfPtnFilterButtonText() {
		return ptnFilterButtonText().getText();
	}

	public List<WebElement> callsTextLogHeaderListForDataTable() {
		List<WebElement> a = headerSingleElement().findElements(callsTextLogHeaderListLoc);
		a.add(0, firstCol());
		return a;
	}

	public WebElement labelTextBox() {
		return driver.findElement(labelTextBoxLoc);
	}

	public boolean checkMarkInLabelTextBoxIsNotEmpty() {
		System.out.println("the check mark css val is:_" + labelTextBox().getCssValue("content"));
		return !labelTextBox().getCssValue("content").isEmpty();

	}

	public int getIndexOfSpecificHeaderForCallsText(String headerName) {
		return getIndexOfElementWithTextFromList(headerName, callsTextLogHeaderListForDataTable());
	}

	public String getValueAtRowNumberAndColumnHeading(int row, String column) {
		int columnIndex = getIndexOfElementWithTextFromList(column, columnHeadings());
		if (columnIndex == 0) {
			return retryElementsUntilPresent(get_column_values(true, columnIndex)).get(row - 1).getText();
		} else {
			return retryElementsUntilPresent(get_column_values(false, columnIndex)).get(row - 1).getText();
		}
	}

	private By get_column_values(boolean firstColumnOrNot, int columnIndex) {
		String text = firstColumnOrNot ? ".first-col" : ".others-col";
		int index = firstColumnOrNot ? 1 : columnIndex;
		String tail = columnIndex == 1 ? " > span" : "";
		Object[] args = { text, index };
		return By.cssSelector(formatPath(PATH_TO_TABLE_VALUE, args, tail));
	}

	public Boolean columnIsHighlighted(String column) {
		int columnIndex = getIndexOfElementWithTextFromList(column, columnHeadings());
		return getElementCssValue(columnHeadings().get(columnIndex), "color")
				.equals(Colors.ROAMING_GREEN.getColorRGBAValue());
	}

	public WebElement getArrow(String column) {
		return retryElementUntilPresent(column_with_heading(column));
	}

	private By column_with_heading(String column) {
		return By.xpath(String.format(COLUMN_WITH_HEADING, column));
	}

	public Boolean arrowPointsDown(String column) {
		WebElement arrow = getArrowNextToColumn(column);
		return arrow != null && arrow.isDisplayed();
	}

	private WebElement getArrowNextToColumn(String column) {
		try {
			return retryElementUntilPresent(arrow_next_to_column(column));
		} catch (Exception e) {
			return null;
		}
	}

	private By arrow_next_to_column(String column) {
		return By.xpath(String.format(DOWN_ARROW_NEXT_TO_COLUMN, column));
	}

	public Boolean dateColumnAscending(String column) {
		List<String> list = makeListElementsStrings(getColumnValues(column));
		DateFormat readFormat = new SimpleDateFormat("MMM dd hh:mm a", Locale.US);
		list.remove("");
		List<Date> dates = list.stream().map(s -> {
			try {
				return readFormat.parse(s);
			} catch (ParseException e) {
				return null;
			}
		}).collect(Collectors.toList());
		List<Date> dateSorted = dates;
		Collections.sort(dateSorted);
		return dates.equals(dateSorted);
	}

	/*
	 * for (int i = 0; i < list.size(); i++) { if (!list.get(i).equals("") &&
	 * !list.get(i).equals("XXXX")) { listMod.add(list.get(i)); } } DateFormat
	 * readFormat = new SimpleDateFormat("MMM dd hh:mm a", Locale.US); Date date1,
	 * date2 = null; for (int i = 1; i < listMod.size(); i++) { date2 =
	 * readFormat.parse(listMod.get(i)); date1 = readFormat.parse(listMod.get(i -
	 * 1)); if (date2.compareTo(date1) < 0) { return false; } } return true; }
	 */
	public Boolean dateColumnDescending(String column) throws ParseException {
		List<String> list = makeListElementsStrings(getColumnValues(column));
		List<String> listMod = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).equals("") && !list.get(i).equals("XXXX")) {
				listMod.add(list.get(i));
			}
		}
		DateFormat readFormat = new SimpleDateFormat("MMM dd hh:mm a", Locale.US);
		Date date1, date2 = null;
		for (int i = 1; i < listMod.size(); i++) {
			date2 = readFormat.parse(listMod.get(i));
			date1 = readFormat.parse(listMod.get(i - 1));
			if (date1.compareTo(date2) < 0) {
				return false;
			}
		}
		return true;
	}

	public List<WebElement> getColumnValues(String column) {
		int columnIndex = getIndexOfElementWithTextFromList(column, columnHeadings());
		if (columnIndex == 0) {
			return retryElementsUntilPresent(get_column_values(true, columnIndex));
		} else {
			return retryElementsUntilPresent(get_column_values(false, columnIndex));
		}
	}

	public List<String> getMessages() {
		return makeListElementsStrings(retryElementsUntilPresent(LIST_MESSAGES));
	}

	public List<String> getRoaming() {
		return makeListElementsStrings(retryElementsUntilPresent(ROAMING_MESSAGES));
	}

	public Boolean listContainsText(List<String> messages, Object dIRECT_CONNECT) {
		for (String item : messages) {
			if (item.equals(dIRECT_CONNECT)) {
				return true;
			}
		}
		return false;
	}

	public WebElement radioButtonWithText(String radioButton) {
		return retryElementUntilPresent(radio_button(radioButton));
	}

	private By radio_button(String radioButton) {
		radioButton = radioButton.replaceAll(" ", "");
		return By.cssSelector(String.format(RADIO_BUTTON_TEXT, radioButton));
	}

	public Boolean minsColumnDescending(String column) throws ParseException {
		List<String> list = makeListElementsStrings(getColumnValues(column));
		List<String> listMod = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).equals("") && !list.get(i).equals("XXXX")) {
				listMod.add(list.get(i));
			}
		}
		DateFormat readFormat = new SimpleDateFormat("hh:mm");
		Date date1, date2 = null;
		for (int i = 1; i < listMod.size(); i++) {
			date2 = readFormat.parse(listMod.get(i));
			date1 = readFormat.parse(listMod.get(i - 1));
			if (date2.compareTo(date1) < 0) {
				return false;
			}
		}
		return true;
	}

	public Boolean minsColumnAscending(String column) throws ParseException {
		List<String> list = makeListElementsStrings(getColumnValues(column));
		List<String> listMod = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).equals("") && !list.get(i).equals("XXXX")) {
				listMod.add(list.get(i));
			}
		}
		DateFormat readFormat = new SimpleDateFormat("hh:mm");
		Date date1, date2 = null;
		for (int i = 1; i < listMod.size(); i++) {
			date2 = readFormat.parse(listMod.get(i));
			date1 = readFormat.parse(listMod.get(i - 1));
			if (date1.compareTo(date2) > 0) {
				return false;
			}
		}
		return true;
	}

	public List<List<String>> getCSVData(String fileName) throws IOException {
		File newFile = new File(path.concat(className).concat("_" + fileName));
		Boolean test;
		List<List<String>> list = new ArrayList<List<String>>();
		br = new BufferedReader(new FileReader(newFile));
		String firstLine = br.readLine();
		String[] potColumns = firstLine.split(",");
		try {
			test = potColumns[1].length() > 0;
		} catch (Exception e) {
			test = false;
		}
		if (firstLine.replaceAll(",", "").length() > 1 && test) {
			String[] columns = firstLine.split(",");
			list.add(Arrays.asList(columns));
		}
		String line = "";
		while ((line = br.readLine()) != null) {
			String[] row = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			list.add(Arrays.asList(row));
		}
		br.close();
		return list;
	}

	public double addUpColumn(List<List<String>> data, String columnHeading) {
		int minStart = 2;
		int minEnd = 4;
		int hourStart = 0;
		int hourEnd = 1;
		int column = getIndexOfStringInList(columnHeading, data.get(0));
		double sum = 0;
		if (!data.get(1).get(column).contains(":")) {
			for (int i = 1; i < data.size(); i++) {
				try {
					String value = data.get(i).get(column).replaceAll("^\"|\"$", "").replaceAll("\\$", "");
					sum += Double.parseDouble(value);
				} catch (Exception e) {
				}
			}
		} else if (data.get(1).get(column).contains(":")) {
			for (int i = 1; i < data.size(); i++) {
				try {
					String value = data.get(i).get(column).replaceAll("^\"|\"$", "");
					int mins = Integer.parseInt(value.substring(minStart, minEnd));
					int hours = Integer.parseInt(value.substring(hourStart, hourEnd));
					sum += (double) (hours * 60 + mins);
				} catch (Exception e) {
				}
			}
		}
		return sum;
	}

	public WebElement expandButtonWithNumber(String phone) {
		return retryElementUntilPresent(button_with_number(phone));
	}

	private By button_with_number(String phone) {
		String xpath = String.format(BUTTON_WITH_NUMBER, phone);
		return By.xpath(xpath);
	}

	public void invokeDownloadAndOkAlternative(String csv) throws Exception {
		int number_of_files = dir.list(filter).length;
		for (int i = 0; i < 3; i++) {
			try {
				resilientClick(openDownloadButton());
				if (csvPopupDisplayed()) {
					resilientClick(openOkayButton());
				}
				waitForCSVToDownload(number_of_files);
				File oldFile = new File(path + "calls.csv");
				File newFile = new File(path + className + "_" + csv);
				oldFile.renameTo(newFile);
				break;
			} catch (Exception e) {
			}
		}
	}

	public final By TABLE_ROWS = By.cssSelector(".bb-responsive-table");
	public final By DIV_TABLE = By.cssSelector("div.table-content.first-line");

	public List<List<String>> getTableValues() {
		List<WebElement> rows = retryElementsUntilPresent(TABLE_ROWS);
		return createListStringFromRows(rows, DIV_TABLE);
	}

}
