package com.pyramix.web.disbursement.report.print;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Iframe;

import com.pyramix.domain.disbursement.DisbursementType;
import com.pyramix.domain.disbursement.report.DisbursementReportDetail;
import com.pyramix.domain.disbursement.report.DisbursementReportHeader;
import com.pyramix.domain.settings.Settings;
import com.pyramix.web.common.GFCBaseController;
import com.pyramix.web.common.PageHandler;
import com.pyramix.web.disbursement.DisbursementPrintData;

public class DisbursementReportPrintControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 338108242201543848L;

	private Iframe iframe;
	private PageHandler handler= new PageHandler();
	
	private DisbursementPrintData disbursementPrintData;
	private DisbursementReportHeader reportHeader;
	private Settings settings;
	private DisbursementType disbursementType;
	
	private static final Logger log = Logger.getLogger(DisbursementReportPrintControl.class);
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// disbursementPrintData
		setDisbursementPrintData(
				(DisbursementPrintData) arg.get("disbursementPrintData"));
		// reportHeader
		setReportHeader(getDisbursementPrintData().getReportHeader());
		// settings
		setSettings(getDisbursementPrintData().getSettings());
		// disbursement type
		setDisbursementType(getDisbursementPrintData().getDisbursementType());
	}

	public void onCreate$disbursementReportPrintWin(Event event) throws Exception {
		Locale locale = new Locale(getSettings().getLanguageCode(),
				getSettings().getCountryCode());

		HashMap<String, Object> dataField = new HashMap<String, Object>();
		HashMap<String, Object> dataList = new HashMap<String, Object>();
		
		dataField.put("printed_date", getLocalDate(getSettings().getDateFormatShort(), 
				ZoneId.of(getSettings().getTimeZoneId())));
		dataField.put("activityHeader", getDisbursementActivityReportHeader(locale));
		dataField.put("activityFooter", getDisbursementActivityReportFooter(locale));
		dataList.put("detailData", null);
		
		iframe.setContent(handler.generateReportAMedia(dataField, dataList, 
				"~./zul/secure/disbursement/print/Report_Activity_A4.jasper", "Report_Activity_A4"));
		
	}

	private Object getDisbursementActivityReportFooter(Locale locale) throws Exception {
		DisbursementReportDetail reportDetail;
		// get the begining balance
		reportDetail = getReportHeader().getReportDetails().get(0);
		BigDecimal beginningBal = reportDetail.getTheSumOf();
		// get the total 'setoran' and expenditure
		BigDecimal setoran = BigDecimal.ZERO;
		BigDecimal expenditure = BigDecimal.ZERO;
		for (int i=1; i<getReportHeader().getReportDetails().size()-1; i++) {
			reportDetail = getReportHeader().getReportDetails().get(i);
			if (reportDetail.isDeposit()) {
				setoran = setoran.add(reportDetail.getTheSumOf());
			} else {
				expenditure = expenditure.add(reportDetail.getTheSumOf());
			}
		}
		BigDecimal endingBal = beginningBal.add(setoran).subtract(expenditure);
		
		log.info("Saldo Awal: "+beginningBal);
		log.info("Total Setoran: "+setoran);
		log.info("Total Expenditure: "+expenditure);
		log.info("Saldo Akhir: "+endingBal);
		
		DisbursementActivityFooter activityFooter = new DisbursementActivityFooter();
		activityFooter.setBeginningBalance(toDecimalFormat(beginningBal, locale, getSettings().getDecimalFormat()));
		activityFooter.setTotalDeposit(toDecimalFormat(setoran, locale, getSettings().getDecimalFormat()));
		activityFooter.setTotalExpenditure(toDecimalFormat(expenditure, locale, getSettings().getDecimalFormat()));
		activityFooter.setEndingBalance(toDecimalFormat(endingBal, locale, getSettings().getDecimalFormat()));
		
		return activityFooter;
	}

	private Object getDisbursementActivityReportHeader(Locale locale) throws Exception {
		DisbursementActivityHeader activityHeader = new DisbursementActivityHeader();
		activityHeader.setTransactionType(getDisbursementType().toString());
		activityHeader.setReportDate(dateToStringDisplay(
				asLocalDate(getReportHeader().getReportDate()), 
				getSettings().getDateFormatShort(), 
				locale));
		activityHeader.setReportTitle(getReportHeader().getReportTitle());
		activityHeader.setReportPeriod(getReportHeader().getReportPeriod());
		
		activityHeader.setActivityHeaderDetails(getDisbursementActivityReportHeaderDetails(locale));
		
		return activityHeader;
	}

	private List<DisbursementActivityHeaderDetail> getDisbursementActivityReportHeaderDetails(Locale locale) throws Exception {
		List<DisbursementActivityHeaderDetail> activityDetails = new ArrayList<DisbursementActivityHeaderDetail>();
		
		for (DisbursementReportDetail detail : getReportHeader().getReportDetails()) {		
			DisbursementActivityHeaderDetail activityDetail = new DisbursementActivityHeaderDetail();
			activityDetail.setDisbursementDate(dateToStringDisplay(
				asLocalDate(detail.getDisbursementDate()), getSettings().getDateFormatShort(), locale));
			activityDetail.setDisbursementType(detail.getDisbursementType().toString());
			activityDetail.setDisbursementSerialNo(detail.getDisbursementSerialNo());
			activityDetail.setDescription(detail.getDescription());
			activityDetail.setTheSumOf(toDecimalFormat(detail.getTheSumOf(), locale, getSettings().getDecimalFormat()));
			activityDetail.setSymbolCR(detail.isDeposit() ? "CR" : "");
			
			activityDetails.add(activityDetail);
		}
		
		return activityDetails;
	}

	public DisbursementPrintData getDisbursementPrintData() {
		return disbursementPrintData;
	}

	public void setDisbursementPrintData(DisbursementPrintData disbursementPrintData) {
		this.disbursementPrintData = disbursementPrintData;
	}

	public DisbursementReportHeader getReportHeader() {
		return reportHeader;
	}

	public void setReportHeader(DisbursementReportHeader reportHeader) {
		this.reportHeader = reportHeader;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public DisbursementType getDisbursementType() {
		return disbursementType;
	}

	public void setDisbursementType(DisbursementType disbursementType) {
		this.disbursementType = disbursementType;
	}
}
