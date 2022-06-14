package com.pyramix.domain.disbursement.report;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.pyramix.domain.common.IdBasedObject;
import com.pyramix.domain.common.SchemaUtil;
import com.pyramix.domain.disbursement.DisbursementStatus;
import com.pyramix.domain.disbursement.DisbursementType;

@Entity
@Table(name = "disbursement_report_detail", schema = SchemaUtil.SCHEMA_COMMON)
public class DisbursementReportDetail extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8827244637500469666L;

	// `the_sum_of` decimal(19,2) DEFAULT NULL,
	@Column(name = "the_sum_of")
	private BigDecimal theSumOf;
	
	// `disbursement_date` date DEFAULT NULL,
	@Column(name = "disbursement_date")
	@Temporal(TemporalType.DATE)
	private Date disbursementDate;
	
	// `description` varchar(255) DEFAULT NULL,
	@Column(name = "description")
	private String description;
	
	// `disbursement_type` int DEFAULT NULL,
	@Column(name = "disbursement_type")
	@Enumerated(EnumType.ORDINAL)
	private DisbursementType disbursementType;
	
	// `deposit` char(1) DEFAULT NULL,
	@Column(name = "deposit")
	@Type(type = "true_false")
	private boolean deposit;
	
	// `create_date` date DEFAULT NULL,
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	// `modified_date` datetime DEFAULT NULL,
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	
	// `disbursement_no` varchar(255) DEFAULT NULL,
	@Column(name = "disbursement_no")
	private String disbursementSerialNo;
	
	// `status` int DEFAULT NULL,
	@Column(name = "status")
	private DisbursementStatus disbursementStatus;

	@Override
	public String toString() {
		return "DisbursementReportDetail [theSumOf=" + theSumOf + ", disbursementDate=" + disbursementDate
				+ ", description=" + description + ", disbursementType=" + disbursementType + ", deposit=" + deposit
				+ ", createDate=" + createDate + ", modifiedDate=" + modifiedDate + ", disbursementStatus="
				+ disbursementStatus + "]";
	}

	public BigDecimal getTheSumOf() {
		return theSumOf;
	}

	public void setTheSumOf(BigDecimal theSumOf) {
		this.theSumOf = theSumOf;
	}

	public Date getDisbursementDate() {
		return disbursementDate;
	}

	public void setDisbursementDate(Date disbursementDate) {
		this.disbursementDate = disbursementDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DisbursementType getDisbursementType() {
		return disbursementType;
	}

	public void setDisbursementType(DisbursementType disbursementType) {
		this.disbursementType = disbursementType;
	}

	public boolean isDeposit() {
		return deposit;
	}

	public void setDeposit(boolean deposit) {
		this.deposit = deposit;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public DisbursementStatus getDisbursementStatus() {
		return disbursementStatus;
	}

	public void setDisbursementStatus(DisbursementStatus disbursementStatus) {
		this.disbursementStatus = disbursementStatus;
	}

	public String getDisbursementSerialNo() {
		return disbursementSerialNo;
	}

	public void setDisbursementSerialNo(String disbursementSerialNo) {
		this.disbursementSerialNo = disbursementSerialNo;
	}

	
}
