package mysql;

import java.sql.SQLException;

import model.AgeCategory;
import model.DataBase;
import model.EmploymentCategory;
import model.Gender;
import model.Person;

public class TestDatabase {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("db up");

		DataBase db = new DataBase();
		try {
			db.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		db.addPerson(new Person("Jimmy", "softwrenginerr", AgeCategory.adult,
				EmploymentCategory.selfEmployed, "17", Gender.male, true));
		
		db.addPerson(new Person("sue", "artist", AgeCategory.adult,
				EmploymentCategory.employed, null, Gender.male, false));
		try {
			db.save();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			db.load();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
;		db.disconnnect();
	}

}
