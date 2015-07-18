package controller;

import gui.FormEvent;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.AgeCategory;
import model.DataBase;
import model.EmploymentCategory;
import model.Gender;
import model.Person;

public class Controller {

	DataBase db = new DataBase();

	public void addPerson(FormEvent e) {

		String name = e.getName();
		String occupation = e.getOccupation();
		int ageCat = e.getAgeCategory();
		String empCat = e.getEmpCat();
		boolean isIndCitz = e.isIndianCitizen();
		String taxId = e.getTaxId();
		String gender = e.getGender();

		AgeCategory ageCategory = null;

		switch (ageCat) {
		case 0:
			ageCategory = AgeCategory.child;
			break;
		case 1:
			ageCategory = AgeCategory.adult;
			break;
		case 2:
			ageCategory = AgeCategory.senior;
			break;

		}

		EmploymentCategory ec = null;
		if (empCat.equals("employed")) {
			ec = EmploymentCategory.employed;
		} else if (empCat.equals("unemployed")) {
			ec = EmploymentCategory.unemployed;
		} else if (empCat.equals("self-employed")) {
			ec = EmploymentCategory.selfEmployed;
		} else {
			ec = EmploymentCategory.other;
			System.err.println(empCat);
		}

		Gender genderCat;
		if (gender.equals("male")) {
			genderCat = Gender.male;
		} else {
			genderCat = Gender.female;
		}

		Person person = new Person(name, occupation, ageCategory, ec, taxId,
				genderCat, isIndCitz);
		db.addPerson(person);

	}
	
	
	//database save , disconnect , load n connect
	public void save() throws SQLException{
		db.save();
	}
	
	public void disconnect(){
		db.disconnnect();
	}
	
	public void load() throws SQLException{
		//db.load();
	}
	
	public void connect() throws Exception{
		db.connect();;
	}
	
	
	public List<Person> getPeople(){
		return db.getPeople();
	}
	
	public void saveToFile(File file) throws IOException
	{
		db.saveToFile(file);
	}
	
	public void loadFromFile(File file) throws IOException 
	{
		db.loadFromFile(file);
	}

	public void removePerson(int row) {
		db.removePerson(row);
		
	}


	public void configure(int port, String user, String pass) throws Exception {
		db.configure(port, user, pass);
		
	}
	
	
	
	
	
}
