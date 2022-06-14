package com.pyramix.web.disbursement.report.print;

import java.util.List;

public class DisbursementActivityHeader {

	private String reportDate;
	
	private String reportTitle;
	
	private String reportPeriod;
	
	private String transactionType;
	
	private List<DisbursementActivityHeaderDetail> activityHeaderDetails;

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getReportPeriod() {
		return reportPeriod;
	}

	public void setReportPeriod(String reportPeriod) {
		this.reportPeriod = reportPeriod;
	}

	public List<DisbursementActivityHeaderDetail> getActivityHeaderDetails() {
		return activityHeaderDetails;
	}

	public void setActivityHeaderDetails(List<DisbursementActivityHeaderDetail> activityHeaderDetails) {
		this.activityHeaderDetails = activityHeaderDetails;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
}
