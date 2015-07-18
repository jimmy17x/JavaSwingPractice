package gui;



//using arbitrary component as a list renderer
//overkill in use JPanel when JPanel can be used directly
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import model.Message;

public class MessageListRenderer implements ListCellRenderer {

	private JPanel panel;
	private JLabel label;
	private Color selectedColor ;
	private Color  normalColor ;
	
	
	
	public MessageListRenderer (){
		panel = new JPanel();
		label = new JLabel();
		
		//set font
		label.setFont(Utils.createFont("/fonts/CrimewaveBB.ttf").deriveFont(Font.PLAIN, 20));
		
		selectedColor = new Color(210,210,255);
		normalColor =Color.WHITE;
		
		
		label.setIcon(Utils.createIcon("/images/info.png "));

		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		panel.add(label);
		
		
		
		
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		// TODO Auto-generated method stub
		Message m =  (Message)value;
		
		label.setText(m.getTitle());
		panel.setBackground ( cellHasFocus ? selectedColor:normalColor);
		
		
		
		return panel;
	}

	
}
