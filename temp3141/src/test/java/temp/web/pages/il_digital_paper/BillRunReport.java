package com.sprint.iice_tests.web.pages.il_digital_paper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.SkipException;

import com.sprint.iice_tests.definitions.DefinitionBase;
import com.sprint.iice_tests.utilities.parser.CLpdfObjects.PrintUtil;
import com.sprint.iice_tests.utilities.test_util.CustomListener;
import com.sprint.iice_tests.web.browser.Browser;

public class BillRunReport extends TabBase {

	private static String pdfDownloadedName;

	public static String getPdfDownloadedName() {
		return pdfDownloadedName;
	}

	public void setPdfDownloadedName(String pdfDownloadedName) {
		synchronized (this.getClass()) {
			BillRunReport.pdfDownloadedName = pdfDownloadedName;
		}
	}

	private static final By ERROR_MESSAGE = By.xpath(
			"//div[contains(@class,'bb-notification')]/div[text()='There is no invoice matching the ID requested']");

	private static final By pdfByBillRunButtonLoc(String billRun) {
		return By.xpath("//td[contains(@class, 'billRunName')]//span[text()='" + billRun
				+ "']/parent::span/parent::td/following-sibling::td");
	}

	public WebElement pdfByBillRunButton(String billRun) {
		return retryElementUntilPresent(pdfByBillRunButtonLoc(billRun));
	}

	/*
	 * private static final By ERROR_MESSAGE1 = By
	 * .xpath("//div[contains(@class,'bb-notification')]/div[text()='Server Error']"
	 * );
	 */
	private final By FILTER_FIELD = By.xpath("//div[@class='form-group']//input");
	private final By filterFieldUAT1Loc = By.xpath("//div[@class='input-filter']//input");
	private final By LOGIN_BUTTON = By.cssSelector(".buttons-panel > button");
	final String DOWNLOAD_FOLDER = "/src/test/resources/TestData";
	File dir = new File(System.getProperty("user.dir") + DOWNLOAD_FOLDER);

	public WebElement getFilterField() {
		WebElement accountButton = null;
		if (Browser.runningOnUAT1()) {
			accountButton = retryElementUntilPresent(filterFieldUAT1Loc);
		} else if (Browser.runningOnUAT2() || Browser.runningOn039()|| Browser.runningOn037()) {
			accountButton = retryElementUntilPresent(FILTER_FIELD);
		}
		return accountButton;
	}

	public By accountRadioButtonUAT1 = By.xpath("//div[@class='ng-scope']/label[contains(text(),' Account Number')]");
	public By accountRadioButtonUAT2 = By.xpath("//label[@class='control-label ng-binding'][3]/input");

	public WebElement getAccountNumberRadioButton() {
		WebElement accountButton = null;
		if (Browser.runningOnUAT1()) {
			accountButton = retryElementUntilPresent(accountRadioButtonUAT1);
		} else if (Browser.runningOnUAT2() || Browser.runningOn039() || Browser.runningOn037()) {
			accountButton = retryElementUntilPresent(accountRadioButtonUAT2);
		}
		return accountButton;
	}

	public By searchInvoicesButtonLoc = By.cssSelector("#bb-bop-search-invoices-radio");

	public WebElement getLoginButton() {
		return retryElementUntilPresent(LOGIN_BUTTON);
	}

	private final By COMPLETE_GO_BUTTON = By.xpath("//button[contains(text(),'Go')]");

	public WebElement getCompleteGoButton() {
		return retryElementUntilPresent(COMPLETE_GO_BUTTON);
	}
	
	private final String DL_LINK = "//span[contains(text(),'%s')]/parent::*/parent::*/parent::*//div[contains(@class,'download-invoice-button')]";
	
	public WebElement getDLLinkButton(String billRun) {
		return retryElementUntilPresent(By.xpath(String.format(DL_LINK, billRun)));
	}
	
	private final By OK_DIALOG = By.xpath("//button[@class='ng-binding confirm']");
	
	public WebElement getOKDialogButton() {
		return retryElementUntilPresent(OK_DIALOG);
	}
	
	/*
	 * This routine polls for a pdf to download in the TestData folder. If the pdf
	 * is not downloaded, it reclicks, a specified number of times.
	 * 
	 */
	
	public void getPDFByEnteringAccountNumber(String accountNumber, String billRun) throws Exception {
		getFilterField().sendKeys(accountNumber);
		
		getPDFByEnteringAccountNumber(billRun);
	}


	public void getPDFByEnteringAccountNumber(String billRun) throws Exception {

		synchronized (this.getClass()) {
			WatchKey watchKey = setWatcher();
			if(System.getProperties().containsKey("runHeadless")&&System.getProperty("runHeadless").equals("true")) {
				if (!completeGoForPDFButtonIsFunctional()) {
					throw new SkipException("COMPLETE GO BUTTON IS NOT FUNCTIONAL WHILE RUNNING HEADLESS");
				}
				Browser.allowSSLHandshake();
				Browser.cookiesThroughRest();
				//Browser.setDownloadSettings();
				
				//Browser.setDownloadSettings(Browser.optionsForChromeHeadless());
				//System.out.println("PAGE SOURCE: "+ driver.getPageSource());
				
				scrollToAndClickElement(getCompleteGoButton());
				Browser.getWindowHandles();
			}
		
			else {
				//System.out.println("PAGE SOURCE: "+ driver.getPageSource());
				scrollToAndClickElement(getCompleteGoButton());
			}
			//scrollToAndClickElement(getCompleteGoButton());}
			boolean pdfDownloaded = false;
			int additionalNumberOfTimesToClick = 3;
			for (int i = 0; i <= additionalNumberOfTimesToClick; i++) {
				pdfDownloaded = pdfCreatedInDownloadFolder(watchKey);
				if (pdfDownloaded) {
					//ReusableDefs.setUpPDFForTestFromFileList(ReusableDefs.desiredJsonTestDataFile());
					break;
				} else if (!pdfDownloaded) {
					scrollToAndClickElement(getCompleteGoButton());
					System.out.println("NEEDED TO RECLICK FOR PDF");
				}
			}

			if (!pdfDownloaded) {
				String fileName = Browser.FILE_PATH.toString() + Browser.fs + "test"
						+ Browser.classMap.get(threadId).toString().replaceAll(".json", "")
						+ ".pdf";
				System.out.println("This is the name of the file that did not download: " + fileName);
				Browser.pdfMap.put(threadId, new File(fileName));
				//Assert.assertTrue(false, "PDF DID NOT DOWNLOAD");
				throw new SkipException("BAN_"+Browser.banMap.get(threadId)+"_BILL_RUN_"+DefinitionBase.billRunForEnv()+"_DID_NOT_DOWNLOAD_");
			}

		}
	}
	
	public void processToDownlad(String accountNumber) throws IOException {
		String url =apiCallForPDF(accountNumber);
		System.out.println("THIS IS THE API CALL: "+url);
		CustomListener.processBuildForWindowsAndUnix(Browser.FILE_PATH, cmdForDownload(url));
	}
	
	public String cmdForDownload(String url) {
//		String cmd = "curl "+url+" -H \"Pragma: no-cache\" ..... -H \"Cache-Control: no-cache\" --compressed -o myfile";
		String cmd = "curl "+url+" -H \"Pragma: no-cache\" ..... -H \"Cache-Control: no-cache\" --compressed -o test.pdf";
		return cmd;
	}
	
	public void openNewURL(String url) throws MalformedURLException, IOException {
		JavascriptExecutor js = (JavascriptExecutor) Browser.getDriverInstance();
//		 String href = js.executeScript("window.open(\""+url+"\",\"_blank\", \"strWindowFeatures\");return this.href;").toString();
//		 System.out.println(href);
		 FileUtils.copyURLToFile(new URL(url), new File(Browser.FILE_PATH+Browser.fs+"479967232-2018-06-01.pdf"));
	}
	//headlessChromeCanDownloadFiles(String url)
	

	public static String apiCallForPDF(String accountNumber) {
		
	String id = "sprint.com";
	int lengthOfID = id.length();
	int offset = 1;
	int testEnvironPos = Browser.site.indexOf(id) +lengthOfID ;
	int serviceNumPos = Browser.getCurrentUrl().lastIndexOf("/");
	String serviceNum = Browser.getCurrentUrl().substring(serviceNumPos+offset);
	System.out.println("This is the service number: "+serviceNum);
	String importantIDOfTestEnviron = Browser.site.substring(0, testEnvironPos);
	System.out.println("This is the importantIDOfTestEnviron: "+importantIDOfTestEnviron);
	String apiCall = null;
	if(Browser.isILPaperRun()) {
		apiCall = importantIDOfTestEnviron+"/api/admin/documents/IL/"+serviceNum+"/service/"+accountNumber+"/pdf?subtype=DEFAULT";
	}
	else {
		apiCall = importantIDOfTestEnviron+"/api/admin/documents/CL/"+serviceNum+"/service/"+accountNumber+"/pdf?subtype=DEFAULT";
	}
	 System.out.println("This is the api call: "+apiCall);
	// String http = apiCall.replaceAll("https", "http");
	// System.out.println("This is the http: "+http);
	return apiCall;
	//https://tvmkg039.test.sprint.com/api/admin/documents/CL/83921696/service/193226862/pdf?subtype=DEFAULT
	//https://tvmkg039.test.sprint.com/api/admin/documents/IL/82946576/service/949992433/pdf?subtype=DEFAULT
	}
	
	public Boolean completeGoForPDFButtonIsFunctional() {
		Boolean enabled = getCompleteGoButton().isEnabled();
		Boolean displayed =getCompleteGoButton().isDisplayed();
		ExpectedCondition<WebElement> toBeClickable =ExpectedConditions.elementToBeClickable(getCompleteGoButton());
		String clickable = toBeClickable.toString();
		wait.until(toBeClickable);
		return enabled&& displayed ;
	}
	
	
	private boolean errorMessageDisplayed() {
		try {
			System.out.println("ERROR MESSAGE IS DISPLAYED");
			return Browser.getDriverInstance().findElement(ERROR_MESSAGE).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * This routine registers a watcher that looks for the creation of a file within
	 * the specified folder destination.
	 * 
	 * @return a register that looks to the folder that will contain the created
	 *         folder
	 * @throws IOException
	 */

	public static WatchKey setWatcher() throws IOException {
		WatchService watcher = FileSystems.getDefault().newWatchService();
		Path dirPath = Paths.get(Browser.FILE_PATH);
		return dirPath.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
	}

	/**
	 * pdfCreatedInDownloadFolder() waits and polls for the creation of a pdf file
	 * within a specific folder location.
	 * 
	 * It will wait until an event occurs in the download folder, TestData.
	 * Specifically, it polls for the creation of a pdf file. The amount of time it
	 * polls for the pdf creation is the specified Browser.getWaitTime(). If the pdf
	 * is downloaded, the routine returns a true value. If it does not download, it
	 * returns a false value.
	 * 
	 * @param watchKey
	 *            = most commonly, WatchKey watchKey
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public boolean pdfCreatedInDownloadFolder(WatchKey watchKey) throws IOException, InterruptedException {
		Boolean isCreated = false;
		long startTime = System.currentTimeMillis();
		long waitTime = (20 * 1000);

		while ((System.currentTimeMillis() - startTime) < (waitTime)) {
			for (WatchEvent<?> event : watchKey.pollEvents()) {

				WatchEvent.Kind<?> kind = event.kind();

				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path fileName = ev.context();

				if (kind == StandardWatchEventKinds.OVERFLOW) {
					System.out.println("OVERFLOWED");
					continue;
				}

				if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
					Thread.sleep(2000);

					if (fileName.toString().endsWith(".pdf")) {
						PrintUtil.printFileDownloaded.accept(fileName.toString());
						String pathToPDFDownloaded = Browser.FILE_PATH + Browser.fs + fileName.toString();
						File desiredPDF = new File(pathToPDFDownloaded);
						Browser.pdfMap.put(threadId, desiredPDF);
						setPdfDownloadedName(pathToPDFDownloaded);
						isCreated = true;
						break;
					}
				}
			}
			if (isCreated)
				break;
		}
		return isCreated;
	}

}