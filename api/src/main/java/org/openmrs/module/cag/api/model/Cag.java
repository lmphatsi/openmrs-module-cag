package org.openmrs.module.cag.api.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Obs;
import org.openmrs.Patient;

@Entity(name = "cag.cag")
@Table(name = "cag_cag")
public class Cag extends BaseOpenmrsData {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "cag_id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description", length = 255)
	private String description;
	
	@Column(name = "village")
	private String village;
	
	@Column(name = "constituency")
	private String constituency;
	
	@Column(name = "district")
	private String district;
	
	@ManyToMany
	@JoinTable(name = "cag_patient", joinColumns = @JoinColumn(name = "cag_id"), inverseJoinColumns = @JoinColumn(name = "patient_id"))
	private Set<Patient> patients = new HashSet<Patient>();
	
	public Cag() {
	}
	
	@Override
	public Integer getId() {
		return this.id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getVillage() {
		return this.village;
	}
	
	public void setVillage(String village) {
		this.village = village;
	}
	
	public String getConstituency() {
		return this.constituency;
	}
	
	public void setConstituency(String constituency) {
		this.constituency = constituency;
	}
	
	public String getDistrict() {
		return this.district;
	}
	
	public void setDistrict(String district) {
		this.district = district;
	}
	
	public Set<Patient> getPatients() {
		return this.patients;
	}
	
	public void setPatients(Set<Patient> patients) {
		this.patients = patients;
	}
	
	public void addPatientToCag(Patient patient) {
		this.patients.add(patient);
		// if (!this.patients.contains(patient)) {
		// 	this.patients.add(patient);
		// }
	}
	
	public void removePatientFromCag(Patient patient) {
		this.patients.remove(patient);
	}
	
	@Override
	public String toString() {
		return "{" + " id='" + getId() + "'" + ", name='" + getName() + "'" + ", description='" + getDescription() + "'"
		        + ", village='" + getVillage() + "'" + ", constituency='" + getConstituency() + "'" + ", district='"
		        + getDistrict() + "'" + ", patients='" + getPatients() + "'" + "}";
	}
	
}
