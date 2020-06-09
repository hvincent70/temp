package com.sprint.iice_tests.utilities.parser.CLpdfObjects;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.pdfbox.pdmodel.PDPage;

import com.sprint.iice_tests.utilities.parser.ILpdfObjects.PDFRegions;

public class CL_Charges extends CL_PageBase {

	public List<String> joinedFullPageData(PDPage page) throws IOException {
		List<String> pageData = appendRightRectToLeftRect(page, ClChargeRegions.LEFT_SIDE.getRegion(),
				ClChargeRegions.RIGHT_SIDE.getRegion());
		List<String> cleanPageData = appendPtnToEquipGridHeader(pageData, regexForNumberAtEndOfString);
	//	 PrintUtil.printListItemsOnNewLine(cleanPageData);
		return cleanPageData;
	}
	
	public List<String> leftSideOfChargesData(PDPage page) throws IOException {
		return recordsInRectangleAsList(page, ClChargeRegions.LEFT_SIDE_SMALL.getRegion());
	}

	public List<String> rightSideOfChargesData(PDPage page) throws IOException {
		return recordsInRectangleAsList(page, ClChargeRegions.RIGHT_SIDE.getRegion());
	}

	public String specificLineFromChargesRightSide(PDPage page, int lineIndex) throws IOException {
		return rightSideOfChargesData(page).get(lineIndex);
	}

	public Map<String, Map<String, List<String>>> chargeMap(PDPage page) throws IOException {
		List<String> chargeTypesList = joinedFullPageData(page);
		Map<String, Map<String, List<String>>> chargeTypeMap = clChargeMap(chargeTypesList);
		PrintUtil.printMaps(chargeTypeMap);
		return chargeTypeMap;
	}

	public String orderedChargeValue(PDPage page, String firstKey, String secondKey, int positionOfCharge)
			throws IOException {
		Map<String, Map<String, List<String>>> topMap = chargeMap( page);
		 Map<String, List<String>> innerMap = new  HashMap<String, List<String>>();
		 List<String> values = null;
		try {
			innerMap=topMap.get(firstKey);
		}
		catch(NullPointerException e) {
		}
		try {
			values=innerMap.get(secondKey);
		}catch(NullPointerException e) {
		}
		List<String> listOfValues = values ;
//		System.out.print("this is the list: "+listOfValues);
		String orderedVal = null;
		org.apache.logging.log4j.Logger logger = LogManager.getLogger();
//		String msg = "\nTHE DATA UNDER '"+firstKey+"' and '"+secondKey+"' COULD NOT BE FOUND ON PAGE "+Browser.mapOfPdfPages.get(Thread.currentThread().getId()).get(page)+" IN THE RECTANGLE REGION WE TESTED AGAINST.\nDATA MAY HAVE SHIFTED POSITION ON THE PDF PAGE.\n";
		String msg = "";
		try {
			orderedVal = listOfValues.get(positionOfCharge);
		}
		catch(IndexOutOfBoundsException e){
			logger.info(msg);
		}
		catch(NullPointerException e) {
			logger.info(msg);
		}
		return orderedVal;
	}
	
	public String firstChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 0;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String secondChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 1;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String thirdChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 2;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String fourthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 3;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String fifthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 4;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String sixthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 5;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String seventhChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 6;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String eigthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 7;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String ninthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 8;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String tenthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 9;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String eleventhChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 10;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String twelfthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 11;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String thirteenthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 12;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String fourteenthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 13;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String fifteenthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 14;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String sixteenthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 15;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String seventeenthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 16;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String eigteenthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 17;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String ninteenthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 18;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String twentiethChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 19;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String twentyFirstChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 20;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String twentySecondChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 21;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String twentyThirdChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 22;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String twentyFourthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 23;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String twentyFifthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 24;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String twentySixthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 25;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String twentySeventhChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 26;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String twentyEigthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 27;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String twentyNinthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 28;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String thirtiethChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 29;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String thirtyFirstChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 30;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String thirtySecondChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 31;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String thirtyThirdChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 32;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String thirtyFourthChargeValue(PDPage page, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 33;
		return orderedChargeValue(page, firstKey, secondKey, positionOfCharge);
	}
	
	public String orderedChargeValueForDataSplitBetweenPages(PDPage topPage,PDPage nextPage, String firstKey, String secondKey, int positionOfCharge)
			throws IOException {
		List<String> listOfValues = chargeMapSplitBetweenPages(topPage, nextPage).get(firstKey).get(secondKey);
		//System.out.print("this is the list: "+listOfValues);
		return listOfValues.get(positionOfCharge);
	}
	
	public String firstChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 0;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String secondChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 1;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String thirdChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 2;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String fourthChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 3;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String fifthChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 4;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String sixthChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 5;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String seventhChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 6;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String eigthChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 7;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String ninthChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 8;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String tenthChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 9;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String eleventhChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 10;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String twelfthChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 11;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String thirteenthChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 12;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String fourteenthChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 13;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String fifteenthChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 14;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String sixteenthChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 15;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	
	public String seventeenthChargeValue(PDPage topPage,PDPage nextPage, String firstKey, String secondKey)
			throws IOException {
		int positionOfCharge = 16;
		return orderedChargeValueForDataSplitBetweenPages(topPage,nextPage, firstKey, secondKey, positionOfCharge);
	}
	

	public List<String> chargeListSplitBetweenPages(PDPage topPage, PDPage bottomPage) throws IOException {
		List<String> dataAcrossTwoPages = appendListToOtherForRecordsSplitBetweenPages(topPage, bottomPage,
				ClChargeRegions.LEFT_SIDE.getRegion(), ClChargeRegions.RIGHT_SIDE.getRegion());
		List<String> cleanPageData = appendPtnToEquipGridHeader(dataAcrossTwoPages, regexForNumberAtEndOfString);
		return cleanPageData;
	}

	public Map<String, Map<String, List<String>>> chargeMapSplitBetweenPages(PDPage topPage, PDPage bottomPage)
			throws IOException {
		Map<String, Map<String, List<String>>> chargeTypeMap = clChargeMap(
				chargeListSplitBetweenPages(topPage, bottomPage));
		PrintUtil.printMaps(chargeTypeMap);
		return chargeTypeMap;
	}

	public String chargeForGivenPtnAndChargeType(PDPage page, String ptnKey, String chargeTypeHeaderKey)
			throws IOException {
		Map<String, Map<String, List<String>>> ptnMap = filterMapByKey(chargeMap(page), isEqualTo.apply(ptnKey));
		Map<String, List<String>> chargeTypeValues = ptnMap.get(chargeTypeHeaderKey);
		String desiredValue = getValByMapKey(chargeTypeValues, ptnKey, chargeTypeHeaderKey);
		return desiredValue;
	}

	public static Map<String, List<String>> mapChargeDescAndAmountsToHeaders(List<String> list) {
		ConcurrentHashMap<String, List<String>> table = new ConcurrentHashMap<String, List<String>>();
		makeMap(list, isFirstKeyHeader, table);
		return table;
	}

	public static Map<String, Map<String, List<String>>> clChargeMap(List<String> list) {
		Map<String, List<String>> table = mapChargeDescAndAmountsToHeaders(list);
		return changeMapListValuesIntoMap(table, chargeMap);
	}
	
	public Map<String, List<String>> mapOfChargesToHeaders(PDPage page) throws IOException{
		List<String> chargeTypesList = joinedFullPageData(page);
		Map<String, List<String>> table = mapChargeDescAndAmountsToHeaders(chargeTypesList);
		return table;
	}

	public List<String> rowForThirdPartyTable(PDPage page, int yPosition) throws IOException {
		int headerHeight = 23;
		int headerWidth = 57;
		int descHeaderWidth = (headerWidth + 70);
		List<String> headers = new ArrayList<>();
		// HEADERS
		Rectangle on = new Rectangle(headerWidth, headerHeight);
		configureRectLocation(0, yPosition, on);
		String onText = PDFRegions.rectangleToTrimmedText(page, on);
		headers.add(onText);

		Rectangle contentProvider = new Rectangle(headerWidth, headerHeight);
		configureRectLocation(headerWidth, yPosition, contentProvider);
		String contentProviderText = PDFRegions.rectangleToTrimmedText(page, contentProvider);
		headers.add(contentProviderText);

		Rectangle contentType = new Rectangle(headerWidth, headerHeight);
		configureRectLocation(headerWidth + headerWidth + 25, yPosition, contentType);
		String contentTypeText = PDFRegions.rectangleToTrimmedText(page, contentType);
		headers.add(contentTypeText);

		Rectangle description = new Rectangle(descHeaderWidth, headerHeight);
		configureRectLocation(headerWidth + headerWidth + headerWidth + 15, yPosition, description);
		String descriptionText = PDFRegions.rectangleToTrimmedText(page, description);
		headers.add(descriptionText);

		Rectangle rate = new Rectangle(headerWidth + 50, headerHeight - 5);
		configureRectLocation(headerWidth + headerWidth + headerWidth + headerWidth + 50, yPosition, rate);
		String rateText = PDFRegions.rectangleToTrimmedText(page, rate);
		headers.add(rateText);

		// Rectangle chargesAdjustments = new Rectangle(headerWidth, headerHeight);
		// configureRectLocation(headerWidth+headerWidth+headerWidth+headerWidth+headerWidth,
		// yPosition, chargesAdjustments);
		// String chargesAdjustmentsText = PDFRegions.rectangleToTrimmedText(page,
		// chargesAdjustments);
		// headers.add(chargesAdjustmentsText);
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
		configureRectLocation(xPosition, yPosition, ClChargeRegions.THIRD_PARTY_ROW_CELL.getRegion());
	}

	public void newLocationForThirdPartyRow(int xCoord, int yCoord) {
		newRectLocation(xCoord, yCoord, ClChargeRegions.THIRD_PARTY_ROW.getRegion());
	}

	static Function<List<String>, Map<String, List<String>>> chargeMap = (list) -> mapDataToChargeTypeHeaders(list,
			regexForNumberAtEndOfString);
	static Function<List<String>, Map<String, List<String>>> mapHandlesImmediateChargesAccrued = (
			list) -> mapDataToChargeTypeHeaders(list, regexForChargeAmountAtEndOfString);

	public enum ClChargeRegions {

		LEFT_SIDE(new Rectangle(5, 100, 400, 500)),
		
		LEFT_SIDE_SMALL(new Rectangle(5, 100, 400, 300)),

		RIGHT_SIDE(new Rectangle(405, 90, 450, 500)),

		CHARGES_TYPE_RECT(new Rectangle(375, 375)),

		THIRD_PARTY_ROW_CELL(new Rectangle(50, 25)),

		THIRD_PARTY_ROW(new Rectangle(500, 25));

		private Rectangle region;

		private ClChargeRegions(Rectangle dimensions) {
			this.region = dimensions;
		}

		public Rectangle getRegion() {
			return region;
		}
	}
}
