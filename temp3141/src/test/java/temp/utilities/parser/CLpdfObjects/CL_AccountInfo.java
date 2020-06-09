package com.sprint.iice_tests.utilities.parser.CLpdfObjects;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;

public class CL_AccountInfo extends CL_PageBase{
	
	public String accountName(PDPage page) throws IOException {
		int namePosition=0;
		return accountInfo(page).get(namePosition);
	}
	
	public String accountNumber(PDPage page) throws IOException {
		int numberPosition=1;
		return accountInfo(page).get(numberPosition);
	}
	
	public String invoiceNumber(PDPage page) throws IOException {
		int invoicePosition=2;
		return accountInfo(page).get(invoicePosition);
	}
	
	public String billDate(PDPage page) throws IOException {
		int billDatePosition=1;
		return dateInfo(page).get(billDatePosition);
	}
	
	public String billPeriod(PDPage page) throws IOException {
		int billPeriodPosition=2;
		return dateInfo(page).get(billPeriodPosition);
	}
	
	public List<String> accountInfo(PDPage page) throws IOException{
		return makeRectIntoListOfString(page, ClAccountInfoRegions.ACCOUNT_NUMBERS.getRegion());
	}
	
	public List<String> dateInfo(PDPage page) throws IOException{
		return makeRectIntoListOfString(page, ClAccountInfoRegions.BILL_DATES.getRegion());
	}
	
	@Deprecated
	public List<String> billChargeTrends(PDPage page) throws IOException{
		return makeRectIntoListOfString(page, ClAccountInfoRegions.BILL_CHARGE_TRENDS.getRegion());
	}
	
	public enum ClAccountInfoRegions {

		ACCOUNT_NUMBERS(new Rectangle(370, 0, 185, 58)),

		BILL_DATES(new Rectangle(555, 0, 185, 75)),

		CHARGES_TYPE_RECT(new Rectangle(375, 375)),

		THIRD_PARTY_ROW_CELL(new Rectangle(50, 25)),

		THIRD_PARTY_ROW(new Rectangle(500, 25)),
		
		@Deprecated
		BILL_CHARGE_TRENDS(new Rectangle(0, 350, 350, 130)),;

		private Rectangle region;

		private ClAccountInfoRegions(Rectangle dimensions) {
			this.region = dimensions;
		}

		public Rectangle getRegion() {
			return region;
		}
	}
}
