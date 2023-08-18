/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cag.api.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.cag.api.model.Cag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("cag.CagDao")
public class CagDao {
	
	@Autowired
	private DbSessionFactory sessionFactory;
	
	private DbSession getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public Cag getCagByUuid(String uuid) {
		return (Cag) getSession().createCriteria(Cag.class).add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}
	
	public Cag saveCag(Cag cag) {
		getSession().saveOrUpdate(cag);
		return cag;
	}
	
	public List<Cag> getCagList() {
		return (List<Cag>) getSession().createCriteria(Cag.class).list();
	}
	
	public Cag getCagById(Integer id) {
		return (Cag) getSession().createCriteria(Cag.class).add(Restrictions.eq("cag_id", id)).uniqueResult();
	}
	
	public List<Cag> getCagList(Boolean includeVoided) {
		if (!includeVoided) {
			return getSession().createCriteria(Cag.class).add(Restrictions.eq("voided", false)).list();
		} else {
			return getSession().createCriteria(Cag.class).list();
		}
	}
	
	public void deleteCag(Cag cag) {
		getSession().delete(cag);
	}
	
	public List<Cag> getCagsContainingPatientId(Integer patientId) {
		Criteria criteria = getSession().createCriteria(Cag.class);
		criteria.createAlias("patients", "p");
		criteria.add(Restrictions.eq("p.patientId", patientId));
		
		return (List<Cag>) criteria.list();
	}
	
	public List<Cag> getCagsContainingPatientUuid(String uuid) {
		Criteria criteria = getSession().createCriteria(Cag.class);
		criteria.createAlias("patients", "p");
		criteria.add(Restrictions.eq("p.uuid", uuid));
		
		return (List<Cag>) criteria.list();
	}
	
}
