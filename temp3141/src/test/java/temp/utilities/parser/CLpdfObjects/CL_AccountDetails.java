package com.sprint.iice_tests.utilities.parser.CLpdfObjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.sprint.iice_tests.utilities.parser.PDF_Searcher;

/**
 * This POJO is an alternative implementation of the CL_Charges PDF object. This
 * object, when we don't care where it appears on a page, can be easily modeled
 * as a Map<String, List<String>> object, so this class creates the needed
 * object based off of the downloaded PDF, which is passed directly from the
 * constructor.
 * 
 * The advantage of doing this is that if the object moves from one page to
 * another, tests implementing this class will still pass as long as the strings
 * and keys are still correct, lowering maintenance time and decreasing the
 * likelihood of failing a test that should pass. While the initial construction
 * is heavy on execution time, once the Map is constructed there is no need for
 * additional method calls to this object, making it comparable in speed to
 * existing PDF objects. Because the object is completely modeled by a HashMap,
 * anyone familiar with that java object can make all the necessary checks they
 * desire without having to access methods unique to this java class.
 * 
 * A sample implementation is found in the
 * com.sprint.iice_tests.definitions.cl_paper.PaperValidateAccountLevelNonRetailEquipmentPurchaseAltRunner
 * package location, and can be run successfully if a feature runner is added to
 * the project pointing to that location. On 4/9/19, this implementation was
 * compared to the original setup; execution time was almost identical (0:58
 * this way, 0:55 original) while this implementation required 57 MB to 45 MB of
 * memory.
 * 
 * Assumptions used by this class:
 * 
 * 1. The start position of the map can be assumed to be found by the first
 * occurrence of "ACCOUNT DETAILS" when the PDF is checked by two columns
 * 
 * 2. The end position is assumed to be the first occurrence of a string that
 * starts with "EQUIPMENT DETAILS BY SUBSCRIBER" occurring after the start
 * position.
 * 
 * If either of these first two assumptions turn out to be incorrect, changes
 * can be made to the START_CONDION and/or END_CONDITION parameter.
 * 
 * 3. The desired key values are the Order numbers found with each equipment
 * purchase table When the constructor iterates through its list of values, it
 * will see "Order Number" and know to begin a new list. It grabs the key
 * between "Order Number" and the date, using the method seen on line 157.
 * 
 */

public class CL_AccountDetails extends PDF_Searcher {

	private static final String START_CONDITION = "ACCOUNT DETAILS";
	private static final Predicate<String> END_CONDITION = s -> s.startsWith("EQUIPMENT DETAILS BY SUBSCRIBER");
	private static final String END_LIST_CONDITION = "Order Number";
	private static final String SHARED_POOLED_SERVICE = "Shared/Pooled Service Usage Report";
	private static final String DATA_SHARED_SERVICE = "Data Shared Services Usage Report";
	private int startSearch = 3;
	public Map<String, List<String>> map;
	public List<String> sharedServiceReport;
	public List<String> dataSharedServiceReport;

	List<String> keyValues = Arrays.asList("Shared/Pooled Service Usage Report", "Data Shared Services Usage Report");

	/**
	 * Constructor cannot be instantiated at the top of the definition class,
	 * because the compiler will attempt to instantiate the definition class before
	 * the pdf download is complete.
	 * 
	 * Instead, declare an empty HashMap during the definition class instantiation,
	 * i.e:
	 * 
	 * Map<String, List<String>> chargesMap = new HashMap<String, List<String>>();
	 * 
	 * Then instantiate the constructor inside a method declaration, and set charges
	 * map equal to the CL_ChargesAlt.map, and it will be available to all following
	 * methods without starting another instance of the POJO. *
	 * 
	 * @param pdfPath:
	 *            in normal implementations, this will be accomplished by
	 *            Browser.pdfMap.get(threadId).toString()
	 * @throws PageNotFoundException:
	 *             Custom Exception, means that the executor never found and
	 *             instance of the START_CONDITION
	 * @throws IOException
	 */
	public CL_AccountDetails(String pdfPath) throws PageNotFoundException, IOException {
		List<String> list = createList(startSearch, pdfPath, START_CONDITION, END_CONDITION, true);
		list.remove(0);
		this.sharedServiceReport = getSharedServiceReport(list);
		list.removeAll(sharedServiceReport);
		this.dataSharedServiceReport = getDataSharedServiceReport(list);
		list.removeAll(dataSharedServiceReport);
		this.map = createMapUsingAccountDetailsRules(list);
	}

	public Map<String, List<String>> getMap() {
		return map;
	}

	public List<String> getSharedServiceReport() {
		List<String> list = new ArrayList<String>();
		for (Iterator<String> iterator = sharedServiceReport.iterator(); iterator.hasNext();) {
			list.add(iterator.next());
		}
		return list;
	}

	public List<String> getDataSharedServiceReport() {
		List<String> list = new ArrayList<String>();
		for (Iterator<String> iterator = dataSharedServiceReport.iterator(); iterator.hasNext();) {
			list.add(iterator.next());
		}
		return list;
	}

	private List<String> getDataSharedServiceReport(List<String> list) {
		int startIndex = list.indexOf(DATA_SHARED_SERVICE);
		if (startIndex == -1) {
			return null;
		} else {
			List<String> subList = list.subList(startIndex, list.size());
			// list.subList(startIndex, list.size()).clear();
			return subList;
		}
	}

	private List<String> getSharedServiceReport(List<String> list) {
		int startIndex = list.indexOf(SHARED_POOLED_SERVICE);
		int endIndex = list.indexOf(DATA_SHARED_SERVICE);
		if (startIndex == -1) {
			return null;
		} else if (endIndex == -2) {
			List<String> subList = list.subList(startIndex, list.size());
			// list.subList(startIndex, list.size()).clear();
			return subList;
		} else {
			List<String> subList = list.subList(startIndex, endIndex);
			// list.subList(startIndex, endIndex).clear();
			return subList;
		}
	}

	private Map<String, List<String>> createMapUsingAccountDetailsRules(List<String> list) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i);
			if (s.startsWith(END_LIST_CONDITION)) {
				int k = 1;
				String key = s.replaceAll("(Order Number: )(.*)", "$2").split(",")[0];
				List<String> newList = new ArrayList<String>();
				newList.add(s);
				while (i + k < list.size() && !list.get(i + k).startsWith(END_LIST_CONDITION)) {
					newList.add(list.get(i + k));
					k++;
				}
				map.put(key, newList);
				i += k - 1;
			}
		}
		return map;
	}
}
