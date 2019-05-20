package com.ibmexico.libraries;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import com.ibmexico.configurations.GeneralConfiguration;


public class Formats {

	private GeneralConfiguration objGeneralConfiguration = GeneralConfiguration.getInstance();
	
	private static Formats instance = new Formats();

	//CONSTRUCTOR
	private Formats(){
		
	}
	
	public static Formats getInstance() {
		return instance;
	}
	
	public LocalDateTime toLocalDateTime(String strLocalDateTime) {
		
		LocalDateTime objLocalDateTime = null;
		
		System.out.println("1 - " + strLocalDateTime);
		System.out.println("2 - " + objGeneralConfiguration.getPatternDateTimeNatural());
		if(Pattern.matches(objGeneralConfiguration.getPatternDateTimeNatural(), strLocalDateTime + " Hrs.")) {
			objLocalDateTime = LocalDateTime.parse(strLocalDateTime.replace(" Hrs.", ""), objGeneralConfiguration.getDateTimeFormatterNatural());
		}
		
		return objLocalDateTime;
	}
	
	public boolean isLocalDateTimeNatural(String strLocalDateTime) {
		return Pattern.matches(objGeneralConfiguration.getPatternDateTimeNatural(), strLocalDateTime);
	}
	
	public boolean isLocalDateNatural(String strLocalDate) {
		return Pattern.matches(objGeneralConfiguration.getPatternDateNatural(), strLocalDate);
	}
	
	public boolean isLocalDate(String strLocalDate) {
		boolean boolReturn = false;
		if(strLocalDate != null) {
			boolReturn = Pattern.matches(objGeneralConfiguration.getPatternDate(), strLocalDate);
		}
		return boolReturn;
	}
	
	public LocalDate toLocalDate(String strLocalDate) {
		LocalDate objReturn = null;
		if(isLocalDate(strLocalDate)) {
			objReturn = LocalDate.parse(strLocalDate, objGeneralConfiguration.getDateFormatter());
		} else if(isLocalDateNatural(strLocalDate)) {
			objReturn = LocalDate.parse(strLocalDate, objGeneralConfiguration.getDateFormatterNatural());
		}
		return objReturn;
	}
	
	public String getCurrencyNatural(BigDecimal value) {
		String resultCurrency = "";
		if(value != null) {
			resultCurrency = GeneralConfiguration.getInstance().getCurrencyNumberFormat().format(value);
		}
		return resultCurrency;
	}
	
}
