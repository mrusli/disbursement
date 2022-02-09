package com.pyramix.web.disbursement.print;

import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Iframe;

import com.pyramix.web.common.GFCBaseController;
import com.pyramix.web.common.PageHandler;

public class DisbursementPrintControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5124356651103340397L;

	private Iframe iframe;
	private PageHandler handler= new PageHandler();	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		
	}

	public void onCreate$disbursementPrintWin(Event event) throws Exception {
		HashMap<String, Object> dataField = new HashMap<String, Object>();
		HashMap<String, Object> dataList = new HashMap<String, Object>();
		
		dataList.put("headerData", null);
		dataField.put("printedDate", "03/09/2021");
		
		iframe.setContent(handler.generateReportAMedia(dataField, dataList, 
			"~./zul/secure/disbursement/print/Report_A4.jasper", "Report_A4"));
		
	}
	
}
