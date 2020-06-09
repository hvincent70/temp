package com.sprint.iice_tests.utilities.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.sprint.iice_tests.utilities.parser.ILpdfObjects.PDFRegions;

public class PDFParser {
	final static String DOWNLOAD_FOLDER = "/src/test/resources/TestData";
	static File dir = new File(System.getProperty("user.dir") + DOWNLOAD_FOLDER);

	// Apache PDFBox Parsing
	public static List<String> parsePDF(String path) {
		List<String> textList = new ArrayList<>();
		try (PDDocument doc = PDDocument.load(new File(path))) {
			doc.getClass();
			if (!doc.isEncrypted()) {
				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);

				PDFTextStripper textStripper = new PDFTextStripper();

				String text = textStripper.getText(doc);

				String[] lines = text.split("\\r?\\n");
				for (String line : lines) {
					textList.add(line);
				}
				doc.close();
			}
		} catch (InvalidPasswordException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return textList;
	}

	static FilenameFilter filter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.matches("[\\d]*[-][\\d]*[-][\\d]*[-][\\d]*.pdf") ||
					name.matches("[\\d]*[-][\\d]*[-][\\d]*[-][\\d]*.*\\.pdf");
		}
	};

	public static String grabDoc() throws InvalidPasswordException, IOException, InterruptedException {
		int i = 0;
		while (dir.list(filter).length == 0 && i < 500) {
			Thread.sleep(50);
		}
		String[] list = dir.list(filter);
		try {
			File file = new File(dir + "\\" + list[0]);
			return file.getAbsolutePath().toString();
		}catch(Exception e) {
			return "";
		}
	}

	public static List<String> parseFilteredPDF() throws InterruptedException {
		String[] list = dir.list(filter);
		File file = new File(dir + "\\" + list[0]);
		List<String> textList = new ArrayList<>();
		Thread.sleep(2000);
		try (PDDocument doc = PDDocument.load(file)) {
			doc.getClass();
			if (!doc.isEncrypted()) {
				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);

				PDFTextStripper textStripper = new PDFTextStripper();

				String text = textStripper.getText(doc);

				String[] lines = text.split("\\r?\\n");
				for (String line : lines) {
					textList.add(line);
				}
				doc.close();
			}
		} catch (InvalidPasswordException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return textList;
	}

	public static void deleteFilteredPDF() {
		String[] list = dir.list(filter);
		try {
			File file = new File(dir + "\\" + list[0]);
			file.delete();
		} catch (Exception e) {
		}
	}

	public static String parsePDFToText(String path) {
		String text = null;
		try (PDDocument doc = PDDocument.load(new File(path))) {
			doc.getClass();
			if (!doc.isEncrypted()) {
				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);

				PDFTextStripper textStripper = new PDFTextStripper();

				text = textStripper.getText(doc);
				doc.close();
			}
		} catch (InvalidPasswordException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	// IText Parsing
	public static List<String> parsePDFIText(String path) {
		PdfReader reader;
		List<String> textList = new ArrayList<>();
		try {
			reader = new PdfReader(path);

			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				textList.add(PdfTextExtractor.getTextFromPage(reader, i));
			}

			reader.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return textList;
	}

	public static void writeAndRotatePDFPage(String source, String destination) throws DocumentException, IOException {
		PdfReader reader = new PdfReader(source);
		PdfDictionary page;
		PdfNumber rotate;
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			page = reader.getPageN(i);
			rotate = page.getAsNumber(PdfName.ROTATE);
			if (rotate == null) {
				page.put(PdfName.ROTATE, new PdfNumber(90));
			} else {
				page.put(PdfName.ROTATE, new PdfNumber((rotate.intValue() + 90) % 360));
			}
		}
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destination));
		stamper.close();
		reader.close();
	}

	// TODO: Work on this, currently brings back all pages still
	public static void writeAndRotatePDFPage(String source, String destination, String pageNumber)
			throws IOException, DocumentException {
		Integer number = Integer.parseInt(pageNumber);
		PdfReader reader = new PdfReader(source);
		PdfNumber rotate;
		PdfDictionary page = reader.getPageN(number);
		rotate = page.getAsNumber(PdfName.ROTATE);
		if (rotate == null) {
			page.put(PdfName.ROTATE, new PdfNumber(90));
		} else {
			page.put(PdfName.ROTATE, new PdfNumber((rotate.intValue() + 90) % 360));
		}
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destination));
		stamper.close();
		reader.close();
	}

	public static void rotateSinglePDFPage(PDPage page, int degreesOfRotation) {
		page.setRotation(degreesOfRotation);
	}

	// public static Rectangle createCellOnPage(PDPage page) throws IOException {
	// // PDFTextStripperByArea stripper = new PDFTextStripperByArea();
	// // stripper.setSortByPosition(true);
	// //
	// // Rectangle singleCell = new Rectangle(500, 500, 500, 1000);
	//
	// Rectangle singleCell =
	// PDFRegions.Regions_For_IL.LEFT_SIDE_OF_CHARGE_TYPES_PG.getRegion();
	// // Rectangle dateAndTime = new Rectangle(100,300);
	//
	// Rectangle date = new Rectangle(60, 300);
	//
	// Rectangle small = new
	// Rectangle(PDFRegions.Regions_For_IL.LEFT_SIDE_OF_CHARGE_TYPES_PG.getRegion().x+80,PDFRegions.Regions_For_IL.LEFT_SIDE_OF_CHARGE_TYPES_PG.getRegion().x+170,
	// 80, 15);
	//
	//
	// String text = PDFRegions.getRectWithinBigRectangle(page,
	// PDFRegions.Regions_For_IL.LEFT_SIDE_OF_CHARGE_TYPES_PG.getRegion(),
	// small);
	// // stripper.addRegion("class1", singleCell);
	// // stripper.addRegion("class1", unionRect);
	// // stripper.extractRegions(page);
	//
	// // String string = stripper.getTextForRegion("class1");
	// System.out.println("TEXT IN THE RECTANGLE:\n" + text + "\nEND OF RECTANGLE");
	// System.out.println("GET TEXT FOR REGION:\n" + text + "\nEND OF TEXT FOR
	// REGION");
	// return singleCell;
	// }

	public static PDDocument doc(String path) throws IOException {
		PDDocument doc = PDDocument.load(new File(path));
		PDFRegions.pdInstances.add(doc);
		return doc;
	}

	public static PDDocument docToDelete(File path) throws IOException {
		return PDDocument.load(path);
	}

	public static PDPage desiredPDFPage(PDDocument doc, int pageNum) {
		return doc.getPage(pageNum);
	}
}
