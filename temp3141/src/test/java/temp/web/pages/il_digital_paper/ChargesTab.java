package com.sprint.iice_tests.web.pages.il_digital_paper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.sprint.iice_tests.lib.dao.data.Constants;

public class ChargesTab extends TabBase implements TabActions {

	private static final By PHONE_ACOUNTS = By.cssSelector(".service-subscriber-charges-header > .light.no-wrap");
	private static final By PHONE_ACOUNTS_SECOND_SERVICE = By
			.cssSelector(".service:nth-child(2) > .row > div > div > .service-subscriber-charges > div > span.light");
	private static final String CHARGE_PATH = "div:nth-child(%d) > div.service-subscriber-charges-details > .charge-type-block:nth-child(%d) > div > .row";
	private static final String CHARGE_PATH_SECOND_SERVICE = ".service:nth-child(2) > .row-spacing-bigger > div > .service-subscribers-charges > div > div > .charge-type-block:nth-child(%d) > div > .row";
	private static final String CHARGE_TAIL = " > .label > div > span:nth-child(1)";
	private static final String LINE_CHARGES = ".regular-charge-%s > span";
	private static final String EXPAND_BUTTON_TAIL = "div > div:nth-child(1) > span > .ico";
	private static final String PHONE_FROM_SERVICE = ".service:nth-child(%d) > .row > div > div > .service-subscriber-charges > div > span.light";
	private static final String CHARGE_DESCR_FROM_SERVICE = ".service:nth-child(%d) > .row-spacing-bigger > div > .service-subscribers-charges > div:nth-child(%d) > div > .charge-type-block > div > .row";

	final String EXPAND_BUTTON_GRAPHS = ".service:nth-of-type(%d) > .row.row-spacing-bigger > div > div > .bb-peu-historic-graph > g > rect.";
	final String EXPAND_BUTTON_GRAPHS_WITH_DATE = ".service:nth-of-type(%d) > .row.row-spacing-bigger > div > div > .bb-peu-historic-graph > g:nth-child(%d) > .";
	final String TEXT_PATH = "div:nth-child(%d) > div.service-subscriber-charges-details > .charge-type-block:nth-child(%d) > div:nth-child(%d) > .row";
	final String TEXT_PATH_NO_LABEL = "div:nth-child(%d) > div.service-subscriber-charges-details > .charge-type-block:nth-child(%d) > div > .row";
	final String QUEST_MARK_ATTRIBUTE = "class";
	final String DOWNLOAD_ATTRIBUTE = "class";
	final String EXPAND_BUTTON_TEXT_TAIL = "div > div > a > span";
	final By PandEBUTTON = By.cssSelector(
			"#current > div:nth-child(3) > div > div.col-xs-12.charge-description > div > div.col-xs-19.col-sm-22 > a > span");
	final By SERVICE_MESSAGE = By.cssSelector(".service-message > span");
	private final By TABLE_CHARGES_ROWS = By.cssSelector(".bb-regular-charges .charge-detail-row");

	private final By CHARGE_GRAPHS = By
			.cssSelector("[ng-if='displayGraph'] > [data='historicCharges'] > svg > g > .bb-filled-rect");
	By blueQuestionMarkLoc = By.cssSelector("div > span > i.ico--info.color--blue");
	By blueQuestMarkPopUpContentLoc = By.cssSelector("div.popup-content > p");

	By blueQuestionMarkMultipleMessages = By.cssSelector(".visible > .popup-content > div.popup-content > div");

	By chargesDescLoc = By.cssSelector(
			"#current > div:nth-child(4) > div:nth-child(2) > div > div > div.premium-charge > div > div > div.charges-description > div > div.col-xs-16 > span");

	By PTNFromChargeDescLoc = By.cssSelector(
			"#current > div:nth-child(4) > div:nth-child(2) > div > div > div.premium-charge > div > div > div.charges-title > div > div.col-xs-16.ptn-block > span.no-wrap");
	By amountFromChargesLoc = By.cssSelector(
			"#current > div:nth-child(4) > div:nth-child(2) > div > div > div.premium-charge > div > div > div.charges-description > div > div.col-xs-8.amount");
	By nameOnChargesLoc = By.cssSelector(
			"#current > div:nth-child(4) > div:nth-child(2) > div > div > div.premium-charge > div > div > div.charges-title > div > div.col-xs-16.ptn-block > span:nth-child(1) > span:nth-child(1)");
	By questMarkIconLoc = By.cssSelector("i[class|='question-icon ico ico--info color--blue']");

	By equipSectionForptnLoc = By.cssSelector(
			"#current > div:nth-child(3) > div:nth-child(2) > div > div > div:nth-child(1) > div:nth-child(2) > div.col-xs-24.col-md-16 > div.service-subscribers-charges > div > div.service-subscriber-charges-details > div:nth-child(2)");
	By equipmentDetailsDescOrderLoc = By.cssSelector("div.col-xs-16.label > div > span:nth-child(2) > span");
	By equipDetailsDescLoc = By.cssSelector("div > div.col-xs-16.label > div > span:nth-child(1)");
	By equipDetailsChargeLoc = By.cssSelector("div > div.col-xs-8.amount > span");
	private final By EXPAND_BUTTONS = By
			.cssSelector(".charge > .row > .charge-description > div  > div:nth-child(1) > span > .ico");
	private final By EXPAND_BUTTON_STUB = By.cssSelector(".charge > .row > .charge-description");
	private final String EXPAND_BUTTON_HEADINGS_BY_SECTION = "#%s > .charge > .row > .charge-";

	By chargesButtonsLoc = By.cssSelector("div[class^='charge']");
	By chargesButtonFullLoc = By.id("current");
	private static final By BUTTON_AMOUNTS = By.cssSelector("#current > .charge > .row > .charge-amount > span");
	private static final By AMOUNT_FROM_DESCRIPTION = By.xpath("./parent::*/parent::*/following-sibling::*/span");
	private static final String SPAN_TEXT = "//span[contains(text(),'%s')]";
	private static final By LINK_FROM_DESCRIPTION = By.xpath("./following-sibling::*/a");
	private static final String PHONE_TO_DESCRIPTION = "./parent::*/following-sibling::*//span[contains(text(),\"%s\")]";
	private static final By PHONE_TO_POPUP = By.xpath("./following-sibling::*//div[@class=\"popup-content\"]/p");
	private static final By DESCRIPTION_FROM_AMOUNT = By.xpath("./parent::*//a");
	final String ROWS_OF_TABLE_WITH_ID = "//*[@id='%s']/parent::*/parent::*//*[@class='row']";
	final String GRAPH_DATE = "div:nth-of-type(%d) > .row > div > div > svg > g > .bb-graph-date";
	final String GRAPH_AMOUNT = "div:nth-of-type(%d) > .row > div > div > svg > g > text.bb-graph-amount.light";

	private final By DATE_TITLE = By.cssSelector(".bb-current-charges > .title-row > .first-md.title > .light");

	private By charge_graphs(int order, String tail) {
		Object[] args = { order + 1 };
		return By.cssSelector(formatPath(EXPAND_BUTTON_GRAPHS, args, tail));
	}

	private By charge_graphs(String service, String tail) {
		int order = getIndexOfElementContainingTextFromList(service, headerChargeDescriptionsAsWebElements());
		Object[] args = { order + 1 };
		return By.cssSelector(formatPath(EXPAND_BUTTON_GRAPHS, args, tail));
	}

	private By line_charges(String amountOrDesciption) {
		Object[] args = { amountOrDesciption };
		return By.cssSelector(formatPath(LINE_CHARGES, args));
	}

	By headerChargeAmountLoc = By.xpath("//div[contains(@class, 'col-xs-8 service-header-amount')]/span");
	By headerChargeDescLoc = By.xpath("//div[contains(@class, 'service-header-title')]/span");

	By buttonNameChargeAmountLoc = By.xpath("//div[contains(@class, 'col-xs-12 text-right charge-amount')]/span");
	By buttonNameChargeDescLoc = By.xpath("//div[contains(@class, 'col-xs-19 col-sm-22')]/a//span");

	By previousActivityChargeAmountLoc = By.xpath(".//div[contains(@class, 'charge-amount')]//span");
	By prevActTotalDescLoc = By
			.xpath(".//div[contains(@class, 'charge-description forwarded-charge-description')]//span");
	By previousActivityContentLoc = By.xpath("//div[@id = 'previous']");
	By previousActivityDescLoc = By.xpath(".//div[contains(@class, 'col-xs-19 col-sm-22')]");

	By adjustmentContentLoc = By.xpath("//div[contains(@class, 'row row-spacing-bigger')]");

	By accountChargeDescLoc = By.xpath(".//div[@class = 'col-xs-16 regular-charge-description']");
	By accountChargeAmountLoc = By.xpath(".//div[@class = 'col-xs-8 regular-charge-amount']");
	By accountChargesContentLoc = By.xpath("//div[contains(@class, 'bb-regular-charges')]");

	By descUnderPlansAndEquipButtonLoc = By.xpath(".//div[contains(@class, 'col-xs-16 label')]");
	By chargeAmountUnderPlansAndEquipButtonLoc = By.xpath(".//div[contains(@class, 'col-xs-8 amount')]/span");
	By totalDescUnderPlansAndEquipLoc = By.xpath(".//div[contains(@class, 'col-xs-16 title')]");

	public By contentUnderSpecifiedHeaderForPlansEquipUsageLoc(String headerText) {
		return By.xpath("//span[contains(@class, 'light') and text() = '" + headerText + "']/../../../../../..");
	}

	By sprintPremiumContentLoc = By.xpath("//div[@class= 'bb-premium-charges']");
	By sprintPremiumDescListLoc = By.xpath(".//div[@class= 'col-xs-16']");
	By sprintPremiumChargeListLoc = By.xpath(".//div[@class= 'col-xs-8 amount']");
	By sprintPremiumDescPtnFromTotalLoc = By.xpath(".//div[@class= 'col-xs-16']//span[@class='medium no-wrap']");
	By sprintPreiumDescNameFromTotalLoc = By.xpath(".//div[@class= 'col-xs-16']//span[@class='medium']/span[1]");

	By barGraphChargeLoc = By.xpath(".//*[@class = 'bb-graph-amount light']");

	By barGraphTextLoc(String date) {
		return By.xpath(".//*[text()='" + date + "']");
	}

	By barGraphPlansLoc = By.xpath(".//*[@class = 'plans']");
	By barGraphEquipLoc = By.xpath(".//*[@class = 'equipment']");
	By barGraphUsageLoc = By.xpath(".//*[@class = 'usage']");
	By barGraphsWithSvgLoc = By.xpath("//div[@class= 'col-xs-24 col-md-8']//div[2]/*");
	By allBarGraphsListLoc = By.xpath("./child::*");

	By plansEquipGraphsLoc(String plansEquipHeader) {
		return By.xpath("//span[text()= '" + plansEquipHeader
				+ "']/../../../../../../div[2]//div[@class= 'col-xs-24 col-md-8']//div[2]/*");
	}

	By allTheDateKeysForThirdPartyLoc = By.xpath("//div[@class='first-col']//div[@class='bb-row']");
	By allTheHeadersForThirdParty = By.xpath("//*[@class='bb-responsive-table']//div[@class='bb-row bb-row-headers']");
	By headersWithinThirdPartyChargesTableLoc = By.xpath(".//div[@class='bb-col']");
	By rowWithinThirdPartyChargesTableLoc = By.xpath("//div[@class='table-content first-line']");
	By cellsWithinThirdPartyChargesRow = By.xpath(".//div[@class='bb-col']");

	private By boxNextToTextLoc(String nameOfText) {
		return By.xpath(".//span[contains(text(),'" + nameOfText + "')]/parent::*//div");
	}

	public By viewDetailsBasedOnTextAboveItLoc(String textAboveDesiredViewDetails) {
		return By.xpath(".//span[contains(text(),'" + textAboveDesiredViewDetails
				+ "')]/parent::*/parent::*//div//a[contains(text(),'View Details')]");
	}

	public By viewDetailsBasedOnTextAboveItLocInSpanish(String textAboveDesiredViewDetails) {
		return By.xpath(".//span[contains(text(),'" + textAboveDesiredViewDetails
				+ "')]/parent::*/parent::*//div//a[contains(text(),'Ver Detalles')]");
	}

	private By contentUnderPhoneNumber(String phoneNumber) {
		return By.xpath(".//span[contains (text(),'" + phoneNumber
				+ "')]/parent::*/parent::div[@class = 'service-subscriber-charges']");
	}

	private By questionMarkNextToPhoneNumberLoc(String phoneNumber) {
		return By.xpath(
				"//span[contains(@class, 'light no-wrap') and text() = '" + phoneNumber + "']/following-sibling::span");
	}

	private By charge_descriptions_equip(int phoneIndex) {
		Object[] args = { phoneIndex, EQUIP };
		return By.cssSelector(formatPath(CHARGE_PATH, args, CHARGE_TAIL));
	}

	private By charge_descriptions_plan(int phoneIndex) {
		Object[] args = { phoneIndex, PLAN };
		return By.cssSelector(formatPath(CHARGE_PATH, args, CHARGE_TAIL));
	}

	private By charge_descriptions_plan_multi_service(int phoneIndex) {
		Object[] args = { phoneIndex, PLAN };
		return By.cssSelector(formatPath(CHARGE_PATH_SECOND_SERVICE, args, CHARGE_TAIL));
	}

	private By charge_descriptions_equip_multi_service(int phoneIndex) {
		Object[] args = { phoneIndex, EQUIP };
		return By.cssSelector(formatPath(CHARGE_PATH_SECOND_SERVICE, args, CHARGE_TAIL));
	}

	private By messageParagraphLoc(String textThatIdentifiesDisclaimer) {
		return By.xpath("//span[contains(text(),'" + textThatIdentifiesDisclaimer + "')]");
	}

	private By user_notification_above_row(String rowText) {
		return By.xpath("//span[contains(text(),'" + rowText + "')]/parent::*/parent::*/parent::*/"
				+ "parent::*/parent::*/parent::*/*[contains(@class,'user-notification')]/span");
	}

	public WebElement getExpandButton(String button) {
		return retryElementUntilPresent(PandEBUTTON);
	}

	public WebElement questMarkIcon() {
		return retryElementUntilVisible(questMarkIconLoc);
	}

	public WebElement nameOnCharges() {
		return retryElementUntilVisible(nameOnChargesLoc);
	}

	public WebElement equipSection() {
		return driver.findElement(equipSectionForptnLoc);
	}

	public WebElement chargesAmount() {
		return driver.findElement(amountFromChargesLoc);
	}

	public WebElement blueQuestionMark() {
		return retryElementUntilPresent(blueQuestionMarkLoc);
	}

	public WebElement blueQuestMarkPopUpContent() {
		return retryElementUntilVisible(blueQuestMarkPopUpContentLoc);
	}

	public String[] blueQuestionMarkMessages() {
		List<WebElement> messages = retryElementsUntilPresent(blueQuestionMarkMultipleMessages);
		String[] text = new String[messages.size()];
		for (int i = 0; i < messages.size(); i++) {
			text[i] = messages.get(i).getText();
		}
		return text;
	}

	public WebElement chargesDesc() {
		return retryElementUntilVisible(chargesDescLoc);
	}

	public String userNotificationAboveRow(String rowText) {
		return retryElementUntilPresent(user_notification_above_row(rowText)).getText();
	}

	public WebElement messageParagraph(String textThatIdentifiesDisclaimer) {
		return driver.findElement(messageParagraphLoc(textThatIdentifiesDisclaimer));
	}

	public WebElement PTNFromChargeDesc() {
		return retryElementUntilVisible(PTNFromChargeDescLoc);
	}

	public List<WebElement> getEquipmentDetailsCharge() throws Exception {
		Callable<List<WebElement>> element = () -> equipSection().findElements(equipDetailsChargeLoc);
		return retryStaleElements(element);
	}

	public List<WebElement> getEquipmentDetailsDesc() throws Exception {
		Callable<List<WebElement>> element = () -> equipSection().findElements(equipDetailsDescLoc);
		return retryStaleElements(element);
	}

	public List<WebElement> getEquipmentDetailsOrder() throws Exception {
		Callable<List<WebElement>> element = () -> equipSection().findElements(equipmentDetailsDescOrderLoc);
		return retryStaleElements(element);
	}

	public List<WebElement> getServiceMessages() {
		return retryAllElementsUntilVisible(SERVICE_MESSAGE);
	}

	public String getMsgParagraphTextBySpan(String identifyingTextForMsg) {
		return messageParagraph(identifyingTextForMsg).getText();
	}
	
	public WebElement msgParagraph(String identifyingTextForMsg) {
		WebElement element = null;
		try {
			element= driver.findElement(By.xpath("//p[contains(text(),'" + identifyingTextForMsg + "')]"));
		}catch(NoSuchElementException e) {
			System.out.println("Doesn't exist.");
		}catch(NullPointerException e) {
			
		}
		return element;
	}
	
	public String getMsgParagraphText(String identifyingTextForMsg) {
		String txt = null;
		try {
			txt=msgParagraph(identifyingTextForMsg).getText();
		}catch(NullPointerException e) {
			
		}catch(NoSuchElementException e) {
			
		}
		return  txt;
	}

	public String getChargesDesc() {
		return chargesDesc().getText();
	}

	public boolean questMarkIconContainsIcon(String iconAttribute) {
		return questMarkIcon().getAttribute(QUEST_MARK_ATTRIBUTE).contains(iconAttribute);
	}

	public boolean downLoadIconContainsIcon(String iconAttribute) {
		return downloadIcon().getAttribute(DOWNLOAD_ATTRIBUTE).contains(iconAttribute);
	}

	public WebElement downloadIcon() {
		return retryElementUntilVisible(downloadIconLoc);
	}

	By downloadIconLoc = By.cssSelector("i[class|='ico ico--download-csv color--blue']");

	public String questMarkIconString() {
		return questMarkIcon().getAttribute(QUEST_MARK_ATTRIBUTE);
	}

	public String getTextOrder(String order) throws Exception {
		for (WebElement orderVal : getEquipmentDetailsOrder()) {
			if (orderVal.getText().equals(order)) {
				String orderLoc = orderVal.getLocation().toString();
				return orderLoc;
			}
		}
		return null;
	}

	/**
	 * questMarkFCAPPPopUpText() gets text from blue question mark pop up message.
	 * 
	 * @return the pooling message that appears in the pop up message
	 */
	public String questMarkFCAPPPopUpText() {
		return blueQuestMarkPopUpContent().getText();
	}

	public List<WebElement> getChargeGraphs() {
		return retryAllElementsUntilVisible(CHARGE_GRAPHS);
	}

	private By graphDate(int order) {
		return By.cssSelector(String.format(GRAPH_DATE, order));
	}

	private By graphDate(String service) {
		int order = getIndexOfElementContainingTextFromList(service, headerChargeDescriptionsAsWebElements()) + 1;
		return By.cssSelector(String.format(GRAPH_DATE, order));
	}

	private By graphAmount(String service) {
		int order = getIndexOfElementContainingTextFromList(service, headerChargeDescriptionsAsWebElements()) + 1;
		return By.cssSelector(String.format(GRAPH_AMOUNT, order));
	}

	private By graphAmount(int order) {
		return By.cssSelector(String.format(GRAPH_AMOUNT, order));
	}

	public List<WebElement> getGraphAmount(int order) {
		return retryAllElementsUntilVisible(graphAmount(order));
	}

	public List<WebElement> getGraphAmount(String service) {
		return retryAllElementsUntilVisible(graphAmount(service));
	}

	/**
	 * Returns a list of equipment graphs from under the Equipmnent and Plans expand
	 * button number of graphs is variable, pass an int (zero-based) to get a
	 * particular graph 0 will always return something
	 */
	public List<WebElement> getGraphDate(int order) {
		return retryElementsUntilPresent(graphDate(order));
	}

	public List<WebElement> getGraphDate(String service) {
		return retryAllElementsUntilVisible(graphDate(service));
	}

	/**
	 * Gets a List of WebElements of the expand buttons for the current charge area
	 * .get(0) is Account Charges .get(1) is Plans and Equipment etc. (varies)
	 */
	public List<WebElement> getExpandButtons() {
		return retryElementsUntilPresent(EXPAND_BUTTONS);
	}

	/**
	 * Returns a list of equipment graphs from under the Equipmnent and Plans expand
	 * button number of graphs is variable, pass an int (zero-based) to get a
	 * particular graph 0 will always return something
	 */
	public List<WebElement> getEquipmentBar(int order) {
		return retryAllElementsUntilVisible(charge_graphs(order, "equipment"));
	}

	public List<WebElement> getEquipmentBar(String service) {
		return retryAllElementsUntilVisible(charge_graphs(service, "equipment"));
	}

	public List<WebElement> getInitGraphs(String service, String date) {
		return retryAllElementsUntilVisible(charge_graphs_by_date(service, date, "bb-init-rect"));
	}

	public String getGraphAmountUnderDateRange(String service, String date) {
		return retryElementUntilPresent(charge_graphs_by_date(service, date, "bb-graph-amount")).getText();
	}

	public WebElement getUsageBarUnderDateRange(String service, String date) {
		return retryElementUntilPresent(charge_graphs_by_date(service, date, "usage"));
	}

	private By charge_graphs_by_date(String service, String date, String tail) {
		int servicePosition = getIndexOfElementContainingTextFromList(service, headerChargeDescriptionsAsWebElements())
				+ 1;
		int datePosition = getIndexOfElementWithTextFromList(date, getGraphDate(servicePosition)) + 1;
		Object[] args = { servicePosition, datePosition };
		return By.cssSelector(formatPath(EXPAND_BUTTON_GRAPHS_WITH_DATE, args, tail));
	}

	/**
	 * Returns a list of usage graphs from under the Equipmnent and Plans expand
	 * button number of graphs is variable, pass an int (zero-based) to get a
	 * particular graph 0 will always return something
	 */
	public List<WebElement> getUsageBar(int order) {
		return retryAllElementsUntilVisible(charge_graphs(order, "usage"));
	}

	public List<WebElement> getUsageBar(String service) {
		return retryAllElementsUntilVisible(charge_graphs(service, "usage"));
	}

	/**
	 * Returns a list of plan graphs from under the Equipmnent and Plans expand
	 * button number of graphs is variable, pass an int (zero-based) to get a
	 * particular graph 0 will always return something
	 */
	public List<WebElement> getPlanBar(int order) {
		return retryElementsUntilPresent(charge_graphs(order, "plans"));
	}

	public List<WebElement> getPlanBar(String service) {
		return retryElementsUntilPresent(charge_graphs(service, "plans"));
	}

	public boolean downloadIsDisplayed() {
		return downloadButton().isDisplayed();
	}

	/**
	 * Method to go to appropriate charges section of the equipment expand button
	 * area based on phone number
	 */
	public void getSubscriberNumberCharges(String phoneNumber) {
		// for (WebElement number: get)

	}

	private final Integer MAC_PHONE_NTH = 3;
	private final Integer EQUIP = 2;
	private final Integer PLAN = 1;
	private final Integer MAC_ROW = 3;
	private final String AMOUNT = " > .amount > span";
	private final String LINK = " > .label > div > div > a";

	public WebElement chargeAmount(String phoneNum, String description) {
		try {
			return retryElementUntilVisible(charge_amount_plan(phoneNum, description));
		} catch (Exception e) {
			return retryElementUntilVisible(charge_amount_second_service(phoneNum, description));
		}
	}

	private By charge_amount_plan(String phoneNum, String description) {
		int phonePosition = getIndexOfElementWithTextFromList(phoneNum, accountsByPhoneNum()) + 1;
		int rowPosition = getIndexOfElementWithTextFromList(description, descriptionOfCharges(phonePosition)) + 1;
		Object[] args = { phonePosition, PLAN, rowPosition };
		return By.cssSelector(formatPath(TEXT_PATH, args, AMOUNT));
	}

	private By charge_amount_second_service(String phoneNum, String description) {
		int phonePosition = getIndexOfElementWithTextFromList(phoneNum, accountsByPhoneNumMultiService()) + 1;
		int rowPosition = getIndexOfElementWithTextFromList(description,
				descriptionOfChargesMultiService(phonePosition)) + 1;
		Object[] args = { phonePosition, PLAN, rowPosition };
		return By.cssSelector(formatPath(CHARGE_PATH_SECOND_SERVICE, args, AMOUNT));
	}
	/*
	 * private By charge_amountExp() { Object[] args = { phoneNuumber(), PLANS,
	 * getDescript() }; return By.cssSelector(formatPath(TEXT_PATH, args, AMOUNT));
	 * }
	 */

	private By mac_amount() {
		Object[] args = { MAC_PHONE_NTH, EQUIP, MAC_ROW };
		return By.cssSelector(formatPath(TEXT_PATH, args, AMOUNT));
	}

	public WebElement macAmount() {
		return retryElementUntilVisible(mac_amount());
	}

	public String getChargeDescriptionUnderService(String service, String phoneNum, String phoneDescr) {
		int serviceIndex = getIndexOfElementContainingTextFromList(service, headerDescFromService()) + 1;
		int phoneIndex = getIndexOfElementContainingTextFromList(phoneNum, phoneNumUnderServiceList(serviceIndex)) + 1;
		int rowIndex = getIndexOfElementContainingTextFromList(phoneDescr,
				chargeDescriptionFromService(serviceIndex, phoneIndex));
		return chargeDescriptionFromService(serviceIndex, phoneIndex).get(rowIndex).getText();
	}

	public List<WebElement> chargeDescriptionFromService(int serviceIndex, int phoneIndex) {
		return retryElementsUntilPresent(charge_description_from_service(serviceIndex, phoneIndex));
	}

	public List<WebElement> chargeAmountFromService(int serviceIndex, int phoneIndex) {
		return retryElementsUntilPresent(charge_amount_from_service(serviceIndex, phoneIndex));
	}

	private By charge_description_from_service(int serviceIndex, int phoneIndex) {
		Object[] args = { serviceIndex, phoneIndex };
		return By.cssSelector(formatPath(CHARGE_DESCR_FROM_SERVICE, args, CHARGE_TAIL));
	}

	private By charge_amount_from_service(int serviceIndex, int phoneIndex) {
		Object[] args = { serviceIndex, phoneIndex };
		return By.cssSelector(formatPath(CHARGE_DESCR_FROM_SERVICE, args, AMOUNT));
	}

	private By expand_button_headings(String chargeSection, String tail) {
		Object[] args = { chargeSection };
		return By.cssSelector(formatPath(EXPAND_BUTTON_HEADINGS_BY_SECTION, args, tail));
	}

	private List<WebElement> headerDescFromService() {
		String HEADER_DESC = ".service > .row > div > .service-header > .row >.service-header-title > .light";
		return retryElementsUntilPresent(By.cssSelector(HEADER_DESC));
	}

	private By phone_num_service_list(int serviceIndex) {
		String AMEND = String.format(PHONE_FROM_SERVICE, serviceIndex);
		return By.cssSelector(AMEND);
	}

	private List<WebElement> phoneNumUnderServiceList(int serviceIndex) {
		return retryElementsUntilPresent(phone_num_service_list(serviceIndex));
	}

	public String getChargeAmountUnderService(String service, String phoneNum, String description) {
		int serviceIndex = getIndexOfElementContainingTextFromList(service, headerDescFromService()) + 1;
		int phoneIndex = getIndexOfElementContainingTextFromList(phoneNum, phoneNumUnderServiceList(serviceIndex)) + 1;
		int rowIndex = getIndexOfElementContainingTextFromList(description,
				chargeDescriptionFromService(serviceIndex, phoneIndex));
		System.out.println(serviceIndex + ", " + phoneIndex + ", " + rowIndex);
		return chargeAmountFromService(serviceIndex, phoneIndex).get(rowIndex).getText();
	}

	private By row_view_details(String phoneNum, String description) {

		int phonePosition = getIndexOfElementWithTextFromList(phoneNum, accountsByPhoneNum()) + 1;
		int rowPosition = getIndexOfElementWithTextFromList(description, descriptionOfCharges(phonePosition)) + 1;
		Object[] args = { phonePosition, PLAN, rowPosition };
		return By.cssSelector(formatPath(TEXT_PATH_NO_LABEL, args, LINK));
	}

	public WebElement rowViewDetails(String phoneNum, String description) {
		return retryElementUntilVisible(row_view_details(phoneNum, description));
	}
	
	public WebElement descViewDetails(String description) {
		return retryElementUntilVisible(contains_text(description)).findElement(DESCRIPTION_FROM_AMOUNT);
	}

	public String getDateTitle() {
		return retryElementUntilPresent(DATE_TITLE).getText();
	}

	public String getTableAmountByDescription(String phoneNum, String description) {
		int phoneIndex = getIndexOfElementWithTextFromList(phoneNum, accountsByPhoneNum()) + 1;
		return getElementWithTextFromList(description, descriptionOfCharges(phoneIndex)).getText();
	}

	private List<WebElement> descriptionOfCharges(int phoneIndex) {
		try {
			return retryElementsUntilPresent(charge_descriptions_plan(phoneIndex));
		} catch (Exception e) {
			return retryElementsUntilPresent(charge_descriptions_equip(phoneIndex));
		}
	}

	private List<WebElement> descriptionOfChargesMultiService(int phoneIndex) {
		try {
			return retryElementsUntilPresent(charge_descriptions_plan_multi_service(phoneIndex));
		} catch (Exception e) {
			return retryElementsUntilPresent(charge_descriptions_equip_multi_service(phoneIndex));
		}
	}

	public List<WebElement> accountsByPhoneNum() {
		return retryElementsUntilPresent(PHONE_ACOUNTS);
	}

	public List<WebElement> accountsByPhoneNumMultiService() {
		return retryElementsUntilPresent(PHONE_ACOUNTS_SECOND_SERVICE);
	}

	public List<WebElement> expandButtonHeadings() {
		return driver.findElements(EXPAND_BUTTON_STUB);
	}

	public List<WebElement> expandButtonHeadingsInCharges() {
		return driver.findElements(expand_button_headings("current", "description div > div > a > span"));
	}

	public List<WebElement> expandButtonAmountsInCharges() {
		return driver.findElements(expand_button_headings("current", "amount > span"));
	}

	public List<WebElement> lineChargeDescriptions() {
		return retryElementsUntilPresent(line_charges("description"));
	}

	public List<WebElement> lineChargeAmounts() {
		return retryAllElementsUntilVisible(line_charges("amount"));
	}

	public String headerChargeDescriptionsWithText(String header) {
		List<WebElement> list = headerChargeDescriptionsAsWebElements();
		int index = getIndexOfElementContainingTextFromList(header, list);
		return list.get(index).getText();
	}

	public Object chargeAmountOfHeader(String header) {
		int index = getIndexOfElementWithTextFromList(header, headerChargeDescriptionsAsWebElements());
		return headerChargeAmounts().get(index);
	}

	public List<String> headerChargeAmounts() {
		return makeListElementsStrings(headerChargeAmountsAsWebElements());
	}

	public String getServiceMessageOfHeaderWithText(String header) {
		int index = getIndexOfElementWithTextFromList(header, headerChargeDescriptionsAsWebElements());
		return getServiceMessages().get(index).getText();
	}

	private String FOOTER_PATH = "#%s > div > .row > div  > .charge-description > .medium.label";
	private String FOOTER_AMOUNT = "#%s > div > .row > div > .charge-amount > span";

	public String getCurrentDateFooter() {
		return retryElementUntilPresent(date_footer("current")).getText();
	}

	public String getCurrentFooterAmount() {
		return retryElementUntilPresent(date_amount("current")).getText();
	}

	private By date_footer(String idText) {
		return By.cssSelector(String.format(FOOTER_PATH, idText));
	}

	private By date_amount(String idText) {
		return By.cssSelector(String.format(FOOTER_AMOUNT, idText));
	}

	/**
	 * BUTTONS CONTENT
	 */

	public String tableOfButtonNameCharges(String chargeDesc) throws Exception {
		Map<String, String> table = createTableOfStringsFromWebElements(buttonNameChargeDescAsWebElements(),
				buttonNameChargeAmountsAsWebElements());
		System.out.println("this is the map of button names and charges "+table);
		return table.get(chargeDesc);
	}
	
	public Map<String, String> mapOfButtonChargeAndDesc(){
		return createTableOfStringsFromWebElements(buttonNameChargeDescAsWebElements(),
				buttonNameChargeAmountsAsWebElements());
	}

	public List<WebElement> buttonNameChargeDescAsWebElements() {
		List<WebElement> buttonNaumeChargeDescList =  retryAllElementsUntilVisible(buttonNameChargeDescLoc);
		buttonNaumeChargeDescList.add(totalChargeDesc());
		return buttonNaumeChargeDescList;
	}

	public List<WebElement> buttonNameChargeAmountsAsWebElements() {
		List<WebElement> buttonNaumeChargeAmountList =  retryAllElementsUntilVisible(buttonNameChargeAmountLoc);
		buttonNaumeChargeAmountList.add(totalChargeAmount());
		return buttonNaumeChargeAmountList;
	}
	
	public WebElement totalChargeDesc() {
		return retryElementUntilVisible(By.xpath("//div[@class='charge sum-row']/div/div/div[contains(@class,'charge-description')]"));
	}
	
	public WebElement totalChargeAmount() {
		return totalChargeDesc().findElement(By.xpath("./following-sibling::div"));
	}

	/**
	 * PREVIOUS ACTIVITY CONTENT
	 */

	public String tableOfPreviousActivity(String chargeDesc) throws Exception {
		Map<String, String> tableofChargeDescAndAmounts = createPreviousActivityTable();
		String chargeAmount = tableofChargeDescAndAmounts.get(chargeDesc);
		return chargeAmount;
	}

	public Map<String, String> createPreviousActivityTable() throws Exception {
		Map<String, String> tableofChargeDescAndAmounts = createTableOfStringsFromWebElements(previousActivityDesc(),
				previousActivityChargeAmount());
		System.out.println("PREVIOUS ACTIVITY MAP : " + tableofChargeDescAndAmounts);
		return tableofChargeDescAndAmounts;
	}

	public WebElement previousActivity() {
		return driver.findElement(previousActivityContentLoc);
	}

	public List<WebElement> previousActivityChargeAmount() {
		return previousActivity().findElements(previousActivityChargeAmountLoc);
	}

	public List<WebElement> previousActivityDesc() {
		List<WebElement> a = previousActivity().findElements(previousActivityDescLoc);
		a.add(prevActTotalDesc());
		return a;
	}

	public WebElement prevActTotalDesc() {
		return previousActivity().findElement(prevActTotalDescLoc);
	}
	
	private final By TABLE_ROWS = By.cssSelector(".bb-forwarded-charges");
	
	private List<WebElement> getTableRows() {
		return retryElementsUntilPresent(TABLE_ROWS);
	}
	
	public List<List<String>> getPreviousContentTable() {
		List<WebElement> rows = getTableRows();
		return createListStringFromRows(rows, SPAN);
	}
	
	private List<WebElement> getTableChargesRows() {
		return retryElementsUntilPresent(TABLE_CHARGES_ROWS);
	}
	
	public List<List<String>> getChargesTable() {
		List<WebElement> rows = getTableChargesRows();
		return createListStringFromRows(rows, SPAN);
	}

	/**
	 * ACCOUNT CHARGES CONTENT
	 */

	public String tableOfAccountCharges(String chargeDesc) throws Exception {
		Map<String, String> table = createTableOfStringsFromWebElements(descUnderAccountChargeButtonAsWebElements(),
				chargeAmountUnderAccountChargeButtonAsWebElements());
		System.out.println("TABLE OF ACCOUNT CHARGES: " + table);
		String chargeAmount = table.get(chargeDesc);
		return chargeAmount;
	}

	public WebElement accountChargesContent() throws Exception {
		return driver.findElement(accountChargesContentLoc);
	}

	public List<WebElement> descUnderAccountChargeButtonAsWebElements() throws Exception {
		return accountChargesContent().findElements(accountChargeDescLoc);
	}

	public List<WebElement> chargeAmountUnderAccountChargeButtonAsWebElements() throws Exception {
		return accountChargesContent().findElements(accountChargeAmountLoc);
	}

	/**
	 * ADJUSTMENT CONTENT
	 */

	public String tableOfAdjustmentCharges(String chargeDesc) throws Exception {
		Map<String, String> table = createTableOfStringsFromWebElements(adjustDesc(), adjustCharge());
		System.out.println(table);
		return table.get(chargeDesc);
	}

	public WebElement adjustmentContent() {
		return driver.findElement(adjustmentContentLoc);
	}

	public List<WebElement> adjustDesc() {
		return adjustmentContent().findElements(accountChargeDescLoc);
	}

	public List<WebElement> adjustCharge() {
		return adjustmentContent().findElements(accountChargeAmountLoc);
	}

	/**
	 * PLANS AND EQUIP CONTENT
	 */

	/**
	 * Within the the Plans, Equip, Usage content, there are "sum tables" for charge
	 * descriptions and amounts
	 * 
	 * @param phoneNumber
	 *            the phone number we are getting charge information from
	 * @param chargeDesc
	 *            = charge description
	 * @return = the charge amount of the charge description
	 * @throws Exception
	 */

	public String tableOfChargeDescAndAmountUnderPlansAndEquip(String headerText, String phoneNumber, String chargeDesc)
			throws Exception {
		Map<String, String> tableofChargeDescAndAmounts = plansEquipTable(headerText, phoneNumber);
		String chargeAmount = tableofChargeDescAndAmounts.get(chargeDesc);
		return chargeAmount;
	}

	public Map<String, String> plansEquipTable(String headerText, String phoneNumber) throws Exception {
		Map<String, String> tableofChargeDescAndAmounts = createTableOfStringsFromWebElements(
				descUnderPlansAndEquipButtonAsWebElement(headerText, phoneNumber),
				chargeAmountUnderPlansAndEquipButtonAsWebElement(headerText, phoneNumber));
		System.out.println(tableofChargeDescAndAmounts);
		return tableofChargeDescAndAmounts;
	}

	public List<WebElement> descUnderPlansAndEquipButtonAsWebElement(String headerText, String phoneNumber)
			throws Exception {
		List<WebElement> e = contentUnderSpecifiedHeaderAndPhoneNumber(headerText, phoneNumber)
				.findElements(descUnderPlansAndEquipButtonLoc);
		e.add(allTheTextForTotalDescUnderPlansAndEquip(headerText, phoneNumber));
		return e;
	}
	
	public boolean descUnderPlansAndEquipButtonExists(String headerText, String phoneNumber, String desc) {
		List<WebElement> e = contentUnderSpecifiedHeaderAndPhoneNumber(headerText, phoneNumber)
				.findElements(descUnderPlansAndEquipButtonLoc);
		e.add(allTheTextForTotalDescUnderPlansAndEquip(headerText, phoneNumber));
		return e.stream().filter(i -> i.getText().startsWith(desc)).count() > 0;
	}
		

	public List<WebElement> chargeAmountUnderPlansAndEquipButtonAsWebElement(String headerText, String phoneNumber)
			throws Exception {
		List<WebElement> list = contentUnderSpecifiedHeaderAndPhoneNumber(headerText, phoneNumber)
				.findElements(chargeAmountUnderPlansAndEquipButtonLoc);
		return list;
	}

	public WebElement allTheTextForTotalDescUnderPlansAndEquip(String headerText, String phoneNumber) {
		WebElement elementContainsMultipleSpans = contentUnderSpecifiedHeaderAndPhoneNumber(headerText, phoneNumber)
				.findElement(totalDescUnderPlansAndEquipLoc);
		return elementContainsMultipleSpans;

	}

	public WebElement contentUnderSpecifiedHeaderForPlansEquipUsage(String headerText) {
//		WebElement element = retryElementUntilPresent(contentUnderSpecifiedHeaderForPlansEquipUsageLoc(headerText));
//		return retryWebElementUntilVisible(element );
		return driver.findElement(contentUnderSpecifiedHeaderForPlansEquipUsageLoc(headerText));
	}
	By serviceLoc = By.xpath(".//div[@class='row row-spacing-bigger']//div[@class='service-message']");
	public WebElement serviceMsg (String headerText) {
		return tryWebElement(contentUnderSpecifiedHeaderForPlansEquipUsage(headerText), serviceLoc);
	}
	
	public String serviceMsgText(String headerText) {
		return serviceMsg (headerText) .getText();
	}

	public Boolean contentUnderSpecifiedHeaderForPlansEquipUsageIsDisplayed(String headerText) {
		WebElement header = contentUnderSpecifiedHeaderForPlansEquipUsage(headerText);
		scrollToElement(header);
		return header.isDisplayed();
	}

	public String contentUnderSpecifiedHeaderAndPhoneNumberAsString(String headerText, String phoneNumber) {
		return contentUnderSpecifiedHeaderAndPhoneNumber(headerText, phoneNumber).getText();
	}

	By iconLoc = By.xpath(".//i");

	public boolean doesDescContainCorrectIcon(String headerText, String phoneNumber, String targetText,
			String iconAttribute) throws Exception {
		WebElement e = getIconFromElementInList(headerText, phoneNumber, targetText);
		return doesPlansEquipDescContainIcon(e, iconAttribute);
	}

	public WebElement getIconFromElementInList(String headerText, String phoneNumber, String targetText)
			throws Exception {
		WebElement e = getElementWithTextFromList(targetText,
				descUnderPlansAndEquipButtonAsWebElement(headerText, phoneNumber));
		WebElement icon = e.findElement(iconLoc);
		return icon;
	}

	public boolean doesPlansEquipDescContainIcon(WebElement e, String iconAttribute) {
		return e.getAttribute("class").contains(iconAttribute);
	}

	public WebElement questionMarkNextToPhoneNumber(String phoneNumber) {
		WebElement el=null;
		try {
			el=driver.findElement(questionMarkNextToPhoneNumberLoc(phoneNumber));
		}catch(NullPointerException e) {
			
		}catch(NoSuchElementException e) {
			
		}
		return el;
	}

	public WebElement popupOfQuesMarkNextToPhoneNumber(String phoneNumber) {
		WebElement questMark = null;
		WebElement txt=null;
		try {
			questMark = questionMarkNextToPhoneNumber(phoneNumber);
		}
		catch(Exception e) {
			
		}
		try {
			txt=questMark.findElement(By.xpath(".//div/div/div/p"));
		}catch(Exception e) {
			
		}
		return txt;
	}

	public String popUpTextNextToPhoneNumber(String phoneNumber) {
		String txt=null;
		try {
			txt=popupOfQuesMarkNextToPhoneNumber(phoneNumber).getText();
		}catch(NullPointerException e) {
			
		}catch(NoSuchElementException e) {
			
		}
		return txt;
	}

	public WebElement contentUnderSpecifiedHeaderAndPhoneNumber(String headerText, String phoneNumber) {
		WebElement content = contentUnderSpecifiedHeaderForPlansEquipUsage(headerText)
				.findElement(contentUnderPhoneNumber(phoneNumber));
		return content;
	}

	public WebElement boxUnderPhoneNumberAndNextToText(String headerText, String phoneNumber, String nameOfText) {
		return contentUnderSpecifiedHeaderAndPhoneNumber(headerText, phoneNumber)
				.findElement(boxNextToTextLoc(nameOfText));
	}

	public String getColorOfBoxUnderHeaderAndPhoneNumberAndNextToText(String headerText, String phoneNumber,
			String nameOfText) {
		return boxUnderPhoneNumberAndNextToText(headerText, phoneNumber, nameOfText)
				.getCssValue(Constants.BOX_COLOR_CSS_VALUE);
	}

	public void clickDesiredViewDetailsFromHeaderPhoneNumberAndTextAboveIt(String headerText, String phoneNumber,
			String textAboveDesiredViewDetails) throws Exception {
		WebElement desiredViewDetails = getViewDetailsFromPhoneNumberAndTextAboveIt(headerText, phoneNumber,
				textAboveDesiredViewDetails);
		if(!(desiredViewDetails==null)) {
		scrollToAndClickElement(desiredViewDetails);
		wait.until(ExpectedConditions.invisibilityOf(desiredViewDetails));
	}}

	public void clickDesiredViewDetailsFromHeaderPhoneNumberAndTextAboveItInSpanish(String headerText,
			String phoneNumber, String textAboveDesiredViewDetails) throws Exception {
		WebElement desiredViewDetails = getViewDetailsFromPhoneNumberAndTextAboveItInSpanish(headerText, phoneNumber,
				textAboveDesiredViewDetails);
		scrollToAndClickElement(desiredViewDetails);
		wait.until(ExpectedConditions.invisibilityOf(desiredViewDetails));
	}

	public WebElement getViewDetailsFromPhoneNumberAndTextAboveIt(String headerText, String phoneNumber,
			String textAboveDesiredViewDetails) {
		WebElement e=null;
		try {
			e=contentUnderSpecifiedHeaderAndPhoneNumber(headerText, phoneNumber)
					.findElement(viewDetailsBasedOnTextAboveItLoc(textAboveDesiredViewDetails));
		}catch(NullPointerException ex) {
			
		}catch(NoSuchElementException ex) {
			
		}
		return e;
	}

	public WebElement getViewDetailsFromPhoneNumberAndTextAboveItInSpanish(String headerText, String phoneNumber,
			String textAboveDesiredViewDetails) {
		return contentUnderSpecifiedHeaderAndPhoneNumber(headerText, phoneNumber)
				.findElement(viewDetailsBasedOnTextAboveItLocInSpanish(textAboveDesiredViewDetails));
	}

	public String viewDetailsColorCssVal(String headerText, String phoneNumber, String textAboveDesiredViewDetails) {
		return getViewDetailsFromPhoneNumberAndTextAboveIt(headerText, phoneNumber, textAboveDesiredViewDetails)
				.getCssValue(Constants.TEXT_COLOR_CSS_VALUE);
	}

	/**
	 * HEADER CONTENT UNDER PLANS AND EQUIP
	 */
	/**
	 * This method grabs the header under Plans, Equip & Usage content. It grabs
	 * header text and charge amount.
	 * 
	 */

	public String tableOfHeaderCharges(String chargeDesc) throws Exception {
		Map<String, String> table = createTableOfStringsFromWebElements(headerChargeDescriptionsAsWebElements(),
				headerChargeAmountsAsWebElements());
		return table.get(chargeDesc);
	}

	public List<WebElement> headerChargeDescriptionsAsWebElements() {
		return retryElementsUntilPresent(headerChargeDescLoc);
	}

	public List<WebElement> headerChargeAmountsAsWebElements() {
		return retryAllElementsUntilVisible(headerChargeAmountLoc);
	}

	/**
	 * BAR GRAPHS CONTENT
	 */
	/**
	 * @param element
	 *            = the specific triplet of plans, equip, and usage bar graphs
	 * @param desiredBarGraphByDate
	 *            = user specifies the header that the bar graphs are attached to
	 *            for the plans, equipment, usage content
	 * @return true if the bar graph has user specified date
	 */
	public WebElement plansEquipGraphs(String plansEquipHeader) {
		return driver.findElement(plansEquipGraphsLoc(plansEquipHeader));
	}

	public List<WebElement> plansEquipBarGraphsList(String plansEquipHeader) {
		return plansEquipGraphs(plansEquipHeader).findElements(allBarGraphsListLoc);
	}

	public WebElement barGraphsWithSvg() {
		return driver.findElement(barGraphsWithSvgLoc);
	}

	public Boolean barGraphHasDesiredDate(WebElement element, String desiredBarGraphByDate) {
		WebElement date = element.findElement(barGraphTextLoc(desiredBarGraphByDate));
		return date.getText().equals(desiredBarGraphByDate);
	}

	public String barGraphChargeAmountText(String plansEquipHeader, String desiredBarGraphByDate) {
		return barGraphChargeAmount(plansEquipHeader, desiredBarGraphByDate).getText();
	}

	/**
	 * 
	 * @param plansEquipHeader
	 *            = the header
	 * @param desiredBarGraphByDate
	 * @return
	 */

	public WebElement barGraphChargeAmount(String plansEquipHeader, String desiredBarGraphByDate) {
		return desiredBarGraphFromList(plansEquipHeader, desiredBarGraphByDate).findElement(barGraphChargeLoc);
	}

	/**
	 * There are a group of three bar graphs per date and header within plans,
	 * equipment, and usage content. This method grabs the plans bar graph given the
	 * user specified header and date.
	 * 
	 * @param plansEquipHeader
	 *            = user specifies the header that the bar graphs are attached to
	 *            for the plans, equipment, usage content
	 * @param desiredBarGraphByDate
	 *            = user specifies the charge date period that the graphs display
	 * @return
	 */

	public WebElement barGraphPlans(String plansEquipHeader, String desiredBarGraphByDate) {
		return desiredBarGraphFromList(plansEquipHeader, desiredBarGraphByDate).findElement(barGraphPlansLoc);
	}

	/**
	 * There are a group of three bar graphs per date and header within plans,
	 * equipment, and usage content. This method grabs the equipment bar graph given
	 * the user specified header and date.
	 * 
	 * @param plansEquipHeader
	 *            = user specifies the header that the bar graphs are attached to
	 *            for the plans, equipment, usage content
	 * @param desiredBarGraphByDate
	 *            = user specifies the charge date period that the graphs display
	 * @return
	 */

	public WebElement barGraphEquip(String plansEquipHeader, String desiredBarGraphByDate) {
		return desiredBarGraphFromList(plansEquipHeader, desiredBarGraphByDate).findElement(barGraphEquipLoc);
	}

	/**
	 * There are a group of three bar graphs per date and header within plans,
	 * equipment, and usage content. This method grabs the usage bar graph given the
	 * user specified header and date.
	 * 
	 * @param plansEquipHeader
	 *            = user specifies the header that the bar graphs are attached to
	 *            for the plans, equipment, usage content
	 * @param desiredBarGraphByDate
	 *            = user specifies the charge date period that the graphs display
	 * @return
	 */

	public WebElement barGraphUsage(String plansEquipHeader, String desiredBarGraphByDate) {
		return desiredBarGraphFromList(plansEquipHeader, desiredBarGraphByDate).findElement(barGraphUsageLoc);
	}

	/**
	 * User specifies whether they want the plans, equip, or usage graph.
	 * 
	 * @param typeOfGraph
	 *            = either a plans, equipment, or usage graph
	 * @return the color of the graph
	 */

	public String getColorOfBarGraph(WebElement typeOfGraph) {
		return typeOfGraph.getCssValue(Constants.BAR_GRAPH_COLOR_FILL_CSS_VALUE);
	}

	/**
	 * 
	 * @param typeOfGraph
	 *            = either a plans, equipment, or usage graph
	 * @return the percentage that the bar graph is filled
	 */

	public String getFillOfBarGraph(WebElement typeOfGraph) {
		return typeOfGraph.getAttribute("width");
	}

	/**
	 * User specifies the header and date to return specific plans, equipment, and
	 * usage graphs
	 * 
	 * @param plansEquipHeader
	 * @param desiredBarGraphByDate
	 * @return user desired plans, equipment, and usage graphs
	 */

	public WebElement desiredBarGraphFromList(String plansEquipHeader, String desiredBarGraphByDate) {
		for (WebElement barGraph : allBarGraphsList(plansEquipHeader)) {
			if (barGraphHasDesiredDate(barGraph, desiredBarGraphByDate)) {
				return barGraph;
			}
		}
		System.out.println("COULD NOT FIND DESIRED BAR GRAPH");
		return null;
	}

	/**
	 * 
	 * @param plansEquipHeader
	 *            = the header of charge content that the bar graphs model
	 * @return = list of the plans, equip, and usage bar graphs given the header
	 *         charge content
	 */

	public List<WebElement> allBarGraphsList(String plansEquipHeader) {
		return plansEquipGraphs(plansEquipHeader).findElements(allBarGraphsListLoc);
	}

	/**
	 * THIRD PARTY CHARGES CONTENT
	 */
	public String tableOfThirdPartyCharges(String key) throws Exception {
		Map<String, String> table = createTableOfStringsFromWebElements(headersWithinThirdPartyChargesAsWebElements(),
				columnValsWithinThirdPartyChargesRowAsWebElements());
		System.out.println("THIRD PARTY CHARGES TABLE IS:_" + table);
		return table.get(key);
	}

	public WebElement headerForThirdPartyCharges() {
		return retryElementUntilPresent(allTheHeadersForThirdParty);
	}

	public List<WebElement> headersWithinThirdPartyChargesAsWebElements() throws Exception {
		Callable<List<WebElement>> element = () -> headerForThirdPartyCharges()
			.findElements(headersWithinThirdPartyChargesTableLoc);
		return retryStaleElements(element);
	}

	public List<String> headersWithinThirdPartyCharges() throws Exception {
		return makeListElementsStrings(headersWithinThirdPartyChargesAsWebElements());
	}

	public WebElement rowWithinThirdPartyChargesTableRow() {
		return retryElementUntilVisible(rowWithinThirdPartyChargesTableLoc);
	}

	public List<WebElement> allTheDateKeysForThirdParty() {
		return driver.findElements(allTheDateKeysForThirdPartyLoc);
	}

	public List<WebElement> columnValsWithinThirdPartyChargesRowAsWebElements() throws Exception {
		Callable<List<WebElement>> element = () -> rowWithinThirdPartyChargesTableRow()
				.findElements(cellsWithinThirdPartyChargesRow);
		return retryStaleElements(element);
	}

	public List<String> columnValsWithinThirdPartyChargesRow() throws Exception {
		return makeListElementsStrings(columnValsWithinThirdPartyChargesRowAsWebElements());
	}

	/**
	 * SPRINT PREMIUM CHARGES CONTENT
	 * 
	 * @param chargeDesc
	 * @return
	 * @throws Exception
	 */

	/**
	 * This method is used to split apart charge descriptions within Sprint Premium
	 * content. We need to do this because Sprint Premium csv column data matches
	 * only certain sections of the charge description.
	 * 
	 * @return the first portion of Sprint Premium Charge Descriptions
	 */
	public ArrayList<String> splitSprintPremiumChargeDescIntoDesc() {
		ArrayList<String> listOfSplits = new ArrayList<String>(sprintPremDescWithOutTotalVal().size());
		for (int i = 0; i < sprintPremDescWithOutTotalVal().size(); i++) {
			WebElement e = sprintPremDescWithOutTotalVal().get(i);
			String chargesFullDesc = e.getText();
			if (!chargesFullDesc.contains("/")) {
				continue;
			}
			int lastHyphen = chargesFullDesc.lastIndexOf("/") - 7;
			// need to subtract 1 to get rid of a space at the end
			String chargesDesc = chargesFullDesc.substring(0, lastHyphen - 1);
			listOfSplits.add(chargesDesc);
		}
		return listOfSplits;
	}

	/**
	 * This method is used to split apart charge descriptions within Sprint Premium
	 * content. We need to do this because Sprint Premium csv column data matches
	 * only certain sections of the charge description.
	 * 
	 * @return the second portion of Sprint Premium Charge Descriptions
	 */

	public ArrayList<String> splitSprintPremiumChargeDescIntoDate() {
		ArrayList<String> listOfSplits = new ArrayList<String>(sprintPremDescWithOutTotalVal().size());
		for (int i = 0; i < sprintPremDescWithOutTotalVal().size(); i++) {
			WebElement e = sprintPremDescWithOutTotalVal().get(i);
			String chargesFullDesc = e.getText();
			if (!chargesFullDesc.contains("/")) {
				continue;
			}
			int lastHyphen = chargesFullDesc.lastIndexOf("/") - 7;
			// need to add 2 to get rid of hyphen and space in the beginning
			String chargesDate = chargesFullDesc.substring(lastHyphen + 2, chargesFullDesc.length());
			listOfSplits.add(chargesDate);
		}
		return listOfSplits;
	}

	/**
	 * This method creates a list of identical ptns. These ptns are from the ptn
	 * that defines the Sprint Premium Charges Content.
	 * 
	 * @return list of identical ptns.
	 */
	public List<String> createListOfSprintPremiumPtns() {
		return createListOfIdenticalPtnWebElements(sprintPremiumDescPtnFromTotal(), sprintPremDescWithOutTotalVal());
	}

	/**
	 * This method creates a list of identical names. These names are from the name
	 * that defines the Sprint Premium Charges Content.
	 * 
	 * @return list of identical names.
	 */
	public List<String> createListOfSprintPremiumNames() {
		return createListOfIdenticalAllCapNames(sprintPreiumDescNameFromTotal(), sprintPremDescWithOutTotalVal());
	}

	public String tableOfSprintPremiumCharges(String chargeDesc) throws Exception {
		Map<String, String> table = createTableOfStringsFromWebElements(sprintPremiumDesc(),
				sprintPremiumChargeAmount());
		System.out.println(table);
		return table.get(chargeDesc);
	}

	public WebElement sprintPremiumContent() {
		return driver.findElement(sprintPremiumContentLoc);
	}

	public List<WebElement> sprintPremiumDesc() {
		return sprintPremiumContent().findElements(sprintPremiumDescListLoc);
	}

	public List<WebElement> sprintPremDescWithOutTotalVal() {
		List<WebElement> list = new ArrayList<>();
		List<WebElement> newList = sprintPremiumDesc();
		newList.remove(sprintPremiumDesc().size() - 1);
		list.addAll(newList);
		return list;
	}

	public List<WebElement> sprintPremChargeWithOutTotalVal() {
		List<WebElement> list = new ArrayList<>();
		sprintPremiumChargeAmount().remove(sprintPremiumChargeAmount().size() - 1);
		list.addAll(sprintPremiumChargeAmount());
		return sprintPremiumChargeAmount();
	}

	public WebElement sprintPremiumDescPtnFromTotal() {
		return sprintPremiumContent().findElement(sprintPremiumDescPtnFromTotalLoc);
	}

	public WebElement sprintPreiumDescNameFromTotal() {
		return sprintPremiumContent().findElement(sprintPreiumDescNameFromTotalLoc);
	}

	public List<WebElement> sprintPremiumChargeAmount() {
		return sprintPremiumContent().findElements(sprintPremiumChargeListLoc);
	}

	public List<String> sprintPremiumChargeAmountsAsDecimals() {
		List<String> listOfCostsAsDec = new ArrayList<>(splitSprintPremiumChargeDescIntoDate().size());
		for (WebElement e : sprintPremiumChargeAmount()) {
			String cleanCost = cleanWebElementCost(e);
			listOfCostsAsDec.add(cleanCost);
		}
		return listOfCostsAsDec;
	}

	public List<WebElement> expandButtonAmounts() {
		return retryElementsUntilPresent(BUTTON_AMOUNTS);
	}

	public void openExpandButtons(String headingText) throws InterruptedException {
		List<WebElement> headings = expandButtonHeadings();
		int index = getIndexOfParentElement(headingText, headings, EXPAND_BUTTON_TEXT_TAIL);
		cleanExpandButtonFromParent(headings, index, EXPAND_BUTTON_TAIL);
	}

	public Boolean chargeDescriptionExists(String description) {
		return retryElementUntilPresent(span_text(description)) == null ? false : true;
	}

	private By span_text(String description) {
		return By.xpath(String.format(SPAN_TEXT, description));
	}

	public String getAmountFromCharge(String description) {
		return retryElementUntilPresent(span_text(description)).findElement(AMOUNT_FROM_DESCRIPTION).getText();
	}

	public WebElement viewDetailsFromRowDescription(String rowDescription) {
		return findChildOfParent(retryElementUntilPresent(span_text(rowDescription)), LINK_FROM_DESCRIPTION);
	}

	public WebElement viewDetailsFromPhoneAndRowDescription(String phoneNum, String rowDescription) {
		return retryElementUntilPresent(phone_root(phoneNum)).findElement(span_text(rowDescription))
				.findElement(LINK_FROM_DESCRIPTION);
	}

	public Boolean chargeDescriptionExistsUnderPhoneNumber(String description, String phoneNumber) {
		phoneNumber = phoneNumber.replaceAll("[^\\d.]", "");
		
		return descriptionFromPhone(phoneNumber, description) == null ? false : true;
	}
	
	public Boolean chargeDescriptionCombinedExistsUnderPhoneNumber(String description, String phoneNumber) {
		phoneNumber = phoneNumber.replaceAll("[^\\d.]", "");
		return descriptionCombinedFromPhone(phoneNumber, description) == null ? false : true;
	}

	private List<WebElement> descriptionCombinedFromPhone(String phoneNumber, String description) {
		return retryElementUntilPresent(phone_root(phoneNumber)).findElements(phone_to_combined_description(description));
	}
	
	private WebElement descriptionFromPhone(String phoneNumber, String description) {
		return retryElementUntilPresent(phone_root(phoneNumber)).findElement(phone_to_description(description));
	}

	private By phone_to_description(String description) {
		String loc = String.format(PHONE_TO_DESCRIPTION, description);
		return By.xpath(loc);
	}
	
	private By phone_to_combined_description(String description) {
		String loc = String.format(PHONE_TO_DESCRIPTION, description);
		return By.xpath(loc);
	}

	private By phone_root(String phoneNumber) {
		String PHONE_NUMBER_LOC = "[id^=\"%s\"]";
		String loc = String.format(PHONE_NUMBER_LOC, phoneNumber);
		return By.cssSelector(loc);
	}

	public String getChargeAmountWithPhoneAndDescription(String description, String phoneNumber) {
		phoneNumber = phoneNumber.replaceAll("[^\\d.]", "");
		return descriptionFromPhone(phoneNumber, description).findElement(AMOUNT_FROM_DESCRIPTION).getText();
	}

	public WebElement blueQuestMarkPopUpContentNextToPhoneNumber(String phoneNumber) {
		phoneNumber = phoneNumber.replaceAll("[^\\d.]", "");
		return retryElementUntilPresent(phone_root(phoneNumber)).findElement(PHONE_TO_POPUP);
	}
	
	FilenameFilter filter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.matches("[\\d]{9}[-][\\d]{4}[-][\\d]{2}[-][\\d]{2}[.pdf]");
		}
	};

	public List<List<String>> getRowsOfTableWithID(String phone) {
		String phoneNumber = phone.replaceAll("[^\\d.]", "");
		List<WebElement> rows = retryElementsUntilPresent(rows_of_table_with_id(phoneNumber));
		return createListStringFromRows(rows, DIV);
	}

	private By rows_of_table_with_id(String phone) {
		return By.xpath(String.format(ROWS_OF_TABLE_WITH_ID, phone));
	}
	
	public List<List<String>> getRowsOfTableWithCharges() {
		List<WebElement> rows = retryElementsUntilPresent(By.xpath("//*[@class='bb-peu-charges']//*[@class='row bottom-xs']"));
		return createListStringFromRows(rows, SPAN);
	}
	
	public List<List<String>> getRowsLeftToRighOfTableWithCharges() {
		List<WebElement> rows = retryElementsUntilPresent(By.xpath("//*[@class='bb-peu-charges']"));
		return createListStringFromRowsLeftToRightKeepBlanks(rows, SPAN);
	}
	
	public List<List<String>> getRowsOfTableWithUnlimited() {
		List<WebElement> rows = retryElementsUntilPresent(By.xpath("(//*[@class='service-subscriber-charges-details']//*[@class='charge-type-block'])[10]"));
		return createListStringFromRows(rows, SPAN);
	}
}