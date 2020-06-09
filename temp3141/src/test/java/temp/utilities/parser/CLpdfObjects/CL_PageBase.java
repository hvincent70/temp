package com.sprint.iice_tests.utilities.parser.CLpdfObjects;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;

import com.sprint.iice_tests.utilities.parser.ILpdfObjects.PDFRegions;

public class CL_PageBase {
	final static String DOWNLOAD_FOLDER = "/src/test/resources/TestData";
	static File dir = new File(System.getProperty("user.dir") + DOWNLOAD_FOLDER);

	/*
	 * CONVERT RECTANGLE TO TESTABLE TEXT UTILITIES
	 */

	public static String rectangleToTrimmedText(PDPage page, Rectangle rect) throws IOException {
		String text = converRectToText(page, rect);
		return deleteNonBreakableSpace(text);
	}

	public static String converRectToText(PDPage page, Rectangle rect) throws IOException {
		PDFTextStripperByArea stripper = new PDFTextStripperByArea();
		stripper.setSortByPosition(true);
		stripper.addRegion("class1", rect);
		stripper.extractRegions(page);
		String text = stripper.getTextForRegion("class1");
		return text;
	}

	public static String lineSeparator(PDPage page, Rectangle rect) throws IOException {
		PDFTextStripperByArea stripper = new PDFTextStripperByArea();
		stripper.setSortByPosition(true);
		stripper.addRegion("class1", rect);
		stripper.extractRegions(page);
		String ls = stripper.getLineSeparator();

		// System.out.println("****************the line separator is:\n_"+ls+"_");
		return ls;
	}

	public static String textPosition(PDDocument doc, PDPage page, Rectangle rect) throws IOException {
		PDFTextStripperByArea stripper = new PDFTextStripperByArea();
		PDPageContentStream cs = new PDPageContentStream(doc, page);
		cs.beginText();
		TextPosition text;
		stripper.setSortByPosition(true);
		stripper.addRegion("class1", rect);
		stripper.extractRegions(page);

		return null;

	}

	public void processTextPosition(PDDocument doc, PDPage page, TextPosition text) {

	}

	public static List<String> makeRectIntoListOfString(PDPage page, Rectangle rect) throws IOException {
		return makeRectStringIntoListOfStringsByLineBreak(PDFRegions.rectangleToTrimmedText(page, rect));
	}

	public static List<String> makeRectIntoListOfStringAccountOverview(PDPage page, Rectangle rect) throws IOException {
		return makeRectStringIntoListOfStringsAccountOverview(PDFRegions.rectangleToTrimmedText(page, rect));
	}

	/**
	 * This routine deletes leading and trailing white space in a String extracted
	 * from a pdf. Using only .trim() on a String from pdf does not work. Using only
	 * element.replaceAll("\u00A0", "") on a String from pdf does not work. This
	 * combination is the only thing that has worked for me. MM 8/21/18
	 * 
	 * @param element
	 *            pdf String that has leading or trailing spaces
	 * @return text without leading or trailing whitespace
	 */

	public static String deleteNonBreakableSpace(String element) {
		// element.replaceAll("\u00A0", "_NONBREAKSPACE_");
		// System.out.println("THIS IS THE NEW ELEMENT: "+element);
		return element.replaceAll("\u00A0", "").trim();
	}

	/*
	 * We grab pdf text by rectangles. We design these rectangles so that they grab
	 * paragraphs or tables of data. The rectangle returns the text as a single
	 * String. We begin parsing this single String by making it a list of Strings.
	 * Each element in this list of String is the text found on a new line.
	 */

	public static List<String> makeRectStringIntoListOfStringsByLineBreak(String text) {
		List<String> lines = new BufferedReader(new StringReader(text)).lines().collect(Collectors.toList());
		return deleteLineBreaksFromList(lines);
	}

	public static List<String> makeRectStringIntoListOfStringsAccountOverview(String text) {
		List<String> lines = new BufferedReader(new StringReader(text)).lines().collect(Collectors.toList());
		return lines;
	}

	// use for troubleshooting rectangle regions
	@SuppressWarnings("unchecked")
	public void lineBreakStartEndPoints(String text) {
		List<String> lines = makeRectStringIntoListOfStringsByLineBreak(text);
		lines.stream().forEachOrdered(PrintUtil.printBegAndEndOfLine);
	}

	/*
	 * This routine is used for pojos that need to map headers to the data below it.
	 * For instance, data can be tables and the paragraph text below it. As a
	 * result, we want the table to remain a list of strings, but the paragraph text
	 * should be converted into a single string. This routine can filter out and
	 * return the paragraph based on the text it starts and ends with.
	 * 
	 * For an example of how this is used see: EquipGridDetails: public String
	 * equipGridParagraph(PDPage page, String key, String beginText, String endText)
	 */

	public static String subListAsSingleString(List<String> list, String beginText, String endText) {
		List<String> subList = createSublist(list, beginText, endText);
		return joinListStringsIntoSingleString(subList);
	}

	public static String joinListStringsIntoSingleString(List<String> list) {
		StringJoiner combinedListElements = new StringJoiner(" ");
		list.forEach(x -> combinedListElements.add(x));
		String combinedText = combinedListElements.toString();
		return combinedText;
	}

	public String priceAtEndOfString(String text) {
		String price = text.substring(text.lastIndexOf("$") + 1).trim();
		System.out.println("THE PRICE IS: " + price);
		return price;
	}

	public static List<String> createSublist(List<String> list, String beginText, String endText) {
		List<String> innerList = new ArrayList<>();
		String beginningElement = list.stream().filter(x -> x.startsWith(beginText) || x.equalsIgnoreCase(beginText))
				.findFirst().get();
		String endingElement = list.stream().filter(x -> x.endsWith(endText) || x.equalsIgnoreCase(endText)).findFirst()
				.get();
		int startIndex = 0;
		int endIndex = 1;
		try {
			startIndex = list.indexOf(beginningElement);
		} catch (IllegalArgumentException e) {

		}
		try {
			endIndex = list.indexOf(endingElement) + 1;
		} catch (IllegalArgumentException e) {

		}
		try {
			innerList = list.subList(startIndex, endIndex);
		} catch (IllegalArgumentException e) {

		}
		return innerList;
	}

	public String joinSublistIntoString(List<String> list, String beginText, String endText) {
		List<String> subList = createSublist(list, beginText, endText);
		return joinListStringsIntoSingleString(subList);
	}

	/*
	 * This routine examines a pdf page and finds the index of a desired header. We
	 * then use that index to find the index of the next header on the pdf page. To
	 * do this, we utilize the keyIndices routine, which contains the indices of all
	 * the headers on the page.
	 * 
	 * see Usage's List<String> usageContentBetweenHeader(PDPage page, String
	 * startingHeader) for implementation example
	 */

	public List<String> contentBetweenHeaders(List<String> fullTableAsList, String startingHeader,
			Predicate<String> filterForHeaders) throws IOException {
		List<String> valueList = new ArrayList<>();
		List<String> list = fullTableAsList.stream().map(x -> deleteLineBreaks(x)).collect(Collectors.toList());
		List<Integer> keyIndices = indicesForHeaderAtTop(list, filterForHeaders);

		// finds index of header on pdf page
		int headerForData = list.indexOf(startingHeader);

		// finds the position of headerForData within the complete list of header
		// indices from the page
		int headerIndex = keyIndices.indexOf(headerForData);

		// uses headerIndex to find the index of the next header on the pdf page
		int nextHeaderOnPage = keyIndices.get(headerIndex + 1);

		valueList = list.subList(headerForData, nextHeaderOnPage);
		return valueList;
	}

	/*
	 * This routine takes a list of Strings and ignores the first and the last
	 * String. It then looks at the values in between. This routine joins each even
	 * indexed value to the odd indexed value just before it to create a new string.
	 * Each new String is added to a list, and this list is returned.
	 */

	public List<String> joinSupplementaryTextToTextAboveIt(List<String> list) {
		List<String> combinedText = new ArrayList<>();
		String header = list.get(0);
		combinedText.add(0, header);
		int valuesExcludingNextHeader = list.size() - 2;
		for (int i = 1; i < valuesExcludingNextHeader; i += 2) {
			StringJoiner j = new StringJoiner(" ");
			j.add(list.get(i)).add(list.get(i + 1));
			combinedText.add(j.toString());
		}
		return combinedText;
	}

	/*
	 * PDF data may change. Sometimes, page objects that normally need a mapping of
	 * a header to a list of Strings (for charge desc and amounts), may need a
	 * mapping of a header to a String(for paragraph text). For example, the first
	 * column on the welcome page can sometimes have Welcome Text (paragraph text)
	 * or Last Bill info (charge desc. and amounts). The following routine can be
	 * used for these variations.
	 */

	public static String makeMapValuesListIntoSingleString(Map<String, List<String>> map,
			String keyOfValuesYouWantToCombine) {
		List<String> values = map.get(keyOfValuesYouWantToCombine);
		String desiredValue = joinListStringsIntoSingleString(values);
		return desiredValue;
	}

	// this method converts a list of one Type into a list of another Type
	public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
		return from.stream().map(func).collect(Collectors.toList());
	}

	/*
	 * UTILITIES FOR MAPPING CHARGE DESCRIPTION TO CHARGE AMOUNT (ESPECIALLY USED IN
	 * WELCOME, CHARGETYPES, EQUIPGRIDDETAILS PAGE OBJECTS)
	 */

	public String chargeAmountGivenMapKeyValue(Map<String, List<String>> map, String key, String value) {
		Map<String, List<String>> filteredMapByKey = filterMapByKey(map, isEqualTo.apply(key));
		// PrintUtil.printMap(filteredMapByKey);
		String desiredValue = getValByMapKey(filteredMapByKey, key, value);
		PrintUtil.printValues.accept(desiredValue);
		// System.out.println("the price at the end of the string is: " +
		// priceAtEndOfString(desiredValue));
		return priceAtEndOfString(desiredValue);
	}

	/*
	 * This routine gets the charge amounts from a list of Strings that end with
	 * charge amounts.
	 */

	public static List<String> chargeAtEndOfStringForList(List<String> list, String regex) {
		List<String> newList = list;
		List<String> listOfCharges = newList.stream().map(text -> grabChargeFromText.apply(text, regex))
				.collect(Collectors.toList());
		return listOfCharges;
	}

	static BiFunction<String, String, String> grabChargeFromText = (text, regex) -> {
		return chargeAtEndOfString(text, regex);
	};

	public static List<String> filterForNotHavingChargeAtEnd(List<String> list, String regex) {
		List<String> lists = list.stream().filter(text -> lineDoesNotEndWithChargeAmount.test(text, regex))
				.collect(Collectors.toList());
		return lists;
	}

	static BiFunction<List<String>, String, Integer> getValueFromList = (list, text) -> {
		return list.indexOf(text);
	};

	public static BiPredicate<String, String> lineDoesNotEndWithChargeAmount = (text,
			regex) -> textDoesNotEndWithChargeAmount(text, regex);
	public static BiPredicate<String, String> lineEndsWithChargeAmount = (text, regex) -> textEndsWithChargeAmount(text,
			regex);

	public static Integer indexOfFirstInstanceOfCharge(List<String> list, String regex) {
		String firstInstance = list.stream().filter(text -> lineEndsWithChargeAmount.test(text, regex)).findFirst()
				.get();
		int indexOfFirstInstance = list.indexOf(firstInstance);
		return indexOfFirstInstance;
	}

	public static List<String> onlyGetChargeTables(List<String> fullList, String regex) {
		int indexOfFirstInstance = indexOfFirstInstanceOfCharge(fullList, regex);
		int sizeOfList = (fullList.size() - 1);
		List<String> listOfChargeTable = new ArrayList<>();
		listOfChargeTable = fullList;
		return listOfChargeTable.subList(indexOfFirstInstance, sizeOfList);
	}

	public static boolean textEndsWithChargeAmount(String text, String regex) {
		boolean textHasChargeAmountAtEnd = matchTextToRegex(text, regex).matches();
		// System.out.println("the text "+text+" matches charge amount
		// regex"+textHasChargeAmountAtEnd);
		return textHasChargeAmountAtEnd;
	}

	public static boolean textDoesNotEndWithChargeAmount(String text, String regex) {
		boolean textDoesNotHaveChargeAmountAtEnd = !textEndsWithChargeAmount(text, regex);
		if (!textEndsWithChargeAmount(text, regex) && !text.contains("continues...")) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * This routine finds the last position of the dollar sign (or negative sign if
	 * present) in a String.
	 */

	public static Integer dollarSignPosition(String text, String regex) {
		String t = text;
		int dollarSignPosition = 0;
		Matcher m = matchTextToRegex(text, regex);
		if (m.find()) {
			String match = m.group(0);
			String numAtEndOfString = m.group(1);
			int removeDollar = 1;
			int lengthOfNotCharge = numAtEndOfString.length() - removeDollar;
			String charge = text.substring(lengthOfNotCharge);
			System.out.println("the match is: " + m.group(1) + " and its length is " + lengthOfNotCharge
					+ " the charge is: " + charge);
			dollarSignPosition = lengthOfNotCharge;
			System.out.println("the charge is: " + charge);
			int negativeSignBeforeDollarSign = dollarSignPosition - 1;
			// sometimes the customer has a negative charge. This is reflected by a negative
			// sign before the dollar sign.
			// this handles where to insert text below when there is a negative sign
			if ((text.lastIndexOf("-") == negativeSignBeforeDollarSign)) {
				dollarSignPosition = negativeSignBeforeDollarSign;
			}
			return dollarSignPosition;
		}
		System.out.println("DID NOT FIND DOLLAR SIGN POSITION FOR: " + text);
		return dollarSignPosition;
	}

	public static String chargeAtEndOfString(String text, String regex) {
		int positionOfCharge = dollarSignPosition(text, regex);
		// System.out.println("the text length is: "+(text.length()-1)+" and the
		// position of the charge is "+positionOfCharge);
		// System.out
		// .println("the charge at the end of the string is: " +
		// text.substring(positionOfCharge, text.length()));
		return text.substring(positionOfCharge);
	}

	public static Matcher matchTextToRegex(String text, String regex) {
		Pattern p = Pattern.compile(regex);
		// Pattern p = Pattern.compile(regexForNumberAtEndOfString);
		Matcher m = p.matcher(text);
		return m;
	}

	// public static String regexForNumberAtEndOfString =
	// ".*(?:\\D|^)(\\d+)|.*shared|.*Service Credit|.*-$";
	public static String regexForNumberAtEndOfString = ".*(?:\\D|^)(\\d+)|.*shared|.*Service Credit|.*-|.*(.*?)\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})$";
	public static String regexForChargeAmountAtEndOfString = "(.*?)\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})$";

	public static Predicate<String> isPtnOrChargeTypeHeader = x -> textIsPtnOrChargeTypeHeader(x);
	public static Predicate<String> isChargeTypeHeader = x -> textIsChargeHeader(x);

	/*
	 * These are all the first key headers.
	 */
	public static Predicate<String> isFirstKeyHeader = x -> ptnHeaderIsNotEquipGridHeader(x) || x.equals("LAST BILL")
			|| x.equals("ACCOUNT CHARGES") || x.equals("ACCOUNT DETAILS") || x.startsWith("Equipment Payment Schedule")
			|| x.equals("EQUIPMENT DETAILS BY SUBSCRIBER");

	public static List<String> firstKeys() {
		List<String> listOfEquipTypes = new ArrayList<>();
		listOfEquipTypes.add("LAST BILL");
		listOfEquipTypes.add("ACCOUNT CHARGES");
		listOfEquipTypes.add("ACCOUNT DETAILS");
		listOfEquipTypes.add("EQUIPMENT DETAILS BY SUBSCRIBER");
		return listOfEquipTypes;
	}

	public boolean textIsFirstKey(String text) {
		return testString(text, firstKeys());
	}

	public static boolean textIsChargeHeader(String text) {
		return textIsChargeTypeHeader(text);
	}

	public static boolean textIsPtnOrChargeTypeHeader(String text) {
		return textIsPtnHeaderForClCharges(text) || textIsChargeTypeHeader(text);
	}

	public static boolean textIsPtnHeaderForClCharges(String text) {
		boolean ptnHeader = text.matches(regexAcceptsOnlyPhoneNumber);
		boolean textContainsPhoneNumber = text.matches(regexAcceptsPhoneNumberAnywhereInText);
		boolean textIsOrderNumber = text.startsWith("Order Number");
		boolean textIsEquip = textStartsWith(text, textIsEquipHeader());
		// return ptnHeader&&textContainsPhoneNumber &&textIsEquip;
		return ((!ptnHeader)
				&& (text.startsWith("Apple") || text.startsWith("Samsung") || text.startsWith("Kyocera")
						|| text.startsWith("LG"))
				&& (text.matches(regexAcceptsPhoneNumberAnywhereInText)) || text.startsWith("Order Number"));
	}

	public static List<String> textIsEquipHeader() {
		List<String> listOfEquipTypes = new ArrayList<>();
		listOfEquipTypes.add("Apple");
		listOfEquipTypes.add("Samsung");
		listOfEquipTypes.add("Kyocera");
		listOfEquipTypes.add("LG");
		return listOfEquipTypes;
	}

	public static boolean textStartsWithPtnButDoesNotEndWithCharge(String text) {
		return text.matches(regexAcceptsPhoneNumberAndTextAfterIt);
	}

	// when the ptn is a header, it has a space between the area code and phone
	// number.
	// when a ptn is a supplementary text underneath charge desc and amount it does
	// not have this space.
	public static String spaceForHeaderPTnRegex = "^.{5}\\s.*|";

	public static boolean textIsChargeTypeHeader(String text) {
		return testString(text, typesOfCharges()) || textIsPtnHeaderForClCharges(text);
	}

	public static boolean ptnHeaderIsNotEquipGridHeader(String text) {
		boolean lineIsSinglePtn = text.matches(regexAcceptsOnlyPhoneNumber);
		boolean textStartsWithptn = text.matches(regexAcceptsPhoneNumberAndTextAfterIt);
		boolean textDoesNotHaveEquipInfo = !text.contains("Apple") && !text.contains("Samsung")
				&& !text.contains("Kyocera") && !text.contains("LG");
		boolean ptnHasChargeAtEnd = text.matches(regexForChargeAmountAtEndOfString);
		return textStartsWithptn && textDoesNotHaveEquipInfo && !ptnHasChargeAtEnd && !lineIsSinglePtn;
	}

	/*
	 * These are all the second key headers.
	 */

	public static List<String> typesOfCharges() {
		List<String> listOfChargeTypes = new ArrayList<>();
		listOfChargeTypes.add("Welcome");
		listOfChargeTypes.add("Revised Bill");
		listOfChargeTypes.add("Account Name:");
		listOfChargeTypes.add("Account Number:");
		listOfChargeTypes.add("Invoice Number:");
		listOfChargeTypes.add("DAC Number");
		listOfChargeTypes.add("DAC Name");
		listOfChargeTypes.add("Bill Date:");
		listOfChargeTypes.add("Bill Period:");
		listOfChargeTypes.add("TIN Number:");
		listOfChargeTypes.add("ABA Number:");
		listOfChargeTypes.add("Current P.O.:");
		listOfChargeTypes.add("Previous P.O.:");
		listOfChargeTypes.add("Previous P.O. Date:");
		listOfChargeTypes.add("This Bill");
		listOfChargeTypes.add("This DAC");
		listOfChargeTypes.add("Last Bill");
		listOfChargeTypes.add("Payments");
		listOfChargeTypes.add("Adjustments");
		listOfChargeTypes.add("Shared/Pooled Service Usage Report");
		listOfChargeTypes.add("Data Shared Services Usage Report");
		listOfChargeTypes.add("Adjustments to Previous Balance");
		listOfChargeTypes.add("MISC.CHARGES & ADJUSTMENTS");
		listOfChargeTypes.add("MISC. CHARGES & ADJUSTMENTS");
		listOfChargeTypes.add("USAGE");
		listOfChargeTypes.add("EQUIPMENT");
		listOfChargeTypes.add("SPRINT SURCHARGES");
		listOfChargeTypes.add("GOVERNMENT TAXES & FEES");
		listOfChargeTypes.add("SUBSCRIBER ADJUSTMENTS BY DAC");
		listOfChargeTypes.add("Sprint Surcharges");
		listOfChargeTypes.add("Government Taxes & Fees");
		listOfChargeTypes.add("Sprint Premium Services");
		listOfChargeTypes.add("Third Party Charges");
		listOfChargeTypes.add("Government Taxes & Fees");
		listOfChargeTypes.add("Plans & Equipment");
		listOfChargeTypes.add("Account Charges");
		listOfChargeTypes.add("Plans");
		listOfChargeTypes.add("On Content Provider Content Type Description Rate");
		listOfChargeTypes.add("Usage");
		listOfChargeTypes.add("Equipment");
		listOfChargeTypes.add("Equipment Purchases at Retail Stores");
		listOfChargeTypes.add("Description Qty Charge Total");
		listOfChargeTypes.add("Immediate Charges Accrued");
		listOfChargeTypes.add("Sprint Magic Box");
		listOfChargeTypes.add("Equipment Payment Schedule");
		listOfChargeTypes.add("Free & Clear Add a Phone");
		listOfChargeTypes.add("Free & Clear Family Plan - 1000 Anytime Minutes Included");
		listOfChargeTypes.add("Unlimited Freedom - Unlimited Talk, Text & Data");
		listOfChargeTypes.add("Sprint Better Choice - 24 GB Shared High Speed Data");
		listOfChargeTypes.add("Sprint Unlimited Plan - Unlimited Talk, Text & Data");
		listOfChargeTypes.add("Sprint Better Choice - 3 GB Shared High Speed Data");
		listOfChargeTypes.add("Sprint Better Choice - Unlimited 3G/4G High Speed Data");
		return listOfChargeTypes;
	}

	/*
	 * This phone number regex matches any string that starts with a phone number.
	 * This is needed because we utilize phone numbers as headers/keys in our
	 * mapping, but some headers/keys will include extra words after the phone
	 * number(e.g. the mobile device type for that phone number).
	 */

	public static String regexAcceptsOnlyPhoneNumber = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";
	// public static String regexAcceptsOnlyPhoneNumber = "^[0-9]{1,45}$";
	public static String regexAcceptsPhoneNumberAndTextAfterIt = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}.*|";
	public static String regexAcceptsPhoneNumberAnywhereInText = "(.*?)(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}.*|";

	public String combineCharactersSurroundingSlash(String fullText) {
		return fullText.replaceAll(" / ", "/");
	}

	/*
	 * UTILITY METHODS TO MAP HEADER TO PARAGRAPH BELOW IT
	 */

	public static Map<String, String> mapParagraphAsListToHeader(List<String> list,
			Predicate<String> filterForHeaders) {
		Map<String, List<String>> table = new HashMap<String, List<String>>();
		makeMap(list, filterForHeaders, table);
		return convertMapValuesIntoSingleString(table);
	}

	/*
	 * This routine is used by all pojos. Most data is in the form of a list of
	 * Strings where each element is a new line. To parse this, we need to know when
	 * to start and stop collecting data. This routine looks through the data
	 * (list), and, depending on the predicate, it will pull out the elements that
	 * match header text. This routine then uses those headers to find their
	 * indexes. The indexes are put into a list, and the list is returned. For
	 * examples in this class:
	 * 
	 * See Map<String, List<String>> mapChargeDescAndAmountsToHeaders(List<String>
	 * list)
	 * 
	 * or
	 * 
	 * Map<String, String> mapParagraphToHeader(List<String> list)
	 * 
	 */

	public static List<Integer> indicesForHeaderAtTop(List<String> list, Predicate<String> filterForHeaderKeys) {
		List<String> headers = filterForHeaders(list, filterForHeaderKeys);
		List<Integer> indices = IntStream.range(0, headers.size()).mapToObj(i -> list.indexOf(headers.get(i)))
				.collect(Collectors.toList());
		indices.add(list.size());
		// indices.add(list.size() - 1);
		return indices;
	}

	/*
	 * This routine handles when headers (keys) have their values above them. See
	 * the right side of ChargeTypes pojo for examples.
	 */

	public static List<Integer> indicesForHeaderAtBottom(List<String> list, Predicate<String> filterForHeaderKeys) {
		List<String> headers = filterForHeaders(list, filterForHeaderKeys);
		List<Integer> indices = IntStream.range(0, headers.size()).mapToObj(i -> list.indexOf(headers.get(i)))
				.collect(Collectors.toList());
		indices.add(0, 0);
		return indices;
	}

	public static List<String> filterForHeaders(List<String> list, Predicate<String> filterForHeaderKeys) {
		return list.stream().filter(filterForHeaderKeys).collect(Collectors.toList());
	}

	/*
	 * FILTERING MAP UTILITIES
	 */

	public static String getValByMapKey(Map<String, List<String>> map, String key, String value) {
		// System.out.println("THE KEY IS: " + key);
		// System.out.println("THE VALUE IS: " + value);
		List<String> values = map.get(key);
		int posOfVal = values.indexOf(value);
		String val = map.get(key).get(posOfVal);

		return val;
	}

	public static String getStringFromList(List<String> list, String text) {
		int posOfVal = list.indexOf(text);
		return list.get(posOfVal);
	}

	public static String getValByMapKey(Map<String, String> map, String key) {
		String val = map.get(key);
		return val;
	}

	public static <K, V> Map<K, V> filterMapByKey(Map<K, V> map, Predicate<K> predicate) {
		return map.entrySet().stream().filter(x -> predicate.test(x.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public static <K, V> Map<K, V> filterMapByValue(Map<K, V> map, Predicate<V> predicate) {
		return map.entrySet().stream().filter(x -> predicate.test(x.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public static Function<String, Predicate<String>> isEqualTo = control -> candidate -> candidate.equals(control);
	public static Function<String, Predicate<String>> matches = control -> candidate -> candidate.matches(control);
	public static Predicate<String> emptyString = x -> x.equals("") || x == null;

	public static Integer sizeOfMapValuesGivenKey(Map<String, List<String>> map, String key) {
		List<String> values = map.get(key);
		int sizeOfValues = values.size();
		return sizeOfValues;
	}

	public boolean amountOfValuesUnderHeaderIsExpected(Map<String, List<String>> map, String key, String expected)
			throws NumberFormatException, IOException {
		return sizeOfMapValuesGivenKey(map, key) == Integer.parseInt(expected);
	}

	public static Integer orderOfMapValueOnPageGivenKey(Map<String, List<String>> map, String key, String value) {
		List<String> values = map.get(key);
		int positionOrderOfValue = values.indexOf(value);
		return positionOrderOfValue;
	}

	/*
	 * MATCHING UTILITIES
	 */

	public static boolean testString(String text, List<String> list) {
		return list.stream().anyMatch(x -> x.equalsIgnoreCase(text));
	}

	public static boolean textStartsWith(String text, List<String> list) {
		return list.stream().anyMatch(prefix -> text.startsWith(prefix));
	}

	public static boolean testStringAgainstRegex(String textToTest, String regex) {
		return textToTest.matches(regex);
	}

	/*
	 * PDFBOX UTILITIES
	 */

	public static PDDocument doc(String path) throws IOException, InterruptedException {
		PDDocument doc = PDDocument.load(new File(path));
		doc.setDocumentId(Thread.currentThread().getId());
		PDFRegions.pdInstances.add(doc);
		System.out.println("Here is the path: " + path);

		return doc;

	}

	public static PDDocument docStep(String path) {
		for (int i = 0; i < 5; i++) {
			try {
				return PDDocument.load(new File(path));
			} catch (IOException io) {
				System.out.print("File not found");
			} catch (Exception InterruptedException) {
				System.out.println("Interrupted, trying again");
			}
		}
		System.out.println("Unable to find file");
		return null;
	}

	public static PDPage desiredPDFPage(PDDocument doc, int pageNum) {
		return doc.getPage(pageNum);
	}

	public static void closeAllOpenPDDocs() throws IOException {
		for (PDDocument d : getPDDocInstances()) {
			d.close();
		}
	}

	public static void rotateSinglePDFPage(PDPage page, int degreesOfRotation) {
		page.setRotation(degreesOfRotation);
	}

	// made for indexing readability
	public static int pdfPageNum(int desiredPdfPage) {
		return desiredPdfPage - 1;
	}

	public static int indexOfLastElementInList(List<?> list) {
		return list.size() - 1;
	}

	// the method that creates the pddocument adds the doc to this list.
	// We want to have a list of pddocuments so that we can delete all open pddocs
	// at once.
	public static List<PDDocument> pdInstances = new ArrayList<>();

	public static List<PDDocument> getPDDocInstances() {
		return pdInstances;
	}

	/*
	 * CONFIGURE DYNAMICALLY CHANGING POJO
	 */

	/*
	 * This routine lets the user add to the width and/or height of a rectangle.
	 * Consequently, the rectangle keeps its x and y position, but it has a newly
	 * configured width and height on the page. The user needs to pass as parameters
	 * how much width or height values to add to the rectangle's pre-existing width
	 * and height.
	 */

	public void configureRectSize(int widthToAdd, int heightToAdd, Rectangle rect) {
		int oldWidth = rect.width;
		int oldHeight = rect.height;
		rect.setSize(oldWidth + widthToAdd, oldHeight + heightToAdd);
	}

	/*
	 * This routine lets the user add to the x and/or y coordinates of a rectangle.
	 * Consequently, the rectangle keeps its width and height, but it has a new x
	 * and y position on the page. The user needs to pass as parameters how many x
	 * and/or y coordinate points to add to the rectangle's pre-existing x and y
	 * coordinates.
	 */

	public static void configureRectLocation(int xPointsToAdd, int yPointsToAdd, Rectangle rect) {
		int oldXPosition = rect.x;
		int oldYPosition = rect.y;
		rect.setLocation(oldXPosition + xPointsToAdd, oldYPosition + yPointsToAdd);
	}

	/*
	 * This routine lets the user completely redefine the x and/or y coordinates of
	 * a rectangle. Consequently, the rectangle keeps its width and height, but it
	 * has a new x and y position on the page. The user needs to pass as parameters
	 * the x and/or y coordinate points he/she wants to set as the rectangle's new x
	 * and y coordinates.
	 */

	public static void newRectLocation(int xCoord, int yCoord, Rectangle rect) {
		rect.setLocation(xCoord, yCoord);
	}

	public void setYLocation(int yCoord, Rectangle rect) {
		int xCoord = rect.x;
		newRectLocation(xCoord, yCoord, rect);
	}

	/*
	 * This routine lets the user completely redefine the width and/or height of a
	 * rectangle. Consequently, the rectangle keeps its x and y coordinates, but it
	 * has a new width and height on the page. The user needs to pass as parameters
	 * the new width/height he/she wants to set as the rectangle's new size.
	 */

	public void newRectSize(int width, int height, Rectangle rect) {
		rect.setSize(width, height);
	}

	public String deleteSpaces(String element) {
		return element.replaceAll("\\s", "");
	}

	public static String deleteLineBreaks(String element) {
		return element.replaceAll("\\r\\n|\\r|\\n", " ");
	}

	public String deleteBulletsBreaksAndDoubleSpaces(String element) {
		return element.replaceAll("\\s|Â·", " ").replaceAll("[ ]+", " ");
	}

	public Function<String, String> deleteSpaces = text -> deleteSpaces(text);
	public Function<String, String> deleteBullets = text -> deleteBulletsBreaksAndDoubleSpaces(text);

	public boolean subStringMatches(String text, String sub) {
		return text.indexOf(sub) != -1;
	}

	public static List<String> deleteLineBreaksFromList(List<String> list) {
		list.removeIf(emptyString);
		return list.stream().map(x -> deleteLineBreaks(x)).collect(Collectors.toList());
		// return list.parallelStream().map(x ->
		// deleteLineBreaks(x)).collect(Collectors.toList());
	}

	public static void makeMap(List<String> list, Predicate<String> filterForHeaderKeys,
			Map<String, List<String>> table) {
		List<String> valueList = new ArrayList<>();
		List<Integer> keyIndices = indicesForHeaderAtTop(list, filterForHeaderKeys);
		// System.out.println("in 'makeMap' the indices are: " + keyIndices);
		for (int i = 0; i < keyIndices.size() - 1; i++) {
			String key = list.get(keyIndices.get(i));
			// PrintUtil.printKey.accept(key);
			int start = startValues.apply(keyIndices, i);
			int end = endValues.apply(keyIndices, i);
			try {
				valueList = list.subList(start, end);
			} catch (IllegalArgumentException e) {

			}
			table.put(key, valueList);
		}
	}

	public static void makeMapForHeadersAtBottom(List<String> list, Predicate<String> filterForHeaderKeys,
			Map<String, List<String>> table) {
		makeMapForHeader(list, indicesForHeaderAtBottom(list, filterForHeaderKeys), table);
	}

	public static void makeMapForHeader(List<String> list, List<Integer> indexList, Map<String, List<String>> table) {
		List<String> valueList = new ArrayList<>();
		List<Integer> keyIndices = indexList;
		// System.out.println("in 'makeMapForHeader' the indices ares: " + keyIndices);
		for (int i = 0; i < keyIndices.size() - 1; i++) {
			int start = startValues.apply(keyIndices, i);
			int end = endValues.apply(keyIndices, i);
			if (i == 0) {
				start = 0;
			}
			valueList = list.subList(start, end);
			String key = list.get(end);
			table.put(key, valueList);
		}
	}

	static BiFunction<List<Integer>, Integer, Integer> endValues = (list, i) -> {
		return list.get(i + 1);
	};
	static Function<List<?>, Integer> lastElementInList = list -> {
		return list.size() - 1;
	};
	static BiFunction<List<Integer>, Integer, Integer> startValues = (list, i) -> {
		return (list.get(i) + 1);
	};

	public static Map<String, String> convertMapValuesIntoSingleString(Map<String, List<String>> map) {
		return map.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(),
				entry -> entry.getValue().stream().collect(Collectors.joining(" "))));
	}

	public static Map<String, List<String>> joinChargeDescValuesThatDontHaveChargeAmounts(
			Map<String, List<String>> map) {
		return map.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> (entry.getValue())));

	}

	public static Map<String, List<String>> changeHeaderFormat(Map<String, List<String>> map,
			Function<String, String> fix) {
		return map.entrySet().stream()
				.collect(Collectors.toMap(entry -> fix.apply(entry.getKey()), entry -> entry.getValue()));
	}

	public static Map<String, List<String>> changeMapListValues(Map<String, List<String>> map,
			Function<List<String>, List<String>> fix) {
		return map.entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> fix.apply(entry.getValue())));
	}

	public static Map<String, List<String>> changeMapListValuesWithBi(Map<String, List<String>> map,
			BiFunction<List<String>, String, List<String>> fix, String regex) {
		return map.entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> fix.apply(entry.getValue(), regex)));
	}

	public static Map<String, Map<String, List<String>>> changeMapListValuesIntoMap(Map<String, List<String>> map,
			Function<List<String>, Map<String, List<String>>> fix) {
		return map.entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> fix.apply(entry.getValue())));
	}

	static Predicate<Map<String, List<String>>> isEquipHeader = x -> x.containsKey("");

	public static Map<String, Map<String, List<String>>> changeListValuesIntoMapOfSpecificEntries(
			Map<String, Map<String, List<String>>> maps,
			Predicate<? super Entry<String, Map<String, List<String>>>> predicate,
			Function<List<String>, Map<String, List<String>>> fix) {
		// Map<String, Map<String, List<String>>> a = filterMapByValue(maps,
		// isEquipHeader).values().computeIfPresent("",
		// fix.apply(Map.Entry::getKey));

		return null;
	}

	public static String keyRecordDate(String record) {
		int secondSpaceIndex = record.indexOf(" ", record.indexOf(" ") + 1);
		String date = record.substring(0, secondSpaceIndex);
		return date;
	}

	public static String removeFirstCharacter(String fullText) {
		return fullText.substring(1);
	}

	public static void deleteFileIfItExists(String fileName) {
		File file = new File(fileName);
		deleteFile(file);
	}

	public static boolean deleteFile(File file) {
		boolean fileDeleted = false;
		// if (file.exists()) {
		file.delete();
		fileDeleted = true;
		// }
		return fileDeleted;
	}

	public List<String> appendListToOtherForRecordsSplitBetweenPages(PDPage topPage, PDPage bottomPage,
			Rectangle leftRect, Rectangle rightRect) throws IOException {
		List<String> topList = appendRightRectToLeftRect(topPage, leftRect, rightRect);
		List<String> bottomList = appendRightRectToLeftRect(bottomPage, leftRect, rightRect);
		topList.addAll(bottomList);
		return topList;
	}

	public List<String> appendRightRectToLeftRect(PDPage page, Rectangle leftRect, Rectangle rightRect)
			throws IOException {
		List<String> newList = recordsInRectangleAsList(page, leftRect);
		newList.addAll(recordsInRectangleAsList(page, rightRect));
		return newList;
	}

	public String fullDataAsString(PDPage page, Rectangle rect) throws IOException {
		return PDFRegions.rectangleToTrimmedText(page, rect);
	}

	public List<String> recordsInRectangleAsList(PDPage page, Rectangle rect) throws IOException {
		String fullDataText = fullDataAsString(page, rect);
		List<String> dataAsList = makeRectStringIntoListOfStringsByLineBreak(fullDataText);
		return dataAsList;
	}

	public static List<Integer> iterateIndicesGivenPredicate(List<String> list, BiPredicate<String, String> predicate,
			String regex) {
		List<Integer> indices = new ArrayList<>();
		int startAtBeginningOfList = 0;
		ListIterator<String> it = list.listIterator(startAtBeginningOfList);
		while (it.hasNext()) {
			Integer nextIndex = it.nextIndex();
			String nextElementWithIndex = list.get(nextIndex);
			it.next();
			if ((predicate).test(nextElementWithIndex, regex)) {
				indices.add(nextIndex);
			}
		}
		// System.out.println("for list: " + list + " the indices are: " + indices);
		return indices;
	}

	public static Map<String, List<String>> mapDataToChargeTypeHeaders(List<String> list, String regex) {
		ConcurrentHashMap<String, List<String>> table = new ConcurrentHashMap<String, List<String>>();
		makeMap(list, isChargeTypeHeader, table);
		Map<String, List<String>> cleanMap = cleanChargeMapValues(table, regex);
		// PrintUtil.printMap(cleanMap);
		return cleanMap;
	}

	public static Map<String, List<String>> cleanChargeMapValues(Map<String, List<String>> map, String regex) {
		// changeListValuesIntoMapOfSpecificEntries(map, cleanChargesInList);
		// System.out.println("************this is the map:\n" + map);
		return changeMapListValuesWithBi(map, cleanChargesInList, regex);
	}

	public static BiFunction<List<String>, String, List<String>> cleanChargesInList = (list,
			regex) -> cleanChargesList(list, regex);

	/*
	 * The following routine cleans up the list appendChargeDesc(List<String> list).
	 * The following routine removes any values that do not have charge amounts,
	 * unless that is the first line of the list (which is text that describes the
	 * header).
	 */

	public static List<String> cleanChargesList(List<String> list, String regex) {
		// System.out.println("****THIS IS THE LIST BEFORE CLEANING IT UP:\n" + list);
		final List<String> newList = new ArrayList<>(appendChargeDesc(list, regex));
		newList.removeIf(x -> textDoesNotEndWithChargeAmount(x, regex) && !x.equals(newList.get(0))
				&& !x.equalsIgnoreCase("Dates reflect a change in services.") && !x.equalsIgnoreCase("charges & taxes")
				&& textIsNotChargeEquipPar(x));

		// System.out.println("****THIS IS THE LIST AFTER CLEANING IT UP:\n" + newList);
		return newList;
	}

	/*
	 * The following routine is used to make charges description and amount table
	 * data testable. The following routine reads each line of a list of charge desc
	 * and amounts from bottom to top.
	 * 
	 * The routine grabs text that does not have a charge amount and the routine
	 * appends that text to the line above it. The routine will keep doing this
	 * until the line above the text has a charge amount. The routine then inserts
	 * all the built up text into the line with the charge amount. The routine
	 * inserts the built up text right before the dollar sign or negative sign (if
	 * present).
	 */

	public static List<String> appendChargeDesc(List<String> list, String regex) {
		List<Integer> indicesOfLinesNotHavingChargeAmounts = indicesForNotHavingChargeAtEnd(list, regex);
		int nonlistIndex = (-1);
		int lastValueInList = indicesOfLinesNotHavingChargeAmounts.size() - 1;
		for (int i = lastValueInList; i > nonlistIndex; i--) {
			int lineWithoutCharge = indicesOfLinesNotHavingChargeAmounts.get(i);
			int lineAboveLineWithoutCharge = (lineWithoutCharge - 1);
			String textWithoutCharge = list.get(lineWithoutCharge);

			if (lineAboveLineWithoutCharge == nonlistIndex) {
				break;
			}
			String textAboveLineWithoutCharge = list.get(lineAboveLineWithoutCharge);
			if (textEndsWithChargeAmount(textAboveLineWithoutCharge, regex)
					&& (!textIsLastEquipGridCharge(textAboveLineWithoutCharge))) {
				String textAppended = appendTextBeforeDollarSign(textAboveLineWithoutCharge, textWithoutCharge, regex);
				list.set(lineAboveLineWithoutCharge, textAppended);

			} else if (!textIsLastEquipGridCharge(textAboveLineWithoutCharge)) {
				// this else statement appends the paragraph text together
				StringJoiner combinedText = new StringJoiner(" ");
				// System.out.println("the text above is: "+textAboveLineWithoutCharge+" and the
				// text without charge is "+textWithoutCharge);
				combinedText.add(textAboveLineWithoutCharge).add(textWithoutCharge);
				String appendedTextWithoutCharge = combinedText.toString();
				list.set(lineAboveLineWithoutCharge, appendedTextWithoutCharge);
			}
		}
		return list;
	}

	public static boolean textIsLastEquipGridCharge(String text) {
		boolean isLastEquipGridCharge = text.startsWith("Principal Amount") || text.contains("Month to Month")
				|| text.startsWith("Paid to Date") || text.startsWith("Payments Remaining")
				|| text.startsWith("Payoff Amount") || text.startsWith("Data") || text.startsWith("charges & taxes")
				|| text.startsWith("Total Lease") || text.startsWith("Remaining Lease Commitment");
		// System.out.println("the text: "+text+" is not an equip grid charge:
		// "+isLastEquipGridCharge);
		return isLastEquipGridCharge;
	}

	public static boolean textIsNotChargeEquipPar(String text) {
		return !text.startsWith("Sprint") && !text.startsWith("The Principal") && !text.startsWith("The Installment");
	}

	/*
	 * This routine inserts text into a String that contains a dollar sign, or a
	 * negative sign if present.
	 * 
	 * We want to insert text before a dollar sign when we need to make chargeTypes
	 * or Welcome pojos testable. That is, when a charge has line break(s) within
	 * its charge description, we need to append all those broken up lines together
	 * and then insert them before the dollar sign of the line that has the charge
	 * amount (first line of the charge).
	 *
	 */

	public static String appendTextBeforeDollarSign(String textWithCharge, String textWithoutCharge, String regex) {
		int dollarSignPos = dollarSignPosition(textWithCharge, regex);
		textWithCharge = new StringBuilder(textWithCharge).insert(dollarSignPos, (textWithoutCharge + " ")).toString();
		return textWithCharge;
	}

	/*
	 * The following routine iterates through a list of charge desc and amounts. It
	 * recognizes when a line does not end with a charge desc. It then gets the
	 * index of this line and adds it to a list. The routine returns this list of
	 * indices.
	 * 
	 * We want to know the lines that do not end with charge amounts so that we can
	 * append them to their lines above.
	 */

	public static List<Integer> indicesForNotHavingChargeAtEnd(List<String> list, String regex) {
		return iterateIndicesGivenPredicate(list, lineDoesNotEndWithChargeAmount, regex);
	}

	public static List<Integer> indicesOfEquipGridPaymentHeaders(List<String> list, String regex) {
		return iterateIndicesGivenPredicate(list, lineIsEquipGridHeader, regex);
	}

	public static BiPredicate<String, String> lineIsEquipGridHeader = (x, y) -> x.equals("Equipment Payment Schedule");
	public static BiPredicate<String, String> lineIsOnlyPtn = (x, y) -> x.matches(regexAcceptsOnlyPhoneNumber);
	public static BiPredicate<String, String> lineIsServiceCredit = (x, y) -> x.endsWith("Service Credit")
			&& x.startsWith("$");

	public static List<Integer> indicesOfLineIsOnlyPtn(List<String> list, String regex) {
		return iterateIndicesGivenPredicate(list, lineIsOnlyPtn, regex);
	}

	public static List<Integer> indicesOfLineIsServiceCredit(List<String> list, String regex) {
		return iterateIndicesGivenPredicate(list, lineIsServiceCredit, regex);
	}

	public List<String> appendPtnToEquipGridHeader(List<String> list, String regex) {
		// System.out.println("before appending equipHeaders: " + list);
		List<Integer> indices = indicesOfEquipGridPaymentHeaders(list, regex);
		for (Integer equipGridHeader : indices) {
			Integer ptn = equipGridHeader + 1;
			Integer phoneDetailLoc = equipGridHeader + 2;
			// String makePtnNonHeaderFormat = list.get(ptn).replace(") ", ")");
			String phoneDetail = list.get(phoneDetailLoc);
			String makePtnNonHeaderFormat = phoneDetail + " " + list.get(ptn);

			String newEquipGridHeader = list.get(equipGridHeader) + " " + makePtnNonHeaderFormat;
			list.set(ptn, makePtnNonHeaderFormat);
			list.set(equipGridHeader, newEquipGridHeader);
		}
		// System.out.println("after appending equipHeaders: " + list);
		return list;
	}





	public String retrieveListValByIndex(List<String> list, int index) {
		String val = null;
		try {
			val = list.get(index).trim();
		} catch (ArrayIndexOutOfBoundsException ex) {
		} catch (IndexOutOfBoundsException ex) {
		} catch (NullPointerException ex) {
		}
		return val;
	}

	public List<String> retrieveListFromMap(Map<String, List<String>> map, String key) {
		List<String> list = new ArrayList<>();

		try {
			list = map.get(key);
		} catch (NullPointerException e) {
		} catch (NoSuchElementException e) {
		}
		return list;
	}
}
