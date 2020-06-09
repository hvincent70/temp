package com.sprint.iice_tests.lib.dao.vo;

import java.io.Serializable;

public class Charges_QuestionMarkMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String line;
	
	public Charges_QuestionMarkMessage() {
	}
	
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "Charges_QuestionMarkMessage [line=" + line + "]";
	}

	private Charges_QuestionMarkMessage(Charges_QuestionMarkMessageBuilder builder) {
		this.line=builder.line;
	}
	
	//Charges_QuestionMarkMessage Builder
	public static class Charges_QuestionMarkMessageBuilder{
		
		private String line;
		
		public Charges_QuestionMarkMessageBuilder(String line) {
			this.line=line;
		}
		
		public Charges_QuestionMarkMessage build() {
			return new Charges_QuestionMarkMessage(this);
		}
	}
	
	
}
