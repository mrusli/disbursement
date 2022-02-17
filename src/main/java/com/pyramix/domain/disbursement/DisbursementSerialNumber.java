package com.pyramix.domain.disbursement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.pyramix.domain.common.IdBasedObject;
import com.pyramix.domain.common.SchemaUtil;

@Entity
@Table(name = "disbursement_serial_number", schema = SchemaUtil.SCHEMA_COMMON)
public class DisbursementSerialNumber extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3649119716114379825L;

	//	`disbursement_type` int DEFAULT NULL,
	@Column(name = "disbursement_type")
	@Enumerated(EnumType.ORDINAL)
	private DisbursementType disbursementType;
	
	//	`serial_date` date DEFAULT NULL,
	@Column(name = "serial_date")
	@Temporal(TemporalType.DATE)
	private Date serialDate;
	
	//	`serial_no` int DEFAULT NULL,
	@Column(name = "serial_no")
	private int serialNo;
	
	//	`serial_comp` varchar(255) DEFAULT NULL,
	@Column(name = "serial_comp")
	private String serialComp;

	@Override
	public String toString() {
		return "DisbursementSerialNumber [id=" + super.getId() + ", disbursementType=" + disbursementType + ", serialDate=" + serialDate
				+ ", serialNo=" + serialNo + ", serialComp=" + serialComp + "]";
	}

	public DisbursementType getDisbursementType() {
		return disbursementType;
	}

	public void setDisbursementType(DisbursementType disbursementType) {
		this.disbursementType = disbursementType;
	}

	public Date getSerialDate() {
		return serialDate;
	}

	public void setSerialDate(Date serialDate) {
		this.serialDate = serialDate;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getSerialComp() {
		return serialComp;
	}

	public void setSerialComp(String serialComp) {
		this.serialComp = serialComp;
	}
}
