package com.pyramix.web.disbursement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.pyramix.domain.disbursement.Disbursement;
import com.pyramix.domain.disbursement.DisbursementStatus;
import com.pyramix.domain.disbursement.DisbursementType;
import com.pyramix.domain.disbursement.report.DisbursementReportDetail;
import com.pyramix.domain.disbursement.report.DisbursementReportHeader;
import com.pyramix.domain.settings.Settings;
import com.pyramix.persistence.disbursement.dao.DisbursementDao;
import com.pyramix.persistence.disbursement.report.dao.DisbursementReportDao;
import com.pyramix.persistence.settings.dao.SettingsDao;
import com.pyramix.web.common.GFCBaseController;
import com.pyramix.web.common.SuppressedException;
import com.pyramix.web.dialogs.DecimalboxDialogData;

public class DisbursementReportControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3890475655420108523L;

	private SettingsDao settingsDao;
	private DisbursementDao disbursementDao; 
	private DisbursementReportDao disbursementReportDao;
	
	private Label formTitleLabel, infoResultlabel;
	private Listbox disbursementReportHeaderListbox, disbursementListListbox;
	private Combobox yearCombobox, typeListCombobox, yearReportCombobox, monthReportCombobox;
	private Button filterListButton, saveButton, printButton;
	
	private Locale settingLocale;	
	private Settings settings;
	private BigDecimal startingBalance = BigDecimal.ZERO;
	private LocalDate startingBalanceDate;
	private List<Disbursement> disbursementList;
	private List<DisbursementReportHeader> reportHeaderList;
	
	private ListModelList<DisbursementReportHeader> disbursementReportHeaderListModelList;
	
	private final Long DEFAULT_SETTINGS_ID = 1L;	
	
	private static final Logger log = Logger.getLogger(DisbursementReportControl.class);
	
	public void onCreate$disbursementReportWin(Event event) throws Exception {
		log.info("onCreate$disbursementReportWin");
		
		formTitleLabel.setValue("Disbursement Report");

		setSettings(
				getSettingsDao().findSettingsById(DEFAULT_SETTINGS_ID));
		setSettingLocale(new Locale(getSettings().getLanguageCode(),
				getSettings().getCountryCode()));		

		// list of report headers
		reportHeaderList = getDisbursementReportDao().findAllReports();
		// list disbursement report header (saved reports)
		listAllDisbursementReportHeader();
		// 'Thn' report header combobox selection
		setupDisbursementReportHeaderYearList();
		
		// find all non-'batal' disbursements - do it here so that we can get the min and max year
		disbursementList =
				getDisbursementDao().findAll_NonBatal_Disbursement();
		
		// 'Tipe' combobox selection
		setupDisbursementTypeList();
		// 'Thn' combobox selection
		setupDisbursementYearList();
		// 'Bln' combobox selection
		setupDisbursementMonthList();
	}

	private void setupDisbursementReportHeaderYearList() {
		if (reportHeaderList.isEmpty()) {
			filterListButton.setDisabled(true);
			return;
		}
		List<Date> listOfDates = new ArrayList<Date>();
		// get all trans peirod dates from disbursementList
		for(DisbursementReportHeader reportHeader : reportHeaderList) {
			Date transPeriodDate = reportHeader.getReportPeriodDate();
			listOfDates.add(transPeriodDate);
		}
		// min and max date
		Date minDate = Collections.min(listOfDates);
		Date maxDate = Collections.max(listOfDates);
		// min and max year
		int minYear = asLocalDate(minDate).getYear();
		int maxYear = asLocalDate(maxDate).getYear();
		
		// clear (in case)
		yearCombobox.getItems().clear();
		
		Comboitem comboitem = null;

		comboitem = new Comboitem();
		comboitem.setLabel("--All--");
		comboitem.setValue(null);
		comboitem.setParent(yearCombobox);
		
		for (int i = minYear; i <= maxYear; i++) {
			comboitem = new Comboitem();
			comboitem.setLabel(String.valueOf(i));
			comboitem.setValue(i);
			comboitem.setParent(yearCombobox);
		}
		// select --All--
		yearCombobox.setSelectedIndex(0);
		// enable filter button
		filterListButton.setDisabled(false);
	}	
	
	private void listAllDisbursementReportHeader() throws Exception {
		int totalReports = reportHeaderList.size();
		infoResultlabel.setValue("Total: "+totalReports+" Report - ");
		
		setDisbursementReportHeaderListModelList(
				new ListModelList<DisbursementReportHeader>(reportHeaderList));
		
		disbursementReportHeaderListbox.setModel(
				getDisbursementReportHeaderListModelList());
		disbursementReportHeaderListbox.setItemRenderer(
				getDisbursementReportHeaderListitemRenderer());
	}

	private ListitemRenderer<DisbursementReportHeader> getDisbursementReportHeaderListitemRenderer() {
		
		return new ListitemRenderer<DisbursementReportHeader>() {

			@Override
			public void render(Listitem item, DisbursementReportHeader header, int index) throws Exception {
				Listcell lc;
				
				// Tgl.
				lc = new Listcell(dateToStringDisplay(
						asLocalDate(header.getReportDate()), 
						getSettings().getDateFormatShort(), 
						getSettingLocale()));
				lc.setParent(item);
				
				// Report-Title
				lc = new Listcell(header.getReportTitle());
				lc.setParent(item);
				
				// Trans-Date
				lc = new Listcell(header.getReportPeriod());
				lc.setParent(item);
				
				// Delete - width="45px"
				lc = initDeleteReportHeader(new Listcell(), header);
				lc.setParent(item);
				
				item.setValue(header);
			}

			private Listcell initDeleteReportHeader(Listcell listcell, DisbursementReportHeader header) {
				Button button = new Button();
				button.setIconSclass("z-icon-trash");
				button.setWidth("28px");
				button.setHeight("23px");
				button.setSclass("delItemButton");
				button.setParent(listcell);
				button.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// log.info("Delete " + header.toString());
						// asking for confirmation
						Messagebox.show("Hapus Laporan Ini?",
							    "Confirmation", 
							    Messagebox.YES | Messagebox.CANCEL,  
							    Messagebox.QUESTION, new EventListener<Event>() {
									
									@Override
									public void onEvent(Event event) throws Exception {
										if (Messagebox.ON_YES.equals(event.getName())) {
											log.info("Confirmed Delete " + header.toString());
											// delete
											getDisbursementReportDao().delete(header);
											
											// list of report headers
											reportHeaderList = getDisbursementReportDao().findAllReports();
											// list disbursement report header (saved reports)
											listAllDisbursementReportHeader();
											// 'Thn' report header combobox selection
											setupDisbursementReportHeaderYearList();											
											
											// clean / display a report into disbursementListListbox
											listDisbursement();
										} else {
											log.info("Cancel Delete...");
										}
									}
								});
					}
				});
								
				return listcell;
			}
		};
	}

	private void listDisbursement() {
		// clean up the disbursement report detail
		disbursementListListbox.setModel(
				new ListModelList<DisbursementReportDetail>());
		disbursementListListbox.setItemRenderer(getDisbursementReportDetailListitemRenderer());
		
		// header report is empty?
		if (getDisbursementReportHeaderListModelList().isEmpty()) {
			// do nothing
		} else {
			// get the last index to display
			int lastIndex = getDisbursementReportHeaderListModelList().size()-1;		
			disbursementReportHeaderListbox.setSelectedIndex(lastIndex);
			// get the reportHeader			 
			DisbursementReportHeader reportHeader =
					getDisbursementReportHeaderListModelList().get(lastIndex);			
			// display
			disbursementListListbox.setModel(
					new ListModelList<DisbursementReportDetail>(reportHeader.getReportDetails()));
			disbursementListListbox.setItemRenderer(getDisbursementReportDetailListitemRenderer());			
		}
	}

	public void onClick$filterListButton(Event event) throws Exception {
		List<DisbursementReportHeader> reportHeaders = null;
		
		if (yearCombobox.getSelectedIndex()==0) {
			// --All--
			log.info("Filter: --All--");
			reportHeaders =
					getDisbursementReportDao().findAllReports();
		} else {
			// filter
			int year = yearCombobox.getSelectedItem().getValue();
			log.info("Filter: "+year);
			
			reportHeaders =
					getDisbursementReportDao().findAllReportsByYear(year);
			
		}

		int totalReports = reportHeaders.size();
		infoResultlabel.setValue("Total: "+totalReports+" Report - ");
		
		setDisbursementReportHeaderListModelList(
				new ListModelList<DisbursementReportHeader>(reportHeaders));			
		
		disbursementReportHeaderListbox.setModel(
				getDisbursementReportHeaderListModelList());
		disbursementReportHeaderListbox.setItemRenderer(
				getDisbursementReportHeaderListitemRenderer());
	}
	
	public void onSelect$disbursementReportHeaderListbox(Event event) throws Exception {
		log.info("reportHeader clicked...");
		DisbursementReportHeader reportHeader = 
				disbursementReportHeaderListbox.getSelectedItem().getValue();
		
		disbursementListListbox.setModel(
				new ListModelList<DisbursementReportDetail>(reportHeader.getReportDetails()));
		disbursementListListbox.setItemRenderer(getDisbursementReportDetailListitemRenderer());
		
		// allow to print
		printButton.setDisabled(false);
	}
	
	private ListitemRenderer<DisbursementReportDetail> getDisbursementReportDetailListitemRenderer() {
		
		return new ListitemRenderer<DisbursementReportDetail>() {
			
			@Override
			public void render(Listitem item, DisbursementReportDetail detail, int index) throws Exception {
				Listcell lc;
				
				// Tgl.
				lc = new Listcell(dateToStringDisplay(
						asLocalDate(detail.getDisbursementDate()), 
						getSettings().getDateFormatShort(), 
						getSettingLocale()));
				lc.setParent(item);

				// Tipe
				lc = new Listcell(detail.getDisbursementType().toString());
				lc.setParent(item);
				
				// No.
				lc = new Listcell(detail.getDisbursementSerialNo());
				lc.setParent(item);
				
				// Description
				lc = new Listcell(detail.getDescription());										
				lc.setParent(item);
				
				// Jumlah
				lc = new Listcell(toDecimalFormat(
						detail.getTheSumOf(), 
						getSettingLocale(), 
						getSettings().getDecimalFormat()));
				lc.setParent(item);
				
				// 
				if (detail.isDeposit()) {
					lc = new Listcell("CR");
				} else {
					lc = new Listcell("");
				}
				lc.setParent(item);
				
			}
		};
	}

	private void setupDisbursementTypeList() {
		Comboitem comboitem;

		for (DisbursementType disbursementType : DisbursementType.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(disbursementType.toString());
			comboitem.setValue(disbursementType);
			comboitem.setParent(typeListCombobox);			
		}
		
		typeListCombobox.setSelectedIndex(0);
	}

	private void setupDisbursementYearList() {
		List<Date> listOfDates = new ArrayList<Date>();
		// get all dates from disbursementList
		for(Disbursement disbursement : disbursementList) {
			Date disbursementDate = disbursement.getDisbursementDate();
			listOfDates.add(disbursementDate);
		}
		// min and max date
		Date minDate = Collections.min(listOfDates);
		Date maxDate = Collections.max(listOfDates);
		// min and max year
		int minYear = asLocalDate(minDate).getYear();
		int maxYear = asLocalDate(maxDate).getYear();
		
		Comboitem comboitem = null;

		for (int i = minYear; i <= maxYear; i++) {
			comboitem = new Comboitem();
			comboitem.setLabel(String.valueOf(i));
			comboitem.setValue(i);
			comboitem.setParent(yearReportCombobox);
		}
		// select the current year
		yearReportCombobox.setSelectedItem(comboitem);
	}

	private void setupDisbursementMonthList() {
		Comboitem comboitem;
		
		for (Month month : Month.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(month.getDisplayName(TextStyle.SHORT, getSettingLocale()));
			comboitem.setValue(month);
			comboitem.setParent(monthReportCombobox);
		}
		// get current month index
		LocalDate currLocalDate = getLocalDate(ZoneId.of(getSettings().getTimeZoneId()));
		int currMonthIndex = currLocalDate.getMonthValue()-1;
		// select current month
		monthReportCombobox.setSelectedIndex(currMonthIndex);
	}

	public void onClick$generateReportButton(Event event) throws Exception {
		// 'Tipe'
		DisbursementType selDisbursementType = typeListCombobox.getSelectedItem().getValue();
		// 'Thn'
		int year = yearReportCombobox.getSelectedItem().getValue();		
		// previous 'Month'
		Month month = monthReportCombobox.getSelectedItem().getValue();
		// month is January - need to look for saldo previous year in December
		if (month.equals(Month.JANUARY)) {
			year = year - 1;
		}
		month = month.minus(1L);
		// end day of previous 'Month'
		int endDay = month.length(Year.isLeap(year));
		
		startingBalanceDate = LocalDate.of(year, month, endDay);
		
		// prompt for starting balance - param req: prompt text; decimalbox value; decimalbox locale
		DecimalboxDialogData dialogData = new DecimalboxDialogData();
		dialogData.setPromptText("Saldo "+selDisbursementType.toString()+" per "+endDay+"-"+month.toString()+"-"+year+" Rp.:");
		dialogData.setDecimalboxLocale(getSettingLocale());
		dialogData.setDecimalboxValue(startingBalance);
		
		Map<String, DecimalboxDialogData> arg =
				Collections.singletonMap("decimalboxDialogData", dialogData);
		
		Window decimalboxDialogWin =
				(Window) Executions.createComponents("~./zul/secure/dialogs/DecimalboxDialog.zul", null, arg);
		
		decimalboxDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// get the startingBalance value from the dialog
				startingBalance = (BigDecimal) event.getData();
				// 'Tipe'
				DisbursementType selDisbursementType = typeListCombobox.getSelectedItem().getValue();
				// start and end date
				Date startDate = asDate(getStartDate(), ZoneId.of(getSettings().getTimeZoneId()));
				Date endDate = asDate(getEndDate(), ZoneId.of(getSettings().getTimeZoneId()));
				
				log.info("DisbursementType: "+selDisbursementType.toString()+
						" - startDate: "+startDate.toString()+
						" - endDate: "+endDate.toString());
				
				// non 'batal' disbursement ONLY
				disbursementList = getDisbursementDao().findAll_NonBatal_Disbursement_ByType_Date(
						selDisbursementType, startDate, endDate);
				disbursementList.add(0, startingBalance());
				
				// calc total 'setoran' - includes startingBalance
				BigDecimal totalSetoran = getTotalSetoran(disbursementList);
				log.info("TotalSetoran: "+totalSetoran);
				// calc total 'expenditure'
				BigDecimal totalExpenditure = getTotalExpenditure(disbursementList);
				log.info("TotalExpenditure: "+totalExpenditure);
				log.info("StartingBalance: "+startingBalance);
				// calc endingBalance : total 'setoran' - total 'expenditure'
				BigDecimal endingBalance = totalSetoran.subtract(totalExpenditure);
				
				int lastIndex = disbursementList.size();
				disbursementList.add(lastIndex, endingBalance(endDate, endingBalance));
				
				disbursementListListbox.setModel(new ListModelList<Disbursement>(
						disbursementList));
				disbursementListListbox.setItemRenderer(getDisbursementListitemRenderer());
				
				// enable save
				saveButton.setDisabled(false); 
			}
		});
		
		decimalboxDialogWin.doModal();
	}

	private LocalDate getStartDate() {
		// 'Thn'
		int year = yearReportCombobox.getSelectedItem().getValue();
		// 'Month'
		Month month = monthReportCombobox.getSelectedItem().getValue();
		// start day
		int startDay = 1;
		
		return LocalDate.of(year, month, startDay);
	}
	
	private LocalDate getEndDate() {
		// 'Thn'
		int year = yearReportCombobox.getSelectedItem().getValue();
		// 'Month'
		Month month = monthReportCombobox.getSelectedItem().getValue();		
		// end day
		int endDay = month.length(Year.of(year).isLeap());
		
		return LocalDate.of(year, month, endDay);
	}
	
	protected BigDecimal getTotalSetoran(List<Disbursement> disbursementList) {
		BigDecimal totalSetoran = BigDecimal.ZERO;
		for (Disbursement disbursement : disbursementList) {
			if (disbursement.isDeposit()) {
				totalSetoran = totalSetoran.add(disbursement.getTheSumOf());
			}
		}

		return totalSetoran;
	}

	protected BigDecimal getTotalExpenditure(List<Disbursement> disbursementList) {
		BigDecimal totalExpenditure = BigDecimal.ZERO;
		for (Disbursement disbursement : disbursementList) {
			if (!disbursement.isDeposit()) {
				totalExpenditure = totalExpenditure.add(disbursement.getTheSumOf());
			}
		}
		
		return totalExpenditure;
	}

	private Disbursement startingBalance() {
		Disbursement disbursementAsBalance = new Disbursement();
		disbursementAsBalance.setTheSumOf(startingBalance);
		disbursementAsBalance.setDisbursementDate(asDate(startingBalanceDate, ZoneId.of(getSettings().getTimeZoneId())));
		disbursementAsBalance.setDescription("Saldo Awal");
		disbursementAsBalance.setDisbursementType(typeListCombobox.getSelectedItem().getValue());
		disbursementAsBalance.setDeposit(true);
		disbursementAsBalance.setPettyCash(false);
		disbursementAsBalance.setDocumentRef("");
		disbursementAsBalance.setCreateDate(asDate(getLocalDate(ZoneId.of(getSettings().getTimeZoneId())), 
				ZoneId.of(getSettings().getTimeZoneId())));
		disbursementAsBalance.setModifiedDate(asDate(getLocalDate(ZoneId.of(getSettings().getTimeZoneId())), 
				ZoneId.of(getSettings().getTimeZoneId())));
		disbursementAsBalance.setDisbursementSerialNumber(null);
		disbursementAsBalance.setDisbursementStatus(DisbursementStatus.OK);
		disbursementAsBalance.setDisbursementDetails(null);
		
		return disbursementAsBalance;
	}

	private Disbursement endingBalance(Date endDate, BigDecimal endingBalance) {
		Disbursement disbursementAsBalance = new Disbursement();
		disbursementAsBalance.setTheSumOf(endingBalance);
		disbursementAsBalance.setDisbursementDate(endDate);
		disbursementAsBalance.setDescription("Saldo Akhir");
		disbursementAsBalance.setDisbursementType(typeListCombobox.getSelectedItem().getValue());
		disbursementAsBalance.setDeposit(true);
		disbursementAsBalance.setPettyCash(false);
		disbursementAsBalance.setDocumentRef("");
		disbursementAsBalance.setCreateDate(asDate(getLocalDate(ZoneId.of(getSettings().getTimeZoneId())), 
				ZoneId.of(getSettings().getTimeZoneId())));
		disbursementAsBalance.setModifiedDate(asDate(getLocalDate(ZoneId.of(getSettings().getTimeZoneId())), 
				ZoneId.of(getSettings().getTimeZoneId())));
		disbursementAsBalance.setDisbursementSerialNumber(null);
		disbursementAsBalance.setDisbursementStatus(DisbursementStatus.OK);
		disbursementAsBalance.setDisbursementDetails(null);
		
		return disbursementAsBalance;

	}

	private ListitemRenderer<Disbursement> getDisbursementListitemRenderer() {
		
		return new ListitemRenderer<Disbursement>() {
			
			@Override
			public void render(Listitem item, Disbursement disbursement, int index) throws Exception {
				Listcell lc;
				
				// Tgl.
				lc = new Listcell(dateToStringDisplay(
						asLocalDate(disbursement.getDisbursementDate()), 
						getSettings().getDateFormatShort(), 
						getSettingLocale()));
				lc.setParent(item);
				
				// Tipe
				lc = new Listcell(disbursement.getDisbursementType().toString());
				lc.setParent(item);
				
				// No.
				lc = new Listcell(disbursement.getDisbursementSerialNumber()==null ?
						"" :
						disbursement.getDisbursementSerialNumber().getSerialComp());
				lc.setParent(item);
				
				// Description
				if (disbursement.isDeposit()) {
					lc = new Listcell(disbursement.getDescription()+" (Setoran)");					
				} else {
					lc = new Listcell(disbursement.getDescription());										
				}
				lc.setParent(item);
				
				// Jumlah
				lc = new Listcell(toDecimalFormat(
						disbursement.getTheSumOf(), 
						getSettingLocale(), 
						getSettings().getDecimalFormat()));
				lc.setParent(item);
				
				// 
				if (disbursement.isDeposit()) {
					lc = new Listcell("CR");
				} else {
					lc = new Listcell("");
				}
				lc.setParent(item);
			}
		};
	}

	public void onClick$saveButton(Event event) throws Exception {
		log.info("saving report...");
		
		// for report date
		Date todayDate = asDate(getLocalDate(ZoneId.of(getSettings().getTimeZoneId())), 
				ZoneId.of(getSettings().getTimeZoneId()));
		// for report title
		DisbursementType selDisbursementType = 
				typeListCombobox.getSelectedItem().getValue();
		// 'Thn'
		int year = yearReportCombobox.getSelectedItem().getValue();		
		// 'Month'
		Month month = monthReportCombobox.getSelectedItem().getValue();
		
		// create report header and details
		DisbursementReportHeader reportHeader = new DisbursementReportHeader();
		reportHeader.setReportDate(todayDate);
		reportHeader.setReportTitle("Disbursement Report ("+selDisbursementType.toString()+")");		
		reportHeader.setReportPeriod(Integer.toString(year)+"-"+month.getDisplayName(TextStyle.SHORT, getSettingLocale()));
		reportHeader.setReportPeriodDate(asDate(LocalDate.of(year, month, 1), ZoneId.of(getSettings().getTimeZoneId())));
		reportHeader.setReportDetails(getDisbursementReportDetails());
		
		// save
		log.info("Save : "+reportHeader.toString());
		Long id = getDisbursementReportDao().save(reportHeader);
		reportHeader = getDisbursementReportDao().findReportById(id);
		log.info("Save : Verify : "+reportHeader.toString());
		
		// list of report headers
		reportHeaderList = getDisbursementReportDao().findAllReports();
		// display
		listAllDisbursementReportHeader();
		// 'Thn' report header combobox selection
		setupDisbursementReportHeaderYearList();

		int lastIndex = getDisbursementReportHeaderListModelList().size()-1;		
		disbursementReportHeaderListbox.setSelectedIndex(lastIndex);
		
		// disable save
		saveButton.setDisabled(true);
		// allow to print
		printButton.setDisabled(false);
	}
	
	private List<DisbursementReportDetail> getDisbursementReportDetails() {
		List<DisbursementReportDetail> reportDetails = new ArrayList<DisbursementReportDetail>();
		
		for (Disbursement disbursement : disbursementList) {
			DisbursementReportDetail detail = new DisbursementReportDetail();
			detail.setTheSumOf(disbursement.getTheSumOf());
			detail.setDisbursementDate(disbursement.getDisbursementDate());
			detail.setDescription(disbursement.isDeposit() ? 
				disbursement.getDescription()+" (Setoran)" :
				disbursement.getDescription());
			detail.setDisbursementType(disbursement.getDisbursementType());
			detail.setDeposit(disbursement.isDeposit());
			detail.setCreateDate(disbursement.getCreateDate());
			detail.setModifiedDate(disbursement.getModifiedDate());
			detail.setDisbursementSerialNo(disbursement.getDisbursementSerialNumber()==null ? " " : 
				disbursement.getDisbursementSerialNumber().getSerialComp());
			detail.setDisbursementStatus(disbursement.getDisbursementStatus());
			
			reportDetails.add(detail);
		}
		
		return reportDetails;
	}
	
	public void onClick$printButton(Event event) throws Exception {
		log.info("printing report...");
		
		if (disbursementReportHeaderListbox.getSelectedItem()==null) {
			throw new SuppressedException("Belum memilih laporan");
		}
		// can print
		DisbursementReportHeader reportHeader = 
				disbursementReportHeaderListbox.getSelectedItem().getValue();
		log.info(reportHeader.toString());
		// create the print package
		DisbursementPrintData printData = new DisbursementPrintData();
		printData.setSettings(getSettings());		
		printData.setReportHeader(reportHeader);
		printData.setDisbursementType(typeListCombobox.getSelectedItem().getValue());
		
		Map<String, DisbursementPrintData> arg = Collections.singletonMap("disbursementPrintData", printData);
		
		Window disbursementReportPrintWin =
				(Window) Executions.createComponents("~./zul/secure/disbursement/print/DisbursementReportPrint.zul", null, arg);
		
		disbursementReportPrintWin.doModal();
	}
	
	public SettingsDao getSettingsDao() {
		return settingsDao;
	}

	public void setSettingsDao(SettingsDao settingsDao) {
		this.settingsDao = settingsDao;
	}

	public DisbursementDao getDisbursementDao() {
		return disbursementDao;
	}

	public void setDisbursementDao(DisbursementDao disbursementDao) {
		this.disbursementDao = disbursementDao;
	}

	public DisbursementReportDao getDisbursementReportDao() {
		return disbursementReportDao;
	}

	public void setDisbursementReportDao(DisbursementReportDao disbursementReportDao) {
		this.disbursementReportDao = disbursementReportDao;
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

	public ListModelList<DisbursementReportHeader> getDisbursementReportHeaderListModelList() {
		return disbursementReportHeaderListModelList;
	}

	public void setDisbursementReportHeaderListModelList(ListModelList<DisbursementReportHeader> disbursementReportHeaderListModelList) {
		this.disbursementReportHeaderListModelList = disbursementReportHeaderListModelList;
	}
}
