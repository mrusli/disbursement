package com.pyramix.persistence.settings.dao;

import java.util.List;

import com.pyramix.domain.settings.Settings;

public interface SettingsDao {

	/**
	 * Find by Id
	 * 
	 * @param id
	 * @return {@link Settings}
	 * @throws Exception
	 */
	public Settings findSettingsById(long id) throws Exception;
	
	/**
	 * Find all
	 * 
	 * @return {@link List} of {@link Settings}
	 * @throws Exception
	 */
	public List<Settings> findAllSettings() throws Exception;
	
	/**
	 * Save
	 * 
	 * @param settings
	 * @return {@link Long}
	 * @throws Exception
	 */
	public Long save(Settings settings) throws Exception;
	
	/**
	 * Update
	 * 
	 * @param settings
	 * @throws Exception
	 */
	public void update(Settings settings) throws Exception;
	
	/**
	 * Delete
	 * 
	 * @param settings
	 * @throws Exception
	 */
	public void delete(Settings settings) throws Exception;
	
}
