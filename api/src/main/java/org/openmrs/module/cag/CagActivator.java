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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.DaemonTokenAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
@Component
public class CagActivator extends BaseModuleActivator implements ApplicationContextAware, DaemonTokenAware {
	
	private static ApplicationContext applicationContext;
	
	private static DaemonToken daemonToken;
	
	@Autowired
	private CagConfig cagConfig;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see #started()
	 */
	public void started() {
		log.info("Starting Cag module");
		try {
			applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
			
			if (daemonToken != null) {
				cagConfig.setDaemonToken(daemonToken);
			}
			
			cagConfig.subscribeToEvents();
			
			log.info("Cag module started");
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * @see #shutdown()
	 */
	public void shutdown() {
		log.info("Shutting down Cag module");
		if (cagConfig != null) {
			cagConfig.unSubscribetoEvents();
		}
	}
	
	@Override
	public void setDaemonToken(DaemonToken token) {
		this.daemonToken = token;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
