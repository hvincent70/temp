package com.sprint.iice_tests;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlWriter {
	private static Document doc;

//	public static void main(String[] args) {
//		createXmlFile();
//	}

	public static void createXmlFile() {

		// get props
		String testName = System.getProperty("testName", "Default Test Name");
		String suiteXmlPath = System.getProperty("suiteXmlOutputPath", "testng.xml");

		String site = "";
		String parallel = System.getProperty("parallel", "false");
		String threadCount = System.getProperty("threadCount", "1");
		String dataProviderThreadCount = System.getProperty("dataProviderThreadCount", "1");
		String configFailurePolicy = System.getProperty("configfailurepolicy","continue");
		String browser = System.getProperty("browser");

		String groups = System.getProperty("includeGroup");
		String[] includeGroups = getIncludeGroups(groups);

		// split by comma
		String[] excludeGroups = splitParams(System.getProperty("excludeGroup"));
		String[] methods = splitParams(System.getProperty("method"));
		String[] classes = splitParams(System.getProperty("class"));
		String[] packages = splitParams(System.getProperty("package"));
		String[] browsers = splitParams(System.getProperty("browser"));

		// build XML
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// elements
			doc = docBuilder.newDocument();
			
			Element suiteElement = addSuite(testName, "3", parallel, threadCount, dataProviderThreadCount, configFailurePolicy);
			Element listener = addListener(suiteElement);

			// build per browser type
			for(int i = 0; i < browsers.length; i++) {

				Element testElement = addTest(suiteElement, testName, browsers[i]);
				Element browserElement = addParameter(testElement, browsers[i]);
				Element classesElement = doc.createElement("classes");
				Element groupsElement = doc.createElement("groups");
				Element runElement = doc.createElement("run");

				if(includeGroups != null && !includeGroups[0].equals("")) {
					addIncludeGroups(testElement, groupsElement, runElement, includeGroups);
				}

				if(excludeGroups != null && !excludeGroups[0].equals("")) {
					addExcludeGroups(testElement, groupsElement, runElement, excludeGroups);
				}

				if (methods != null && !methods[0].equals("")) {
					addMethods(testElement, classesElement, methods);
				}

				if (classes != null && !classes[0].equals("")) {
					addClasses(testElement, classesElement, classes);
				}

				if (packages != null && !packages[0].equals("")) {
					addPackages(testElement, packages);
				}
			}
				saveXML(suiteXmlPath);


		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Get groups. Always add the site as first group.
	 *
	 * @param groups The comma separated groups.
	 * @return The array of groups.
	 */
	private static String[] getIncludeGroups(String groups) {
		String[] includeGroups = null;
		includeGroups = splitParams(groups);

		return includeGroups;
	}

	/**
	 * Get current directory.
	 *
	 * @return The path.
	 */
	private static String presentWorkingDirectory() {
		return System.getProperty("user.dir");
	}

	/**
	 * Add the suite tag and attributes.
	 *
	 * @param suitename Suite name.
	 * @param verbose Verbose attribute.
	 * @param parallel Parallel attribute.
	 * @param threadCount Thread count attribute.
	 * @param dpThreadCount Data provider thread attribute.
	 * @return The suite element tag.
	 */
	private static Element addSuite(String suitename, String verbose, String parallel, String threadCount, String dpThreadCount, String configFailure) {
		Element rootElement = doc.createElement("suite");

		rootElement.setAttribute("name", suitename);
		rootElement.setAttribute("verbose", verbose);
		rootElement.setAttribute("parallel", parallel);
		rootElement.setAttribute("thread-count", threadCount);
		rootElement.setAttribute("configfailurepolicy", configFailure);
//		rootElement.setAttribute("data-provider-thread-count", dpThreadCount);
		doc.appendChild(rootElement);
		return rootElement;
	}

	private static String customListener = "com.sprint.iice_tests.utilities.test_util.CustomListener";
	private static Element addListener(Element element){
		Element listenerElement = doc.createElement("listeners");
		Element rootElement = doc.createElement("listener");
		rootElement.setAttribute("class-name",customListener);
		listenerElement.appendChild(rootElement);
		element.appendChild(listenerElement);
		return listenerElement;
	}

	/**
	 * Add the test tag and attributes.
	 *
	 * @param element The parent tag.
	 * @param testName The test name.
	 * @return The test element tag.
	 */
	public static Element addTest(Element element, String testName, String browser) {
		Element test = doc.createElement("test");
		test.setAttribute("name", testName + " - " + browser);
		element.appendChild(test);
		return test;

	}

	/**
	 * Add the browser parameter and attributes.
	 *
	 * @param element The parent tag.
	 * @param browser The browser name.
	 * @return The parameter element tag.
	 */
	public static Element addParameter(Element element, String browser){
		Element parameter = doc.createElement("parameter");
		parameter.setAttribute("name", "browser");
		parameter.setAttribute("value", browser);
		element.appendChild(parameter);
		return parameter;
	}

	/**
	 * Add the include group tag.
	 *
	 * @param element The parent element.
	 * @param groupsElement The groups tag element.
	 * @param runElement The run tag element.
	 * @param groups The list of groups.
	 */
	private static void addIncludeGroups(Element element, Element groupsElement, Element runElement, String[] groups) {
		addGroups(element, groupsElement, runElement, groups, true);
	}

	/**
	 * Add the exclude group tag.
	 *
	 * @param element The parent element.
	 * @param groupsElement The groups tag element.
	 * @param runElement The run tag element.
	 * @param groups The list of groups.
	 */
	private static void addExcludeGroups(Element element, Element groupsElement, Element runElement, String[] groups) {
		addGroups(element, groupsElement, runElement, groups, false);
	}

	/**
	 * Add a group tag.
	 *
	 * @param element The parent tag.
	 * @param groups The group tag.
	 * @param runElement The run tag.
	 * @param groupNames The list of groups.
	 * @param include To include or exclude.
	 * @return The newly created node.
	 */
	private static Node addGroups(Element element, Element groups, Element runElement, String[] groupNames, boolean include) {
		groups.appendChild(runElement);

		for(int i = 0; i < groupNames.length; i++) {
			Element groupElement = doc.createElement(include ? "include" : "exclude");
			groupElement.setAttribute("name", groupNames[i]);
			runElement.appendChild(groupElement);
		}
		return element.appendChild(groups);
	}

	/**
	 * Add the method tag.
	 *
	 * @param element The parent tag.
	 * @param classesElement The classes tag.
	 * @param methods The list of methods.
	 * @return The newly created node.
	 */
	public static Node addMethods(Element element, Element classesElement, String[] methods) {

		ArrayList<String> classList = getUniqueClasses(methods);
		Map<String, ArrayList<String>> map = getMethods(classList, methods);

		// add elements
		for(Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {

			Element classElement = doc.createElement("class");
			classElement.setAttribute("name", entry.getKey());
			Element methodsElement = doc.createElement("methods");

			for(String method : entry.getValue()) {
				Element methodElement = doc.createElement("include");
				methodElement.setAttribute("name", method);
				methodsElement.appendChild(methodElement);
			}

			classElement.appendChild(methodsElement);
			classesElement.appendChild(classElement);
		}

		return element.appendChild(classesElement);
	}

	/**
	 * Add the class tag.
	 *
	 * @param element The parent element.
	 * @param classesElement The classes element.
	 * @param classNames The list of classes.
	 * @return The newly created node.
	 */
	public static Node addClasses(Element element, Element classesElement, String[] classNames) {
		for(int i = 0; i < classNames.length; i++) {
			Element classElement = doc.createElement("class");
			classElement.setAttribute("name", classNames[i]);
			classesElement.appendChild(classElement);
		}
		return element.appendChild(classesElement);
	}

	/**
	 * Add the package tag.
	 *
	 * @param element The parent element.
	 * @param packageNames THe list of package names.
	 * @return The newly created node.
	 */
	public static Node addPackages(Element element, String[] packageNames) {
		Element packages = doc.createElement("packages");
		for(int i = 0; i < packageNames.length; i++) {
			Element elementPackage = doc.createElement("package");
			elementPackage.setAttribute("name", packageNames[i]);
			packages.appendChild(elementPackage);
		}
		return element.appendChild(packages);
	}

	/**
	 * Save the XML to the specified path.
	 *
	 * @param path The path to save to.
	 */
	public static void saveXML(String path) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			
			DOMImplementation impl = doc.getImplementation();
			DocumentType doctype = impl.createDocumentType("doctype", "SYSTEM", "http://testng.org/testng-1.0.dtd");
			
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,  doctype.getSystemId());
			DOMSource source = new DOMSource(doc);
			File f = new File(path);
			System.out.println("XML getting written to: " + f.getCanonicalPath());
			StreamResult result = new StreamResult(f);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Split the params on comma.
	 *
	 * @param str The string to split.
	 * @return The split string.
	 */
	private static String[] splitParams(String str) {
		if(str != null) {
			return str.split("\\s*,\\s*");
		}
		return null;
	}

	/**
	 * Map the methods to the correct class.
	 *
	 * @param classList The list of classes.
	 * @param methods The list of methods.
	 * @return The map of packages and methods.
	 */
	private static Map<String, ArrayList<String>> getMethods(ArrayList<String> classList, String[] methods) {
		Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

		for(String clazz : classList) {
			ArrayList<String> methodsForClass = new ArrayList<String>();

			for(String method : methods) {
				String[] classMethod = method.split("#");
				if(classMethod[0].equals(clazz)) {
					methodsForClass.add(classMethod[1]);
				}
			}
			map.put(clazz, methodsForClass);
		}
		return map;
	}

	/**
	 * Get a list of unique classes.
	 *
	 * @param methods The list of fully qualified method names.
	 * @return The list of classes.
	 */
	private static ArrayList<String> getUniqueClasses(String[] methods) {
		ArrayList<String> classList = new ArrayList<String>();

		for(String method : methods) {
			String clazz = method.split("#")[0];

			if(!classList.contains(clazz)) {
				classList.add(clazz);
			}
		}
		return classList;
	}
}
