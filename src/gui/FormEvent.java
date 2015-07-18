package gui;

import java.util.EventObject;

public class FormEvent extends EventObject {

	private String name;
	private String occupation;
	private int ageCategory;
	private String empCat, taxId , gender;
	private boolean indianCitizen;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public FormEvent(Object source) {
		super(source);

	}

	public FormEvent(Object source, String name, String occupation, int ageCat,
			String empCat, String taxId, boolean ind , String gender) {
		super(source);
		this.name = name;
		this.occupation = occupation;
		this.ageCategory = ageCat;
		this.empCat = empCat;
		this.taxId = taxId;
		this.indianCitizen = ind;
		this.gender = gender;

	}

	public String getGender() {
		return gender;
	}

	public String getTaxId() {
		return taxId;
	}

	public boolean isIndianCitizen() {
		return indianCitizen;
	}

	public String getEmpCat() {
		return empCat;
	}

	public int getAgeCategory() {
		return ageCategory;
	}

}
