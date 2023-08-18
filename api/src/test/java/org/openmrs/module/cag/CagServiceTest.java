/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cag;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.PatientService;
import org.openmrs.api.UserService;
import org.openmrs.module.cag.api.dao.CagDao;
import org.openmrs.module.cag.api.model.Cag;
import org.openmrs.module.cag.api.service.CagService;
import org.openmrs.module.cag.api.service.impl.CagServiceImpl;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openmrs.api.context.Context;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * This is a unit test, which verifies logic in CagService. It doesn't extend
 * BaseModuleContextSensitiveTest, thus it is run without the in-memory DB and Spring context.
 */
public class CagServiceTest extends BaseModuleContextSensitiveTest {
	
	@InjectMocks
	CagServiceImpl basicModuleService;
	
	@Mock
	CagDao dao;
	
	@Mock
	UserService userService;
	
	@Mock
	PatientService patientService;
	
	CagService cagService;
	
	@Before
	public void setupMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void saveCag_shouldSetCreatorIfNotSet() {
		//Given
		Cag cag = new Cag();
		cag.setDescription("some description");
		
		when(dao.saveCag(cag)).thenReturn(cag);
		
		User user = new User();
		when(userService.getUser(1)).thenReturn(user);
		
		//When
		basicModuleService.saveCag(cag);
		
		//Then
		assertThat(cag, hasProperty("creator", is(user)));
		
	}
	
	@Before
	public void initializeCagService() throws Exception {
		initializeInMemoryDatabase();
		executeDataSet("org/openmrs/include/standardTestDataset.xml");
		cagService = Context.getService(CagService.class);
	}
	
	@Test
	public void TestAddingPatientsToCag() {
		//Given
		String uuid = "5396c62f-aa3a-4bb8-aed4-433b1e7b9296";
		Cag cag = new Cag();
		cag.setUuid(uuid);
		cag.setName("Kopanang Basotho CAG");
		cag.setDescription("some description");
		cag.setCreator(userService.getUser(1));
		// Set<Patient> patients = new HashSet<>();
		// patients.add(patientService.getPatient(2));
		// patients.add(patientService.getPatient(6));
		// cag.setPatients(patients);
		cag.addPatientToCag(patientService.getPatient(2));
		cag.addPatientToCag(patientService.getPatient(6));
		
		List<Cag> cagsList = cagService.getCagList();
		assertEquals(cagsList.size(), 0);
		
		cagService.saveCag(cag);
		Cag savedCag = cagService.getCagByUuid(uuid);
		
		assertNotNull(savedCag.getId());
		assertEquals(cag.getUuid(), savedCag.getUuid());
		
		assertEquals(1, cagService.getCagList().size());
		
		//assertEquals(2, savedCag.getPatients().size());
		
		cagService.addPatientToCag(savedCag, patientService.getPatient(7));
		
		savedCag = cagService.getCagByUuid(uuid);
		
		// assertEquals(3, savedCag.getPatients().size());
		//assertEquals(2, cag.getPatients().size());
		
	}
	
	@Override
	public Boolean useInMemoryDatabase() {
		return true;
	}
	
}
