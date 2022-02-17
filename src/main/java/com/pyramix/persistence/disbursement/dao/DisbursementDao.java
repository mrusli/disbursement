package com.pyramix.persistence.disbursement.dao;

import java.util.List;

import com.pyramix.domain.disbursement.Disbursement;

public interface DisbursementDao {
	/**
	 * Find {@link Disbursement} by id
	 * 
	 * @param id
	 * @return {@link Disbursement}
	 * @throws Exception
	 */
	public Disbursement findDisbursementById(long id) throws Exception;
	
	/**
	 * Find all {@link Disbursement}
	 * 
	 * @return {@link List} of {@link Disbursement}
	 * @throws Exception
	 */
	public List<Disbursement> findAllDisbursement() throws Exception;
	
	/**
	 * Save a disbursement ( {@link Disbursement} ) object to the database table
	 * 
	 * @param disbursement
	 * @return {@link Long}
	 * @throws Exception
	 */
	public Long save(Disbursement disbursement) throws Exception;
	
	/**
	 * Update an existing disbursement ( {@link Disbursement} ) object in the database table
	 * 
	 * @param disbursement
	 * @throws Exception
	 */
	public void update(Disbursement disbursement) throws Exception;
	
	/**
	 * Delete an existing disbursement ( {@link Disbursement} ) object in the database table
	 * 
	 * @param disbursement
	 * @throws Exception
	 */
	public void delete(Disbursement disbursement) throws Exception;
}
