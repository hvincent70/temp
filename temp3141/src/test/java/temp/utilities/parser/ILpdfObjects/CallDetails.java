package com.sprint.iice_tests.utilities.parser.ILpdfObjects;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDPage;

public class CallDetails extends PDFRegions {

	public CallDetails() throws IOException {
		super();
	}

	public void configureCallDetailsRectLocation(int yPointsToAdd) {
		configureRectLocation(0, yPointsToAdd, CallDetailsRegions.FULL_TABLE.getRegion());
	}

	public void callDetailsIncludesExtraHeaders() {
		int width = 85;
		configureRectSize(width, 0, CallDetailsRegions.FULL_TABLE.getRegion());
	}

	public String fullTableAsString(PDPage page) throws IOException {
		return PDFRegions.rectangleToTrimmedText(page, CallDetailsRegions.FULL_TABLE.getRegion());
	}

	public List<String> recordsInTableAsList(PDPage page) throws IOException {
		String fullTableText = fullTableAsString(page);
		List<String> chargeTypeList = makeRectStringIntoListOfStringsByLineBreak(fullTableText);
		chargeTypeList.removeIf(notARecord);
		return chargeTypeList;
	}

	public List<String> fullTableAsList(PDPage page) throws IOException {
		String fullTableText = fullTableAsString(page);
		List<String> chargeTypeList = makeRectStringIntoListOfStringsByLineBreak(fullTableText);
		return chargeTypeList;
	}

	public String messageAtEndOfPage(PDPage page) throws IOException {
		int lastTextOnPage = fullTableAsList(page).size() - 1;
		String msg  = null;
		try {
			msg=fullTableAsList(page).get(lastTextOnPage);
		}catch(ArrayIndexOutOfBoundsException ex) {
			
		}catch(IndexOutOfBoundsException ex) {
			
		}
		return msg;
	}

	public Map<String, List<String>> fullTableAsMap(PDPage page) throws IOException {
		List<String> tableList = recordsInTableAsList(page);
		Map<String, List<String>> chargeTypeMap = mapCallDetailsTable(tableList);
		return chargeTypeMap;
	}

	public List<String> allCostsFromCallDetailsAsStrings(PDPage page) throws IOException {
		List<String> list = recordsInTableAsList(page);
		list.removeIf(notARecord);
		list.removeIf((hasNoCharge));
		List<String> allCosts = grabChargeFromText.apply(list);
		return allCosts;
	}

	public List<String> allTypesFromCallDetailsAsStrings(PDPage page) throws IOException {
		List<String> list = recordsInTableAsList(page);
		list.removeIf(notARecord);
		List<String> allTypes = sublistTextToJustType.apply(list);
		return allTypes;
	}

	List<Double> allCostsFromCallDetailsAsDoubles(PDPage page) throws IOException {
		List<String> costsWithoutDollarSign = allCostsFromCallDetailsAsStrings(page).stream().map(deleteDollarSign)
				.collect(Collectors.toList());

		costsWithoutDollarSign.forEach(makeStringADouble);
		List<Double> allCostsAsDoubles = costsWithoutDollarSign.stream().map(changeCostStringToDouble)
				.collect(Collectors.toList());
		return allCostsAsDoubles;
	}

	public static Double roundDouble(Double value) {
		return (value * 100 / 100);
	}

	public Double sumCost(PDPage page) throws IOException {
		Double sum = allCostsFromCallDetailsAsDoubles(page).stream().mapToDouble(Double::doubleValue).sum();
		return sum;
	}

	public Consumer<String> makeStringADouble = x -> Double.parseDouble(x);

	Predicate<String> notARecord = x -> textDoesNotStartWithBeginningOfRecord(x);
	Predicate<String> hasNoCharge = x -> textEndsWithNullCost(x);
	public boolean textEndsWithNullCost(String text) {
		return text.endsWith("-");
	}

	public boolean textDoesNotStartWithBeginningOfRecord(String text) {
		return !textMatchesRegex(text, timeRegex) || !textMatchesRegex(text, CALLS_DETAILS_REGEX);
	}

	static Function<String, Double> changeCostStringToDouble = text -> {
		return Double.parseDouble(text);
	};

	static Function<String, String> deleteDollarSign = text -> {
		String noDollars = text.replace("$", "").trim();
		return noDollars;
	};

	static Function<List<String>, List<String>> grabChargeFromText = text -> {
		return chargeAtEndOfStringForList(text);
	};

	public Map<String, List<String>> tableAsFilteredMap(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMapByKey = filterMapByKey(fullTableAsMap(page), isEqualTo.apply(key));
		return filteredMapByKey;
	}

	public List<String> tableAsFilteredMapCosts(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMapByKey = filterMapByKey(fullTableAsMap(page), isEqualTo.apply(key));
		List<String> cost = new ArrayList<String>();
		if(filteredMapByKey.containsKey(key)) {
		for (String s : filteredMapByKey.get(key)) {
			String[] arr = s.split(" ");
			cost.add(arr[arr.length - 1]);
		}}
		return cost;
	}

	public String specificValFromFullTableMapByKey(PDPage page, String key, String value) throws IOException {
		Map<String, List<String>> filteredMap = tableAsFilteredMap(page, key);
		String desiredValue = getValByMapKey(filteredMap, key, value);
		return desiredValue;
	}

	public static Map<String, List<String>> mapCallDetailsTable(List<String> list) {
		Map<String, List<String>> table = new HashMap<String, List<String>>();
		List<Integer> keyIndices = indicesForHeaderAtTop(list, isCallDetailsHeader);
		List<String> valueList = new ArrayList<>();
		for (int i = 0; i < keyIndices.size() - 1; i++) {
			String headerRecord = list.get(keyIndices.get(i));
			String headerDate = keyRecordDate(headerRecord);
			valueList = list.subList(keyIndices.get(i), keyIndices.get(i + 1));
			table.put(headerDate, valueList);
		}
		printMap(table);
		return table;
	}

	/*
	 * RECORDS ARE SPLIT BETWEEN PAGES
	 */

	/**
	 * When a record is split between two pages, use the following methods to
	 * retrieve data by month
	 * 
	 * @param pageEndsWithKey
	 * @param pageAfterThatStartsWithoutKey
	 * @return
	 * @throws IOException
	 */

	public List<String> appendListToOtherForSplitRecords(PDPage pageEndsWithKey, PDPage pageAfterThatStartsWithoutKey)
			throws IOException {
		List<String> newList = recordsInTableAsList(pageEndsWithKey);
		newList.addAll(recordsInTableAsList(pageAfterThatStartsWithoutKey));
		return newList;
	}

	public Map<String, List<String>> combinedTablesOfTwoPagesAsMap(PDPage pageEndsWithKey,
			PDPage pageAfterThatStartsWithoutKey) throws IOException {
		List<String> tableList = appendListToOtherForSplitRecords(pageEndsWithKey, pageAfterThatStartsWithoutKey);
		Map<String, List<String>> chargeTypeMap = mapCallDetailsTable(tableList);
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

	static Function<String, String> changeKeyToRecordDate = text -> {
		return keyRecordDate(text);
	};

	/*
	 * RATE TYPE
	 */

	public String rateTypeAsString(PDPage page) throws IOException {
		return PDFRegions.rectangleToTrimmedText(page, CallDetailsRegions.RATE_TYPE.getRegion());
	}

	public List<String> rateTypeAsList(PDPage page) throws IOException {
		String rateTypeText = rateTypeAsString(page);
		List<String> rateTypeList = makeRectStringIntoListOfStringsByLineBreak(rateTypeText);
		return rateTypeList;
	}

	/*
	 * HEADER ABOVE CALL DETAILS TABLE
	 */

	/*
	 * The header above the Call Details table can vary. To consistently grab
	 * whatever text is above the Call Details table, we get all the text up to
	 * include the Call Details Column headers. We then remove the Call Details
	 * column headers.
	 */

	public List<String> headerAboveTable(PDPage page) throws IOException {
		String beginningOfHeader = "Call";
		String rightBeforeTableDataBegins = "Cost";
		List<String> headerAboveTable = createSublist((fullTableAsList(page)), beginningOfHeader,
				rightBeforeTableDataBegins);
		int columnHeaders = indexOfLastElementInList(headerAboveTable);
		System.out.println("\n***headerAboveTable"+headerAboveTable+"\n***columnHeaders"+columnHeaders);
	
		try{headerAboveTable.remove(columnHeaders);
	}catch(ArrayIndexOutOfBoundsException e) {
	}catch(IndexOutOfBoundsException e) {
	}
		return headerAboveTable;
	}

	public List<String> headersForTable(PDPage page) throws IOException {
		String beginningOfHeader = "Call";
		String rightBeforeTableDataBegins = "Cost";
		List<String> headerAboveTable = createSublist((fullTableAsList(page)), beginningOfHeader,
				rightBeforeTableDataBegins);
		int howMuchToTrim = headerAboveTable(page).size();
		for (int i = 0; i < howMuchToTrim - 1; i++) {
			headerAboveTable.remove(i);
		}
		return headerAboveTable;
	}

	public String headerForTable(PDPage page) throws IOException {
		if (headersForTable(page).size() > 1) {
			String headerAbove = headersForTable(page).get(0);
			String headers = headersForTable(page).get(1);
			int positionOfParticipantsHeader = indexOfTextWeWantToMatchAt(headers, "Participants");
			headers = new StringBuilder(headers).insert(positionOfParticipantsHeader, (headerAbove + " ")).toString();
			return headers;
		} else {
			String header = null;
			try {
				header=headersForTable(page).get(0);
			}catch(ArrayIndexOutOfBoundsException ex) {
				
			}catch(IndexOutOfBoundsException ex) {
				
			}
			return header;
		}
	}

	public static String textBeforeDollarSign(String textWithCharge, String textWithoutCharge) {
		int dollarSignPos = dollarSignPosition(textWithCharge);
		textWithCharge = new StringBuilder(textWithCharge).insert(dollarSignPos, (textWithoutCharge + " ")).toString();
		return textWithCharge;
	}

	/**
	 * we are defining call details header/key based on the date. The date is of the
	 * format MM/dd. However, this method is only looking to see if the characters
	 * before the first space match a month
	 * 
	 * @param e
	 * @return
	 */

	public static boolean recordIsCallDetailsKey(String e) {
		if (e.contains(" ")) {
			int firstSpace = e.indexOf(" ");
			String date = e.substring(0, firstSpace);
			return (date.matches(CALLS_DETAILS_REGEX));
		} else {
			return false;
		}
	}

	public static Predicate<String> isCallDetailsHeader = x -> recordIsCallDetailsKey(x);

	public static final String CALLS_DETAILS_REGEX = "^Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec.*|";
	public static final String timeRegex = "\\b((1[0-2]|0?[1-9]):([0-5][0-9]) ([AaPp][Mm]))";
	public static final String minRegex = "(.*?)([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].*|";
	public static final String typeRegex = " [A-Z]{2} | [A-Z]{2}/[A-Z]{2}";

	public String grabType(String text) {
		String type = grabTextMatchingRegexFromString(text, typeRegex);
		return type;
	}

	public Function<String, String> cleanTime = text -> replaceTimeWithTimeWithoutSpaces(text);
	public Function<String, String> cleanPtn = text -> replacePtnWithPtnWithoutSpaces(text);
	public Function<String, String> grabType = text -> grabType(text);

	public Function<List<String>, List<String>> cleanRecordsForTimeAndPtn = list -> removeSpacesFromTimeAndPtn(list);
	public Function<List<String>, List<String>> cleanRecordsForTime = list -> removeSpacesFromTime(list);
	public Function<List<String>, List<String>> sublistTextToJustType = list -> grabTypeInList(list);

	public Integer indexOfTextWeWantToMatchAt(String text, String regex) {
		String textWeAreLookingToMatch = grabTextMatchingRegexFromString(text, regex);
		int positionOfDesiredText = text.indexOf(textWeAreLookingToMatch);
		return positionOfDesiredText;
	}

	public List<String> grabTypeInList(List<String> list) {
		return list.stream().map(grabType.andThen(deleteSpaces)).collect(Collectors.toList());
	}

	public List<String> removeSpacesFromTimeAndPtn(List<String> list) {
		return list.stream().map(cleanTime.andThen(cleanPtn)).collect(Collectors.toList());
	}

	public List<String> removeSpacesFromTime(List<String> list) {
		return list.stream().map(cleanTime).collect(Collectors.toList());
	}

	public String replaceTimeWithTimeWithoutSpaces(String text) {
		return text.replaceAll(timeRegex, removeSpacesFromTime(text));
	}

	public String replacePtnWithPtnWithoutSpaces(String text) {
		return text.replaceAll(regexAcceptsPhoneNumberAnywhereInText, removeSpacesFromPtn(text));
	}

	public String removeSpacesFromTime(String text) {
		return deleteSpaces(grabTextMatchingRegexFromString(text, timeRegex));
	}

	public String removeSpacesFromPtn(String text) {
		return deleteSpaces(grabTextMatchingRegexFromString(text, regexAcceptsPhoneNumberAnywhereInText));
	}

	public String grabTextMatchingRegexFromString(String text, String regex) {
		Matcher m = matchTextToRegex(text, regex);
		String match = null;
		if (m.find()) {
			match = m.group(0);
		}
		return match;
	}

	public Integer sizeOfTextThatMatchesRegex(String text, String regex) {
		Matcher m = matchTextToRegex(text, regex);
		String match = null;
		int lengthOfMatch = 0;
		if (m.find()) {
			match = m.group(1);
			lengthOfMatch = match.length();
			System.out.println("the size of text " + match + " is " + lengthOfMatch);
		}

		return lengthOfMatch;
	}

	public boolean textMatchesRegex(String text, String regex) {
		Matcher m = matchTextToRegex(text, regex);
		return m.find();

	}

	public static Matcher matchTextToRegex(String text, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		return m;
	}

	/*
	 * RECTANGLES OF REGIONS ON A CALL DETAILS PAGE OF THE PDF THESE RECTANGLES
	 * FUNCTION WITHIN OUR TESTS LIKE XPATH OR CSS SELECTORS- THEY GET US TEXT THAT
	 * WE PARSE AND THEN TEST AGAINST
	 */

	public enum CallDetailsRegions {
		// FULL_TABLE(new Rectangle(0, 155, 400, 800)),
		FULL_TABLE(new Rectangle(0, 100, 400, 800)),

		RATE_TYPE(new Rectangle(400, 700, 355, 50));

		private Rectangle region;

		private CallDetailsRegions(Rectangle dimensions) {
			this.region = dimensions;
		}

		public Rectangle getRegion() {
			return region;
		}
	}
}
