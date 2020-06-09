package com.sprint.iice_tests.web.pages.cl_digital_paper;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sprint.iice_tests.web.pages.il_digital_paper.TabBase;

public class CallLogsTab extends TabBase implements TabActionsCL {

	private final String DATE_FILTER_INSTANCE = "//bb-date-filter//span[contains(text(),'%s')]//parent::*//parent::*//span[contains(text(),'%s')]";
	public final String ARROW_NEXT_TO_COLUMN = "//*[contains(@class,'header')]//*[contains(text(),'%s')]//following-sibling::*";
	private final String RADIO_BUTTON_WITH_TEXT = "//label//*[contains(text(),'%s')]";
	private final String BUTTON_WITH_TEXT = "//*[starts-with(@class,'bb-accordion-title')]/*[contains(text(),'%s')]/parent::*/parent::*//i";
	public final By TOTAL_COST = By.cssSelector(".total-cost span:nth-of-type(2)");
	public final By TOTAL_MINUTES = By.cssSelector(".total-min span:nth-of-type(2)");
	private final By PHONE_NUMBER_ENTRY_FIELD = By.cssSelector(".bb-calls .bb-input");
	private final By COLUMN_HEADINGS = By.cssSelector(".bb-row.header .header-label");
	private final By CALL_LOGS_INPUT_FIELD = By.cssSelector("bb-calls .bb-input");
	private final By FILTER_OPTIONS = By.cssSelector(".bb-call-options");
	private final By EXPAND_BUTTONS = By.cssSelector(".bb-accordion-icon i");
	private final By UPDATE_FIELD = By.cssSelector("button.update-filter-btn");
	private final By TABLE_ROWS = By.cssSelector("#scrollable .bb-row.data-row");
	public Function<String, Boolean> totalMinutesChanges = s -> !getTotalMinutes().equals(s);
	public BiFunction<String, String, Boolean> totalMinutesAndCostChanges = (s, t) -> !getTotalMinutes().equals(s) && !getTotalCost().equals(t);

	public void enterPhoneNumber(String phoneNumber) {
		sendTextToElement(getPhoneNumberEntryField(), phoneNumber);
	}

	private WebElement getPhoneNumberEntryField() {
		return retryElementUntilPresent(PHONE_NUMBER_ENTRY_FIELD);
	}

	public String getTotalMinutes() {
		return retryElementUntilPresent(TOTAL_MINUTES).getText();
	}

	public String getTotalCost() {
		return retryElementUntilPresent(TOTAL_COST).getText();
	}

	/**
	 * setColumnDirection(String column, String direction) sorts call logs according
	 * to designated column and designated direction
	 * 
	 * @param column
	 *            the column the user wants to sort by
	 * @param direction
	 *            the direction the user wants the items in the column sorted by,
	 *            either "asc" or "dsc"
	 */
	public void setColumnDirection(String column, String direction) {
		if (direction.equalsIgnoreCase("asc")) {

		}
	}

	public WebElement getRadioButtonWithText(String button) {
		return retryElementUntilPresent(radio_button_with_text(button));
	}

	private By radio_button_with_text(String button) {
		return By.xpath(String.format(RADIO_BUTTON_WITH_TEXT, button));
	}

	public List<String> getColumnHeadings() {
		return turnWebElementsToText(retryElementsUntilPresent(COLUMN_HEADINGS));
	}
/*
	public WebElement arrowNextToColumn(String column) {
		return retryElementUntilPresent(arrow_next_to_column(column));
	}

	private By arrow_next_to_column(String column) {
		return By.xpath(String.format(ARROW_NEXT_TO_COLUMN, column));
	}
*/
	public void enterStringToInputField(String entry) {
		sendTextToElement(getInputField(), entry);
	}

	private WebElement getInputField() {
		return retryElementUntilPresent(CALL_LOGS_INPUT_FIELD);
	}

	public WebElement getFilterOptions() {
		return retryElementUntilPresent(FILTER_OPTIONS);
	}

	@Override
	public void openExpandButtons(String headingText) {
		List<WebElement> buttons = getExpandButtons();
		clickElementsMatchingConditionHandleStale(buttons, buttonIsOpen);
		clickUntilConditionIsMet(getButtonWithText(headingText), buttonElementIsOpen, getButtonWithText(headingText));
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

	public WebElement dateFilterInstance(String monthRange, String dateRange) {
		return retryElementUntilPresent(date_filter_instance(monthRange, dateRange));
	}

	private By date_filter_instance(String monthRange, String dateRange) {
		Object[] args = { monthRange, dateRange };
		return By.xpath(String.format(DATE_FILTER_INSTANCE, args));
	}

	public WebElement getUpDateField() {
		return retryElementUntilPresent(UPDATE_FIELD);
	}
	
	public List<List<String>> getTableValues() {
		List<WebElement> rows = retryElementsUntilPresent(TABLE_ROWS);
		return createListStringFromRows(rows, SPAN);
	}

}
