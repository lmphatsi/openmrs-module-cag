package org.openmrs.module.web.resources;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.cag.api.model.Cag;
import org.openmrs.module.cag.api.service.CagService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.representation.CustomRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.web.bind.annotation.RequestBody;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;

import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;

/* This is our custom OpenMRS REST API resource handler for the "cag" resource */

@Resource(name = RestConstants.VERSION_1 + "/cag", supportedClass = Cag.class, supportedOpenmrsVersions = { "2.*", "3.*" })
public class CagResource extends DataDelegatingCrudResource<Cag> {
	
	// CagService cagService = Context.getService(CagService.class);
	private CagService getService() {
		return Context.getService(CagService.class);
	}
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
	protected void delete(Cag delegate, String reason, RequestContext context) throws ResponseException {
		
	}
	
	@Override
	public Cag newDelegate() {
		return new Cag();
	}
	
	@Override
	public Object create(@RequestBody SimpleObject propertiesToCreate, RequestContext context) throws ResponseException {
		log.error("inside  the create function");
		String name = (String) propertiesToCreate.get("name");
		String district = (String) propertiesToCreate.get("district");
		String constituency = (String) propertiesToCreate.get("constituency");
		String village = (String) propertiesToCreate.get("village");
		log.error("Inside the create function");
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Cag name is required.");
		}
		
		Cag cag = new Cag();
		cag.setName(name);
		cag.setDistrict(district);
		cag.setConstituency(constituency);
		cag.setVillage(village);
		
		log.error("Cag to string is " + cag.toString());
		//getService().saveCag(cag);
		save(cag);
		
		SimpleObject responseObject = new SimpleObject();
		responseObject.add("message", "Cag created successfully");
		responseObject.add("uuid", cag.getUuid());
		
		return responseObject;
	}
	
	@Override
	public Cag update(String uuid, SimpleObject propertiesToUpdate, RequestContext context) throws ResponseException {
		Cag existingCag = getService().getCagByUuid(uuid);
		log.error("inside  the update function");
		if (existingCag == null) {
			throw new IllegalArgumentException("Cag with UUID " + uuid + " not found.");
		}
		
		String name = (String) propertiesToUpdate.get("name");
		String district = (String) propertiesToUpdate.get("district");
		String constituency = (String) propertiesToUpdate.get("constituency");
		String village = (String) propertiesToUpdate.get("village");
		
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Cag name is required.");
		}
		
		existingCag.setName(name);
		existingCag.setDistrict(district);
		existingCag.setConstituency(constituency);
		existingCag.setVillage(village);
		
		//getService().saveCag(existingCag);
		save(existingCag);
		
		return existingCag;
	}
	
	@Override
	public Cag save(Cag delegate) {
		validateCag(delegate);
		try {
			return getService().saveCag(delegate);
		}
		catch (APIException e) {
			throw new IllegalArgumentException("Error saving Cag: " + e.getMessage());
		}
	}
	
	private void validateCag(Cag delegate) {
		//suppose cag name is the only mandatory field
		if (delegate.getName().isEmpty() || delegate.getName() == null) {
			throw new IllegalArgumentException("Cag name is required.");
		}
		//other validation checks here
		
	}
	
	@Override
	public void purge(Cag delegate, RequestContext context) throws ResponseException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'newDelegate'");
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("uuid");
		description.addProperty("name");
		description.addProperty("description");
		description.addProperty("district");
		description.addProperty("constituency");
		description.addProperty("village");
		description.addProperty("voided");
		description.addProperty("patients", Representation.REF); //maybe hide this
		description.addSelfLink();
		return description;
	}
	
	@PropertyGetter("name")
	public String getDisplayString(Cag delegate) {
		return "Cag name : " + delegate.getName() + " is active : " + !delegate.getVoided();
	}
	
	@Override
	public Cag getByUniqueId(String uniqueId) {
		return getService().getCagByUuid(uniqueId);
	}
	
	// @Override
	// protected NeedsPaging<Cag> doSearch(RequestContext context) {
	// 	String patientId = (String) context.getParameter("pid");
	// 	String patientUuid = (String) context.getParameter("puuid");
	// 	Boolean includevoided = Boolean.valueOf(context.getParameter("includevoided"));
	// 	if (includevoided != null) {
	// 		List<Cag> cags = cagService.getCagList(includevoided);
	// 		return new NeedsPaging<Cag>(cags, context);
	// 	} else if (patientId != null) {
	// 		List<Cag> cags = cagService.getCagsContainingPatientId(Integer.parseInt(patientId));
	// 		return new NeedsPaging<Cag>(cags, context);
	// 	} else if (patientUuid != null) {
	// 		List<Cag> cags = cagService.getCagsContainingPatientUuid(patientUuid);
	// 		return new NeedsPaging<Cag>(cags, context);
	// 	} else {
	// 		throw new IllegalArgumentException("Missing required parameter: includevoided, puuid or pid");
	// 	}
	
	// }
	
	@Override
	protected NeedsPaging<Cag> doSearch(RequestContext context) {
		
		String patientUuid = (String) context.getParameter("puuid");
		
		if (patientUuid != null) {
			List<Cag> cags = getService().getCagsContainingPatientUuid(patientUuid);
			return new NeedsPaging<Cag>(cags, context);
		} else {
			throw new IllegalArgumentException("Missing required parameter: includevoided, puuid or pid");
		}
		
	}
	
	@Override
	public PageableResult doGetAll(RequestContext context) {
		List<Cag> cags = getService().getCagList();
		return new NeedsPaging<Cag>(cags, context);
	}
}
