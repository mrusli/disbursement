package com.pyramix.web.disbursement;

import com.pyramix.domain.disbursement.Disbursement;
import com.pyramix.domain.settings.Settings;

public class DisbursementPrintData {

	private Settings settings;
	
	private Disbursement disbursement;
	
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
	
}
