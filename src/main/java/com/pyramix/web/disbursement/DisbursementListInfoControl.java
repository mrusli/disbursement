package com.pyramix.web.disbursement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import com.pyramix.domain.disbursement.DisbursementStatus;
import com.pyramix.domain.disbursement.DisbursementType;
import com.pyramix.domain.settings.Settings;
import com.pyramix.persistence.disbursement.dao.DisbursementDao;
import com.pyramix.persistence.disbursement.serialnumber.dao.DisbursementSerialNumberDao;
import com.pyramix.persistence.settings.dao.SettingsDao;
import com.pyramix.web.common.GFCBaseController;
import com.pyramix.web.common.SuppressedException;

public class DisbursementListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7260934396641532805L;

	private SettingsDao settingsDao;
	private DisbursementDao disbursementDao;
	private DisbursementSerialNumberDao disbursementSerialNumberDao;
	
	private Label formTitleLabel, infoResultlabel, totalDisbursementLabel;
	private Datebox disbursementDatebox;
	private Combobox typeListCombobox, disbursementTypeCombobox, yearCombobox,
		monthCombobox;
	private Checkbox isDepositCheckbox, isPettyCashCheckbox, batalCheckbox;
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
		displayDisbursementList();
		// display 1st row onto the 'right pane'
		if (getDisbursementListModelList().isEmpty()) {
			return;
		} else {
			// indicate it's selected
			disbursementListbox.setSelectedIndex(0);
			// get the value
			Disbursement selDisbursement = getDisbursementListModelList().get(0);
			// display selected
			displayDisbursementInfo(selDisbursement);
		}
	}

	private void setupDisbursementTypeList() {
		Comboitem comboitem;
		
		setUpComboitemAllSelect(typeListCombobox);		
		for (DisbursementType disbursementType : DisbursementType.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(disbursementType.toString());
			comboitem.setValue(disbursementType);
			comboitem.setParent(typeListCombobox);			
		}
		
		typeListCombobox.setSelectedIndex(0);
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
		
		// isPettyCashCheckbox.setDisabled(disbursementType.equals(DisbursementType.BANK));
		isDepositCheckbox.setDisabled(false);
				// !disbursementType.equals(DisbursementType.BANK));
		
		// pettyCash 'checked' when 'CASH' is selected
		isPettyCashCheckbox.setChecked(disbursementType.equals(DisbursementType.CASH));
	}

	private void displayDisbursementList() throws Exception {
		List<Disbursement> disbursementList = 
				getDisbursementDao().findAllDisbursement();
		
		// setup the filter selections
		setupDisbursementListFilter(disbursementList);
		// current date
		LocalDate currentDate = getLocalDate(ZoneId.of(getSettings().getTimeZoneId()));
		// set filter to current year and month
		setDisbursementFilterCurrentDate(currentDate);
		// query for current year month only
		Date startDate = asDate(currentDate.with(TemporalAdjusters.firstDayOfMonth()), ZoneId.of(getSettings().getTimeZoneId()));
		Date endDate = asDate(currentDate.with(TemporalAdjusters.lastDayOfMonth()), ZoneId.of(getSettings().getTimeZoneId()));		
		disbursementList = 
				getDisbursementDao().findDisbursementByType_Date(null, startDate, endDate, false);
		// display
		displayDisbursementListInfo(disbursementList);
	}
	
	private void displayDisbursementListInfo(List<Disbursement> disbursementList) throws Exception {
		// display
		setDisbursementListModelList(new ListModelList<Disbursement>(
				disbursementList));
		disbursementListbox.setModel(getDisbursementListModelList());
		disbursementListbox.setItemRenderer(getDisbursementListitemRenderer());
		
		infoResultlabel.setValue("Total: " + disbursementList.size() + " Disbursement - ");		
	}

	private void setDisbursementFilterCurrentDate(LocalDate currentDate) {
		int currentYear = currentDate.getYear();
		for(Comboitem comboitem:yearCombobox.getItems()) {
			if (comboitem.getValue()!=null) {
				if (comboitem.getValue().equals(currentYear)) {
					yearCombobox.setSelectedItem(comboitem);
				}
			}
		}
		Month currentMonth = currentDate.getMonth();
		for(Comboitem comboitem:monthCombobox.getItems()) {
			if (comboitem.getValue()!=null) {
				if (comboitem.getValue().equals(currentMonth)) {
					monthCombobox.setSelectedItem(comboitem);
				}
			}
		}
	}

	private void setupDisbursementListFilter(List<Disbursement> disbursementList) {
		List<Date> listOfDates = new ArrayList<Date>();
		for(Disbursement disbursement : disbursementList) {
			Date disbursementDate = disbursement.getDisbursementDate();
			listOfDates.add(disbursementDate);
		}

		// set / reset
		typeListCombobox.setSelectedIndex(0);
		
		Date minDate = Collections.min(listOfDates);
		Date maxDate = Collections.max(listOfDates);
		log.info("Filter from: "+minDate+" to: "+maxDate);
		
		int minYear = asLocalDate(minDate).getYear();
		int maxYear = asLocalDate(maxDate).getYear();
		
		Comboitem comboitem;
		
		// reset combobox
		yearCombobox.getItems().clear();
		monthCombobox.getItems().clear();
		
		setUpComboitemAllSelect(yearCombobox);
		for (int i = minYear; i <= maxYear; i++) {
			comboitem = new Comboitem();
			comboitem.setLabel(String.valueOf(i));
			comboitem.setValue(i);
			comboitem.setParent(yearCombobox);
		}
		yearCombobox.setSelectedIndex(0);
		setUpComboitemAllSelect(monthCombobox);
		for (Month month : Month.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(month.getDisplayName(TextStyle.SHORT, getSettingLocale()));
			comboitem.setValue(month);
			comboitem.setParent(monthCombobox);
		}
		monthCombobox.setSelectedIndex(0);
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
				lc = new Listcell(disbursement.getDisbursementType().toString());
				lc.setParent(item);
				
				//	No. - disbursement number
				lc = new Listcell(disbursement.getDisbursementSerialNumber().getSerialComp());
				lc.setParent(item);
				
				//	Jumlah
				lc = new Listcell(toDecimalFormat(
						disbursement.getTheSumOf(), 
						getSettingLocale(), 
						getSettings().getDecimalFormat()));
				lc.setParent(item);
				
				//	Status - NORMAL / BATAL (need to create in db table)
				lc = new Listcell();
				lc.setParent(item);
				Label label = new Label();
				label.setValue(disbursement.getDisbursementStatus().toString());
				label.setClass(disbursement.getDisbursementStatus().equals(DisbursementStatus.BATAL) ?
						"label label-danger" :
						"label label-primary");
				label.setParent(lc);
				
				
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
						if (getDisbursementListModelList().get(0).getId().compareTo(Long.MIN_VALUE)==0) {
							// 'New' in progress
							return;
						}
						log.info("Edit " + disbursement.toString());
						formTitleLabel.setValue("Disbursement - Edit");
						// set selected - update to db later
						disbursementListbox.setSelectedItem(item);
						
						disbursementTypeCombobox.setDisabled(false);
						disbursementDatebox.setDisabled(false);
						isDepositCheckbox.setDisabled(false);
						isPettyCashCheckbox.setDisabled(false);
						theSumOfTextbox.setDisabled(false);
						theSumOfTextbox.setReadonly(false);
						theSumOfTextbox.setValue(disbursement.getTheSumOf().toString());
						descriptionTextbox.setDisabled(false);
						descriptionTextbox.setReadonly(false);
						referenceTextbox.setDisabled(false);
						referenceTextbox.setReadonly(false);
						batalCheckbox.setDisabled(false);
						
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
									lc.setStyle("vertical-align: top;");
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
									lc.setStyle("vertical-align: top;");
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
		isDepositCheckbox.setChecked(disbursement.isDeposit());
		isPettyCashCheckbox.setChecked(disbursement.isPettyCash());
		theSumOfTextbox.setValue(toDecimalFormat(
				disbursement.getTheSumOf(), 
				getSettingLocale(),
				getSettings().getDecimalFormat()));
		descriptionTextbox.setValue(disbursement.getDescription());
		referenceTextbox.setValue(disbursement.getDocumentRef());
		batalCheckbox.setChecked(
				disbursement.getDisbursementStatus().equals(DisbursementStatus.BATAL));
		
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
					textbox.setPlaceholder("Click for data entry");
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
					// textbox.setAttribute("amounttb", detail.getAmount());
					textbox.setParent(lc);
					textbox.addEventListener(Events.ON_BLUR, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							log.info("check for non decimal value.");
							Textbox jumlahTextbox = (Textbox) event.getTarget();
							
							BigDecimal decimalValue = getDecimalValue(jumlahTextbox.getValue());
							log.info("verified decimal value: "+
									toDecimalFormat(decimalValue, 
											getSettingLocale(), 
											getSettings().getDecimalFormat()));
						}
					});
				} else {
					lc = new Listcell(toDecimalFormat(
							detail.getAmount(), 
							getSettingLocale(), 
							getSettings().getDecimalFormat()));
					lc.setAttribute("amountlc", detail.getAmount());
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

	public void onAfterRender$disbursementListListbox(Event event) throws Exception {
		log.info("Display the total from details");
		BigDecimal total = getDisbursementDetailTotal();
		totalDisbursementLabel.setValue(toDecimalFormat(total, getSettingLocale(), getSettings().getDecimalFormat()));
	}
	
	public void onSelect$disbursementListbox(Event event) throws Exception {
		if ((isAllowEditDetails()) || (getDisbursementListModelList().get(0).getId().compareTo(Long.MIN_VALUE)==0)) {
			// cancel the 'new' or 'edit' operation
			cancelOperation();
			// do not continue
			return;
		}
		Disbursement disbursement = disbursementListbox.getSelectedItem().getValue();
		log.info("Select: "+disbursement.toString());
		displayDisbursementInfo(disbursement);
	}
	
	public void onClick$filterListButton(Event event) throws Exception {
		log.info("Filter Disbursement");
		List<Disbursement> disbursementList = null;
		int year;
		Date startDate = null;
		Date endDate = null;
		DisbursementType disbursementType = typeListCombobox.getSelectedItem().getValue();
		
		if ((yearCombobox.getSelectedItem().getValue()==null) && (monthCombobox.getSelectedItem().getValue()==null)) {
			// disbursementType not null and year and month are null -- select by disbursementType
			log.info("select by disbursementType");
			disbursementList = getDisbursementDao().findDisbursementByType_Date(disbursementType, null, null, false);
		} else if (monthCombobox.getSelectedItem().getValue()==null) {
			log.info("select year");
			// year not null, month is null -- select for the year ONLY
			year = yearCombobox.getSelectedItem().getValue();
			startDate = asDate(LocalDate.of(year, Month.JANUARY, 1), ZoneId.of(getSettings().getTimeZoneId()));
			endDate = asDate(LocalDate.of(year, Month.DECEMBER, 31), ZoneId.of(getSettings().getTimeZoneId()));
			disbursementList = 
					getDisbursementDao().findDisbursementByType_Date(disbursementType, startDate, endDate, false);			
		} else if (yearCombobox.getSelectedItem().getValue()==null) {
			// year is null, month not null -- error
			throw new SuppressedException("Invalid Filter");
		} else {
			log.info("select year and month");
			year = yearCombobox.getSelectedItem().getValue();
			Month month = monthCombobox.getSelectedItem().getValue();
			
			startDate = asDate(LocalDate.of(year, month, 1), ZoneId.of(getSettings().getTimeZoneId()));
			int dateLength = month.length(LocalDate.of(year, month, 1).isLeapYear());
			endDate = asDate(LocalDate.of(year, month, dateLength), ZoneId.of(getSettings().getTimeZoneId()));
			disbursementList = 
					getDisbursementDao().findDisbursementByType_Date(disbursementType, startDate, endDate, false);
		}
		// display
		displayDisbursementListInfo(disbursementList);
		
		if (getDisbursementListModelList().isEmpty()) {
			resetComponents();
		} else {
			// select 1st item on the list
			Disbursement selDisbursement = getDisbursementListModelList().get(0);
			// display detail info
			displayDisbursementInfo(selDisbursement);			
		}
	}
	
	public void onClick$addButton(Event event) throws Exception {
		// log.info("Add Disbursement : User : "+getLoginUser().getUser_name());
		if (isAllowEditDetails()) {
			return;
		}
		log.info("Add Disbursement");
		formTitleLabel.setValue("Disbursement - New");
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
		isDepositCheckbox.setChecked(false);
		// not petty-cash since type is defaulted to bank
		isPettyCashCheckbox.setChecked(false);
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
		referenceTextbox.setDisabled(false);
		referenceTextbox.setReadonly(false);
		referenceTextbox.setValue("");
		// ref document
		// referenceIframe.setVisible(false);
		// referenceImage.setVisible(false);
		
		// status set to 'OK' -- not BATAL
		batalCheckbox.setChecked(false);
		disbursement.setDisbursementStatus(DisbursementStatus.OK);
		
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
		if (getDecimalValue(theSumOfTextbox.getValue()).compareTo(BigDecimal.ZERO)==0) {
			throw new SuppressedException("Jumlah Disbursement 0 (nol) TIDAK diperkenankan.");
		}
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

	public void onClick$saveButton(Event event) throws Exception {
		Long id;

		Disbursement disbursement = getDisbursementListModelList().get(0);
		// check total
		BigDecimal disbursementTotal = getDecimalValue(theSumOfTextbox.getValue());
		log.info(disbursementTotal);
		BigDecimal disbursementDetailTotal = getDisbursementDetailTotal();
		log.info(disbursementDetailTotal);
		if (disbursementTotal.compareTo(disbursementDetailTotal)!=0) {
			throw new SuppressedException("Jumlah Tidak Sesuai");
		}
		if (disbursement.getId().compareTo(Long.MIN_VALUE)==0) {
			disbursement = getModifiedDisbursement(disbursement);
			log.info("Save : "+disbursement.toString());
			id = getDisbursementDao().save(disbursement);
			disbursement = getDisbursementDao().findDisbursementById(id);
			log.info("Save : Verify : "+disbursement.toString());
		} else {
			disbursement = disbursementListbox.getSelectedItem().getValue();
			disbursement = getModifiedDisbursement(disbursement);
			log.info("Update : "+disbursement.toString());
			getDisbursementDao().update(disbursement);
			id = disbursement.getId();
			disbursement = getDisbursementDao().findDisbursementById(id);
			log.info("Update : Verify : "+disbursement.toString());			
		}
		formTitleLabel.setValue("Disbursement");
		// disable / make readOnly
		disableComponents();
		// re-display
		displayDisbursementList();
		// get and display the last item on the list
		int lastIndex = getDisbursementListModelList().size()-1;
		Disbursement selDisbursement = getDisbursementListModelList().get(lastIndex);
		// set as selected
		disbursementListbox.setSelectedIndex(lastIndex);
		// display selected info
		displayDisbursementInfo(selDisbursement);
	}

	private void disableComponents() {
		log.info("Disable all components");
		// disable the components
		disbursementDatebox.setDisabled(true);
		disbursementTypeCombobox.setDisabled(true);
		isDepositCheckbox.setDisabled(true);
		theSumOfTextbox.setDisabled(true);
		theSumOfTextbox.setReadonly(true);
		descriptionTextbox.setDisabled(true);
		descriptionTextbox.setReadonly(true);
		referenceTextbox.setDisabled(true);
		referenceTextbox.setReadonly(true);
		// disable the details button
		addDetailButton.setDisabled(true);
		batalCheckbox.setDisabled(true);
		
		// do not allow to edit details
		setAllowEditDetails(false);
		// disable the button
		saveButton.setDisabled(true);
		cancelButton.setDisabled(true);
	}
	
	private void resetComponents() {
		log.info("Reset all components");
		disbursementNoCompTextbox.setValue("");
		disbursementTypeCombobox.setSelectedIndex(0);
		disbursementDatebox.setValue(null);
		isDepositCheckbox.setChecked(false);
		isPettyCashCheckbox.setChecked(false);
		theSumOfTextbox.setValue("");
		descriptionTextbox.setValue("");
		referenceTextbox.setValue("");
		batalCheckbox.setChecked(false);
		
		setDisbursementDetailListModelList(new ListModelList<DisbursementDetail>());
		disbursementListListbox.setModel(getDisbursementDetailListModelList());
		disbursementListListbox.setItemRenderer(getDisbursementDetailListitemRenderer());
	}
	
	private Disbursement getModifiedDisbursement(Disbursement disbursement) throws Exception {
		DisbursementSerialNumber disbursementSerialNumber = null;
		
		disbursement.setTheSumOf(getDecimalValue(theSumOfTextbox.getValue()));
		disbursement.setDisbursementDate(disbursementDatebox.getValue());
		disbursement.setDescription(descriptionTextbox.getValue());
		disbursement.setDisbursementType(disbursementTypeCombobox.getSelectedItem().getValue());
		disbursement.setDocumentRef(referenceTextbox.getValue());
		disbursement.setDeposit(isDepositCheckbox.isChecked());
		disbursement.setPettyCash(isPettyCashCheckbox.isChecked());
		disbursement.setDisbursementStatus(
				batalCheckbox.isChecked()? DisbursementStatus.BATAL : DisbursementStatus.OK);
		
		ZoneId zoneId = ZoneId.of(getSettings().getTimeZoneId());
		LocalDateTime localDateTime = getLocalDateTime(zoneId);
		if (disbursement.getId().compareTo(Long.MIN_VALUE)==0) {
			// re-create the disbursement number
			disbursementSerialNumber =
					createDisbursementSerialNumber(disbursementTypeCombobox.getSelectedItem().getValue());
			// set disbursement serial number
			disbursement.setDisbursementSerialNumber(disbursementSerialNumber);
			
			disbursement.setCreateDate(Date.from(localDateTime.atZone(zoneId).toInstant()));
			disbursement.setModifiedDate(Date.from(localDateTime.atZone(zoneId).toInstant()));
		} else {
			disbursement.setModifiedDate(Date.from(localDateTime.atZone(zoneId).toInstant()));			
		}
		// details
		disbursement.setDisbursementDetails(getDisbursementDetails(disbursement));
		
		return disbursement;
	}

	private List<DisbursementDetail> getDisbursementDetails(Disbursement disbursement) {
		List<DisbursementDetail> disbursementDetailList = null;
		if (disbursement.getId().compareTo(Long.MIN_VALUE)==0) {
			// create new list
			disbursementDetailList = new ArrayList<DisbursementDetail>();
		} else {
			// clear the list
			disbursement.getDisbursementDetails().clear();
			disbursementDetailList = disbursement.getDisbursementDetails();
		}
		
		for (Listitem item : disbursementListListbox.getItems()) {
			DisbursementDetail disbursementDetail = new DisbursementDetail();
			// keterangan
			Textbox descriptionTextbox = (Textbox) item.getChildren().get(1).getFirstChild();
			disbursementDetail.setDescription(descriptionTextbox.getValue());			
			// jumlah
			Textbox amountTextbox = (Textbox) item.getChildren().get(2).getFirstChild();
			disbursementDetail.setAmount(getDecimalValue(amountTextbox.getValue()));
			
			disbursementDetailList.add(disbursementDetail);
		}
		
		return disbursementDetailList;
	}

	public void onClick$cancelButton(Event event) throws Exception {
		cancelOperation();
	}

	private void cancelOperation() throws Exception {
		formTitleLabel.setValue("Disbursement");
		displayDisbursementList();
		// display 1st row onto the 'right pane'
		if (getDisbursementListModelList().isEmpty()) {
			return;
		} else {
			// indicate it's selected
			disbursementListbox.setSelectedIndex(0);
			// get the value
			Disbursement selDisbursement = getDisbursementListModelList().get(0);
			// display selected
			displayDisbursementInfo(selDisbursement);
		}
		// disable components
		disableComponents();
		
		cancelButton.setDisabled(true);
		saveButton.setDisabled(true);
	}

	private void setUpComboitemAllSelect(Combobox combobox) {
		Comboitem comboitem;
		comboitem = new Comboitem();
		comboitem.setLabel("--All--");
		comboitem.setValue(null);
		comboitem.setParent(combobox);
	}

	private BigDecimal getDisbursementDetailTotal() {
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal amount = BigDecimal.ZERO;
		for (Listitem item : disbursementListListbox.getItems()) {
			// jumlah
			log.info(item.getChildren().get(2).getChildren());
			if (item.getChildren().get(2).getChildren().isEmpty()) {
				Listcell lc = (Listcell) item.getChildren().get(2);
				amount = (BigDecimal) lc.getAttribute("amountlc");
			} else {
				Textbox textbox = (Textbox) item.getChildren().get(2).getFirstChild();
				amount = getDecimalValue(textbox.getValue());
			}
			log.info("amount: "+amount);
			total = total.add(amount);
		}

		return total;
	}

	private BigDecimal getDecimalValue(String stringValue) {
		try {

			return BigDecimal.valueOf(Double.valueOf(stringValue));
			
		} catch (Exception e) {
			throw new SuppressedException("Huruf TIDAK diperkenankan di kolom Jumlah.");
		}
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
