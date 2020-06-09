package com.sprint.iice_tests.utilities.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;



public class WordDocumentParser {

	public static List<String> readSimpleWordDocument(String path) throws InvalidFormatException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
		String[] lines =  new XWPFWordExtractor(xdoc).getText().split("\n");
		List<String> docLineList = new ArrayList<>();
		for(String line:lines) {
			docLineList.add(line);
		}
		return docLineList;
	}
	
	public static List<String> readSimpleHeaderWordDocument(String path) throws InvalidFormatException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
		XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(xdoc);
		
		XWPFHeader header = policy.getDefaultHeader();
		List<String> docLineList = new ArrayList<>();
		if(header != null) {
			String text = header.getText();
			String[] lines = text.split("\n");
			for(String line:lines) {
				docLineList.add(line);
			}
		}
		return docLineList;
	}
	
	public static List<String> readSimpleFooterWordDocument(String path) throws InvalidFormatException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
		XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(xdoc);
		
		XWPFFooter footer = policy.getDefaultFooter();
		List<String> docLineList = new ArrayList<>();
		if(footer != null) {
			String text = footer.getText();
			String[] lines = text.split("\n");
			for(String line:lines) {
				docLineList.add(line);
			}
		}
		return docLineList;
	}
	
	
	
}
