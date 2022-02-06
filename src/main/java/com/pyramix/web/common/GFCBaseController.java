package com.pyramix.web.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public class GFCBaseController extends GenericForwardComposer<Component> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3343794621590724566L;

	// Format : Date
	private final String longDateFormat 	= "dd-MMMM-yyyy";
	private final String shortDateFormat 	= "dd-MM-yyyy";
	private final String emphYearMonth		= "yyyy-MMMM-dd";
	private final String emphYearMonthShort = "yyyyMMdd";
	
	// Format : Decimal and Non-Decimal 
	private final String floatFormatLocal	= "##.##0,00";
	private final String decimalFormatLocal	= "###.###.###,00";
	private final String intFormat			= "##,##0";
	private final String integerFormat		= "###,###,###";

	// private final String floatFormat		= "##,##0.00";
	// private final String decimalFormat	= "###,###,###.00";
	
	// Format : Time
	private final String timeFormat			= "HH:mm:ss";
	
	// Locale
	private final Locale locale				= new Locale("in","ID");
	
	/*
	 * Date functions
	 * 
	 * Ref: 
	 * 	https://stackoverflow.com/questions/33066904/localdate-to-java-util-date-and-vice-versa-simplest-conversion/40143687
	 * 	https://www.baeldung.com/java-date-to-localdate-and-localdatetime
	 * 
	 * Note when saving date and date with time to MySQL database table:
	 * 1. Remove the hibernate hibernate jdbc timezone (hibernate.jdbc.time_zone) - this parameter causes incorrect time during update / save.
	 * 2. MySQL database table must specify whether the date data includes the current time or date only. Use 'date' datatype for date only,
	 * 		and 'datetime' datatype for date with time.
	 * 3. In the hibernate table mapping, attribute that requires date and time, must be specified with: TemporalType Timestamp.
	 * 		Attribute with date only, specify with TemporalType Date.
	 * 4. In the case of date with time (for example 'last_modified' column which requires time), 
	 * 		convert from LocalDate to Date using asDateTime(LocalDate localDate) function.
	 * 5. In the case of date without time (for example 'create_date' column),
	 * 		convert from LocalDate to Date using asDate(LocalDate localDate) function.
	 */
	
	/**
	 * Converting LocalDate to DateTime.  The resulting Date includes the date and current time.
	 * 
	 * @param localDate
	 * @return {@link Date}
	 */
	public Date asDateTime(LocalDate localDate, ZoneId zoneId) {
		
		// return java.sql.Date.valueOf(localDate);
		
		return java.util.Date.from(localDate.atTime(LocalTime.now())
				.atZone(zoneId)
				.toInstant());
	}
	
	/**
	 * Converting LocalDate to Date.  The resulting Date contains date only.
	 * 
	 * @param localDate
	 * @return {@link Date}
	 */
	public Date asDate(LocalDate localDate, ZoneId zoneId) {
		
		return java.util.Date.from(localDate.atStartOfDay()
				.atZone(zoneId)
				.toInstant());
	}
	
	/**
	 * Converting Date to LocalDate.  The resulting LocalDate is date-only value.
	 * For LocalDate with time, use asLocalDateTime(Date date) function.
	 * 
	 * @param date
	 * @return {@link LocalDate}
	 */
	public LocalDate asLocalDate(Date date) {
		
		return new java.sql.Date(date.getTime())
				.toLocalDate();
	}

	/**
	 * Converting Date to LocalDateTime. The resulting LocalDateTime includes date and current time.
	 * For LocalDate with Date only, use asLocalDate(Date date) function.
	 * 
	 * @param date
	 * @return {@link LocalDateTime}
	 */
	public LocalDateTime asLocalDateTime(Date date, ZoneId zoneId) {

		Instant current = date.toInstant();
		
		return LocalDateTime.ofInstant(current, zoneId);
	}
	

	/**
	 * Get the current date as LocalDate value.
	 * 
	 * @param zoneId
	 * @return {@link LocalDate}
	 */
	public LocalDate getLocalDate(ZoneId zoneId) {
		
		return LocalDate.now(ZoneId.of(zoneId.toString()));
	}
	
	/**
	 * Get the current datetime as LocalDateTime value.
	 * 
	 * @param zoneId
	 * @return {@link LocalDateTime}
	 */
	public LocalDateTime getLocalDateTime(ZoneId zoneId) {
		
		return LocalDateTime.now(zoneId);
	}
	
	/**
	 * Get the current date as a string value with dateFormat: 
	 *		longDateFormat 		= "dd-MMMM-yyyy";
	 *		shortDateFormat 	= "dd-MM-yyyy";
	 *		emphYearMonth		= "yyyy-MMMM-dd";
	 *		emphYearMonthShort	= "yyyyMMdd";
	 *
	 * @param dateFormat
	 * @return {@link String}
	 */
	public String getLocalDate(String dateFormat, ZoneId zoneId) {		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat, getLocale());
		
		return getLocalDate(zoneId).format(formatter);
	}	
	
	/**
	 * Return the current year - with 'yyyy' format
	 * 
	 * @return {@link String} value of the year
	 */
	public String getLocalDateYear(ZoneId zoneId) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy", getLocale());
		
		return getLocalDate(zoneId).format(formatter);
	}
	
	/**
	 * Return the current year - with yearFormat format
	 * 
	 * @param yearFormat - format string ("yy" or "yyyy")
	 * @return {@link String} value of the year
	 */
	public String getLocalDateYear(String yearFormat, Locale locale, ZoneId zoneId) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(yearFormat, locale);
		
		return getLocalDate(zoneId).format(formatter);
	}
	
	/**
	 * Return the year as specified by the localDate and formated as specified by the yearFormat
	 * 
	 * @param localDate - the date for the year to be extracted from
	 * @return {@link String} value of the year
	 */
	public String getLocalDateYear(LocalDate localDate, Locale locale) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy", locale);
		
		return localDate.format(formatter);
	}

	/**
	 * Return the year as specified by the localDate and the year format
	 * 
	 * @param localDate - the date for the year to be extracted from
	 * @param yearFormat - year format ("yy" or "yyyy")
	 * @return {@link String} value of the year
	 */
	public String getLocalDateYear(LocalDate localDate, String yearFormat, Locale locale) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(yearFormat, locale);
		
		return localDate.format(formatter);
	}
			
	/**
	 * 
	 * 
	 * @param localDate
	 * @return {@link Integer}
	 */
	public int getLocalDateYearValue(LocalDate localDate) {
		
		return localDate.getYear();
	}
	
	/**
	 * 
	 * 
	 * @return {@link String}
	 */
	public String getLocalDateMonth(Locale locale, ZoneId zoneId) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", locale);
		
		return getLocalDate(zoneId).format(formatter);		
	}
	
	/**
	 * 
	 * 
	 * @param monthFormat - the format ("MM" or "MMMM")
	 * @return {@link String} value of current month according to the monthFormat
	 */
	public String getLocalDateMonth(String monthFormat, Locale locale, ZoneId zoneId) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(monthFormat, locale);
		
		return getLocalDate(zoneId).format(formatter);
	}
	
	/**
	 * 
	 * 
	 * @param monthFormat - the format ("MM" or "MMMM")
	 * @param localDate - the date given
	 * @return {@link String} value of the date given according to the monthFormat
	 */
	public String getLocalDateMonth(LocalDate localDate, String monthFormat, Locale locale) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(monthFormat, locale);

		return localDate.format(formatter);
	}	
	
	/**
	 * 
	 * 
	 * @param localDate
	 * @return {@link String}
	 */
	public String getLocalDateMonth(LocalDate localDate, Locale locale) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", locale);
		
		return localDate.format(formatter);
	}
	
	/**
	 * 
	 * 
	 * @param localDate - the date given
	 * @return {@link Integer} of the date given
	 */
	public int getLocalDateMonthValue(LocalDate localDate) {
		
		return localDate.getMonthValue();
	}
	
	
	/**
	 * 
	 * 
	 * @return {@link String}
	 */
	public String getLocalDateDay(ZoneId zoneId) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd", getLocale());
		
		return getLocalDate(zoneId).format(formatter);		
	}
	
	/**
	 * 
	 * 
	 * @param localDate
	 * @return {@link Integer}
	 */
	public int getLocalDateDay(LocalDate localDate) {
		
		return localDate.getDayOfMonth();
	}

	
	/**
	 * dateToStringDisplay returns the date with a specified Locale,
	 * to a string value according to the formatStr parameter.  The
	 * format must be in the form of: dd-MMMM-yyyy to produce the
	 * Locale required for the month's spelling.
	 * 
	 * @param localDate
	 * @param format
	 * @param locale
	 * @return {@link String}
	 */
	public String dateToStringDisplay(LocalDate localDate, String format, Locale locale) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, locale);

		return localDate.format(formatter);	
	}
	
	/*
	 * Integer Functions
	 */
	
	
	/**
	 * Return a formatted ("###,###,###") string of an integer value
	 * 
	 * @param integerValue
	 * @return {@link String}
	 */
	public String getFormatedInteger(int integerValue) {
		DecimalFormat df = new DecimalFormat(getIntegerFormat());
		
		return df.format(integerValue); 
	}
	
	/**
	 * Return a formated ("###,###,###") string of a long value
	 * 
	 * @param longValue
	 * @return {@link String}
	 */
	public String getFormatedLong(long longValue) {
		DecimalFormat df = new DecimalFormat(getIntegerFormat());
		
		return df.format(longValue);
	}
	
	/*
	 * Decimal Functions
	 */
	
	/**
	 * Decimal format for money amount, using Locale value, and decimal format.
	 * NOTE: the decimal format MUST adhere to locale.  For example, country IDN must
	 * use ###.###.###,00 decimal format.
	 * 
	 * @param nominal
	 * @param locale
	 * @param decimalFormat
	 * @return {@link String}
	 */
	public String toDecimalFormat(BigDecimal nominal, Locale locale, String decimalFormat) throws Exception {
		BigDecimal nominalRound = nominal.setScale(2);
		
		DecimalFormat decimalFormatInstance = (DecimalFormat) NumberFormat.getNumberInstance(locale);
		decimalFormatInstance.applyLocalizedPattern(decimalFormat);
		
		return decimalFormatInstance.format(nominalRound);
	}
	
	/**
	 * Get the currency info according to the locale.  Currency comes with the following data:
	 * Symbol, Code, Display Name, Faction Digits.
	 * 
	 * @param locale
	 * @return {@link Currency}
	 */
	public Currency getCurrency(Locale locale) {
		
		return Currency.getInstance(locale);
	}
	
	/*
	 * get/set functions
	 */

	/**
	 * @return {@link String} - "dd-MMMM-yyyy" format
	 */
	public String getLongDateFormat() {
		return longDateFormat;
	}

	/**
	 * @return {@link String} - "dd-MM-yyyy" format
	 */
	public String getShortDateFormat() {
		return shortDateFormat;
	}

	/**
	 * @return {@link String} - "yyyy-MMMM-dd" format
	 */
	public String getEmphYearMonth() {
		return emphYearMonth;
	}

	/**
	 * @return {@link String} - "yyyyMMdd" format
	 */
	public String getEmphYearMonthShort() {
		return emphYearMonthShort;
	}

	/**
	 * @return {@link String} - "##.##0,00" format
	 */
	public String getFloatFormatLocal() {
		return floatFormatLocal;
	}

	/**
	 * @return {@link String} - "###.###.###,00" format
	 */
	public String getDecimalFormatLocal() {
		return decimalFormatLocal;
	}

	/**
	 * @return String - "##,##0" format
	 */
	public String getIntFormat() {
		return intFormat;
	}

	/**
	 * @return {@link String} - "###,###,###" format
	 */
	private String getIntegerFormat() {
		return integerFormat;
	}

	/**
	 * @return {@link String} - "HH:mm:ss" format
	 */
	public String getTimeFormat() {
		return timeFormat;
	}

	public Locale getLocale() {
		return locale;
	}

	/*************************************************************************************
	 * 
	 * Serial Number Format
	 * 
	 *************************************************************************************/
	
	
	/**
	 * Given all the integer numbers, formatCoaNumber returns a string value for the composed
	 * 'Chart of Account' number with the format '0.000.0000'
	 * 
	 * @param accountTypeNo
	 * @param accountGroupNo
	 * @param subAccount01No
	 * @param subAccount02No
	 * @param masterAccountNo
	 * @return {@link String}
	 */
	public String formatCoaNumber(int accountTypeNo, int accountGroupNo, int subAccount01No, int subAccount02No, int masterAccountNo) {
		String typeNo = String.valueOf(accountTypeNo);
		String groupNo = String.valueOf(accountGroupNo);
		String sub01No = String.valueOf(subAccount01No);
		String sub02No = String.valueOf(subAccount02No);
		String masterNo = String.valueOf(masterAccountNo);
		
		switch (masterNo.length()) {
			case 1: return typeNo+"."+groupNo+sub01No+sub02No+".000"+masterNo;
			case 2: return typeNo+"."+groupNo+sub01No+sub02No+".00"+masterNo;
			case 3: return typeNo+"."+groupNo+sub01No+sub02No+".0"+masterNo;
			case 4: return typeNo+"."+groupNo+sub01No+sub02No+"."+masterNo;
			default:
				return "0.000.0000";
		}
	}
	
	public String formatTo2DigitWithLeadingZeo(int digit) {
		String digitStrValue = String.valueOf(digit);
		
		switch (digitStrValue.length()) {
			case 1: return "0"+digit;
			case 2: return digitStrValue;
			default:
				return "00";
		}		
	}
	
	public String formatTo4DigitWithLeadingZeo(int digit) {		
		String digitStrValue = String.valueOf(digit);
	
		switch (digitStrValue.length()) {
			case 1: return "000"+digit;
			case 2:	return "00"+digit;
			case 3: return "0"+digit;
			case 4: return digitStrValue;
			default:
				return "0000";
		}
	}
	
	public String formatSerialComp(String code, Date currentDate, int serialNum, Locale locale) {
		
		String year 	= getLocalDateYear(asLocalDate(currentDate), "yy", locale);
		String month	= getLocalDateMonth(asLocalDate(currentDate), "MM", locale);		
		
		return code+"."+
			formatTo2DigitWithLeadingZeo(Integer.valueOf(year))+"."+
			formatTo2DigitWithLeadingZeo(Integer.valueOf(month))+"."+
			formatTo4DigitWithLeadingZeo(serialNum);
	}
}
