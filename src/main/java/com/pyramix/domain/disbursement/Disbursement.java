package com.pyramix.domain.disbursement;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.pyramix.domain.common.IdBasedObject;
import com.pyramix.domain.common.SchemaUtil;
import com.pyramix.domain.user.User;

@Entity
@Table(name = "disbursement", schema = SchemaUtil.SCHEMA_COMMON)
public class Disbursement extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2628985553769364012L;

	//	`the_sum_of` decimal(19,2) DEFAULT NULL,
	@Column(name = "the_sum_of")
	private BigDecimal theSumOf;
	
	//	`disbursement_date` date DEFAULT NULL,
	@Column(name = "disbursement_date")
	@Temporal(TemporalType.DATE)
	private Date disbursementDate;
	
	//	`description` varchar(255) DEFAULT NULL,
	@Column(name = "description")
	private String description;
	
	//	`disbursement_type` int DEFAULT NULL,
	@Column(name = "disbursement_type")
	@Enumerated(EnumType.ORDINAL)
	private DisbursementType disbursementType;
	
	//	`deposit` char(1) DEFAULT NULL,
	@Column(name = "deposit")
	@Type(type = "true_false")
	private boolean deposit;

	//	`petty_cash` char(1) DEFAULT NULL,
	@Column(name = "petty_cash")
	@Type(type = "true_false")
	private boolean pettyCash;
	
	//	`document_ref` varchar(255) DEFAULT NULL,
	@Column(name = "document_ref")
	private String documentRef;
	
	//	`create_date` date DEFAULT NULL,
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	//	`modified_date` datetime DEFAULT NULL,
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	
	//	`disbursement_no_id_fk` bigint DEFAULT NULL,
	@OneToOne(cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "disbursement_no_id_fk")
	private DisbursementSerialNumber disbursementSerialNumber;
	
	//	`user_create_id_fk` bigint DEFAULT NULL,
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_create_id_fk")
	private User userCreate;
	
	//  `status` int DEFAULT NULL,
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private DisbursementStatus disbursementStatus;
	
	//	`disbursement_join_detail`
	@OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinTable(name = "disbursement_join_detail",
			joinColumns = @JoinColumn(name = "id_disbursement"),
			inverseJoinColumns = @JoinColumn(name = "id_detail"))
	private List<DisbursementDetail> disbursementDetails;

	@Override
	public String toString() {
		return "Disbursement [id=" + super.getId() + ", theSumOf=" + theSumOf + ", disbursementDate=" + disbursementDate + ", description="
				+ description + ", disbursementType=" + disbursementType + ", documentRef=" + documentRef
				+ ", createDate=" + createDate + ", modifiedDate=" + modifiedDate + ", disbursementSerialNumber="
				+ disbursementSerialNumber + ", disbursementDetails=" + disbursementDetails + "]";
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

	public String getDocumentRef() {
		return documentRef;
	}

	public void setDocumentRef(String documentRef) {
		this.documentRef = documentRef;
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

	public DisbursementSerialNumber getDisbursementSerialNumber() {
		return disbursementSerialNumber;
	}

	public void setDisbursementSerialNumber(DisbursementSerialNumber disbursementSerialNumber) {
		this.disbursementSerialNumber = disbursementSerialNumber;
	}

	public User getUserCreate() {
		return userCreate;
	}

	public void setUserCreate(User userCreate) {
		this.userCreate = userCreate;
	}

	public List<DisbursementDetail> getDisbursementDetails() {
		return disbursementDetails;
	}

	public void setDisbursementDetails(List<DisbursementDetail> disbursementDetails) {
		this.disbursementDetails = disbursementDetails;
	}

	public boolean isDeposit() {
		return deposit;
	}

	public void setDeposit(boolean deposit) {
		this.deposit = deposit;
	}

	public boolean isPettyCash() {
		return pettyCash;
	}

	public void setPettyCash(boolean pettyCash) {
		this.pettyCash = pettyCash;
	}

	public DisbursementStatus getDisbursementStatus() {
		return disbursementStatus;
	}

	public void setDisbursementStatus(DisbursementStatus disbursementStatus) {
		this.disbursementStatus = disbursementStatus;
	}
}
