package com.pyramix.web.main;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

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
		
		// disbursement menu
		setupDisbursementMenu();
		
		// logout menu
		setupLogoutMenu();
		
		// include
		mainInclude.setSrc("~./zul/secure/disbursement/DisbursementListInfo.zul");
	}

	private void setupDisbursementMenu() {
		log.info("Setting up Disbursement menu: Disbrusement, Report");
		
		// Menu
		Menu menu = new Menu();
		menu.setLabel("Disbursement");
		menu.setParent(mainMenubar);
		
		// Menupopup
		Menupopup menupopup = new Menupopup();
		menupopup.setParent(menu);
		
		// Menuitem - Disbursement
		Menuitem disbursementMenuitem;
		disbursementMenuitem = new Menuitem();
		disbursementMenuitem.setLabel("Disbursement");
		disbursementMenuitem.setSclass("main-menuitem");
		disbursementMenuitem.setParent(menupopup);
		disbursementMenuitem.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				log.info(event.getTarget());
				
				mainInclude.setSrc("~./zul/secure/disbursement/DisbursementListInfo.zul");
			}
		});
		
		// Menuitem - Disbursement Report
		Menuitem disbursementReportMenuitem;
		disbursementReportMenuitem = new Menuitem();
		disbursementReportMenuitem.setLabel("Disbursement Report");
		disbursementReportMenuitem.setSclass("main-menuitem");
		disbursementReportMenuitem.setParent(menupopup);
		disbursementReportMenuitem.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				log.info(event.getTarget());
				
				mainInclude.setSrc("~./zul/secure/disbursement/DisbursementReport.zul");
			}
		});
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
