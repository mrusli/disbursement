package com.pyramix.domain.disbursement;

public enum DisbursementStatus {
	OK(0), BATAL(1);
	
	private int value;

	private DisbursementStatus(int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
