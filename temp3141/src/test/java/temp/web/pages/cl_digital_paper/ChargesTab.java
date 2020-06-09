package com.sprint.iice_tests.web.pages.cl_digital_paper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sprint.iice_tests.web.pages.il_digital_paper.TabBase;

public class ChargesTab extends TabBase implements TabActionsCL {

	private final By SPRINT_PREMIUM_SERVICES_SPECAIL_SELECTOR = By
			.cssSelector("#bb-charges-table > div:nth-child(7) > div.row.bottom-xs.middle-lg.charge-row");
	private final By ACCOUNT_CHARGE_TOTAL = By
			.cssSelector(".account-charges bb-charges-table:nth-of-type(1) .is-total-label");
	private final By ACCOUNT_TOTAL_CHARGES_ROW = By
			.cssSelector("#bb-charges-table > div.data-row");
	private final By OPEN_ROW_TO_TABLE = By.xpath(
			"//*[contains(@class,'ico--collapse')]/parent::*/parent::*/following-sibling::*//*[contains(@class,'row')]/following-sibling::*//*[contains(@class,'row')]//*[contains(@class,'row')]");
	private final By LABEL_BLOCK = By.cssSelector(".label-block .label");
	public final By AMOUNT_FROM_EXPAND_TEXT = By
			.xpath("./parent::*/parent::*//div[contains(@class,'bb-accordion-amount')]/div");
	private final By HEADER_TO_ROWS = By
			.xpath("./parent::*/parent::*/parent::*/following-sibling::*//*[contains(@class,'row element ng-scope')]");
	public final By GREEN_POPUP_TEXT = By
			.cssSelector("#bb-charges-table #myModal .modal-body");
	private final By EQUIP_DETAILS_HEADER = By
			.cssSelector("#bb-equipments-details-payments-scheduled .row.header span");
	public final By USAGE_RESPONSIVE_TABLE_ROWS = By.cssSelector("#bb-usage-responsive-table .data-row span.data");
	private final By TOP_SUBSCRIBERS_BY_USAGE_ROWS = By.cssSelector(".subscriber .row");
	private final By VIEW_CALL_DETAILS_LINK = By.cssSelector(".call-details a");
	private final By EXPAND_BUTTON_ROWS = By.cssSelector(".bb-accordion-row");
	private final By EXPAND_BUTTON_TOTAL = By.cssSelector("bb-total span");
	private final By PLANS_BY_SUBSCRIBER_ROWS = By.cssSelector(".plan .row");
	private final By EXCLAMATION_POINTS = By.cssSelector(".ico--incorrect");
	private final By HISTORIC_GRAPH = By.cssSelector(".bb-historic-graph");
	private final By GRPAH_DATES = By.cssSelector(".bb-graph-date");
	private final By GRPAH_AMOUNTS = By.cssSelector(".bb-graph-amount");
	private final By chargeParagraphLoc = By.xpath(".//div[@class='bb-regular-charge-bottom-description ng-scope']");
	private final By totalChargeByDateLoc = By.xpath("//div[@class='bb-total']");
	private final By chargeTotalLoc = By.xpath(".//div[@class='bb-regular-charge-total']");
	private final By NEXT_ELEMENT = By.xpath("./following-sibling::*");
	private final By ROW_DESCRIPTION = By.cssSelector(".row .description span");
	private final By TOTAL_ROW_SELECT = By.cssSelector("bb-subscriber-badge div");;
	private final By FOOTER = By.cssSelector(".footer-row span");
	private final By ROW_VALUES = By.cssSelector(".row.middle-xs .charge-col > span");
	private final By HEADERS = By.cssSelector(".header-label");
	private final By EXPAND_BUTTONS = By.cssSelector(".bb-accordion-icon i");
	private final By DOWNLOAD_LINK = By.cssSelector("span.downloadLink");
	private final By COLUMN_HEADINGS = By.cssSelector(".header-label");
	private final By SUBSCRIBERS_TOGGLE = By.cssSelector(".sprint-toggle label");
	private final By fullBreadCrumbLoc = By.cssSelector("#breadcrumb-ul");
	private final By breadCrumbsLoc = By.cssSelector(" > li > a");
	private final By DEVICE_IMAGE_IN_ROW = By.cssSelector(".header .device-image");
	private final By ACCOUNT_CHARGE_HEADERS = By.cssSelector(".account-charges .header-label");
	public final By ACCOUNT_CHARGE_DATA = By.cssSelector(".account-charges .charge-row .charge-col");
	private final By CHARGES_OVERVIEW_DATA = By.cssSelector("bb-charges-table .charge-row .charge-col");
	private final By CHEVRON_RIGHT = By.cssSelector("#bb-usage-responsive-table .ico--chevron-right");
	public final By USAGE_RESPONSIVE_TABLE_HEADERS = By.cssSelector("#bb-usage-responsive-table .bb-row.header span");
	public final By USAGE_RESPONSIVE_TABLE_GRAPH = By.cssSelector("#bb-usage-responsive-table .modal-body");
	public final By USAGE_RESPONSIVE_TABLE_GRAPH_DATES = By.cssSelector("#bb-usage-responsive-table .modal-body .bb-graph-date");
	public final By USAGE_RESPONSIVE_TABLE_GRAPH_AMOUNTS = By.cssSelector("#bb-usage-responsive-table .modal-body .bb-graph-date .bb-graph-amount");
	public final By GRAPH_DATES = By.cssSelector(" .bb-graph-date");
	public final By GRAPH_AMOUNTS = By.cssSelector(" .bb-graph-amount");
	private final By VIEW_TRENDS_LINKS = By.cssSelector("#bb-usage-responsive-table a.link");
	private final By POPUP_CLOSE_BUTTON = By.cssSelector("#bb-close-modal");
	public final String COLUMN_WITH_TEXT = "//div[contains(@class,'header')]//*[contains(text(),'%s')]";
	private final String SPAN_MEDIUM_A = " span, a, .medium";
	private final String DATES_NEXT_TO_TITLE = "//*[contains(@class,'subscriber-usage')][%d]//*[contains(text(),'%s')]//following-sibling::*";
	private final String DOWNLOAD_FULL_OVERVIEW = "Download Full Overview";
	public final String BUTTON_WITH_TEXT = "//*[starts-with(@class,'bb-accordion-title')]/*[contains(text(),'%s')]/parent::*/parent::*//i";
	private final String SUBSCRIBER_ROW_FROM_PHONE_NUMBER = "//main-component//div[contains(text(),'%s')]";
	private final String DAC_ROW_WITH_ID = "//*[contains(text(),'%s')]/parent::*/parent::*";
	private final String HEADER_COL_WITH_TEXT = "//*[contains(@class,'headerCol')]//*[contains(text(),'%s')]";
	private final String SUMMARY_MESSAGE = "//strong[contains(text(),'%s')]";
	private final String LABEL_BLOCK_TITLE = "//*[contains(@class,'label-block')]//*[contains(text(),'%s')]";
	private final String TABLE_HEADER = "//*[text()='%s']//parent::*//parent::*//parent::*//parent::*//parent::*//bb-charges-table//*[contains(text(),'%s')]";
	private final String STANDARD_TABLE_ROWS = "//*[text()='%s']//parent::*//parent::*//parent::*//parent::*//parent::*//bb-charges-table//*[contains(@class,'charge-row')]";
	private final String SUBSCRIBERS = "Subscribers";
	private final String DEPARTMENT = "Departments";
	private final String DAC = "DACs";
	public  Function<WebElement, Boolean> arrowPointsUp = e -> e.getAttribute("class").contains("arrow-up");
	private BooleanSupplier labelTextEqualsSubscribers = () -> getLabelText().equals(SUBSCRIBERS);
	private BooleanSupplier labelTextEqualsDACs = () -> getLabelText().equals(DAC);


	/*
	 * Charges Overview methods
	 */
	public WebElement getViewAllSubscibersToggle() {
		return retryElementUntilPresent(SUBSCRIBERS_TOGGLE);
	}

	public WebElement getDownloadFullOverview() {
		return retryElementUntilPresent(equals_text(DOWNLOAD_FULL_OVERVIEW));
	}

	public List<String> getHeadersText() {
		return turnWebElementsToText(getHeaders());
	}

	public WebElement totalRowSelect() {
		return retryElementUntilPresent(TOTAL_ROW_SELECT);
	}

	public List<String> getRowValuesChargesOverviewText() {
		return turnWebElementsToTextKeepBlanks(getRowValuesChargesOverview());
	}

	/*
	 * Account Charges methods
	 */
	
	
	public List<String> getAccountChargeData() {
		return turnWebElementsToTextKeepBlanks(retryElementsUntilPresent(ACCOUNT_CHARGE_DATA));
	}

	public List<String> getAccountChargeHeaders() {
		return turnWebElementsToText(retryElementsUntilPresent(ACCOUNT_CHARGE_HEADERS));
	}

	public String getGreenPopupText() {
		List<String> list = turnWebElementsToText(retryElementsUntilPresent(GREEN_POPUP_TEXT));
		return String.join(" ", list);
	}

	public WebElement getAccountChargeTotal() {
		return retryElementUntilPresent(ACCOUNT_CHARGE_TOTAL);
	}
	
	public WebElement getAccountTotalChargesRow() {
		return retryElementUntilPresent(ACCOUNT_TOTAL_CHARGES_ROW);
	}

	/*
	 * Subscriber Methods
	 */
	public WebElement subscriberRowWithPhoneNumber(String ptnId) {
		return retryElementUntilPresent(subscriber_row_from_phone_number(ptnId));
	}

	/*
	 * DAC table methods
	 */
	public void clickDACRowWithPhoneNumber(String ptnId) {
		clickUntilConditionIsMet(dacRowWithPhoneNumber(ptnId), labelTextEqualsSubscribers);
	}

	public void clickeDepartmentRowWithID(String ptnId) {
		clickUntilConditionIsMet(dacRowWithPhoneNumber(ptnId), labelTextEqualsDACs);
	}

	/*
	 * Expand Button table methods
	 */
	public void openExpandButtons(String headingText) {
		List<WebElement> buttons = getExpandButtons();
		clickElementsMatchingConditionHandleStale(buttons, buttonIsOpen);
		clickUntilConditionIsMet(getButtonWithText(headingText), buttonElementIsOpen, getButtonWithText(headingText));
	}

	public List<List<String>> getTableDataInOpenRow() {
		List<WebElement> rows = retryElementsUntilPresent(OPEN_ROW_TO_TABLE);
		return createListStringFromRows(rows, DIV);
	}

	public WebElement graphsInOpenButton() {
		return getDisplayedElement(retryElementsUntilPresent(HISTORIC_GRAPH));
	}

	public List<String> getGraphDatesInOpenRow() {
		return turnWebElementsToText(graphsInOpenButton().findElements(GRPAH_DATES));
	}

	public List<String> getGraphAmountsInOpenRow() {
		return turnWebElementsToTextKeepDuplicates(graphsInOpenButton().findElements(GRPAH_AMOUNTS));
	}

	public String getAmountFromExpandText(String header1) {
		return amount_from_expand_text(header1).getText();
	}

	public WebElement getDownloadButton() {
		return retryElementUntilPresent(DOWNLOAD_LINK);
	}

	public WebElement dacRowWithPhoneNumber(String ptnId) {
		return retryElementUntilPresent(dac_row_with_id(ptnId));
	}

	public String getRowDescription() {
		return retryElementUntilPresent(ROW_DESCRIPTION).getText();
	}

	public boolean titleIsDisplayed(String equipmentInfo) {
		return retryElementUntilPresent(header_column(equipmentInfo)) != null ? true : false;
	}

	public boolean iconInOpenRowDisplays() {
		return getIcon() != null ? true : false;
	}

	public boolean summaryMessageIsDisplayed(String summaryMessage) {
		return getSummaryMessage(summaryMessage) != null ? true : false;
	}

	public List<List<String>> getTableDataFromHeaders(String equipmentInfo) {
		WebElement head = retryElementUntilPresent(header_column(equipmentInfo));
		List<WebElement> rows = head.findElements(HEADER_TO_ROWS);
		return createListStringFromRows(rows, SPAN);
	}

	public List<WebElement> getExclamationPoints() {
		return retryElementsUntilPresent(EXCLAMATION_POINTS);
	}

	public String getAccountChargeTotalText() {
		return getAccountChargeTotal().getText();
	}

	public List<String> getEquipDetailHeaders() {
		return turnWebElementsToText(retryElementsUntilPresent(EQUIP_DETAILS_HEADER));
	}

	public WebElement downloadPDFIcon(String ptn) {
		return retryElementUntilPresent(downloadPDFIconLoc(ptn));
	}

	public String totalChargeByDateText() {
		return deleteLineBreaks(totalChargeByDate().getText());
	}

	public WebElement totalChargeByDate() {
		return retryElementUntilPresent(totalChargeByDateLoc);
	}

	public String chargeParagraphText(String buttonName) {
		return chargeParagraph(buttonName).getText();
	}

	public WebElement chargeParagraph(String buttonName) {
		return findChildOfParent(contentUnderOpenButton(buttonName), chargeParagraphLoc);
	}

	public WebElement contentUnderOpenButton(String buttonName) {
		return retryElementUntilPresent(contentUnderOpenButtonLoc(buttonName));
	}

	public WebElement chargeTotal(String buttonName) {
		return findChildOfParent(contentUnderOpenButton(buttonName), chargeTotalLoc);
	}

	public WebElement fullBreadCrumb() {
		return retryElementUntilPresent(fullBreadCrumbLoc);
	}

	public String fullBreadCrumbText() {
		return retryElementAndGetText(fullBreadCrumbLoc);
	}

	public List<WebElement> breadCrumbs() {
		return fullBreadCrumb().findElements(breadCrumbsLoc);
	}

	public boolean csvFileNameMatches(String fileName) {
		List<File> csv = new ArrayList<File>();
		sleep(2000);
		for (File file : dir.listFiles()) {
			if (file.getName().contains(fileName))
				csv.add(file);
		}
		return csv.size() == 1;
	}

	public List<String> getCSVData() throws Exception {
		List<File> csv = new ArrayList<File>();
		sleep(2000);
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith(".csv"))
				csv.add(file);
		}
		List<String> list = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(csv.get(0)));
		String line = null;
		while ((line = br.readLine()) != null) {
			list.add(line);
		}
		br.close();
		//csv.get(0).delete();
		return list;
	}

	public List<List<String>> get2_DimCSVData() throws Exception {
		List<File> csv = new ArrayList<File>();
		sleep(2000);
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith(".csv"))
				csv.add(file);
		}
		List<List<String>> list = new ArrayList<List<String>>();
		BufferedReader br = new BufferedReader(new FileReader(csv.get(0)));
		String line = null;
		while ((line = br.readLine()) != null) {
			list.add(Arrays.asList(line.split(",")));
		}
		br.close();
		//csv.get(0).delete();
		return list;
	}

	/*
	 * Unsorted
	 */
	public void sortByColumnAscTrueDescFalse(String column, boolean ascending) {
		resilientClick(getColumnIndexWebElement(column));
		String arrowDirection = getElementAttribute(getColumnIndexWebElement(column).findElement(NEXT_ELEMENT),
				"class");
		boolean click = arrowDirection.matches(".*down.*") && ascending
				|| arrowDirection.matches(".*up.*") && !ascending;
		if (click)
			resilientClick(getColumnIndexWebElement(column));
		sleep(5000);
	}

	public List<String> getRowValuesText() {
		return turnWebElementsToTextKeepBlanks(getRowValues());
	}

	public String getFooterDescription() {
		return retryElementsUntilPresent(FOOTER).get(0).getText();
	}

	public String getFooterAmount() {
		return retryElementsUntilPresent(FOOTER).get(1).getText();
	}

	/*
	 * Private methods driving public methods above
	 */
	private final By downloadPDFIconLoc(String ptn) {
		return By.xpath("//span[contains(text(),'" + ptn
				+ "')]/parent::*/parent::*/div[@class='col-xs-24 col-lg-auto last-lg links-block']/span//a[@class='bb-pdf-download']");
	};

	private final By contentUnderOpenButtonLoc(String buttonName) {
		return By.xpath("//div[text()='" + buttonName + "']/parent::*/parent::*/parent::*");
	};

	private By dac_row_with_id(String ptnId) {
		return By.xpath(String.format(DAC_ROW_WITH_ID, ptnId));
	}

	private List<WebElement> getRowValues() {
		return retryElementsUntilPresent(ROW_VALUES);
	}

	private WebElement amount_from_expand_text(String header1) {
		return getButtonWithText(header1).findElement(AMOUNT_FROM_EXPAND_TEXT);
	}

	private By subscriber_row_from_phone_number(String ptnId) {
		return By.xpath(String.format(SUBSCRIBER_ROW_FROM_PHONE_NUMBER, ptnId));
	}

	private WebElement getButtonWithText(String headingText) {
		return retryElementUntilPresent(button_with_text(headingText));
	}

	private By button_with_text(String headingText) {
		return By.xpath(String.format(BUTTON_WITH_TEXT, headingText));
	}

	private List<WebElement> getExpandButtons() {
		return resilientRetry(EXPAND_BUTTONS);
	}

	private WebElement getColumnIndexWebElement(String column) {
		List<WebElement> headings = getColumnHeadings();
		Optional<WebElement> element = headings.stream().filter(e -> e.getText().equalsIgnoreCase(column)).findFirst();
		return element.orElseThrow(() -> new NullPointerException("Null pointer exception"));
	}

	private List<WebElement> getColumnHeadings() {
		return retryElementsUntilPresent(COLUMN_HEADINGS);
	}

	private List<WebElement> getHeaders() {
		return retryElementsUntilPresent(HEADERS);
	}

	private List<WebElement> getRowValuesChargesOverview() {
		return retryElementsUntilPresent(CHARGES_OVERVIEW_DATA);
	}

	public String getLabelText() {
		return getText(retryElementUntilPresent(LABEL_BLOCK));
	}

	private WebElement getSummaryMessage(String summaryMessage) {
		return retryElementUntilPresent(summary_message(summaryMessage));
	}

	private By summary_message(String summaryMessage) {
		return By.xpath(String.format(SUMMARY_MESSAGE, summaryMessage));
	}

	private By header_column(String equipmentInfo) {
		return By.xpath(String.format(HEADER_COL_WITH_TEXT, equipmentInfo));
	}

	private WebElement getIcon() {
		return retryElementUntilPresent(DEVICE_IMAGE_IN_ROW);
	}

	public List<List<String>> getExpandButtonDetailsAndValues() {
		List<WebElement> rows = retryElementsUntilPresent(EXPAND_BUTTON_ROWS);
		return createListStringFromRows(rows, DIV);
	}

	public String getExpandButtonTotals() {
		List<String> list = turnWebElementsToText(retryElementsUntilPresent(EXPAND_BUTTON_TOTAL));
		return String.join(" ", list);
	}

	public List<List<String>> getPlansBySubscribersData() {
		List<WebElement> rows = retryElementsUntilPresent(PLANS_BY_SUBSCRIBER_ROWS);
		return createListStringFromRows(rows, SPAN);
	}

	public List<List<String>> getTopSubscribersByUsageData() {
		List<WebElement> rows = retryElementsUntilPresent(TOP_SUBSCRIBERS_BY_USAGE_ROWS);
		return createListStringFromRows(rows, SPAN_OR_DIV);
	}

	public List<String> getUsageResponsiveTableDisplayedValues() {
		return createListStringFromRowOnlyDispalyedElements(retryElementsUntilPresent(USAGE_RESPONSIVE_TABLE_ROWS));
	}
	/*	
	public String getUsageResponsiveTableAmountUnderHeader(String column) {
		List<String> list = getUsageResponsiveTableHeaders();
		int x = getIndexOfStringContainedInList(column, list);
		List<WebElement> row = retryElementsUntilPresent(USAGE_RESPONSIVE_TABLE_ROWS);
		return getText(row.get(x));
	}

	public WebElement displayedGraph() {
		return getDisplayedElement(getUsageResponsiveTableGraph());
	}

	private List<WebElement> getUsageResponsiveTableGraph() {
		return retryElementsUntilPresentOnlyDisplayedValues(USAGE_RESPONSIVE_TABLE_GRAPH);
	}

	private List<String> getUsageResponsiveTableHeaders() {
		return turnWebElementsToText(retryElementsUntilPresent(USAGE_TABLE_HEADERS));
	}

	public List<String> getGraphDatesInColumn() {
		List<WebElement> webElements = displayedGraph().findElements(GRAPH_DATES);
		return makeListElementsStrings(webElements);
	}

	public List<String> getGraphAmountsInColumn() {
		List<WebElement> webElements = displayedGraph().findElements(GRAPH_AMOUNTS);
		return makeListElementsStrings(webElements);
	}
*/
	public WebElement viewTrendsUnderColumn(String arg1) {
		int x = getColumnWithText(arg1).getLocation().getX();
		return webElementInX_Pos(getViewTrends(), x);	
	}

	private List<WebElement> getViewTrends() {
		return retryElementsUntilPresent(VIEW_TRENDS_LINKS);
	}

	private WebElement getColumnWithText(String arg1) {
		return retryElementUntilPresent(column_with_text(arg1));
	}

	private By column_with_text(String arg1) {
		return By.xpath(String.format(COLUMN_WITH_TEXT, arg1));
	}

	public WebElement rowWithValueInColumnSubcriberTables(String column, String value) {
		return retryElementUntilPresent(SPRINT_PREMIUM_SERVICES_SPECAIL_SELECTOR);
	}

	public List<List<String>> getDACTableValues() {
		List<WebElement> rows = retryElementsUntilPresent(standard_table_rows(DAC));
		return createListStringFromRowsLeftToRightKeepBlanks(rows, By.cssSelector(SPAN_MEDIUM_A));
	}

	public WebElement getChevronRight() {
		return retryElementUntilPresent(CHEVRON_RIGHT);
	}

	public WebElement popupCloseButton() {
		return getDisplayedElement(retryElementsUntilPresent(POPUP_CLOSE_BUTTON));
	}

	public List<List<String>> getDepartmentTableValues() {
		List<WebElement> rows = retryElementsUntilPresent(standard_table_rows(DEPARTMENT));
		return createListStringFromRowsLeftToRightKeepBlanks(rows, SPAN_OR_DIV);
	}

	public List<List<String>> getSubscribersTableValues() {
		List<WebElement> rows = retryElementsUntilPresent(standard_table_rows(SUBSCRIBERS));
		return createListStringFromRows(rows, DIV);
	}

	private By standard_table_rows(String s) {
		return By.xpath(String.format(STANDARD_TABLE_ROWS, s));
	}

	public boolean labelBlockIsDisplayed(String equipmentInfo) {
		return retryElementUntilPresent(label_block(equipmentInfo)) != null ? true : false;
	}

	private By label_block(String equipmentInfo) {
		return By.xpath(String.format(LABEL_BLOCK_TITLE, equipmentInfo));
	}

	public WebElement tableHeader(String table, String column) {
		return retryElementUntilPresent(table_header(table, column));
	}

	private By table_header(String table, String column) {
		Object[] args = { table, column };
		return By.xpath(String.format(TABLE_HEADER, args));
	}

	public WebElement arrowNextToTableHeader(String table, String column) {
		return tableHeader(table, column).findElement(FOLLOWING_SIBLING);
	}

	public WebElement getViewCallDetailsLink() {
		return retryElementUntilPresent(VIEW_CALL_DETAILS_LINK);
	}

	public String getDateNextToTitleInstance(String title, int i) {
		return retryElementUntilPresent(dates_next_to_title(title, i)).getText();
	}

	private By dates_next_to_title(String title, int i) {
		Object[] args = {++i, title};
		return By.xpath(String.format(DATES_NEXT_TO_TITLE, args));
	}

}
