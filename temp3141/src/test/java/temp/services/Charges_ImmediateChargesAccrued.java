package com.sprint.iice_tests.lib.dao.vo;

import java.io.Serializable;

public class Charges_ImmediateChargesAccrued implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String chargeAccruedText;
	private String chargeAccruedPrice;
	
	public Charges_ImmediateChargesAccrued() {
	}

	public String getChargeAccruedText() {
		return chargeAccruedText;
	}

	public void setChargeAccruedText(String chargeAccruedText) {
		this.chargeAccruedText = chargeAccruedText;
	}

	public String getChargeAccruedPrice() {
		return chargeAccruedPrice;
	}

	public void setChargeAccruedPrice(String chargeAccruedPrice) {
		this.chargeAccruedPrice = chargeAccruedPrice;
	}

	@Override
	public String toString() {
		return "Charges_ImmediateChargesAccrued [chargeAccruedText=" + chargeAccruedText + ", chargeAccruedPrice="
				+ chargeAccruedPrice + "]";
	}
	
	private Charges_ImmediateChargesAccrued(Charges_ImmediateChargesAccruedBuilder builder) {
		this.chargeAccruedText=builder.chargeAccruedText;
		this.chargeAccruedPrice=builder.chargeAccruedPrice;
	}
	
	
	//Charges_ImmediateChargesAccrued Builder
	public static class Charges_ImmediateChargesAccruedBuilder{
		
		private String chargeAccruedText;
		private String chargeAccruedPrice;
		
		public Charges_ImmediateChargesAccruedBuilder(String chargeAccruedText, String chargeAccruedPrice) {
			this.chargeAccruedText=chargeAccruedText;
			this.chargeAccruedPrice=chargeAccruedPrice;
		}
		
		public Charges_ImmediateChargesAccrued build() {
			return new Charges_ImmediateChargesAccrued(this);
		}
	}

}
