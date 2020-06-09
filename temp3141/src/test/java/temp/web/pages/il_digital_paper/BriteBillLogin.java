package com.sprint.iice_tests.web.pages.il_digital_paper;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sprint.iice_tests.config.Config;
import com.sprint.iice_tests.config.Config.PROPERTIES;
import com.sprint.iice_tests.web.browser.Browser;

public class BriteBillLogin extends TabBase{
	
	private final By EMAIL_FIELD = By.cssSelector("#username");
	private final By PASSWORD_FIELD = By.cssSelector("#password");
	private final By LOGIN_BUTTON = By.cssSelector(".bb-btn-primary");
	
	private final By loginButtonUAT1Loc = By.xpath("//button[text()='Log in']");

	public WebElement getEmailField() {
		return retryElementUntilPresent(EMAIL_FIELD);
	}
	
	public WebElement getPasswordField() {
		return retryElementUntilPresent(PASSWORD_FIELD);
	}
	
	public WebElement getLoginButtonUAT1() {
		return retryElementUntilPresent(loginButtonUAT1Loc);
	}
	
	public WebElement getLoginButtonUAT2() {
		return retryElementUntilPresent(LOGIN_BUTTON);
	}
	
	synchronized public void login() throws InterruptedException, IOException {
		try {
			System.out.println("IN LOGIN");
			getEmailField().sendKeys(Config.getProperty(PROPERTIES.USER));
			getPasswordField().sendKeys(Config.getProperty(PROPERTIES.PASSWORD));
			if(Browser.runningOnUAT1()) {
			resilientClick(getLoginButtonUAT1());}
			else if(Browser.runningOnUAT2()||Browser.runningOn039()||Browser.runningOn037()) {
				resilientClick(getLoginButtonUAT2());
			}
			System.out.println("LAST STEP IN LOGIN");
		} catch(Exception e) {}
	}

}
