package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DataBase {
	private List<Person> people;
	private Connection con;
	
	private int port;
	private String user;
	private String password;

	public DataBase() {
		people = new LinkedList<>();

	}

	public void addPerson(Person person) {
		people.add(person);
	}

	public List<Person> getPeople() {
		return Collections.unmodifiableList(people);
	}

	
	
	public void configure(int port , String user , String p) throws Exception{
		this.password = p ;
		this.port = port;
		this.user = user;
		
		if(con!= null){
			disconnnect();
			connect();
		}
	}
	public void save() throws SQLException {
		// dont give a fucking space between count(*) :P _!_
		String checkSql = "select count(*) as count from people where id=?";

		// sql injection safety measure
		PreparedStatement checkStmt = con.prepareStatement(checkSql);

		// prepare insert statement
		String sqlInsert = "insert into people (id , name, age , employment_status , tax_id , ind_citizen , gender , occupation) values (?,?,?,?,?,?,?,?)";
		PreparedStatement insertStmt = con.prepareStatement(sqlInsert);

		// prepare update statement
		String sqlUpdate = "update people set   name=?, age=? , employment_status=? , tax_id=? , ind_citizen=? , gender=? , occupation=?  where id =?";
		PreparedStatement updateStmt = con.prepareStatement(sqlUpdate);

		for (Person p : people) {
			int id = p.geTId();

			AgeCategory age = p.getAgeCategory();
			EmploymentCategory emp = p.getEmpCat();
			Gender g = p.getGender();
			String name = p.getName();
			String occupation = p.getOccupation();
			String tax = p.getTaxId();
			boolean ind = p.isIndianCitizen();

			checkStmt.setInt(1, id);
			// execute the prepared sql query
			ResultSet checkResult = checkStmt.executeQuery();

			// move cursor to first row
			checkResult.next();

			int count = checkResult.getInt(1);

			if (count == 0) {
				// insert into database this unique person id
				System.out.println("inserting person with id " + id);

				int col = 1;

				insertStmt.setInt(col++, id);
				insertStmt.setString(col++, name);
				insertStmt.setString(col++, age.name());
				insertStmt.setString(col++, emp.name());
				insertStmt.setString(col++, tax);
				insertStmt.setBoolean(col++, ind);
				insertStmt.setString(col++, g.name());
				insertStmt.setString(col++, occupation);

				insertStmt.executeUpdate();

			} else {
				System.out.println("updating person with id " + id);

				int col = 1;

				updateStmt.setString(col++, name);
				updateStmt.setString(col++, age.name());
				updateStmt.setString(col++, emp.name());
				updateStmt.setString(col++, tax);
				updateStmt.setBoolean(col++, ind);
				updateStmt.setString(col++, g.name());
				updateStmt.setString(col++, occupation);
				updateStmt.setInt(col++, id);

				updateStmt.executeUpdate();

			}

		}

		updateStmt.close();
		insertStmt.close();
		checkStmt.close();
	}

	public void connect() throws Exception {

		if (con != null)
			return;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new Exception("no driver");
		}

		String url = "jdbc:mysql://localhost:3306/swingtest";
		con = DriverManager.getConnection(url, "root", "jimmyx");
		System.out.println("connected " + con);
	}

	public void disconnnect() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("cant close connection");
			}
		}

	}

	public void load() throws SQLException {
		people.clear();

		// create statement
		String sql = "select id , name, age , employment_status , tax_id , ind_citizen , gender , occupation from people order by name";
		Statement selectStmnt = con.createStatement();

		ResultSet results = selectStmnt.executeQuery(sql);

		while (results.next()) {
			int id = results.getInt("id");
			String name = results.getString("name");
			String age = results.getString("age");
			String emp = results.getString("employment_status");
			String tax = results.getString("tax_id");
			Boolean isInd = results.getBoolean("ind_citizen");
			String gender = results.getString("gender");
			String occup = results.getString("occupation");

			Person p = new Person(id, name, occup, AgeCategory.valueOf(age),
					EmploymentCategory.valueOf(emp), tax,
					Gender.valueOf(gender), isInd);
			people.add(p);

			 System.out.println(p);
		}

		selectStmnt.close();
	}

	public void saveToFile(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		Person[] persons = people.toArray(new Person[people.size()]);
		oos.writeObject(persons);
		oos.close();
	}

	public void loadFromFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);

		try {
			Person[] persons = (Person[]) ois.readObject();
			people.clear();
			people.addAll(Arrays.asList(persons));
		} catch (ClassNotFoundException e) {

		}
	}

	public void removePerson(int row) {
		people.remove(row);

	}
}
