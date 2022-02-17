package com.pyramix.domain.disbursement;

public enum DisbursementType {
	BANK(0), CASH(1);
	
	private int value;

	private DisbursementType(int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public String toCode(int value) {
		switch (value) {
			case 0: return "DB";
			case 1: return "DC";
			default:
				return null;
		}
	}
}
