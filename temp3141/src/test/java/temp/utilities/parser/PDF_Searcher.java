package com.sprint.iice_tests.utilities.parser;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.sprint.iice_tests.utilities.parser.CLpdfObjects.CL_PageBase;
import com.sprint.iice_tests.utilities.parser.CLpdfObjects.PageNotFoundException;

public class PDF_Searcher extends CL_PageBase {
	private static Rectangle leftColCL = new Rectangle(5, 100, 400, 500);
	private static Rectangle rightColCL = new Rectangle(405, 90, 450, 500);
	private static Rectangle leftColIL = new Rectangle(5, 90, 375, 675);
	private static Rectangle rightColIL = new Rectangle(380, 90, 225, 675);
	private static Rectangle pageNumLoc = new Rectangle(576, 0, 180, 36);
	private static Rectangle wholePageCL = new Rectangle(0, 100, 800, 800);
	private static Rectangle wholePageIL = new Rectangle(10, 90, 600, 675);
	public static Rectangle[] CL_TwoCol = { leftColCL, rightColCL };
	public static Rectangle[] IL_TwoCol = { leftColIL, rightColIL };
	public static Rectangle[] CL_WholePage = { wholePageCL };
	public static Rectangle[] IL_WholePage = { wholePageIL };
	private Predicate<String> isPhoneNumber = s -> s.matches("^\\(\\d{3}.*");

	public List<String> createList(String searchMethod, String pdfPath, String startCondition, String endCondition,
			boolean splitPageOrNot) throws PageNotFoundException {
		try {
			PDDocument doc = docStep(pdfPath);
			switch (searchMethod) {
			case "BinaryBackwards":
				int startPage = findPageMatchingConditionBinaryBackWards(doc, startCondition, searchMethod,
						splitPageOrNot);
				Predicate<String> condition = s -> s.startsWith(endCondition);
				List<String> list = createListFromStartAndEndCondition(startPage, doc, startCondition, condition,
						splitPageOrNot);
				return list;
			}
			return null;
		} catch (IOException io) {
			return null;
		}
	}

	public List<String> createList(int startSearch, String pdfPath, String startCondition, String endCondition,
			boolean splitPageOrNot) throws PageNotFoundException {
		try {
			PDDocument doc = docStep(pdfPath);
			int startPage = findPageMatchingCondition(doc, startCondition, startSearch, splitPageOrNot);
			Predicate<String> condition = s -> s.startsWith(endCondition);
			List<String> list = createListFromStartAndEndCondition(startPage, doc, startCondition, condition,
					splitPageOrNot);
			return list;
		} catch (IOException io) {
			return null;
		}
	}

	public List<String> createList(int startSearch, String pdfPath, String startCondition, String endCondition,
			Rectangle[] columns) throws PageNotFoundException, IOException {
		PDDocument doc = docStep(pdfPath);
		int startPage = findPageMatchingCondition(doc, startCondition, startSearch, columns);
		Predicate<String> condition = s -> s.startsWith(endCondition);
		List<String> list = createListFromStartAndEndCondition(startPage, doc, startCondition, condition, columns);
		return list;
	}
	
	private List<String> createListFromStartAndEndCondition(int startPage, PDDocument doc, String startCondition,
			Predicate<String> condition, Rectangle[] columns) throws IOException {
		int[] pageColumnAndY = new int[3];
		int[] pageColumnAndYEnd = new int[3];
		PDPage page = desiredPDFPage(doc, startPage);
		pageColumnAndY = findStartPositionOnPage(startPage, page, startCondition, columns);
		pageColumnAndYEnd = findEndPostionOnPage(doc, pageColumnAndY, condition, columns);
		return listBoundedByStartAndEndPosition(doc, pageColumnAndY, pageColumnAndYEnd, columns);
	}

	private List<String> listBoundedByStartAndEndPosition(PDDocument doc, int[] pageColumnAndY, int[] pageColumnAndYEnd,
			Rectangle[] columns) throws IOException {
		List<String> list = new ArrayList<String>();
		PDPage page = desiredPDFPage(doc, pageColumnAndY[0]);
		int col = pageColumnAndY[1];
		int pageNum = pageColumnAndY[0];
		int endPage = pageColumnAndYEnd[0];
		int endCol = pageColumnAndYEnd[1];
		if (pageNum == pageColumnAndYEnd[0] && col == pageColumnAndYEnd[1]) {
			list.addAll(makeRectIntoListOfString(page, new Rectangle(columns[col].x, pageColumnAndY[2],
					columns[col].width, pageColumnAndYEnd[2] - pageColumnAndY[2] + 10)));
			return list;
		}
		list.addAll(makeRectIntoListOfString(page,
				new Rectangle(columns[col].x, pageColumnAndY[2], columns[col].width, columns[col].height)));
		if (col == 1) {
			col = 0;
			pageNum++;
			page = desiredPDFPage(doc, pageNum);
		} else {
			col++;
		}
		while (!(pageNum == endPage && col == endCol)) {
			list.addAll(makeRectIntoListOfString(page,
					new Rectangle(columns[col].x, columns[col].y, columns[col].width, columns[col].height)));
			if (col == 1) {
				col = 0;
				pageNum++;
				page = desiredPDFPage(doc, pageNum);
			} else {
				col++;
			}
		}
		list.addAll(makeRectIntoListOfString(page,
				new Rectangle(columns[col].x, columns[col].y, columns[col].width, pageColumnAndYEnd[2] - 80)));
		list.removeIf(s -> s.length() == 0);
		return list;
	}

	private int[] findEndPostionOnPage(PDDocument doc, int[] pageColumnAndY, Predicate<String> condition,
			Rectangle[] columns) throws IOException {
		PDPage page = desiredPDFPage(doc, pageColumnAndY[0]);
		int[] ans = new int[3];
		int pageNum = pageColumnAndY[0];
		int col = pageColumnAndY[1];
		for (int i = col; i < columns.length; i++) {
			for (int y_pos = pageColumnAndY[2]; y_pos < columns[i].y; y_pos += 10) {
				String s = converRectToText(page, new Rectangle(columns[i].x, y_pos, columns[i].width, 10));
				if (condition.test(s)) {
					ans[0] = pageColumnAndY[0];
					ans[1] = i;
					ans[2] = y_pos;
					return ans;
				}
			}
		}
		if (col == 1) {
			col = 0;
			pageNum++;
			page = desiredPDFPage(doc, pageNum);
		} else {
			col++;
		}
		while (true) {
			page = desiredPDFPage(doc, pageNum);
			while (col < columns.length) {
				for (int y_pos = columns[col].y; y_pos < columns[col].height; y_pos += 10) {
					String s = converRectToText(page, new Rectangle(columns[col].x, y_pos, columns[col].width, 10));
					if (condition.test(s)) {
						ans[0] = pageNum;
						ans[1] = col;
						ans[2] = y_pos;
						return ans;
					}
				}
				col++;
			}
			pageNum++;
			col = 0;
		}
	}

	private int[] findStartPositionOnPage(int startPage, PDPage page, String startCondition, Rectangle[] columns)
			throws IOException {
		int[] ans = new int[3];
		for (int i = 0; i < columns.length; i++) {
			for (int y_pos = columns[i].y; y_pos < columns[i].height + 10; y_pos += 10) {
				String s = converRectToText(page, new Rectangle(columns[i].x, y_pos, columns[i].width, 10));
				if (s.startsWith(startCondition)) {
					ans[0] = startPage;
					ans[1] = i;
					ans[2] = y_pos;
					return ans;
				}
			}
		}
		return null;
	}

	public int findPageMatchingCondition(PDDocument doc, String startCondition, int startSearch, Rectangle[] columns)
			throws PageNotFoundException {
		int endPage = doc.getNumberOfPages();
		for (int i = startSearch; i < endPage; i++) {
			PDPage page = desiredPDFPage(doc, i);
			Set<String> set = populateSet(page, columns);
			if (!set.add(startCondition)) {
				return i;
			}
		}
		throw new PageNotFoundException(startCondition);
	}

	private Set<String> populateSet(PDPage page, Rectangle[] columns) {
		try {
			Set<String> set = new HashSet<String>();
			for (int i = 0; i < columns.length; i++) {
				List<String> list = makeRectIntoListOfString(page, columns[i]);
				set.addAll(list);
			}
			return set;
		} catch (IOException io) {
			io.getMessage();
			return null;
		}
	}

	public List<String> createList(int startSearch, String pdfPath, String startCondition,
			Predicate<String> endCondition, boolean splitPageOrNot) throws PageNotFoundException {
		try {
			PDDocument doc = docStep(pdfPath);
			int startPage = findPageMatchingCondition(doc, startCondition, startSearch, splitPageOrNot);
			List<String> list = createListFromStartAndEndCondition(startPage, doc, startCondition, endCondition,
					splitPageOrNot);
			return list;
		} catch (IOException io) {
			return null;
		}
	}

	private int findPageMatchingConditionBinaryBackWards(PDDocument doc, String startCondition, String searchMethod,
			boolean splitPageOrNot) throws IOException, PageNotFoundException {
		int size = doc.getNumberOfPages() - 1;
		PDPage page = doc.getPage(size);
		String xofY = converRectToText(page, pageNumLoc);
		String number = xofY.replaceAll("(.*of\\W)(.*)", "$2").replaceAll("(\\D)", "");
		System.out.println(number);
		int range = Integer.parseInt(number);
		return binarySearch(doc, startCondition, size - range, size, 0);
	}

	private int binarySearch(PDDocument doc, String startCondition, int startPos, int endPos, int counter)
			throws IOException, PageNotFoundException {
		if (startPos == endPos) {
			counter++;
			if (counter == 2)
				throw new PageNotFoundException(startCondition);
		}
		int pageNum = startPos == endPos ? startPos++ : (startPos + endPos) / 2;
		long checkAgainst = getNumberFromString(startCondition);
		System.out.println("PAGE NUMBER: " + pageNum);
		PDPage page = doc.getPage(pageNum);
		while (counter < 2) {
			List<String> list = new ArrayList<String>();
			list.addAll(makeRectIntoListOfString(page, leftColCL));
			list.addAll(makeRectIntoListOfString(page, rightColCL));
			String checkThis = list.stream().filter(isPhoneNumber).findFirst().orElse(" ");
			System.out.println(checkThis);
			long checkNum = getNumberFromString(checkThis);
			if (checkNum == 0) {
				pageNum++;
				page = doc.getPage(pageNum);
			} else if (checkAgainst == checkNum) {
				return pageNum;
			} else if (checkAgainst > checkNum) {
				return binarySearch(doc, startCondition, pageNum, endPos, counter);
			} else {
				return binarySearch(doc, startCondition, startPos, pageNum, counter);
			}
		}
		throw new PageNotFoundException(startCondition);
	}

	private long getNumberFromString(String checkThis) {
		try {
			return Long.parseLong(checkThis.replaceAll("(\\D)", ""));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public int findPageMatchingCondition(PDDocument doc, String condition, int startPage, boolean splitPages)
			throws PageNotFoundException {
		int endPage = doc.getNumberOfPages();
		for (int i = startPage; i < endPage; i++) {
			PDPage page = desiredPDFPage(doc, i);
			Set<String> set = populateSet(page, splitPages);
			if (!set.add(condition)) {
				return i;
			}
		}
		throw new PageNotFoundException(condition);
	}

	private Set<String> populateSet(PDPage page, boolean splitPages) {
		try {
			Set<String> set = new HashSet<String>();
			if (splitPages) {
				List<String> left = makeRectIntoListOfString(page, leftColCL);
				List<String> right = makeRectIntoListOfString(page, rightColCL);
				set.addAll(left);
				set.addAll(right);
			} else {
				List<String> all = makeRectIntoListOfString(page, wholePageCL);
				set.addAll(all);
			}
			return set;
		} catch (IOException io) {
			io.getMessage();
			return null;
		}
	}

	public List<String> createListFromStartAndEndCondition(int startPage, PDDocument doc, String startCondition,
			Predicate<String> endCondition, boolean splitPages) throws IOException {
		int[] pageColumnAndY = new int[3];
		int[] pageColumnAndYEnd = new int[3];
		if (splitPages) {
			PDPage page = desiredPDFPage(doc, startPage);
			pageColumnAndY = findStartPositionOnPageTwoCols(startPage, page, startCondition);
			pageColumnAndYEnd = findEndPostionOnPageTwoCols(doc, pageColumnAndY, endCondition);
			return listBoundedByStartAndEndPosition(doc, pageColumnAndY, pageColumnAndYEnd);
		} else {
			PDPage page = desiredPDFPage(doc, startPage);
			pageColumnAndY = findStartPositionFullPage(startPage, page, startCondition);
			pageColumnAndYEnd = findEndPostionFullPage(doc, pageColumnAndY, endCondition);
			return listBoundedByStartAndEndPositionFullPage(doc, pageColumnAndY, pageColumnAndYEnd);
		}
	}

	private List<String> listBoundedByStartAndEndPositionFullPage(PDDocument doc, int[] pageColumnAndY,
			int[] pageColumnAndYEnd) throws IOException {
		List<String> list = new ArrayList<String>();
		PDPage page = desiredPDFPage(doc, pageColumnAndY[0]);
		int col = 0;
		int pageNum = pageColumnAndY[0];
		int endPage = pageColumnAndYEnd[0];
		int endCol = 0;
		if (pageNum == pageColumnAndYEnd[0]) {
			list.addAll(makeRectIntoListOfString(page,
					new Rectangle(0, pageColumnAndY[2], 800, pageColumnAndYEnd[2] - pageColumnAndY[2] + 10)));
			return list;
		}
		pageNum++;
		page = desiredPDFPage(doc, pageNum);
		while (!(pageNum == endPage && col == endCol)) {
			list.addAll(makeRectIntoListOfString(page, new Rectangle(0, 90, 800, 710)));
			pageNum++;
			page = desiredPDFPage(doc, pageNum);
		}
		list.addAll(makeRectIntoListOfString(page, new Rectangle(0, 90, 800, pageColumnAndYEnd[2] - 80)));
		list.removeIf(s -> s.length() == 0);
		return list;
	}

	private int[] findEndPostionFullPage(PDDocument doc, int[] pageColumnAndY, Predicate<String> endCondition)
			throws IOException {
		PDPage page = desiredPDFPage(doc, pageColumnAndY[0]);
		int[] ans = new int[3];
		int pageNum = pageColumnAndY[0];
		for (int y_pos = pageColumnAndY[2]; y_pos < 500; y_pos += 10) {
			String s = converRectToText(page, new Rectangle(0, y_pos, 800, 10));
			if (endCondition.test(s)) {
				ans[0] = pageColumnAndY[0];
				ans[1] = 0;
				ans[2] = y_pos;
				return ans;
			}
		}
		pageNum++;
		while (true) {
			page = desiredPDFPage(doc, pageNum);
			for (int y_pos = 90; y_pos < 500; y_pos += 10) {
				String s = converRectToText(page, new Rectangle(0, y_pos, 800, 10));
				if (endCondition.test(s)) {
					ans[0] = pageNum;
					ans[1] = 0;
					ans[2] = y_pos;
					return ans;
				}
			}
			pageNum++;
		}

	}

	private int[] findStartPositionFullPage(int startPage, PDPage page, String startCondition) throws IOException {
		int[] ans = new int[3];
		for (int y_pos = 90; y_pos < 505; y_pos += 10) {
			String s = converRectToText(page, new Rectangle(0, y_pos, 800, 10));
			if (s.startsWith(startCondition)) {
				ans[0] = startPage;
				ans[1] = 0;
				ans[2] = y_pos;
				return ans;
			}
		}
		return null;
	}

	private List<String> listBoundedByStartAndEndPosition(PDDocument doc, int[] pageColumnAndY, int[] pageColumnAndYEnd)
			throws IOException {
		List<String> list = new ArrayList<String>();
		PDPage page = desiredPDFPage(doc, pageColumnAndY[0]);
		int col = pageColumnAndY[1];
		int pageNum = pageColumnAndY[0];
		int endPage = pageColumnAndYEnd[0];
		int endCol = pageColumnAndYEnd[1];
		if (pageNum == pageColumnAndYEnd[0] && col == pageColumnAndYEnd[1]) {
			list.addAll(makeRectIntoListOfString(page, new Rectangle(5 + 400 * col, pageColumnAndY[2], 400,
					pageColumnAndYEnd[2] - pageColumnAndY[2] + 10)));
			return list;
		}
		list.addAll(makeRectIntoListOfString(page, new Rectangle(5 + 400 * col, pageColumnAndY[2], 400, 500)));
		if (col == 1) {
			col = 0;
			pageNum++;
			page = desiredPDFPage(doc, pageNum);
		} else {
			col++;
		}
		while (!(pageNum == endPage && col == endCol)) {
			list.addAll(makeRectIntoListOfString(page, new Rectangle(5 + 400 * col, 90, 400, 500)));
			if (col == 1) {
				col = 0;
				pageNum++;
				page = desiredPDFPage(doc, pageNum);
			} else {
				col++;
			}
		}
		list.addAll(makeRectIntoListOfString(page, new Rectangle(5 + 400 * col, 90, 400, pageColumnAndYEnd[2] - 80)));
		list.removeIf(s -> s.length() == 0);
		return list;
	}

	private int[] findEndPostionOnPageTwoCols(PDDocument doc, int[] pageColumnAndY, Predicate<String> endCondition)
			throws IOException {
		PDPage page = desiredPDFPage(doc, pageColumnAndY[0]);
		int[] ans = new int[3];
		int pageNum = pageColumnAndY[0];
		int col = pageColumnAndY[1];
		for (int i = col; i < 2; i++) {
			for (int y_pos = pageColumnAndY[2]; y_pos < 500; y_pos += 10) {
				String s = converRectToText(page, new Rectangle(5 + 400 * col, y_pos, 400, 10));
				if (endCondition.test(s)) {
					ans[0] = pageColumnAndY[0];
					ans[1] = i;
					ans[2] = y_pos;
					return ans;
				}
			}
		}
		if (col == 1) {
			col = 0;
			pageNum++;
			page = desiredPDFPage(doc, pageNum);
		} else {
			col++;
		}
		while (true) {
			page = desiredPDFPage(doc, pageNum);
			while (col < 2) {
				for (int y_pos = 90; y_pos < 500; y_pos += 10) {
					String s = converRectToText(page, new Rectangle(5 + 400 * col, y_pos, 400, 10));
					if (endCondition.test(s)) {
						ans[0] = pageNum;
						ans[1] = col;
						ans[2] = y_pos;
						return ans;
					}
				}
				col++;
			}
			pageNum++;
			col = 0;
		}
	}

	private int[] findStartPositionOnPageTwoCols(int startPage, PDPage page, String startCondition) throws IOException {
		int[] ans = new int[3];
		Rectangle[] col = CL_TwoCol;
		for (int i = 0; i < 2; i++) {
			for (int y_pos = col[i].y; y_pos < col[i].height + 10; y_pos += 10) {
				String s = converRectToText(page, new Rectangle(col[i].x, y_pos, col[i].width, 10));
				if (s.startsWith(startCondition)) {
					ans[0] = startPage;
					ans[1] = i;
					ans[2] = y_pos;
					return ans;
				}
			}
		}
		return null;
	}

	public Map<String, List<String>> createMapWithDesignatedKeyValues(List<String> keyValues, List<String> list) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < list.size(); i++) {
			if (keyValues.indexOf(list.get(i)) != -1) {
				int k = 1;
				List<String> newList = new ArrayList<String>();
				while (i + k < list.size() && keyValues.indexOf(list.get(i + k)) == -1) {
					newList.add(list.get(i + k));
					k++;
				}
				map.put(list.get(i), newList);
				i += k - 1;
			}
		}
		return map;
	}

}
