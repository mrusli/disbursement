package com.pyramix.web;

import java.time.LocalDateTime;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import com.pyramix.domain.settings.Settings;
import com.pyramix.persistence.settings.dao.SettingsDao;

@SpringBootApplication
@ImportResource({
	"classpath*:ApplicationContext-GuiController.xml"})
public class DisbursementApplication {

	@Autowired
	private SettingsDao settingsDao;

	private static final Logger log = Logger.getLogger(DisbursementApplication.class);
	private final Long DEFAULT_SETTINGS_ID = 1L;
	
	@PostConstruct
	public void init() throws Exception {
		log.info("---JVM to use User Defined Settings---");
		Settings settings = 
				getSettingsDao().findSettingsById(DEFAULT_SETTINGS_ID);
		
		TimeZone.setDefault(TimeZone.getTimeZone(settings.getTimeZoneId()));
		log.info("---Setting TimeZone to: "+TimeZone.getDefault().getDisplayName()+" ["+TimeZone.getDefault().getID()+"]");
		log.info("---Synchronize the Clock to: "+LocalDateTime.now());
		log.info("--------------------------------------");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(DisbursementApplication.class, args);
	}

	public SettingsDao getSettingsDao() {
		return settingsDao;
	}

	public void setSettingsDao(SettingsDao settingsDao) {
		this.settingsDao = settingsDao;
	}

}
