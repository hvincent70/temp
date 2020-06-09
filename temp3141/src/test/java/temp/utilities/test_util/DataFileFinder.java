package com.sprint.iice_tests.utilities.test_util;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sprint.iice_tests.lib.dao.data.Constants;
import com.sprint.iice_tests.web.browser.Browser;
import com.sprint.iice_tests.web.pages.il_digital_paper.BillRunReport;
import com.sprint.iice_tests.web.pages.il_digital_paper.ChargesTab;

public class DataFileFinder {
	final static String DOWNLOAD_FOLDER = "/src/test/resources/TestData";
	File dir = new File(System.getProperty("user.dir") + DOWNLOAD_FOLDER);
	protected WebDriver driver;
	protected WebDriverWait wait;
	long threadId = Thread.currentThread().getId();

	public static String getTestDataPathByName(String relativeName) {

		String path = null;
		if (relativeName.endsWith(Constants.JSON)) {

			List<File> fileList = getTestClassList();
			for (int i = 0; i < fileList.size(); i++) {
				if (fileList.get(i).getName().equals(relativeName)) {
					path = fileList.get(i).getAbsolutePath();
				}
				// if(fileList.get(i).getName().contains(relativeName)){
				// path = fileList.get(i).getAbsolutePath();
				// }
			}
			return path;

		} else if (relativeName.endsWith(Constants.PDF)) {
			return new File(Constants.TEST_DATA_PATH.concat(Constants.PDF_PATH).concat(relativeName)).getAbsolutePath();
		} else if (relativeName.endsWith(Constants.DOCX)) {
			return new File(Constants.TEST_DATA_PATH.concat(Constants.DOCX_PATH.concat(relativeName)))
					.getAbsolutePath();
		} else {
			System.out.println("No path found.");
			return null;
		}
	}

	public static String getJsonTestData(@SuppressWarnings("rawtypes") Class clazz) {
		
		System.out.println("Here is the data path: " + clazz.getSimpleName().concat(Constants.JSON));
		
		return getTestDataPathByName(clazz.getSimpleName().concat(Constants.JSON));
	}

	public static String getScreenshotPath() {
		return new File(Constants.SCREENSHOT_PATH).getAbsolutePath().concat("\\");
	}

	public static void deleteFilesInDirectory(String path) {
		File file = new File(path);
		if (file.isDirectory() && file.list().length > 0) {
			try {
				FileUtils.cleanDirectory(file);
			} catch (IOException e) {
				System.out.println("Files are deleted.");
			}
		}
	}

	public static void deleteScreenshots() {
		deleteFilesInDirectory(getScreenshotPath());
	}

	public static List<File> getTestClassList() {
		// File folder1 = new
		// File(Constants.TEST_DATA_PATH.concat(Constants.JSON_PATH).concat(Constants.IL_DIGITAL_PATH)).getAbsoluteFile();
		// File folder2 = new
		// File(Constants.TEST_DATA_PATH.concat(Constants.JSON_PATH).concat(Constants.IL_PAPER_PATH)).getAbsoluteFile();
		// File folder3 = new
		// File(Constants.TEST_DATA_PATH.concat(Constants.JSON_PATH).concat(Constants.CL_DIGITAL_PATH)).getAbsoluteFile();
		// File folder4 = new
		// File(Constants.TEST_DATA_PATH.concat(Constants.JSON_PATH).concat(Constants.CL_PAPER_PATH)).getAbsoluteFile();
		// File[] list1 = folder1.listFiles();
		// File[] list2 = folder2.listFiles();
		// File[] list3 = folder3.listFiles();
		// File[] list4 = folder4.listFiles();
		// List<File> fileList = new ArrayList<>();
		// for(File file:list1) {
		// fileList.add(file);
		// }
		// for(File file:list2) {
		// fileList.add(file);
		// }
		// for(File file:list3) {
		// fileList.add(file);
		// }
		// for(File file:list4) {
		// fileList.add(file);
		// }
		// return fileList;
		List<File> fileList = Arrays.asList(
				new File(Constants.TEST_DATA_PATH.concat(Constants.JSON_PATH).concat(Constants.IL_DIGITAL_PATH))
						.getAbsoluteFile(),
				new File(Constants.TEST_DATA_PATH.concat(Constants.JSON_PATH).concat(Constants.IL_PAPER_PATH))
						.getAbsoluteFile(),
				new File(Constants.TEST_DATA_PATH.concat(Constants.JSON_PATH).concat(Constants.CL_DIGITAL_PATH))
						.getAbsoluteFile(),
				new File(Constants.TEST_DATA_PATH.concat(Constants.JSON_PATH).concat(Constants.CL_PAPER_PATH))
						.getAbsoluteFile(),
				new File(Constants.TEST_DATA_PATH.concat(Constants.JSON_PATH).concat(Constants.API_PATH))
				.getAbsoluteFile());

		List<File[]> list = fileList.stream().map((f) -> f.listFiles()).collect(Collectors.toList());

		List<File> files = new ArrayList<>();
		for (File[] file : list) {
			for (File f : file) {
				files.add(f);
			}
		}
		return files;
	}

	public static void clearLocalDataGoogleFolder() throws IOException {
		if (FileUtils
				.getFile(System.getProperty("user.home") + ("\\") + Constants.LOCAL_APP_DATA_PATH_GOOGLE) != null) {
			FileUtils.deleteDirectory(
					new File(System.getProperty("user.home") + ("\\") + Constants.LOCAL_APP_DATA_PATH_GOOGLE));
		}

	}

	public static String csvFileRegex = ".*csv$";

	public static boolean fileIsCSV(String textToTest) {
		boolean regex = testStringAgainstRegex(textToTest, csvFileRegex);
		boolean endsWith = textToTest.endsWith(".csv");
		// System.out.println("For " +textToTest+" regex test is: "+regex+" endsWith
		// test is: "+endsWith);
		return regex || endsWith;
	}

	/*
	 * FilenameFilter filter = new FilenameFilter() {
	 * 
	 * @Override public boolean accept(File dir, String name) { return
	 * name.endsWith(".pdf") && !name.equals("test.pdf"); } };
	 */
	static FilenameFilter filter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".pdf") && !name.startsWith("test");
		}
	};

	FilenameFilter filterForCSV = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".csv");
		}
	};

	public synchronized boolean renameDownloadedPDF(String fileName) throws InterruptedException {

		boolean pdfRenamed = false;
		synchronized (this.getClass()) {
			String testname = BillRunReport.getPdfDownloadedName();
			File testPDF = new File(testname);
			File newName = new File(fileName);
			pdfRenamed = testPDF.renameTo(newName);
			System.out.println("THE FILE " + testname + " was renamed to " + fileName + " " + pdfRenamed);
		}
		return pdfRenamed;

	}

	public static boolean testStringAgainstRegex(String textToTest, String regex) {
		return textToTest.matches(regex);
	}

	public static Predicate<String> fileIsCSV = x -> fileIsCSV(x);

	public void deleteAllCSVs() throws InterruptedException, IOException {
		deleteAllTestDataFilesThatAreFilteredByPredicate(fileIsCSV);
	}

	static FilenameFilter filterForPDFsNotRenamed = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return !name.matches("test*.pdf");
		}
	};

	public static boolean pdfNotRenamedExistsInTestDataDir() throws InvalidPasswordException, IOException {
		File dir = new File(Browser.FILE_PATH);
		String[] list = dir.list(filterForPDFsNotRenamed);
		return list.length > 0;
	}

	public static void doSomethingToAllSimilarFilesInDirWithPredicate(String dir, Predicate<String> filterForFiles,
			Predicate<File> actionToPerformOnFiles) throws InterruptedException {
		File testDir = new File(dir);

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean testAgainstName = filterForFiles.test(name);
				return testAgainstName;
			}
		};
		Thread.sleep(2000);
		String[] listOfFilesWeWantToModifyInDir = testDir.list(filter);
		for (String fileName : listOfFilesWeWantToModifyInDir) {
			String testname = testDir.toString() + Browser.fs
					+ listOfFilesWeWantToModifyInDir[findIndexOfPrimArray(fileName, listOfFilesWeWantToModifyInDir)];
			System.out.println("this is the file name: " + fileName + " and its full name is " + testname);
			File file = new File(testname);

			actionToPerformOnFiles.test(file);

		}
	}

	public void deleteAllTestDataFilesThatAreFilteredByPredicate(Predicate<String> filterForFileToDelete)
			throws InterruptedException, IOException {
		doSomethingToAllSimilarFilesInDirWithPredicate(Browser.FILE_PATH, filterForFileToDelete, deleteFile);
	}

	public Predicate<File> deleteFile = file -> {
		synchronized (this.getClass()) {
			Boolean fileDeleted = false;

			try {
				fileDeleted = deleteFileIfExists(file);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return fileDeleted;
		}
	};

	public boolean deleteFileIfExists(File file) throws InterruptedException {
		synchronized (this.getClass()) {
			boolean deletedFile = false;
			String fileName = file.toString();
			for (int i = 0; i < 3; i++) {
				if (file.exists()) {
					Thread.sleep(2000);

					deletedFile = file.delete();

					System.out.println(fileName + " was deleted: " + deletedFile);
				}
			}
			System.out.println(fileName + " was deleted: " + deletedFile);
			return deletedFile;
		}
	}

	public static void copyDriversToLocOutsideProject() throws IOException, InterruptedException {
		DataFileFinder.doSomethingToAllSimilarFilesInDirWithPredicate(Browser.locationOfDrivers, isDriver, copyDrivers);
	}

	public void deleteDriversFromNewLoc() throws IOException, InterruptedException {
		doSomethingToAllSimilarFilesInDirWithPredicate(Browser.retrieveDrLocForOS(), isDriver, deleteFile);
	}

	// static Predicate<String> isDriver = x -> x.contains("driver") ||
	// x.contains("Server");
	static Predicate<String> isDriver = x -> correctDriver(x);

	static Boolean correctDriver(String driver) {
		Boolean isCorrectDriver = false;
		if (Browser.isUnix()) {
			isCorrectDriver = (driver.contains("driver") || driver.contains("Server")) && (!driver.contains(".exe"));
		} else if (Browser.isWindows()) {
			isCorrectDriver = driver.endsWith("driver.exe") || driver.endsWith("Server.exe");
		}
		return isCorrectDriver;
	}

	static Predicate<File> copyDrivers = driver -> {
		int drPos = driver.toString().lastIndexOf(Browser.fs) + 1;
		String dr = driver.toString().substring(drPos);
		String newLoc = Browser.retrieveDrLocForOS() + Browser.fs + dr;
		Boolean driverExistsInNewLoc = new File(newLoc).exists();

		System.out.println(newLoc + " exists " + driverExistsInNewLoc);
		if (!driverExistsInNewLoc) {
			try {
				FileUtils.copyFileToDirectory(driver, new File(Browser.retrieveDrLocForOS()));
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Driver already exists in: " + Browser.retrieveDrLocForOS());
		}

		return driverExistsInNewLoc;
	};

	public static int findIndexOfPrimArray(String targetInArray, String[] arrayName) {
		return IntStream.range(0, arrayName.length).filter(i -> targetInArray == arrayName[i]).findFirst().orElse(-1);
		// return -1 if target is not found
	}

	public Boolean clickUntilFileDownloadsByOKButton(WatchKey watchKey, String nameOfFile) throws Exception {
		synchronized (this.getClass()) {
			boolean fileDownloaded = false;
			ChargesTab p = new ChargesTab();
			p.scrollToAndClickElement(p.openOkayButton());
			int additionalNumberOfTimesToClick = 3;
			for (int i = 0; i <= additionalNumberOfTimesToClick; i++) {
				fileDownloaded = waitForFile(nameOfFile, watchKey);
				if (fileDownloaded) {
					System.out.println("FILE: " + nameOfFile + " DOWNLOADED");
					renameCSVAndAddToMap(Browser.FILE_PATH + Browser.fs + nameOfFile);
					break;
				} else if (!fileDownloaded) {
					p.scrollToAndClickElement(p.openOkayButton());
					System.out.println("NEEDED TO RECLICK FOR FILE: " + nameOfFile);
				}
			}
			return fileDownloaded;
		}
	}

	public void renameCSVAndAddToMap(String nameOfFile) {
		File oldName = new File(nameOfFile);
		if (oldName.exists()) {
			String newNameText = Browser.FILE_PATH + Browser.fs
					+ Browser.classMap.get(threadId).toString().replace(".json", "") + "_" + oldName.getName();
			System.out.println("newNameText is: " + newNameText);
			File newName = new File(newNameText);
			Boolean rename = oldName.renameTo(newName);
			System.out.println(oldName + " was renamed to " + newName + " " + rename);
			addFileToCSVMap(newName);
		}
	}

	public void addFileToCSVMap(File newName) {
		List<File> list = new ArrayList<>();
		list.add(newName);
		Map<String, List<File>> map = new HashMap<>();
		map.put(Browser.classMap.get(threadId), list);
		if (!Browser.csvMap.containsKey(threadId)) {
			Browser.csvMap.put(threadId, map);
		} else if (!Browser.csvMap.get(threadId).containsKey(Browser.classMap.get(threadId))) {
			Browser.csvMap.put(threadId, map);
		} else {
			Browser.csvMap.get(threadId).get(Browser.classMap.get(threadId)).add(newName);
		}
	}

	public Boolean clickUntilFileDownloadsByDownloadButton(WatchKey watchKey, String nameOfFile) throws Exception {
		synchronized (this.getClass()) {
			boolean fileDownloaded = false;
			ChargesTab p = new ChargesTab();
			p.scrollToAndClickElement(p.openDownloadButton());
			int additionalNumberOfTimesToClick = 3;
			for (int i = 0; i <= additionalNumberOfTimesToClick; i++) {
				fileDownloaded = waitForFile(nameOfFile, watchKey);
				if (fileDownloaded) {
					System.out.println("FILE: " + nameOfFile + " DOWNLOADED");
					renameCSVAndAddToMap(Browser.FILE_PATH + Browser.fs + nameOfFile);
					break;
				} else if (!fileDownloaded) {
					p.scrollToAndClickElement(p.openDownloadButton());
					System.out.println("NEEDED TO RECLICK FOR FILE: " + nameOfFile);
				}
			}
			return fileDownloaded;
		}
	}

	/**
	 * This routine registers a watcher that looks for the creation of a file within
	 * the specified folder destination.
	 * 
	 * @return a register that looks to the folder that will contain the created
	 *         folder
	 * @throws IOException
	 */

	public static WatchKey setWatcher() throws IOException {
		WatchService watcher = FileSystems.getDefault().newWatchService();
		Path dirPath = Paths.get(Browser.FILE_PATH);
		return dirPath.register(watcher, ENTRY_CREATE);
	}

	/**
	 * waitForFile() waits and polls for the creation of a file within a specific
	 * folder location.
	 * 
	 * @param nameOfFile
	 *            = name of the csv that the watcher is looking for
	 * @param watchKey
	 *            = most commonly, WatchKey watchKey = ReadRecord.setWatcher(); This
	 *            is set in the invokeDownload () and invokeDownloadAndOK ()
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public Boolean waitForFile(String nameOfFile, WatchKey watchKey) throws IOException, InterruptedException {
		long startTime = System.currentTimeMillis();
		long twelveSec = Browser.getWaitTime() * 1000;
		Boolean isCreated = false;
		while ((System.currentTimeMillis() - startTime) < (twelveSec)) {

			for (WatchEvent<?> event : watchKey.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				@SuppressWarnings("unchecked")
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path fileName = ev.context();
				if (kind == OVERFLOW) {
					continue;
				}

				if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
					Thread.sleep(5000);
					if (fileName.toString().endsWith(".csv")) {
						isCreated = true;
						break;
					}
				}
			}
			if (isCreated)
				break;
		}
		System.out.println("FILE " + nameOfFile + " WAS CREATED: " + isCreated);
		return isCreated;
	}

	public void waitForCSVToDownload(int fileNum) {
		int addedFile = fileNum + 1;
		System.out.println("addedFile " + addedFile);
		System.out.println("new filter length " + dir.list(filterForCSV).length);

		wait.until((ExpectedCondition<Boolean>) driver -> (dir.list(filterForCSV).length == addedFile));
	}

	/*
	 * private static void waitForFileDownload(Path fileFromWatcher, String
	 * expectedFileName) throws IOException { Path path =
	 * Paths.get(Browser.FILE_PATH); File fileToCheck =path
	 * .resolve(expectedFileName) .toFile(); wait.until((ExpectedCondition<Boolean>)
	 * driver -> fileToCheck.exists());
	 * 
	 * }
	 */

	public static boolean fileDoesNotExist(File file) {
		boolean fileIsNull = !(file.exists()) || !(file.length() > 0) || file == null || file.equals(null);
		System.out.print("THE FILE " + file.toString() + " IS NULL: " + fileIsNull + "\n");
		return fileIsNull;
	}

	public List<String> listOfClassesUserWantsToRunByBAN(String className) {
		List<String> list = new ArrayList<>();
		list.add(className);
		return list;
	}

}