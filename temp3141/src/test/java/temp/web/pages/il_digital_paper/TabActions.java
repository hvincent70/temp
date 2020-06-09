package com.sprint.iice_tests.web.pages.il_digital_paper;

public interface TabActions {

	public void openExpandButtons(String expandText) throws InterruptedException;
	
	public void openDesiredButtonAndCloseUndesiredButtons(String nameOfPlusSign) throws Exception;

	public void scrollToPhoneNumberHeaderByItsTextFont(String elementName);

}
