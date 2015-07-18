package model;

import java.io.Serializable;

public class Person implements Serializable {

	private static final long serialVersionUID = 1878052688541449414L;

	private static int count = 1;

	private int id;
	private String name;
	private String occupation;
	private AgeCategory ageCategory;
	private EmploymentCategory empCat;
	private String taxId;
	private Gender gender;
	private boolean indianCitizen;

	public Person(int id, String name, String occupation,
			AgeCategory ageCategory, EmploymentCategory empC, String ti,
			Gender g, boolean ind) {

		// send values to default constructor
		this(name, occupation, ageCategory, empC, ti, g, ind);
		System.out.println("id");
		// overwrite id assigned by default constructor
		this.id = id;

	}

	public Person(String name, String occupation, AgeCategory ageCategory,
			EmploymentCategory empC, String ti, Gender g, boolean ind) {

		this.name = name;
		this.occupation = occupation;
		this.ageCategory = ageCategory;
		this.empCat = empC;
		this.taxId = ti;
		this.gender = g;
		this.indianCitizen = ind;

		this.id = count;
		count++;

	}

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

	public AgeCategory getAgeCategory() {
		return ageCategory;
	}

	public void setAgeCategory(AgeCategory ageCategory) {
		this.ageCategory = ageCategory;
	}

	public EmploymentCategory getEmpCat() {
		return empCat;
	}

	public void setEmpCat(EmploymentCategory empCat) {
		this.empCat = empCat;
	}

	public String getTaxId() {
		return taxId;
	}

	public int geTId() {
		return id;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public boolean isIndianCitizen() {
		return indianCitizen;
	}

	public void setIndianCitizen(boolean indianCitizen) {
		this.indianCitizen = indianCitizen;
	}

	public String toString() {
		return id + " : " + name;
	}
}
