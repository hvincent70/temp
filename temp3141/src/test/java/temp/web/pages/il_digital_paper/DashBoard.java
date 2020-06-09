package com.sprint.iice_tests.web.pages.il_digital_paper;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;

import com.sprint.iice_tests.web.browser.Browser;

public class DashBoard extends TabBase {
	private final By COMPLETE_BILL_RUN_FIELD = By.cssSelector("#billRun");
	private final By COMPLETE_BILL_SORT_BY_DATE = By.xpath("//div[text()='Received']");
	private final By COMPLETE_APPROVED_BILL_RUNS = By.xpath("//div[@class='billrun-completed-status approved']");
	private final By billingOpButtonLoc = By.xpath("//div[@class='bb-route-menu-generator']");
	private final By billingOpWithinListLoc = By
			.xpath("//div[@class='bb-route-menu-generator']/div/select/option[text()='Billing Operations']");
	private static final By findButtonLocX = By.xpath("//div[@class='find-button']");
	private static final By billRunFilterFieldLoc = By.cssSelector("#billRun");

	private static final By completeBillRunButtonLoc = By.xpath("//i[@class='billrun-completed']");
	private static final By MAIN_PAGE_ICON = By.cssSelector(".navbar-brand");
	private static final By SEARCH_INV_RADIO = By.id("bb-bop-search-invoices-radio");
	private static final By ACCOUNT_INPUT = By.id("accountId");
	private static final By GO_BUTTON = By.xpath("//div[@class='action-buttons']/button[contains(@class,'btn-search')]");
	
	public static enum FindBy {
		FILTER_BILLRUN, SEARCH_INVOICE;
	}
	
	public WebElement billingOpButton() {
		return retryElementUntilPresent(billingOpButtonLoc);
	}

	public WebElement billingOpWithinList() {
		return retryElementUntilPresent(billingOpWithinListLoc);
	}

	public void clickBillingOptions() throws InterruptedException {
		synchronized (this.getClass()) {
			Optional<WebElement> billingOpButton = Optional.ofNullable(billingOpButton());
			System.out.println(billingOpButton + " is present: " + billingOpButton.isPresent());
			if (billingOpButton.isPresent()) {
				resilientClick(billingOpButton());
				resilientClick(billingOpWithinList());
			} else {
				// Assert.assertTrue(false, "BILL RUN FIND BUTTON IS NULL");
				throw new SkipException("BILLING OPERATIONS BUTTON IS NULL");
			}
		}
	}

	public WebElement getCompleteBillRunField() {
		return retryElementUntilPresent(COMPLETE_BILL_RUN_FIELD);
	}

	public WebElement completeBillRunButton() {
		return retryElementUntilPresent(completeBillRunButtonLoc);
	}

	public WebElement findButton() {
		return retryElementUntilPresent(findButtonLocX);
	}

	public WebElement billRunFilterField() {
		return retryElementUntilPresent(billRunFilterFieldLoc);
	}
	
	public WebElement searchInvoiceRadio() {
		return retryElementUntilPresent(SEARCH_INV_RADIO);
	}
	
	public WebElement accountIdInput() {
		return retryElementUntilPresent(ACCOUNT_INPUT);
	}
	
	synchronized public void sendTextToCompleteBillRunUAT2(String text) throws Exception {
		sendTextToCompleteBillRunUAT2(text, FindBy.FILTER_BILLRUN);
	}

	synchronized public void sendTextToCompleteBillRunUAT2(String text, FindBy findBy) throws Exception {
		Optional<WebElement> findButton = Optional.ofNullable(findButton());
		System.out.println(findButton + " is present: " + findButton.isPresent());
		if (findButton.isPresent()) {
			resilientClick(findButton());

			if (findBy == FindBy.SEARCH_INVOICE) {
				resilientClick(searchInvoiceRadio());
				accountIdInput().sendKeys(text);
			} 
			if (findBy == FindBy.FILTER_BILLRUN) {
				getCompleteBillRunField().sendKeys(text);
			}
			
			resilientClick(getCompleteGoButton());

		} else {
			// Assert.assertTrue(false, "BILL RUN FIND BUTTON IS NULL");
			throw new SkipException("BILL RUN FIND BUTTON IS NULL");
		}
		
		if (findBy == FindBy.FILTER_BILLRUN) {
			Optional<WebElement> completeButton = Optional.ofNullable(completeBillRunButton());
			System.out.println(completeButton + " is present: " + completeButton.isPresent());
			if (completeButton.isPresent()) {

				resilientClick(completeBillRunButton());
				sortAndClickApprovedBillRun();
			} else {
				// Assert.assertTrue(false, "THERE ARE NO COMPLETE RUNS");
				throw new SkipException("THERE ARE NO COMPLETE RUNS");
			}
		}
	}

	synchronized public void sendTextToCompleteBillRun(String text, FindBy findBy) throws Exception {
		if (Browser.runningOnUAT1()) {
			sendTextToCompleteBillRunUAT1(text);
		} else if (Browser.runningOnUAT2() || Browser.runningOn039()||Browser.runningOn037()) {
			sendTextToCompleteBillRunUAT2(text, findBy);
		}
	}

	By billRunTextBoxUAT1Loc = By.xpath("//div[@class='billrun-name-field']/input");

	public WebElement billRunTextBoxUAT1() {
		return retryElementUntilPresent(billRunTextBoxUAT1Loc);
	}

	By goButtonUAT1Loc = By.xpath("//button[text()='Go']");

	public WebElement goButtonUAT1() {
		return retryElementUntilPresent(goButtonUAT1Loc);
	}

	By receivedUAT1Loc = By.xpath("//div[@id='billRun-completed']//div[text()='Received']");

	public WebElement receivedUAT1() {
		return retryElementUntilPresent(receivedUAT1Loc);
	}

	By approvedBillRunUAT1Loc = By.xpath("//tr[contains(@class, 'status-approved')]//td[1]");

	public WebElement approvedBillRunUAT1() {
		return retryElementUntilPresent(approvedBillRunUAT1Loc);
	}

	synchronized public void sendTextToCompleteBillRunUAT1(String text) throws Exception {
		Optional<WebElement> billRunBox = Optional.ofNullable(billRunTextBoxUAT1());
		System.out.println("billRunBox" + " is present: " + billRunBox.isPresent());
		if (billRunBox.isPresent()) {
			billRunTextBoxUAT1().sendKeys(text);
			resilientClick(goButtonUAT1());
			receivedUAT1().click();
			receivedUAT1().click();
			resilientClick(approvedBillRunUAT1());
		} else {
			// Assert.assertTrue(false, "BILL RUN TEXT BOX IS NULL");
			throw new SkipException("BILL RUN TEXT BOX IS NULL");
		}
	}

	public void sortAndClickApprovedBillRun() throws Exception {
		completeBillSortByDateAscending();
		clickApprovedBillRun();
	}

	private final By COMPLETE_GO_BUTTON = By.xpath("//button[text()='Go']");

	public WebElement getCompleteGoButton() {
		return retryElementUntilPresent(COMPLETE_GO_BUTTON);
	}

	// div[text()='No bill run']
	WebElement noCompleteBilRun() {
		return retryElementUntilPresent(By.xpath(
				"//div[@id='billrun-completed']/section/billrun-table/div/div/div[2]/div[text()='No bill run']"));
	}

	By rejectedBillRunLoc = By.xpath(
			"//td[@class='billrun-status bb-grid-cell-0-9 bb-grid-cell-callback']/span/span/billrun-completed-status/div/div[text()='Rejected']");

	public WebElement rejectedBillRun() {
		return retryElementUntilPresent(rejectedBillRunLoc);
	}

	public void clickApprovedBillRun() throws Exception {
		
		if ((!noCompleteBilRun().isDisplayed())) {
				
				if (Optional.ofNullable(getCompleteApprovedBillRuns()).isPresent()) {
					//(rejectedBillRun().equals(null))&&Optional.ofNullable(getCompleteApprovedBillRuns()).isPresent()
					resilientClick(getCompleteApprovedBillRuns().get(0).findElement(By.xpath("./../../../../..")));
				}
		
				else {
					throw new SkipException("THERE IS NO APPROVED BILL RUN");
				}
				
		} else {
			// Assert.assertFalse(true, "THERE IS NO APPROVED BILL RUN");
			throw new SkipException("THERE IS NO APPROVED BILL RUN");
		}
}


	public WebElement getCompleteBillSortByDate() {
		int ignoreHiddenSortButtonAndGrabVisibleButton = 1;
		List<WebElement> sortByDateList = retryElementsUntilPresent(COMPLETE_BILL_SORT_BY_DATE);
		return sortByDateList.get(ignoreHiddenSortButtonAndGrabVisibleButton);
	}

	public void completeBillSortByDateAscending() throws InterruptedException {
		resilientClick(getCompleteBillSortByDate());
		Thread.sleep(500);
		resilientClick(getCompleteBillSortByDate());
		Thread.sleep(500);
	}

	public List<WebElement> getCompleteApprovedBillRuns() {

		return retryElementsUntilPresent(COMPLETE_APPROVED_BILL_RUNS);
	}

	public WebElement getMainPage() {
		return retryElementUntilPresent(MAIN_PAGE_ICON);
	}

}