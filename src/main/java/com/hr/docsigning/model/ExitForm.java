package com.hr.docsigning.model;

import lombok.Data;

@Data
public class ExitForm {

	private String firstName;

	private String lastName;

	private String resignationDate;

	private String lastDate;

	private String hiringDate;

	private String designation;

	private String resignationReason;

	public String getDesignation() {
		return this.designation;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	public String getHiringDate() {
		return this.hiringDate;
	}

	public String getLastDate() {
		return this.lastDate;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getResignationDate() {
		return this.resignationDate;
	}

	public String getResignationReason() {
		return this.resignationReason;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setHiringDate(String hiringDate) {
		this.hiringDate = hiringDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setResignationDate(String resignationDate) {
		this.resignationDate = resignationDate;
	}

	public void setResignationReason(String resignationReason) {
		this.resignationReason = resignationReason;
	}
}
