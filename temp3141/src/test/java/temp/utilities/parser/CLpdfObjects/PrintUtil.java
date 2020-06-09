package com.sprint.iice_tests.utilities.parser.CLpdfObjects;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.pdfbox.pdmodel.PDPage;

import com.sprint.iice_tests.web.browser.Browser;

public class PrintUtil {
	/*
	 * PRINTING UTILITIES
	 */

	public static Consumer<String> printKey = (x -> System.out.println("Key: " + x));
	public static Consumer<String> printValues = (x -> System.out.println("Values: " + x));
	public static Consumer<String> printOnNewLine = (x -> System.out.println("\n" + x));
	public static Consumer<String> printBegAndEndOfLine = x -> System.out.println("_" + x + "_");
	public static Consumer<String> printBeforeChange = x -> System.out.println("before change: " + x);
	public static Consumer<String> printAfterChange = x -> System.out.println("after change: " + x);
	Consumer<String> printHeader = (x -> System.out.println("\nHEADER: " + x));
	Consumer<String> printPTN = (x -> System.out.println("\nPTN: " + x));
	Consumer<String> printPlan = (x -> System.out.println("\nPLAN: " + x));
	Consumer<String> printUnit = (x -> System.out.println("\nUNIT: " + x));
	Consumer<String> printTotal = (x -> System.out.println("\nTOTAL: " + x));
	public static Consumer<String> printFileDownloaded = (x -> System.out.println("FILE "+x+" WAS DOWNLOADED"));
	public static Consumer<? super File> printTestName = (x -> System.out.println("\nTEST NAME: " + x.getName()));
	public static Consumer<? super File> printThreadID = x->System.out.println("Thread name for "+x+" "+Thread.currentThread().getId());
	public static Consumer<String> printBillRun = (billRun) -> System.out.println("Bill run for environment " + Browser.site+" is "+billRun);
	public Consumer<String> printEquipmentDetail = (x -> System.out.println("EQUIPMENT DETAIL: " + x));
	public static Consumer<String> printAccountPath = (x -> System.out.println("\nACCOUNT PATH TO JSON DATA: " + x));

	public static void printListItemsOnNewLine(List<String> list) {
		list.stream().forEach(x -> printOnNewLine.accept(x));
	}

	public static void printRectangle(PDPage page, Rectangle rect) throws IOException {
		printListItemsOnNewLine(CL_PageBase.makeRectStringIntoListOfStringsByLineBreak(CL_PageBase.rectangleToTrimmedText(page, rect)));
	}

	public static void printIndexOfEveryStringInList(List<String> list) {
		list.stream().forEachOrdered(x -> System.out.println("the index of '" + x + "' is " + list.indexOf(x)));
	}

	public static void printMap(Map<String, List<String>> map) {
		map.forEach((K, Y) -> {
			printKey.accept(K);
			Y.stream().forEachOrdered(x -> printValues.accept(x));
		});
	}
	
	public static void printMaps(Map<String, Map<String, List<String>>> map) {
		map.forEach((K, Y) -> {
			Y.forEach((W, X) -> {
				System.out.println("\n***FIRST KEY: " + K + "\n***SECOND KEY: " + W);
				printListItemsOnNewLine(X);
			});
		});
	}

	public static void printUsageMap(Map<String, Map<String, String>> map) {
		map.forEach((K, Y) -> {
			Y.forEach((W, X) -> {
				System.out.println("FOR PTN: " + K + " COLUMN: " + W);
				printValues.accept(X);
			});
		});
	}

	public static void printMapValueSingleString(Map<String, String> map) {
		map.forEach((K, Y) -> {
			printKey.accept(K);
			printValues.accept(Y);
		});
	}


	
	public static void printPDFPageNumbers() {
		String pdfPages = ("\nThis test asserts data on the following pdf pages: "
				+ Browser.getPdfPages());
		System.out.println(pdfPages);
	}
}
