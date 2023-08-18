package org.openmrs.module.cag.api.event;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Daemon;
import org.openmrs.event.EventListener;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.cag.api.model.Cag;
import org.openmrs.module.cag.api.service.CagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EncounterListener implements EventListener {
	
	private DaemonToken daemonToken;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	EncounterService encounterService;
	
	@Autowired
	PatientService patientService;
	
	@Autowired
	CagService cagService;
	
	public DaemonToken getDaemonToken() {
		return daemonToken;
	}
	
	public void setDaemonToken(DaemonToken daemonToken) {
		this.daemonToken = daemonToken;
	}
	
	@Override
    public void onMessage(Message message) {
        try {
            Daemon.runInDaemonThread(() -> {
                try {
                    processMessage(message);
                }
                catch (Exception e) {
                    log.error(String.format("Failed to process Patient message!\n%s", message.toString()), e);
                }
            }, daemonToken);
        }
        catch (Exception e) {
            log.error(String.format("Failed to start Daemon thread to process message!\n%s", message.toString()), e);
        }
    }
	
	private void processMessage(Message message) throws JMSException {
		if (message instanceof MapMessage) {
			MapMessage mapMessage = (MapMessage) message;
			
			String uuid;
			try {
				uuid = mapMessage.getString("uuid");
				log.debug(String.format("Handling encounter %s", uuid));
			}
			catch (JMSException e) {
				log.error("Exception caught while trying to get encounter uuid for event.", e);
				return;
			}
			
			if (uuid == null || StringUtils.isBlank(uuid)) {
				return;
			}
			
			Encounter encounter = encounterService.getEncounterByUuid(uuid);
			
			if (encounter.getEncounterType().getName().equals("Consultation")) {
				Cag cag = new Cag();
				cag.setName("Kopano Community ART Group2");
				cag.setDescription("Kopanong2");
				cag.setDistrict("Mafeteng");
				cag.setConstituency("Maliepetsane");
				cag.setVillage("Ha Mphatsi");
				// cag.addPatientToCag(encounter.getPatient());
				// cag.addPatientToCag(patientService.getPatient(5143));
				// cag.addPatientToCag(patientService.getPatient(5143));
				// cag.addPatientToCag(encounter.getPatient());
				// cagService.saveCag(cag);
				cagService.saveCag(cag);
				cagService.addPatientToCag(cag, encounter.getPatient());
				cagService.addPatientToCag(cag, patientService.getPatient(5143));
			}
			
		}
	}
	
}
