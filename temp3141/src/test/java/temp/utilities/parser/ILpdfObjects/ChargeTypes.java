package com.sprint.iice_tests.utilities.parser.ILpdfObjects;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.pdfbox.pdmodel.PDPage;

public class ChargeTypes extends PDFRegions {

	/*
	 * CONFIGURABLE CHARGES TYPE UTILITIES: the following utilities can be used to
	 * adjust the dimensions of the generic rectangle we use to grab the text of a
	 * single charge desc and amount table. All you need to do is execute one of the
	 * following methods first, and then you can proceed to use the 'UTILITY METHODS
	 * FOR A SINGLE CHARGES DESC AND AMOUNT TABLE'
	 */

	public ChargeTypes() throws IOException {
		super();
	}

	public void configureChargeTypesRectSize(int widthToAdd, int heightToAdd) {
		configureRectSize(widthToAdd, heightToAdd, ChargeTypesRegions.CHARGES_TYPE_RECT.getRegion());
	}

	public void configureChargeTypesRectLocation(int xPointsToAdd, int yPointsToAdd) {
		configureRectLocation(xPointsToAdd, yPointsToAdd, ChargeTypesRegions.CHARGES_TYPE_RECT.getRegion());
	}

	public void newChargeTypesRectLocation(int xCoord, int yCoord) {
		newRectLocation(xCoord, yCoord, ChargeTypesRegions.CHARGES_TYPE_RECT.getRegion());
	}

	public void newChargeTypesSize(int width, int height) {
		newRectSize(width, height, ChargeTypesRegions.CHARGES_TYPE_RECT.getRegion());
	}

	/*
	 * UTILITY METHODS FOR A SINGLE CHARGES DESC AND AMOUNT TABLE
	 */

	public String chargeTypes(PDPage page) throws IOException {
		return PDFRegions.rectangleToTrimmedText(page, ChargeTypesRegions.CHARGES_TYPE_RECT.getRegion());
	}

	public List<String> chargeTypesList(PDPage page) throws IOException {
		String text = chargeTypes(page);
		List<String> chargeTypeList = makeRectStringIntoListOfStringsByLineBreak(text);
		return chargeTypeList;
	}

	public Map<String, List<String>> chargeTypesMap(PDPage page) throws IOException {
		List<String> chargeTypesList = chargeTypesList(page);
		Map<String, List<String>> chargeTypeMap = mapChargeDescAndAmountsToHeaders(chargeTypesList);
		return chargeTypeMap;
	}

	public Map<String, List<String>> chargeTypesFilteredMap(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMapByKey = filterMapByKey(chargeTypesMap(page), isEqualTo.apply(key));
		return filteredMapByKey;
	}

	public String specificValFromChargesTypeMapByKey(PDPage page, String key, String value) throws IOException {
		Map<String, List<String>> filteredMap = chargeTypesFilteredMap(page, key);
		String desiredValue = getValByMapKey(filteredMap, key, value);
		return desiredValue;
	}

	public boolean specificValFromChargesTypeMapByKeyExists(PDPage page, String key, String value) throws IOException {
		Map<String, List<String>> filteredMap = chargeTypesFilteredMap(page, key);
		try {
			return getValByMapKey(filteredMap, key, value) != null;
		} catch (Exception e) {
			return false;
		}
	}

	public String headerToTextBelowIt(PDPage page, String keyHeader) throws IOException {
		System.out.println("TEXT BELOW " + keyHeader + " IS "
				+ makeMapValuesListIntoSingleString(chargeTypesFilteredMap(page, keyHeader), keyHeader));
		return makeMapValuesListIntoSingleString(chargeTypesFilteredMap(page, keyHeader), keyHeader);
	}

	/*
	 * LEFT SIDE OF CHARGES TYPE PAGE the following utility methods can be used for
	 * a charges type page that only contains charge desc and amount tables
	 */

	public String leftSideOfChargeTypes(PDPage page) throws IOException {
		return PDFRegions.rectangleToTrimmedText(page, ChargeTypesRegions.LEFT_SIDE_OF_CHARGE_TYPES_PG.getRegion());
	}

	public List<String> leftSideChargeTypesList(PDPage page) throws IOException {
		String leftSideText = leftSideOfChargeTypes(page);
		List<String> chargeTypeList = makeRectStringIntoListOfStringsByLineBreak(leftSideText);
		return chargeTypeList;
	}

	public Map<String, List<String>> leftSideChargeTypesMap(PDPage page) throws IOException {
		List<String> chargeTypesList = leftSideChargeTypesList(page);
		Map<String, List<String>> chargeTypeMap = mapChargeDescAndAmountsToHeaders(chargeTypesList);
		printMap(chargeTypeMap);
		return chargeTypeMap;
	}

	public Map<String, List<String>> leftSideChargeTypesFilteredMap(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMapByKey = filterMapByKey(leftSideChargeTypesMap(page), isEqualTo.apply(key));

		System.out.println("Here are the values: " + filteredMapByKey.values());

		return filteredMapByKey;
	}

	public List<String> allValuesFromKeyLeftSide(PDPage page, String key) throws IOException {
		return leftSideChargeTypesFilteredMap(page, key).get(key);
	}

	/*
	 * CHARGE TYPE DATA SPLIT BETWEEN PAGES
	 */

	public List<String> appendListToOtherForSplitRecords(PDPage pageEndsWithKey, PDPage pageAfterThatStartsWithoutKey)
			throws IOException {
		List<String> newList = leftSideChargeTypesList(pageEndsWithKey);
		newList.addAll(leftSideChargeTypesList(pageAfterThatStartsWithoutKey));
		return newList;
	}

	public Map<String, List<String>> combinedTablesOfTwoPagesAsMap(PDPage pageEndsWithKey,
			PDPage pageAfterThatStartsWithoutKey) throws IOException {
		List<String> tableList = appendListToOtherForSplitRecords(pageEndsWithKey, pageAfterThatStartsWithoutKey);
		Map<String, List<String>> chargeTypeMap = mapChargeDescAndAmountsToHeaders(tableList);
		return chargeTypeMap;
	}

	public Map<String, List<String>> combinedTablesOfTwoPagesAsFilteredMap(PDPage pageEndsWithKey,
			PDPage pageAfterThatStartsWithoutKey, String key) throws IOException {
		Map<String, List<String>> filteredMapByKey = filterMapByKey(
				combinedTablesOfTwoPagesAsMap(pageEndsWithKey, pageAfterThatStartsWithoutKey), isEqualTo.apply(key));
		return filteredMapByKey;
	}

	public String specificValFromCombinedTablesOfTwoPagesByKey(PDPage pageEndsWithKey,
			PDPage pageAfterThatStartsWithoutKey, String key, String value) throws IOException {
		Map<String, List<String>> filteredMap = combinedTablesOfTwoPagesAsFilteredMap(pageEndsWithKey,
				pageAfterThatStartsWithoutKey, key);
		String desiredValue = getValByMapKey(filteredMap, key, value);
		return desiredValue;
	}

	// this method grabs ptn data under a specific charge type.
	public Map<String, List<String>> valuesFromKeyAndChargeTypeLeftSide(PDPage page, String chargeType)
			throws IOException {
		List<String> charges = contentBetweenHeaders(leftSideChargeTypesList(page), chargeType, filterForChargeDesc);
		Map<String, List<String>> chargeTypeMap = mapChargeDescAndAmountsToHeaders(charges);
		printMap(chargeTypeMap);
		return chargeTypeMap;
	}

	// this method is a follow up method to
	// valuesFromKeyAndChargeTypeLeftSide(PDPage page,String chargeType).
	// use when requirements ask for content under specific charge type and specific
	// ptn
	public String specificValueFromKeyAndChargeTypeLeftSide(PDPage page, String chargeType, String key, String value)
			throws IOException {
		Map<String, List<String>> map = valuesFromKeyAndChargeTypeLeftSide(page, chargeType);
		return getValByMapKey(map, key, value);
	}

	public String chargeValuesWithDoubleLines(PDPage page, String key, String value) throws IOException {
		List<String> fullList = allValuesFromKeyLeftSide(page, key);
		fullList.add(0, key);
		List<String> newValues = joinSupplementaryTextToTextAboveIt(fullList);
		return getStringFromList(newValues, value);
	}

	public String specificValFromChargesTypeLeftSideMapByKey(PDPage page, String key, String value) throws IOException {
		Map<String, List<String>> filteredMap = leftSideChargeTypesFilteredMap(page, key);
		String desiredValue = getValByMapKey(filteredMap, key, value);
		return desiredValue;
	}

	public String priceAtEndOfLeftSideCharge(PDPage page, String key, String value) throws IOException {
		return chargeAmountGivenMapKeyValue(leftSideChargeTypesMap(page), key, value);
	}

	public boolean specificValFromChargesTypeLeftSideMapByKeyExists(PDPage page, String key, String value)
			throws IOException {
		Map<String, List<String>> filteredMap = leftSideChargeTypesFilteredMap(page, key);
		try {
			return getValByMapKey(filteredMap, key, value) != null;
		} catch (Exception e) {
			return false;
		}
	}

	public Integer amountOfChargesUnderHeader(PDPage page, String key) throws IOException {
		return sizeOfMapValuesGivenKey(leftSideChargeTypesMap(page), key);
	}

	public boolean amountOfChargesUnderHeaderIsExpected(PDPage page, String key, String expected)
			throws NumberFormatException, IOException {
		return amountOfValuesUnderHeaderIsExpected(leftSideChargeTypesMap(page), key, expected);
	}

	public Integer positionOrderOfChargeUnderHeader(PDPage page, String key, String value) throws IOException {
		return orderOfMapValueOnPageGivenKey(leftSideChargeTypesMap(page), key, value);
	}

	public boolean comparePositionOfValuesOnPDF(PDPage page, String key, String value1, String value2)
			throws IOException {
		Integer firstValue = positionOrderOfChargeUnderHeader(page, key, value1);
		Integer secondValue = positionOrderOfChargeUnderHeader(page, key, value2);
		return secondValue > firstValue;
	}

	/*
	 * RIGHT SIDE OF CHARGES TYPE PAGE the following utility methods can be used for
	 * a charges type page that only contains charge desc and amount tables
	 */

	public String rightSideOfChargeTypes(PDPage page) throws IOException {
		return PDFRegions.rectangleToTrimmedText(page, ChargeTypesRegions.RIGHT_SIDE_OF_CHARGE_TYPES_PG.getRegion());
	}

	public List<String> rightSideChargeTypesList(PDPage page) throws IOException {
		String rightSideText = rightSideOfChargeTypes(page);
		List<String> chargeTypeList = makeRectStringIntoListOfStringsByLineBreak(rightSideText);
		System.out.println("This is the chargeTypeList: "+chargeTypeList);
		return chargeTypeList;
	}

	public String msgInRightSideChargesAmount(PDPage page, String beginText, String endText) throws IOException {
		return subListAsSingleString((rightSideChargeTypesList(page)), beginText, endText);
	}

	public String rightSideChargeTypesTotal(PDPage page, String total) throws IOException {
		return getStringFromList(rightSideChargeTypesList(page), total);
	}

	public Map<String, List<String>> rightSideChargeTypesMapValuesAsList(PDPage page) throws IOException {
		List<String> chargeTypesList = rightSideChargeTypesList(page);
		System.out
				.println("THE CHARGE TYPES LIST FOR THE RIGHT SIDE OF THE PAGE IS: " + rightSideChargeTypesList(page));
		Map<String, List<String>> rightSideMap = new HashMap<>();
		makeMapForHeadersAtBottom(chargeTypesList, filterForRightSideHeaderTotals, rightSideMap);
		return rightSideMap;
	}

	public boolean rightSideChargeTypesValueExists(PDPage page, String value) throws IOException {
		List<String> chargeTypesList = rightSideChargeTypesList(page);
		System.out
				.println("THE CHARGE TYPES LIST FOR THE RIGHT SIDE OF THE PAGE IS: " + rightSideChargeTypesList(page));
		return chargeTypesList.stream().filter(i -> i.equals(value)).count() > 0;
	}
	
	public boolean rightSideChargeTypesValueCombinedExists(PDPage page, String value) throws IOException {
		List<String> chargeTypesList = rightSideChargeTypesList(page);
		return stringExistsInCombinedList(chargeTypesList, value);
	}
	

	public Map<String, String> rightSideChargeTypesMapValuesAsParagraph(PDPage page) throws IOException {
		Map<String, List<String>> rightSideMap = rightSideChargeTypesMapValuesAsList(page);
		return convertMapValuesIntoSingleString(rightSideMap);
	}

	public Map<String, String> specificValueFromRightSideChargeTypesMap(PDPage page, String key) throws IOException {
		Map<String, String> filteredMapByKey = filterMapByKey(rightSideChargeTypesMapValuesAsParagraph(page),
				isEqualTo.apply(key));
		printMapValueSingleString(filteredMapByKey);
		return filteredMapByKey;
	}

	public String valueFromRightSideChargeTypesMap(PDPage page, String key) throws IOException {
	//	System.out.println("THE RIGHT SIDE VALUE IS: " + specificValueFromRightSideChargeTypesMap(page, key).get(key));
		return specificValueFromRightSideChargeTypesMap(page, key).get(key);
	}

	public boolean rightSideTotalContainsCorrectTextForCharges(PDPage page, String key, String desiredText)
			throws IOException {
		String fullTotalChargeText = valueFromRightSideChargeTypesMap(page, key);
	//	System.out.println("THE TEXT ABOVE THE TOTAL CHARGE: " + key + " IS " + fullTotalChargeText);
		return fullTotalChargeText.contains(desiredText);
	}

	public static Predicate<String> filterForRightSideHeaderTotals = x -> textIsEquipGridrHeader(x);

	public static boolean textIsEquipGridrHeader(String text) {
		return testStringForRightChargeTypes(text, regexForRightSideHeaderTotals()) || testStringForRightChargeTypes(text,regexForRightSideHeaderTotalsSpanish());
	}

	public static boolean testStringForRightChargeTypes(String text, List<String> list) {
		return list.stream().anyMatch(x -> text.matches(x));
	}

	public static List<String> regexForRightSideHeaderTotals() {
		List<String> list = new ArrayList<>();
		list.add("^Adjustments.*$");
		list.add("^Sprint Surcharges.*$");
		list.add("^Government Taxes & Fees.*$");
		list.add("^Sprint Premium Services.*$");
		list.add("^Previous Charges Due.*$");
		list.add("^Third Party Charges.*$");
		list.add("^Government Taxes & Fees.*$");
		list.add("^Plans & Equipment.*$");
		list.add("^Account Charges.*$");
		list.add("^Sprint Magic Box.*$");
		list.add("^Unlimited Freedom - Unlimited Talk, Text & Data.*$");

		return list;
	}
	
	public static List<String> regexForRightSideHeaderTotalsSpanish() {
		List<String> list = new ArrayList<>();
		list.add("^Saldo previo vencido.*$");
		list.add("^Planes y Equipo.*$");

		return list;
	}

	public String desiredChargeTotal(PDPage page, String desiredTotalText) throws IOException {
		return getStringFromList(rightSideChargeTypesList(page), desiredTotalText);
	}

	public String leftSideHeaderToTextBelowIt(PDPage page, String keyHeader) throws IOException {
		printMap(leftSideChargeTypesMap(page));
		System.out.println("TEXT BELOW " + keyHeader + " IS "
				+ makeMapValuesListIntoSingleString(leftSideChargeTypesMap(page), keyHeader));
		return makeMapValuesListIntoSingleString(leftSideChargeTypesMap(page), keyHeader);
	}

	public String lineAboveThirdPartyRow(PDPage page, int yPositionOfRow) throws IOException {
		int yPositionOfLine = (yPositionOfRow - 15);
		newLocationForThirdPartyRow(0, yPositionOfLine);
		return PDFRegions.rectangleToTrimmedText(page, ChargeTypesRegions.THIRD_PARTY_ROW.getRegion());
	}

	public String lineBelowThirdPartyRow(PDPage page, int yPositionOfRow) throws IOException {
		int yPositionOfLine = (yPositionOfRow + 50);
		newLocationForThirdPartyRow(0, yPositionOfLine);
		return PDFRegions.rectangleToTrimmedText(page, ChargeTypesRegions.THIRD_PARTY_ROW.getRegion());
	}

	public List<String> rowForThirdPartyTable(PDPage page, int yPosition) throws IOException {
		int headerHeight = 40;
		int headerWidth = 50;
		int descHeaderWidth = (headerWidth + 70);
		List<String> headers = new ArrayList<>();
		// HEADERS
		Rectangle on = new Rectangle(headerWidth, headerHeight);
		configureRectLocation(0, yPosition, on);
		String onText = PDFRegions.rectangleToTrimmedText(page, on);
		headers.add(onText);

		Rectangle contentProvider = new Rectangle(headerWidth, headerHeight);
		configureRectLocation(50, yPosition, contentProvider);
		String contentProviderText = PDFRegions.rectangleToTrimmedText(page, contentProvider);
		headers.add(contentProviderText);

		Rectangle contentType = new Rectangle(headerWidth, headerHeight);
		configureRectLocation(100, yPosition, contentType);
		String contentTypeText = PDFRegions.rectangleToTrimmedText(page, contentType);
		headers.add(contentTypeText);

		Rectangle description = new Rectangle(descHeaderWidth, headerHeight);
		configureRectLocation(150, yPosition, description);
		String descriptionText = PDFRegions.rectangleToTrimmedText(page, description);
		headers.add(descriptionText);

		Rectangle rate = new Rectangle(headerWidth, headerHeight);
		configureRectLocation(270, yPosition, rate);
		String rateText = PDFRegions.rectangleToTrimmedText(page, rate);
		headers.add(rateText);

		Rectangle chargesAdjustments = new Rectangle(headerWidth, headerHeight);
		configureRectLocation(320, yPosition, chargesAdjustments);
		String chargesAdjustmentsText = PDFRegions.rectangleToTrimmedText(page, chargesAdjustments);

		headers.add(chargesAdjustmentsText);
		return deleteLineBreaksFromList(headers);
	}

	public Map<String, String> thirdPartyMap(PDPage page, int headerYPosition, int rowYPosition) throws IOException {
		List<String> headerCells = rowForThirdPartyTable(page, headerYPosition);
		List<String> rowCells = rowForThirdPartyTable(page, rowYPosition);
		return createThirdPartyMap(headerCells, rowCells);
	}

	public Map<String, String> createThirdPartyMap(List<String> headerCells, List<String> rowCells) {
		Map<String, String> thirdPartyMap = new HashMap<String, String>();
		for (String header : headerCells) {
			int headerPosition = headerCells.indexOf(header);
			String row = rowCells.get(headerPosition);
			thirdPartyMap.put(header, row);
		}
		return thirdPartyMap;
	}

	public void configureLocationForThirdPartyCell(int xPosition, int yPosition) {
		configureRectLocation(xPosition, yPosition, ChargeTypesRegions.THIRD_PARTY_ROW_CELL.getRegion());
	}

	public void newLocationForThirdPartyRow(int xCoord, int yCoord) {
		newRectLocation(xCoord, yCoord, ChargeTypesRegions.THIRD_PARTY_ROW.getRegion());
	}

	/*
	 * RECTANGLES OF REGIONS ON A CHARGES TYPES PAGE OF THE PDF THESE RECTANGLES
	 * FUNCTION WITHIN OUR TESTS LIKE XPATH OR CSS SELECTORS- THEY GET US TEXT THAT
	 * WE PARSE AND THEN TEST AGAINST
	 */

	public enum ChargeTypesRegions {

		LEFT_SIDE_OF_CHARGE_TYPES_PG(new Rectangle(5, 100, 375, 800)),

		RIGHT_SIDE_OF_CHARGE_TYPES_PG(new Rectangle(375, 100, 450, 1000)),

		CHARGES_TYPE_RECT(new Rectangle(375, 375)),

		THIRD_PARTY_ROW_CELL(new Rectangle(50, 25)),

		THIRD_PARTY_ROW(new Rectangle(500, 25));

		private Rectangle region;

		private ChargeTypesRegions(Rectangle dimensions) {
			this.region = dimensions;
		}

		public Rectangle getRegion() {
			return region;
		}
	}
}
