package com.pyramix.web.dialogs;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.pyramix.web.common.GFCBaseController;

public class DecimalboxDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5064091393251849244L;
	
	private Window decimalboxDialogWin;
	private Label promptLabel;
	private Decimalbox promptDecimalbox;
	
	private DecimalboxDialogData decimalboxDialogData;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// decimalboxDialogData
		setDecimalboxDialogData(
				(DecimalboxDialogData) arg.get("decimalboxDialogData"));
	}

	public void onCreate$decimalboxDialogWin(Event event) throws Exception {
		promptLabel.setValue(getDecimalboxDialogData().getPromptText());
		
		promptDecimalbox.setLocale(getDecimalboxDialogData().getDecimalboxLocale());
		promptDecimalbox.setValue(getDecimalboxDialogData().getDecimalboxValue());
	}

	public void onClick$okButton(Event event) throws Exception {
		Events.sendEvent(Events.ON_OK, decimalboxDialogWin, promptDecimalbox.getValue());
		
		decimalboxDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {

		decimalboxDialogWin.detach();
	}
	
	public DecimalboxDialogData getDecimalboxDialogData() {
		return decimalboxDialogData;
	}

	public void setDecimalboxDialogData(DecimalboxDialogData decimalboxDialogData) {
		this.decimalboxDialogData = decimalboxDialogData;
	}
}
