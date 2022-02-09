package com.pyramix.web.disbursement;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.pyramix.web.common.GFCBaseController;

public class DisbursementListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7260934396641532805L;

	private Label formTitleLabel;
	
	private static final Logger log = Logger.getLogger(DisbursementListInfoControl.class);
	
	public void onCreate$disbursementListInfoWin(Event event) throws Exception {
		formTitleLabel.setValue("Disbursement");
	}
	
	public void onClick$printButton(Event event) throws Exception {
		log.info("printing...");
		
		Window disbursementReportPrintWin =
				(Window) Executions.createComponents("~./zul/secure/disbursement/print/DisbursementPrint.zul", null, null);
		
		disbursementReportPrintWin.doModal();
	}
	
}
