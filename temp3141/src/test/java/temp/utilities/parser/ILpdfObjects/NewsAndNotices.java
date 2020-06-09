package com.sprint.iice_tests.utilities.parser.ILpdfObjects;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.pdfbox.pdmodel.PDPage;

public class NewsAndNotices extends PDFRegions {

	/*
	 * GENERALIZED UTILITY METHODS
	 */

	public NewsAndNotices() throws IOException {
		super();
	}

	public String columnText(PDPage page, NewsAndNoticesRegions column) throws IOException {
		return rectangleToTrimmedText(page, column.getRegion());
	}

	public List<String> columnList(PDPage page, NewsAndNoticesRegions column) throws IOException {
		String columnText = columnText(page, column);
		List<String> columnList = PDFRegions.makeRectStringIntoListOfStringsByLineBreak(columnText);
		return columnList;
	}

	public String columnHeader(PDPage page, NewsAndNoticesRegions column) throws IOException {
		int columnHeaderPosition = 0;
		return columnList(page, column).get(columnHeaderPosition);
	}

	public Map<String, String> columnMap(PDPage page, NewsAndNoticesRegions column) throws IOException {
		List<String> colList = columnList(page, column);
		Map<String, String> colMap = mapParagraphToHeader(colList);
		return colMap;
	}

	public Map<String, String> columnFilteredMap(PDPage page, NewsAndNoticesRegions column, String key)
			throws IOException {
		Map<String, String> filteredMapByKey = filterMapByKey(columnMap(page, column), isEqualTo.apply(key));
		return filteredMapByKey;
	}

	public String specificValFromColumnMapByKey(PDPage page, NewsAndNoticesRegions column, String key)
			throws IOException {
		Map<String, String> filteredMap = columnFilteredMap(page, column, key);
		String desiredValue = getValByMapKey(filteredMap, key);
		return desiredValue;
	}

	public static Map<String, String> mapParagraphToHeader(List<String> list) {
		return mapParagraphAsListToHeader(list, isBillingInfoHeader);
	}
	public static Predicate<String> isBillingInfoHeader = x -> textIsBillingInfoHeader(x);

	public static boolean textIsBillingInfoHeader(String text) {
		return testString(text, typesOfBillingInfoHeaders());
	}

	public static List<String> typesOfBillingInfoHeaders() {
		List<String> listOfBillingInfoHeaders = new ArrayList<>();
		listOfBillingInfoHeaders.add("Billing Information");
		listOfBillingInfoHeaders.add("Contact Sprint");
		listOfBillingInfoHeaders.add("Call Detail");
		listOfBillingInfoHeaders.add("E911");
		listOfBillingInfoHeaders.add("Terms & conditions/service updates");
		listOfBillingInfoHeaders.add("Your privacy");
		listOfBillingInfoHeaders.add("ETF per line");
		return listOfBillingInfoHeaders;
	}
	
	/*
	 * FIRST COLUMN UTILITY METHODS
	 */

	public String firstColumnText(PDPage page) throws IOException {
		return columnText(page, NewsAndNoticesRegions.FIRST_COL);
	}

	public List<String> firstColumnList(PDPage page) throws IOException {
		return columnList(page, NewsAndNoticesRegions.FIRST_COL);
	}

	public Map<String, String> firstColumnMap(PDPage page) throws IOException {
		return columnMap(page, NewsAndNoticesRegions.FIRST_COL);
	}

	public Map<String, String> firstColumnFilteredMap(PDPage page, String key) throws IOException {
		return columnFilteredMap(page, NewsAndNoticesRegions.FIRST_COL, key);
	}

	public String specificValFromFirstColumnMapByKey(PDPage page, String key) throws IOException {
		return specificValFromColumnMapByKey(page, NewsAndNoticesRegions.FIRST_COL, key);
	}

	/*
	 * SECOND COLUMN UTILITY METHODS
	 */

	public String secondColumnText(PDPage page) throws IOException {
		return columnText(page, NewsAndNoticesRegions.SECOND_COL);
	}

	public List<String> secondColumnList(PDPage page) throws IOException {
		return columnList(page, NewsAndNoticesRegions.SECOND_COL);
	}

	public Map<String, String> secondColumnMap(PDPage page) throws IOException {
		return columnMap(page, NewsAndNoticesRegions.SECOND_COL);
	}

	public Map<String, String> secondColumnFilteredMap(PDPage page, String key) throws IOException {
		return columnFilteredMap(page, NewsAndNoticesRegions.SECOND_COL, key);
	}

	public String specificValFromSecondColumnMapByKey(PDPage page, String key) throws IOException {
		return specificValFromColumnMapByKey(page, NewsAndNoticesRegions.SECOND_COL, key);
	}

	/*
	 * THIRD COLUMN UTILITY METHODS
	 */

	public String thirdColumnText(PDPage page) throws IOException {
		return columnText(page, NewsAndNoticesRegions.THIRD_COL);
	}

	public List<String> thirdColumnList(PDPage page) throws IOException {
		return columnList(page, NewsAndNoticesRegions.THIRD_COL);
	}

	public Map<String, String> thirdColumnMap(PDPage page) throws IOException {
		return columnMap(page, NewsAndNoticesRegions.THIRD_COL);
	}

	public Map<String, String> thirdColumnFilteredMap(PDPage page, String key) throws IOException {
		return columnFilteredMap(page, NewsAndNoticesRegions.THIRD_COL, key);
	}

	public String specificValFromThirdColumnMapByKey(PDPage page, String key) throws IOException {
		return specificValFromColumnMapByKey(page, NewsAndNoticesRegions.THIRD_COL, key);
	}

	/*
	 * RECTANGLES OF REGIONS ON A NEWS AND NOTICES PAGE OF THE PDF THESE RECTANGLES
	 * FUNCTION WITHIN OUR TESTS LIKE XPATH OR CSS SELECTORS- THEY GET US TEXT THAT
	 * WE PARSE AND THEN TEST AGAINST
	 */

	public enum NewsAndNoticesRegions {
		FIRST_COL(new Rectangle(5, 100, 200, 450)),

		SECOND_COL(new Rectangle(205, 100, 185, 450)),

		THIRD_COL(new Rectangle(395, 100, 185, 450)),

		BOTTOM_RIGHT_MSG(new Rectangle(400, 555, 185, 500)),

		BOTTOM_LEFT_MSG(new Rectangle(0, 555, 400, 500));

		private Rectangle region;

		private NewsAndNoticesRegions(Rectangle dimensions) {
			this.region = dimensions;
		}

		public Rectangle getRegion() {
			return region;
		}
	}
}
