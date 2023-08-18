/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cag.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.UserService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.cag.api.dao.CagDao;
import org.openmrs.module.cag.api.model.Cag;
import org.openmrs.module.cag.api.service.CagService;
import org.springframework.beans.factory.annotation.Autowired;

public class CagServiceImpl extends BaseOpenmrsService implements CagService {
	
	@Autowired
	CagDao dao;
	
	@Autowired
	UserService userService;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(CagDao dao) {
		this.dao = dao;
	}
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public Cag getCagByUuid(String uuid) {
		return dao.getCagByUuid(uuid);
	}
	
	@Override
	public Cag getCagById(Integer id) {
		return dao.getCagById(id);
	}
	
	@Override
	public Cag saveCag(Cag cag) throws APIException {
		if (cag.getCreator() == null) {
			cag.setCreator(userService.getUser(1));
		}
		return (Cag) dao.saveCag(cag);
	}
	
	@Override
	public List<Cag> getCagList() {
		return dao.getCagList();
	}
	
	@Override
	public List<Cag> getCagList(Boolean includeVoided) {
		return dao.getCagList(includeVoided);
	}
	
	@Override
	public void voidCag(Cag cag) {
		cag.setVoided(true);
		cag.setVoidReason("voided for some reason");
		dao.saveCag(cag);
	}
	
	@Override
	public void unvoidCag(Cag cag) {
		cag.setVoided(false);
		cag.setVoidReason(""); //To-do handle this properly
		dao.saveCag(cag);
	}
	
	@Override
	public void addPatientToCag(Cag cag, Patient patient) {
		if (patient != null) {
			List<Cag> patientCags = getCagsContainingPatient(patient);
			for (Cag cagItem : patientCags) {
				removePatientFromCag(cagItem, patient);
			}
			cag.getPatients().add(patient);
			dao.saveCag(cag);
		}
	}
	
	@Override
	public List<Cag> getCagsForPatient(Patient patient) {
		List<Cag> allCags = getCagList(false);
		List<Cag> patientCags = new ArrayList<>();
		for (Cag cag : allCags){
			if(cag.getPatients().contains(patient)){
				patientCags.add(cag);
			}
		}
		return patientCags;
	}
	
	@Override
	public List<Cag> getCagsContainingPatient(Patient patient) {
		return getCagsContainingPatientId(patient.getPatientId());
	}
	
	@Override
	public List<Cag> getCagsContainingPatientId(Integer patientId) {
		return dao.getCagsContainingPatientId(patientId);
	}
	
	@Override
	public void deleteCag(Cag cag) {
		dao.deleteCag(cag);
	}
	
	@Override
	public void removePatientFromCag(Cag cag, Patient patient) {
		cag.getPatients().remove(patient);
		dao.saveCag(cag);
	}
	
	@Override
	public List<Cag> getCagsContainingPatientUuid(String uuid) {
		return dao.getCagsContainingPatientUuid(uuid);
	}
}
