package com.pyramix.persistence.disbursement.serialnumber.dao;

import java.util.List;

import com.pyramix.domain.disbursement.DisbursementSerialNumber;
import com.pyramix.domain.disbursement.DisbursementType;

public interface DisbursementSerialNumberDao {

	/**
	 * Find {@link DisbursementSerialNumber} by id value
	 * 
	 * @param id
	 * @return {@link DisbursementSerialNumber}
	 * @throws Exception
	 */
	public DisbursementSerialNumber findDisbursementSerialNumberById(long id) throws Exception;
	
	/**
	 * Find all of the {@link DisbursementSerialNumber} from database table
	 * 
	 * @return {@link List} of {@link DisbursementSerialNumber}
	 * @throws Exception
	 */
	public List<DisbursementSerialNumber> findAllDisbursementSerialNumber() throws Exception;
	
	/**
	 * Save a new {@link DisbursementSerialNumber} into the database table
	 * 
	 * @param disbursementSerialNumber
	 * @return {@link Long}
	 * @throws Exception
	 */
	public Long save(DisbursementSerialNumber disbursementSerialNumber) throws Exception;
	
	/**
	 * Update an existing {@link DisbursementSerialNumber} in the database table
	 * 
	 * @param disbursementSerialNumber
	 * @throws Exception
	 */
	public void update(DisbursementSerialNumber disbursementSerialNumber) throws Exception;
		
	/**
	 * Find the last {@link DisbursementSerialNumber} by {@link DisbursementType}
	 * 
	 * @param disbursementType
	 * @return {@link DisbursementSerialNumber}
	 * @throws Exception
	 */
	public DisbursementSerialNumber findLastDisbursementSerialNumberByType(DisbursementType disbursementType) throws Exception;
	
}
