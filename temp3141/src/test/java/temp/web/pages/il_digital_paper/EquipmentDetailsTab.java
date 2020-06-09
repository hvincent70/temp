package com.sprint.iice_tests.web.pages.il_digital_paper;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class EquipmentDetailsTab extends TabBase implements TabActions {
	private static final String ROW_TEXT_TAIL = ".text-center > span";
	private static final String EXPAND_BUTTON_TAIL = ".first-md.equipment-text > i";
	private static final By ROW_MESSAGES = By.cssSelector(".equipment-element-message");
	private static final String LEFT_TEXT_TAIL = ".text-left > a > span";
	private static final String SECTION_PHONE = "div[id*=\"%s\"]";
	private static final By DESCRIPTIONS_UNDER_PHONE_NUMBER = By
			.cssSelector("div > div .equipment-element-body > div > .row > .text-left > span");
	private static final By AMOUNTS_UNDER_PHONE_NUMBER = By
			.cssSelector("div > div .equipment-element-body > div > .row > .text-right > span");
	private static final By AMOUNT_FROM_DESCRIPTION = By.xpath(".//parent::*/following-sibling::*/span");
	private static final String EQUIPMENT_ELEMENT_SECOND_MESSAGE = "[id^=\"%s\"] > div:nth-child(3) > div > div > div.equipment-element-message > div > div";
	//orig path
	//private static final String PHONE_TO_DESCRIPTION = "//span[contains(text(),\"%s\")]";
	
	private static final String PHONE_TO_DESCRIPTION = "//*[@id=\"1199273796_GIC-63863925\"]/div[3]/div/div/div[2]/div[1]/span";
	
	
//	private static final String PHONE_TO_DESCRIPTION = ".row.equipment-element-body-row .col-xs-12-.col-md-12.text-left > span";
	
	
	//private static final String PHONE_TO_DESCRIPTION = "[id*=\"%s\"]/div[3]/div/div/div[2]/div[1]";
	
	//private static final By PHONE_TO_DESCRIPTION = By.xpath("//*[@id=\"1199273796_GIC-63863925\"]/div[3]/div/div/div[2]/div[1]/span");
	private static final String EXPAND_BUTTON_FROM_EQUIPMENT = "//*[text()='%s']/parent::*/parent::*//i";
	// Main selector string to get to any table value
	private final String CSS_VALUE_PATH = "[id*=\"%s\"] > div:nth-child(3) > div > div > div > div.col-xs-12.col-md-12.text-%s > span";
	private final String REMAINING_PAYMENT = "[id*=\"%s\"]  > div:nth-child(3) > div > div > div:nth-child(6) > div.col-xs-12.col-md-12.text-right > span";
	private final String PAYOFF_AMOUNT = "[id*=\"%s\"] > div:nth-child(3) > div > div > div:nth-child(7) > div.col-xs-12.col-md-12.text-right > span";
	private final String EQUIPMENT_ELEMENT_MESSAGE = "[id^=\"%s\"] > div:nth-child(3) > div > div > div.equipment-element-message > div:nth-child(1) > div";

	// By's for public methods
	private final By FINAL_PAYMENT = By
			.cssSelector(".row.equipment-element-body-row[ng-if*='isIbFinalPayment'] > div:nth-of-type(2) > span");
	private final By HEADER = By.cssSelector(".equipments >.title-row > div > span");
	private final By VIEW_PDF = By.cssSelector(".bb-pdf-download");
	private final By EXPAND_BUTTONS = By.cssSelector(".first-md.equipment-text > i");
	private final By EQUIPMENT_DETAIL_ROWS = By.cssSelector(".equipment-row > .hidden-md > .row");
	private final By PHONE_NUMBERS = By.cssSelector(".equipment-text.text-center > span");
	private final By ID = By.cssSelector("[ng-repeat='bill_item in equipments.billItemsFromEquipments'] > div");
	private final By TABLE_IN_OPEN_ROW = By.xpath(
			"//*[contains(@class,'ico--collapse')]/parent::*/parent::*/following-sibling::*//*[contains(@class,'row content')]");
	private static final By EQUIPMENT_ELEMENT_MESSAGE_IN_OPEN_ROW = By.xpath(
			"//*[contains(@class,'ico--collapse')]/parent::*/parent::*/parent::*/parent::*//div[contains(@class, 'equipment-element-message')]");

	// private final By restoreMessageLoc =
	// By.cssSelector(".equipment-element-message");
	private final By customerNameLoc = By.xpath(".//div[@class='col-md-5 first-md equipment-text text-left']//a");
	private final By ptnLoc = By.xpath(".//div[@class='col-md-4 title equipment-text text-center']//span");
	private final By equipIconLoc = By.xpath(".//div[@class ='col-md-2 equipment-element-image']//img");
	private final By equipNameLoc = By.xpath(".//div[@class ='col-md-6 equipment-text text-right']//span");

	/**
	 * Main Header, text is "Equipment Details"
	 */
	public WebElement getHeader() {
		return retryElementUntilVisible(HEADER);
	}

	public WebElement getViewPDFLink() {
		return retryElementUntilVisible(VIEW_PDF);
	}

	public WebElement equipDetails(String ID) {
		return elementWithID(ID);
	}

	public boolean equipDetailsIsVisible(String ID) {
		return equipDetails(ID).isDisplayed();
	}

	/**
	 * Returns a list of expand buttons for each phone number. An individual number
	 * can be selected based on the ordinal position using a List's .get(int)
	 * method, and can be selected based on phonenumber or subscriber name using
	 * this object's phonePosition or subscriber position ex.)
	 * getExpandButtons().get(phonePosition("(169) 736-6983"))
	 */
	public List<WebElement> getExpandButtons() {
		return retryElementsUntilPresent(EXPAND_BUTTONS);
	}

	public List<WebElement> getEquipmentDetailRows() {
		return retryAllElementsUntilVisible(EQUIPMENT_DETAIL_ROWS);
	}

	public List<List<String>> getEquipmentDetailTableRows() {
		List<WebElement> rows = getTableRows();
		return createListStringFromRows(rows, SPAN);
	}

	private List<WebElement> getTableRows() {
		return retryElementsUntilPresent(By.cssSelector(".row.equipment-element-body-row"));
	}

	/**
	 * Returns a list of expand buttons for each phone number. An individual number
	 * can be selected based on the ordinal position using a List's .get(int)
	 * method, and can be selected based on phonenumber using this object's
	 * phonePosition ex.) getExpandButtons().get(phonePosition("(169) 736-6983"))
	 */
	public int phonePosition(String phoneNumber) {
		List<WebElement> entries = getPhoneNumbers();
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
	public List<WebElement> getPhoneNumbers() {
		return retryAllElementsUntilVisible(PHONE_NUMBERS);
	}

	/**
	 * Returns the final payment element in the open tab
	 */
	public WebElement getFinalPayment(int order) {
		return retryElementUntilVisible(FINAL_PAYMENT);
	}

	private By remaining_payment(String id) {
		return By.cssSelector(String.format(REMAINING_PAYMENT, id));
	}

	/**
	 * Returns the remaining payment element in the open tab
	 */
	public WebElement getRemainingPayments(int order) {
		String[] id = getElementAttribute(getID().get(order), "id").split("_");
		return retryElementUntilVisible(remaining_payment(id[1]));
	}

	/**
	 * Returns the remaining payoff amount in the open tab
	 */
	public WebElement getPayoffAmount(int order) {
		String[] id = getElementAttribute(getID().get(order), "id").split("_");
		return wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector(String.format(PAYOFF_AMOUNT, id[1]))));
	}

	private By equipment_element_message() {
		String ID = getOpenRowPhoneNumber().replaceAll("[^\\d.]", "");
		if (ID.equals("")) {
			ID = "_";
		}
		return By.cssSelector(String.format(EQUIPMENT_ELEMENT_MESSAGE, ID));
	}

	/**
	 * When expand buttons are clicked, below the table of descriptions and amounts
	 * is a subrow that can contain a message. This message is used to get this
	 * message.
	 */
	public String getEquipmentElementMessage() {
		return retryElementUntilVisible(equipment_element_message()).getText();
	}

	public String getSecondEquipmentElementMessage() {
		return retryElementsUntilPresent(equipment_element_multiple_message()).get(1).getText();
	}

	private By equipment_element_multiple_message() {
		String ID = getOpenRowPhoneNumber().replaceAll("[^\\d.]", "");
		if (ID.equals("")) {
			ID = "_";
		}
		return By.cssSelector(String.format(EQUIPMENT_ELEMENT_SECOND_MESSAGE, ID));
	}

	public List<WebElement> getID() {
		return retryAllElementsUntilVisible(ID);
	}

	@Override
	/**
	 * Method returns the expand button defined by a phone number on the given row
	 */
	public void openExpandButtons(String expandText) throws InterruptedException {
		int index = getIndexOfParentElement(expandText, getEquipmentDetailRows(), ROW_TEXT_TAIL);
		cleanExpandButtonFromParent(getEquipmentDetailRows(), index, EXPAND_BUTTON_TAIL);
	}

	public void openExpandButtonsBasedOnName(String expandText) throws InterruptedException {
		int index = getIndexOfParentElement(expandText, getEquipmentDetailRows(), LEFT_TEXT_TAIL);
		cleanExpandButtonFromParent(getEquipmentDetailRows(), index, EXPAND_BUTTON_TAIL);
	}

	public WebElement getRowMessage(String message) {
		return getElementWithTextFromList(message, rowMessages());
	}

	private List<WebElement> rowMessages() {
		return retryElementsUntilPresent(ROW_MESSAGES);
	}

	private By table_values(String phoneNum, String side) {
		// System.out.println(phoneNum + ", " + phoneNum.replaceAll("[^\\d.]", ""));
		phoneNum = phoneNum.replaceAll("[^\\d.]", "");
		Object[] args = { phoneNum, side };
		return By.cssSelector(formatPath(CSS_VALUE_PATH, args));
	}

	private List<WebElement> tableDescriptions(String phoneNum) {
		return retryElementsUntilPresent(table_values(phoneNum, "left"));
	}

	private List<WebElement> tableAmounts(String phoneNum) {
		return retryElementsUntilPresent(table_values(phoneNum, "right"));
	}

	private String getRightColValueBasedOnLeftCol(String phoneNum, String description) {
		int index = getIndexOfElementWithTextFromList(description, tableDescriptions(phoneNum));
		return tableAmounts(phoneNum).get(index).getText();
	}

	/*
	 * private String getRightColValueBasedOnLeftColContainsText(String phoneNum,
	 * String description) { int index =
	 * getIndexOfElementContainingTextFromList(description,
	 * tableDescriptions(phoneNum)); return
	 * tableAmounts(phoneNum).get(index).getText(); }
	 * 
	 */ public String getFinalPaymentAmount() {
		String ID = getOpenRowPhoneNumber();
		return this.getRightColValueBasedOnLeftCol(ID, "Final Payment Due");
	}

	public String getContractNumber() {
		String ID = getOpenRowPhoneNumber();
		return getRightColValueBasedOnLeftCol(ID, "Installment Contract");
	}

	public String getContractNumber(String phoneNum) {
		String ID = phoneNum.replaceAll("[^\\d.]", "");
		return this.getRightColValueBasedOnLeftCol(ID, "Installment Contract");
	}

	/*
	 * Four methods to be used by step definition runner to call out individual
	 * components from designated row header
	 */
	public String getOpenRowName() {
		int index = getIndexOfOpenRow();
		String NAME_TAIL = "div > a";
		String NAME_TAIL_MOD = "div > a > span";
		try {
			return getEquipmentDetailRows().get(index).findElement(By.cssSelector(NAME_TAIL)).getText();
		} catch (Exception e) {
			return getEquipmentDetailRows().get(index).findElement(By.cssSelector(NAME_TAIL_MOD)).getText();
		}
	}

	public String getOpenRowPhoneNumber() {
		int index = getIndexOfOpenRow();
		String PHONE_TAIL = ".text-center > span";
		// System.out.println(getEquipmentDetailRows().get(index).findElement(By.cssSelector(PHONE_TAIL)).getText());
		return getEquipmentDetailRows().get(index).findElement(By.cssSelector(PHONE_TAIL)).getText();
	}

	By leaseDescLoc = By.xpath(".//div[contains (@class , '12 text-left')]");

	By leaseDescAmount = By.xpath(".//div[contains (@class , '12 text-right')]");

	public List<WebElement> leaseDesc(String ID) {
		return elementWithID(ID).findElements(leaseDescLoc);
	}

	public List<WebElement> leaseDescAmount(String ID) {
		return elementWithID(ID).findElements(leaseDescAmount);
	}

	public String tableOfLeaseContractDetails(String ID, String key) throws Exception {
		Map<String, String> table = createTableOfStringsFromWebElements(leaseDesc(ID), leaseDescAmount(ID));
		System.out.println(table);
		return table.get(key);
	}

	public WebElement equipNameWithSpecificID(String ID) {
		WebElement el = tryWebElement(elementWithID(ID), equipNameLoc);
		return el;
	}

	public WebElement customerWithSpecificID(String ID) {
		WebElement el = tryWebElement(elementWithID(ID), customerNameLoc);
		return el;
	}

	public String customerTextWithSpecificID(String ID) {
		String t = null;
		try {
			t = customerWithSpecificID(ID).getText();
		} catch (NoSuchElementException ex) {
		} catch (NullPointerException ex) {
		}
		return t;
	}

	public WebElement ptnWithSpecificID(String ID) {
		WebElement el = tryWebElement(elementWithID(ID), ptnLoc);
		return el;
	}

	public String ptnTextWithSpecificID(String ID) {
		String t = null;
		try {
			t = ptnWithSpecificID(ID).getText();
		} catch (NoSuchElementException ex) {
		} catch (NullPointerException ex) {
		}
		return t;
	}

	public WebElement equipIconWithSpecificID(String ID) {
		WebElement el = tryWebElement(elementWithID(ID), equipIconLoc);
		return el;
	}

	public String equipIconSrcTextWithSpecificID(String ID) {
		String srcText = equipIconWithSpecificID(ID).getAttribute("src").toString();
		return srcText;
	}

	public String IMGequipIconSrcTextWithSpecificID(String ID) {
		String srcText = equipIconWithSpecificID(ID).getAttribute("img").toString();
		return srcText;
	}

	public String equipNameTextWithSpecificID(String ID) {
		return textOfElement(equipNameWithSpecificID(ID));
	}

	public Boolean getOpenRowImage() {
		int index = getIndexOfOpenRow();
		String IMAGE_TAIL = "div > img";
		return getEquipmentDetailRows().get(index).findElement(By.cssSelector(IMAGE_TAIL)) != null;
	}

	public String getOpenRowEquipment() {
		int index = getIndexOfOpenRow();
		String EQUIP_TAIL = ".text-right > span";
		return getEquipmentDetailRows().get(index).findElement(By.cssSelector(EQUIP_TAIL)).getText();
	}

	public Boolean docLinkIsActive() {
		int index = getIndexOfOpenRow();
		int indexmod = getIndexOfOpenRow() + (int) Math.floor(index / 2);
		return getMyDocumentsLink().get(indexmod).isDisplayed();
	}

	private List<WebElement> getMyDocumentsLink() {
		By DOCUMENT_LINKS = By.cssSelector("#lnkInstallmentMyDocuments");
		return retryElementsUntilPresent(DOCUMENT_LINKS);
	}

	private int getIndexOfOpenRow() {
		List<WebElement> list = getExpandButtons();
		System.out.println(list.size());
		for (WebElement item : list) {
			if (item.getAttribute("class").contains("collapse")) {
				return list.indexOf(item);
			}
		}
		return 0;
	}

	public List<WebElement> listOfMyDocs(String ID) {
		System.out.println("THE mydoc links ELEMENTs are: "
				+ elementWithID(ID).findElements(myDocById("lnkInstallmentMyDocuments")));
		return elementWithID(ID).findElements(myDocById("lnkInstallmentMyDocuments"));
	}

	By myDocById(String idLoc) {
		return By.xpath(".//*[@id='" + idLoc + "']");
	}

	public WebElement myDoc(String ID) {

		for (WebElement e : listOfMyDocs(ID)) {
			if (elementIsNotHidden(e)) {
				System.out.println("THE unhidden element is: " + e);
				return e;

			} else {
				int hiddenElementLoc = listOfMyDocs(ID).indexOf(e);
				listOfMyDocs(ID).remove(hiddenElementLoc);
			}
		}
		return listOfMyDocs(ID).get(0);
	}

	public Boolean elementIsNotHidden(WebElement e) {
		return !e.getAttribute("class").contains("hidden-xs");
	}

	public Boolean elementIsHidden(WebElement e) {
		return e.getAttribute("class").contains("hidden-xs");
	}

	public Boolean myDocLinkIsEnabled(String ID) {
		System.out.println("mydoc link is displayed??? " + myDoc(ID));
		return myDoc(ID).isEnabled();
	}

	public List<WebElement> equipmentMessages() {
		return retryAllElementsUntilVisible(equipment_element_message());
	}

	public WebElement getEquipmentElementMessageContainingText(String messageText) {
		int index = getIndexOfElementContainingTextFromList(messageText, equipmentMessages());
		return equipmentMessages().get(index);
	}

	public Map<String, String> createTableFromPhoneNumber(String phone) {
		phone = phone.replaceAll("[^\\d.]", "");
		return createTableOfStringsFromWebElements(listOfDescriptions(phone), listOfAmounts(phone));
	}

	private List<WebElement> listOfAmounts(String phone) {
		return sectionByPhone(phone).findElements(AMOUNTS_UNDER_PHONE_NUMBER);
	}

	public List<WebElement> listOfDescriptions(String phone) {
		return sectionByPhone(phone).findElements(DESCRIPTIONS_UNDER_PHONE_NUMBER);
	}

	private WebElement sectionByPhone(String phone) {
		return retryElementUntilPresent(section_phone(phone));
	}

	private By section_phone(String phone) {
		return By.cssSelector(String.format(SECTION_PHONE, phone));
	}

	public Boolean lineUnderNumberExists(String description, String phoneNumber) {
		phoneNumber = phoneNumber.replaceAll("[^\\d.]", "");
		return descriptionUnderPhone(phoneNumber, description) == null ? false : true;
	}

	private WebElement descriptionUnderPhone(String phoneNumber, String description) {
		return retryElementUntilPresent(phone_root(phoneNumber)).findElement(phone_to_description(description));
	}

	private By phone_to_description(String description) {
		String loc = String.format(PHONE_TO_DESCRIPTION, description);
		return By.xpath(loc);
	}

	public String getChargeAmountWithPhoneAndDescription(String description, String phoneNumber) {
		phoneNumber = phoneNumber.replaceAll("[^\\d.]", "");
		return descriptionUnderPhone(phoneNumber, description).findElement(AMOUNT_FROM_DESCRIPTION).getText();
	}

	private By phone_root(String phoneNumber) {
		String PHONE_NUMBER_LOC = "[id^=\"%s\"]";
		String loc = String.format(PHONE_NUMBER_LOC, phoneNumber);
		return By.cssSelector(loc);
	}

	private WebElement getTableInOpenRow() {
		return retryElementUntilPresent(TABLE_IN_OPEN_ROW);
	}

	public boolean tableInOpenRowContainsText(String text) {
		try {
			getTableInOpenRow().findElement(contains_text(text));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public WebElement expandButtonFromEquipment(String ptnId) {
		return retryElementUntilPresent(expand_button_from_equipment(ptnId));
	}

	private By expand_button_from_equipment(String ptnId) {
		return By.xpath(String.format(EXPAND_BUTTON_FROM_EQUIPMENT, ptnId));
	}

	public String getEquipmentElementMessageInOpenRow() {
		String s = retryElementUntilPresent(EQUIPMENT_ELEMENT_MESSAGE_IN_OPEN_ROW).getText();
		return s.replace("\n", " ").replace("\r", " ");
	}

	/**
	 * This method works by grabbing a list of the webelements that are messages in the 
	 * open row. It then gets the text from those elements, and searches for the desired text
	 * in the resulting list, if the string is not found the ArrayList.findIndexOf() method returns
	 * -1.  In this way, you avoid waiting for an element that might not be 
	 * there, avoid passing a string to code just to get the string back, and 
	 * reducing execution time and possible exceptions
	 */
	public boolean equipmentMessageExists(String msg) {
		List<WebElement> elements = equipmentMessages();
		List<String> list = turnWebElementsToText(elements);
		return list.indexOf(msg) != -1;
	}

}
