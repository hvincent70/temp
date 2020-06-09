package com.sprint.iice_tests.lib.dao.vo;

import java.io.Serializable;
import java.util.List;

public class Account implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String ban;
	private String firstName;
	private String lastName;
	private String phone;
	private Long acctNumber;
	private String billPeriod1;
	private String billPeriod2;
	private String billPeriod3;
	private String billPeriod4;
	private String billPeriod5;
	private String billPeriod6;
	private String dateRange;
	private String year;
	private String installmentMessage;
	private List<Phone> mobileList;
	private List<EquipmentRow> equipmentRow;
	private AccountCharges accountCharges;
	private DeviceAccessory deviceAccessory;
	private PlansEquipmentAndUsage plansEquipmentAndUsage;
	private List<CallTextLogs_CallDetails> callDetailsList;
	private List<Charges_ImmediateChargesAccrued> chargesAccruedList;
	private List<Charges_QuestionMarkMessage> questionMessageList;
	private PremiumServices premiumServices;
	private ThirdPartyCharges thirdPartyCharges;
	private BillAlerts billAlerts;
	private String ptnId;
	private String disclaimer1;
	private String disclaimer2;
	private String disclaimer3;
	private PreviousActivity previousActivity;
	private String header1;
	private String header2;
	private String header3;
	private String header4;
	private String header5;
	private String header6;
	private String header7;
	private String header8;
	private String header9;
	private String header10;
	private String header11;
	private String minutes;
	private String email;
	private String totalCost;
	private List<CSV_VO> csvVoList;
	private String textNextToQuestionIcon;
	private String popupContent1;
	private String monthRange;
	private String color;
	private String textDefiningBox;	
	private String billRun;
	private String billRun2;
	private List<TableData> tableData;
	private List<TableValues> tableValues;

	public List<TableValues> getTableValues() {
		return tableValues;
	}

	public void setTableValues(List<TableValues> tableValues) {
		this.tableValues = tableValues;
	}

	public String getBillRun2() {
		return billRun2;
	}

	public void setBillRun2(String billRun2) {
		this.billRun2 = billRun2;
	}

	public List<TableData> getTableData() {
		return tableData;
	}

	public void setTableData(List<TableData> tableData) {
		this.tableData = tableData;
	}

	public String getTextDefiningBox() {
		return textDefiningBox;
	}

	public void setTextDefiningBox(String textDefiningBox) {
		this.textDefiningBox = textDefiningBox;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getMonthRange() {
		return monthRange;
	}

	public void setMonthRange(String monthRange) {
		this.monthRange = monthRange;
	}

	public String getTextNextToQuestionIcon() {
		return textNextToQuestionIcon;
	}

	public void setTextNextToQuestionIcon(String textNextToQuestionIcon) {
		this.textNextToQuestionIcon = textNextToQuestionIcon;
	}

	public String getPopupContent1() {
		return popupContent1;
	}

	public void setPopupContent1(String popupContent1) {
		this.popupContent1 = popupContent1;
	}

	public Account() {
	}
	
	public List<EquipmentRow> getEquipmentRow() {
		return equipmentRow;
	}

	public void setEquipmentRow(List<EquipmentRow> equipmentRow) {
		this.equipmentRow = equipmentRow;
	}

	public String getBan() {
		return ban;
	}

	public void setBan(String ban) {
		this.ban = ban;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getAcctNumber() {
		return acctNumber;
	}

	public void setAcctNumber(Long acctNumber) {
		this.acctNumber = acctNumber;
	}

	public String getDateRange() {
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getInstallmentMessage() {
		return installmentMessage;
	}

	public void setInstallmentMessage(String installmentMessage) {
		this.installmentMessage = installmentMessage;
	}

	public String getBillPeriod1() {
		return billPeriod1;
	}

	public void setBillPeriod1(String billPeriod1) {
		this.billPeriod1 = billPeriod1;
	}

	public String getBillPeriod2() {
		return billPeriod2;
	}

	public void setBillPeriod2(String billPeriod2) {
		this.billPeriod2 = billPeriod2;
	}

	public String getBillPeriod3() {
		return billPeriod3;
	}

	public void setBillPeriod3(String billPeriod3) {
		this.billPeriod3 = billPeriod3;
	}

	public String getBillPeriod4() {
		return billPeriod4;
	}

	public void setBillPeriod4(String billPeriod4) {
		this.billPeriod4 = billPeriod4;
	}

	public String getBillPeriod5() {
		return billPeriod5;
	}

	public void setBillPeriod5(String billPeriod5) {
		this.billPeriod5 = billPeriod5;
	}

	public String getBillPeriod6() {
		return billPeriod6;
	}

	public void setBillPeriod6(String billPeriod6) {
		this.billPeriod6 = billPeriod6;
	}

	public List<Phone> getMobileList() {
		return mobileList;
	}

	public void setMobileList(List<Phone> mobileList) {
		this.mobileList = mobileList;
	}
	
	public AccountCharges getAccountCharges() {
		return accountCharges;
	}

	public void setAccountCharges(AccountCharges accountCharges) {
		this.accountCharges = accountCharges;
	}
	
	public DeviceAccessory getDeviceAccessory() {
		return deviceAccessory;
	}

	public void setDeviceAccessory(DeviceAccessory deviceAccessory) {
		this.deviceAccessory = deviceAccessory;
	}

	public PlansEquipmentAndUsage getPlansEquipmentAndUsage() {
		return plansEquipmentAndUsage;
	}

	public void setPlansEquipmentAndUsage(PlansEquipmentAndUsage plansEquipmentAndUsage) {
		this.plansEquipmentAndUsage = plansEquipmentAndUsage;
	}

	public List<CallTextLogs_CallDetails> getCallDetailsList() {
		return callDetailsList;
	}

	public void setCallDetailsList(List<CallTextLogs_CallDetails> callDetailsList) {
		this.callDetailsList = callDetailsList;
	}

	public List<Charges_ImmediateChargesAccrued> getChargesAccruedList() {
		return chargesAccruedList;
	}

	public void setChargesAccruedList(List<Charges_ImmediateChargesAccrued> chargesAccruedList) {
		this.chargesAccruedList = chargesAccruedList;
	}

	public List<Charges_QuestionMarkMessage> getQuestionMessageList() {
		return questionMessageList;
	}

	public void setQuestionMessageList(List<Charges_QuestionMarkMessage> questionMessageList) {
		this.questionMessageList = questionMessageList;
	}

	public PremiumServices getPremiumServices() {
		return premiumServices;
	}

	public void setPremiumServices(PremiumServices premiumServices) {
		this.premiumServices = premiumServices;
	}

	public ThirdPartyCharges getThirdPartyCharges() {
		return thirdPartyCharges;
	}

	public void setThirdPartyCharges(ThirdPartyCharges thirdPartyCharges) {
		this.thirdPartyCharges = thirdPartyCharges;
	}

	public BillAlerts getBillAlerts() {
		return billAlerts;
	}

	public void setBillAlerts(BillAlerts billAlerts) {
		this.billAlerts = billAlerts;
	}

	public String getPtnId() {
		return ptnId;
	}

	public void setPtnId(String ptnId) {
		this.ptnId = ptnId;
	}

	public String getDisclaimer1() {
		return disclaimer1;
	}

	public void setDisclaimer1(String disclaimer1) {
		this.disclaimer1 = disclaimer1;
	}

	public String getDisclaimer2() {
		return disclaimer2;
	}

	public void setDisclaimer2(String disclaimer2) {
		this.disclaimer2 = disclaimer2;
	}

	public String getDisclaimer3() {
		return disclaimer3;
	}

	public void setDisclaimer3(String disclaimer3) {
		this.disclaimer3 = disclaimer3;
	}

	public PreviousActivity getPreviousActivity() {
		return previousActivity;
	}

	public void setPreviousActivity(PreviousActivity previousActivity) {
		this.previousActivity = previousActivity;
	}

	public String getHeader1() {
		return header1;
	}

	public void setHeader1(String header1) {
		this.header1 = header1;
	}

	public String getHeader2() {
		return header2;
	}

	public void setHeader2(String header2) {
		this.header2 = header2;
	}

	public String getHeader3() {
		return header3;
	}

	public void setHeader3(String header3) {
		this.header3 = header3;
	}

	public String getHeader4() {
		return header4;
	}

	public void setHeader4(String header4) {
		this.header4 = header4;
	}

	public String getHeader5() {
		return header5;
	}

	public void setHeader5(String header5) {
		this.header5 = header5;
	}

	public String getHeader6() {
		return header6;
	}

	public void setHeader6(String header6) {
		this.header6 = header6;
	}

	public String getHeader7() {
		return header7;
	}

	public void setHeader7(String header7) {
		this.header7 = header7;
	}

	public String getHeader8() {
		return header8;
	}

	public void setHeader8(String header8) {
		this.header8 = header8;
	}

	public String getHeader9() {
		return header9;
	}

	public void setHeader9(String header9) {
		this.header9 = header9;
	}

	public String getHeader10() {
		return header10;
	}

	public void setHeader10(String header10) {
		this.header10 = header10;
	}

	public String getHeader11() {
		return header11;
	}

	public void setHeader11(String header11) {
		this.header11 = header11;
	}

	public String getMinutes() {
		return minutes;
	}

	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	public List<CSV_VO> getCsvVoList() {
		return csvVoList;
	}

	public void setCsvVoList(List<CSV_VO> csvVoList) {
		this.csvVoList = csvVoList;
	}

	public String getBillRun() {
		return billRun;
	}

	public void setBillRun(String billRun) {
		this.billRun = billRun;
	}

	@Override
	public String toString() {
		return "Account [ban=" + ban + ", firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone
				+ ", acctNumber=" + acctNumber + ", billPeriod1=" + billPeriod1 + ", billPeriod2=" + billPeriod2
				+ ", billPeriod3=" + billPeriod3 + ", billPeriod4=" + billPeriod4 + ", billPeriod5=" + billPeriod5
				+ ", billPeriod6=" + billPeriod6 + ", dateRange=" + dateRange + ", year=" + year
				+ ", installmentMessage=" + installmentMessage + ", mobileList=" + mobileList + ", equipmentRow="
				+ equipmentRow + ", accountCharges=" + accountCharges + ", deviceAccessory=" + deviceAccessory
				+ ", plansEquipmentAndUsage=" + plansEquipmentAndUsage + ", callDetailsList=" + callDetailsList
				+ ", chargesAccruedList=" + chargesAccruedList + ", questionMessageList=" + questionMessageList
				+ ", premiumServices=" + premiumServices + ", thirdPartyCharges=" + thirdPartyCharges + ", billAlerts="
				+ billAlerts + ", ptnId=" + ptnId + ", disclaimer1=" + disclaimer1 + ", disclaimer2=" + disclaimer2
				+ ", disclaimer3=" + disclaimer3 + ", previousActivity=" + previousActivity + ", header1=" + header1
				+ ", header2=" + header2 + ", header3=" + header3 + ", header4=" + header4 + ", header5=" + header5
				+ ", header6=" + header6 + ", header7=" + header7 + ", header8=" + header8 + ", header9=" + header9
				+ ", header10=" + header10 + ", header11=" + header11 + ", minutes=" + minutes + ", email=" + email
				+ ", totalCost=" + totalCost + ", csvVoList=" + csvVoList + ", textNextToQuestionIcon="
				+ textNextToQuestionIcon + ", popupContent1=" + popupContent1 + ", monthRange=" + monthRange
				+ ", color=" + color + ", textDefiningBox=" + textDefiningBox + ", billRun=" + billRun + ", billRun2="
				+ billRun2 + ", tableData=" + tableData + ", tableValues=" + tableValues + "]";
	}
/*	
	Field[] fields = this.getClass().getDeclaredFields();
	StringBuilder sb = new StringBuilder();
	for(Field f: fields) {
		try {
		System.out.println(f.getName());
		f.setAccessible(true);
		System.out.println(f.get(this.getClass()));
		if(f.get(this.getClass()) != null && !Modifier.isStatic(f.getModifiers()));
			sb.append(f.getName() + ": " + f.get(this.getClass()) + "\n");
		} catch(Exception e) {}
	}
	return sb.toString();
*/
	private Account(AccountBuilder builder) {
		this.ban=builder.ban;
		this.firstName=builder.firstName;
		this.lastName=builder.lastName;
		this.phone=builder.phone;
		this.acctNumber=builder.acctNumber;
		this.dateRange=builder.dateRange;
		this.year=builder.year;
		this.installmentMessage=builder.installmentMessage;
		this.billPeriod1=builder.billPeriod1;
		this.billPeriod2=builder.billPeriod2;
		this.billPeriod3=builder.billPeriod3;
		this.billPeriod4=builder.billPeriod4;
		this.billPeriod5=builder.billPeriod5;
		this.billPeriod6=builder.billPeriod6;
		this.mobileList=builder.mobileList;
		this.accountCharges=builder.accountCharges;
		this.equipmentRow=builder.equipmentRow;
		this.deviceAccessory=builder.deviceAccessory;
		this.plansEquipmentAndUsage=builder.plansEquipmentAndUsage;
		this.callDetailsList=builder.callDetailsList;
		this.chargesAccruedList=builder.chargesAccruedList;
		this.questionMessageList=builder.questionMessageList;
		this.premiumServices=builder.premiumServices;
		this.thirdPartyCharges=builder.thirdPartyCharges;
		this.billAlerts=builder.billAlerts;
		this.ptnId=builder.ptnId;
		this.disclaimer1=builder.disclaimer1;
		this.disclaimer2=builder.disclaimer2;
		this.disclaimer3=builder.disclaimer3;
		this.previousActivity=builder.previousActivity;
		this.header1=builder.header1;
		this.header2=builder.header2;
		this.header3=builder.header3;
		this.header4=builder.header4;
		this.header5=builder.header5;
		this.header6=builder.header6;
		this.header7=builder.header7;
		this.header8=builder.header8;
		this.header9=builder.header9;
		this.header10=builder.header10;
		this.header11=builder.header11;
		this.minutes=builder.minutes;
		this.totalCost=builder.totalCost;
		this.email=builder.email;
		this.csvVoList=builder.csvVoList;
		this.billRun=builder.billRun;
		this.billRun2=builder.billRun2;
		this.tableData=builder.tableData;
	}
	
	//Account Builder
	public static class AccountBuilder{
		
		private String ban;
		private String firstName;
		private String lastName;
		private String phone;
		private Long acctNumber;
		private String dateRange;
		private String year;
		private String installmentMessage;
		private String billPeriod1;
		private String billPeriod2;
		private String billPeriod3;
		private String billPeriod4;
		private String billPeriod5;
		private String billPeriod6;
		private List<Phone> mobileList;
		private AccountCharges accountCharges;
		private DeviceAccessory deviceAccessory;
		private List<EquipmentRow> equipmentRow;
		private PlansEquipmentAndUsage plansEquipmentAndUsage;
		private List<CallTextLogs_CallDetails> callDetailsList;
		private List<Charges_ImmediateChargesAccrued> chargesAccruedList;
		private List<Charges_QuestionMarkMessage> questionMessageList;
		private PremiumServices premiumServices;
		private ThirdPartyCharges thirdPartyCharges;
		private BillAlerts billAlerts;
		private String ptnId;
		private String disclaimer1;
		private String disclaimer2;
		private String disclaimer3;
		private PreviousActivity previousActivity;
		private String header1;
		private String header2;
		private String header3;
		private String header4;
		private String header5;
		private String header6;
		private String header7;
		private String header8;
		private String header9;
		private String header10;
		private String header11;
		private String minutes;
		private String totalCost;
		private String email;
		private List<CSV_VO> csvVoList;
		private String billRun;
		private String billRun2;
		private List<TableData> tableData;
		
		public AccountBuilder(String ban, String firstName, String lastName, String phone, Long acctNumber, String billPeriod1,
				String billPeriod2, String billPeriod3, String billPeriod4, String billPeriod5, String billPeriod6,
				String installmentMessage, String dateRange, String year, List<Phone> mobileList, 				
				AccountCharges accountCharges, DeviceAccessory deviceAccessory, List<EquipmentRow> equipmentRow,
				PlansEquipmentAndUsage plansEquipmentAndUsage, List<CallTextLogs_CallDetails> callDetailsList,
				List<Charges_ImmediateChargesAccrued> chargesAccruedList, List<Charges_QuestionMarkMessage> questionMessageList,
				PremiumServices premiumServices, ThirdPartyCharges thirdPartyCharges, BillAlerts billAlerts,
				String ptnId, String disclaimer1, String disclaimer2, String disclaimer3, PreviousActivity previousActivity,
				String header1, String header2, String header3, String header4, String header5, String header6, 
				String header7, String header8, String header9, String header10, String header11, String minutes,
				String totalCost, String email, List<CSV_VO> csvVoList, String billRun, List<TableData> tableData, String billRun2) {
			
			this.ban=ban;
			this.firstName=firstName;
			this.lastName=lastName;
			this.phone=phone;
			this.acctNumber=acctNumber;
			this.dateRange=dateRange;
			this.year=year;
			this.installmentMessage=installmentMessage;
			this.billPeriod1=billPeriod1;
			this.billPeriod2=billPeriod2;
			this.billPeriod3=billPeriod3;
			this.billPeriod4=billPeriod4;
			this.billPeriod5=billPeriod5;
			this.billPeriod6=billPeriod6;
			this.mobileList=mobileList;
			this.accountCharges=accountCharges;
			this.equipmentRow=equipmentRow;
			this.deviceAccessory=deviceAccessory;
			this.plansEquipmentAndUsage=plansEquipmentAndUsage;
			this.callDetailsList=callDetailsList;
			this.chargesAccruedList=chargesAccruedList;
			this.questionMessageList=questionMessageList;
			this.premiumServices=premiumServices;
			this.thirdPartyCharges=thirdPartyCharges;
			this.billAlerts=billAlerts;
			this.ptnId=ptnId;
			this.disclaimer1=disclaimer1;
			this.disclaimer2=disclaimer2;
			this.disclaimer3=disclaimer3;
			this.previousActivity=previousActivity;
			this.header1=header1;
			this.header2=header2;
			this.header3=header3;
			this.header4=header4;
			this.header5=header5;
			this.header6=header6;
			this.header7=header7;
			this.header8=header8;
			this.header9=header9;
			this.header10=header10;
			this.header11=header11;
			this.minutes=minutes;
			this.totalCost=totalCost;
			this.email=email;
			this.csvVoList=csvVoList;
			this.billRun=billRun;
			this.billRun=billRun2;
			this.tableData=tableData;
		}
		
		public Account build() {
			return new Account(this);
		}
	}
}


