package com.sprint.iice_tests.web.pages.cl_digital_paper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sprint.iice_tests.lib.dao.data.Constants;
import com.sprint.iice_tests.web.pages.il_digital_paper.TabBase;

public class Header extends TabBase {

	private final String UNDERLINE_RGB = "rgb(255, 206, 10)";
	private final String UNDERLINE_RGBA = "rgba(255, 206, 10, 1)";
	private final String DATE_RANGE_UNDER_BILL_TRENDS = "//*[text()='%s']/following-sibling::*";
	private final String FILLED_RECTANGLE = "//*[text()='%S']/parent::*/*[@class='bb-filled-rect']";
	private final String FILTER_DROPDOWN = "./parent::*/parent::*/parent::*/parent::*/bb-modal//*[text()='%s']";
	private final String DOWNLOAD_FULL_OVERVIEW = "Download Full Overview";
	private final String YEAR_FORMAT = "//*[contains(@class,'year') and contains(text(),'%s')]";
	private final String DATE_RANGE_FORMAT = "//*[contains(@class,'bill-col')]/*[contains(text(),'%s')]";
	private final String BILL_ALERTS = "Bill Alerts";
	private final String WIDTH = "width";
	private final String BILL_ALERTS_MSG = "//bb-bill-alert" + ELEMENT_TEXT;
	private final By BILL_PERIOD_DATES = By.cssSelector(".bill-col span");
	private final By BILL_ALERTS_CLOSE = By.cssSelector(".visible .ico--close");
	private final By ACCOUNT_INFORMATION = By.cssSelector(".link.right-border");
	private final By ACCOUNT_INFORMATION_TABLES = By.cssSelector("bb-account-information .row");
	private final By NAV_BAR_TABS = By.cssSelector(".scroll-menu a");
	private final By RED_BANNER = By.cssSelector(".bb-user-notifications div");
	private final By DATE_RANGE_DROPDOWN = By.cssSelector(".selected-date > span");
	private final By BAN_ELEMENT_LOCATION = By.cssSelector("#main .account-number");
	private final By BILL_ALERTS_ICON_NUMBER = By.cssSelector(".indicator > span");
	private final By MESSAGE_TO_ICON = By.xpath("/parent::*/parent::*/div/*[contains(@class,'ico')]");
	private final By PDF_BILL_ICON = By.cssSelector(".bb-pdf-download .medium");
	private final By INPUT_FIELD = By.cssSelector(".input-block .bb-input");
	private final By INPUT_FIELD_ERROR = By.cssSelector(".input-block .bb-input-search > .input-message.input-error");
	private final By INPUT_NOT_FOUND = By.cssSelector(".input-block .bb-input-search > .input-message.input-not-found");
	private final By FILTER_DROPDOWN_MENU = By.cssSelector(".input-block .ico--chevron-down");
	private final By FOLLOWING_SIBLING = By.xpath("./following-sibling::*");
	private final By BILL_TRENDS_GRAPHS = By.cssSelector("#myModal .bb-filled-rect");
	private final By BILL_TRENDS_BUTTON = By
			.xpath("//span[contains(text(),'Bill Charge Trends') and contains(@class,'btn-label')]");
	private final By BILL_TRENDS_GRAPHS_PARENT = By.cssSelector("#myModal .bb-historic-graph");
	private final By BILL_TRENDS_DATE = By.cssSelector(".bb-graph-date");
	private final By BILL_TRENDS_AMOUNT = By.cssSelector(".bb-graph-amount");
	private final By PREVIOUS_CHARGES_DUE_HEADER = By.xpath("//*[@id='main']/bb-recent-bill-banner/div/div/div/div[1]");
	private final By PREVIOUS_CHARGES_DUE_TABLE = By.id("previous-bill");
	private final By CURRENT_CHARGES_DUE_HEADER = By.xpath("//*[@id='main']/bb-recent-bill-banner/div/div/div/div[2]");
	private final By CURRENT_CHARGES_DUE_TABLE = By.id("current-bill");
	private final By AUTOPAY_MSG = By.cssSelector("div.col-auto.auto-pay-label > span");
	private Function<String, Boolean> navBarSelected = i -> desiredTabIsSelected(i);

	/*
	 * BAN Element Methods
	 */
	public WebElement banElement() {
		return retryElementUntilPresent(BAN_ELEMENT_LOCATION);
	}

	public String getBanText() {
		return getText(banElement());
	}

	/*
	 * Bill Alerts Methods
	 */
	public WebElement billAlerts() {
		return retryElementUntilPresent(contains_text(BILL_ALERTS));
	}

	public WebElement getBillAlertsIconNumber() {
		return retryElementUntilPresent(BILL_ALERTS_ICON_NUMBER);
	}

	public WebElement getIconByBillAlertsMessage(String message) {
		return getBillAllertWithMessage(message).findElement(MESSAGE_TO_ICON);
	}

	public String getBillAlertsIconNumberValue() {
		return getText(getBillAlertsIconNumber());
	}

	public boolean billAlertsMessageExists(String message) {
		return getBillAllertWithMessage(message) != null ? true : false;
	}
	
	public boolean billAlertsMsgExists(String msg) {
		return getBillAllertWithMessages(msg) != null ? true : false;
	}

	public void closeBillAlertsMessageBox() {
		resilientClick(getBillAllertsClose());
	}
	
	public boolean autopayMsgExists(String msg) {
		String value = jsGetTextOfWebElement(retryElementUntilVisible(AUTOPAY_MSG));
		System.out.println("[autopay]" + value);
		
		return StringUtils.equals(value, msg);
	}
	
	public WebElement autopayMsg(String msg) {
		return retryElementUntilPresent(contains_text(msg));
	}

	/*
	 * Bill Charge Trends Button Methods
	 */
	public WebElement getBillTrendsButton() {
		return retryElementUntilPresent(BILL_TRENDS_BUTTON);
	}

	public List<WebElement> getBillTrendsButtonGraphs() {
		return retryElementsUntilPresent(BILL_TRENDS_GRAPHS);
	}

	public String getAmountUnderDateRangeBillTrends(String dateRange) {
		return getText(retryElementUntilPresent(date_range_under_bill_trends(dateRange)));
	}

	public String getBillTrendsFillPercentUnderDateRange(String dateRange) {
		return getElementAttribute(retryElementUntilPresent(fill_percent_under_bill_trends(dateRange)), WIDTH);
	}
	
	public WebElement getBillTrendsParent() {
		return retryElementUntilPresent(BILL_TRENDS_GRAPHS_PARENT);
	}
	
	public WebElement getPreviousChargesDue() {
		return retryElementUntilPresent(PREVIOUS_CHARGES_DUE_HEADER);
	}

	/*
	 * Navigation Bar Methods
	 */
	public WebElement navBarTab(String tab) {
		return retryElementUntilPresent(contains_text(tab));
	}

	public boolean desiredTabIsSelected(String tab) {
		String color = getElementCssValue(navBarTab(tab), Constants.TAB_UNDERLINE_COLOR_CSS_VALUE);
		return color.equals(UNDERLINE_RGB) || color.equals(UNDERLINE_RGBA);
	}

	/*
	 * Red Banner Methods
	 */
	public WebElement getRedBanner() {
		return retryElementUntilPresent(RED_BANNER);
	}

	public String getRedBannerText() {
		return getText(getRedBanner());
	}

	/*
	 * Bill Period Dropdwon methods
	 */
	public WebElement dateRangeDropdown() {
		return retryElementUntilPresent(DATE_RANGE_DROPDOWN);
	}

	public void changeDateRange(String year, String monthRange) throws Exception {
		for (int i = 0; i < 5; i++) {
			resilientClick(dateRangeDropdown());
			resilientClick(retryElementRefreshFromBy(year_select(year)));
			resilientClick(monthRangeSelect(monthRange));
			sleep(2000);
			if (stringEquals(getText(dateRangeDropdown()), monthRange))
				break;
		}
	}

	/*
	 * Filter Element methods
	 */
	public WebElement getInputFieldError() {
		return retryElementUntilPresent(INPUT_FIELD_ERROR);
	}

	public WebElement getInputFieldNotFound() {
		return retryElementUntilPresent(INPUT_NOT_FOUND);
	}

	public WebElement getFilterDropdownMenu() {
		return retryElementUntilPresent(FILTER_DROPDOWN_MENU);
	}

	public String inputFieldNotFoundText() {
		return getText(getInputFieldNotFound());
	}

	public boolean inputFieldErrorIsDisplayed() {
		return getInputFieldError().isDisplayed();
	}

	public boolean greenCheckMarkNextToFilterIsDisplayed(String filter) {
		return findChildOfParent(filterDropdownSelection(filter), FOLLOWING_SIBLING).isDisplayed();
	}

	public void enterStringToInputField(String entry) {
		sendTextToElement(getInputField(), entry);
	}

	public void selectFilterFromFilterDropDownMenu(String filter) {
		resilientClick(getFilterDropdownMenu());
		resilientClick(filterDropdownSelection(filter));
	}

	/*
	 * Private methods driving public methods
	 */
	private WebElement getBillAllertsClose() {
		return retryElementUntilPresent(BILL_ALERTS_CLOSE);
	}

	private WebElement getBillAllertWithMessage(String message) {
		return retryElementUntilPresent(contains_text(message));
	}
	
	public WebElement getBillAllertWithMessages(String msg) {
		return retryElementUntilPresent(contains_billAlerts_msg(msg));
	}

	public WebElement getSavePrintPDFBill() {
		return retryElementUntilPresent(PDF_BILL_ICON);
	}

	public WebElement getDownloadFullOverview() {
		return retryElementUntilPresent(equals_text(DOWNLOAD_FULL_OVERVIEW));
	}

	private WebElement getInputField() {
		return retryElementUntilPresent(INPUT_FIELD);
	}

	private WebElement filterDropdownSelection(String filter) {
		return findChildOfParent(getFilterDropdownMenu(), filter_dropdown_selection(filter));
	}

	private By fill_percent_under_bill_trends(String dateRange) {
		return By.xpath(String.format(FILLED_RECTANGLE, dateRange));
	}

	private By date_range_under_bill_trends(String dateRange) {
		return By.xpath(String.format(DATE_RANGE_UNDER_BILL_TRENDS, dateRange));
	}

	private By month_range_select(String monthRange) {
		return By.xpath(String.format(DATE_RANGE_FORMAT, monthRange));
	}

	private By year_select(String year) {
		return By.xpath(String.format(YEAR_FORMAT, year));
	}

	private By filter_dropdown_selection(String filter) {
		return By.xpath(String.format(FILTER_DROPDOWN, filter));
	}
	
	public By contains_billAlerts_msg(String msg) {
		return By.xpath(String.format(BILL_ALERTS_MSG, msg));
	}


	private WebElement monthRangeSelect(String monthRange) {
		return retryElementUntilPresent(month_range_select(monthRange));
	}
	
	public WebElement getPreviousChargesDueViewDetails() {
		return retryElementUntilPresent(PREVIOUS_CHARGES_DUE_HEADER);
	}
	
	private WebElement getPreviousChargesDueViewDetailsButton() {
		return getPreviousChargesDueViewDetails().findElement(By.xpath("//*[contains(@class,'btn')]"));
	}
	
	private WebElement getCurrentChargesDueViewDetailsButton() {
		return retryElementUntilPresent(CURRENT_CHARGES_DUE_HEADER).findElement(By.xpath("//*[contains(@class,'btn')]"));
	}
	
	public WebElement getPreviousChargesDueTable() {
		return retryElementUntilPresent(PREVIOUS_CHARGES_DUE_TABLE);
	}

	public void clickUntilTabIsSelected(String navBarTab) {
		clickUntilConditionIsMet(navBarTab(navBarTab), navBarSelected, navBarTab);
	}

	public void waitForHighlightedTab(String tab) {
		int i = 0;
		while(!desiredTabIsSelected(tab) && i < 100) {
			sleep(100);
			i++;
		}
	}

	public WebElement getAccountInformation() {
		return retryElementUntilPresent(ACCOUNT_INFORMATION);	
	}

	public List<List<String>> getAccountInformationTableValues() {
		List<WebElement> rows = retryElementsUntilPresent(ACCOUNT_INFORMATION_TABLES);
		return createListStringFromRows(rows, SPAN);
	}

	public List<String> getAllTabsInNavBar() {
		return turnWebElementsToText(retryElementsUntilPresent(NAV_BAR_TABS));
	}

	public List<String> getBillPeriodDates() {
		return turnWebElementsToText(retryElementsUntilPresent(BILL_PERIOD_DATES));
	}
	
	public List<String> getBillTrendsGraphs() {
		return getBillTrendsButtonGraphs().stream().map(i -> i.getAttribute("width")).collect(Collectors.toList());
	}
	
	public List<String> getBillTrendsDates() {
		List<WebElement> elemDates = getBillTrendsParent().findElements(BILL_TRENDS_DATE);
		return elemDates.stream().map(i -> i.getText()).collect(Collectors.toList());		
	}
	
	public List<String> getBillTrendsAmounts() {
		List<WebElement> elemDates = getBillTrendsParent().findElements(BILL_TRENDS_AMOUNT);
		return elemDates.stream().map(i -> i.getText()).collect(Collectors.toList()); 			
	}
	
	public int locatePreviousChargesDueViewDetailsButton() {
		return getPreviousChargesDueViewDetailsButton().getLocation().getY();
	}
	
	public void clickPreviousChargesDueViewDetailsButton() {
		resilientClick(getPreviousChargesDueViewDetailsButton());
	}
	
	public List<String> getPreviousChargesDueHeaderValues() {
		List<WebElement> elemData = retryElementsUntilPresent(PREVIOUS_CHARGES_DUE_HEADER);
		return turnWebElementsToText(elemData);
	}
	
	public List<String> getPreviousChargesDueTableValues() {
		List<WebElement> elemData = retryElementsUntilPresent(PREVIOUS_CHARGES_DUE_TABLE);
		return turnWebElementsToText(elemData);
	}
	
	public List<String> getCurrentChargesDueHeaderValues() {
		List<WebElement> elemData = retryElementsUntilPresent(CURRENT_CHARGES_DUE_HEADER);
		return turnWebElementsToText(elemData);
	}
	
	public void clickCurrentChargesDueViewDetailsButton() {
		resilientClick(getCurrentChargesDueViewDetailsButton());
	}
	
	public List<String> getCurrentChargesDueTableValues() {
		List<WebElement> elemData = retryElementsUntilPresent(CURRENT_CHARGES_DUE_TABLE);
		return turnWebElementsToText(elemData);
	}
}