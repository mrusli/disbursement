package com.pyramix.web.disbursement.print;

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

import com.pyramix.domain.disbursement.Disbursement;
import com.pyramix.domain.disbursement.DisbursementDetail;
import com.pyramix.domain.settings.Settings;
import com.pyramix.web.common.GFCBaseController;
import com.pyramix.web.common.PageHandler;
import com.pyramix.web.disbursement.DisbursementPrintData;

public class DisbursementPrintControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5124356651103340397L;

	private Iframe iframe;
	private PageHandler handler= new PageHandler();	
	
	private DisbursementPrintData disbursementPrintData;
	private Disbursement disbursement;
	private Settings settings;
	
	private static final Logger log = Logger.getLogger(DisbursementPrintControl.class);
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// disbursementPrintData
		setDisbursementPrintData(
				(DisbursementPrintData) arg.get("disbursementPrintData"));
		// disbursement
		setDisbursement(getDisbursementPrintData().getDisbursement());
		// settings
		setSettings(getDisbursementPrintData().getSettings());
	}

	public void onCreate$disbursementPrintWin(Event event) throws Exception {
		Locale locale = new Locale(getSettings().getLanguageCode(),
				getSettings().getCountryCode());
		
		HashMap<String, Object> dataField = new HashMap<String, Object>();
		HashMap<String, Object> dataList = new HashMap<String, Object>();
		
		dataField.put("printedDate", getLocalDate(getSettings().getDateFormatShort(), ZoneId.of(getSettings().getTimeZoneId())));
		dataField.put("headerData", getDisbursementHeader(locale));
		dataField.put("totalDisbursement", getTotalDisbursement(locale));
		dataList.put("detailData", null);
		
		iframe.setContent(handler.generateReportAMedia(dataField, dataList, 
			"~./zul/secure/disbursement/print/Report_A4.jasper", "Report_A4"));
		
	}

	private DisbursementHeader getDisbursementHeader(Locale locale) throws Exception {
		DisbursementHeader disbursementHeader = new DisbursementHeader();
		disbursementHeader.setTheSumOf(toDecimalFormat(getDisbursement().getTheSumOf(), locale, getSettings().getDecimalFormat()));
		disbursementHeader.setDisbursementDate(dateToStringDisplay(
				asLocalDate(getDisbursement().getDisbursementDate()), 
				getSettings().getDateFormatLong(), 
				locale));
		disbursementHeader.setDisbursementDescription(getDisbursement().getDescription());
		disbursementHeader.setDisbursementType(getDisbursement().getDisbursementType().toString()+" DISBURSEMENT");
		disbursementHeader.setDocumentRef(getDisbursement().getDocumentRef());
		disbursementHeader.setDocumentSerialNo(getDisbursement().getDisbursementSerialNumber().getSerialComp());
		
		disbursementHeader.setDisbursementHeaderDetails(getDisbursementHeaderDetail(locale));
		
		return disbursementHeader;
	}
	
	private List<DisbursementHeaderDetail> getDisbursementHeaderDetail(Locale locale) throws Exception {
		List<DisbursementHeaderDetail> headerDetailList = new ArrayList<DisbursementHeaderDetail>();
		
		int no = 0;
		// get from disbursement details
		for(DisbursementDetail detail : getDisbursement().getDisbursementDetails()) {
			no = no + 1;
			
			DisbursementHeaderDetail headerDetail = new DisbursementHeaderDetail();
			headerDetail.setOrderNo(no+".");
			headerDetail.setDescription(detail.getDescription());
			headerDetail.setAmount(toDecimalFormat(detail.getAmount(), locale, getSettings().getDecimalFormat()));
			
			headerDetailList.add(headerDetail);
		}
		
		headerDetailList.forEach(detail->log.info(detail.toString()));
		
		return headerDetailList;
	}

	private String getTotalDisbursement(Locale locale) throws Exception {
		BigDecimal total = BigDecimal.ZERO;
		// get from disbursement details
		for(DisbursementDetail detail : getDisbursement().getDisbursementDetails()) {
			total = total.add(detail.getAmount());
		}
		
		return toDecimalFormat(total, locale, getSettings().getDecimalFormat());
	}

	public Disbursement getDisbursement() {
		return disbursement;
	}

	public void setDisbursement(Disbursement disbursement) {
		this.disbursement = disbursement;
	}

	public DisbursementPrintData getDisbursementPrintData() {
		return disbursementPrintData;
	}

	public void setDisbursementPrintData(DisbursementPrintData disbursementPrintData) {
		this.disbursementPrintData = disbursementPrintData;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
}
