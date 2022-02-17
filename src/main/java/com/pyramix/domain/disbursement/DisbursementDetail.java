package com.pyramix.domain.disbursement;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.pyramix.domain.common.IdBasedObject;
import com.pyramix.domain.common.SchemaUtil;

@Entity
@Table(name = "disbursement_detail", schema = SchemaUtil.SCHEMA_COMMON)
public class DisbursementDetail extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2474872215198175401L;

	//	`description` varchar(255) DEFAULT NULL,
	@Column(name = "description")
	private String description;
	
	//	`amount` decimal(19,2) DEFAULT NULL,
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Override
	public String toString() {
		return "DisbursementDetail [id=" + super.getId() + ", description=" + description + ", amount=" + amount + "]";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
