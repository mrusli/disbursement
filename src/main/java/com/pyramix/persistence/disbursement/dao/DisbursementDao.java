package com.pyramix.persistence.disbursement.dao;

import java.util.Date;
import java.util.List;

import com.pyramix.domain.disbursement.Disbursement;
import com.pyramix.domain.disbursement.DisbursementType;

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
	
	/**
	 * Find all {@link Disbursement} by {@link DisbursementType} and start to end {@link Date}.  DisbursementType
	 * parameter maybe passed in as <code>null</code> value, therefore the query will disregard the DisbursementType.
	 * The desc parameter controls whether it will list the disbursement in descending (true) or ascending (false) order
	 * - ascending : earliest on top of the list
	 * - descending : latest on top of the list
	 * 
	 * @param disbursementType
	 * @param startDate
	 * @param endDate
	 * @param desc
	 * @return
	 * @throws Exception
	 */
	public List<Disbursement> findDisbursementByType_Date(DisbursementType disbursementType, Date startDate, Date endDate, boolean desc) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	public List<Disbursement> findAll_NonBatal_Disbursement() throws Exception;

	/**
	 * @param disbursementType
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<Disbursement> findAll_NonBatal_Disbursement_ByType_Date(DisbursementType disbursementType,
			Date startDate, Date endDate) throws Exception;
}
