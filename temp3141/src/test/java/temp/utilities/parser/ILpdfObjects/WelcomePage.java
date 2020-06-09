package com.sprint.iice_tests.utilities.parser.ILpdfObjects;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDPage;

public class WelcomePage extends PDFRegions {

	public WelcomePage() throws IOException {
		super();
	}

	/**
	 * ACCOUNT DETAILS (TOP RIGHT CORNER OF FIRST PAGE) UTILITY METHODS
	 */

	public String accountNumberDetailsSingleString(PDPage page) throws IOException {
		String getAccountNumberDetails = rectangleToTrimmedText(page, WelcomePageRegions.PAGE_NUM_AND_BAN.getRegion());
		return getAccountNumberDetails;
	}

	public List<String> accountNumberDetailsList(PDPage page) throws IOException {
		List<String> listOfFirstBillInfo = makeRectStringIntoListOfStringsByLineBreak(
				accountNumberDetailsSingleString(page));
		return listOfFirstBillInfo;
	}

	public String pageNumber(PDPage page) throws IOException {
		int positionOfPageNum = 0;
		return accountNumberDetailsList(page).get(positionOfPageNum);
	}

	public String accountNumber(PDPage page) throws IOException {
		int positionOfAccountNum = 1;
		return accountNumberDetailsList(page).get(positionOfAccountNum);
	}

	public String lastTextOfAccountNumDetails(PDPage page) throws IOException {
		int handleIndexingAndTakeOffTrailingPgNum = 1;
		int lastTextOfAccountDetails = accountNumberDetailsList(page).size() - handleIndexingAndTakeOffTrailingPgNum;
		return accountNumberDetailsList(page).get(lastTextOfAccountDetails);
	}
	
	public String grabDatePeriodFromText(PDPage page) throws IOException {
		String fullBillPeriodText = lastTextOfAccountNumDetails(page);
	//	System.out.println("The full bill period text is: "+fullBillPeriodText);
		int indexOfColon = fullBillPeriodText.indexOf(":");
		String billPeriod = fullBillPeriodText.substring(indexOfColon+1, fullBillPeriodText.length());
	//	System.out.println("The bill period text with year is: "+billPeriod);
		int indexOfCommaBeforeYear = billPeriod.indexOf(",");
		String billPeriodBeforeYear = billPeriod.substring(0,indexOfCommaBeforeYear).trim();
	//	System.out.println("The bill period text without year is: "+billPeriodBeforeYear );
		return billPeriodBeforeYear;
	}

	/**
	 * FIRST COLUMN OF FIRST PAGE (AKA 'WELCOME' OR 'LAST BILL') UTILITY METHODS
	 */

	public String firstColumnText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, WelcomePageRegions.FIRST_COL_FIRST_PAGE.getRegion());
	}
	
	public String accessibleColumnText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, WelcomePageRegions.ACCESSIBLE_COL_FIRST_PAGE.getRegion());
	}

	public List<String> firstColumnList(PDPage page) throws IOException {
		String firstColumnText = firstColumnText(page);
		List<String> firstColumnList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(firstColumnText);
		return firstColumnList;
	}
	
	public List<String> accessibleColumnList(PDPage page) throws IOException {
		String allColumnText = accessibleColumnText(page);
		List<String> firstColumnList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(allColumnText);
		return firstColumnList;
	}

	public String firstColumnHeader(PDPage page) throws IOException {
		int firstColumnHeaderPosition = 0;
		return firstColumnList(page).get(firstColumnHeaderPosition);
	}

	// use when the header is "welcome"
	public Map<String, String> firstColumnMapForParagraph(PDPage page) throws IOException {
		List<String> colList = firstColumnList(page);
		Map<String, String> colMap = mapParagraphAsListToHeader(colList, filterForWelcomePageHeader);
		return colMap;
	}

	// use when the header is "welcome"
	public Map<String, String> firstColumnParagraphFilteredMap(PDPage page, String key) throws IOException {
		Map<String, String> filteredMapByKey = filterMapByKey(firstColumnMapForParagraph(page), isEqualTo.apply(key));
		return filteredMapByKey;
	}

	// use when the header is "Last Bill"
	public Map<String, List<String>> firstColumnChargeTypesMap(PDPage page) throws IOException {
		List<String> chargeTypesList = firstColumnList(page);
		Map<String, List<String>> chargeTypeMap = mapChargeDescAndAmountsToHeaders(chargeTypesList);
		return chargeTypeMap;
	}
	
	public Map<String, List<String>> accessibleColumnChargeTypesMap(PDPage page) throws IOException {
		List<String> chargeTypesList = accessibleColumnList(page);
		Map<String, List<String>> chargeTypeMap = mapChargeDescAndAmountsToHeaders(chargeTypesList);
		return chargeTypeMap;
	}

	// use when the header is "Last Bill"
	public Map<String, List<String>> firstColumnChargeTypesFilteredMap(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMapByKey = filterMapByKey(firstColumnChargeTypesMap(page),
				isEqualTo.apply(key));
		return filteredMapByKey;
	}
	
	public Map<String, List<String>> accessibleColumnChargeTypesFilteredMap(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMapByKey = filterMapByKey(accessibleColumnChargeTypesMap(page),
				isEqualTo.apply(key));
		return filteredMapByKey;
	}

	public String specificValFromFirstColumnChargeTypesFilteredMap(PDPage page, String key, String value)
			throws IOException {
		Map<String, List<String>> filteredMap = firstColumnChargeTypesFilteredMap(page, key);
		String desiredValue = getValByMapKey(filteredMap, key, value);
		return desiredValue;
	}
	
	public String specificValFromAccessibleColumnChargeTypesFilteredMap(PDPage page, String key, String value)
			throws IOException {
		Map<String, List<String>> filteredMap = accessibleColumnChargeTypesFilteredMap(page, key);
		String desiredValue = getValByMapKey(filteredMap, key, value);
		return desiredValue;
	}

	public String priceAtEndOfLeftSideChargeForWelcomePage(PDPage page, String key, String value) throws IOException {
		return chargeAmountGivenMapKeyValue(firstColumnChargeTypesMap(page), key, value);
	}

	/*
	 * SECOND COLUMN (AKA 'THIS BILL')
	 */

	public String secondColumnText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, WelcomePageRegions.SECOND_COL_FIRST_PAGE.getRegion());
	}

	public List<String> secondColumnList(PDPage page) throws IOException {
		String secondColumnText = secondColumnText(page);
		List<String> secondColumnList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(secondColumnText);
		return secondColumnList;
	}
	
	public List<String> AllColumnList(PDPage page) throws IOException {
		String secondColumnText = accessibleColumnText(page);
		List<String> secondColumnList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(secondColumnText);
		return secondColumnList;
	}

	public Map<String, List<String>> secondColumnChargeTypesMap(PDPage page) throws IOException {
		List<String> chargeTypesList = secondColumnList(page);
		Map<String, List<String>> chargeTypeMap = mapChargeDescAndAmountsToHeaders(chargeTypesList);
		return chargeTypeMap;
	}

	public Map<String, List<String>> secondColumnChargeTypesFilteredMap(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMapByKey = filterMapByKey(secondColumnChargeTypesMap(page),
				isEqualTo.apply(key));
		return filteredMapByKey;
	}

	public String specificValFromSecondColumnChargeTypeMapByKey(PDPage page, String key, String value)
			throws IOException {
		Map<String, List<String>> filteredMap = secondColumnChargeTypesFilteredMap(page, key);
		System.out.println(filteredMap);
		String desiredValue = getValByMapKey(filteredMap, key, value);
		return desiredValue;
	}
	
	public String specificValFromAllColumnChargeTypeMapByKey(PDPage page, String key, String value)
			throws IOException {
		Map<String, List<String>> filteredMap = accessibleColumnChargeTypesFilteredMap(page, key);
		System.out.println(filteredMap);
		String desiredValue = getValByMapKey(filteredMap, key, value);
		return desiredValue;
	}

	public Map<String, String> secondColumnMapForParagraph(PDPage page) throws IOException {
		List<String> colList = secondColumnList(page);
		Map<String, String> colMap = mapParagraphAsListToHeader(colList, filterForWelcomePageHeader);
		return colMap;
	}

	public String secondColumnParagraphByKey(PDPage page, String key) throws IOException {
		return secondColumnMapForParagraph(page).get(key);
	}

	/*
	 * THIRD COLUMN (AKA TOTAL AMOUNT DUE) sometimes the third column has paragraph
	 * text. If you need to extract the text as a single string for testing
	 * purposes, use subListAsSingleString(List<String> list, String beginText,
	 * String endText)) from PDFRegions by using thirdColumnList(PDPage page) as the
	 * List<String> list parameter
	 */

	public String thirdColumnText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, WelcomePageRegions.THIRD_COL_FIRST_PAGE.getRegion());
	}

	public List<String> thirdColumnList(PDPage page) throws IOException {
		String thirdColumnText = thirdColumnText(page);
		List<String> thirdColumnList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(thirdColumnText);
		return thirdColumnList;
	}

	public Map<String, String> thirdColumnMapForParagraph(PDPage page) throws IOException {
		List<String> colList = thirdColumnList(page);
		Map<String, String> colMap = mapParagraphAsListToHeader(colList, filterForWelcomePageHeader);
		return colMap;
	}

	public Map<String, List<String>> thirdColumnMap(PDPage page) throws IOException {
		Map<String, List<String>> map = mapWelcomeTextToHeaders(thirdColumnList(page));
		return map;
	}

	public Map<String, List<String>> thirdColumnFilteredMap(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMap = filterMapByKey(thirdColumnMap(page), isEqualTo.apply(key));
		return filteredMap;
	}
	
	public String thirdColumnAlerts(PDPage page, String key, String beginText, String endText) throws IOException {
		List<String> valuesFromMap = thirdColumnFilteredMap(page, key).get(key);
		List<String> cleanList = valuesFromMap.stream().map(deleteBullets).collect(Collectors.toList());
		System.out.println("the values are: "+ cleanList);
		String thirdColAlert  = subListAsSingleString(cleanList, beginText, endText);
		System.out.println("the values are: "+ thirdColAlert);
		return thirdColAlert;
	}

	public List<String> valuesFromThirdColumnMap(PDPage page, String key) throws IOException {
		return thirdColumnFilteredMap(page, key).get(key);
	}

	public String textAboveBulletPoints(PDPage page, String key) throws IOException {
		String firstBulletPoint = valuesFromThirdColumnMap(page, key).stream().filter(startsWithBulletPoint).findFirst()
				.get();
		int firstBulletPointPosition = valuesFromThirdColumnMap(page, key).indexOf(firstBulletPoint);
		List<String> valuesAboveBulletPoints = valuesFromThirdColumnMap(page, key).subList(0, firstBulletPointPosition);
		String paragraphAboveBulletPoints = joinListStringsIntoSingleString(valuesAboveBulletPoints);
		return paragraphAboveBulletPoints;
	}

	public static Map<String, List<String>> mapWelcomeTextToHeaders(List<String> list) {
		ConcurrentHashMap<String, List<String>> table = new ConcurrentHashMap<String, List<String>>();
		makeMap(list, filterForWelcomePageHeader, table);
		return (table);
	}

	Predicate<String> startsWithBulletPoint = x -> lineStartsWithBulletPoint(x);

	public boolean lineStartsWithBulletPoint(String text) {
		return text.matches(regexForStartingBulletPoint);
	}

	String regexForStartingBulletPoint = "^·.*$";

	/*
	 * ADDRESS CONTENT
	 */

	public String autoPayText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, WelcomePageRegions.AUTOPAY_MSG.getRegion());
	}

	public List<String> autoPayList(PDPage page) throws IOException {
		String autoPayText = autoPayText(page);
		List<String> autoPayList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(autoPayText);
		return autoPayList;
	}
	
	public String thankYouForPayingAutoPay(PDPage page) throws IOException {
		return autoPayList(page).get(0);
	}

	public String autoPayChargeDetails(PDPage page) throws IOException {
		return autoPayList(page).get(1);
	}
	
	public String lastThreeMonthsText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, WelcomePageRegions.LAST_THREE_MONTH_CHARGES.getRegion());
	}

	public List<List<String>> createLastThreeMonthGraphs(PDPage page) throws IOException {
		List<List<String>> threeMonths = new ArrayList<>();
		int width = 58;
		int height = 50;
		int yCoordToAdd = 40;
		int xMargin = WelcomePageRegions.LAST_THREE_MONTH_CHARGES.getRegion().x;
		int yMargin = WelcomePageRegions.LAST_THREE_MONTH_CHARGES.getRegion().y + yCoordToAdd;

		Rectangle firstMonth = new Rectangle(xMargin, yMargin, width, height);
		threeMonths.add(makeRectIntoListOfString(page, firstMonth));

		Rectangle secondMonth = new Rectangle(firstMonth.x + width, yMargin, width, height);
		threeMonths.add(makeRectIntoListOfString(page, secondMonth));

		Rectangle thirdMonth = new Rectangle(secondMonth.x + width, yMargin, width, height);
		threeMonths.add(makeRectIntoListOfString(page, thirdMonth));

		return threeMonths;
	}

	public Map<String, String> mapOfLastThreeCharges(PDPage page) throws IOException {
		Map<String, String> monthToCharge = new HashMap<String, String>();
		for (List<String> list : createLastThreeMonthGraphs(page)) {
			monthToCharge.put(list.get(1), list.get(0));
		}
		return monthToCharge;
	}

	public String lastThreeMonthsHeader(PDPage page) throws IOException {
		return lastThreeMonthsList(page).get(0);
	}

	public List<String> lastThreeMonthsList(PDPage page) throws IOException {
		String lastThreeMonthsText = lastThreeMonthsText(page);
		List<String> lastThreeMonthsList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(lastThreeMonthsText);
		return lastThreeMonthsList;
	}

	public String amountDueText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, WelcomePageRegions.AMOUNT_DUE_DETAILS.getRegion());
	}

	public List<String> amountDueList(PDPage page) throws IOException {
		String amountDueText = amountDueText(page);
		List<String> amountDueList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(amountDueText);
		return amountDueList;
	}

	public String senderAddressesText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, WelcomePageRegions.SENDER_ACCOUNT_ADDRESSES.getRegion());
	}

	public List<String> senderAddressesList(PDPage page) throws IOException {
		String senderAddressesText = senderAddressesText(page);
		List<String> senderAddressesList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(senderAddressesText);
		return senderAddressesList;
	}

	public String recipientAddressText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, WelcomePageRegions.SENDER_ACCOUNT_ADDRESSES.getRegion());
	}

	public List<String> recipientAddressList(PDPage page) throws IOException {
		String recipientAddressText = recipientAddressText(page);
		List<String> recipientAddressList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(recipientAddressText);
		return recipientAddressList;
	}

	public String imBarcodeText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, WelcomePageRegions.IM_BARCODE.getRegion());
	}

	public static Predicate<String> filterForWelcomePageHeader = x -> textIsWelcomePageHeader(x);

	public static boolean textIsWelcomePageHeader(String text) {
		return testString(text, typesOfWelcomePageHeaders());
	}

	public static List<String> typesOfWelcomePageHeaders() {
		List<String> listOfWelcomePageHeaders = new ArrayList<>();
		listOfWelcomePageHeaders.add("Welcome");
		listOfWelcomePageHeaders.add("Revised Bill");
		listOfWelcomePageHeaders.add("Total Amount Due");
		listOfWelcomePageHeaders.add("Let's talk");
		listOfWelcomePageHeaders.add("about this bill");
		return listOfWelcomePageHeaders;
	}

	/*
	 * RECTANGLES OF REGIONS ON THE FIRST PAGE OF THE PDF THESE RECTANGLES FUNCTION
	 * WITHIN OUR TESTS LIKE XPATH OR CSS SELECTORS- THEY GET US TEXT THAT WE PARSE
	 * AND THEN TEST AGAINST
	 */

	public enum WelcomePageRegions {

		FIRST_PAGE_HEADER(new Rectangle(50, 25, 355, 50)),

		PAGE_NUM_AND_BAN(new Rectangle(410, 0, 185, 58)),

		FIRST_COL_FIRST_PAGE(new Rectangle(5, 100, 200, 300)),

		SECOND_COL_FIRST_PAGE(new Rectangle(205, 100, 200, 300)),

		THIRD_COL_FIRST_PAGE(new Rectangle(405, 100, 200, 300)),
		
		ACCESSIBLE_COL_FIRST_PAGE(new Rectangle(5, 100, 600, 400)),

		AUTOPAY_MSG(new Rectangle(0, 425, 400, 100)),

		LAST_THREE_MONTH_CHARGES(new Rectangle(400, 425, 400, 100)),

		AMOUNT_DUE_DETAILS(new Rectangle(300, 525, 500, 100)),

		SENDER_ACCOUNT_ADDRESSES(new Rectangle(0, 525, 200, 200)),

		RECIPIENT_ADDRESS(new Rectangle(300, 625, 500, 100)),

		IM_BARCODE(new Rectangle(0, 750, 500, 200));

		private Rectangle region;

		private WelcomePageRegions(Rectangle dimensions) {
			this.region = dimensions;
		}

		public Rectangle getRegion() {
			return region;
		}
	}

	public List<String> thirdColumnList(PDPage page, String key) throws IOException {
		List<String> valuesFromMap = thirdColumnFilteredMap(page, key).get(key);
		List<String> cleanList = valuesFromMap.stream().map(deleteBullets).collect(Collectors.toList());
		
		return cleanList;
	}
}