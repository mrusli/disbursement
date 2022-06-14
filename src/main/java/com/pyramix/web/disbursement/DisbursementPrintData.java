package com.pyramix.web.disbursement;

import com.pyramix.domain.disbursement.Disbursement;
import com.pyramix.domain.disbursement.DisbursementType;
import com.pyramix.domain.disbursement.report.DisbursementReportHeader;
import com.pyramix.domain.settings.Settings;

public class DisbursementPrintData {

	private Settings settings;
	
	private Disbursement disbursement;
	
	private DisbursementReportHeader reportHeader;
	
	private DisbursementType disbursementType;
	
	public Disbursement getDisbursement() {
		return disbursement;
	}

	public void setDisbursement(Disbursement disbursement) {
		this.disbursement = disbursement;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public DisbursementReportHeader getReportHeader() {
		return reportHeader;
	}

	public void setReportHeader(DisbursementReportHeader reportHeader) {
		this.reportHeader = reportHeader;
	}

	public DisbursementType getDisbursementType() {
		return disbursementType;
	}

	public void setDisbursementType(DisbursementType disbursementType) {
		this.disbursementType = disbursementType;
	}
	
}
