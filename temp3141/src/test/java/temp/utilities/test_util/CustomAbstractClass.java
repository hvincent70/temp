package com.sprint.iice_tests.utilities.test_util;

import java.io.File;
import java.io.FileWriter;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cucumber.api.testng.AbstractTestNGCucumberTests;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.PickleEventWrapper;
import cucumber.api.testng.TestNGCucumberRunner;

/**
 * Runs each cucumber scenario found in the features as separated test
 */
public abstract class CustomAbstractClass extends AbstractTestNGCucumberTests {
	private TestNGCucumberRunner testNGCucumberRunner;
	File file = new File("C:\\ProgramData\\Eclipse\\eclipse-workspace\\eclipse-workspace\\UYV\\ClassName.txt");

	@BeforeClass(alwaysRun = true)
	public void setUpClass() throws Exception {
		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
	}

	@Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
	public void runScenario(PickleEventWrapper pickleWrapper, CucumberFeatureWrapper featureWrapper) throws Throwable {
		// the 'featureWrapper' parameter solely exists to display the feature file in a
		// test report'
		FileWriter fw = new FileWriter(file, true);
		fw.append(pickleWrapper.toString() + testNGCucumberRunner.toString() + System.lineSeparator());
		fw.append(featureWrapper.toString() + pickleWrapper.getPickleEvent().uri + System.lineSeparator());
		for (int i = 0; i < 5; i++) {
			try {
				testNGCucumberRunner.runScenario(pickleWrapper.getPickleEvent());
			} catch (Exception NullPointerException) {
				System.out.println(this.getClass().getSimpleName() + " Needs a timeout");
				fw.write(this.getClass().getSimpleName() + " Needs a timeout" + System.lineSeparator());
				Thread.sleep(5000);
			}
		}
		fw.close();
	}

	/**
	 * Returns two dimensional array of PickleEventWrapper scenarios with their
	 * associated CucumberFeatureWrapper feature.
	 *
	 * @return a two dimensional array of scenarios features.
	 */
	@DataProvider
	public Object[][] scenarios() {
		if (testNGCucumberRunner == null) {
			return new Object[0][0];
		}
		return testNGCucumberRunner.provideScenarios();
	}

	@AfterClass(alwaysRun = true)
	public void tearDownClass() throws Exception {
		if (testNGCucumberRunner == null) {
			return;
		}
		testNGCucumberRunner.finish();
	}
}
