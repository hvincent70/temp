package com.sprint.iice_tests.utilities.parser.CLpdfObjects;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;

public class CL_AccountOverview extends CL_PageBase {

	public static String accountOverviewSingleString(PDPage page) throws IOException {
		String getAccountNumberDetails = converRectToText(page, new Rectangle(0, 0, 800, 800));
		return getAccountNumberDetails;
	}

	public static List<String> accountOverviewList(PDPage page) throws IOException {
		List<String> listOfFirstBillInfo = makeRectStringIntoListOfStringsAccountOverview(
				accountOverviewSingleString(page));
		return listOfFirstBillInfo;
	}

	public String grabDesiredChargeTextByIndex(PDPage page, String desiredCharge) throws IOException {
		return accountOverviewList(page).get(grabIndexOfDesiredCharge(page, desiredCharge));
	}

	public int grabIndexOfDesiredCharge(PDPage page, String desiredCharge) throws IOException {
		return accountOverviewList(page).indexOf(desiredCharge);
	}

	public String grabDesiredCharge(PDPage page, String desiredCharge) throws IOException {
		return accountOverviewList(page).get(grabIndexOfDesiredCharge(page, desiredCharge));
	}

	public String grabFirstLineAfterDesiredCharge(PDPage page, String desiredCharge) throws IOException {
		int charge = accountOverviewList(page).indexOf(desiredCharge);
		int lineAfterCharge = charge + 1;
		return accountOverviewList(page).get(lineAfterCharge);
	}

	public String grabSecondLineAfterDesiredCharge(PDPage page, String desiredCharge) throws IOException {
		int charge = accountOverviewList(page).indexOf(desiredCharge);
		int secondLineAfterCharge = charge + 2;
		return accountOverviewList(page).get(secondLineAfterCharge);
	}

	public String grabThirdLineAfterDesiredCharge(PDPage page, String desiredCharge) throws IOException {
		int charge = accountOverviewList(page).indexOf(desiredCharge);
		int thirdLineAfterCharge = charge + 3;
		return accountOverviewList(page).get(thirdLineAfterCharge);
	}

	public String grabFourthLineAfterDesiredCharge(PDPage page, String desiredCharge) throws IOException {
		int charge = accountOverviewList(page).indexOf(desiredCharge);
		int fourthLineAfterCharge = charge + 4;
		return accountOverviewList(page).get(fourthLineAfterCharge);
	}

}
