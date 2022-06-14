package com.pyramix.persistence.disbursement.dao.hibernate;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import com.pyramix.domain.disbursement.Disbursement;
import com.pyramix.domain.disbursement.DisbursementStatus;
import com.pyramix.domain.disbursement.DisbursementType;
import com.pyramix.persistence.common.dao.hibernate.DaoHibernate;
import com.pyramix.persistence.disbursement.dao.DisbursementDao;

public class DisbursementHibernate extends DaoHibernate implements DisbursementDao {

	@Override
	public Disbursement findDisbursementById(long id) throws Exception {

		return (Disbursement) super.findById(Disbursement.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Disbursement> findAllDisbursement() throws Exception {

		return super.findAll(Disbursement.class);
	}

	@Override
	public Long save(Disbursement disbursement) throws Exception {

		return super.save(disbursement);
	}

	@Override
	public void update(Disbursement disbursement) throws Exception {

		super.update(disbursement);
	}

	@Override
	public void delete(Disbursement disbursement) throws Exception {

		super.delete(disbursement);
	}

	@Override
	public List<Disbursement> findDisbursementByType_Date(DisbursementType disbursementType, Date startDate, Date endDate, boolean desc) throws Exception {
		Session session = getSessionFactory().openSession();
		
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Disbursement> criteriaQuery = criteriaBuilder.createQuery(Disbursement.class);
		Root<Disbursement> root = criteriaQuery.from(Disbursement.class);
		if ((disbursementType==null) && (startDate!=null) && (endDate!=null)) {
			// disbursementType null, (starDate & endDate) not null
			criteriaQuery.select(root).where(
					criteriaBuilder.between(root.get("disbursementDate"), startDate, endDate));			
		} else if ((disbursementType!=null) && (startDate==null) && (endDate==null)) {
			// disbursementType not null, (starDate & endDate) null
			criteriaQuery.select(root).where(
					criteriaBuilder.equal(root.get("disbursementType"), disbursementType));			
		} else if ((disbursementType==null) && (startDate==null) && (endDate==null)) {
			// everything is null
			super.findAll(Disbursement.class);
		} else {
			criteriaQuery.select(root).where(
					criteriaBuilder.equal(root.get("disbursementType"), disbursementType),
					criteriaBuilder.between(root.get("disbursementDate"), startDate, endDate));			
		}		
		criteriaQuery.orderBy(desc ? 
				criteriaBuilder.desc(root.get("disbursementDate")):
				criteriaBuilder.asc(root.get("disbursementDate")));		
		try {
			
			return session.createQuery(criteriaQuery).getResultList();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public List<Disbursement> findAll_NonBatal_Disbursement() throws Exception {
		Session session = getSessionFactory().openSession();
		
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Disbursement> criteriaQuery = criteriaBuilder.createQuery(Disbursement.class);
		Root<Disbursement> root = criteriaQuery.from(Disbursement.class);

		criteriaQuery.select(root).where(
				criteriaBuilder.equal(root.get("disbursementStatus"), DisbursementStatus.OK));			
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("disbursementDate")));
		
		try {
			
			return session.createQuery(criteriaQuery).getResultList();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public List<Disbursement> findAll_NonBatal_Disbursement_ByType_Date(DisbursementType disbursementType,
			Date startDate, Date endDate) throws Exception {

		Session session = getSessionFactory().openSession();
		
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Disbursement> criteriaQuery = criteriaBuilder.createQuery(Disbursement.class);
		Root<Disbursement> root = criteriaQuery.from(Disbursement.class);

		criteriaQuery.select(root).where(
				criteriaBuilder.equal(root.get("disbursementType"), disbursementType),
				criteriaBuilder.between(root.get("disbursementDate"), startDate, endDate),
				criteriaBuilder.equal(root.get("disbursementStatus"), DisbursementStatus.OK));			
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("disbursementDate")));
		
		try {
			
			return session.createQuery(criteriaQuery).getResultList();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
}
