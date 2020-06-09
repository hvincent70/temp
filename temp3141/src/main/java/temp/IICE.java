package com.sprint.iice_tests;

import java.io.IOException;

public class IICE {
	

	public static void main(String[] args) throws IOException, InterruptedException {
		
		DataDriveRunConfig.addTestClassToSysProp();
		
		XmlWriter.createXmlFile();
		

	}

}