package com.sprint.iice_tests.feature_runners;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.sprint.iice_tests.lib.dao.data.Constants;
import com.sprint.iice_tests.lib.dao.vo.Account;
import com.sprint.iice_tests.utilities.test_util.DataFileFinder;
import com.sprint.iice_tests.web.browser.Browser;

public class _DigitalFeatureRunnerBase extends _FeatureRunnerBase {
	
	String logConfigPath = new File(Constants.PROPERITES_PATH).getAbsolutePath();
	private final Logger logger = LogManager.getLogger(this.getClass());
	Browser browser = new Browser();
	
	
	@Parameters({ "browser", "device", "site", "remoteDriverEnabled", "runHeadless"})
	@BeforeClass(alwaysRun = true)
	public void beforeClass(String testBrowser, @Optional("device") String device, String site,
			@Optional("remoteDriverEnabled") Boolean remoteDriverEnabled, @Optional("runHeadless") Boolean runHeadless) 
	
			throws IOException, InterruptedException {


		synchronized (this.getClass()) {
			long threadId = Thread.currentThread().getId();
			final String path = DataFileFinder.getJsonTestData(this.getClass());
			Account account = accountService.getAccount(path);
			currentTest = this.getClass().getName();
			System.out.println("SIMPLE NAME: "+this.getClass().getSimpleName().toString());
			logger.info("Setting up browser for " + this);
			browser.getWebDriver(testBrowser, account.getBan(), device, site, remoteDriverEnabled, runHeadless);
			Browser.banMap.put(threadId, account.getBan());
			logger.info("BAN number: " + account.getBan());
			Browser.classMap.put(threadId, Browser.convertClassToJSON(this.getClass()));
			Browser.accountMap.put(threadId, account);
			Browser.quitTestIfBanIsDown(site);
		}
	}
	

	@AfterTest(alwaysRun = true)
	public void afterClass() throws InterruptedException, IOException {
		synchronized (this.getClass()) {
			logger.info("Quitting browser...");
		}
	}

}