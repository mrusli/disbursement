package com.pyramix.domain.settings;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.pyramix.domain.common.IdBasedObject;
import com.pyramix.domain.common.SchemaUtil;

@Entity
@Table(name = "settings", schema = SchemaUtil.SCHEMA_COMMON)
public class Settings extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -664190187704324689L;

	//  `name` varchar(255) DEFAULT NULL,
	@Column(name = "name")
	private String name;
	
	//  `language_code` varchar(2) DEFAULT NULL,
	@Column(name = "language_code")
	private String languageCode;
	
	//  `country_code` varchar(3) DEFAULT NULL,
	@Column(name = "country_code")
	private String countryCode;
	
	//  `date_format_long` varchar(255) DEFAULT NULL,
	@Column(name = "date_format_long")
	private String dateFormatLong;
	
	//  `date_format_short` varchar(255) DEFAULT NULL,
	@Column(name = "date_format_short")
	private String dateFormatShort;
	
	//  `base_currency_code` varchar(3) DEFAULT NULL,
	@Column(name = "base_currency_code")
	private String currencyCode;
	
	//  `decimal_format` varchar(255) DEFAULT NULL,
	@Column(name = "decimal_format")
	private String decimalFormat;
	
	//  `trans_start_date` date DEFAULT NULL,
	@Column(name = "trans_start_date")
	private Date transactionStartDate;
	
	//  `time_zone_id` varchar(255) DEFAULT NULL,
	@Column(name = "time_zone_id")
	private String timeZoneId;
	
	//  `trans_day` int DEFAULT NULL,
	@Column(name = "trans_day")
	private int transactionDay;
	
	//  `upload_max_filesize` varchar(255) DEFAULT NULL,
	@Column(name = "upload_max_filesize")
	private String uploadMaxFileSize;
	
	//  `upload_file_filter` varchar(255) DEFAULT NULL,
	@Column(name = "upload_file_filter")
	private String uploadFileFilter;
	
	//  `upload_directory` varchar(255) DEFAULT NULL,
	@Column(name = "upload_directory")
	private String uploadDirectory;

	@Override
	public String toString() {
		return "Settings [id=" + super.getId() + ", name=" + name + ", languageCode=" + languageCode + ", countryCode=" + countryCode
				+ ", dateFormatLong=" + dateFormatLong + ", dateFormatShort=" + dateFormatShort + ", currencyCode="
				+ currencyCode + ", decimalFormat=" + decimalFormat + ", transactionStartDate=" + transactionStartDate
				+ ", timeZoneId=" + timeZoneId + ", transactionDay=" + transactionDay + ", uploadMaxFileSize="
				+ uploadMaxFileSize + ", uploadFileFilter=" + uploadFileFilter + ", uploadDirectory=" + uploadDirectory
				+ "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getDateFormatLong() {
		return dateFormatLong;
	}

	public void setDateFormatLong(String dateFormatLong) {
		this.dateFormatLong = dateFormatLong;
	}

	public String getDateFormatShort() {
		return dateFormatShort;
	}

	public void setDateFormatShort(String dateFormatShort) {
		this.dateFormatShort = dateFormatShort;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getDecimalFormat() {
		return decimalFormat;
	}

	public void setDecimalFormat(String decimalFormat) {
		this.decimalFormat = decimalFormat;
	}

	public Date getTransactionStartDate() {
		return transactionStartDate;
	}

	public void setTransactionStartDate(Date transactionStartDate) {
		this.transactionStartDate = transactionStartDate;
	}

	public String getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	public int getTransactionDay() {
		return transactionDay;
	}

	public void setTransactionDay(int transactionDay) {
		this.transactionDay = transactionDay;
	}

	public String getUploadMaxFileSize() {
		return uploadMaxFileSize;
	}

	public void setUploadMaxFileSize(String uploadMaxFileSize) {
		this.uploadMaxFileSize = uploadMaxFileSize;
	}

	public String getUploadFileFilter() {
		return uploadFileFilter;
	}

	public void setUploadFileFilter(String uploadFileFilter) {
		this.uploadFileFilter = uploadFileFilter;
	}

	public String getUploadDirectory() {
		return uploadDirectory;
	}

	public void setUploadDirectory(String uploadDirectory) {
		this.uploadDirectory = uploadDirectory;
	}
	
}
