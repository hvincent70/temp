package com.sprint.iice_tests.web.browser;

public enum BrowserType {
	
	CHROME			("chrome", "chromedriver.exe"),
	FIREFOX			("firefox", "geckodriver.exe"),
	MOBILE_CHROME	("mobileChrome", "chromedriver.exe"),
	IE				("ie", "IEdriverserver.exe"),
	EDGE			("edge", "MicrosoftWebDriver.exe");
	
	private String name;
	private String driverName;
	
	private BrowserType(String name, String driverName) {
		this.name=name;
		this.driverName=driverName;
	}
	
	public String getBrowserTypeName() {
		return name;
	}
	
	public String getBrowserTypeDriverName() {
		return driverName;
	}

}
