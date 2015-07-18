package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.EmploymentCategory;
import model.Person;

public class PersonTableModel extends AbstractTableModel {

	private List<Person> db;
	private String[] colNames = { "ID", "Name", "Occupation", "Age Category",
			"Employment-Category", "INDIAN Citizen", "TAX ID" };

	public PersonTableModel() {

	}

	// set header of the table
	public void setData(List<Person> db) {
		this.db = db;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		switch (col) {
		case 1:
			return true;
		case 4:
			return true; 
		case 5:
			return true;
		default:
			return false;
		}

	}

	// returns the col's c;ass to be rendered by table model in view 
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Integer.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return EmploymentCategory.class;
		case 5:
			return Boolean.class;
		case 6:
			return String.class;
		default:
			return null;
		}

	}

	@Override
	//sets the value after editting the table model
	public void setValueAt(Object aValue, int row, int col) {

		if (db == null)
			return;

		Person person = db.get(row);

		switch (col) {
		case 1:
			person.setName((String) aValue);
			break;
		case 5:
			person.setIndianCitizen((Boolean)aValue);
		case 4:
			person.setEmpCat((EmploymentCategory)aValue);
			
		default:
			return;
		}
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return db.size();

	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Person person = db.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return person.geTId();
		case 1:
			return person.getName();
		case 2:
			return person.getOccupation();
		case 3:
			return person.getAgeCategory();
		case 4:
			return person.getEmpCat();
		case 5:
			return person.isIndianCitizen();
		case 6:
			return person.getTaxId();
		}

		return null;
	}
}
