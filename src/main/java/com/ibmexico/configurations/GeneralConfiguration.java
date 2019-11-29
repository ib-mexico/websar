package com.ibmexico.configurations;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GeneralConfiguration {
	
	private Locale locale;
	private DateTimeFormatter objDateFormatterNatural;
	private DateTimeFormatter objDateFormatterNatural2;
	private DateTimeFormatter objDateTimeFullFormatterNatural;
	private DateTimeFormatter objDateFullFormatterNatural;
	private DateTimeFormatter objDateFormatter;
	private DateTimeFormatter objDateTimeFormatterNatural;
	private DateTimeFormatter objTimeFormatterNatural;
	private DateTimeFormatter objMonthFormatterNatural;
	
	private String objPatternDateTimeNatural;
	private String objPatternTimeNatural;
	private String objPatternDateNatural;
	private String objPatternDate;
	private String emailSender;
	private String twilio_ACCOUNT_SID;
	private String twilio_AUTH_TOKEN;
	
	public static GeneralConfiguration Instance = new GeneralConfiguration();
	
	//CONSTRUCTOR
	private GeneralConfiguration(){
		locale = new Locale("es", "MX");
		
		objDateTimeFullFormatterNatural = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy 'a la(s)' hh:mm a");
		objDateTimeFullFormatterNatural.withLocale(locale);
		
		objDateFullFormatterNatural = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy");
		objDateFullFormatterNatural.withLocale(locale);
		
		objDateFormatterNatural = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		objDateFormatterNatural.withLocale(locale);
		
		objDateFormatterNatural2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		objDateFormatterNatural2.withLocale(locale);
		
		objDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		objDateFormatter.withLocale(locale);
		
		
		objDateTimeFormatterNatural = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		objDateTimeFormatterNatural.withLocale(locale);
		
		objTimeFormatterNatural = DateTimeFormatter.ofPattern("HH:mm");
		objTimeFormatterNatural.withLocale(locale);
		
		objMonthFormatterNatural = DateTimeFormatter.ofPattern("MMMM");
		objMonthFormatterNatural.withLocale(locale);
		
		objPatternDateTimeNatural = "\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2} Hrs.";
		objPatternTimeNatural = "\\d{2}:\\d{2} Hrs.";
		objPatternDateNatural = "\\d{2}/\\d{2}/\\d{4}";
		
		
		objPatternDate = "\\d{4}-\\d{2}-\\d{2}";
		
		emailSender = "jorge.cortes@ib-mexico.com";
		
		twilio_ACCOUNT_SID = "_ACd6081dd6ea62e62d4e70f3354e635da0";
		twilio_AUTH_TOKEN = "_75263e6ecfc8e45f99eec630d8ab266d";
		
	}
	
	public DateTimeFormatter getMonthFormatterNatural() {
		return objMonthFormatterNatural;
	}

	public void setObjMonthFormatterNatural(DateTimeFormatter objMonthFormatterNatural) {
		this.objMonthFormatterNatural = objMonthFormatterNatural;
	}

	public static GeneralConfiguration getInstance() {
		return Instance;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale locale) {
		if(locale != null) {
			this.locale = locale; 
		}
	}
	
	public NumberFormat getCurrencyNumberFormat() {
		return NumberFormat.getCurrencyInstance(locale);
	}
	
	public NumberFormat getNumberFormat() {
		return NumberFormat.getNumberInstance(locale);
	}
	
	public NumberFormat getPercentNumberFormat() {
		return NumberFormat.getPercentInstance(locale);
	}
	
	public DateTimeFormatter getDateTimeFormatterNatural() {
		return objDateTimeFormatterNatural;
	}
	
	public DateTimeFormatter getDateFormatterNatural() {
		return objDateFormatterNatural;
	}
	
	public DateTimeFormatter getDateFormatterNatural2() {
		return objDateFormatterNatural2;
	}
	
	public DateTimeFormatter getDateTimeFullFormatterNatural() {
		return objDateTimeFullFormatterNatural;
	}
	
	public DateTimeFormatter getDateFullFormatterNatural() {
		return objDateTimeFullFormatterNatural;
	}
	
	public DateTimeFormatter getTimeFormatterNatural() {
		return objTimeFormatterNatural;
	}
	
	public DateTimeFormatter getDateFormatter() {
		return objDateFormatter;
	}
	
	public String getEmailSender() {
		return emailSender;
	}
	
	public String getPatternDateTimeNatural() {
		return objPatternDateTimeNatural;
	}
	
	public String getPatternTimeNatural() {
		return objPatternTimeNatural;
	}
	
	public String getPatternDateNatural() {
		return objPatternDateNatural;
	}
	
	public String getPatternDate() {
		return objPatternDate;
	}
	
	public String getCurrentMonthNatural(int month) {
		String[] monthNames = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
	    return monthNames[month - 1];
	}
	
	public Boolean isValidEmail(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}
	
	public String getTwilioAccountSID() {
		return twilio_ACCOUNT_SID;
	}
	
	public String getTwilioAuthToken() {
		return twilio_AUTH_TOKEN;
	}
}
