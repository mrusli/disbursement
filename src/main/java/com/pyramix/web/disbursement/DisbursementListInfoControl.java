package com.pyramix.web.disbursement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.pyramix.domain.disbursement.Disbursement;
import com.pyramix.domain.disbursement.DisbursementDetail;
import com.pyramix.domain.disbursement.DisbursementSerialNumber;
import com.pyramix.domain.disbursement.DisbursementType;
import com.pyramix.domain.settings.Settings;
import com.pyramix.persistence.disbursement.dao.DisbursementDao;
import com.pyramix.persistence.disbursement.serialnumber.dao.DisbursementSerialNumberDao;
import com.pyramix.persistence.settings.dao.SettingsDao;
import com.pyramix.web.common.GFCBaseController;

public class DisbursementListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7260934396641532805L;

	private SettingsDao settingsDao;
	private DisbursementDao disbursementDao;
	private DisbursementSerialNumberDao disbursementSerialNumberDao;
	
	private Label formTitleLabel, infoResultlabel;
	private Datebox disbursementDatebox;
	private Combobox typeListCombobox, disbursementTypeCombobox;
	private Checkbox isDepositCheckbox, isPettyCashCheckbox;
	private Listbox disbursementListbox, disbursementListListbox;
	private Textbox theSumOfTextbox, descriptionTextbox, disbursementNoCompTextbox, 
		referenceTextbox;
	private Button addDetailButton, saveButton, cancelButton;
	
	private Locale settingLocale;	
	private Settings settings;
	private ListModelList<Disbursement> disbursementListModelList;
	private ListModelList<DisbursementDetail> disbursementDetailListModelList;
	private boolean allowEditDetails;
	
	private final Long DEFAULT_SETTINGS_ID = 1L;
	
	private static final Logger log = Logger.getLogger(DisbursementListInfoControl.class);
	
	public void onCreate$disbursementListInfoWin(Event event) throws Exception {
		log.info("Creating disbursementListInfoWin...");
		
		formTitleLabel.setValue("Disbursement");
		
		setSettings(
				getSettingsDao().findSettingsById(DEFAULT_SETTINGS_ID));
		
		setSettingLocale(new Locale(getSettings().getLanguageCode(),
				getSettings().getCountryCode()));
		disbursementDatebox.setLocale(getSettingLocale());
		disbursementDatebox.setFormat(getSettings().getDateFormatLong());
		
		// user select type to display the list
		setupDisbursementTypeList();
		
		// user select type during data entry / for data display when selected
		setupDisbursementType();
		
		// display listInfo
		displayDisbursementListInfo();
		// display 1st row onto the 'right pane'
		if (getDisbursementListModelList().isEmpty()) {
			return;
		} else {
			Disbursement selDisbursement = getDisbursementListModelList().get(0);
			displayDisbursementInfo(selDisbursement);
		}
	}

	private void setupDisbursementTypeList() {
		Comboitem comboitem;
		
		comboitem = new Comboitem();
		comboitem.setLabel("--All--");
		comboitem.setValue(null);
		comboitem.setParent(typeListCombobox);
		
		for (DisbursementType disbursementType : DisbursementType.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(disbursementType.toString());
			comboitem.setValue(disbursementType);
			comboitem.setParent(typeListCombobox);			
		}
	}

	private void setupDisbursementType() {
		Comboitem comboitem;

		for (DisbursementType disbursementType : DisbursementType.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(disbursementType.toString());
			comboitem.setValue(disbursementType);
			comboitem.setParent(disbursementTypeCombobox);			
		}	
	}

	public void onSelect$disbursementTypeCombobox(Event event) throws Exception {
		DisbursementType disbursementType = disbursementTypeCombobox.getSelectedItem().getValue();
		
		isPettyCashCheckbox.setDisabled(disbursementType.equals(DisbursementType.BANK));
		isDepositCheckbox.setDisabled(!disbursementType.equals(DisbursementType.BANK));			
	}

	private void displayDisbursementListInfo() throws Exception {
		List<Disbursement> disbursementList = 
				getDisbursementDao().findAllDisbursement();
		setDisbursementListModelList(new ListModelList<Disbursement>(
				disbursementList));
		disbursementListbox.setModel(getDisbursementListModelList());
		disbursementListbox.setItemRenderer(getDisbursementListitemRenderer());
		
		infoResultlabel.setValue("Total: " + disbursementList.size() + " Disbursement - ");		
	}
	
	private ListitemRenderer<Disbursement> getDisbursementListitemRenderer() {

		return new ListitemRenderer<Disbursement>() {
			
			@Override
			public void render(Listitem item, Disbursement disbursement, int index) throws Exception {
				Listcell lc;
				
				//	Tgl. - display with shortdate format
				lc = new Listcell(dateToStringDisplay(
						asLocalDate(disbursement.getDisbursementDate()), 
						getSettings().getDateFormatShort(), 
						getSettingLocale()));
				lc.setParent(item);
				
				//	Tipe
				lc = new Listcell();
				lc.setParent(item);
				
				//	No. - disbursement number
				lc = new Listcell();
				lc.setParent(item);
				
				//	Jumlah
				lc = new Listcell();
				lc.setParent(item);
				
				//	Status - NORMAL / BATAL (need to create in db table)
				lc = new Listcell();
				lc.setParent(item);
				
				//	edit
				lc = initEdit(new Listcell(), item, disbursement);
				lc.setParent(item);
				
				item.setValue(disbursement);
				item.setHeight("40px");
				
			}

			private Listcell initEdit(Listcell listcell, Listitem item, Disbursement disbursement) {
				Button button = new Button();
				button.setIconSclass("z-icon-edit");
				button.setWidth("25px");
				button.setHeight("23px");
				button.setSclass("selectButton");
				button.setParent(listcell);
				button.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// log.info("Edit Disbursement : User : "+getLoginUser().getUser_name());
						log.info("Edit " + disbursement.toString());

						// set selected - update to db later
						disbursementListbox.setSelectedItem(item);
						
						disbursementTypeCombobox.setDisabled(false);
						disbursementDatebox.setDisabled(false);
						theSumOfTextbox.setDisabled(false);
						theSumOfTextbox.setReadonly(false);
						theSumOfTextbox.setValue(disbursement.getTheSumOf().toString());
						descriptionTextbox.setDisabled(false);
						descriptionTextbox.setReadonly(false);
						
						// eanble to upload reference doc
						// uploadReferenceButton.setDisabled(false);
						// enable details button
						addDetailButton.setDisabled(false);
						// allow edit details
						setAllowEditDetails(true);
						
						// re-display details
						setDisbursementDetailListModelList(new ListModelList<DisbursementDetail>(
								disbursement.getDisbursementDetails()));
						disbursementListListbox.setModel(getDisbursementDetailListModelList());
						disbursementListListbox.setItemRenderer(getDisbursementDetailListitemRenderer());						
						
						// enable save and cancel button
						saveButton.setDisabled(false);
						cancelButton.setDisabled(false);					
					}

					private ListitemRenderer<DisbursementDetail> getDisbursementDetailListitemRenderer() {
						
						return new ListitemRenderer<DisbursementDetail>() {
							
							@Override
							public void render(Listitem item, DisbursementDetail detail, int index) throws Exception {
								Listcell lc;
								Textbox textbox;
								
								// No
								lc = new Listcell(String.valueOf(index+1)+".");
								lc.setParent(item);
								
								// Keterangan
								if (isAllowEditDetails()) {
									lc = new Listcell();
									lc.setParent(item);
									
									textbox = new Textbox();
									textbox.setValue(detail.getDescription());
									textbox.setWidth("100%");
									textbox.setInplace(true);
									textbox.setParent(lc);
								} else {
									lc = new Listcell(detail.getDescription());
									lc.setParent(item);
								}
								
								// Jumlah
								if (isAllowEditDetails()) {
									lc = new Listcell();
									lc.setParent(item);			
									
									textbox = new Textbox();
									textbox.setValue(detail.getAmount().toString());
									textbox.setWidth("100%");
									textbox.setInplace(true);
									textbox.setParent(lc);
								} else {
									lc = new Listcell(toDecimalFormat(
											detail.getAmount(), 
											getSettingLocale(), 
											getSettings().getDecimalFormat()));
									lc.setParent(item);
								}
								
								// Edit
								lc = new Listcell();
								lc.setParent(item);
								
								item.setValue(detail);
								item.setHeight("40px");								
							}
						};
					}
				});
				
				return listcell;
			}
		};
	}

	private void displayDisbursementInfo(Disbursement disbursement) throws WrongValueException, Exception {
		disbursementNoCompTextbox.setValue(disbursement.getDisbursementSerialNumber().getSerialComp());
		for (Comboitem comboitem : disbursementTypeCombobox.getItems()) {
			if (comboitem.getValue().equals(disbursement.getDisbursementType())) {
				disbursementTypeCombobox.setSelectedItem(comboitem);
			}
		}
		disbursementDatebox.setValue(disbursement.getDisbursementDate());
		theSumOfTextbox.setValue(toDecimalFormat(
				disbursement.getTheSumOf(), 
				getSettingLocale(),
				getSettings().getDecimalFormat()));
		descriptionTextbox.setValue(disbursement.getDescription());
		referenceTextbox.setValue(disbursement.getDocumentRef());
		
		// displayReferenceDocument(disbursement);
		
		setAllowEditDetails(false);
		
		setDisbursementDetailListModelList(new ListModelList<DisbursementDetail>(
				disbursement.getDisbursementDetails()));
		disbursementListListbox.setModel(getDisbursementDetailListModelList());
		disbursementListListbox.setItemRenderer(getDisbursementDetailListitemRenderer());
		
	}

	private ListitemRenderer<DisbursementDetail> getDisbursementDetailListitemRenderer() {

		return new ListitemRenderer<DisbursementDetail>() {
			
			@Override
			public void render(Listitem item, DisbursementDetail detail, int index) throws Exception {
				Listcell lc;
				Textbox textbox;
				
				// No
				lc = new Listcell(String.valueOf(index+1)+".");
				lc.setParent(item);
				
				// Keterangan
				if (isAllowEditDetails()) {
					lc = new Listcell();
					lc.setParent(item);
					
					textbox = new Textbox();
					textbox.setValue(detail.getDescription());
					textbox.setWidth("100%");
					textbox.setInplace(true);
					textbox.setParent(lc);
				} else {
					lc = new Listcell(detail.getDescription());
					lc.setParent(item);
				}
				
				// Jumlah
				if (isAllowEditDetails()) {
					lc = new Listcell();
					lc.setParent(item);			
					
					textbox = new Textbox();
					textbox.setValue(detail.getAmount().toString());
					textbox.setWidth("100%");
					textbox.setInplace(true);
					textbox.setParent(lc);
				} else {
					lc = new Listcell(toDecimalFormat(
							detail.getAmount(), 
							getSettingLocale(), 
							getSettings().getDecimalFormat()));
					lc.setParent(item);
				}
				
				// Edit
				lc = new Listcell();
				lc.setParent(item);
				
				item.setValue(detail);
				item.setHeight("40px");				
			}
		};
	}

	public void onClick$addButton(Event event) throws Exception {
		// log.info("Add Disbursement : User : "+getLoginUser().getUser_name());
		log.info("Add Disbursement");		
		// create a new disbursement object
		Disbursement disbursement = new Disbursement();
		// date		
		ZoneId zoneId = ZoneId.of(getSettings().getTimeZoneId());
		disbursement.setDisbursementDate(asDate(getLocalDate(zoneId), zoneId));
		disbursementDatebox.setDisabled(false);
		disbursementDatebox.setValue(disbursement.getDisbursementDate());
		// type - default to bank
		disbursementTypeCombobox.setDisabled(false);
		disbursementTypeCombobox.setSelectedIndex(0);
		DisbursementType disbursementType = disbursementTypeCombobox.getSelectedItem().getValue();
		disbursement.setDisbursementType(disbursementType);
		// enable 'deposit' option checkbox since type is defaulted to bank
		isDepositCheckbox.setDisabled(false);
		// disbursement number
		DisbursementSerialNumber disbursementSerialNumber =
				createDisbursementSerialNumber(disbursementType);
		disbursement.setDisbursementSerialNumber(disbursementSerialNumber);
		disbursementNoCompTextbox.setValue(disbursementSerialNumber.getSerialComp());
		// amount
		disbursement.setTheSumOf(BigDecimal.ZERO);
		theSumOfTextbox.setDisabled(false);
		theSumOfTextbox.setReadonly(false);
		theSumOfTextbox.setValue(toDecimalFormat(BigDecimal.ZERO, getSettingLocale(), getSettings().getDecimalFormat()));
		// description
		disbursement.setDescription("");
		descriptionTextbox.setDisabled(false);
		descriptionTextbox.setReadonly(false);
		descriptionTextbox.setValue("");
		// ref
		disbursement.setDocumentRef("");
		referenceTextbox.setValue("");
		// ref document
		// referenceIframe.setVisible(false);
		// referenceImage.setVisible(false);
		
		setDisbursementDetailListModelList(new ListModelList<DisbursementDetail>());
		disbursementListListbox.getItems().clear();
		disbursementListListbox.setModel(getDisbursementDetailListModelList());
		disbursementListListbox.setItemRenderer(getDisbursementDetailListitemRenderer());
		
		// allow to upload reference document
		// uploadReferenceButton.setDisabled(false);
		// enable details button
		addDetailButton.setDisabled(false);
		
		getDisbursementListModelList().add(0, disbursement);
		log.info(disbursement.toString());
		
		// enable save and cancel button
		saveButton.setDisabled(false);
		cancelButton.setDisabled(false);		
	}
	
	private DisbursementSerialNumber createDisbursementSerialNumber(DisbursementType disbursementType) throws Exception {
		// today's date
		LocalDate todayDate = getLocalDate(ZoneId.of(getSettings().getTimeZoneId()));
		ZoneId zoneId = ZoneId.of(getSettings().getTimeZoneId());
		int serialNum = 0;
		
		DisbursementSerialNumber disbursementSerialNumber = new DisbursementSerialNumber();
		disbursementSerialNumber.setDisbursementType(disbursementType);
		disbursementSerialNumber.setSerialDate(asDate(todayDate, zoneId));
		
		int day = getLocalDateDay(todayDate);
		if (day==1) {
			log.info("Today's Date is: "
					+ getLocalDate(getSettings().getDateFormatShort(), zoneId)
					+ " - 1st day of the month, serial number reset to 1 (one).");
			serialNum = 1;
			disbursementSerialNumber.setSerialNo(serialNum);
		} else {
			log.info("Continue from previous number");
			DisbursementSerialNumber lastDisbursementSerialNumber = 
					getDisbursementSerialNumberDao().findLastDisbursementSerialNumberByType(disbursementType);
			if (lastDisbursementSerialNumber==null) {
				serialNum = 1;				
			} else {
				// get last serialNumber date
				LocalDate lastSerialNumberDate = asLocalDate(lastDisbursementSerialNumber.getSerialDate());
				if (lastSerialNumberDate.getYear()==todayDate.getYear()) {
					// same year
					// compare last serialNumber month with todayDate's month
					if (lastSerialNumberDate.getMonth().compareTo(todayDate.getMonth())==0) {
						// if month is equal:
						serialNum = lastDisbursementSerialNumber.getSerialNo()+1;					
					} else {
						log.info("Last serial number is last month's number");
						serialNum = 1;
					}					
				} else {
					log.info("Last serial number is last year's number");
					serialNum = 1;
				}
			}
			disbursementSerialNumber.setSerialNo(serialNum);
		}
		String serialComp = formatSerialComp(
				disbursementType.toCode(disbursementType.getValue()),
				asDate(todayDate, zoneId),
				serialNum,
				getSettingLocale());
		disbursementSerialNumber.setSerialComp(serialComp);
		
		return disbursementSerialNumber;
	}

	public void onClick$addDetailButton(Event event) throws Exception {
		// allow edit disbursement details
		setAllowEditDetails(true);
		
		int index = getDisbursementDetailListModelList().size();
		DisbursementDetail disbursementDetail = new DisbursementDetail();
		disbursementDetail.setDescription("");
		disbursementDetail.setAmount(BigDecimal.ZERO);
		getDisbursementDetailListModelList().add(index, disbursementDetail);		
	}
	
	public void onClick$printButton(Event event) throws Exception {
		if (disbursementListbox.getSelectedItem()==null) {
			log.info("Disbursement not selected. Nothing to print.");
			
			return;
		}
		// can print
		Disbursement disbursement = disbursementListbox.getSelectedItem().getValue();				
		log.info("printing "+disbursement.toString());
		// create the print package
		DisbursementPrintData printData = new DisbursementPrintData();
		printData.setSettings(getSettings());
		printData.setDisbursement(disbursement);
		
		Map<String, DisbursementPrintData> arg = Collections.singletonMap("disbursementPrintData", printData);
		
		Window disbursementReportPrintWin =
				(Window) Executions.createComponents("~./zul/secure/disbursement/print/DisbursementPrint.zul", null, arg);
		
		disbursementReportPrintWin.doModal();
	}

	public void onClick$cancelButton(Event event) throws Exception {
		displayDisbursementListInfo();
		// display 1st row onto the 'right pane'
		if (getDisbursementListModelList().isEmpty()) {
			return;
		} else {
			Disbursement selDisbursement = getDisbursementListModelList().get(0);
			displayDisbursementInfo(selDisbursement);
		}
		
		cancelButton.setDisabled(true);
		saveButton.setDisabled(true);
	}
	
	public SettingsDao getSettingsDao() {
		return settingsDao;
	}

	public void setSettingsDao(SettingsDao settingsDao) {
		this.settingsDao = settingsDao;
	}

	public Locale getSettingLocale() {
		return settingLocale;
	}

	public void setSettingLocale(Locale settingLocale) {
		this.settingLocale = settingLocale;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public DisbursementDao getDisbursementDao() {
		return disbursementDao;
	}

	public void setDisbursementDao(DisbursementDao disbursementDao) {
		this.disbursementDao = disbursementDao;
	}

	public ListModelList<Disbursement> getDisbursementListModelList() {
		return disbursementListModelList;
	}

	public void setDisbursementListModelList(ListModelList<Disbursement> disbursementListModelList) {
		this.disbursementListModelList = disbursementListModelList;
	}

	public ListModelList<DisbursementDetail> getDisbursementDetailListModelList() {
		return disbursementDetailListModelList;
	}

	public void setDisbursementDetailListModelList(ListModelList<DisbursementDetail> disbursementDetailListModelList) {
		this.disbursementDetailListModelList = disbursementDetailListModelList;
	}

	public boolean isAllowEditDetails() {
		return allowEditDetails;
	}

	public void setAllowEditDetails(boolean allowEditDetails) {
		this.allowEditDetails = allowEditDetails;
	}

	public DisbursementSerialNumberDao getDisbursementSerialNumberDao() {
		return disbursementSerialNumberDao;
	}

	public void setDisbursementSerialNumberDao(DisbursementSerialNumberDao disbursementSerialNumberDao) {
		this.disbursementSerialNumberDao = disbursementSerialNumberDao;
	}
	
}
