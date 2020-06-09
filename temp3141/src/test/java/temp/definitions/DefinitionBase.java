package com.sprint.iice_tests.definitions;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.sprint.iice_tests.lib.dal.services.AccountService;
import com.sprint.iice_tests.lib.dao.data.Buttons;
import com.sprint.iice_tests.lib.dao.data.Colors;
import com.sprint.iice_tests.lib.dao.data.Constants;
import com.sprint.iice_tests.lib.dao.data.Tab;
import com.sprint.iice_tests.lib.dao.vo.Account;
import com.sprint.iice_tests.utilities.parser.CLpdfObjects.PrintUtil;
import com.sprint.iice_tests.utilities.test_util.CustomSoftAssert;
import com.sprint.iice_tests.utilities.test_util.DataFileFinder;
import com.sprint.iice_tests.web.browser.Browser;
import com.sprint.iice_tests.web.browser.BrowserType;
import com.sprint.iice_tests.web.pages.cl_digital_paper.EquipTab;
import com.sprint.iice_tests.web.pages.cl_digital_paper.SummaryTab;
import com.sprint.iice_tests.web.pages.cl_digital_paper.TabActionsCL;
import com.sprint.iice_tests.web.pages.il_digital_paper.BillRunReport;
import com.sprint.iice_tests.web.pages.il_digital_paper.BriteBillLogin;
import com.sprint.iice_tests.web.pages.il_digital_paper.CallsTextLogTab;
import com.sprint.iice_tests.web.pages.il_digital_paper.ChargesTab;
import com.sprint.iice_tests.web.pages.il_digital_paper.DashBoard;
import com.sprint.iice_tests.web.pages.il_digital_paper.EquipmentDetailsTab;
import com.sprint.iice_tests.web.pages.il_digital_paper.TabActions;
import com.sprint.iice_tests.web.pages.il_digital_paper.TabBase;
import com.sprint.iice_tests.web.pages.il_digital_paper.UsageTab;

public abstract class DefinitionBase extends TabBase {
	protected Account account = Browser.accountMap.get(threadId);
	BillRunReport bill;
	BriteBillLogin login;
	DashBoard dash;
	Set<String> failedMethods;

	private final String CHARGES = "Charges";

	private final String SUMMARY = "Summary";

	private final String EQUIPMENT = "Equipment";

	private final String USAGE = "Usage";

	private final String CALL_LOGS = "Call Logs";

	public Function<By, List<String>> getListOfStringsFromBy = i -> createListStringFromRowOnlyDispalyedElements(
			retryElementsUntilPresent(i));

	public Function<By, List<String>> getListOfStringsFromByKeepBlanks = i -> turnWebElementsToTextKeepBlanks(
			retryElementsUntilPresent(i));

	public Function<By, String> combineListOfStringsDefinedWithBy = i -> String.join(" ",
			turnWebElementsToTextKeepBlanks(retryAllElementsUntilDisplayed(i)));

	public Function<By, Boolean> elementWithByNotNull = i -> getDisplayedElement(retryElementsUntilPresent(i)) != null;

	public BiFunction<String, String, String> classOfElementWithText = (i,
			j) -> getElementAttribute(retryElementUntilPresent(By.xpath(String.format(i, j))), "class");

	public Function<By, String> stringFromBy = i -> retryElementUntilPresent(i).getText();

	public BiFunction<By, By, List<String>> getListOfStringsFromByOfDisplayedParentWtihChildren = (i,
			j) -> createListStringFromRowOnlyDispalyedElements(
					getDisplayedElement(retryElementsUntilPresent(i)).findElements(j));

	public By onlineBillNotAvailableMsgLoc = By.cssSelector("#main > div.widget.no-bill.margin-top-30");
	public By billNotAvail = By.xpath("//span[contains(text(), 'bill is not available')]");

	public final String ACCOUNT_BASE = "account.getTableValues().get(%d).get%s%d()";
	public final String JSON_BASE = "	\"%s%d\": \"%s\",";

	public final String[] ARRAY_VALUES = { "ZerothVal", "FirstVal", "SecondVal", "ThirdVal", "FourthVal", "FifthVal",
			"SixthVal", "SeventhVal", "EighthVal", "NinthVal", "TenthVal", "EleventhVal", "TwelthVal", "ThirteenthVal",
			"FourteenthVal", "FifteenthVal", "SixteenthVal" , "SeventeenthVal", "EighteenthVal", "NineteenthVal", 
			"TwentiethVal", "TwentyfirstVal", "TwentysecondVal"};


	public final String[] JSON_VALUES = { "zerothVal", "firstVal", "secondVal", "thirdVal", "fourthVal", "fifthVal",
			"sixthVal", "seventhVal", "eighthVal", "ninthVal", "tenthVal", "eleventhVal", "twelthVal", "thirteenthVal",
			"fourteenthVal", "fifteenthVal", "sixteenthVal", "seventeenthVal", "eighteenthVal", "nineteenthVal", 
			"twentiethVal", "twentyfirstVal", "twentysecondVal"};

	public WebElement onlineBillNotAvailableMsg() {
		return Browser.getDriverInstance().findElement(onlineBillNotAvailableMsgLoc);
	}

	protected CallsTextLogTab callsTextLog;
	protected CustomSoftAssert softAssert = new CustomSoftAssert();
	protected static AccountService accountService = new AccountService();

	public TabActions getTabActionsInstanceForTab(String tab) {
		TabActions actions = null;
		if (tab.equalsIgnoreCase(Tab.CHARGES.getTab())) {
			actions = new ChargesTab();
		} else if (tab.equalsIgnoreCase(Tab.EQUIPMENT_DETAILS.getTab())) {
			actions = new EquipmentDetailsTab();
		} else if (tab.equalsIgnoreCase(Tab.CALLS_TEXTS_LOGS.getTab())) {
			actions = new CallsTextLogTab();
		} else if (tab.equalsIgnoreCase(Tab.USAGE.getTab())) {
			actions = new UsageTab();
		}
		return actions;
	}

	public TabActionsCL getTabActionsInstanceForTabCL(String tab) {
		TabActionsCL actions = null;
		switch (tab) {
		case CHARGES:
			actions = new com.sprint.iice_tests.web.pages.cl_digital_paper.ChargesTab();
			break;
		case SUMMARY:
			actions = new SummaryTab();
			break;
		case EQUIPMENT:
			actions = new EquipTab();
			break;
		case USAGE:
			actions = new com.sprint.iice_tests.web.pages.cl_digital_paper.UsageTab();
			break;
		case CALL_LOGS:
			actions = new com.sprint.iice_tests.web.pages.cl_digital_paper.CallLogsTab();
			break;
		}
		return actions;
	}

	public void all1_DimDataMatches(List<String> list, String[] arr, String string) {
		try {
			for (int i = 0; i < arr.length; i++) {
				softAssert.assertEquals(list.get(i), arr[i], string + " in position " + i);
			}
		} catch (IndexOutOfBoundsException ie) {
			softAssert.assertFalse(true, string + " missing data");
		} catch (NullPointerException np) {
			softAssert.assertFalse(true, string + " data object not found");
		}
	}

	public void all2_DimDataMatches(List<List<String>> list, String[][] arr, String s) {
		try {
			for (int i = 0; i < arr.length; i++) {
				for (int j = 0; j < arr[i].length; j++) {
					softAssert.assertEquals(list.get(i).get(j), arr[i][j], s + " in row " + i + " in column " + j);
				}
			}
		} catch (IndexOutOfBoundsException ie) {
			softAssert.assertFalse(true, s + " missing data");
		} catch (NullPointerException np) {
			softAssert.assertFalse(true, s + " data table not found");
		}
	}

	public void print1_DimJSONAndArray(List<String> list, int list_num) {
		StringBuilder sb = new StringBuilder("String[] TABLE_VALUES = ");
		sb.append("{");
		for (int i = 0; i < list.size(); i++) {
			Object[] args = { JSON_VALUES[i], 0, list.get(i) };
			Object[] args1 = { list_num, ARRAY_VALUES[i], 0 };
			System.out.println(String.format(JSON_BASE, args));
			if (i == list.size() - 1) {
				sb.append(String.format(ACCOUNT_BASE, args1) + " ");
				sb.append("}; ");
			} else {
				sb.append(String.format(ACCOUNT_BASE, args1) + ", ");
			}
		}
		System.out.println(sb.toString());
	}

	public void printJSONAndArray(List<List<String>> list, int list_num) {
		StringBuilder sb = new StringBuilder("String[][] TABLE_VALUES = ");
		sb.append("{");
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).size(); j++) {
				Object[] args = { JSON_VALUES[j], i, list.get(i).get(j) };
				Object[] args1 = { list_num, ARRAY_VALUES[j], i };
				System.out.println(String.format(JSON_BASE, args));
				if (j == 0) {
					sb.append("{");
					sb.append(String.format(ACCOUNT_BASE, args1) + ", ");
				} else if (j == list.get(i).size() - 1) {
					sb.append(String.format(ACCOUNT_BASE, args1) + " ");
					sb.append("}, ");
				} else {
					sb.append(String.format(ACCOUNT_BASE, args1) + ", ");
				}
			}
		}
		String s = sb.toString();
		s = s.substring(0, s.length() - 2);
		s = s + "\\};";
		s = s.replace("\\", "");
		System.out.println(s);
	}
	
	public void printJSONAndArray(List<List<String>> list, int list_num, int max) {
		StringBuilder sb = new StringBuilder("String[][] TABLE_VALUES = ");
		sb.append("{");
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).size(); j++) {
				Object[] args = { JSON_VALUES[j], i, list.get(i).get(j) };
				Object[] args1 = { list_num, ARRAY_VALUES[j], i };
				System.out.println(String.format(JSON_BASE, args));
				if (j == 0) {
					sb.append("{");
					sb.append(String.format(ACCOUNT_BASE, args1) + ", ");
				} else if (j == list.get(i).size() - 1) {
					sb.append(String.format(ACCOUNT_BASE, args1) + " ");
					sb.append("}, ");
				} else {
					sb.append(String.format(ACCOUNT_BASE, args1) + ", ");
				}
			}
		}
		String s = sb.toString();
		s = s.substring(0, s.length() - 2);
		s = s + "\\};";
		s = s.replace("\\", "");
		System.out.println(s);
	}

	public static Consumer<SoftAssert> assertSoftAsserts = (x -> x.assertAll());

	public void testAssertList(List<SoftAssert> list) {
		list.stream().forEachOrdered(x -> assertSoftAsserts.accept(x));
	}

	public void hardAssert(boolean bool, WebElement e) {
		if (bool && e == null) {
			Assert.assertFalse(e == null);
		}
	}

	public String getButtonActionsInstance(String button) {
		String buttonActions = null;
		if (button.equals(Buttons.ACCOUNT_CHARGES.getButton())) {
			buttonActions = Buttons.ACCOUNT_CHARGES.getButton();
		} else if (button.equalsIgnoreCase(Buttons.DEVICE_ACCESSORY.getButton())) {
			buttonActions = Buttons.DEVICE_ACCESSORY.getButton();
		} else if (button.equalsIgnoreCase(Buttons.EVERYTHING_DATA_1500_SHARED_ANYTIME.getButton())) {
			buttonActions = Buttons.EVERYTHING_DATA_1500_SHARED_ANYTIME.getButton();
		} else if (button.equalsIgnoreCase(Buttons.FREE_AND_CLEAR_ADD_A_PHONE.getButton())) {
			buttonActions = Buttons.FREE_AND_CLEAR_ADD_A_PHONE.getButton();
		} else if (button.equalsIgnoreCase(Buttons.GOVERNMENT_TAXES_AND_FEES.getButton())) {
			buttonActions = Buttons.GOVERNMENT_TAXES_AND_FEES.getButton();
		} else if (button.equalsIgnoreCase(Buttons.PLANS_AND_EQUIPMENT.getButton())) {
			buttonActions = Buttons.PLANS_AND_EQUIPMENT.getButton();
		} else if (button.equalsIgnoreCase(Buttons.PLANS_AND_EQUIPMENT_AND_USAGE.getButton())) {
			buttonActions = Buttons.PLANS_AND_EQUIPMENT_AND_USAGE.getButton();
		} else if (button.equalsIgnoreCase(Buttons.SPRINT_PREMIUM_SERVICES.getButton())) {
			buttonActions = Buttons.SPRINT_PREMIUM_SERVICES.getButton();
		} else if (button.equalsIgnoreCase(Buttons.RECENT_ADJUSTMENTS.getButton())) {
			buttonActions = Buttons.RECENT_ADJUSTMENTS.getButton();
		} else if (button.equalsIgnoreCase(Buttons.ADJUSTMENTS.getButton())) {
			buttonActions = Buttons.ADJUSTMENTS.getButton();
		} else if (button.equalsIgnoreCase(Buttons.RECENT_CHARGES.getButton())) {
			buttonActions = Buttons.RECENT_CHARGES.getButton();
		} else if (button.equalsIgnoreCase(Buttons.SPRINT_SURCHARGES.getButton())) {
			buttonActions = Buttons.SPRINT_SURCHARGES.getButton();
		} else if (button.equalsIgnoreCase(Buttons.THIRD_PARTY_CHARGES.getButton())) {
			buttonActions = Buttons.THIRD_PARTY_CHARGES.getButton();
		} else if (button.equalsIgnoreCase(Buttons.UNLIMITED_MY_WAY.getButton())) {
			buttonActions = Buttons.UNLIMITED_MY_WAY.getButton();
		} else if (button.equalsIgnoreCase(Buttons.PAYMENTS_THANK_YOU.getButton())) {
			buttonActions = Buttons.PAYMENTS_THANK_YOU.getButton();
		} else if (button.equalsIgnoreCase(Buttons.IMMEDIATE_CHARGES_ACCRUED.getButton())) {
			buttonActions = Buttons.IMMEDIATE_CHARGES_ACCRUED.getButton();
		} else if (button.equalsIgnoreCase(Buttons.FAIR_AND_FLEXIBLE_PLAN.getButton())) {
			buttonActions = Buttons.FAIR_AND_FLEXIBLE_PLAN.getButton();
		} else if (button.equalsIgnoreCase(Buttons.UNLIMITED_FREEDOM_TALK_TXT_DATA.getButton())) {
			buttonActions = Buttons.UNLIMITED_FREEDOM_TALK_TXT_DATA.getButton();
		} else if (button.equalsIgnoreCase(Buttons.SPRINT_BETTER_3GB_CHOICE.getButton())) {
			buttonActions = Buttons.SPRINT_BETTER_3GB_CHOICE.getButton();
		} else if (button.equalsIgnoreCase(Buttons.SELECT_DATE_RANGE.getButton())) {
			buttonActions = Buttons.SELECT_DATE_RANGE.getButton();
		} else if (button.equalsIgnoreCase(Buttons.PLANES_EQUIPO_Y_USO.getButton())) {
			buttonActions = Buttons.PLANES_EQUIPO_Y_USO.getButton();
		} else if (button.equalsIgnoreCase(Buttons.PLANES_Y_EQUIPO.getButton())) {
			buttonActions = Buttons.PLANES_Y_EQUIPO.getButton();
		} else if (button.equalsIgnoreCase(Buttons.SPRINT_BETTER_6GB_CHOICE.getButton())) {
			buttonActions = Buttons.SPRINT_BETTER_6GB_CHOICE.getButton();
		}

		return buttonActions;
	}

	public Colors getEnumColorFromUserSpecifiedColor(String color) {
		Colors rgb = null;

		if (color.equalsIgnoreCase(Colors.RED.getColorText())) {
			rgb = Colors.RED;
		} else if (color.equalsIgnoreCase(Colors.GRAPH_RED.getColorText())) {
			rgb = Colors.GRAPH_RED;
		} else if (color.equalsIgnoreCase(Colors.YELLOW.getColorText())) {
			rgb = Colors.YELLOW;
		} else if (color.equalsIgnoreCase(Colors.GRAPH_YELLOW.getColorText())) {
			rgb = Colors.GRAPH_YELLOW;
		} else if (color.equalsIgnoreCase(Colors.GREEN.getColorText())) {
			rgb = Colors.GREEN;
		} else if (color.equalsIgnoreCase(Colors.ROAMING_GREEN.getColorText())) {
			rgb = Colors.ROAMING_GREEN;
		} else if (color.equalsIgnoreCase(Colors.BLUE.getColorText())) {
			rgb = Colors.BLUE;
		} else if (color.equalsIgnoreCase(Colors.GRAPH_BLUE.getColorText())) {
			rgb = Colors.GRAPH_BLUE;
		} else if (color.equalsIgnoreCase(Colors.LIGHT_GREY.getColorText())) {
			rgb = Colors.LIGHT_GREY;
		} else if (color.equalsIgnoreCase(Colors.MEDIUM_GREY.getColorText())) {
			rgb = Colors.MEDIUM_GREY;
		} else if (color.equalsIgnoreCase(Colors.DARK_GREY.getColorText())) {
			rgb = Colors.DARK_GREY;
		}
		return rgb;
	}

	public WebElement getRadioButton(String radioButtonName) {
		callsTextLog = new CallsTextLogTab();
		WebElement radioButton = null;
		if (radioButtonName.equalsIgnoreCase("voice")) {
			radioButton = callsTextLog.voiceButton();
		} else if (radioButtonName.equalsIgnoreCase("roaming")) {
			radioButton = callsTextLog.roamingButton();
		} else if (radioButtonName.equalsIgnoreCase("text")) {
			radioButton = callsTextLog.textbutton();
		} else if (radioButtonName.equalsIgnoreCase("filter options")) {
			radioButton = callsTextLog.filterButton();
		} else if (radioButtonName.equalsIgnoreCase("International Direct Connect")) {
			radioButton = callsTextLog.internationalDirectConnectButton();
		}
		return radioButton;
	}

	protected String checkPhoneNumFormat(String headingText) {
		if (headingText.replaceAll("[^\\d.]", "").length() != 10) {
			return headingText.replaceAll("[^\\d.]", "");
		}
		return headingText;
	}

	public void testStringtIsDesiredColor(String text, Colors val) {
		softAssert.assertTrue(colorValuesCorrectInRGBorRGBA(text, val), "The string is the desired color.");
	}

	/**
	 * This routine will determine whether a text's color is the expected color.
	 * Though Chrome usually expects rgba values, it has occasionally expected rgb
	 * values (e.g. bar graph color). This routine will ascertain that, regardless
	 * of the color's value being in rgb or rgba format, the color is correct.
	 * 
	 * @param text
	 *            = the css color value of the text under test
	 * @param val
	 *            = the enum color we are testing the text color against.
	 * @return true if the text color equals either the rgb value or rgba value of
	 *         the enum color. Returns false otherwise.
	 */

	public boolean colorValuesCorrectInRGBorRGBA(String text, Colors val) {
		// System.out.println("the rbg val from enum is: " + val.getColorRGBValue() + "
		// and from the website is: " + text);
		return text.equals(val.getColorRGBValue()) || text.equals(val.getColorRGBAValue());
	}

	public boolean browserIsChrome() {
		// System.out.println("Here is my browser: " + Browser.getBrowserType());
		return Browser.getBrowserType().equals(BrowserType.CHROME.getBrowserTypeName());
	}

	public boolean browserIsFirefox() {
		// System.out.println("Here is my browser: " + Browser.getBrowserType());
		return Browser.getBrowserType().equals(BrowserType.FIREFOX.getBrowserTypeName());
	}

	public boolean browserIsMobileChrome() {
		return Browser.getBrowserType().equals(BrowserType.MOBILE_CHROME.getBrowserTypeName());
	}

	public void testHeavyFontWeightCrossBrowser(String text) {
		Double TextfontWeight = makeStringADouble(text);
		Double controlFontWeight = makeStringADouble(Constants.FONT_WEIGHT_300);

		softAssert.assertTrue(TextfontWeight > controlFontWeight, "This font is not larger.");
	}

	public double makeStringADouble(String text) {
		double stringIntoDouble = Double.valueOf(text).doubleValue();
		return stringIntoDouble;
	}

	protected BiConsumer<List<String>, List<String>> testColumnData = (csv, website) -> softAssert.assertEquals(csv,
			website, "csv data matches website content");

	public static String jsGetBillErrorMsg() {
		JavascriptExecutor js = (JavascriptExecutor) Browser.getDriverInstance();
		return js.executeScript("return document.getElementsByClassName('widget no-bill margin-top-30', args);")
				.toString();
	}

	public static Boolean billUnavailableMsgDisplayed() {
		String billError = jsGetBillErrorMsg();
		Boolean isNull = billError.equals(null);
		int lengthOfErrorMsgByJS = 2;
		Boolean isLengthZero = billError.length() <= lengthOfErrorMsgByJS;
		Boolean isEmpty = billError.isEmpty();

		return (!isNull && !isLengthZero && isEmpty);
	}

	public String[] createStubs(String[] m) {
		int LENGTH = 30; // xpath selectors will not work if text exceeds a certain length, 50 is long
							// enough to be unique
		String[] s = new String[m.length];
		for (int i = 0; i < m.length; i++) {
			s[i] = m[i].substring(0, LENGTH);
		}
		return s;
	}

	protected static Account returnTestAccount(String test) throws Exception {
		// return accountService.getAccount(grabJsonPathForTest(test));
		return accountService.getAccount((test));
	}

	public final static String billRunForEnv() throws Exception {
		Account account = Browser.accountMap.get(Thread.currentThread().getId());
		String billRun = "BillRunIsNull";
		if (Browser.isILPaperRun()) {
			billRun = account.getBillRun();
		} else if (Browser.runningOnUAT1()) {
			billRun = account.getBillRun();
		} else if (Browser.runningOnUAT2() || Browser.runningOn039()||Browser.runningOn037()) {
			billRun = account.getBillRun2();
		}
		return billRun;
	}

	public static String desiredJsonTestDataFile() {

		Object runnerName = Browser.classMap.get(Thread.currentThread().getId());
		final String ban = Browser.banMap.get(Thread.currentThread().getId());

		Predicate<File> banIsCorrect = (file) -> accountService.getAccount(file.getAbsolutePath()).getBan().equals(ban);
		Predicate<File> correctTestNameForData = file -> file.getName().equals(runnerName);

		List<File> fileList = DataFileFinder.getTestClassList();
		// File desiredFile =
		// fileList.parallelStream().filter(correctTestNameForData).peek(PrintUtil.printTestName)
		// .filter(banIsCorrect).peek(PrintUtil.printThreadID).findAny().get();
		File desiredFile = fileList.parallelStream().filter(correctTestNameForData).filter(banIsCorrect).findAny()
				.get();
		return (DataFileFinder.getTestDataPathByName(desiredFile.getName()));
	}

	public WebElement getElementDirectlyUnderColumnHeadingWithText(String column, String text, By elementsToChoose) {
		WebElement e = retryElementUntilPresent(By.xpath(String.format(column, text)));
		int x = e.getLocation().getX();
		return webElementInX_Pos(retryElementsUntilPresent(elementsToChoose), x);
	}

	public String getStringDefinedByParentDefinedByTextToChild(String parent, String text, By child) {
		return retryElementUntilPresent(By.xpath(String.format(parent, text))).findElement(child).getText();
	}

	public void setUp() throws Exception {
		dash = new DashBoard();
		bill = new BillRunReport();
		dash.clickBillingOptions();
		String billRun = billRunForEnv();
		PrintUtil.printBillRun.accept(billRun);
		String banNumber = account.getBan();
		
		//1. FILTER_BILLRUN, 2. SEARCH_INVOICE
		proceedToFindBy(DashBoard.FindBy.SEARCH_INVOICE, banNumber, billRun);
	}
	
	private void proceedToFindBy(DashBoard.FindBy findBy, String banNumber, String billRun) throws Exception {
		switch (findBy) {
			case FILTER_BILLRUN: 
				proceedToFilterBillRun(banNumber, billRun);
				break;
			
			case SEARCH_INVOICE:
				proceedToSearchInvoice(banNumber, billRun);
				break;
		}
	}
	
	//Old way of downloading PDF
	synchronized void proceedToFilterBillRun(String banNumber, String billRun) throws Exception {
		dash.sendTextToCompleteBillRun(billRun, DashBoard.FindBy.FILTER_BILLRUN);
		dash.resilientClick(bill.getAccountNumberRadioButton());
		bill.getPDFByEnteringAccountNumber(banNumber, billRun);
	}
	
	//New way of downloading PDF
	synchronized void proceedToSearchInvoice(String banNumber, String billRun) throws Exception {
		dash.sendTextToCompleteBillRun(banNumber, DashBoard.FindBy.SEARCH_INVOICE);
		dash.resilientClick(bill.getDLLinkButton(billRun));
		dash.resilientClick(bill.getOKDialogButton());
		bill.getPDFByEnteringAccountNumber(billRun);
	}

	public final static void setUpPDFForTestFromFileList(String test) throws InterruptedException {
		setUpPDFForTest(cleanTestNameFromFileList(test));
		//setUpPDFForTest(Browser.classMap.get(threadId).replaceAll(".json", ""));
	}

	public final static void setUpPDFForTest(String cleanTestName) throws InterruptedException {

		String newNameText = Browser.FILE_PATH.toString() + "\\test_" + cleanTestName + System.currentTimeMillis()
				+ ".pdf";

		DataFileFinder data = new DataFileFinder();
		synchronized (data) {
			// System.out.println("***At time in method setUpPDFForTest " +
			// System.currentTimeMillis() + " the thread is "
			// + Thread.currentThread());
			data.renameDownloadedPDF(newNameText);
			File newName = new File(newNameText);
			Browser.pdfMap.put(Thread.currentThread().getId(), newName);
		}
	}

	public final static String cleanTestNameFromFileList(String test) {
		String pathToJsonData = test;
		String jsonPackagePathPos = "\\json\\";
		int indexOfJsonPackageStart = pathToJsonData.indexOf(jsonPackagePathPos) + (jsonPackagePathPos.length());
		String nameOfTestInPackage = pathToJsonData.substring(indexOfJsonPackageStart, pathToJsonData.length())
				.replace(Browser.fs, "");
		String name = nameOfTestInPackage.replace(".json", "");
		// System.out.println("cleanTestNameFromFileList() has the test as: " + name);
		return name;

	}
}