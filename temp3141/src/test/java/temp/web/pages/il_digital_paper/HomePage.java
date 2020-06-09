package com.sprint.iice_tests.web.pages.il_digital_paper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.sprint.iice_tests.lib.dao.data.Colors;
import com.sprint.iice_tests.lib.dao.data.Constants;
import com.sprint.iice_tests.web.browser.Browser;
import com.sprint.iice_tests.web.browser.BrowserType;

/**
 * NavBarAndViewBill includes functionality for Web Elements within the main
 * "View My Bill" and the Navigation Bar (Scroll Menu), where you can toggle the
 * tabs for Charges, Usage, Calls/Text Logs, and Equipment Details.
 */

public class HomePage extends TabBase implements TabActions {

	private static final String DATE_PERIOD_DROPDOWN_ICONS = ".dropdown-columns > .row > div:nth-child(2) > .bill-col > a > ng-transclude > span:nth-child(%d)";
	private static final String DATE_PERIOD_DROPDOWN = ".dropdown-columns > .row > div:nth-child(%d) > div:nth-child(%d) > div";
	private static final String COLUMN_PERIOD_DROPDOWN = ".dropdown-columns > .row > div:nth-child(%d)";
	private static final By SUMMARY_MESSAGE = By.cssSelector(".text-block > span > span");
	private static final By ALERT_MESSAGE = By.cssSelector(".text-block > [ng-if=\"!isCsrUser\"]");
	private static final String BILL_ALERTS_TEXT_TAIL = ".text-block > span > span";
	private static final String BILL_ALERTS_ICON_TAIL = ".text-center > i";
	private static final String DATE_ROW_WITH_RANGE = "//*[@class='panel-item']//*[text()='%s']";
	final String TAB_UNDERLINE_COLOR_CSS_VALUE = "border-bottom-color";
	private String TAB_SELECT = "//a[contains(text(),\"%s\")]";

	private static final By BILL_PERIOD_TITLE = By.cssSelector(".bill-period-title");
	private static final By DETAIL_BUTTONS = By.cssSelector(".btn-details");
	private static final By FAIL_WIDGET = By.cssSelector(".widget > span");
	private static final By SUMMARY_MESSAGE_BROAD_SEARCH = By.cssSelector(".summary-msg span");
	private static final By ICON_FROM_DATE_RANGE = By.xpath("./../../../span");
	
	private final By blackLiveBillTopBar = By.xpath("//div[contains(@class, 'bb-top-bar')]");
	private final By blackLiveBillDescLoc = By.xpath("//div[contains(@class, 'col-xs-24 col-lg-6')]");
	private final By blackLiveBillChargeLoc = By.xpath("//div[contains(@class, 'charge-amount')]");
	private final By justTheBlackSectionOfBlackLiveBillLoc = By.xpath("//div[@class='summary-charges']/..");
	private final By redBannerUnderBlackLiveBillLoc = By
			.xpath(".//div[contains(@class, 'notification red-notification')]");
	private final By viewMyBillLoc = By.cssSelector(".light.title");
	private final By BILL_CHARGE_TRENDS = By.cssSelector("#btn-trends");
	private final By NAV_BAR_TABS = By.cssSelector(".scroll-menu > a");
	private final By CHARGE_GRAPHS = By.cssSelector("[data*='historicCharges'] > svg > g > .bb-filled-rect");
	private final By GRAPH_DATE = By.cssSelector("[data*='historicCharges'] > svg > g > .bb-graph-date");
	private final By GRAPH_AMOUNT = By.cssSelector("[data*='historicCharges'] > svg > g > .bb-graph-amount");
	private final By viewSavePDFLinkButtonLoc = By.xpath("//a[@class = 'bb-pdf-download']");
	private final By viewSavePDFLinkButtonRightSideOfPageLoc = By.xpath("//div[@class = 'col-md-10 download-pdf-block']/a[contains(@class, 'bb-pdf-download') and not(contains(@class, 'inactive'))]");
	private final By viewSavePDFIconLoc = By.xpath("./*/*[1]/*/child::*/*/svg[@id = 'pdf']");
	private final By billPeriodLoc = By.cssSelector(".dropdown .open-call-filters");
	private final By billPeriodDropDownlistLoc = By.cssSelector(".bill-col");
	By navBarLoc = By.cssSelector("#bb-menu-navigation > div.row.middle-xs > div.col-xs-24.col-lg-13 > div");
	private final By billAlertsLoc = By.cssSelector("#lnkBillAlerts .message-icon .ico");
	private final By billAlertsText = By.cssSelector(".bill-alert > span");
	private final By billAlertsListLoc = By.cssSelector(".summary-msg > .row > .text-block > span ,.summary-msg > .row > .text-block > span > span");
	private final By billAlertsListStub = By.cssSelector(".summary-msg > .row");

	private final By banNumberLoc = By.cssSelector(".account-number");

	private final By PDF_DOWNLOAD = By.cssSelector(".bb-pdf-download");
	private final String PDF_MESSAGE = ".bb-pdf-download span[ng-show^='%s']";
	private final By POPUP_CLOSE_BUTTON = By
			.cssSelector(".popup.visible > .popup-close-button > a > .ico.bb-svg-icon-close.ico--close");
	private final By fullNavBarLoc = By.cssSelector(".scroll-menu");

	private final By errorPage = By.cssSelector("#main > div.widget.no-bill.margin-top-30 > span:nth-child(1)");
	// private final By errorPage = By.xpath("//*[@id=\"main\"]/div[3]/span[1]");

	private By tab_select(String tab) {
		Object[] args = { tab };
		return By.xpath(formatPath(TAB_SELECT, args));
	}

	private By dropdown_year_period(int col, int row) {
		Object[] args = { col, row };
		return By.cssSelector(formatPath(DATE_PERIOD_DROPDOWN, args));
	}

	private By columnDropDown(int col, String tail) {
		Object[] args = { col };
		return By.cssSelector(formatPath(COLUMN_PERIOD_DROPDOWN, args, tail));
	}

	private By date_range_with_icons(boolean textTrueIconFalse) {
		int i = textTrueIconFalse ? 1 : 2;
		Object[] args = { i };
		return By.cssSelector(formatPath(DATE_PERIOD_DROPDOWN_ICONS, args));
	}

	private By pdf_message(Boolean active) {
		String ngshow = active ? "oliveDocumentId" : "!oliveDocumentId";
		Object[] args = { ngshow };
		return By.cssSelector(formatPath(PDF_MESSAGE, args));
	}

	public WebElement getValidateBillImage() {
		return retryElementUntilPresent(PDF_DOWNLOAD);
	}

	public WebElement viewSavePDFIcon() {
		return viewSavePDFLinkButton().findElement(viewSavePDFIconLoc);
	}

	public WebElement getValidatePDFMessage(Boolean active) {
		return retryElementUntilPresent(pdf_message(active));
	}

	public WebElement tabElement(String tab) {
		return retryElementUntilPresent(tab_select(tab));
	}

	public WebElement banNumber() {
		return retryElementUntilPresent(banNumberLoc);
	}

	public WebElement banNumWithoutRetry() {
		return driver.findElement(banNumberLoc);
	}

	public String banByJSExec() {
		return jsGetTextOfWebElement(banNumWithoutRetry());
	}

	public WebElement getPopupCloseButton() {
		return retryElementUntilPresent(POPUP_CLOSE_BUTTON);
	}

	public WebElement NavBar() throws Exception {
		return retryElementUntilPresent(fullNavBarLoc);
	}

	public WebElement billPeriod() throws Exception {
		Callable<WebElement> element = () -> retryElementUntilPresent(billPeriodLoc);
		return retryStaleElements(element);
		// return retryElementUntilPresent(billPeriodLoc);
	}

	public WebElement billAlerts() {
		return retryElementUntilPresent(billAlertsLoc);
	}

	public WebElement billAlertsText() {
		return retryElementUntilPresent(billAlertsText);
	}

	public WebElement getSummaryMessageIcon(String message) throws Exception {
		return getSiblingElementBasedOnText(billAlertsStub(), message, BILL_ALERTS_TEXT_TAIL, BILL_ALERTS_ICON_TAIL);
	}

	public WebElement viewSavePDFLinkButton() {
		return driver.findElement(viewSavePDFLinkButtonLoc);
	}
	
	public WebElement viewSavePDFLinkButtonRightSideOfPage() {
		return driver.findElement(viewSavePDFLinkButtonRightSideOfPageLoc);
	}
	
	public String hrefOfDownloadButton() {
	return 	viewSavePDFLinkButtonRightSideOfPage().getAttribute("href").toString();
	}
	
	public String targetOfDownloadButton() {
		return 	viewSavePDFLinkButtonRightSideOfPage().getAttribute("target").toString();
	}
	
	public WebElement viewSavePDFLinkIconRightSideOfPage() {
		return viewSavePDFLinkButtonRightSideOfPage().findElement(By.xpath("./*/*[1]"));
	}

	public WebElement viewMyBill() {
		return retryElementUntilPresent(viewMyBillLoc);
	}

	public WebElement redBannerUnderBlackLiveBill() {
		return blackLiveBillTopBar().findElement(redBannerUnderBlackLiveBillLoc);
	}

	public WebElement blackLiveBillTopBar() {
		return retryElementUntilPresent(blackLiveBillTopBar);
	}

	public WebElement justTheBlackSectionOfBlackLiveBill() {
		return driver.findElement(justTheBlackSectionOfBlackLiveBillLoc);
	}

	public WebElement getDateRowIcon(String dateRange) {
		return retryElementUntilPresent(date_row_with_range(dateRange)).findElement(ICON_FROM_DATE_RANGE);
	}

	private By date_row_with_range(String dateRange) {
		return By.xpath(String.format(DATE_ROW_WITH_RANGE, dateRange));
	}

	public WebElement navBarTab(String tab) {
		int index = getIndexOfElementWithTextFromList(tab, navBarTabs());
		return navBarTabs().get(index);
	}

	/**
	 * Gets the webelement Bill Charges Trends button found in the header section
	 */
	public WebElement getBillChargeTrends() {
		return retryElementUntilPresent(BILL_CHARGE_TRENDS);
	}

	/**
	 * Gets the webelement from the navbar with the appropriate link text.
	 * 
	 * @param tab
	 *            = the tab name for either Charges, Usage, Calls/Text Logs or
	 *            Equipment tabs
	 */
	public WebElement getTab(String tab) throws Exception {
		return NavBar().findElement(By.linkText(tab));
	}

	/**
	 * Gets the webelement Bill Charges Trends button found in the header section
	 * 
	 * @param tab
	 *            = the tab name for either Charges, Usage, Calls/Text Logs or
	 *            Equipment tabs
	 */
	public WebElement getSiblingElementBasedOnText(List<WebElement> stub, String textToChoose, String pathToSelector,
			String pathToSelected) throws InterruptedException {
		int index = getIndexOfParentElement(textToChoose, stub, pathToSelector);
		return stub.get(index).findElement(By.cssSelector(pathToSelected));
	}

	private WebElement handleElementVariance(WebElement element, String BUTTON_TEXT_TAIL) {
		try {
			return element.findElement(By.cssSelector(BUTTON_TEXT_TAIL));
		} catch (Exception e) {
			return element;
		}
	}

	public List<WebElement> billAlertsList() {
		return retryElementsUntilPresent(billAlertsListLoc);
	}

	public List<WebElement> billAlertsStub() {
		return retryElementsUntilPresent(billAlertsListStub);
	}

	/**
	 * Gets a List of WebElements representing each bill period in the graphs pop-up
	 */
	public List<WebElement> getChargeGraphs() {
		return retryElementsUntilPresent(CHARGE_GRAPHS);
	}
	
	public List<String> getGraphChargeString() {
		return turnWebElementsToText(getChargeGraphs());
	}

	/**
	 * Gets a List of WebElements representing the dates in the graphs pop-up
	 */
	public List<WebElement> getGraphDate() {
		return retryElementsUntilPresent(GRAPH_DATE);
	}
	
	public List<String> getGraphDateString() {
		return turnWebElementsToText(getGraphDate());
	}

	/**
	 * Gets a List of WebElements representing the dollar amounts for each period
	 * period in the graphs pop-up
	 */
	public List<WebElement> getGraphAmount() {
		return retryElementsUntilPresent(GRAPH_AMOUNT);
	}
	
	public List<String> getGraphAmountString() {
		return turnWebElementsToText(getGraphAmount());
	}

	public List<WebElement> getBillPeriodList() {
		return retryElementsUntilPresent(billPeriodDropDownlistLoc);
	}

	public List<WebElement> getYears() {
		return retryElementsUntilPresent(columnDropDown(1, " span"));
	}

	public List<WebElement> getDateRange() {
		return retryElementsUntilPresent(columnDropDown(2, " span"));
	}

	public List<WebElement> blackLiveBillDesc() {
		return blackLiveBillTopBar().findElements(blackLiveBillDescLoc);
	}

	public List<WebElement> blackLiveBillCharge() {
		return blackLiveBillTopBar().findElements(blackLiveBillChargeLoc);
	}

	public List<WebElement> navBarTabs() {
		return retryElementsUntilPresent(NAV_BAR_TABS);
	}

	public List<WebElement> getDetailButtons() {
		return retryElementsUntilPresent(DETAIL_BUTTONS);
	}

	/**
	 * getDateRangeWithIcons method returns a list of icons inside the bill period
	 * dropdown
	 * 
	 * @param textTrueIconFalse
	 *            = true is the path where icons are present, false is when there is
	 *            none
	 * @return list of icons inside the bill.
	 * @throws InterruptedException
	 */
	public List<WebElement> getDateRangeWithIcons(boolean textTrueIconFalse) {
		return retryElementsUntilPresent(date_range_with_icons(textTrueIconFalse));
	}

	/**
	 * navBarNavsToNewPage method clicks on a nav bar tab which navigates to a new
	 * URL.
	 * 
	 * @param link
	 *            = the tab name for either Charges, Usage, Calls/Text Logs or
	 *            Equipment tabs
	 * @return the URL of the page navigated to after clicking a tab.
	 * @throws InterruptedException
	 */
	public String navBarNavsToNewPage(String link) throws Exception {
		// clicks a tab from the nav bar menu
		clickNavBarTab(link);

		// grabs the URL of the page navigated to
		String tabURL = driver.getCurrentUrl().toString();

		return tabURL;
	}

	public String getBanText() {
		try {
			return banNumber().getText();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Gets the tab and returns the rgba value of yellow bar that appears
	 * 
	 * @param link=
	 *            tab name
	 * @return the rgba value of the yellow bar that appears after clicking a tab
	 * @throws Exception
	 */

	public String tabUnderlinedRGBA(String tab) throws Exception {
		scrollToElement(getTab(tab));
		// gets the rgba value for the tab's "border-bottom-color"
		return getTab(tab).getCssValue(Constants.TAB_UNDERLINE_COLOR_CSS_VALUE);
	}

	/**
	 * billPeriodText method locates the Web Element that displays the billing
	 * period of the bill.
	 * 
	 * @return the text for the bill per iod drop down menu
	 * @throws Exception
	 */
	public String billPeriodText() throws Exception {
		return billPeriod().getText();
	}

	public String getBillAlertsText(String submittedText) throws InterruptedException {
		return getTextFromList(billAlertsList(), submittedText);
	}

	/**
	 * getTextFromList method takes a list of WebElements and creates a list of text
	 * from those elements.
	 * 
	 * @param textList
	 *            is the list of WebElements to be converted to text
	 * @throws Exception
	 */
	public String getTextFromList(List<WebElement> textList, String submittedText) throws InterruptedException {
		// if there is a WebElement that equals the parameter submittedText
		for (WebElement desiredText : textList) {
			System.out.println("This is the text: "+desiredText.getText());
			if (desiredText.getText().equals(submittedText)) {
				String text = desiredText.getText();
				return text;
			}
		}
		return submittedText;
	}

	public List<WebElement> getSummaryMessage() {
		return retryElementsUntilPresent(SUMMARY_MESSAGE);
	}

	public List<WebElement> getAlertMessage() {
		return retryElementsUntilPresent(ALERT_MESSAGE);
	}

	public String getBillAlertText(String message) {
		int x = getIndexOfElementWithTextFromList(message, billAlertsList());
		return billAlertsList().get(x).getText();
	}

	public String getBillAlertSummaryText(String message) {
		int x = getIndexOfElementWithTextFromList(message, getAlertMessage());
		return getAlertMessage().get(x).getText();
	}

	public String redBannerAboveBlackLiveBillText() {
		return redBannerUnderBlackLiveBill().getText();
	}

	public String redBannerUnderBlackLiveBillColorValue() {
		return redBannerUnderBlackLiveBill().getCssValue(Constants.RED_BANNER_UNDER_LIVE_BILL_CSS_VALUE);
	}

	public String blackLiveBillTable(String chargeDesc) throws Exception {
		Map<String, String> tableofChargeDescAndAmounts = createBlackLiveBillTable();
		System.out.println("BLACK LIVE BILL TABLE: " + tableofChargeDescAndAmounts);
		String chargeAmount = tableofChargeDescAndAmounts.get(chargeDesc);
		return chargeAmount;
	}

	public String billPeriodTitle() {
		return retryElementUntilPresent(BILL_PERIOD_TITLE).getText();
	}

	public String viewSavePDFLinkButtonText() {
		return viewSavePDFLinkButton().getText();
	}

	public Boolean specificSummaryMessageIsPresent(String message) {
		return getElementWithTextFromList(message, billAlertsBroadSearchList()) != null;
	}

	private List<WebElement> billAlertsBroadSearchList() {
		return retryElementsUntilPresent(SUMMARY_MESSAGE_BROAD_SEARCH);
	}

	public Boolean viewSavePDFIconIsDisplayed() {
		return viewSavePDFIcon().isDisplayed();
	}

	public int getIndexOfParentElement(String headingText, List<WebElement> expandButtonHeadings,
			String BUTTON_TEXT_TAIL) {
		for (WebElement element : expandButtonHeadings) {
			WebElement child = handleElementVariance(element, BUTTON_TEXT_TAIL);
			if (child.getText().equals(headingText)) {
				return expandButtonHeadings.indexOf(element);
			}
		}
		return 0;
	}

	/**
	 * getBillPeriod() clicks a user specified bill period displayed within the bill
	 * period drop down menu
	 * 
	 * @param desiredDatePeriod
	 *            the bill period that is to be clicked
	 * @throws Exception
	 */

	public void clickBillPeriodFromDropDown(String desiredDatePeriod) throws Exception {
		try {
			clickButtonFromList(getBillPeriodList(), desiredDatePeriod);
		} catch (StaleElementReferenceException e) {
			clickButtonFromList(getBillPeriodList(), desiredDatePeriod);
		}
		wait.until(ExpectedConditions.textToBePresentInElement(billPeriod(), desiredDatePeriod));
	}

	public void clickBillPeriodElement() throws Exception {
		scrollToAndClickElement(billPeriod());
	}

	/**
	 * clickButtonFromList clicks a button within a user specified list of
	 * WebElement buttons
	 * 
	 * @param buttonlist=
	 *            list of WebElement buttons
	 * @param submittedButton=
	 *            the name of the button the user wants to click
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public void clickButtonFromList(List<WebElement> buttonList, String submittedButton) throws Exception {
		// if there is a WebElement that equals the parameter submittedButton
		for (WebElement desiredButton : buttonList) {
			if (desiredButton.getText().equals(submittedButton)) {
				scrollToAndClickElement(desiredButton);
			}
		}
	}

	/**
	 * changeDateRange executes all the steps necessary to change the page's date
	 * range
	 * 
	 * @param year=
	 *            desired year to be selected
	 * @param dateRange=
	 *            desired daterange, takes the form as seen in the dropdown, i.e
	 *            "Feb 01 - Feb 28"
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public void changeDateRange(String year, String dateRange) throws Exception {
		loop: for (int i = 0; i < 5; i++) {
			try {
				resilientClick(billPeriod());
			} catch (StaleElementReferenceException e) {
				resilientClick(billPeriod());
			}
			int yearNum = getIndexOfElementWithTextFromList(year, getYears()) + 1;
			int rangeNum = getIndexOfElementWithTextFromList(dateRange, getDateRange()) + 1;
			resilientClick(retryElementRefreshFromBy(dropdown_year_period(1, yearNum)));
			try {
				resilientClick(retryElementRefreshFromBy(dropdown_year_period(2, rangeNum)));
			} catch (StaleElementReferenceException e) {
				resilientClick(retryElementRefreshFromBy(dropdown_year_period(2, rangeNum)));
			}
			Thread.sleep(8000);
			if (billPeriodText().equals(dateRange)) {
				break loop;
			}
		}
	}

	public void popupWait(String dateRange) {
		wait.until(ExpectedConditions.textToBe(billPeriodLoc, dateRange));
	}

	@Override
	public void openExpandButtons(String expandText) throws InterruptedException {
		// Not necessary, but required for TabActions
	}

	/**
	 * clickNavBarTab method clicks a tab for either Charges, Usage, Calls/Text Logs
	 * or Equipment tabs
	 * 
	 * @param charges
	 *            = the name of the nav bar tab
	 * @throws Exception
	 */

	public void clickNavBarTab(String tab) throws Exception {
		try {
			scrollToAndClickElement(getTab(tab));
		} catch (StaleElementReferenceException e) {
			resilientClick(getTab(tab));
		}

		if (Browser.getBrowserType().equals(BrowserType.CHROME.getBrowserTypeName())) {
			wait.until(ExpectedConditions.attributeToBe(getTab(tab), Constants.TAB_UNDERLINE_COLOR_CSS_VALUE,
					Colors.YELLOW.getColorRGBAValue()));
		} else if (Browser.getBrowserType().equals(BrowserType.FIREFOX.getBrowserTypeName())) {
			wait.until(ExpectedConditions.attributeToBe(getTab(tab), Constants.TAB_UNDERLINE_COLOR_CSS_VALUE,
					Colors.YELLOW.getColorRGBValue()));
		}
	}

	public Map<String, String> createBlackLiveBillTable() throws Exception {
		Map<String, String> tableofChargeDescAndAmounts = createTableOfStringsFromWebElements(blackLiveBillDesc(),
				blackLiveBillCharge());

		return tableofChargeDescAndAmounts;
	}

	public String getEndOfYearTextFromBillPeriod() throws Exception {
		billPeriod().click();
		return activeYearText();
	}

	public String getErrorPageText() {
		return driver.findElement(errorPage).getText();
	}

	public Boolean isErrorPageVisible() {
		return driver.findElement(errorPage).isDisplayed();
	}

	By onlineBillNotAvailableMsgLoc = By.cssSelector("#main > div.widget.no-bill.margin-top-30");
	By billNotAvail = By.xpath("//span[contains(text(),'bill is not available')]");

	public WebElement onlineBillNotAvailableMsg() {
		return driver.findElement(onlineBillNotAvailableMsgLoc);
	}

	public WebElement getFailWidget() {
		return retryElementUntilPresent(FAIL_WIDGET);
	}
	
	public boolean billAlertsMessageExists(String message) {
		return getBillAllertWithMessage(message) != null ? true : false;
	}
	
	private WebElement getBillAllertWithMessage(String message) {
		return retryElementUntilPresent(contains_text(message));
	}

}
