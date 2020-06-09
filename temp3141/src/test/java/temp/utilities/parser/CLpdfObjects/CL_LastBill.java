package com.sprint.iice_tests.utilities.parser.CLpdfObjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.sprint.iice_tests.utilities.parser.PDF_Searcher;

/**
 * This POJO is an alternative implementation of the CL_Charges PDF object. This
 * object, when we don't care where it appears on a page, can be easily modeled
 * as a handful of objects, so this class creates the needed
 * objects based off of the downloaded PDF, which is passed directly from the
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
 * Modeling:
 * Last bill is broken into three distinct components: a list of Previous Total Due,
 * A map of Adjustments to Previous Balance, and a String of Balance Forward
 * 
 * Assumptions used by this class:
 * 
 * 1. The start position of the map can be assumed to be found by the first
 * occurrence of "LAST BILL" when the PDF is checked by two columns
 * 
 * 2. The end position is assumed to be the first occurrence of a string that
 * starts with "BALANCE FORWARD" occurring after the start
 * position.
 * 
 * If either of these first two assumptions turn out to be incorrect, changes
 * can be made to the START_CONDION and/or END_CONDITION parameter.
 * 
 * 3. The desired key values are contained in the list created in the
 * getKeyValues() method. This method can have additional keys added to it as
 * necessary
 * 
 */

public class CL_LastBill extends PDF_Searcher {

	private static final String START_CONDITION = "LAST BILL";
	private static final Predicate<String> END_CONDITION = s -> s.startsWith("BALANCE FORWARD");
	private static final String ADJUSTMENTS_TO_PREVIOUS_BALANCE = "Adjustments to Previous Balance";
	private int startSearch = 3;
	private List<String> keyValues = Arrays.asList("ACCOUNT LEVEL ADJUSTMENTS", "SUBSCRIBER ADJUSTMENTS BY DAC");
	public String balanceForward;
	public List<String> previousTotalDueList;
	public Map<String, List<String>> adjustmentsMap;

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
	public CL_LastBill(String pdfPath) throws PageNotFoundException, IOException {
		List<String> list = createList(startSearch, pdfPath, START_CONDITION, END_CONDITION, true);
		list.remove(0);
		this.balanceForward = list.get(list.size() - 1);
		this.previousTotalDueList = createPreviousTotalDue(list);
		list.removeAll(previousTotalDueList);
		this.adjustmentsMap = createMapWithDesignatedKeyValues(keyValues, list);
	}

	private List<String> createPreviousTotalDue(List<String> list) {
		List<String> newList = new ArrayList<String>();
		int i = 0;
		while(!list.get(i).startsWith(ADJUSTMENTS_TO_PREVIOUS_BALANCE)) {
			newList.add(list.get(i));
			i++;
		}
		return newList;
	}
	
	public String getBalanceForward() {
		return balanceForward;
	}

	public List<String> getPreviousTotalDueList(){
		return previousTotalDueList;
	}
	
	public Map<String, List<String>> getAdjustmentsMap(){
		return adjustmentsMap;
	}
}
