package com.sprint.iice_tests.lib.dao.vo;

import java.io.Serializable;

public class CSV_VO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String header;
	private String columnName1;
	private String value1;
	private String columnName2;
	private String value2;
	private String columnName3;
	private String value3;
	private String columnName4;
	private String value4;
	private String columnName5;
	private String value5;
	private String columnName6;
	private String value6;
	private String columnName7;
	private String value7;
	private String columnName8;
	private String value8;
	private String columnName9;
	private String value9;
	
	public String getColumnName9() {
		return columnName9;
	}

	public void setColumnName9(String columnName9) {
		this.columnName9 = columnName9;
	}

	public String getValue9() {
		return value9;
	}

	public void setValue9(String value9) {
		this.value9 = value9;
	}

	public CSV_VO() {
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getColumnName1() {
		return columnName1;
	}

	public void setColumnName1(String columnName1) {
		this.columnName1 = columnName1;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getColumnName2() {
		return columnName2;
	}

	public void setColumnName2(String columnName2) {
		this.columnName2 = columnName2;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getColumnName3() {
		return columnName3;
	}

	public void setColumnName3(String columnName3) {
		this.columnName3 = columnName3;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getColumnName4() {
		return columnName4;
	}

	public void setColumnName4(String columnName4) {
		this.columnName4 = columnName4;
	}

	public String getValue4() {
		return value4;
	}

	public void setValue4(String value4) {
		this.value4 = value4;
	}

	public String getColumnName5() {
		return columnName5;
	}

	public void setColumnName5(String columnName5) {
		this.columnName5 = columnName5;
	}

	public String getValue5() {
		return value5;
	}

	public void setValue5(String value5) {
		this.value5 = value5;
	}

	public String getColumnName6() {
		return columnName6;
	}

	public void setColumnName6(String columnName6) {
		this.columnName6 = columnName6;
	}

	public String getValue6() {
		return value6;
	}

	public void setValue6(String value6) {
		this.value6 = value6;
	}

	public String getColumnName7() {
		return columnName7;
	}

	public void setColumnName7(String columnName7) {
		this.columnName7 = columnName7;
	}

	public String getValue7() {
		return value7;
	}

	public void setValue7(String value7) {
		this.value7 = value7;
	}
	
	public String getColumnName8() {
		return columnName8;
	}

	public void setColumnName8(String columnName8) {
		this.columnName8 = columnName8;
	}

	public String getValue8() {
		return value8;
	}

	public void setValue8(String value8) {
		this.value8 = value8;
	}
	
	@Override
	public String toString() {
		return "CSV_VO [header=" + header + ", columnName1=" + columnName1 + ", value1=" + value1 + ", columnName2="
				+ columnName2 + ", value2=" + value2 + ", columnName3=" + columnName3 + ", value3=" + value3
				+ ", columnName4=" + columnName4 + ", value4=" + value4 + ", columnName5=" + columnName5 + ", value5="
				+ value5 + ", columnName6=" + columnName6 + ", value6=" + value6 + ", columnName7=" + columnName7
				+ ", value7=" + value7 + ", columnName8=" + columnName8 + ", value8=" + value8 + "]";
	}

	private CSV_VO (CSV_VOBuilder builder) {
		this.header=builder.header;
		this.columnName1=builder.columnName1;
		this.value1=builder.value1;
		this.columnName2=builder.columnName2;
		this.value2=builder.value2;
		this.columnName3=builder.columnName3;
		this.value3=builder.value3;
		this.columnName4=builder.columnName4;
		this.value4=builder.value4;
		this.columnName5=builder.columnName5;
		this.value5=builder.value5;
		this.columnName6=builder.columnName6;
		this.value6=builder.value6;
		this.columnName7=builder.columnName7;
		this.value7=builder.value7;
		this.columnName8=builder.columnName8;
		this.value8=builder.value8;
	}
	
	//CSV_VO Builder
	public static class CSV_VOBuilder{
		
		private String header;
		private String columnName1;
		private String value1;
		private String columnName2;
		private String value2;
		private String columnName3;
		private String value3;
		private String columnName4;
		private String value4;
		private String columnName5;
		private String value5;
		private String columnName6;
		private String value6;
		private String columnName7;
		private String value7;
		private String columnName8;
		private String value8;
		
		public CSV_VOBuilder(String header, String columnName1, String value1, String columnName2, String value2, String columnName3,
				String value3, String columnName4, String value4, String columnName5, String value5, String columnName6, String value6,
				String columnName7, String value7, String columnName8, String value8) {
			
			this.header=header;
			this.columnName1=columnName1;
			this.value1=value1;
			this.columnName2=columnName2;
			this.value2=value2;
			this.columnName3=columnName3;
			this.value3=value3;
			this.columnName4=columnName4;
			this.value4=value4;
			this.columnName5=columnName5;
			this.value5=value5;
			this.columnName6=columnName6;
			this.value6=value6;
			this.columnName7=columnName7;
			this.value7=value7;
			this.columnName8=columnName8;
			this.value8=value8;
			
		}
		
		public CSV_VO build() {
			return new CSV_VO(this);
		}
	}
	
}
