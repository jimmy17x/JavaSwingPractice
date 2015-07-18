package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.EmploymentCategory;
import model.Person;

// table panel 

public class TablePanel extends JPanel {

	private JTable  table;
	private PersonTableModel tableModel;
	private JPopupMenu popup;
	private PersonTableListener personTableListener;
	
	public TablePanel(){
		
		
		
		tableModel = new PersonTableModel();
		table =  new JTable(tableModel);
		popup = new JPopupMenu();
		JMenuItem removeItem = new JMenuItem("Delete Row");
		popup.add(removeItem);
		
		
		//custom table cell renderer and editor
		table.setDefaultRenderer(EmploymentCategory.class,new EmploymentCategoryRenderer());
		table.setDefaultEditor(EmploymentCategory.class,new EmploymentCategoryEditor());
		
		//set row height
		table.setRowHeight(25);
		
		//add mouseListener to the table
		table.addMouseListener(new MouseAdapter(){

			@Override
			public void mousePressed(MouseEvent e) {
				
				//get the row over which mouse is pressed over table and select it
				int row = table.rowAtPoint(e.getPoint());
				table.getSelectionModel().setSelectionInterval(row, row);
				
				if(e.getButton() == MouseEvent.BUTTON3)
					popup.show(table, e.getX(),e.getY());
			}
			
		});
		
		//action listener for remove item
		removeItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				
				//call person table listener
				if(personTableListener != null )
					personTableListener.rowDeleted(row);
				tableModel.fireTableRowsDeleted(row, row);;
				
				
			}
			
		});
		
		
		setLayout(new BorderLayout());
		
		add(new JScrollPane(table),BorderLayout.CENTER);
	}
	
	public void  setData( List<Person> db){
		tableModel.setData(db);
	}

	public void refresh() {
		tableModel.fireTableDataChanged();
	}

	public void setPersonTableListener(PersonTableListener personTableListener) {
		this.personTableListener = personTableListener;
		
	}
}
