package com.sprint.iice_tests.web.pages.cl_digital_paper;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.sprint.iice_tests.web.pages.il_digital_paper.TabBase;

public class SummaryTab extends TabBase implements TabActionsCL {

	private final By OPEN_ROW_ICONS = By.xpath(
			"//*[contains(@class,'ico--collapse')]/parent::*/parent::*/following-sibling::*//*[contains(@class,'row')]/following-sibling::*//*[@class='row']//i");
	private final By TOTAL_IN_OPEN_ROW = By.xpath(
			"//*[contains(@class,'ico--collapse')]/parent::*/parent::*/parent::*//*[contains(@class,'bb-regular-charge-total')]//div/span");
	private final By SUBSCRIBER_NUMBER = By
			.xpath("./parent::*/following-sibling::*//*[contains(@class,'badge-subscriber')]");
	private final By AMOUNT_FROM_DESCRIPTION = By
			.xpath("./parent::*/parent::*/*[contains(@class,'regular-charge-amount')]/span");
	private final By CENTER_VALUE_FROM_DESCRIPTION = By
			.xpath("./parent::*/parent::*/*[contains(@class,'col-lg-6 regular-charge-description')]/span");
	private final By BANNER_ABOVE_EXPAND_BUTTON = By
			.xpath("./parent::*/parent::*/parent::*/parent::*/parent::*/parent::*/div/div/span");
	private final By TABLE_IN_OPEN_ROW = By.xpath(
			"//*[contains(@class,'ico--collapse')]/parent::*/parent::*/following-sibling::*//*[contains(@class,'row')]/following-sibling::*//*[@class='row']");
	private final By AMOUNT_FROM_LEFT_COLUMN = By.xpath("./parent::*/following-sibling::*/span");
	private final By TOP_SUBSCRIBER_BY_USAGE_ROWS = By.cssSelector("bb-subscriber-by-usage .row");
	private final By SUBSCRIBER_NAME_FROM_PHONE_NUMBER = By.xpath("./following-sibling::*");
	private final By GET_DAC_ROW = By.cssSelector(".search-result .data-row");
	private final By ACCOUNT_TABLES = By.cssSelector(".charge-entry");
	private final By AUTOPAY_ICON = By.cssSelector("#Layer_1 title");
	private final By PLANS_BY_SUBSCRIBERS = By.cssSelector(".bb-plan-by-subscriber");
	private final By SUBSCRIBERS_BY_USAGE = By.cssSelector(".bb-subscriber-by-usage");
	private final By PREVIOUS_CHARGES_LABEL = By.cssSelector("#previous-bill .label.light");
	private final By ACTIVITIES_SINCE_LABEL = By.cssSelector("#live-activities .label.light");
	private final By AMOUNT_BY_EXPAND_BUTTON = By.xpath("./parent::*/following-sibling::*/div");
	private final By EXPAND_BUTTONS = By.cssSelector(".bb-accordion-icon > .ico.color--blue");
	private final By PDF_BILL_ICON = By.cssSelector(".bb-pdf-download .medium");
	private final By TABLE_TO_DATA = By.cssSelector(" bb-regular-charges .row .row");
	private final By SUBSCRIBER_ROWS = By.cssSelector("bb-plan-by-subscriber .row");
	private final String BUTTON_WITH_TEXT = "//*[starts-with(@class,'bb-accordion-title')]/*[contains(text(),'%s')]";
	private final String ROW_UNDER_OPEN_BUTTON = "//*[contains(@class,'ico--collapse')]/parent::*/parent::*/parent::*//*[text()='%s']";
	private final String ADJUSTMENT_TITLE = "//*[contains(@class,'adjustment-title')]//*[contains(text(),'%s')]";
	private final String DOWNLOAD_FULL_OVERVIEW = "Download Full Overview";
	private final String VIEW_USAGE_OVERVIEW_LINK = "View usage overview";

	/*
	 * Set 1 Methods
	 */
	public WebElement getPreviousChargesLabel() {
		return retryElementUntilPresent(PREVIOUS_CHARGES_LABEL);
	}

	public WebElement getSavePrintPDFBill() {
		return retryElementUntilPresent(PDF_BILL_ICON);
	}

	public String getAmountFromExpandButtonText(String expandText) {
		return getText(findChildOfParent(getButtonWithText(expandText), AMOUNT_BY_EXPAND_BUTTON));
	}

	public void openExpandButtons(String expandText) {
		List<WebElement> buttons = getExpandButtons();
		clickElementsMatchingCondition(buttons, buttonIsOpen);
		resilientClick(getButtonWithText(expandText));
	};

	/*
	 * Set 2 Methods
	 */
	public WebElement getBlueBannerAboveExpandButton(String buttonText) {
		return getButtonWithText(buttonText).findElement(BANNER_ABOVE_EXPAND_BUTTON);
	}

	public String getBlueBannerAboveExpandButtonText(String buttonText) {
		return getText(getBlueBannerAboveExpandButton(buttonText));
	}

	public WebElement getActivitiesSinceLabel() {
		return retryElementUntilPresent(ACTIVITIES_SINCE_LABEL);
	}

	/*
	 * Page Bottom Methods
	 */
	public WebElement getViewUsageOverviewLink() {
		return retryElementUntilPresent(equals_text(VIEW_USAGE_OVERVIEW_LINK));
	}

	public WebElement getDownloadFullOverview() {
		return retryElementUntilPresent(equals_text(DOWNLOAD_FULL_OVERVIEW));
	}

	public String getSubscriberNameFromTopSubscriberTablePhoneNumber(String phoneNumber) {
		WebElement subscriberTable = retryElementUntilPresent(SUBSCRIBERS_BY_USAGE);
		WebElement phoneRow = findChildOfParent(subscriberTable, top_subscriber_phone_number(phoneNumber));
		return getText(findChildOfParent(phoneRow, SUBSCRIBER_NAME_FROM_PHONE_NUMBER));
	}

	public String getAmountFromTopSubscriberTablePhoneNumber(String phoneNumber) {
		WebElement subscriberTable = retryElementUntilPresent(SUBSCRIBERS_BY_USAGE);
		WebElement phoneRow = findChildOfParent(subscriberTable, top_subscriber_phone_number(phoneNumber));
		return getText(findChildOfParent(phoneRow, AMOUNT_FROM_LEFT_COLUMN));
	}

	public String getAmountFromDescriptionUnderPlansBySubscriber(String description) {
		WebElement subscriberTable = retryElementUntilPresent(PLANS_BY_SUBSCRIBERS);
		WebElement phoneRow = findChildOfParent(subscriberTable, plans_by_subscribers_description(description));
		return getText(findChildOfParent(phoneRow, AMOUNT_FROM_LEFT_COLUMN));
	}

	public String getSubscriberNumberFromDescriptionUnderPlansBySubscriber(String description) {
		WebElement subscriberTable = retryElementUntilPresent(PLANS_BY_SUBSCRIBERS);
		WebElement phoneRow = findChildOfParent(subscriberTable, plans_by_subscribers_description(description));
		return getText(findChildOfParent(phoneRow, SUBSCRIBER_NUMBER));
	}

	public boolean topSubscribersByUsageTableExists() {
		return retryElementUntilPresent(SUBSCRIBERS_BY_USAGE).isDisplayed();
	}

	public boolean topPlansBySubscribersExists() {
		return retryElementUntilPresent(PLANS_BY_SUBSCRIBERS).isDisplayed();
	}

	public boolean subscriberPhoneNumberUnderTopSubscribersTableExists(String phoneNumber) {
		WebElement subscriberTable = retryElementUntilPresent(SUBSCRIBERS_BY_USAGE);
		return findChildOfParent(subscriberTable, top_subscriber_phone_number(phoneNumber)) != null ? true : false;
	}

	public boolean topPlansBySubscribersPhoneNumberExists(String phoneNumber) {
		WebElement subscriberTable = retryElementUntilPresent(PLANS_BY_SUBSCRIBERS);
		return findChildOfParent(subscriberTable, plans_by_subscribers_description(phoneNumber)) != null ? true : false;
	}

	/*
	 * Open Button Methods
	 */
	public String getAmountFromRowDescriptionInOpenButton(String description) {
		WebElement rowDescription = retryElementUntilPresent(row_under_open_button_by_description(description));
		return getText(findChildOfParent(rowDescription, AMOUNT_FROM_DESCRIPTION));
	}

	public String getCenterValueFromRowDescriptionInOpenButton(String description) {
		WebElement rowDescription = retryElementUntilPresent(row_under_open_button_by_description(description));
		return getText(findChildOfParent(rowDescription, CENTER_VALUE_FROM_DESCRIPTION));
	}

	public boolean rowDescriptionExistsInOpenButton(String description) {
		return retryElementUntilPresent(row_under_open_button_by_description(description)) != null ? true : false;
	}

	public String getPhoneFromRowDescription(String description) {
		WebElement rowDescription = retryElementUntilPresent(row_under_open_button_by_description(description));
		return getText(findChildOfParent(rowDescription, FOLLOWING_SIBLING));
	}

	/*
	 * Method turns total charge line, description and amount, into string
	 */
	public String getTotalChargeLineInOpenRow() {
		return String.join(" ",
				getListOfTotalRowElements().stream().map(i -> i.getText()).collect(Collectors.toList()));
	}

	/*
	 * Table methods
	 */

	public String getAmountFromSubHeader(String subheader) {
		return getText(findChildOfParent(getAdjustmentTitle(subheader), FOLLOWING_SIBLING));
	}

	public boolean subheaderInOpenRowExists(String header1) {
		return getAdjustmentTitle(header1) != null ? true : false;
	}

	public List<List<String>> getTableDataWithTitle(String arg1) {
		List<WebElement> tables = getTables();
		int i = getIndexOfEntryWhoseChildrenEquals(tables, arg1);
		List<WebElement> rows = findChildrenOfParent(tables.get(i), TABLE_TO_DATA);
		return createListStringFromRows(rows, DIV);
	}

	public List<List<String>> getTableDataInOpenRow() {
		return createListStringFromRows(tableInOpenRow(), DIV);
	}

	/*
	 * Private methods driving above methods
	 */

	private WebElement getButtonWithText(String expandText) {
		return retryElementUntilPresent(button_with_text(expandText));
	}

	private List<WebElement> getExpandButtons() {
		return resilientRetry(EXPAND_BUTTONS);
	}

	private By button_with_text(String expandText) {
		return By.xpath(String.format(BUTTON_WITH_TEXT, expandText));
	}

	private By row_under_open_button_by_description(String description) {
		return By.xpath(String.format(ROW_UNDER_OPEN_BUTTON, description));
	}

	private By top_subscriber_phone_number(String phoneNumber) {
		return By.xpath(String.format(CHILD_CONTAINS_TEXT, phoneNumber));
	}

	private By plans_by_subscribers_description(String description) {
		return By.xpath(String.format(CHILD_CONTAINS_TEXT, description));
	}

	private List<WebElement> getListOfTotalRowElements() {
		return retryElementsUntilPresent(TOTAL_IN_OPEN_ROW);
	}

	private List<WebElement> tableInOpenRow() {
		return retryElementsUntilPresent(TABLE_IN_OPEN_ROW);
	}

	private List<WebElement> getTables() {
		return retryElementsUntilPresent(ACCOUNT_TABLES);
	}

	private WebElement getAdjustmentTitle(String header1) {
		return retryElementUntilPresent(adjustment_title(header1));
	}

	private By adjustment_title(String header1) {
		return By.xpath(String.format(ADJUSTMENT_TITLE, header1));
	}

	public WebElement getDACRow() {
		return retryElementUntilPresent(GET_DAC_ROW);
	}

	public List<String> getIconsInOpenRow() {
		return getClassValuesOfList(retryElementsUntilPresent(OPEN_ROW_ICONS));
	}
	
	public WebElement getAutoPayIcon() {
		return retryElementUntilPresent(AUTOPAY_ICON);
	}

	public List<List<String>> getPlansBySubscriberTableValues() {
		List<WebElement> rows = getPlansBySubscriberRows();
		return createListStringFromRows(rows, SPAN);
	}

	private List<WebElement> getPlansBySubscriberRows() {
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(SUBSCRIBER_ROWS, 2));
		return retryElementsUntilPresent(SUBSCRIBER_ROWS);
	}

	public List<List<String>> getTopSubscribersByUsage() {
		List<WebElement> rows = getTopSubscribersByUsageRows();
		return createListStringFromRows(rows, SPAN_OR_DIV);
	}

	private List<WebElement> getTopSubscribersByUsageRows() {
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(TOP_SUBSCRIBER_BY_USAGE_ROWS, 2));
		return retryElementsUntilPresent(TOP_SUBSCRIBER_BY_USAGE_ROWS);
	}
	
}
