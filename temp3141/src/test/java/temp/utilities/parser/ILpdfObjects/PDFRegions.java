package com.sprint.iice_tests.utilities.parser.ILpdfObjects;

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
import java.util.NoSuchElementException;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class PDFRegions extends PDFTextStripper {

	final static String DOWNLOAD_FOLDER = "/src/test/resources/TestData";
	static File dir = new File(System.getProperty("user.dir") + DOWNLOAD_FOLDER);

	public PDFRegions() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

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

	public static List<String> makeRectIntoListOfString(PDPage page, Rectangle rect) throws IOException {
		return makeRectStringIntoListOfStringsByLineBreak(PDFRegions.rectangleToTrimmedText(page, rect));
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
		// return element.replaceAll("\u00A0", "").trim();
		String takeOut = element.replaceAll("(?<!(/|planfit)(?:.*)?)\u00A0", "");

		// return element.replaceAll("(?<!/)[0-9a-zA-z _\\.\\-:]*\u00A0$", "").trim();
		return takeOut.trim();
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

	// use for troubleshooting rectangle regions
	@SuppressWarnings("unchecked")
	public void lineBreakStartEndPoints(String text) {
		List<String> lines = makeRectStringIntoListOfStringsByLineBreak(text);
		lines.stream().forEachOrdered(printBegAndEndOfLine);
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
		// String combinedText = list.stream().collect(Collectors.joining(" "));
		// System.out.println("This is the combined text: " + combinedText);
		String fixURL = combinedText.replaceAll("sprint.com/ ", "sprint.com/");
		return fixURL;
	}

	public String priceAtEndOfString(String text) {
		String price = text.substring(text.lastIndexOf("$") + 1).trim();
		// System.out.println("THE PRICE IS: " + price);
		return price;
	}

	public static List<String> createSublist(List<String> list, String beginText, String endText) {
		List<String> innerList = new ArrayList<>();
		// System.out.println("this is the list " + list);
		String beginningElement = null;
		String endingElement = null;
		try {
			beginningElement = list.stream().filter(x -> x.startsWith(beginText) || x.equalsIgnoreCase(beginText))
					.findFirst().get();
			endingElement = list.stream().filter(x -> x.endsWith(endText) || x.equalsIgnoreCase(endText)).findFirst()
					.get();
		} catch (NoSuchElementException e) {

		} catch (NullPointerException e) {

		} catch (ArrayIndexOutOfBoundsException e) {
		} catch (IndexOutOfBoundsException e) {
		}
		int startIndex = list.indexOf(beginningElement);
		int endIndex = list.indexOf(endingElement) + 1;
		try {
			innerList = list.subList(startIndex, endIndex);
		} catch (ArrayIndexOutOfBoundsException e) {
		} catch (IndexOutOfBoundsException e) {
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
try {
		valueList = list.subList(headerForData, nextHeaderOnPage);
}catch(IndexOutOfBoundsException e) {
	
}
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

	public static Map<String, List<String>> mapChargeDescAndAmountsToHeaders(List<String> list) {
		ConcurrentHashMap<String, List<String>> table = new ConcurrentHashMap<String, List<String>>();
		makeMap(list, isChargeTypeHeader, table);
		Map<String, List<String>> cleanMap = cleanChargeMapValues(table);
		printMap(cleanMap);
		return cleanMap;
	}

	public String chargeAmountGivenMapKeyValue(Map<String, List<String>> map, String key, String value) {
		Map<String, List<String>> filteredMapByKey = filterMapByKey(map, isEqualTo.apply(key));
		printMap(filteredMapByKey);
		String desiredValue = getValByMapKey(filteredMapByKey, key, value);
		printValues.accept(desiredValue);
		System.out.println("the price at the end of the string is: " + priceAtEndOfString(desiredValue));
		return priceAtEndOfString(desiredValue);
	}

	/*
	 * The following routine cleans up the list appendChargeDesc(List<String> list).
	 * The following routine removes any values that do not have charge amounts,
	 * unless that is the first line of the list (which is text that describes the
	 * header).
	 */

	public static List<String> cleanChargesList(List<String> list) {
		final List<String> newList = new ArrayList<>(appendChargeDesc(list));
		newList.removeIf(x -> textDoesNotEndWithChargeAmount(x) && !x.equals(newList.get(0)));
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

	public static List<String> appendChargeDesc(List<String> list) {
		List<Integer> indicesOfLinesNotHavingChargeAmounts = indicesForNotHavingChargeAtEnd(list);
		// System.out.println(
		// "these are the indices for lines without charge amounts: " +
		// indicesOfLinesNotHavingChargeAmounts);
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
			if (textEndsWithChargeAmount(textAboveLineWithoutCharge)) {
				String textAppended = appendTextBeforeDollarSign(textAboveLineWithoutCharge, textWithoutCharge);
				list.set(lineAboveLineWithoutCharge, textAppended);

			} else {
				// this else statement appends the paragraph text together
				StringJoiner combinedText = new StringJoiner(" ");
				combinedText.add(textAboveLineWithoutCharge).add(textWithoutCharge);
				String appendedTextWithoutCharge = combinedText.toString();
				list.set(lineAboveLineWithoutCharge, appendedTextWithoutCharge);
			}
		}
		return list;
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

	public static String appendTextBeforeDollarSign(String textWithCharge, String textWithoutCharge) {
		int dollarSignPos = dollarSignPosition(textWithCharge);
		textWithCharge = new StringBuilder(textWithCharge).insert(dollarSignPos, (textWithoutCharge + " ")).toString();
		return textWithCharge;
	}

	/*
	 * This routine finds the last position of the dollar sign (or negative sign if
	 * present) in a String.
	 */

	public static Integer dollarSignPosition(String text) {
		String t = text;
		int dollarSignPosition = 0;
		Matcher m = matchTextToChargeAmountAtEndRegex(text);
		if (m.find()) {
			String match = m.group(1);
			dollarSignPosition = t.lastIndexOf("$");
			int negativeSignBeforeDollarSign = dollarSignPosition - 1;
			// sometimes the customer has a negative charge. This is reflected by a negative
			// sign before the dollar sign.
			// this handles where to insert text below when there is a negative sign
			if ((text.lastIndexOf("-") == negativeSignBeforeDollarSign)) {
				dollarSignPosition = negativeSignBeforeDollarSign;
			}
			return dollarSignPosition;
		}
		// System.out.println("DID NOT FIND DOLLAR SIGN POSITION FOR: " + text);
		return dollarSignPosition;
	}

	/*
	 * This routine gets the charge amounts from a list of Strings that end with
	 * charge amounts.
	 */

	public static List<String> chargeAtEndOfStringForList(List<String> list) {
		List<String> newList = list;
		List<String> listOfCharges = newList.stream().map(grabChargeFromText).collect(Collectors.toList());
		return listOfCharges;
	}

	public static String chargeAtEndOfString(String text) {
		int positionOfCharge = dollarSignPosition(text);
		return text.substring(positionOfCharge, text.length() - 1);
	}

	static Function<String, String> grabChargeFromText = text -> {
		return chargeAtEndOfString(text);
	};

	public static Matcher matchTextToChargeAmountAtEndRegex(String text) {
		Pattern p = Pattern.compile(regexForChargeAmountAtEndOfString);
		Matcher m = p.matcher(text);
		return m;
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

	public static List<Integer> indicesForNotHavingChargeAtEnd(List<String> list) {
		List<Integer> indices = new ArrayList<>();
		int startAtBeginningOfList = 0;
		ListIterator<String> it = list.listIterator(startAtBeginningOfList);
		while (it.hasNext()) {
			Integer nextIndex = it.nextIndex();
			String nextElementWithIndex = list.get(nextIndex);
			it.next();
			if ((lineDoesNotEndWithChargeAmount).test(nextElementWithIndex)) {
				indices.add(nextIndex);
			}
		}
		return indices;
	}

	public static List<Integer> indicesForFilteredText(Predicate<String> filterForDesiredText, List<String> list) {
		List<Integer> indices = new ArrayList<>();
		int startAtBeginningOfList = 0;
		ListIterator<String> it = list.listIterator(startAtBeginningOfList);
		while (it.hasNext()) {
			Integer nextIndex = it.nextIndex();
			String nextElementWithIndex = list.get(nextIndex);
			it.next();
			if ((filterForDesiredText).test(nextElementWithIndex)) {
				indices.add(nextIndex);
			}
		}
		return indices;
	}

	public static List<String> filterForNotHavingChargeAtEnd(List<String> list) {
		List<String> lists = list.stream().filter(lineDoesNotEndWithChargeAmount).collect(Collectors.toList());
		return lists;
	}

	static BiFunction<List<String>, String, Integer> getValueFromList = (list, text) -> {
		return list.indexOf(text);
	};

	public static Predicate<String> lineDoesNotEndWithChargeAmount = x -> textDoesNotEndWithChargeAmount(x);
	public static Predicate<String> lineEndsWithChargeAmount = x -> textEndsWithChargeAmount(x);
	public static Predicate<String> lineEndsWithForwardSlash = x -> x.endsWith("/");

	public static Integer indexOfFirstInstanceOfCharge(List<String> list) {
		String firstInstance = list.stream().filter(lineEndsWithChargeAmount).findFirst().get();
		int indexOfFirstInstance = list.indexOf(firstInstance);
		return indexOfFirstInstance;
	}

	public static List<String> onlyGetChargeTables(List<String> fullList) {

		int indexOfFirstInstance = indexOfFirstInstanceOfCharge(fullList);
		int sizeOfList = (fullList.size() - 1);
		List<String> listOfChargeTable = new ArrayList<>();
		listOfChargeTable = fullList;
		return listOfChargeTable.subList(indexOfFirstInstance, sizeOfList);
	}

	public static boolean textEndsWithChargeAmount(String text) {
		boolean textHasChargeAmountAtEnd = matchTextToChargeAmountAtEndRegex(text).matches();
		return textHasChargeAmountAtEnd;
	}

	public static boolean textDoesNotEndWithChargeAmount(String text) {
		boolean textHasChargeAmountAtEnd = !matchTextToChargeAmountAtEndRegex(text).matches();
		return textHasChargeAmountAtEnd;
	}

	public static String regexForChargeAmountAtEndOfString = "(.*?)\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})";

	public static Map<String, List<String>> cleanChargeMapValues(Map<String, List<String>> map) {
		return changeMapListValues(map, cleanChargesInList);
	}

	public static Function<List<String>, List<String>> cleanChargesInList = list -> cleanChargesList(list);
	public static Predicate<String> isChargeTypeHeader = x -> textIsPtnOrHeader(x);
	public static Predicate<String> filterForChargeDesc = x -> textIsCharge(x);

	public static boolean textIsCharge(String text) {
		return textIsChargeTypeHeader(text);
	}

	public static boolean textIsPtnOrHeader(String text) {
		return textIsPtnHeader(text) || textIsChargeTypeHeader(text);
	}

	public static boolean textIsPtnHeader(String text) {
		return text.matches(regexAcceptsOnlyPhoneNumber) && text.matches(spaceForHeaderPTnRegex);

	}

	// when the ptn is a header, it has a space between the area code and phone
	// number.
	// when a ptn is a supplementary text underneath charge desc and amount it does
	// not have this space.
	public static String spaceForHeaderPTnRegex = "^.{5}\\s.*|";

	public static boolean textIsChargeTypeHeader(String text) {
		return testString(text, typesOfCharges());
	}

	public static List<String> typesOfCharges() {
		List<String> listOfChargeTypes = new ArrayList<>();
		listOfChargeTypes.add("Welcome");
		listOfChargeTypes.add("Revised Bill");
		listOfChargeTypes.add("This Bill");
		listOfChargeTypes.add("Last Bill");
		listOfChargeTypes.add("Payments");
		listOfChargeTypes.add("Adjustments");
		listOfChargeTypes.add("Sprint Surcharges");
		listOfChargeTypes.add("Surcharges");
		listOfChargeTypes.add("Government Taxes & Fees");
		listOfChargeTypes.add("Sprint Premium Services");
		listOfChargeTypes.add("Third Party Charges");
		listOfChargeTypes.add("Government Taxes & Fees");
		listOfChargeTypes.add("Plans & Equipment");
		listOfChargeTypes.add("Account Charges");
		listOfChargeTypes.add("Immediate Charges Accrued");
		listOfChargeTypes.add("Everything Data");
		listOfChargeTypes.add("Sprint Magic Box");
		listOfChargeTypes.add("Free & Clear Add a Phone");
		listOfChargeTypes.add("Free & Clear Family Plan - 1000 Anytime Minutes Included");
		listOfChargeTypes.add("Unlimited Freedom - Unlimited Talk, Text & Data");
		listOfChargeTypes.add("Sprint Better Choice - 24 GB Shared High Speed Data");
		listOfChargeTypes.add("Sprint Unlimited Plan - Unlimited Talk, Text & Data");
		listOfChargeTypes.add("Equipment Payment Schedule");
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

	public static String regexAcceptsOnlyPhoneNumber = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}";
	public static String regexAcceptsPhoneNumberAndTextAfterIt = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}.*|";
	public static String regexAcceptsPhoneNumberAnywhereInText = "(.*?)(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}.*|";

	// refactor so that it gets diff. between each header- I can imagine a test
	// requirement asking for there to be a specified amount of charges under a
	// header
	public static Integer getDifferenceFromIndices(List<Integer> list) {
		List<Integer> listOfDiffs = new ArrayList<>();
		for (int i = 0; i < list.size() - 1; i++) {
			int diff = list.get(i + 1) - list.get(i);
			listOfDiffs.add(diff);
		}
		Integer max = listOfDiffs.stream().mapToInt(v -> v).max().orElseThrow(NoSuchElementException::new);
		return max;
	}

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
		System.out.println("THE KEY IS: " + key);
		System.out.println("THE VALUE IS: " + value);
		List<String> values = map.get(key);
		String val = null;
		Logger logger = LogManager.getLogger();
		String msg = "\nTHE DATA '" + value + "' UNDER '" + key
				+ "' COULD NOT BE FOUND IN THE RECTANGLE REGION WE TESTED AGAINST.\nDATA MAY HAVE SHIFTED POSITION ON THE PDF PAGE.";
		try {
			int posOfVal = values.indexOf(value);
			val = map.get(key).get(posOfVal);
		} catch (IndexOutOfBoundsException e) {
			logger.info(msg);
		} catch (NullPointerException e) {
			logger.info(msg);
		}
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

	public boolean stringExistsInCombinedList(Map<String, List<String>> map, String key, String desired) {
		List<String> val = map.get(key);
		StringBuilder sb = new StringBuilder();
		int j = 1;
		for (int i = 0; i < val.size(); i += j) {
			j = 1;
			if (desired.contains(val.get(i))) {
				sb.append(val.get(i) + " ");
				while (desired.contains(sb.toString()) && !desired.equals(sb.toString().trim())) {
					sb.append(val.get(i + j++) + " ");
				}
				if (desired.equals(sb.toString().trim()))
					return true;
			}
			sb.setLength(0);
		}
		return false;
	}

	public boolean stringExistsInCombinedList(List<String> list, String desired) {
		StringBuilder sb = new StringBuilder();
		int j = 1;
		for (int i = 0; i < list.size(); i += j) {
			j = 1;
			if (desired.contains(list.get(i))) {
				sb.append(list.get(i) + " ");
				while (desired.contains(sb.toString()) && !desired.equals(sb.toString().trim())) {
					sb.append(list.get(i + j++) + " ");
				}
				if (desired.equals(sb.toString().trim()))
					return true;
			}
			sb.setLength(0);
		}
		return false;
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
	 * PRINTING UTILITIES
	 */

	public static Consumer<String> printKey = (x -> System.out.println("Key: " + x));
	public static Consumer<String> printValues = (x -> System.out.println("Values: " + x));
	public static Consumer<String> printOnNewLine = (x -> System.out.println("\n" + x));
	public static Consumer<String> printBegAndEndOfLine = x -> System.out.println("_" + x + "_");
	public static Consumer<String> printBeforeChange = x -> System.out.println("before change: " + x);
	public static Consumer<String> printAfterChange = x -> System.out.println("after change: " + x);
	Consumer<String> printHeader = (x -> System.out.println("\nHEADER: " + x));
	Consumer<String> printPTN = (x -> System.out.println("\nPTN: " + x));
	Consumer<String> printPlan = (x -> System.out.println("\nPLAN: " + x));
	Consumer<String> printUnit = (x -> System.out.println("\nUNIT: " + x));
	Consumer<String> printTotal = (x -> System.out.println("\nTOTAL: " + x));
	public Consumer<String> printEquipmentDetail = (x -> System.out.println("EQUIPMENT DETAIL: " + x));
	public static Consumer<String> printAccountPath = (x -> System.out.println("\nACCOUNT PATH TO JSON DATA: " + x));

	public static void printListItemsOnNewLine(List<String> list) {
		// list.stream().forEach(x -> printOnNewLine.accept(x));
		list.stream().forEach(x -> System.out.println(x));
	}

	public static void printRectangle(PDPage page, Rectangle rect) throws IOException {
		printListItemsOnNewLine(makeRectStringIntoListOfStringsByLineBreak(rectangleToTrimmedText(page, rect)));
	}

	public static void printIndexOfEveryStringInList(List<String> list) {
		list.stream().forEachOrdered(x -> System.out.println("the index of '" + x + "' is " + list.indexOf(x)));
	}

	public static void printMap(Map<String, List<String>> map) {
		map.forEach((K, Y) -> {
			printKey.accept(K);
			Y.stream().forEachOrdered(x -> printValues.accept(x));
		});
	}

	public static String grabChargeAmount(String fullText) {
		String grabCharge = fullText.substring(dollarSignPosition(fullText), fullText.length()).trim();
		/// System.out.println("The charge amount is: " + grabCharge);
		return grabCharge;
	}

	public static String grabChargeDesc(String fullText) {
		String grabDesc = fullText.substring(0, dollarSignPosition(fullText)).trim();
		// System.out.println("The charge description is: " + grabDesc);
		return grabDesc;
	}

	public static void printUsageMap(Map<String, Map<String, String>> map) {
		map.forEach((K, Y) -> {
			Y.forEach((W, X) -> {
				System.out.println("FOR PTN: " + K + " COLUMN: " + W);
				printValues.accept(X);
			});
		});
	}

	public static void printMapValueSingleString(Map<String, String> map) {
		map.forEach((K, Y) -> {
			printKey.accept(K);
			printValues.accept(Y);
		});
	}

	/*
	 * MATCHING UTILITIES
	 */

	public static boolean testString(String text, List<String> list) {
		return list.stream().anyMatch(x -> x.equalsIgnoreCase(text));
	}

	public static boolean testStringAgainstRegex(String textToTest, String regex) {
		return textToTest.matches(regex);
	}

	/*
	 * HANDLING EXPLORATORY PDF UTILITIES
	 */

	// this regex matches any pdf that starts with the prefix "test"
	public static String pdfTestTextRegex = "^test.*pdf$";
	public static String csvFileRegex = ".*csv$";
	public static String pdfTestTxtTextRegex = "^test.*txt$";
	public static String txtFileIsFailedLogger = "^FAILED.*txt$";
	public static String txtFileIsPassedLogger = "^PASSED.*txt$";
	public static String txtFileIsSkippedLogger = "^SKIPPED.*txt$";

	public static boolean pdfIsTestPDF(String textToTest) {
		return testStringAgainstRegex(textToTest, pdfTestTextRegex)
				|| testStringAgainstRegex(textToTest, pdfTestTxtTextRegex) || pdfThatDoNotGetRenamed(textToTest);
	}

	private static boolean pdfThatDoNotGetRenamed(String textToTest) {
		return textToTest.contains("-") && textToTest.contains(".pdf");
	}

	public static Predicate<String> pdfIsTestPDF = x -> pdfIsTestPDF(x);

	public static void deleteAllContentsWithinTestDataFolder() throws IOException, InterruptedException {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (!file.toString().contains("gitkeep"))
				file.delete();
		}
	}

	public static int findIndexOfPrimArray(String targetInArray, String[] arrayName) {
		return IntStream.range(0, arrayName.length).filter(i -> targetInArray == arrayName[i]).findFirst().orElse(-1);
		// return -1 if target is not found
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
		return element.replaceAll("\\s|·", " ").replaceAll("[ ]+", " ");
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
		// System.out.println("the indices ares: "+keyIndices);
		for (int i = 0; i < keyIndices.size() - 1; i++) {
			String key = list.get(keyIndices.get(i));
			int start = startValues.apply(keyIndices, i);
			int end = endValues.apply(keyIndices, i);
			valueList = list.subList(start, end);

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
		// System.out.println("the indices ares: " + keyIndices);
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

	public static String keyRecordDate(String record) {
		int secondSpaceIndex = record.indexOf(" ", record.indexOf(" ") + 1);
		String date = record.substring(0, secondSpaceIndex);
		return date;
	}

	public static String removeFirstCharacter(String fullText) {
		return fullText.substring(1);
	}

	public String retrieveListValByIndex(List<String> list, int index) {
		String val = null;
		try {
			val = list.get(index);
		} catch (ArrayIndexOutOfBoundsException ex) {
		} catch (IndexOutOfBoundsException ex) {
		}
		return val;
	}

	public <T, U, V> int findStartPosition(PDPage page, int x_coord, int y_start,
			BiFunction<PDPage, Integer, String> function, Predicate<String> predicate) {
			for (int i = y_start; i < y_start + 250; i += 10) {
				String cleanText = function.apply(page, i);
				if (predicate.test(cleanText)) {
					return i;
				}
			}
			return 0;
	}
	
	public <T, U, V> int findStartPosition(PDPage page, int x_coord, int y_start,
			BiFunction<PDPage, Integer, String> function, Predicate<String> predicate, int ordinal_position) {
		int count = 0;
			for (int i = y_start; i < y_start + 790; i += 10) {
				String cleanText = function.apply(page, i);
				if (predicate.test(cleanText)) {
					count++;
					if(count == ordinal_position) {
						return i;
					} else {
						i += 50;
					}
				}
			}
			return 0;
	}

}