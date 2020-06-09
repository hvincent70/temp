package com.sprint.iice_tests.utilities.parser;

import java.io.IOException;
import java.util.List;

import java.awt.Rectangle;
import com.sprint.iice_tests.utilities.parser.CLpdfObjects.PageNotFoundException;

/**
 * This is a worst case scenario page object. Through system updates or other
 * factors, you simply cannot find a page object that grabs the data you want to
 * check for, you can't figure out how to update the POJOs, and you're in a time
 * crunch.
 * 
 * You can always grab a list of strings, and you can always set the boundaries
 * of that list. If you have a list of strings, you can check if the data
 * matches what you expect it to, whether it shows up in the right order, etc.
 * 
 * Choose a start string that is unique enough that the first instance the
 * executor finds it, its in the right place. Likewise, choose a string that
 * ends the area of the PDF you want to inspect. If the data you care about is
 * on a page divided into two columns, pass true to the constructor, otherwise
 * pass false.
 * 
 * This class, by default, will begin its search on page three. If the data you
 * care about is several hundred pages into the document, an alternative
 * constructor is provided where you can pass a start page to the constructor
 */

public class PDF_FailSafe extends PDF_Searcher {

	private int startSearch = 1;
	public String totalAccountCharges;
	public List<String> failSafeList;

	/**
	 * Constructor cannot be instantiated with the definition class, because the
	 * compiler will attempt to instantiate the definition class before the pdf
	 * download is complete.
	 * 
	 * Instead, declare an empty List during the definition class instantiation,
	 * i.e:
	 * 
	 * List<String> list;
	 * 
	 * Then instantiate the constructor inside a method declaration, and set charges
	 * map equal to the CL_ChargesAlt.map, and it will be available to all following
	 * methods without starting another instance of the POJO.
	 * 
	 * @param pdfPath:
	 *            in normal implementations, this will be accomplished by
	 *            Browser.pdfMap.get(threadId).toString()
	 * @param startCondition
	 *            - the string that starts the desired list
	 * @param endCondition:
	 *            the string that ends the desire list. Constructor uses a
	 *            startsWith method to use this string, so only use the text that
	 *            won't change; for example, if the end of the list is "TOTAL COST
	 *            $3.14", perhaps "TOTAL COST" is explicit enough to get the list
	 *            you want.
	 * @param splitPageOrNot:
	 *            If the data you want is on a page that has two columns, put true.
	 *            Otherwise, put false.
	 * @throws PageNotFoundException
	 * @throws IOException
	 */
	public PDF_FailSafe(String pdfPath, String startCondition, String endCondition, boolean splitPageOrNot)
			throws PageNotFoundException, IOException {
		List<String> list = createList(startSearch, pdfPath, startCondition, endCondition, splitPageOrNot);
		this.failSafeList = list;
	}
	
	
	public PDF_FailSafe(String pdfPath, String startCondition, String endCondition, Rectangle[] columns)
			throws PageNotFoundException, IOException {
		List<String> list = createList(startSearch, pdfPath, startCondition, endCondition, columns);
		this.failSafeList = list;
	}




	/**
	 * Same method, but now you set the start page.
	 * 
	 * @param pdfPath:
	 *            in normal implementations, this will be accomplished by
	 *            Browser.pdfMap.get(threadId).toString()
	 * @param startCondition
	 *            - the string that starts the desired list
	 * @param endCondition:
	 *            the string that ends the desire list. Constructor uses a
	 *            startsWith method to use this string, so only use the text that
	 *            won't change; for example, if the end of the list is "TOTAL COST
	 *            $3.14", perhaps "TOTAL COST" is explicit enough to get the list
	 *            you want.
	 * @param splitPageOrNot:
	 *            If the data you want is on a page that has two columns, put true.
	 *            Otherwise, put false.
	 * @throws PageNotFoundException
	 * @throws IOException
	 */
	public PDF_FailSafe(String pdfPath, String startCondition, String endCondition, boolean splitPageOrNot, int startPage)
			throws PageNotFoundException, IOException {
		List<String> list = createList(startPage, pdfPath, startCondition, endCondition, splitPageOrNot);
		this.failSafeList = list;
	}
	
	/**
	 * Same method, but now you set the start page.
	 * 
	 * @param pdfPath:
	 *            in normal implementations, this will be accomplished by
	 *            Browser.pdfMap.get(threadId).toString()
	 * @param startCondition
	 *            - the string that starts the desired list
	 * @param endCondition:
	 *            the string that ends the desire list. Constructor uses a
	 *            startsWith method to use this string, so only use the text that
	 *            won't change; for example, if the end of the list is "TOTAL COST
	 *            $3.14", perhaps "TOTAL COST" is explicit enough to get the list
	 *            you want.
	 * @param splitPageOrNot:
	 *            If the data you want is on a page that has two columns, put true.
	 *            Otherwise, put false.
	 * @throws PageNotFoundException
	 * @throws IOException
	 */
	public PDF_FailSafe(String pdfPath, String startCondition, String endCondition, boolean splitPageOrNot, String searchMethod)
			throws PageNotFoundException, IOException {
		List<String> list = createList(searchMethod, pdfPath, startCondition, endCondition, splitPageOrNot);
		this.failSafeList = list;
	}
	
	public List<String> getList(){
		return failSafeList;
	}
}
