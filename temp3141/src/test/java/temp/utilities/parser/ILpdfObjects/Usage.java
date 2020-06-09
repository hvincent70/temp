package com.sprint.iice_tests.utilities.parser.ILpdfObjects;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDPage;

public class Usage extends PDFRegions {

	int xCoord = 117;

	public Predicate<String> isUserTableHeader = i -> i.startsWith("Anytime Minutes")
			|| i.startsWith("Data / Mobile Hotspot") || i.startsWith("Datos con Hotspot Móvil (Compartido, GB)") || i.startsWith("Group Connect");

	public BiFunction<PDPage, Integer, String> getUserTableHeader = (page, y) -> {
		try {
			newRectLocation(xCoord, y, UsageRegions.COLUMN_HEADER_CELL.getRegion());
			String text = rectangleToTrimmedText(page, UsageRegions.COLUMN_HEADER_CELL.getRegion());
			configureRectLocation(UsageRegions.COLUMN_HEADER_CELL.getRegion().width, 0,
					UsageRegions.COLUMN_HEADER_CELL.getRegion());
			return deleteLineBreaks(text);
		} catch (Exception e) {
			return null;
		}
	};

	public Usage() throws IOException {
		super();
	}

	/*
	 * If the usage table is at the top of the page yCoord= 180. This is configured
	 * so that the x margin is flush. If you are trying to figure out where the
	 * table is located on the page, first troubleshoot with the yCoord to see where
	 * the headers are. Fine tune it so that
	 */

	public List<String> usageTableHeaders(PDPage page, int yCoord) throws IOException {
		int xCoord = 117;
		return createUsageTableHeaders(page, xCoord, yCoord);
	}
	
	public int findUsageTable(PDPage page, int ordinal_position) {
		return findStartPosition(page, xCoord, 100, getUserTableHeader, isUserTableHeader, ordinal_position);
	}

	/*
	 * public List<String> usageTableHeaders(PDPage page, int y_start) throws
	 * IOException { int xCoord = 117; int yCoord = findStartPosition(page, y_start,
	 * xCoord, getUserTableHeader, isUserTableHeader); return
	 * createUsageTableHeaders(page, xCoord, yCoord); }
	 */
	/*
	 * This creates a list of Strings of the header cells of a Usage Data Table.
	 * Using the column header cell dimensions for the Usage Data Table, this
	 * routine creates a max of 8 rectangles on the page that have the same width,
	 * height, and y coordinates. It does this 8 times because that is the max
	 * number of columns that is in a Usage Table. It takes the text from each of
	 * these rectangles and adds it to a list. We are left with a list of the Usage
	 * Table's header values. xCoord is the x margin. yCoord is the distance from
	 * top of the page to the table header.
	 */

	public List<String> createUsageTableHeaders(PDPage page, int xCoord, int yCoord) throws IOException {
		int noChangeToHeight = 0;
		int maxNumOfColumns = 8;
		List<String> list = new LinkedList<>();
		newRectLocation(xCoord, yCoord, UsageRegions.COLUMN_HEADER_CELL.getRegion());

		for (int i = 0; i < maxNumOfColumns; i++) {
			String text = rectangleToTrimmedText(page, UsageRegions.COLUMN_HEADER_CELL.getRegion());
			configureRectLocation(UsageRegions.COLUMN_HEADER_CELL.getRegion().width, noChangeToHeight,
					UsageRegions.COLUMN_HEADER_CELL.getRegion());
			String cleanText = deleteLineBreaks(text);
			list.add(cleanText);
		}
		list.removeIf(emptyString);
		return list;
	}

	/*
	 * USAGE GRAPHS UTILITY METHODS The rectangles within the following methods have
	 * coordinates that are defined by the enum Historical Usage because these bar
	 * graphs are always within the historical usage rectangle. The tester may need
	 * to configure where historical usage is on the pdf page as test data changes.
	 * Once the tester configures the historical usage enum, they are also
	 * configuring the coordinates of these bar graphs. This limits the amount of
	 * configuration the tester needs to do as pdf data changes.
	 */

	/*
	 * The following routine grabs the usage bar graph details. It grabs the user
	 * usage data. This is a number that is presented on top of the bar graph. This
	 * routine creates three rectangles. Each rectangle contains the usage data
	 * number and the month.
	 */

	public List<List<String>> createUsageThreeMonthGraphs(PDPage page) throws IOException {
		List<List<String>> threeMonths = new ArrayList<>();
		int width = 50;
		int height = 100;
		int yCoordToAdd = 40;
		int yMargin = UsageRegions.HISTORICAL_USAGE.getRegion().y + yCoordToAdd;
		int xMargin = UsageRegions.HISTORICAL_USAGE.getRegion().x + 64;

		Rectangle firstMonth = new Rectangle(xMargin, yMargin, width, height);
		threeMonths.add(makeRectIntoListOfString(page, firstMonth));

		Rectangle secondMonth = new Rectangle(firstMonth.x + width, yMargin, width, height);
		threeMonths.add(makeRectIntoListOfString(page, secondMonth));

		Rectangle thirdMonth = new Rectangle(secondMonth.x + width, yMargin, width, height);
		threeMonths.add(makeRectIntoListOfString(page, thirdMonth));

		return threeMonths;
	}

	public Map<String, String> mapOfUsageGraphs(PDPage page) throws IOException {
		Map<String, String> monthToCharge = new HashMap<String, String>();
		for (List<String> list : createUsageThreeMonthGraphs(page)) {
			monthToCharge.put(list.get(1), list.get(0));
		}
		return monthToCharge;
	}

	/*
	 * This routine grabs the bar graph usage details. These details include the
	 * type of plan the customer had and their ptn.
	 */

	public List<String> graphPtnDetails(PDPage page) throws IOException {
		Rectangle header = new Rectangle(UsageRegions.HISTORICAL_USAGE.getRegion().x,
				UsageRegions.HISTORICAL_USAGE.getRegion().y, 280, 40);
		return makeRectIntoListOfString(page, header);
	}

	public String graphPTN(PDPage page) throws IOException {
		int ptnPosition = 1;
		return graphPtnDetails(page).get(ptnPosition);
	}

	public String graphUsagePlan(PDPage page) throws IOException {
		int planPosition = 0;
		return graphPtnDetails(page).get(planPosition);
	}

	/*
	 * This routine grabs the unit that defines the bar graph (e.g. GB).
	 */

	public String graphUnit(PDPage page) throws IOException {
		Rectangle unit = new Rectangle(UsageRegions.HISTORICAL_USAGE.getRegion().x + 202,
				UsageRegions.HISTORICAL_USAGE.getRegion().y + 100, 75, 25);
		return PDFRegions.rectangleToTrimmedText(page, unit);
	}

	/*
	 * This routine grabs the total amount of data presented in the historical usage
	 * bar graph(s).
	 */

	public String graphTotal(PDPage page) throws IOException {
		Rectangle total = new Rectangle(UsageRegions.HISTORICAL_USAGE.getRegion().x + 230,
				UsageRegions.HISTORICAL_USAGE.getRegion().y, 75, 100);
		return PDFRegions.rectangleToTrimmedText(page, total);
	}

	public String retrieveValFromUsageTable(Map<String, Map<String, String>> map, String ptn, String columnHeader) {
		String cellVal = null;
		Map<String, String> innerMap = null;
		Logger logger = LogManager.getLogger();
		String msg = " USAGE TABLE MAY HAVE CHANGED POSITION ON PAGE OR HEADERS MAY BE DIFFERENT";
		try {
			innerMap = map.get(ptn);
		} catch (IndexOutOfBoundsException ex) {
			logger.info(msg);
		} catch (NullPointerException ex) {
			logger.info(msg);
		}
		try {
			cellVal = innerMap.get(columnHeader);
		} catch (IndexOutOfBoundsException ex) {
			logger.info(msg);
		} catch (NullPointerException ex) {
			logger.info(msg);
		}
		return cellVal;
	}
	/*
	 * This routine creates a map within a map for the Usage Table Data.
	 *
	 * Outer map: Key = ptn. Values = inner map
	 * 
	 * Inner map: Key= column header text. Value= column value for the Outer map ptn
	 */

	public Map<String, Map<String, String>> createUsageTable(PDPage page, int yCoordForColumnHeaders,
			String headerForUsageTable) throws IOException {
		resetUsageTable();

		Map<String, Map<String, String>> table = new ConcurrentHashMap<String, Map<String, String>>();
		// System.out.println("THE USAGE TABLE DATA IS: " + usageTableData(page,
		// headerForUsageTable));
		for (String e : usageTableData(page, headerForUsageTable)) {
			if (elementIsRecord(e)) {
				String ptn = stripHeaderFromRecord(e);
				String record = combineCharactersSurroundingSlash(stripRecordFromHeader(e));
				// System.out.println("THE HEADER IS: " + ptn + " AND THE RECORD IS " + record);

				List<String> recordValues = new LinkedList<String>(Arrays.asList(record.split("\\s+")));

				Map<String, String> headerToRecord = new HashMap<String, String>();
				for (int i = 0; i < recordValues.size(); i++) {
					// System.out.println("THE RECORD SIZE IS: "+recordValues.size()+" AND THE
					// HEADER SIZE IS: "+usageTableHeaders(page, yCoordForColumnHeaders).size());
					String msg = " USAGE TABLE MAY HAVE CHANGED POSITION ON PAGE OR HEADERS MAY BE DIFFERENT";
					if (recordValues.size() == usageTableHeaders(page, yCoordForColumnHeaders).size()) {
						String header = null;
						Logger logger = LogManager.getLogger();
						try {
							header = usageTableHeaders(page, yCoordForColumnHeaders).get(i);
						} catch (IndexOutOfBoundsException ex) {
							logger.info(msg);
						} catch (NullPointerException ex) {
							logger.info(msg);
						}
						String recordValue = null;
						try {
							recordValue = recordValues.get(i);
						} catch (IndexOutOfBoundsException ex) {
							logger.info(msg);
						} catch (NullPointerException ex) {
							logger.info(msg);
						}

						headerToRecord.put(header, recordValue);

						table.put(ptn, headerToRecord);
					}
				}
			}

		}

		printUsageMap(table);

		return table;
	}

	public String stripRecordFromHeader(String text) {
		String record = null;
		String randomPtn = "(123) 456-7891";
		int sizeOfPtn = randomPtn.length();

		if (textIsPlanTotals(text)) {
			int sizeOfPlanTotalsHeader = 0;
			if (text.startsWith("Plan Totals")) {
				sizeOfPlanTotalsHeader = "Plan Totals".length();
			} else if (text.startsWith("Total del Plan")) {
				sizeOfPlanTotalsHeader = "Total del Plan".length();
			}
			int startOfRecordAfterPlanTotals = sizeOfPlanTotalsHeader + 1;
			record = text.substring(startOfRecordAfterPlanTotals);
			return record;
		}

		if (testStringAgainstRegex(text.substring(0, sizeOfPtn), regexAcceptsOnlyPhoneNumber)) {

			int startOfRecord = sizeOfPtn + 1;
			record = text.substring(startOfRecord);
			return record;
		}
		return record;
	}

	public boolean textIsPlanTotals(String text) {
		boolean a = text.startsWith("Plan Totals") || text.startsWith("Total del Plan");
		return a;
	}

	public String stripHeaderFromRecord(String text) {
		String header = null;
		String randomPtn = "(123) 456-7891";
		int sizeOfPtn = randomPtn.length();
		if (testStringAgainstRegex(text.substring(0, sizeOfPtn), regexAcceptsOnlyPhoneNumber)) {
			header = text.substring(0, sizeOfPtn);
			return header;
		} else if (textIsPlanTotals(text)) {
			int sizeOfPlanTotalsHeader = 0;
			if (text.startsWith("Plan Totals")) {
				sizeOfPlanTotalsHeader = "Plan Totals".length();
			} else if (text.startsWith("Total del Plan")) {
				sizeOfPlanTotalsHeader = "Total del Plan".length();
			}
			header = text.substring(0, sizeOfPlanTotalsHeader);
			return header;
		}
		return header;
	}

	/*
	 * This routine gets all the content for Usage data (not Historical Usage) as a
	 * list of Strings.
	 */

	public List<String> usageTableData(PDPage page, String headerForUsageTable) throws IOException {
		List<String> listOfData = null;
		if (usageTableDoesNotHaveHeader(headerForUsageTable)) {
			listOfData = fullTableAsList(page);
			return listOfData;
		} else if (!usageTableDoesNotHaveHeader(headerForUsageTable)) {
			listOfData = usageContentBetweenHeader(page, headerForUsageTable);
			return listOfData;
		}
		return listOfData;
	}

	public boolean usageTableDoesNotHaveHeader(String headerForUsageTable) {
		return headerForUsageTable == (null) || headerForUsageTable.equals(null);
	}

	public List<String> historicalUsageGraph(PDPage page) throws IOException {
		String historicalUsageHeader = "Historical Usage - Presents after three or more consecutive months of plan usage is available.";
		return usageContentBetweenHeader(page, historicalUsageHeader);
	}

	public List<String> usageContentBetweenHeader(PDPage page, String startingHeader) throws IOException {
		return contentBetweenHeaders(fullTableAsList(page), startingHeader, isUsageHeader);
	}

	// the following routine can be used to grab the last line under a given header.
	// this is useful for grabbing alert messages at the bottom of header content
	public String lastTextContentUnderHeader(PDPage page, String startingHeader) throws IOException {
		List<String> allContentUnderHeader = usageContentBetweenHeader(page, startingHeader);
		int posOfLastLineUnderHeader = allContentUnderHeader.size() - 1;
		return allContentUnderHeader.get(posOfLastLineUnderHeader);
	}

	public List<String> fullTableAsList(PDPage page) throws IOException {
		String fullTableText = fullTableAsString(page);
		List<String> ptnRecords = makeRectStringIntoListOfStringsByLineBreak(fullTableText);
		return ptnRecords;
	}

	public String fullTableAsString(PDPage page) throws IOException {
		return PDFRegions.rectangleToTrimmedText(page, UsageRegions.FULL_TABLE.getRegion());
	}

	public boolean desiredHeaderIsPresentOnPDFPage(PDPage page, String desiredHeader) throws IOException {
		allUsageHeadersOnPage(page).stream().filter(x -> x.equals(desiredHeader)).forEach(printHeader);
		return testString(desiredHeader, allUsageHeadersOnPage(page));
	}

	public List<String> allUsageHeadersOnPage(PDPage page) throws IOException {
		return filterForHeaders(fullTableAsList(page), isUsageHeader);
	}

	public List<String> historicalUsageAsList(PDPage page) throws IOException {
		String historicalUsageText = historicalUsageAsString(page);
		List<String> list = makeRectStringIntoListOfStringsByLineBreak(historicalUsageText);
		return list;
	}

	public String historicalUsageAsString(PDPage page) throws IOException {
		resetHistoricalUsage();
		return PDFRegions.rectangleToTrimmedText(page, UsageRegions.HISTORICAL_USAGE.getRegion());
	}

	public static Predicate<String> isUsageHeader = x -> textIsUsageTypeHeader(x);

	public static boolean textIsUsageTypeHeader(String text) {
		return testStringIsUsageHeader(text, usageEquipmentHeaders());
	}

	public static boolean testStringIsUsageHeader(String text, List<String> list) {
		for (String e : list) {
			if (text.equals(e)) {
				return true;
			}
		}
		return false;
	}

	static List<String> usageEquipmentHeaders() {
		List<String> list = new ArrayList<>();
		list.add("Usage -Sprint Unlimited Plan - Unlimited Talk, Text & Data");
		list.add("Historical Usage - Presents after three or more consecutive months of plan usage is available.");
		list.add("Equipment Payment Schedule");
		list.add("Calendario de Pago de Equipo");
		list.add("Usage -Free & Clear Add A Phone");
		list.add("Usage -Sprint Better Choice - 3 GB Shared High Speed Data");
		list.add("Usage -Free & Clear Family Plan - 1200 Anytime Minutes Included");
		list.add("Usage -Unlimited Freedom - Unlimited Talk, Text & Data");
		list.add("Uso -Opción Móvil ATT - 15GB 50% dcto");
		return list;
	}

	public boolean elementIsRecord(String text) {
		String randomPtn = "(123) 456-7891";
		int ptnSize = randomPtn.length();
		if (textIsPlanTotals(text)) {
			return true;
		}
		if (text.length() > ptnSize) {
			return testStringAgainstRegex(text.substring(0, ptnSize), regexAcceptsOnlyPhoneNumber);
		}
		return false;
	}

	public void displayUsageDetails(String ptn, String usagePlan, String unit, String total) {
		printPTN.accept(ptn);
		printPlan.accept(usagePlan);
		printTotal.accept(total);
		printUnit.accept(unit);
	}

	public void resetUsageTable() {
		Rectangle rect = UsageRegions.FULL_TABLE.getRegion();
		System.out.println("THE USAGE TABLE COORD BEFORE THE RESET IS: " + rect);
		int xCoord = 0;
		int yCoord = 0;
		int width = 800;
		int height = 900;
		newRectLocation(xCoord, yCoord, rect);
		newRectSize(width, height, rect);
	}

	public void resetHistoricalUsage() {
		Rectangle rect = UsageRegions.HISTORICAL_USAGE.getRegion();
		System.out.println("THE USAGE TABLE COORD BEFORE THE RESET IS: " + rect);
		int xCoord = 0;
		int yCoord = 0;
		int width = 300;
		int height = 165;
		newRectLocation(xCoord, yCoord, rect);
		newRectSize(width, height, rect);
	}

	/*
	 * RECTANGLES OF REGIONS ON A USAGE PAGE OF THE PDF THESE RECTANGLES FUNCTION
	 * WITHIN OUR TESTS LIKE XPATH OR CSS SELECTORS- THEY GET US TEXT THAT WE PARSE
	 * AND THEN TEST AGAINST
	 */

	public enum UsageRegions {
		COLUMN_HEADER_CELL(new Rectangle(65, 75)), FULL_TABLE(new Rectangle(800, 900)), HISTORICAL_USAGE(
				new Rectangle(300, 165));

		private Rectangle region;

		private UsageRegions(Rectangle dimensions) {
			this.region = dimensions;
		}

		public Rectangle getRegion() {
			return region;
		}
	}
}