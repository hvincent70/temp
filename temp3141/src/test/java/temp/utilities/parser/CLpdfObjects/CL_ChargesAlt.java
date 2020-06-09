package com.sprint.iice_tests.utilities.parser.CLpdfObjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.sprint.iice_tests.utilities.parser.PDF_Searcher;

/**
 * This POJO is an alternative implementation of the CL_Charges PDF object. This
 * object, when we don't care where it appears on a page, can be easily modeled
 * as a Map<String, List<String>> object, so this class creates the needed
 * object based off of the downloaded PDF, which is passed directly from the
 * constructor. An additional parameter, totalAccountCharges, is accessible
 * after the constructor is instantiated, and this returns the final line
 * without using the main HashMap.
 * 
 * The advantage of doing this is that if the object moves from one page to
 * another, tests implementing this class will still pass as long as the strings
 * and keys are still correct, lowering maintenance time and decreasing the
 * likelihood of failing a test that should pass. While the initial construction is heavy on
 * execution time, once the Map is constructed there is no need for additional
 * method calls to this object, making it comparable in speed to existing PDF
 * objects. Because the object is completely modeled by a HashMap, anyone
 * familiar with that java object can make all the necessary checks they desire
 * without having to access methods unique to this java class.
 * 
 * A sample implementation is found in the
 * com.sprint.iice_tests.definitions.cl_paper.PaperValidateAccountLevelAdjustmentsAltSample
 * package location, and can be run successfully if a feature runner is added to
 * the project in that location. On 4/8/19, this implementation was compared to
 * the original setup; execution time was similar (1:06 this way, 1:12 original)
 * while this implementation required 54 MB to 45 MB of memory.
 * 
 * Assumptions used by this class:
 * 
 * 1. The start position of the map can be assumed to be found by the first
 * occurrence of "ACCOUNT CHARGES" when the PDF is checked by two columns
 * 
 * 2. The end position is assumed to be the first occurrence of a string that
 * starts with "TOTAL ACCOUNT CHARGES" occurring after the start position.
 * 
 * If either of these first two assumptions turn out to be incorrect, changes
 * can be made to the START_CONDION and/or END_CONDITION parameter.
 * 
 * 3. The desired key values are contained in the list created in the
 * getKeyValues() method. This method can have additional keys added to it as
 * necessary
 */

public class CL_ChargesAlt extends PDF_Searcher {

	private static final String START_CONDITION = "ACCOUNT CHARGES";
	private static final Predicate<String> END_CONDITION = s -> s.startsWith("TOTAL ACCOUNT CHARGES");
	public static final String MISC_CHARGES_AND_ADJUSTMENTS = "MISC.CHARGES & ADJUSTMENTS";
	public static final String EQUIPMENT = "EQUIPMENT";
	public static final String GOVERNMENT_TAXES_AND_FEES = "GOVERNMENT TAXES & FEES";
	private int startSearch = 3;
	public String totalAccountCharges;
	public Map<String, List<String>> map;

	private List<String> getKeyValues() {
		List<String> keyValues = new ArrayList<String>();
		keyValues.add(MISC_CHARGES_AND_ADJUSTMENTS);
		keyValues.add(EQUIPMENT);
		keyValues.add(GOVERNMENT_TAXES_AND_FEES);
		return keyValues;
	}

	private List<String> keyValues = getKeyValues();

	/**
	 * Constructor cannot be instantiated with the definition class, because the
	 * compiler will attempt to instantiate the definition class before the pdf
	 * download is complete.
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
	public CL_ChargesAlt(String pdfPath) throws PageNotFoundException, IOException {
		List<String> list = createList(startSearch, pdfPath, START_CONDITION, END_CONDITION, true);
		this.totalAccountCharges = list.get(list.size() - 1);
		list.remove(0);
		list.remove(list.size() - 1);
		this.map = createMapWithDesignatedKeyValues(keyValues, list);
	}
	
	public Map<String, List<String>> getMap(){
		return map;
	}
	
	public String getTotalAccountCharges() {
		return totalAccountCharges;
	}
}
