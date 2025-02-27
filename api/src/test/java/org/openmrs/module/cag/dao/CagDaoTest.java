/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cag.dao;

import org.junit.Test;
import org.junit.Ignore;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.cag.api.dao.CagDao;
import org.openmrs.module.cag.api.model.Cag;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * It is an integration test (extends BaseModuleContextSensitiveTest), which verifies DAO methods
 * against the in-memory H2 database. The database is initially loaded with data from
 * standardTestDataset.xml in openmrs-api. All test methods are executed in transactions, which are
 * rolled back by the end of each test method.
 */
public class CagDaoTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	CagDao dao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PatientService patientService;
	
	@Test
	@Ignore("Unignore if you want to make the Item class persistable, see also Item and liquibase.xml")
	public void saveItem_shouldSaveAllPropertiesInDb() {
		//Given
		Cag cag = new Cag();
		cag.setDescription("some description");
		cag.setCreator(userService.getUser(1));
		
		//When
		dao.saveCag(cag);
		
		//Let's clean up the cache to be sure getItemByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.clearSession();
		
		//Then
		Cag savedCag = dao.getCagByUuid(cag.getUuid());
		
		assertThat(savedCag, hasProperty("uuid", is(cag.getUuid())));
		assertThat(savedCag, hasProperty("creator", is(cag.getCreator())));
		assertThat(savedCag, hasProperty("description", is(cag.getDescription())));
	}
	
	@Test
	@Ignore("Unignore if you want to make the Item class persistable, see also Item and liquibase.xml")
	public void saveCag_shouldSaveAllPropertiesInDb() {
		//Given
		Cag cag = new Cag();
		cag.setDescription("some description");
		cag.setCreator(userService.getUser(1));
		Set<Patient> patients = new HashSet<Patient>();
		patients.add(patientService.getPatient(1000));
		patients.add(patientService.getPatient(1001));
		cag.setPatients(patients);
		
		//When
		dao.saveCag(cag);
		
		//Let's clean up the cache to be sure getItemByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.clearSession();
		
		//Then
		Cag savedCag = dao.getCagByUuid(cag.getUuid());
		
		assertThat(savedCag, hasProperty("uuid", is(cag.getUuid())));
		assertThat(savedCag, hasProperty("creator", is(cag.getCreator())));
		assertThat(savedCag, hasProperty("description", is(cag.getDescription())));
		assertThat(savedCag, hasProperty("patients", is(cag.getPatients())));
		assertEquals(savedCag.getPatients(), cag.getPatients());
		
	}
}
