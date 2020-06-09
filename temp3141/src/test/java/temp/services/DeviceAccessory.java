package com.sprint.iice_tests.lib.dao.vo;

import java.io.Serializable;

public class DeviceAccessory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String principleAmountStr;
	private String principleAmountDollarStr;
	
	public DeviceAccessory(){
	}

	public String getPrincipleAmountStr() {
		return principleAmountStr;
	}

	public void setPrincipleAmountStr(String principleAmountStr) {
		this.principleAmountStr = principleAmountStr;
	}

	public String getPrincipleAmountDollarStr() {
		return principleAmountDollarStr;
	}

	public void setPrincipleAmountDollarStr(String principleAmountDollarStr) {
		this.principleAmountDollarStr = principleAmountDollarStr;
	}

	@Override
	public String toString() {
		return "DeviceAccessory [principleAmountStr=" + principleAmountStr + ", principleAmountDollarStr="
				+ principleAmountDollarStr + "]";
	}
	
	private DeviceAccessory(DeviceAccessoryBuilder builder) {
		this.principleAmountStr=builder.principleAmountStr;
		this.principleAmountDollarStr=builder.principleAmountDollarStr;
	}
	
	
	//DeviceAccessoryBuilder
	public static class DeviceAccessoryBuilder{
		
		private String principleAmountStr;
		private String principleAmountDollarStr;
		
		public DeviceAccessoryBuilder(String principleAmountStr, String principleAmountDollarStr) {
			
			this.principleAmountStr=principleAmountStr;
			this.principleAmountDollarStr=principleAmountDollarStr;
		}
		
		public DeviceAccessory build() {
			return new DeviceAccessory(this);
		}
		
	}
}
