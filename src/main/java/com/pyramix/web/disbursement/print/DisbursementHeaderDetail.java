package com.pyramix.web.disbursement.print;

public class DisbursementHeaderDetail {

	private String orderNo;
	
	private String description;
	
	private String amount;
	
	@Override
	public String toString() {
		return "DisbursementHeaderDetail [orderNo=" + orderNo + ", description=" + description + ", amount=" + amount
				+ "]";
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
}
