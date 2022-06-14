package com.pyramix.persistence.disbursement.report.dao.hibernate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import com.pyramix.domain.disbursement.report.DisbursementReportHeader;
import com.pyramix.persistence.common.dao.hibernate.DaoHibernate;
import com.pyramix.persistence.disbursement.report.dao.DisbursementReportDao;

public class DisbursementReportHibernate extends DaoHibernate implements DisbursementReportDao {

	@Override
	public DisbursementReportHeader findReportById(long id) throws Exception {
		
		return (DisbursementReportHeader) super.findById(DisbursementReportHeader.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DisbursementReportHeader> findAllReports() throws Exception {

		return super.findAll(DisbursementReportHeader.class);
	}

	@Override
	public Long save(DisbursementReportHeader reportHeader) throws Exception {

		return super.save(reportHeader);
	}

	@Override
	public void update(DisbursementReportHeader reportHeader) throws Exception {

		super.update(reportHeader);
	}

	@Override
	public void delete(DisbursementReportHeader reportHeader) throws Exception {

		super.delete(reportHeader);
	}

	@Override
	public List<DisbursementReportHeader> findAllReportsByYear(int year) throws Exception {
		Session session = getSessionFactory().openSession();
		
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<DisbursementReportHeader> criteriaQuery = criteriaBuilder.createQuery(DisbursementReportHeader.class);
		Root<DisbursementReportHeader> root = criteriaQuery.from(DisbursementReportHeader.class);		
		criteriaQuery.select(root).where(criteriaBuilder.equal(
				criteriaBuilder.function("YEAR", Integer.class, root.get("reportPeriodDate")), year));
		
		try {
			
			return session.createQuery(criteriaQuery).getResultList();
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			session.close();
		}

	}

}
