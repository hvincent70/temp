package com.sprint.iice_tests.utilities.parser.CLpdfObjects;

public class PageNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public PageNotFoundException(String condition) {
		super ("Could not find desired page object in this document.  \n" +
				"Looking for start position defined by the string " + condition + "\n" +
				"is it possible the pdf has an error in the name of the section? \n" +
				"If not, should a different condition be used?");
	}
	
}
