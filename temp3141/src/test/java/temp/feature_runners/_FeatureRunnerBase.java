package com.sprint.iice_tests.feature_runners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.sprint.iice_tests.lib.dal.services.AccountService;
import com.sprint.iice_tests.utilities.parser.ILpdfObjects.PDFRegions;
import com.sprint.iice_tests.utilities.test_util.DataFileFinder;
import com.sprint.iice_tests.web.browser.Browser;

import cucumber.api.testng.AbstractTestNGCucumberTests;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;

//@Listeners(RetryListener.class)
public class _FeatureRunnerBase extends AbstractTestNGCucumberTests {

	protected AccountService accountService = new AccountService();
	PDFRegions pdfRegions;
	public static String currentTest;
	

	File dir = new File(Browser.SYSTEM_FILE_PATH);

	@BeforeSuite(alwaysRun = true)
	public void initalSetup(ITestContext context) throws IOException, Exception {
		// Browser.printSystemProps();

		// DataFileFinder.clearLocalDataGoogleFolder();
		// for (ITestNGMethod method:context.getAllTestMethods()) {
		// method.setRetryAnalyzer(new RetryAnalyzer());
		// }
		PDFRegions.deleteAllContentsWithinTestDataFolder();
		DataFileFinder.deleteScreenshots();
		// XmlWriter.createXmlFile();
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws InterruptedException, IOException {	
		WebDriver driver = Browser.getDriverInstance();
		if (((RemoteWebDriver) driver).getSessionId() != null)
			driver.quit();		
		/*	new Browser().execKill();*/
	}

	@BeforeMethod(alwaysRun = true)
	public void startTest()  {
	}
	
	@AfterMethod(alwaysRun = true)
	public void screenShot() {
		// Screenshot.takeScreenShot(result, DataFileFinder.getScreenshotPath());
		// Check to see if this needs to move to @AfterClass or later
		// Browser.getBrowserProxy().stop();
	}

	@AfterSuite(alwaysRun = true)
	public void finalClean(ITestContext context) throws IOException, InterruptedException {
		File reportInputDirectory = new File("target//cucumber-reports");
		File reportOutputDirectory = getOutputDirectory();
		String buildNumber = getBuildNumber(reportOutputDirectory);
		if(!reportOutputDirectory.getPath().endsWith("target")) {
			reportOutputDirectory = new File(reportOutputDirectory.getAbsolutePath() + "\\" + buildNumber);
			reportOutputDirectory.mkdir();
			reportOutputDirectory.createNewFile();
		}
		String projectName = "cucumberProject";
		Configuration configuration = new Configuration(reportOutputDirectory, projectName);
		List<String> skippedFiles = new ArrayList<String>();
		if (dir.listFiles(skippedFilter).length == 1) {
			createClassificationFile(skippedFiles, dir.listFiles(skippedFilter)[0], true);
		} else if (dir.listFiles(passedFilter).length == 1) {
			createClassificationFile(skippedFiles, dir.listFiles(passedFilter)[0], false);
		} else {
			createClassificationFile(skippedFiles, dir.listFiles(failedFilter)[0], false);
		}

		List<String> files = new ArrayList<String>();
		files.add(reportInputDirectory + "\\CONFIG.properties");
		configuration.addClassificationFiles(files);

		/*
		 * Addition of dummy string necessary to avoid index out of bounds exception,
		 * when final skipped test is iterated over.
		 */
		skippedFiles.add("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		List<String> jsonFiles = new ArrayList<>();
		int i = 0;
		for (File file : reportInputDirectory.listFiles()) {
			if (file.getName().contains(".json")) {
				BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
				String test = file.getName().replaceAll("(.*cucumber-reports)(.*)(\\.json)", "$2");
				test = test.replaceAll(".json", "").replaceAll("Runner", "");
				String check = skippedFiles.get(i).replaceAll("Runner", "");
				try {
					String line = br.readLine();
					if (line.equals(null) || line.equals("") || line.equals("[]") || check.equals(test)) {
						br.close();
						if (skippedFiles.get(i).contains(test)) {
							i++;
						}
						file.delete();
					} else {
						jsonFiles.add(file.getAbsolutePath());
					}
					} catch(Exception e) {
						br.close();
						file.delete();
					}
				br.close();
			}
		}

		boolean runWithJenkins = true;

		// optional configuration - check javadoc
		configuration.setRunWithJenkins(runWithJenkins);
		configuration.setBuildNumber(buildNumber);
		// addidtional metadata presented on main page
		// configuration.addClassifications("Platform", "Windows");
		// configuration.addClassifications("Browser", "Firefox");
		// configuration.addClassifications("Branch", "release/1.0");

		// optionally add metadata presented on main page via properties file
		// List<String> classificationFiles = new ArrayList<>();
		// configuration.addClassificationFiles(classificationFiles);
		ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
		@SuppressWarnings("unused")
		Reportable result = reportBuilder.generateReports();
		// and here validate 'result' to decide what to do if report has failed

		new Browser().execKill();
		// System.exit(0);
	}
	
	private String getBuildNumber(File file) {
		if(file.getPath().endsWith("target")) 
			return "1";
		System.out.println(file.getAbsolutePath());
		int i = file.listFiles().length;
		return String.format("%d", i);
	}

	private File getOutputDirectory() {
		try {
			return new File(System.getProperty("outputDirectory"));
		} catch(NullPointerException e) {
			return new File("target");
		}
	}

	private List<String> createClassificationFile(List<String> list, File file, boolean skipped) throws IOException {
		File reportInputDirectory = new File("target//cucumber-reports");
		File newFile = new File(reportInputDirectory + "\\CONFIG.properties");
		FileWriter fw = new FileWriter(newFile);
		String line;
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(file));
		if (skipped) {
			if (file.getName().contains("PAPER"))
				fw.write("SKIPPED_TEST: BAN_______BILL RUN");
			else
				fw.write("SKIPPED_TEST: BAN");
			fw.write(System.lineSeparator());
			while (!(line = br.readLine()).startsWith("Number of skipped tests")) {
				String writeLine = "";
				if (line.length() != 0) {
					if (line.contains("BILL_RUN")) {
						writeLine = parseLineWithBillRun(line);
					} else {
						writeLine = parseLineFromDigital(line);
					}
					list.add(writeLine.split(":")[0]);
					fw.write(writeLine);
					fw.write(System.lineSeparator());
				}
			}
			fw.write(line.replaceAll(" ", "_"));
			fw.write(System.lineSeparator());
		}
		while ((line = br.readLine()) != null) {
			boolean writeLine = line.startsWith("Test suite duration") || line.startsWith("Environment")
					|| line.startsWith("Browser") || line.startsWith("Device");
			boolean modified = line.startsWith("Local branch is");
			if (writeLine) {
				fw.write(line.replaceAll(" ", "-"));
				fw.write(System.lineSeparator());
			} else if (modified) {
				String behind = line.replaceAll("(Local branch is )(.*)( commits behind upstream.*)", "$2");
				String ahead = line.replaceAll("(.* and upstream is )(.*)( commits behind local.*)", "$2");
				fw.write("Commits_behind_upstream: " + behind);
				fw.write(System.lineSeparator());
				fw.write("Commits_ahead_of_upstream: " + ahead);
				fw.write(System.lineSeparator());
			}
		}
		String notes = getSystemNotes();
		fw.write("Notes: " + notes);
		fw.close();
		return list;
	}

	private String getSystemNotes() {
		try {
			return System.getProperty("notes");
		} catch(Exception e) {
			return "none";
		}
	}

	private String parseLineWithBillRun(String line) {
		String test, ban = "", billRun = "";
		if (line.startsWith("BAN")) {
			test = line.replaceAll("(.*DOWNLOAD_)(.*)(_skipped.*)", "$2");
			ban = line.replaceAll("(BAN_)(.*)(_BILL_RUN.*)", "$2");
			billRun = line.replaceAll("(.*BILL_RUN_)(.*)(_DID_NOT.*)", "$2");
		} else {
			test = line.replaceAll("(.*)(_skipped.*)", "$1");
		}
		return test + ":" + ban + " : " + billRun;
	}

	private String parseLineFromDigital(String line) {
		String test, ban = "";
		if (line.startsWith("BAN")) {
			test = line.replaceAll("(.*DOWN_)(.*)(Runner_skipped.*)", "$2");
			ban = line.replaceAll("(BAN_)(.*)(_IS_DOWN_.*)", "$2");
		} else {
			test = line.replaceAll("(.*)(Runner_skipped.*)", "$1");
		}
		return test + ":" + ban;
	}

	FilenameFilter skippedFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.contains("SKIPPED");
		}
	};

	FilenameFilter passedFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.contains("PASSED");
		}
	};

	FilenameFilter failedFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.contains("FAILED");
		}
	};
	
	FilenameFilter pdfFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".pdf");
		}
	};

}