package model;

public enum EmploymentCategory {
	employed("Employed"),
	selfEmployed("Self Employed"),
	unemployed("Unemployed"),
	other("Other");
	
	String text ;
	//for the enum constructor; only private is permitted
	private EmploymentCategory(String text){
		this.text  = text;
	}
	public String toString(){
		 return text;
	}
}
