package com.pyramix.persistence.disbursement.dao.hibernate;

import java.util.List;

import com.pyramix.domain.disbursement.Disbursement;
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

}
