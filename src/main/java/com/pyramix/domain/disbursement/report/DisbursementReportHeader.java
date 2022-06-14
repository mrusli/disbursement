package com.pyramix.domain.disbursement.report;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.pyramix.domain.common.IdBasedObject;
import com.pyramix.domain.common.SchemaUtil;

@Entity
@Table(name = "disbursement_report_header", schema = SchemaUtil.SCHEMA_COMMON)
public class DisbursementReportHeader extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1925502291420409283L;

	// `report_date` date DEFAULT NULL,
	@Column(name = "report_date")
	@Temporal(TemporalType.DATE)
	private Date reportDate;
	
	// `report_title` varchar(255) DEFAULT NULL,
	@Column(name = "report_title")
	private String reportTitle;
	
	// `report_period` varchar(255) DEFAULT NULL,
	@Column(name = "report_period")
	private String reportPeriod;

	// `report_period_date` date DEFAULT NULL,
	@Column(name = "report_period_date")
	@Temporal(TemporalType.DATE)
	private Date reportPeriodDate;
	
	// `disbursement_report_header_join_detail`
	@OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinTable(name = "disbursement_report_header_join_detail",
			joinColumns = @JoinColumn(name = "id_report_header"),
			inverseJoinColumns = @JoinColumn(name = "id_report_detail"))
	private List<DisbursementReportDetail> reportDetails;
	
	@Override
	public String toString() {
		return "DisbursementReportHeader [reportDate=" + reportDate + ", reportTitle=" + reportTitle + ", reportPeriod="
				+ reportPeriod + ", reportDetails=" + reportDetails + "]";
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getReportPeriod() {
		return reportPeriod;
	}

	public void setReportPeriod(String reportPeriod) {
		this.reportPeriod = reportPeriod;
	}

	public List<DisbursementReportDetail> getReportDetails() {
		return reportDetails;
	}

	public void setReportDetails(List<DisbursementReportDetail> reportDetails) {
		this.reportDetails = reportDetails;
	}

	public Date getReportPeriodDate() {
		return reportPeriodDate;
	}

	public void setReportPeriodDate(Date reportPeriodDate) {
		this.reportPeriodDate = reportPeriodDate;
	}
}
