package com.sprint.iice_tests.feature_runners.cl_digital;

import com.sprint.iice_tests.feature_runners._DigitalFeatureRunnerBase;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features/cl_digital/AccountLevelAdjustments", glue = {
				"com.sprint.iice_tests.definitions.cl_digital.AccountLevelAdjustments",
				"com.sprint.iice_tests.definitions.cl_digital.Reusable",
				"com.sprint.iice_tests.utilities.test_util"}, plugin = {
				"pretty", "html:target/cucumber-reports/cucumber-pretty",
				"json:target/cucumber-reports/AccountLevelAdjustmentsRunner.json" })

public class AccountLevelAdjustmentsRunner extends _DigitalFeatureRunnerBase {

}
