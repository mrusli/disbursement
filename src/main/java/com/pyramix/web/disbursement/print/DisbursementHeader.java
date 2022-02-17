package com.pyramix.web.disbursement.print;

import java.util.List;

public class DisbursementHeader {

	private String theSumOf;
	
	private String disbursementDate;
	
	private String disbursementDescription;
	
	private String disbursementType;
	
	private String documentRef;
	
	private String documentSerialNo;
	
	private String createdBy;
	
	private List<DisbursementHeaderDetail> disbursementHeaderDetails;

	public String getTheSumOf() {
		return theSumOf;
	}

	public void setTheSumOf(String theSumOf) {
		this.theSumOf = theSumOf;
	}

	public String getDisbursementDate() {
		return disbursementDate;
	}

	public void setDisbursementDate(String disbursementDate) {
		this.disbursementDate = disbursementDate;
	}

	public String getDisbursementDescription() {
		return disbursementDescription;
	}

	public void setDisbursementDescription(String disbursementDescription) {
		this.disbursementDescription = disbursementDescription;
	}

	public String getDisbursementType() {
		return disbursementType;
	}

	public void setDisbursementType(String disbursementType) {
		this.disbursementType = disbursementType;
	}

	public String getDocumentRef() {
		return documentRef;
	}

	public void setDocumentRef(String documentRef) {
		this.documentRef = documentRef;
	}

	public String getDocumentSerialNo() {
		return documentSerialNo;
	}

	public void setDocumentSerialNo(String documentSerialNo) {
		this.documentSerialNo = documentSerialNo;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<DisbursementHeaderDetail> getDisbursementHeaderDetails() {
		return disbursementHeaderDetails;
	}

	public void setDisbursementHeaderDetails(List<DisbursementHeaderDetail> disbursementHeaderDetails) {
		this.disbursementHeaderDetails = disbursementHeaderDetails;
	}
}
