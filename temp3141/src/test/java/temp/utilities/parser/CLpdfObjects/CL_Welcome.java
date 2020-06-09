package com.sprint.iice_tests.utilities.parser.CLpdfObjects;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDPage;

import com.sprint.iice_tests.utilities.parser.ILpdfObjects.PDFRegions;

public class CL_Welcome extends CL_PageBase {

	public String firstColumnText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, ClWelcomeRegions.FIRST_COL.getRegion());
	}
	
	public String secondColumnText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, ClWelcomeRegions.SECOND_COL.getRegion());
	}
	
	public String thirdColumnText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, ClWelcomeRegions.THIRD_COL.getRegion());
	}

	public List<String> secondColumnList(PDPage page) throws IOException {
		String secondColumnText = secondColumnText(page);
		List<String> firstColumnList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(secondColumnText);
		return firstColumnList;
	}
	
	public Map<String, List<String>> secondColumnChargeTypesMap(PDPage page) throws IOException {
		List<String> chargeTypesList = secondColumnList(page);
		Map<String, List<String>> chargeTypeMap = mapDataToChargeTypeHeaders(chargeTypesList,
				regexForChargeAmountAtEndOfString);
		return chargeTypeMap;

		// Map<String, List<String>> table =
		// mapChargeDescAndAmountsToHeaders(firstColumnList(page));
		// return changeMapListValuesIntoMap(table, chargeMap);
	}
	
	public List<String> thirdColumnList(PDPage page) throws IOException {
		String secondColumnText = thirdColumnText(page);
		List<String> firstColumnList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(secondColumnText);
		return firstColumnList;
	}
	
	public Map<String, List<String>> thirdColumnChargeTypesMap(PDPage page) throws IOException {
		List<String> chargeTypesList = thirdColumnList(page);
		Map<String, List<String>> chargeTypeMap = mapDataToChargeTypeHeaders(chargeTypesList,
				regexForChargeAmountAtEndOfString);
		return chargeTypeMap;

		// Map<String, List<String>> table =
		// mapChargeDescAndAmountsToHeaders(firstColumnList(page));
		// return changeMapListValuesIntoMap(table, chargeMap);
	}
	public List<String> firstColumnList(PDPage page) throws IOException {
		String firstColumnText = firstColumnText(page);
		List<String> firstColumnList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(firstColumnText);
		return firstColumnList;
	}
	
	
	public String autoPayText(PDPage page) throws IOException {
		return deleteLineBreaks(rectangleToTrimmedText(page, ClWelcomeRegions.AUTO_PAY.getRegion()));
	}

	public Map<String, List<String>> firstColumnChargeTypesMap(PDPage page) throws IOException {
		List<String> chargeTypesList = firstColumnList(page);
		Map<String, List<String>> chargeTypeMap = mapDataToChargeTypeHeaders(chargeTypesList,
				regexForChargeAmountAtEndOfString);
		return chargeTypeMap;

		// Map<String, List<String>> table =
		// mapChargeDescAndAmountsToHeaders(firstColumnList(page));
		// return changeMapListValuesIntoMap(table, chargeMap);
	}

	public Map<String, List<String>> firstColumnChargeTypesFilteredMap(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMapByKey = filterMapByKey(firstColumnChargeTypesMap(page),
				isEqualTo.apply(key));
		return filteredMapByKey;
	}
	
	public Map<String, List<String>> secondColumnChargeTypesFilteredMap(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMapByKey = filterMapByKey(secondColumnChargeTypesMap(page),
				isEqualTo.apply(key));
		return filteredMapByKey;
	}
	
	public Map<String, List<String>> thirdColumnChargeTypesFilteredMap(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMapByKey = filterMapByKey(thirdColumnChargeTypesMap(page),
				isEqualTo.apply(key));
		return filteredMapByKey;
	}
	
	public String firstColFirstVal(PDPage page, String key) throws IOException {
		int chargePosition = 0;
	List<String> chargeList=	firstColumnChargeTypesMap(page).get(key);
	return retrieveListValByIndex(chargeList, chargePosition);
	}
	

	
	public String firstColSecondVal(PDPage page, String key) throws IOException {
		int chargePosition = 1;
		List<String> chargeList=	firstColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String firstColThirdVal(PDPage page, String key) throws IOException {
		int chargePosition = 2;
		List<String> chargeList=	firstColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String firstColFourthVal(PDPage page, String key) throws IOException {
		int chargePosition = 3;
		List<String> chargeList=	firstColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String firstColFifthVal(PDPage page, String key) throws IOException {
		int chargePosition = 4;
		List<String> chargeList=	firstColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String firstColSixthVal(PDPage page, String key) throws IOException {
		int chargePosition = 5;
		List<String> chargeList=	firstColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String firstColSeventhVal(PDPage page, String key) throws IOException {
		int chargePosition = 6;
		List<String> chargeList=	firstColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String firstColEigthVal(PDPage page, String key) throws IOException {
		int chargePosition = 7;
		List<String> chargeList=	firstColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String firstColNinthVal(PDPage page, String key) throws IOException {
		int chargePosition = 8;
		List<String> chargeList=	firstColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String firstColTenthVal(PDPage page, String key) throws IOException {
		int chargePosition = 8;
		List<String> chargeList=	firstColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String thirdColFirstVal(PDPage page, String key) throws IOException {
		int chargePosition = 0;
		List<String> chargeList=	thirdColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String thirdColSecondVal(PDPage page, String key) throws IOException {
		int chargePosition = 1;
		List<String> chargeList=	thirdColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String thirdColThirdVal(PDPage page, String key) throws IOException {
		int chargePosition = 2;
		List<String> chargeList=	thirdColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String thirdColFourthVal(PDPage page, String key) throws IOException {
		int chargePosition = 3;
		List<String> chargeList=	thirdColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String thirdColFifthVal(PDPage page, String key) throws IOException {
		int chargePosition = 4;
		List<String> chargeList=	thirdColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}

	public String thirdColSixthVal(PDPage page, String key) throws IOException {
		int chargePosition = 5;
		List<String> chargeList=	thirdColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String thirdColSeventhVal(PDPage page, String key) throws IOException {
		int chargePosition = 6;
		List<String> chargeList=	thirdColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String thirdColEigthVal(PDPage page, String key) throws IOException {
		int chargePosition = 7;
		List<String> chargeList=	thirdColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String thirdColNinthVal(PDPage page, String key) throws IOException {
		int chargePosition = 8;
		List<String> chargeList=	thirdColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String secondColFirstVal(PDPage page, String key) throws IOException {
		int chargePosition = 0;
		List<String> chargeList=	secondColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String secondColSecondVal(PDPage page, String key) throws IOException {
		int chargePosition = 1;
		List<String> chargeList=	secondColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String secondColThirdVal(PDPage page, String key) throws IOException {
		int chargePosition = 2;
		List<String> chargeList=	secondColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String secondColFourthVal(PDPage page, String key) throws IOException {
		int chargePosition = 3;
		List<String> chargeList=	secondColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String secondColFifthVal(PDPage page, String key) throws IOException {
		int chargePosition = 4;
		List<String> chargeList=	secondColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}

	public String secondColSixthVal(PDPage page, String key) throws IOException {
		int chargePosition = 5;
		List<String> chargeList=	secondColumnChargeTypesMap(page).get(key);
		return retrieveListValByIndex(chargeList, chargePosition);
	}
	
	public String specificValFromFirstColumnChargeTypesFilteredMap(PDPage page, String key, String value)
			throws IOException {
		Map<String, List<String>> filteredMap = firstColumnChargeTypesFilteredMap(page, key);
		String desiredValue = getValByMapKey(filteredMap, key, value);
		return desiredValue;
	}
	
	public List<List<String>> createLastThreeMonthGraphs(PDPage page) throws IOException {
		List<List<String>> threeMonths = new ArrayList<>();
//		int width = 58;
		int width =100;
		int height = 75;
//		int yCoordToAdd = 40;
		int yCoordToAdd = 38;
		int xMargin = ClWelcomeRegions.LAST_THREE_MONTH_CHARGES.getRegion().x;
		int yMargin = ClWelcomeRegions.LAST_THREE_MONTH_CHARGES.getRegion().y + yCoordToAdd;

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
	
	public String lastThreeMonthsText(PDPage page) throws IOException {
		return rectangleToTrimmedText(page, ClWelcomeRegions.LAST_THREE_MONTH_CHARGES.getRegion());
	}
	
	public String totalAmountDueText(PDPage page) throws IOException {
		return deleteLineBreaks(rectangleToTrimmedText(page, ClWelcomeRegions.AMOUNT_DUE.getRegion()));
	}
	
	public List<String> totalAmountDueList(PDPage page) throws IOException{
		String totalAmountDueText = totalAmountDueText(page);
		List<String> totalAmountDueList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(totalAmountDueText);
		return totalAmountDueList;
	}
	


	public enum ClWelcomeRegions {

		FIRST_COL(new Rectangle(5, 100, 160, 255)),

		SECOND_COL(new Rectangle(165, 100, 160, 250)),

		THIRD_COL(new Rectangle(335, 100, 175, 255)),

		RIGHT_SIDE(new Rectangle(510, 0, 300, 610)),

		AMOUNT_DUE(new Rectangle(335, 345, 175, 100)),
		
		LAST_THREE_MONTH_CHARGES(new Rectangle(25, 350, 350, 100)),
		
		
		
//		AUTO_PAY(new Rectangle(5, 350, 330,70)),;
		AUTO_PAY(new Rectangle(5, 460, 330,50)),;

		private Rectangle region;

		private ClWelcomeRegions(Rectangle dimensions) {
			this.region = dimensions;
		}

		public Rectangle getRegion() {
			return region;
		}
	}

}
