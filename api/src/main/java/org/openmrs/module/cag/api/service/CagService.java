/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cag.api.service;

import java.util.List;

import org.openmrs.Patient;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.cag.CagConfig;
import org.openmrs.module.cag.api.model.Cag;
import org.springframework.transaction.annotation.Transactional;

/**
 * The main service of this module, which is exposed for other modules. See
 * moduleApplicationContext.xml on how it is wired up.
 */
public interface CagService extends OpenmrsService {
	
	// /**
	//  * Returns an item by uuid. It can be called by any authenticated user. It is fetched in read
	//  * only transaction.
	//  * 
	//  * @param uuid
	//  * @return
	//  * @throws APIException
	//  */
	// @Authorized()
	// @Transactional(readOnly = true)
	// Cag getItemByUuid(String uuid) throws APIException;
	
	// /**
	//  * Saves an item. Sets the owner to superuser, if it is not set. It can be called by users with
	//  * this module's privilege. It is executed in a transaction.
	//  * 
	//  * @param item
	//  * @return
	//  * @throws APIException
	//  */
	// @Authorized(CagConfig.MODULE_PRIVILEGE)
	// @Transactional
	// Cag saveItem(Cag item) throws APIException;
	
	@Transactional(readOnly = true)
	Cag getCagByUuid(String uuid);
	
	@Transactional(readOnly = true)
	Cag getCagById(Integer id);
	
	@Transactional
	Cag saveCag(Cag cag) throws APIException;
	
	@Transactional(readOnly = true)
	List<Cag> getCagList();
	
	@Transactional
	void voidCag(Cag cag);
	
	@Transactional
	void unvoidCag(Cag cag);
	
	@Transactional(readOnly = true)
	List<Cag> getCagList(Boolean includeVoided);
	
	@Transactional
	void addPatientToCag(Cag cag, Patient patient);
	
	@Transactional
	void removePatientFromCag(Cag cag, Patient patient);
	
	@Transactional
	List<Cag> getCagsForPatient(Patient patient);
	
	@Transactional
	void deleteCag(Cag cag);
	
	@Transactional(readOnly = true)
	List<Cag> getCagsContainingPatient(Patient patient);
	
	@Transactional(readOnly = true)
	List<Cag> getCagsContainingPatientId(Integer patientId);
	
	@Transactional(readOnly = true)
	List<Cag> getCagsContainingPatientUuid(String uuid);
}
