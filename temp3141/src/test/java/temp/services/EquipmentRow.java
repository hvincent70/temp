package com.sprint.iice_tests.lib.dao.vo;

public class EquipmentRow {
	
	private static final long serialVersionUID = 1L;
	
	private String equipmentName;
	private String message1;
	private String finalPaymentDue;
	private String paymentsRemaining;
	private String payoffAmount;

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getMessage1() {
		return message1;
	}

	public void setMessage1(String message1) {
		this.message1 = message1;
	}

	public String getFinalPaymentDue() {
		return finalPaymentDue;
	}

	public void setFinalPaymentDue(String finalPaymentDue) {
		this.finalPaymentDue = finalPaymentDue;
	}

	public String getPaymentsRemaining() {
		return paymentsRemaining;
	}

	public void setPaymentsRemaining(String paymentsRemaining) {
		this.paymentsRemaining = paymentsRemaining;
	}

	public String getPayoffAmount() {
		return payoffAmount;
	}

	public void setPayoffAmount(String payoffAmount) {
		this.payoffAmount = payoffAmount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public EquipmentRow() {
	}

	@Override
	public String toString() {
		return "EquipmentRow [equipmentName=" + equipmentName + ", message1=" + message1 + ", finalPaymentDue="
				+ finalPaymentDue + ", paymentsRemaining=" + paymentsRemaining + ", payoffAmount=" + payoffAmount + "]";
	}
	
	private EquipmentRow(EquipmentRowBuilder builder) {
		this.equipmentName=builder.equipmentName;
		this.message1=builder.message1;
		this.finalPaymentDue=builder.finalPaymentDue;
		this.paymentsRemaining=builder.paymentsRemaining;
		this.payoffAmount=builder.payoffAmount;
	}
	
	//Equipment Row builder
	public static class EquipmentRowBuilder{
		
		private String equipmentName;
		private String message1;
		private String finalPaymentDue;
		private String paymentsRemaining;
		private String payoffAmount;
		
		public EquipmentRowBuilder(String equipmentName, String message1, String finalPaymentDue, String paymentsRemaining,
				String payoffAmount) {
			this.equipmentName=equipmentName;
			this.message1=message1;
			this.finalPaymentDue=finalPaymentDue;
			this.paymentsRemaining=paymentsRemaining;
			this.payoffAmount=payoffAmount;
		}
		
		public EquipmentRow build() {
			return new EquipmentRow(this);
		}
	}

}
