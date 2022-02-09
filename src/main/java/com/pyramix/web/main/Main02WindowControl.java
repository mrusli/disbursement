package com.pyramix.web.main;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;

import com.pyramix.web.common.GFCBaseController;

public class Main02WindowControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3990517417266398587L;
	
	private Label pageTitleLabel;
	private Menubar mainMenubar;
	private Include mainInclude;
	
	private static final Logger log = Logger.getLogger(Main02WindowControl.class);
	
	public void onCreate$main02Window(Event event) throws Exception {
		pageTitleLabel.setValue("Pyramix System");
		
		// logout menu
		setupLogoutMenu();
		
		// include
		mainInclude.setSrc("~./zul/secure/disbursement/DisbursementListInfo.zul");
	}

	private void setupLogoutMenu() {
		log.info("Setting up Logout menu : /logout");
		
		Menuitem logoutMenuitem;
		logoutMenuitem = new Menuitem();
		logoutMenuitem.setLabel("Logout");
		logoutMenuitem.setSclass("logout-main-menuitem");
		logoutMenuitem.setHref("/logout");
		logoutMenuitem.setParent(mainMenubar);		
	}
	
}
