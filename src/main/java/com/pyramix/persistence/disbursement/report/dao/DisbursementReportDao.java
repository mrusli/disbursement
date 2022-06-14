package com.pyramix.persistence.disbursement.report.dao;

import java.util.List;

import com.pyramix.domain.disbursement.report.DisbursementReportHeader;

public interface DisbursementReportDao {

	/**
	 * Find {@link DisbursementReportHeader} by id
	 * 
	 * @param id
	 * @return {@link DisbursementReportHeader}
	 * @throws Exception
	 */
	public DisbursementReportHeader findReportById(long id) throws Exception;
	
	/**
	 * Find all {@link DisbursementReportHeader}
	 * 
	 * @return {@link List} of {@link DisbursementReportHeader}
	 * @throws Exception
	 */
	public List<DisbursementReportHeader> findAllReports() throws Exception;
	
	/**
	 * Save disbursement report ( {@link DisbursementReportHeader} ) object to the database table
	 * 
	 * @param reportHeader
	 * @return {@link Long}
	 * @throws Exception
	 */
	public Long save(DisbursementReportHeader reportHeader) throws Exception;
	
	/**
	 * Update an existing report ( {@link DisbursementReportHeader} ) object in the database table
	 * 
	 * @param reportHeader
	 * @throws Exception
	 */
	public void update(DisbursementReportHeader reportHeader) throws Exception;
	
	/**
	 * Delete an existing report ( {@link DisbursementReportHeader} ) object in the database table
	 * 
	 * @param reportHeader
	 * @throws Exception
	 */
	public void delete(DisbursementReportHeader reportHeader) throws Exception;

	/**
	 * 
	 * 
	 * @param year
	 * @return
	 * @throws Exception
	 */
	public List<DisbursementReportHeader> findAllReportsByYear(int year) throws Exception;
	
}
