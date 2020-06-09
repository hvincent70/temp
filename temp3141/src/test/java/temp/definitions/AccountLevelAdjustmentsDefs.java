package com.sprint.iice_tests.definitions.cl_digital.AccountLevelAdjustments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sprint.iice_tests.definitions.DefinitionBase;
import com.sprint.iice_tests.web.pages.cl_digital_paper.ChargesTab;

import cucumber.api.java8.En;

public class AccountLevelAdjustmentsDefs extends DefinitionBase implements En {

	String[] ACCOUNT_CHARGE_DATA = { account.getTableValues().get(0).getZerothVal0(),
			account.getTableValues().get(0).getFirstVal0(), account.getTableValues().get(0).getSecondVal0(),
			account.getTableValues().get(0).getThirdVal0(), account.getTableValues().get(0).getFourthVal0(),
			account.getTableValues().get(0).getFifthVal0(), account.getTableValues().get(0).getSixthVal0(),
			account.getTableValues().get(0).getSeventhVal0(), account.getTableValues().get(0).getEighthVal0(),
			account.getTableValues().get(0).getNinthVal0(), account.getTableValues().get(0).getTenthVal0(),
			account.getTableValues().get(0).getEleventhVal0() };

	String MESSAGE = account.getHeader8();

	String[] EXPAND_BUTTONS = { account.getHeader5(), account.getHeader6(), account.getHeader7() };

	String[][] MISC_CHARGES = {
			{ account.getTableValues().get(1).getZerothVal0(), account.getTableValues().get(1).getFirstVal0() },
			{ account.getTableValues().get(1).getZerothVal1(), account.getTableValues().get(1).getFirstVal1() },
			{ account.getTableValues().get(1).getZerothVal2(), account.getTableValues().get(1).getFirstVal2() },
			{ account.getTableValues().get(1).getZerothVal3(), account.getTableValues().get(1).getFirstVal3() },
			{ account.getTableValues().get(1).getZerothVal4(), account.getTableValues().get(1).getFirstVal4() },
			{ account.getTableValues().get(1).getZerothVal5(), account.getTableValues().get(1).getFirstVal5() },
			{ account.getTableValues().get(1).getZerothVal6(), account.getTableValues().get(1).getFirstVal6() } };

	String[][] EQUIP = {
			{ account.getTableValues().get(2).getZerothVal0(), account.getTableValues().get(2).getFirstVal0() },
			{ account.getTableValues().get(2).getZerothVal1(), account.getTableValues().get(2).getFirstVal1() },
			{ account.getTableValues().get(2).getZerothVal2(), account.getTableValues().get(2).getFirstVal2() },
			{ account.getTableValues().get(2).getZerothVal3(), account.getTableValues().get(2).getFirstVal3() },
			{ account.getTableValues().get(2).getZerothVal4(), account.getTableValues().get(2).getFirstVal4() },
			{ account.getTableValues().get(2).getZerothVal5(), account.getTableValues().get(2).getFirstVal5() } };

	String[][] GOVERNMENT_TAXES = {
			{ account.getTableValues().get(3).getZerothVal0(), account.getTableValues().get(3).getFirstVal0(),
					account.getTableValues().get(3).getSecondVal0() },
			{ account.getTableValues().get(3).getZerothVal1(), account.getTableValues().get(3).getFirstVal1(),
					account.getTableValues().get(3).getSecondVal1() },
			{ account.getTableValues().get(3).getZerothVal2(), account.getTableValues().get(3).getFirstVal2(),
					account.getTableValues().get(3).getSecondVal2() },
			{ account.getTableValues().get(3).getZerothVal3(), account.getTableValues().get(3).getFirstVal3(),
					account.getTableValues().get(3).getSecondVal3() },
			{ account.getTableValues().get(3).getZerothVal4(), account.getTableValues().get(3).getFirstVal4(),
					account.getTableValues().get(3).getSecondVal4() },
			{ account.getTableValues().get(3).getZerothVal5(), account.getTableValues().get(3).getFirstVal5(),
					account.getTableValues().get(3).getSecondVal5() },
			{ account.getTableValues().get(3).getZerothVal6(), account.getTableValues().get(3).getFirstVal6() } };

	private Map<String, String[][]> createMap() {
		Map<String, String[][]> map = new HashMap<String, String[][]>();
		map.put(EXPAND_BUTTONS[0], MISC_CHARGES);
		map.put(EXPAND_BUTTONS[1], EQUIP);
		map.put(EXPAND_BUTTONS[2], GOVERNMENT_TAXES);
		return map;
	}

	private Map<String, String> createButtonAmounts() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(EXPAND_BUTTONS[0], account.getAccountCharges().getAccountChargeAmount1());
		map.put(EXPAND_BUTTONS[1], account.getAccountCharges().getAccountChargeAmount2());
		map.put(EXPAND_BUTTONS[2], account.getAccountCharges().getAccountChargeAmount3());
		return map;
	}

	String[] DATES = { account.getTableValues().get(4).getZerothVal0(), account.getTableValues().get(4).getFirstVal0(),
			account.getTableValues().get(4).getSecondVal0() };;

	String[][] AMOUNTS = {
			{ account.getTableValues().get(5).getZerothVal0(), account.getTableValues().get(5).getFirstVal0(),
					account.getTableValues().get(5).getSecondVal0() },
			{ account.getTableValues().get(5).getZerothVal1(), account.getTableValues().get(5).getFirstVal1(),
					account.getTableValues().get(5).getSecondVal1() },
			{ account.getTableValues().get(5).getZerothVal2(), account.getTableValues().get(5).getFirstVal2(),
					account.getTableValues().get(5).getSecondVal2() } };

	private Map<String, String[]> createGraphAmounts() {
		Map<String, String[]> dates = new HashMap<String, String[]>();
		dates.put(EXPAND_BUTTONS[0], AMOUNTS[0]);
		dates.put(EXPAND_BUTTONS[1], AMOUNTS[1]);
		dates.put(EXPAND_BUTTONS[2], AMOUNTS[2]);
		return dates;
	}

	public AccountLevelAdjustmentsDefs() {
		ChargesTab charges = new ChargesTab();

		Then("^Account charges data displays correctly$", () -> {
			List<String> list = charges.getAccountChargeData();
			all1_DimDataMatches(list, ACCOUNT_CHARGE_DATA, "Account charges data displays correctly");
		});

		Then("^Green popup appears with appropriate message$", () -> {
			softAssert.assertEquals(charges.getGreenPopupText(), account.getPopupContent1(),
					"Green popup appears with appropriate message");

		});

		Then("^\"([^\"]*)\" Summary line displays appropriate value$", (String expandText) -> {
			softAssert.assertEquals(charges.getAmountFromExpandText(expandText), createButtonAmounts().get(expandText),
					expandText + "Summary line displays appropriate value");

		});

		And("^\"([^\"]*)\" Charge Descriptions and amounts display appropriately$", (String arg1) -> {
			String[][] values = createMap().get(arg1);
			all2_DimDataMatches(charges.getTableDataInOpenRow(), values,
					arg1 + " Charge Descriptions and amounts display appropriately");
		});

		Then("^special message appears in open row$", () -> {
			softAssert.assertEquals(charges.messageDisplaysString(MESSAGE), MESSAGE,
					"special message appears in open row");
		});

		And("^\"([^\"]*)\" All data within the month graph displays correctly$", (String arg1) -> {
			if (charges.graphsInOpenButton() != null) {
				all1_DimDataMatches(charges.getGraphDatesInOpenRow(), DATES,
						arg1 + " All data within the month graph displays correctly date");
				all1_DimDataMatches(charges.getGraphAmountsInOpenRow(), createGraphAmounts().get(arg1),
						arg1 + " All data within the month graph displays correctly amount");
			} else {
				softAssert.assertTrue(false,
						arg1 + " All data within the month graph displays correctly graph not displayed");
			}
		});

		Then("^All verifications are tested$", () -> {
			softAssert.assertAll();
		});

	}

}
