package com.pyramix.web.dialogs;

import java.math.BigDecimal;
import java.util.Locale;

public class DecimalboxDialogData {
	
	// prompt text for the promptLabel 
	private String promptText;
	
	// decimalbox value - assigned BigDecimal value
	private BigDecimal decimalboxValue;
	
	// decimalbox locale - set the decimalbox locale
	private Locale decimalboxLocale;

	/**
	 * prompt text with the type {@link String} for the promptLabel
	 * 
	 * @return String
	 */
	public String getPromptText() {
		return promptText;
	}

	/**
	 * set the prompt text with the type {@link String} for the promptLabel
	 * 
	 * @param promptText
	 */
	public void setPromptText(String promptText) {
		this.promptText = promptText;
	}

	/**
	 * decimalbox value to get as a BigDecimal value
	 * 
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getDecimalboxValue() {
		return decimalboxValue;
	}

	/**
	 * set the decimalbox with a {@link BigDecimal} value
	 * 
	 * @param decimalboxValue
	 */
	public void setDecimalboxValue(BigDecimal decimalboxValue) {
		this.decimalboxValue = decimalboxValue;
	}

	/**
	 * decimalbox locale to get the decimalbox locale
	 * 
	 * @return {@link Locale}
	 */
	public Locale getDecimalboxLocale() {
		return decimalboxLocale;
	}

	/**
	 * decimalbox locale set the decimalbox {@link Locale} value
	 * 
	 * @param decimalboxLocale
	 */
	public void setDecimalboxLocale(Locale decimalboxLocale) {
		this.decimalboxLocale = decimalboxLocale;
	}
	
}
