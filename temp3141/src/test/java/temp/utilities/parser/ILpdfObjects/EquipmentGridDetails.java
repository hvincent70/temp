package com.sprint.iice_tests.utilities.parser.ILpdfObjects;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import org.apache.pdfbox.pdmodel.PDPage;

public class EquipmentGridDetails extends PDFRegions {

	/*
	 * CONFIGURABLE EQUIPMENT DETAILS UTILITIES: the following utilities can be used
	 * to adjust the dimensions of the generic rectangle we use to grab the text of
	 * a single equipment grid section. All you need to do is execute one of the
	 * following methods first, and then you can proceed to use the 'UTILITY METHODS
	 * FOR A SINGLE EQUIPMENT GRID'
	 */

	public EquipmentGridDetails() throws IOException {
		super();
	}

	public void configureEquipGridRectLocation(int xPointsToAdd, int yPointsToAdd) {
		configureRectLocation(xPointsToAdd, yPointsToAdd, EquipDetailsRegions.EQUIP_GRID.getRegion());
	}

	public void configureEquipGridRectSize(int widthToAdd, int heightToAdd) {
		configureRectSize(widthToAdd, heightToAdd, EquipDetailsRegions.EQUIP_GRID.getRegion());
	}

	public void newEquipGridRectLocation(int newXCoord, int newYCoord) {
		newRectLocation(newXCoord, newYCoord, EquipDetailsRegions.EQUIP_GRID.getRegion());
	}

	public void newEquipGridRectSize(int newWidth, int newHeight) {
		newRectSize(newWidth, newHeight, EquipDetailsRegions.EQUIP_GRID.getRegion());
	}

	/*
	 * UTILITY METHODS FOR A SINGLE EQUIPMENT GRID
	 */

	public String equipGridText(PDPage page) throws IOException {
		return PDFRegions.rectangleToTrimmedText(page, EquipDetailsRegions.EQUIP_GRID.getRegion());
	}

	public List<String> equipGridList(PDPage page) throws IOException {
		String equipGridText = equipGridText(page);
		List<String> equipGridList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(equipGridText);
		return equipGridList;
	}

	public Map<String, List<String>> equipGridMap(PDPage page) throws IOException {
		List<String> equipGridList = equipGridList(page);
		Map<String, List<String>> equipGridMap = mapEquipGridToHeaders(equipGridList);
		return equipGridMap;
	}

	public Map<String, List<String>> equipGridFilteredMap(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMap = filterMapByKey(equipGridMap(page), isEqualTo.apply(key));
		return filteredMap;
	}

	public String equipGridParagraph(PDPage page, String key, String beginText, String endText) throws IOException {
		List<String> valuesFromMap = equipGridFilteredMap(page, key).get(key);
	//	System.out.println("all the values from the map: " + valuesFromMap);
		return subListAsSingleString(valuesFromMap, beginText, endText);
	}

	public String specificValFromEquipGridMapByKey(PDPage page, String key, String value) throws IOException {
		Map<String, List<String>> filteredMap = equipGridFilteredMap(page, key);
		String desiredValue = getValByMapKey(filteredMap, key, value);
	//	System.out.println("THE KEY IS: " + key);
		return desiredValue;
	}

	public boolean specificValFromEquipGridMapByKeyExists(PDPage page, String key, String value) throws IOException {
		Map<String, List<String>> filteredMap = equipGridFilteredMap(page, key);
		try {
			return getValByMapKey(filteredMap, key, value) != null;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean specificValFromEitherSideEquipGridMapByKeyExists(PDPage page, String key, String value)
			throws IOException {
		return specificValFromLeftSideEquipGridMapByKeyExists(page, key, value)
				|| specificValFromRightSideEquipGridMapByKeyExists(page, key, value);
	}

	public List<String> getListWithKey(PDPage page, String key) throws IOException {
		return leftSideListFromKey(page, key).size() > 0 ? leftSideListFromKey(page, key) : rightSideListFromKey(page, key);
	}

	/*
	 * LEFT SIDE OF PURE EQUIP GRID PDF PAGE the following utility methods can be
	 * used for a charges type page that only contains equip grid po
	 */
	public void configureLeftSideEquipGridLocation(int xPointsToAdd, int yPointsToAdd) {
		configureRectLocation(xPointsToAdd, yPointsToAdd, EquipDetailsRegions.LEFT_HALF_EQUIP_GRID.getRegion());
	}
	public String leftSideEquipGridText(PDPage page) throws IOException {
		return PDFRegions.rectangleToTrimmedText(page, EquipDetailsRegions.LEFT_HALF_EQUIP_GRID.getRegion());
	}

	public List<String> leftSideEquipGridList(PDPage page) throws IOException {
		String leftEquipGridText = leftSideEquipGridText(page);
		List<String> leftEquipGridList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(leftEquipGridText);
		return leftEquipGridList;
	}

	public Map<String, List<String>> leftSideEquipGridMap(PDPage page) throws IOException {
		List<String> leftEquipGridList = leftSideEquipGridList(page);
		Map<String, List<String>> leftEquipGridMap = mapEquipGridToHeaders(leftEquipGridList);
		return leftEquipGridMap;
	}

	public Map<String, List<String>> leftSideEquipGridFilteredMap(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMap = filterMapByKey(leftSideEquipGridMap(page), isEqualTo.apply(key));
		return filteredMap;
	}

	public List<String> leftSideListFromKey(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMap = filterMapByKey(leftSideEquipGridMap(page), isEqualTo.apply(key));
		List<String>vals=new ArrayList<>();
		if(filteredMap.containsKey(key)) {
			vals=filteredMap.get(key);
		}
		return vals;
	}

	public String specificValFromLeftSideEquipGridMapByKey(PDPage page, String key, String value) throws IOException {
		Map<String, List<String>> filteredMap = leftSideEquipGridFilteredMap(page, key);
		String desiredValue = getValByMapKey(filteredMap, key, value);
		return desiredValue;
	}

	public String equipGridParagraphLeftSide(PDPage page, String key, String beginText, String endText)
			throws IOException {
		List<String> valuesFromMap = leftSideEquipGridFilteredMap(page, key).get(key);
	//	System.out.println("all the values from the map: " + valuesFromMap);
		return subListAsSingleString(valuesFromMap, beginText, endText);
	}

	public boolean specificValFromLeftSideEquipGridMapByKeyExists(PDPage page, String key, String value)
			throws IOException {
		Map<String, List<String>> filteredMap = leftSideEquipGridFilteredMap(page, key);
		try {
			return stringExistsInCombinedList(filteredMap, key, value);
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * RIGHT SIDE OF PURE EQUIP GRID PDF PAGE the following utility methods can be
	 * used for a charges type page that only contains equip grid po
	 */
	public void configureRightSideEquipGridLocation(int xPointsToAdd, int yPointsToAdd) {
		configureRectLocation(xPointsToAdd, yPointsToAdd, EquipDetailsRegions.RIGHT_HALF_EQUIP_GRID.getRegion());
	}
	public String rightSideEquipGridText(PDPage page) throws IOException {
		return PDFRegions.rectangleToTrimmedText(page, EquipDetailsRegions.RIGHT_HALF_EQUIP_GRID.getRegion());
	}

	public List<String> rightSideEquipGridList(PDPage page) throws IOException {
		String rightEquipGridText = rightSideEquipGridText(page);
		List<String> rightEquipGridList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(rightEquipGridText);
		return rightEquipGridList;
	}

	public Map<String, List<String>> rightSideEquipGridMap(PDPage page) throws IOException {
		List<String> rightEquipGridList = rightSideEquipGridList(page);
		Map<String, List<String>> rightEquipGridMap = mapEquipGridToHeaders(rightEquipGridList);
		printMap(rightEquipGridMap);
		return rightEquipGridMap;
	}

	public Map<String, List<String>> rightSideEquipGridFilteredMap(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMap = filterMapByKey(rightSideEquipGridMap(page), isEqualTo.apply(key));
		return filteredMap;
	}

	public List<String> rightSideListFromKey(PDPage page, String key) throws IOException {
		Map<String, List<String>> filteredMap = filterMapByKey(leftSideEquipGridMap(page), isEqualTo.apply(key));
		List<String>vals=new ArrayList<>();
		if(filteredMap.containsKey(key)) {
			vals=filteredMap.get(key);
		}
		return vals;
	}

	public String specificValFromRightSideEquipGridMapByKey(PDPage page, String key, String value) throws IOException {
		Map<String, List<String>> filteredMap = rightSideEquipGridFilteredMap(page, key);
		String desiredValue = getValByMapKey(filteredMap, key, value);
		return desiredValue;
	}

	public boolean specificValFromRightSideEquipGridMapByKeyExists(PDPage page, String key, String value)
			throws IOException {
		Map<String, List<String>> filteredMap = rightSideEquipGridFilteredMap(page, key);
		try {
			return stringExistsInCombinedList(filteredMap, key, value);
		} catch (Exception e) {
			return false;
		}
	}
	
	public List<String> eitherSideEquipMentGridFilteredMap(PDPage page, String key){
		try {
			Map<String, List<String>> filteredMap = rightSideEquipGridFilteredMap(page, key);
			return filteredMap.get(key);
		} catch (IOException e) {
			try {
			Map<String, List<String>> filteredMap = leftSideEquipGridFilteredMap(page, key);
			return filteredMap.get(key);
			} catch(Exception ex) {
				return null;
			}
		}
		
	}

	public static Map<String, List<String>> mapEquipGridToHeaders(List<String> list) {
		ConcurrentHashMap<String, List<String>> table = new ConcurrentHashMap<String, List<String>>();
		makeMap(list, filterForEquipGridHeader, table);
		return (table);
	}

	public static Predicate<String> filterForEquipGridHeader = x -> textIsEquipGridrHeader(x);

	public static boolean textIsEquipGridrHeader(String text) {
		return text.matches(regexAcceptsPhoneNumberAndTextAfterIt)
				|| testStringForEquipmentGridHeader(text, regexForEquipment());
	}

	public static boolean testStringForEquipmentGridHeader(String text, List<String> list) {
		return list.stream().anyMatch(x -> text.matches(x));
	}

	public static List<String> regexForEquipment() {
		List<String> list = new ArrayList<>();
		list.add(regexForSamsungDevice);
		list.add(regexForIPhoneDevice);
		list.add(regexForAppleDevice);
		list.add("Accessory");
		return list;
	}

	public static String regexForSamsungDevice = "^Samsung.*$";
	public static String regexForIPhoneDevice = "^iPhone.*$";
	public static String regexForAppleDevice = "^Apple.*$";

	public void resetEquipmentGrid() {
		Rectangle rect = EquipDetailsRegions.EQUIP_GRID.getRegion();
		System.out.println("THE USAGE TABLE COORD BEFORE THE RESET IS: " + rect);
		int xCoord = 0;
		int yCoord = 0;
		int width = 300;
		int height = 350;
		newRectLocation(xCoord, yCoord, rect);
		newRectSize(width, height, rect);
	}

	/*
	 * RECTANGLES OF REGIONS ON AN EQUIPMENT DETAILS PAGE OF THE PDF THESE
	 * RECTANGLES FUNCTION WITHIN OUR TESTS LIKE XPATH OR CSS SELECTORS- THEY GET US
	 * TEXT THAT WE PARSE AND THEN TEST AGAINST
	 */

	public enum EquipDetailsRegions {

		LEFT_HALF_EQUIP_GRID(new Rectangle(5, 100, 300, 800)),

		RIGHT_HALF_EQUIP_GRID(new Rectangle(305, 100, 300, 800)),

		TOP_LEFT_EQUIP_GRID(new Rectangle(5, 100, 300, 300)),

		BOTTOM_LEFT_EQUIP_GRID(new Rectangle(5, 400, 300, 800)),

		EQUIP_GRID(new Rectangle(300, 350));

		private Rectangle region;

		private EquipDetailsRegions(Rectangle dimensions) {
			this.region = dimensions;
		}

		public Rectangle getRegion() {
			return region;
		}
	}
}
