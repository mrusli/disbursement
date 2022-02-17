package com.pyramix.persistence.disbursement.serialnumber.dao.hibernate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import com.pyramix.domain.disbursement.DisbursementSerialNumber;
import com.pyramix.domain.disbursement.DisbursementType;
import com.pyramix.persistence.common.dao.hibernate.DaoHibernate;
import com.pyramix.persistence.disbursement.serialnumber.dao.DisbursementSerialNumberDao;

public class DisbursementSerialNumberHibernate extends DaoHibernate implements DisbursementSerialNumberDao {

	@Override
	public DisbursementSerialNumber findDisbursementSerialNumberById(long id) throws Exception {
		
		return (DisbursementSerialNumber) super.findById(DisbursementSerialNumber.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DisbursementSerialNumber> findAllDisbursementSerialNumber() throws Exception {
		
		return super.findAll(DisbursementSerialNumber.class);
	}

	@Override
	public Long save(DisbursementSerialNumber disbursementSerialNumber) throws Exception {
		
		return super.save(disbursementSerialNumber);
	}

	@Override
	public void update(DisbursementSerialNumber disbursementSerialNumber) throws Exception {
		
		super.update(disbursementSerialNumber);
	}

	@Override
	public DisbursementSerialNumber findLastDisbursementSerialNumberByType(DisbursementType disbursementType)
			throws Exception {
		Session session = getSessionFactory().openSession();
		
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<DisbursementSerialNumber> criteriaQuery = criteriaBuilder.createQuery(DisbursementSerialNumber.class);
		Root<DisbursementSerialNumber> root = criteriaQuery.from(DisbursementSerialNumber.class);
		criteriaQuery.select(root).where(
			criteriaBuilder.equal(root.get("disbursementType"), disbursementType));
		criteriaQuery
			.orderBy(criteriaBuilder.desc(root.get("id")));
		
		try {
			List<DisbursementSerialNumber> serialNumberList = session.createQuery(criteriaQuery).getResultList();
			// serialNumberList.forEach(serialNumber -> log.info(serialNumber.toString()));
			
			if (serialNumberList.isEmpty()) {
				return null;
			} else {
				
				return serialNumberList.get(0);
				
			}
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			session.close();
			
		}	
	}

}
