package com.sprint.iice_tests.lib.dao.vo;

import java.io.Serializable;

public class CallTextLogs_CallDetails implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String onColumn;
	private String toFrom;
	private String destination;
	private String rate;
	private String mins;
	private String cost;
	
	public CallTextLogs_CallDetails() {
	}

	public String getOnColumn() {
		return onColumn;
	}

	public void setOnColumn(String onColumn) {
		this.onColumn = onColumn;
	}
	
	public String getToFrom() {
		return toFrom;
	}

	public void setToFrom(String toFrom) {
		this.toFrom = toFrom;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getMins() {
		return mins;
	}

	public void setMins(String mins) {
		this.mins = mins;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "CallTextLogs_CallDetails [onColumn=" + onColumn + ", toFrom=" + toFrom + ", destination=" + destination
				+ ", rate=" + rate + ", mins=" + mins + ", cost=" + cost + "]";
	}

	private CallTextLogs_CallDetails(CallTextLogs_CallDetailsBuilder builder) {
		
		this.onColumn=builder.onColumn;
		this.toFrom=builder.toFrom;
		this.destination=builder.destination;
		this.rate=builder.rate;
		this.mins=builder.mins;
		this.cost=builder.cost;
	}
	
	//CallTextLogs_CallDetailsBuilder
	public static class CallTextLogs_CallDetailsBuilder{
		
		private String onColumn;
		private String toFrom;
		private String destination;
		private String rate;
		private String mins;
		private String cost;
		
		public CallTextLogs_CallDetailsBuilder(String onColumn, String toFrom, String destination, String rate,
				String mins, String cost) {
			
			this.onColumn=onColumn;
			this.toFrom=toFrom;
			this.destination=destination;
			this.rate=rate;
			this.mins=mins;
			this.cost=cost;
		}
		
		public CallTextLogs_CallDetails build() {
			return new CallTextLogs_CallDetails(this);
		}
	}

}
