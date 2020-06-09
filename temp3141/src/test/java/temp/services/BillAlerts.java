package com.sprint.iice_tests.lib.dao.vo;

public class BillAlerts {

	private static final long serialVersionUID = 1L;
	
	private String disclaimerText;
	private String billAlert1;
	private String billAlert2;
	private String billAlert3;
	private String billAlert4;
	
	public BillAlerts() {
	}

	public String getDisclaimerText() {
		return disclaimerText;
	}

	public void setDisclaimerText(String disclaimerText) {
		this.disclaimerText = disclaimerText;
	}

	public String getBillAlert1() {
		return billAlert1;
	}

	public void setBillAlert1(String billAlert1) {
		this.billAlert1 = billAlert1;
	}

	public String getBillAlert2() {
		return billAlert2;
	}

	public void setBillAlert2(String billAlert2) {
		this.billAlert2 = billAlert2;
	}

	public String getBillAlert3() {
		return billAlert3;
	}

	public void setBillAlert3(String billAlert3) {
		this.billAlert3 = billAlert3;
	}

	public String getBillAlert4() {
		return billAlert4;
	}

	public void setBillAlert4(String billAlert4) {
		this.billAlert4 = billAlert4;
	}
	
	@Override
	public String toString() {
		return "BillAlerts [disclaimerText=" + disclaimerText + ", billAlert1=" + billAlert1 + ", billAlert2="
				+ billAlert2 + ", billAlert3=" + billAlert3 + ", billAlert4=" + billAlert4 + "]";
	}

	private BillAlerts(BillAlertsBuilder builder) {
		this.disclaimerText=builder.disclaimerText;
		this.billAlert1=builder.billAlert1;
		this.billAlert2=builder.billAlert2;
		this.billAlert3=builder.billAlert3;
		this.billAlert4=builder.billAlert4;
	}
	
	//BillAlerts Builder
	public static class BillAlertsBuilder{
		
		private String disclaimerText;
		private String billAlert1;
		private String billAlert2;
		private String billAlert3;
		private String billAlert4;
		
		public BillAlertsBuilder(String disclaimerText, String billAlert1, String billAlert2, String billAlert3, String billAlert4) {
			
			this.disclaimerText=disclaimerText;
			this.billAlert1=billAlert1;
			this.billAlert2=billAlert2;
			this.billAlert3=billAlert3;
			this.billAlert4=billAlert4;
		}
		
		public BillAlerts build() {
			return new BillAlerts(this);
		}
	}
	
}

