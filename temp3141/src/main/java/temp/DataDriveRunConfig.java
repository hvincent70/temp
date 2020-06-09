package com.sprint.iice_tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class DataDriveRunConfig {
	public final static String fs = System.getProperty("file.separator");
	public final static String SYSTEM_DOWNLOAD_FOLDER = fs + "src" + fs + "test" + fs + "resources" + fs + "TestData";
	public final static String FILE_PATH = System.getProperty("user.dir") + SYSTEM_DOWNLOAD_FOLDER;
	

	/*
	 * The following routine will run tests by their ban or bill run, if the tester
	 * sets the system property typeOfTest in their run config.
	 * 
	 * typeOfTest should be either: cl_digital, cl_paper, il_digital, or il_paper.
	 * 
	 * Refer to getTestByJsonKey(String jsonFile, String key, String value, String
	 * typeOfTest) for why we need to use the system property typeOfTest instead of
	 * setting the run config to run all the tests within a package.
	 * 
	 * We do not want to set the package system property because we want to run
	 * specific tests by their ban or bill run (by setting the class system
	 * property).
	 * 
	 * Run config example for running test by ban: -DtypeOfTest=il_digital,
	 * -Dban=737137345
	 * 
	 * Run config example for running test by billRun: -DtypeOfTest=cl_paper,
	 * -DUAT2BillRun=201808-28-01-43
	 */

	public static void addTestClassToSysProp() throws FileNotFoundException, InterruptedException {

		if (System.getProperties().containsKey("typeOfTest")) {

			String testType = System.getProperty("typeOfTest");
			String jsonFilePath = FILE_PATH + fs + "json" + fs + testType;

			if (System.getProperties().containsKey("ban")) {
				addClassToSystemPropsByKey(jsonFilePath, "ban", System.getProperty("ban"), testType);
			}

			if (System.getProperties().containsKey("UAT1BillRun")) {
				addClassToSystemPropsByKey(jsonFilePath, "billRun", System.getProperty("UAT1BillRun"), testType);
			}

			if (System.getProperties().containsKey("UAT2BillRun")) {
				addClassToSystemPropsByKey(jsonFilePath, "billRun2", System.getProperty("UAT2BillRun"), testType);
			}
		}
	}

	public static void addClassToSystemPropsByKey(String dir, String key, String value, String typeOfTest)
			throws InterruptedException, FileNotFoundException {
		addClassToSystemPropsByItsJsonKey(dir, key, value, typeOfTest, jsonGrabbingForKey);
	}

	static QuadPredicate<Object, Object, Object, Object, Boolean> jsonGrabbingForKey = (Object jsonFile, Object jsonKey,
			Object jsonValue, Object typeOfTest) -> {
		Boolean jsonByKey = false;
		try {
			jsonByKey = getTestByJsonKeys(jsonFile.toString(), jsonKey.toString(), jsonValue.toString(),
					typeOfTest.toString());
			return jsonByKey;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonByKey;
	};

	public static Boolean getTestByJsonKeys(String jsonFile, String jsonKey, String jsonValue, String typeOfTest)
			throws FileNotFoundException {
		return getTestByJsonKey(jsonFile, jsonKey, jsonValue, typeOfTest);
	}

	@FunctionalInterface
	interface QuadPredicate<S, T, U, V, W> {
		W compute(S s, T t, U u, V v);

		default void getTestByJsonKey(S s, T t, U u, V v, W w) {

		}
	}

	public static void addClassToSystemPropsByItsJsonKey(String dir, String key, String value, String typeOfTest,
			QuadPredicate<Object, Object, Object, Object, Boolean> predicate)
			throws InterruptedException, FileNotFoundException {
		File testDir = new File(dir);

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean testAgainstName = filterForJsonFile.test(name);
				return testAgainstName;
			}
		};
		Thread.sleep(2000);
		String[] listOfFilesWeWantToModifyInDir = testDir.list(filter);
		for (String fileName : listOfFilesWeWantToModifyInDir) {
			String testname = testDir.toString() + "\\"
					+ listOfFilesWeWantToModifyInDir[findIndexOfPrimArray(fileName, listOfFilesWeWantToModifyInDir)];
			// System.out.println("this is the file name: " + fileName + " and its full name
			// is " + testname);

			predicate.compute(testname, key, value, typeOfTest);

		}
	}

	static Predicate<String> filterForJsonFile = file -> file.endsWith(".json");

	public static Boolean getTestByJsonKey(String jsonFile, String key, String value, String typeOfTest)
			throws FileNotFoundException {
		Boolean jsonHasValue = false;

		String simpleClassName = jsonFile.substring(jsonFile.lastIndexOf(fs) + 1, (jsonFile.indexOf(".json")));
		String className = "com.sprint.iice_tests.feature_runners." + typeOfTest + "." + simpleClassName;
		// System.out.println("className: ".toUpperCase() + className);
		@SuppressWarnings("resource")
		List<String> lines = new BufferedReader(new FileReader(jsonFile)).lines()
				.map(x -> x.toString().replaceAll(" ", "").trim()).collect(Collectors.toList());
		// System.out.println("LINES: " + lines);
		// System.out.println("DESIRED KEY "+key+"\nDESIRED VALUE: "+"\""+value+"\"");

		String keyLine = lines.stream().filter(line -> line.contains("\"" + key + "\"")).findFirst().get();
		int offsetToJsonValue = 2;
		int startOfValue = keyLine.indexOf(":") + offsetToJsonValue ;
		int endOfValue = keyLine.lastIndexOf("\"");
		String valueFromJson = keyLine.substring(startOfValue, endOfValue);

		if (value.equals(valueFromJson)) {
			jsonHasValue = true;
			// System.out.println(key+" FROM JSON: " + keyFromJson +"\n"+key+" FROM USER: "
			// + value);
			if (System.getProperties().containsKey("class")) {
				String classProp = System.getProperty("class") + ", " + className;
				System.setProperty("class", classProp);

			} else {
				System.setProperty("class", className);
			}
		}
		// System.out.println(key+" FROM JSON: " + keyFromJson +"\n"+key+" FROM USER: "
		// + value);
		return jsonHasValue;
	}

	public static int findIndexOfPrimArray(String targetInArray, String[] arrayName) {
		return IntStream.range(0, arrayName.length).filter(i -> targetInArray == arrayName[i]).findFirst().orElse(-1);
		// return -1 if target is not found
	}

	public static void printSysProps() {
		java.util.Properties props = System.getProperties();
		System.out.println("The system properties are:\n");
		for (String p : props.stringPropertyNames()) {
			System.out.println(p + "=" + System.getProperty(p));
		}
	}

}