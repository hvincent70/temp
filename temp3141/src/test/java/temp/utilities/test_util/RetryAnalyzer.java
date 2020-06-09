package com.sprint.iice_tests.utilities.test_util;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

	int counter = 0;
	int retryLimit = 2;
	
	
	@Override
	public boolean retry(ITestResult result) {
		if(!result.isSuccess()) {
			if(counter < retryLimit) {
				counter++;
				result.setStatus(ITestResult.FAILURE);
				return true;
			} else {
				result.setStatus(ITestResult.FAILURE);
			}
		} else {
			result.setStatus(ITestResult.SUCCESS);
		}
		return false;
	}
		
}
