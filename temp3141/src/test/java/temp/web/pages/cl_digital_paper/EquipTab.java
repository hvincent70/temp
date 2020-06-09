package com.sprint.iice_tests.web.pages.cl_digital_paper;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sprint.iice_tests.web.pages.il_digital_paper.TabBase;

public class EquipTab extends TabBase implements TabActionsCL {

	private final By TABLE_IN_OPEN_ROW = By.xpath(
			"//*[contains(@class,'ico--collapse')]/parent::*/parent::*/following-sibling::*//*[contains(@class,'row content')]");
	private final By ACCOUNT_CHARGE_TOTAL = By
			.cssSelector(".account-charges bb-charges-table:nth-of-type(1) .is-total-label span");
	private final By ROW_HEADER = By.cssSelector(".row.header span");
	private final By EXCLAMATION_POINTS = By.cssSelector(".ico--incorrect");
	private final By TABLE_ROWS = By.cssSelector("bb-equipment-details .row.element");
	private final By FILTER_FIELD_IN_EXPAND_BUTTON = By.cssSelector("#bb-equipments-table .bb-input");
	private final By TABLE_ROW = By.cssSelector("#bb-equipments-details-purchases-table .row");
	private final By TABLE_HEADERS = By.cssSelector(".tableHeaders span");
	private final By COL_HEADING = By.cssSelector(".headerCol span,strong");
	private final By ROW_VALUES = By.cssSelector(".dataCol span:nth-child(1)");
	private final By CONTENT_ROW = By.cssSelector(".row.content");
	private final By EXPAND_BUTTONS = By.cssSelector(".bb-accordion-icon");
	private final By DEVICE_ICON_LOCATION = By.cssSelector("img.device-image");
	private final By FILTER_FIELD_OUTSIDE_EXPAND_BUTTON = By.cssSelector(".show-desktop input.bb-input");
	private final String BUTTON_WITH_TEXT = "//*[starts-with(@class,'bb-accordion-title')]/*[contains(text(),'%s')]";
	private final String EQUIP_ROW_DEVICE = "//*[contains(@class,'device')]//*[contains(text(),'%s')]";
	private final String EQUIPMENT_TABLE_BY_TITLE = "//*[contains(text(),'%s')]/ancestor::*[@id='bb-equipments-table']";
	private final String FIRST_ROW_EQUIPMENT_TABLE_BY_TITLE = EQUIPMENT_TABLE_BY_TITLE + "/child::*/following-sibling::div[2]";
	private final By EQUIPMENT_TABLE_ORDER_DETAILS = By.xpath("//*[@id='bb-equipments-details-purchases-table']");

	public void openExpandButtons(String headingText) {
		List<WebElement> buttons = getExpandButtons();
		clickElementsMatchingCondition(buttons, buttonIsOpen);
		resilientClick(getButtonWithText(headingText));
	}

	private List<WebElement> getExpandButtons() {
		return resilientRetry(EXPAND_BUTTONS);
	}

	private WebElement getButtonWithText(String headingText) {
		return retryElementUntilPresent(button_with_text(headingText));
	}

	private By button_with_text(String headingText) {
		return By.xpath(String.format(BUTTON_WITH_TEXT, headingText));
	}

	public WebElement detailPurchaseRow() {
		return retryElementUntilPresent(CONTENT_ROW);
	}

	public List<String> colHeadings() {
		return turnWebElementsToText(retryElementsUntilPresent(COL_HEADING));
	}

	public List<String> rowValues() {
		return turnWebElementsToText(retryElementsUntilPresent(ROW_VALUES));
	}

	public List<String> tableHeaders() {
		return turnWebElementsToText(retryElementsUntilPresent(TABLE_HEADERS));
	}

	public List<List<String>> equipmentDetailsAndPurchesTableValues() {
		List<WebElement> rows = equipmentDetailsAndPurchesTableValuesRows();
		return createListStringFromRows(rows, DIV_SPAN);
	}

	private List<WebElement> equipmentDetailsAndPurchesTableValuesRows() {
		return retryElementsUntilPresent(TABLE_ROW);
	}

	/*
	 * Newest, unordered methods
	 */
	public void enterPhoneInFilterFieldInExpandButton(String phone) {
		sendTextToElement(filterFieldInExpandButton(), phone);
	}

	public void enterPhoneInFilterFieldOutsideExpandButton(String phone) {
		sendTextToElement(filterFieldOutsideExpandButton(), phone);
	}

	private WebElement filterFieldOutsideExpandButton() {
		return retryElementUntilPresent(FILTER_FIELD_OUTSIDE_EXPAND_BUTTON);
	}

	private WebElement filterFieldInExpandButton() {
		return retryElementUntilPresent(FILTER_FIELD_IN_EXPAND_BUTTON);
	}

	public WebElement equipmentRowWithDevice(String disclaimer1) {
		return retryElementUntilPresent(equip_row_with_device(disclaimer1));
	}

	private By equip_row_with_device(String disclaimer1) {
		return By.xpath(String.format(EQUIP_ROW_DEVICE, disclaimer1));
	}
	
	public List<List<String>> getEquipmentTableDataInRow(String title) {
		List<WebElement> rows = equipmentsTableWithTitle(title);
		return createListStringFromRows(rows, SPAN);
	}
	
	public List<WebElement> equipmentsTableWithTitle(String title) {
		return retryElementsUntilPresent(byEquipmentsTableWithTitle(title));
	}
	
	private By byEquipmentsTableWithTitle(String title) {
		return By.xpath(String.format(EQUIPMENT_TABLE_BY_TITLE, title));
	}
	
	private By byFirstRowEquipmentTableWithTitle(String title) {
		return By.xpath(String.format(FIRST_ROW_EQUIPMENT_TABLE_BY_TITLE, title));
	}
	
	public WebElement getEquipmentTableDataFirstRow(String title) {
		return retryElementUntilPresent(byFirstRowEquipmentTableWithTitle(title));
	}
	
	public void clickEquipmentTableDataFirstRow(String title) {
		resilientClick(getEquipmentTableDataFirstRow(title));
	}
	
	public List<List<String>> getEquipmentTableOrderDetailsInRow() {
		List<WebElement> rows = equipmentsTableOrderDetails();
		return createListStringFromRows(rows, SPAN);
	}
	
	public List<WebElement> equipmentsTableOrderDetails() {
		return retryElementsUntilPresent(EQUIPMENT_TABLE_ORDER_DETAILS);
	}


	public WebElement getAccountChargeTotal() {
		return retryElementUntilPresent(ACCOUNT_CHARGE_TOTAL);
	}

	public List<List<String>> getTableDataInOpenRow() {
		List<WebElement> rows = getTableInOpenRowRows();
		return createListStringFromRows(rows, SPAN);
	}

	private List<WebElement> getTableInOpenRowRows() {
		return retryElementsUntilPresent(TABLE_IN_OPEN_ROW);
	}

	private List<WebElement> getTableRows() {
		return retryElementsUntilPresent(TABLE_ROWS);
	}

	public List<WebElement> getExclamationPoints() {
		return retryElementsUntilPresent(EXCLAMATION_POINTS);
	}
	
	public int getNumberOfExclamationPoints() {
		try {
			return getExclamationPoints().size();
		} catch(Exception e) {
			return 0;
		}
	}

	public List<String> dataTableHeader() {
		return turnWebElementsToText(retryElementsUntilPresent(ROW_HEADER));
	}

	public List<List<String>> getEquipDetails() {
		List<WebElement> rows = getTableRows();
		return createListStringFromRows(rows, SPAN);
	}

	public boolean deviceIconIsPresent() {
		return getDeviceIcon() != null ? true : false;
	}

	private WebElement getDeviceIcon() {
		return retryElementUntilPresent(DEVICE_ICON_LOCATION);
	}

}
