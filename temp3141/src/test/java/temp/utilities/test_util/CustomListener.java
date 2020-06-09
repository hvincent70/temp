package com.sprint.iice_tests.utilities.test_util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.sprint.iice_tests.lib.dao.vo.Account;
import com.sprint.iice_tests.utilities.parser.ILpdfObjects.PDFRegions;
import com.sprint.iice_tests.web.browser.Browser;

public class CustomListener extends TestListenerAdapter implements ISuiteListener {

	// protected AccountService accountService = new AccountService();
	Browser browser = new Browser();
	private int m_count = 0;
	LocalTime endTime;
	LocalTime startTime;

	@Override
	public void onStart(ISuite arg0) {
		startTime = CustomListener.timeAtThisInstant();
	}

	@Override
	public void onFinish(ITestContext testContext) {
		int totalNumOfTestsRunInSuite = testContext.getAllTestMethods().length;
		System.out.println("Total number of tests run in suite: " + totalNumOfTestsRunInSuite);

		endTime = CustomListener.timeAtThisInstant();
		try {
			CustomListener.addInfoLinesToTestResultTxtFiles(endTime, startTime, totalNumOfTestsRunInSuite, testContext);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CustomListener.changeTestLoggerNames();
		// Reporter.log("About to end executing Suite " + arg0.getName(), true);
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		logTestResult(tr, "FAILED");
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		logTestResult(tr, "SKIPPED");
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		logTestResult(tr, "PASSED");
	}

	public void logTestResult(ITestResult tr, String result) {
		String simpleTestName = tr.getInstance().getClass().getSimpleName();
		log(simpleTestName + "--Test " + result + "\n");
		if (!Browser.runIsDigital() && !Browser.runIsAPI() && !tr.isSuccess()) {
			try {
				simpleTestName = handlePaper(simpleTestName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (!Browser.runIsAPI()) {
				try {
					simpleTestName = renameTest() + "_" + simpleTestName;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			System.out.println("ABOUT TO LOG TEST RESULT");
			logTestResult(simpleTestName, result, tr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String handlePaper(String simpleTestName) throws Exception {
		String testName = simpleTestName;
		String pdfNameWithTimeStamp = Browser.FILE_PATH.toString() + Browser.fs + "test"
				+ Browser.classMap.get(Thread.currentThread().getId()).toString().replace(".json", ".pdf");
		if (testNameIsPaperTestName(simpleTestName)) {
			System.out.println("IN CUSTOM LISTENER");
			if (Browser.pdfMap.containsKey(Thread.currentThread().getId())) {
				System.out.println("IN CUSTOM LISTENER");
				pdfNameWithTimeStamp = Browser.pdfMap.get(Thread.currentThread().getId()).toString();
				System.out.println("This is the pdf name with time stampe: " + pdfNameWithTimeStamp);
				try {
					testName = changeSimpleTestNameIfFileDidNotDownload(pdfNameWithTimeStamp, simpleTestName);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
				testName = changeSimpleTestNameIfFileDidNotDownload(pdfNameWithTimeStamp, simpleTestName);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (!testNameIsPaperTestName(simpleTestName)) {
			try {
				testName = changeSimpleTestNameIfBANIsDown(simpleTestName);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return testName;
	}

	public void renamePDFToIncludeTestName(String pdfNameWithTimeStamp, String simpleTestName) {
		File oldName = new File(pdfNameWithTimeStamp);
		if (oldName.exists()) {
			String pdfNameToIncludeTestName = generatePDFNameToIncludeTestName(pdfNameWithTimeStamp, simpleTestName);
			File newName = new File(pdfNameToIncludeTestName);
			Boolean rename = oldName.renameTo(newName);
			System.out.println(
					"THE FILE " + oldName.toString() + " WAS NAMED TO " + newName.toString() + " BOOLEAN: " + rename);
		}
	}

	public void renameFileThatExistsInDirectory(String pdfNameWithTimeStamp, String simpleTestName) throws IOException {
		String pdfNameToIncludeTestName = generatePDFNameToIncludeTestName(pdfNameWithTimeStamp, simpleTestName);
		Path source = Paths.get(pdfNameWithTimeStamp);
		Path dest = source.resolveSibling(pdfNameToIncludeTestName);
		System.out.println("the source is: " + source.toString() + " and the destination is " + dest);
		Files.move(source, dest);
	}

	public String generatePDFNameToIncludeTestName(String pdfNameWithTimeStamp, String simpleTestName) {
		String pdfNameToIncludeTestName = pdfNameWithTimeStamp;
		int beforePDF = pdfNameToIncludeTestName.indexOf(".pdf");
		pdfNameToIncludeTestName = new StringBuilder(pdfNameToIncludeTestName).insert(beforePDF, "_" + simpleTestName)
				.toString();
		return pdfNameToIncludeTestName;
	}

	public String changeSimpleTestNameIfFileDidNotDownload(String pdfNameWithTimeStamp, String simpleTestName)
			throws Exception {
		synchronized (this.getClass()) {
			File newName = new File(pdfNameWithTimeStamp);

			if (DataFileFinder.fileDoesNotExist(newName)) {
				String didNotDownloadMsg = renameTest() + "_DID_NOT_DOWNLOAD_";
				int beforeName = 0;
				simpleTestName = new StringBuilder(simpleTestName).insert(beforeName, didNotDownloadMsg).toString();
				return simpleTestName;
			} else {
				simpleTestName = renameTest() + "_" + simpleTestName;
			}
			return simpleTestName;
		}
	}

	public String renameTest() throws Exception {
		String prefixBeforeTestMsg = "BAN_" + Browser.banMap.get(Thread.currentThread().getId());

		if (!Browser.runIsDigital() && !Browser.runIsAPI()) {
			prefixBeforeTestMsg = "BAN_" + Browser.banMap.get(Thread.currentThread().getId()) + "_BILL_RUN_"
					+ billRunForEnv();
		}
		return prefixBeforeTestMsg;
	}

	public String changeSimpleTestNameIfBANIsDown(String simpleTestName) throws IOException {
		synchronized (this.getClass()) {
			String banIsDownMsg = "BAN_" + Browser.banMap.get(Thread.currentThread().getId()) + "_IS_DOWN_";
			String site = System.getProperty("site");
			boolean banIsDown = Browser.banIsDown(site);
			if (banIsDown) {
				int beforeName = 0;
				simpleTestName = new StringBuilder(simpleTestName).insert(beforeName, banIsDownMsg).toString();
				return simpleTestName;
			}
			return simpleTestName;
		}
	}

	public void handleWhenPDFDoesNotExist(String pdfNameWithTimeStamp, String simpleTestName) {
		File newName = new File(pdfNameWithTimeStamp);
		if (DataFileFinder.fileDoesNotExist(newName)) {
			System.out.println("THE FOLLOWING TEST DID NOT DOWNLOAD A PDF: " + simpleTestName);
			String pdfDidNotDownloadName = pdfNameWithTimeStamp.replace(".pdf", "_");
			String txtFileIndicatesDidNotDownload = pdfDidNotDownloadName + "_DID_NOT_DOWNLOAD_PDF.txt";
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(

					new FileOutputStream(txtFileIndicatesDidNotDownload), "utf-8"))) {
				writer.write(pdfDidNotDownloadName + " did not download pdf");
				// File dest = new File(Browser.FILE_PATH + Browser.fs + "pdf" + Browser.fs);
				// FileUtils.copyFileToDirectory(new File(txtFileIndicatesDidNotDownload),
				// dest);

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean testNameIsPaperTestName(String testName) {
		boolean testIsPaper = testName.contains("PaperValidate");
		System.out.println("THIS TEST IS A PAPER TEST: " + testIsPaper);
		return testIsPaper;
	}

	private void log(String string) {
		System.out.print(string);
		if (++m_count % 40 == 0) {
			System.out.println("");
		}
	}

	public void logTestResult(String simpleTestName, String passOrFail, ITestResult tr) throws IOException {
		String nl = System.lineSeparator();
		String fileName = Browser.FILE_PATH + Browser.fs + passOrFail + ".txt";
		String text = nl + nl + simpleTestName + "_" + passOrFail.toLowerCase() + "_duration_was_"
				+ testExecutionTime(tr);
		File fileLogger = new File(fileName);
		if (DataFileFinder.fileDoesNotExist(fileLogger)) {
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"))) {
				writer.write(text);
			}
		} else if (!DataFileFinder.fileDoesNotExist(fileLogger)) {
			Files.write(Paths.get(fileName), text.getBytes(), StandardOpenOption.APPEND);
		}
	}

	public static int countLinesOfTextInFile(String fileNameAsString) throws FileNotFoundException, IOException {
		String fileName = fileNameAsString;
		int result = 0;
		try (FileReader input = new FileReader(fileName); LineNumberReader count = new LineNumberReader(input);) {
			while (count.skip(Long.MAX_VALUE) > 0) {
				// Loop just in case the file is > Long.MAX_VALUE or skip() decides to not read
				// the entire file
			}
			result = count.getLineNumber();
		}
		return result;
	}

	public static void infoLineForTestResults(String fileNameAsString, LocalTime endTime, LocalTime startTime,
			int totalTestsRun, int numberOfTests) throws FileNotFoundException, IOException {
		String fileName = Browser.FILE_PATH + Browser.fs + fileNameAsString;
		String result = fileNameAsString.replace(".txt", "");
		if (!DataFileFinder.fileDoesNotExist(new File(fileName))) {
			String percentage = percentAsString(totalTestsRun, numberOfTests);
			String numberOfTestsDisplayedInFileAsString = Integer.toString(numberOfTests);
			LocalTime timeElapsedForSuiteRun = findElapsedTime(startTime, endTime);
			String text = textToAddAtEndOfLog(result, percentage, totalTestsRun, numberOfTestsDisplayedInFileAsString,
					startTime, endTime, timeElapsedForSuiteRun);
			Files.write(Paths.get(fileName), text.getBytes(), StandardOpenOption.APPEND);
		}
	}

	static String textToAddAtEndOfLog(String result, String percentage, int totalTestsRun,
			String numberOfTestsDisplayedInFileAsString, LocalTime startTime, LocalTime endTime,
			LocalTime timeElapsedForSuiteRun) throws IOException {
		String nl = System.lineSeparator();

		String text = nl + nl + "Number of " + result.toLowerCase() + " tests in suite: "
				+ numberOfTestsDisplayedInFileAsString + nl + "Total number of tests run in suite: " + totalTestsRun
				+ nl + "Percent of tests " + result.toLowerCase() + " in suite: " + percentage + "%" + nl
				+ "Suite started at " + startTime + " and it ended at " + endTime + nl + "Test suite duration: "
				+ timeElapsedForSuiteRun + nl + grabDate() + nl + systemPropsSetByTester();
		return text;
	}

	public static String systemPropsSetByTester() throws IOException {
		String url = System.getProperty("site");
		String userHomeDir = System.getProperty("user.dir");
		String device = System.getProperty("device");
		String browser = System.getProperty("browser");
		String runningHeadless = System.getProperty("runHeadless");
		String nl = System.lineSeparator();
		/*
		 * String gitLine = "This regression was run on Jenkins."; if
		 * (mapOfGitCommandOutputs().containsKey("numOfCommitsRemoteBehindLocal") &&
		 * mapOfGitCommandOutputs().containsKey("numOfCommitsLocalBehindRemote")) {
		 * gitLine = "Local branch is " +
		 * mapOfGitCommandOutputs().get("numOfCommitsLocalBehindRemote") +
		 * " commits behind upstream, and upstream is " +
		 * mapOfGitCommandOutputs().get("numOfCommitsRemoteBehindLocal") +
		 * " commits behind local."; }
		 */
		String sysProps = nl + "Environment: " + url + nl + "Browser: " + browser + nl + "Device: " + device + nl
				+ "Test was run headless: " + runningHeadless + nl + "Test executed on user directory: " + userHomeDir
				+ nl + nl;
		;
		System.out.println(sysProps);
		return sysProps;

	}

	public static Map<String, String> mapOfGitCommandOutputs() throws IOException {
		Map<String, String> gitMap = new HashMap<>();
		if (System.getProperty("user.dir").contains(Browser.fs + "git" + Browser.fs) && Browser.isWindows()) {

			String gitPath = System.getProperty("user.dir") + Browser.fs + ".git";
			// System.out.println(gitPath);
			String grabNameOfBranch = "git rev-parse --abbrev-ref ";
			String nameOfLocalBranch = grabNameOfBranch + "HEAD";
			String nameOfRemoteThatLocalTracks = grabNameOfBranch + "@{u}";
			String fetch = "git fetch";

			// System.out.println("This is the name of the current branch:
			// "+nameOfLocalBranch);
			String nameOfLocal = processBuildForWindowsAndUnix(gitPath, nameOfLocalBranch);
			String nameOfRemote = processBuildForWindowsAndUnix(gitPath, nameOfRemoteThatLocalTracks);
			String numOfCommitsRemoteBehindLocal = "git rev-list --count " + nameOfRemote + ".." + nameOfLocal;
			String numOfCommitsLocalBehindRemote = "git rev-list --count " + nameOfLocal + ".." + nameOfRemote;
			processBuildForWindowsAndUnix(gitPath, fetch);
			String remoteBehindLocal = processBuildForWindowsAndUnix(gitPath, numOfCommitsRemoteBehindLocal);
			String localBehindRemote = processBuildForWindowsAndUnix(gitPath, numOfCommitsLocalBehindRemote);
			gitMap.put("numOfCommitsRemoteBehindLocal", remoteBehindLocal);
			gitMap.put("numOfCommitsLocalBehindRemote", localBehindRemote);

			return gitMap;
		}
		return gitMap;
	}

	public static Map<String, String> gitCommandsBeginLocalSuiteRun() throws IOException {
		Map<String, String> gitMap = new HashMap<>();
		Boolean runningLocally = System.getProperty("user.dir").contains(Browser.fs + "git" + Browser.fs)
				&& (!System.getProperty("user.dir").contains("jenkins"));
		if (runningLocally && Browser.isWindows()) {

			String gitPath = System.getProperty("user.dir") + Browser.fs + ".git";
			// System.out.println(gitPath);
			String grabLastCommitMsg = "git log -1 --pretty=%B";
			String checkLastCommitMessage = processBuildForWindowsAndUnix(gitPath, grabLastCommitMsg);
			gitMap.put("LAST_COMMIT_MSG", checkLastCommitMessage);

			String grabCommitBeforeLastCommitMsg = "git log -2 --pretty=%B";
			String checkCommitBeforeLastCommitMessage = processBuildForWindowsAndUnix(gitPath,
					grabCommitBeforeLastCommitMsg);
			gitMap.put("COMMIT_BEFORE_LAST_COMMIT_MSG", checkCommitBeforeLastCommitMessage);
		}
		return gitMap;
	}

	public static void failIfLastCommitMsgIsNotPrefixedCorrectly() throws IOException {
		String keyForLastCommit = "LAST_COMMIT_MSG";
		String prefix = "UYV-2 ";
		if (gitCommandsBeginLocalSuiteRun().containsKey(keyForLastCommit)) {

			String lastCommitMsg = gitCommandsBeginLocalSuiteRun().get(keyForLastCommit);
			String commitBeforeLastCommit = gitCommandsBeginLocalSuiteRun().get("COMMIT_BEFORE_LAST_COMMIT_MSG");

			Boolean lastCommitIsPrefixedCorrectly = lastCommitMsg.startsWith(prefix);
			Boolean joinedLast2CommitsContain2Prefixes = containsTwoPrefixes(commitBeforeLastCommit);

			System.out.println("YOUR LAST 2 COMMITS WERE PREFIXED CORRECTLY: "
					+ (lastCommitIsPrefixedCorrectly && joinedLast2CommitsContain2Prefixes)
					+ "\nIF THEY WERE NOT, THE PROGRAM WILL FAIL HERE.");

			if (!lastCommitIsPrefixedCorrectly) {
				System.out.println("THIS IS YOUR LAST COMMIT: " + lastCommitMsg);
			}
			if (!joinedLast2CommitsContain2Prefixes) {
				System.out.println("CHECK YOUR LAST TWO COMMITS");
			}

			Assert.assertTrue(lastCommitIsPrefixedCorrectly && joinedLast2CommitsContain2Prefixes,
					"EITHER OF YOUR LAST 2 COMMIT MSGS WERE NOT PREFIXED WITH UYV-2 ");
		}
	}

	public static Boolean containsTwoPrefixes(String line) {
		int count = StringUtils.countMatches(line, "UYV-2 ");
		Boolean contains2Prefixes = count == 2;
		return contains2Prefixes;
	}

	public static String processBuildForWindowsAndUnix(String dir, String command) throws IOException {
		String[] command0 = null;
		String output = null;
		if (Browser.isWindows()) {
			command0 = new String[] { "CMD", "/C", command };
		} else if (Browser.isUnix()) {
			command0 = new String[] { "/bin/bash", "-c", command };
		}
		ProcessBuilder pr = new ProcessBuilder();
		pr.directory(new File(dir));
		try {
			// System.out.println("We are in this directory: "+pr.directory().toString());
			pr.command(command0);
			Process process = pr.start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			output = new BufferedReader(isr).lines().collect(Collectors.joining(" "));
		} catch (IOException e) {
			System.out.println("PROCESS BUILDER TO KILL CHROMEDRIVER BY PID WAS NOT SUCESSFUL");
		}
		return output;
	}

	public static LocalTime timeAtThisInstant() {
		LocalTime localTime = LocalTime.now().truncatedTo(ChronoUnit.NANOS);
		return localTime;
	}

	public static LocalTime findElapsedTime(LocalTime startTime, LocalTime endTime) {
		LocalTime timeElapsed = endTime.minus(startTime.getHour(), ChronoUnit.HOURS)
				.minus(startTime.getMinute(), ChronoUnit.MINUTES).minus(startTime.getSecond(), ChronoUnit.SECONDS)
				.minus(startTime.getNano(), ChronoUnit.NANOS);
		return timeElapsed;
	}

	public static void addInfoLinesToTestResultTxtFiles(LocalTime endTime, LocalTime startTime, int totalTestsRun,
			ITestContext testContext) throws FileNotFoundException, IOException {
		int numberOfFailed = testContext.getFailedTests().size();
		int numberOfPassed = testContext.getPassedTests().size();
		int numberOfSkipped = testContext.getSkippedTests().size();
		System.out.println("Number of passed tests: " + numberOfPassed + "\nNummber of failed tests: " + numberOfFailed
				+ "\nNumber of skipped tests: " + numberOfSkipped);
		infoLineForTestResults("PASSED.txt", endTime, startTime, totalTestsRun, numberOfPassed);
		infoLineForTestResults("FAILED.txt", endTime, startTime, totalTestsRun, numberOfFailed);
		infoLineForTestResults("SKIPPED.txt", endTime, startTime, totalTestsRun, numberOfSkipped);
	}

	public static String percentAsString(int totalVal, int numerator) {
		float percent = percentage(totalVal, numerator);
		String percentAsText = Float.toString(percent);
		return percentAsText;
	}

	public static float percentage(int totalVal, int numerator) {
		float total = totalVal;
		float num = numerator;
		float percentage = ((num * 100) / total);
		return percentage;
	}

	public static void changeLoggerNameToIncludeDate(String fileName) {
		LocalDate date = LocalDate.now();
		String fileNameWithDir = Browser.FILE_PATH + Browser.fs + fileName;
		File oldFileName = new File(fileNameWithDir);
		String dateAsString = System.currentTimeMillis() + "_" + date.toString().replaceAll("-", "_");
		String fileNameIncludesDate = generateLoggerNameToIncludeDate(fileNameWithDir, dateAsString);
		File newFileName = new File(fileNameIncludesDate);
		if (oldFileName.exists()) {
			oldFileName.renameTo(newFileName);
			System.out.println("the date is " + dateAsString + " and the newFileName is " + fileNameIncludesDate);
		}
	}

	public static String grabDate() {
		return zonedDate("CST", "Date in Overland Park, KS:") + " "
				+ zonedDate("Asia/Manila", "Date in the Philippines:");
	}

	public static String zonedDate(String timeZone, String msg) {
		SimpleDateFormat sd = new SimpleDateFormat();
		sd.setTimeZone(TimeZone.getTimeZone(timeZone));
		Date date = new Date();
		String dateTimeZoned = sd.format(date);
		String logDate = msg + " " + dateTimeZoned + ".";
		return logDate;
	}

	public static String generateLoggerNameToIncludeDate(String oldLoggerName, String textToAdd) {
		String name = oldLoggerName;
		int offsetForFileSep = 1;
		int beforeTxt = oldLoggerName.indexOf(".txt");
		int locOfTestData = oldLoggerName.indexOf("TestData");
		int beforeFileName = locOfTestData + "TestData".length() + offsetForFileSep;
		String nameOfRun = Browser.grabTestRunnerType();

		String featRun = "feature_runners";
		int sizeOfFeatureRunnersTxt = featRun.length();
		int posOfFeatRun = nameOfRun.indexOf(featRun);
		int textSizeOfRunnerType = 8;
		int beginningOfRunnerType = posOfFeatRun + sizeOfFeatureRunnersTxt + offsetForFileSep;
		String grabTypeOfTest = nameOfRun.substring(beginningOfRunnerType, beginningOfRunnerType + textSizeOfRunnerType)
				.toUpperCase();
		name = new StringBuilder(name).insert(beforeTxt, "_" + textToAdd).insert(beforeFileName, grabTypeOfTest + "_")
				.toString();
		return name;
	}

	public static void changeTestLoggerNames() {
		changeLoggerNameToIncludeDate("FAILED.txt");
		changeLoggerNameToIncludeDate("PASSED.txt");
		changeLoggerNameToIncludeDate("SKIPPED.txt");
	}

	public String testExecutor() {
		System.getProperties();
		return System.getProperty("");
	}

	public static String grabSpecificParameterFromTestNG(ITestResult tr, String parameter) {
		Object[] testNGArray = tr.getParameters();
		String[] testNGStringArray = Arrays.copyOf(testNGArray, testNGArray.length, String[].class);
		String parameterAsString = testNGStringArray[PDFRegions.findIndexOfPrimArray(parameter, testNGStringArray)];
		return parameterAsString;
	}

	public void printTestNGInfo(ITestResult tr) {
		Object[] testNGArray = tr.getParameters();
		String[] testNGStringArray = Arrays.copyOf(testNGArray, testNGArray.length, String[].class);
		System.out.println("**************This is testNGStringArray: " + testNGStringArray);
	}

	public static String siteURL(ITestResult tr) {
		return grabSpecificParameterFromTestNG(tr, "site");
	}

	public String testExecutionTime(ITestResult tr) {
		long testEndTime = tr.getEndMillis();
		long testStartTime = tr.getStartMillis();
		long elapsedTimeAsLong = testEndTime - testStartTime;
		String elapsedTime = DurationFormatUtils.formatDurationHMS(elapsedTimeAsLong);
		System.out.println("Duration of test " + tr.getInstance().getClass().getSimpleName() + " was " + elapsedTime);
		return elapsedTime;
	}

	@Override
	public void onFinish(ISuite suite) {
		// TODO Auto-generated method stub

	}

	public final String billRunForEnv() throws Exception {
		Account account = Browser.accountMap.get(Thread.currentThread().getId());
		String billRun = "BillRunIsNull";
		if (Browser.isILPaperRun() || Browser.runningOnUAT1()) {
			billRun = account.getBillRun();
		} else if (Browser.runningOnUAT2() || Browser.runningOn039() || Browser.runningOn037()) {
			billRun = account.getBillRun2();
		}
		return billRun;
	}

}
