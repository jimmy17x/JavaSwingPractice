package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

public class PrefsDialog extends JDialog {

	private JButton okBtn;
	private JButton cnclBtn;
	private JSpinner portSpinner;
	private SpinnerNumberModel spinnerModel;
	private JTextField userField;
	private JPasswordField passField;
	private PrefsListener prefsListner;

	public PrefsDialog(JFrame parent) {
		super(parent, "Preferences", false);
		setSize(400, 300);

		okBtn = new JButton("OK");
		cnclBtn = new JButton("CANCEL");

		spinnerModel = new SpinnerNumberModel(3306, 0, 9999, 1);
		portSpinner = new JSpinner(spinnerModel);

		userField = new JTextField(10);
		passField = new JPasswordField(10);
		passField.setEchoChar('*');

		// controls gridbaglayout for this dialog
		layoutControl();

		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer port = (Integer) portSpinner.getValue();

				String user = userField.getText();
				char[] password = passField.getPassword();

				System.out.println(user + " " + new String(password));
				if (prefsListner != null)
					prefsListner
							.preferenceSet(user, new String(password), port);

				// close dialog box
				setVisible(false);
			}

		});

		cnclBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = (Integer) portSpinner.getValue();
				// close dialog box
				setVisible(false);
			}

		});

		setSize(320, 230);
		setLocationRelativeTo(parent);

	}

	private void layoutControl() {

		Border titleBorder = BorderFactory
				.createTitledBorder("DataBase Preferences");
		int space = 15;
		Border spaceBorder = BorderFactory.createEmptyBorder(space, space,
				space, space);

		// divide layout in two panels
		JPanel controlsPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();

		// controlsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// controlsPanel.setBorder(titleBorder);
		controlsPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder,
				titleBorder));
		// buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		controlsPanel.setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();
		Insets rightPadding = new Insets(0, 0, 0, 15);
		Insets noPadding = new Insets(0, 0, 0, 15);
		gc.gridy = 0;

		// /////// first row controls panel ///////////////

		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = rightPadding;
		gc.gridx = 0;

		controlsPanel.add(new JLabel("User Name : "), gc);

		gc.gridx++;
		gc.insets = noPadding;
		gc.anchor = GridBagConstraints.WEST;
		controlsPanel.add(userField, gc);

		// /////////// next row password /////////

		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.EAST;
		gc.gridx = 0;
		gc.insets = rightPadding;
		controlsPanel.add(new JLabel("Password : "), gc);

		gc.gridx++;
		gc.insets = noPadding;
		gc.anchor = GridBagConstraints.WEST;
		controlsPanel.add(passField, gc);

		// /////////// next row spinner /////////

		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.EAST;
		gc.gridx = 0;
		gc.insets = rightPadding;
		controlsPanel.add(new JLabel("Port : "), gc);

		gc.gridx++;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = noPadding;
		controlsPanel.add(portSpinner, gc);

		// ////// buttons pannel////////////////

		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(okBtn);
		buttonsPanel.add(cnclBtn);

		// set sizes of buttons
		Dimension btnSize = cnclBtn.getPreferredSize();
		okBtn.setPreferredSize(btnSize);

		// add subpanel to dialog
		setLayout(new BorderLayout());
		add(controlsPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);

	}

	public void setPrefsListener(PrefsListener prefsListener) {
		this.prefsListner = prefsListener;

	}

	public void setDefaults(String u, String p, int port) {
		userField.setText(u);
		passField.setText(p);
		portSpinner.setValue(port);
	}
}
