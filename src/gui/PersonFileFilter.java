package gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PersonFileFilter extends FileFilter {
	
	
	public boolean accept(File f) {
		
		if(f.isDirectory()) return true; 

		String name = f.getName();
		String extension = Utils.getFileExtension(name);
		if (extension == null)
			return false;
		else if (extension.equals("per"))
			return true;
		return false;

	}

	
	public String getDescription() {
		return "Person database files (*.per)";
	}

}
