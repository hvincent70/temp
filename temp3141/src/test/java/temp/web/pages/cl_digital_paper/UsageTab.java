package com.sprint.iice_tests.web.pages.cl_digital_paper;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sprint.iice_tests.web.pages.il_digital_paper.TabBase;

public class UsageTab extends TabBase implements TabActionsCL {

	private final By SUBSCRIBERS_TOGGLE = By.cssSelector(".sprint-toggle label");
	private final By TABLE_IN_OPEN_ROW = By.xpath(
			"//*[contains(@class,'ico--collapse')]/parent::*/parent::*/following-sibling::*//*[contains(@class,'row')]");
	private final By AMOUNT_BY_EXPAND_BUTTON = By.xpath("./parent::*/following-sibling::*/div");
	private final By EXPAND_BUTTONS = By.cssSelector("i.ico.color--blue");
	private final String BUTTON_WITH_TEXT = "//*[starts-with(@class,'bb-accordion-title')]/*[contains(text(),'%s')]";
	// additional for deptanddac
	private final By TOP_SUBSCRIBERS_BY_USAGE_ROWS = By.cssSelector(".subscriber .row");
	private final By PLANS_BY_SUBSCRIBER_ROWS = By.cssSelector(".plan .row");
	private final By EXPAND_BUTTON_TOTAL = By.cssSelector("bb-total span");
	private final By LABEL_BLOCK = By.cssSelector(".label-block .label");
	private final By EXPAND_BUTTON_ROWS = By.cssSelector(".bb-accordion-row");
	private final By TOTAL_ROW_CHARGES_AND_USAGE = By.cssSelector("#bb-usage-table .row.content");
	private final By USAGE_DEPARTMENT_PAGE_NUMBER = By.cssSelector("#bb-usage-table .bb-pagination .next");
	private final By USAGE_DEPARTMENT_LINE_NAME = By.cssSelector(
			"#bb-usage-table > div:nth-child(4) > div.col-xs-11.pt-20.pr-10.pb-20.pl-10.middle-xs.ng-scope.firstColumn > div > span.title.ng-binding.ng-scope");
	private final By USAGE_DAC_LINE_NAME = By.cssSelector("#bb-usage-table .row.content .col-xs-11 .text-left .block");
	private final By GRPAH_DATES = By.cssSelector(".bb-graph-date");
	private final By HISTORIC_GRAPH = By.cssSelector(".bb-historic-graph");
	private final By GRPAH_AMOUNTS = By.cssSelector(".bb-graph-amount");

	public List<List<String>> getTableDataInOpenRow() {
		return createListStringFromRows(tableInOpenRow(), DIV);
	}

	public String getAmountFromExpandButtonText(String expandText) {
		return getText(findChildOfParent(getButtonWithText(expandText), AMOUNT_BY_EXPAND_BUTTON));
	}

	@Override
	public void openExpandButtons(String expandText) {
		List<WebElement> buttons = getExpandButtons();
		clickElementsMatchingCondition(buttons, buttonIsOpen);
		resilientClick(getButtonWithText(expandText));
	}

	private List<WebElement> getExpandButtons() {
		return resilientRetry(EXPAND_BUTTONS);
	}

	private WebElement getButtonWithText(String expandText) {
		return retryElementUntilPresent(button_with_text(expandText));
	}

	private By button_with_text(String expandText) {
		return By.xpath(String.format(BUTTON_WITH_TEXT, expandText));
	}

	private List<WebElement> tableInOpenRow() {
		return retryElementsUntilPresent(TABLE_IN_OPEN_ROW);
	}

	public WebElement getViewAllSubscibersToggle() {
		return retryElementUntilPresent(SUBSCRIBERS_TOGGLE);
	}

	public WebElement getTotalRowChargesAndUsage() {

		return retryElementUntilPresent(TOTAL_ROW_CHARGES_AND_USAGE);

	}

	// created
	public List<List<String>> getTopSubscribersByUsageData() {
		List<WebElement> rows = retryElementsUntilPresent(TOP_SUBSCRIBERS_BY_USAGE_ROWS);
		return createListStringFromRows(rows, SPAN_OR_DIV);
	}

	// created
	public List<List<String>> getPlansBySubscribersData() {
		List<WebElement> rows = retryElementsUntilPresent(PLANS_BY_SUBSCRIBER_ROWS);
		return createListStringFromRows(rows, SPAN);
	}

	// created
	public String getExpandButtonTotals() {
		List<String> list = turnWebElementsToText(retryElementsUntilPresent(EXPAND_BUTTON_TOTAL));
		return String.join(" ", list);
	}

	// created
	public String getLabelText() {
		return getText(retryElementUntilPresent(LABEL_BLOCK));
	}

	// created
	public List<List<String>> getExpandButtonDetailsAndValues() {
		List<WebElement> rows = retryElementsUntilPresent(EXPAND_BUTTON_ROWS);
		return createListStringFromRows(rows, DIV);
	}

	// created
	public WebElement getUsageDepartmentPageNumber() {

		return retryElementUntilPresent(USAGE_DEPARTMENT_PAGE_NUMBER);

	}

	// created
	public WebElement getUsageDepartmentLineName() {

		return retryElementUntilPresent(USAGE_DEPARTMENT_LINE_NAME);

	}

	// created
	public WebElement getUsageDacLineName() {

		return retryElementUntilPresent(USAGE_DAC_LINE_NAME);

	}

	// created
	public WebElement graphsInOpenButton() {
		return getDisplayedElement(retryElementsUntilPresent(HISTORIC_GRAPH));
	}

	// created
	public List<String> getGraphDatesInOpenRow() {
		return turnWebElementsToText(graphsInOpenButton().findElements(GRPAH_DATES));
	}

	// created
	public List<String> getGraphAmountsInOpenRow() {
		return turnWebElementsToTextKeepDuplicates(graphsInOpenButton().findElements(GRPAH_AMOUNTS));
	}

}
