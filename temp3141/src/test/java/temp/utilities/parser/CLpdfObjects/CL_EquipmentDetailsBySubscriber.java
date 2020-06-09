package com.sprint.iice_tests.utilities.parser.CLpdfObjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
 */

//TODO
/**
 * This method can be made substantially faster. The page numbers up top
 * indicate where a section ends, this could be grabbed and used to find the end
 * position, and instead of working from the front of the section, the search
 * for the start condition can move backwards. Given time and tester interest,
 * this will be accomplished in short order.
 * 
 */

/**
 * A sample implementation is found in the
 * com.sprint.iice_tests.definitions.cl_paper.PaperValidateInstallmentEquipmentSchedAltRunner
 * package location, and can be run successfully if a feature runner is added to
 * the project pointing to that location. On 4/9/19, this implementation was
 * compared to the original setup; execution time was substantially longer (1:27
 * this way, 1:02 original) while this implementation required 56 MB to 45 MB of
 * memory.
 * 
 * Assumptions used by this class:
 * 
 * 1. The start position of the map can be assumed to be found by the first
 * occurrence of "EQUIPMENT DETAILS BY SUBSCRIBER" when the PDF is checked by
 * two columns
 * 
 * 2. The end position is assumed to be the first occurrence of a string that
 * starts with "DAC Information" occurring after the start position.
 * 
 * If either of these first two assumptions turn out to be incorrect, changes
 * can be made to the START_CONDION and/or END_CONDITION parameter.
 * 
 * 3. The desired key values are the phone numbers found directly under ever
 * instance of "Equipment Payment Schedule" It is assumed that the phone number
 * will be found in the first position when the String is split by the comma
 * 
 * Several methods could be created using this method.  
 * 
 */

public class CL_EquipmentDetailsBySubscriber extends PDF_Searcher {

	private static final String START_CONDITION = "EQUIPMENT DETAILS BY SUBSCRIBER";
	private static final Predicate<String> END_CONDITION = s -> s.startsWith("DAC Information");
	private static final String END_LIST_CONDITION = "Equipment Payment Schedule";
	private int startSearch = 3;
	public String totalAccountCharges;
	public Map<String, List<String>> map;

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
	public CL_EquipmentDetailsBySubscriber(String pdfPath) throws PageNotFoundException, IOException {
		List<String> list = createList(startSearch, pdfPath, START_CONDITION, END_CONDITION, true);
		list.remove(0);
		this.map = createMapUsingEquipmentPaymentScheduleRules(list);
	}
	
	public Map<String, List<String>> getMap(){
		return map;
	}

	private Map<String, List<String>> createMapUsingEquipmentPaymentScheduleRules(List<String> list) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).equals(END_LIST_CONDITION)) {
				int k = 0;
				String key = list.get(i).split(",")[0];
				List<String> newList = new ArrayList<String>();
				while (i + k < list.size() && !list.get(i + k).equals(END_LIST_CONDITION)) {
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
