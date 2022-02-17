package com.pyramix.persistence.settings.dao.hibernate;

import java.util.List;

import com.pyramix.domain.settings.Settings;
import com.pyramix.persistence.common.dao.hibernate.DaoHibernate;
import com.pyramix.persistence.settings.dao.SettingsDao;

public class SettingsHibernate extends DaoHibernate implements SettingsDao {

	@Override
	public Settings findSettingsById(long id) throws Exception {

		return (Settings) super.findById(Settings.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Settings> findAllSettings() throws Exception {

		return super.findAll(Settings.class);
	}

	@Override
	public Long save(Settings settings) throws Exception {

		return super.save(settings);
	}

	@Override
	public void update(Settings settings) throws Exception {

		super.update(settings);
	}

	@Override
	public void delete(Settings settings) throws Exception {

		super.delete(settings);
	}

}
