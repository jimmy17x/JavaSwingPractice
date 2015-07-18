package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar implements ActionListener {

	// two buttons
	private JButton saveButton;
	private JButton refreshButton;

	private ToolbarListener textListener;

	public ToolBar() {

		//setBorder(BorderFactory.createEtchedBorder());//setting border makes toolbar non draggable
		//setFloatable(false);
		
		saveButton = new JButton();
		refreshButton = new JButton();

		// set icons and tooltip to buttons on toolbar
		saveButton.setIcon(Utils.createIcon("/images/save.png"));
		refreshButton.setIcon(Utils.createIcon("/images/refresh.png"));
		saveButton.setToolTipText("Save stuff");
		refreshButton.setToolTipText("Refresh");

		// saveButton.setText("Save Stuff");
		// refreshButton.setText("Refresh");

		saveButton.addActionListener(this);
		refreshButton.addActionListener(this);

		// set flowlayout for this toolbar jpnale with left justification
		//setLayout(new FlowLayout(FlowLayout.LEADING));

		add(saveButton);
		//addSeparator();
		add(refreshButton);

	}



	public void setToolbarListener(ToolbarListener listener) {
		this.textListener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton) e.getSource();

		if (clicked == saveButton) {
			if (textListener != null) {
				textListener.saveEventOccured();
			}
		} else if (clicked == refreshButton) {
			textListener.refreshEventOccured();
		}

	}

}
