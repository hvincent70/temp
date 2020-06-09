package com.sprint.iice_tests.utilities.test_util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.sprint.iice_tests.lib.dao.vo.Account;
import com.sprint.iice_tests.utilities.parser.ILpdfObjects.PDFRegions;
import com.sprint.iice_tests.web.browser.Browser;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CucumberHooks {
	long threadId = Thread.currentThread().getId();
	Account account = Browser.accountMap.get(threadId);

	String className = Browser.classMap.get(threadId);
	String lookfor = className.replaceAll(".json", "").replaceAll(" ", "").replaceAll("Runner", "");

	File screenshot_dir = new File(System.getProperty("user.dir") + "/src/test/resources/TestData/screenshot");
	File test_data = new File(System.getProperty("user.dir") + "/src/test/resources/TestData/");
	File pdf_data = new File(System.getProperty("user.dir") + "/src/test/resources/TestData/pdf/");
	Predicate<PDDocument> docIDEqualsThread = x -> x.getDocumentId() == threadId;

	boolean paperTest = className.startsWith("PaperValidate");

	@Before
	public void embedInfo(Scenario scenario) {
		StringBuilder sb = new StringBuilder("BAN: " + account.getBan());
		if (paperTest) {
			String s = "";
			s = Browser.runningOnUAT1() || Browser.isILPaperRun() ? account.getBillRun() : account.getBillRun2();
			sb.append(" Bill Run: " + s);
		}
		scenario.write(sb.toString());
	}

	@After
	public void embedScreenShots(Scenario scenario) throws IOException, InterruptedException {
		boolean failed = scenario.getStatus().toString().equals("FAILED");
		if (!paperTest) {
			boolean softFail = false;
			File[] files = screenshot_dir.listFiles();
			try {
				for (File file : files) {
					if (file.getName().startsWith(lookfor)) {
						scenario.embed(Files.readAllBytes(file.toPath()), "image/png");
						softFail = true;
					}
				}
			} catch (Exception e) {
			}
			if (!softFail && failed) {
				Screenshot.takeFullscreenShot(DataFileFinder.getScreenshotPath(), lookfor + "FinalScreenshot.png");
				files = screenshot_dir.listFiles(finalScreenshot);
				scenario.embed(Files.readAllBytes(files[0].toPath()), "image/png");
				files[0].delete();
			}
			File[] csvs = test_data.listFiles(csvFilter);
			if (csvs.length > 0) {
				for (File csv : csvs) {
					if (failed || softFail)
						scenario.embed(Files.readAllBytes(csv.toPath()), "text/csv");
					csv.delete();
				}
			}
		}
		if (paperTest) {
			File pdf_Name = Browser.pdfMap.get(threadId);
			PDDocument testDoc = selectInstanceForThread(PDFRegions.pdInstances);
			testDoc.close();
			testDoc.getDocument().close();
			try {
				if (failed) {
					List<Integer> decToCorrect = new ArrayList<Integer>(
							Browser.mapOfPdfPages.get(threadId).values()).stream().map(x -> x - 1)
									.collect(Collectors.toList());
					List<Integer> pages = alterList(decToCorrect, testDoc.getNumberOfPages());
					File parsedPDF = createParsedDoc(pdf_Name, pages);
					scenario.embed(Files.readAllBytes(parsedPDF.toPath()), "application/pdf");
					parsedPDF.delete();
				}
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}
			try {
				pdf_Name.delete();
			} catch (Exception e) {
			}
			;
		}
	}

	private PDDocument selectInstanceForThread(List<PDDocument> pdInstances) {
		return pdInstances.stream().filter(docIDEqualsThread).findFirst().get();
	}

	private File createParsedDoc(File testDoc, List<Integer> pages) throws IOException, InterruptedException {
		PDDocument testPDD = PDDocument.load(testDoc);
		PDDocument doc = new PDDocument();
		pages.forEach(x -> doc.addPage(testPDD.getPage(x)));
		File pdf = new File(
				Browser.pdfMap.get(threadId).toString().replaceAll("Runner", "TESTPDF"));
		doc.save(pdf);
		doc.close();
		return pdf;
	}

	private List<Integer> alterList(List<Integer> pages, int size) {
		Set<Integer> set = new HashSet<Integer>(pages);
		addPlusMinus(set);
		set.remove(-1);
		set.remove(size);
		return new ArrayList<Integer>(set);
	}

	private void addPlusMinus(Set<Integer> set) {
		List<Integer> list = new ArrayList<Integer>();
		for(Iterator<Integer> iterator = set.iterator(); iterator.hasNext();) {
			Integer x = iterator.next();
			list.add(x - 1);
			list.add(x + 1);
		}
		set.addAll(list);
	}

	FilenameFilter finalScreenshot = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.contains("FinalScreenshot");
		}
	};

	FilenameFilter csvFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.startsWith(lookfor);
		}
	};

}
