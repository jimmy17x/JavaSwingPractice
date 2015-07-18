package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class FormPanel extends JPanel {

	private JLabel nameLabel;
	private JLabel occupationLabel;
	private JTextField nameField;
	private JTextField occupationField;
	private JButton okBtn;
	private FormListener formListener;
	private JList ageList;
	private JComboBox empCombo;
	private JCheckBox citizenCheck;
	private JTextField taxField;
	private JLabel taxLabel;
	// add button group with radio button in it
	private ButtonGroup genderGroup;
	private JRadioButton femaleRadio, maleRadio;

	public FormPanel() {
		// set preffered size for custom component using layoutmanager
		Dimension dim = getPreferredSize();
		dim.width = 250;
		setPreferredSize(dim);
		setMinimumSize(dim);

		nameLabel = new JLabel("Name : ");
		occupationLabel = new JLabel("Occupation : ");
		nameField = new JTextField(10);
		occupationField = new JTextField(10);
		ageList = new JList();
		empCombo = new JComboBox();
		taxLabel = new JLabel("Tax ID : ");
		citizenCheck = new JCheckBox();
		taxField = new JTextField(10);
		genderGroup = new ButtonGroup();
		maleRadio = new JRadioButton("male");
		femaleRadio = new JRadioButton("female");
		okBtn = new JButton("OK");
		
		//set mnemonic for okBtn
		okBtn.setMnemonic(KeyEvent.VK_O);
		
		//set menmonic for nameLabelField
		nameLabel.setDisplayedMnemonic(KeyEvent.VK_N);
		nameLabel.setLabelFor(nameField);
		
		maleRadio.setSelected(true);
		maleRadio.setActionCommand("male");
		femaleRadio.setActionCommand("female");
		
		// set up gender radios
		genderGroup.add(femaleRadio);
		genderGroup.add(maleRadio);

		taxLabel.setEnabled(false);
		taxField.setEnabled(false);

		// add action listener to checkbox
		citizenCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isTicked = citizenCheck.isSelected();
				taxLabel.setEnabled(isTicked);
				taxField.setEnabled(isTicked);

			}

		});

		// add the list model and leements of age category in list
		DefaultListModel ageModel = new DefaultListModel();
		ageModel.addElement(new AgeCategory(0, "Under 18 "));
		ageModel.addElement(new AgeCategory(1, "18  - 65 "));
		ageModel.addElement(new AgeCategory(2, " 65 n abv"));
		ageList.setModel(ageModel);

		// set size of list and border
		ageList.setPreferredSize(new Dimension(110, 70));
		ageList.setBorder(BorderFactory.createEtchedBorder());
		ageList.setSelectedIndex(1);

		// add employ combo box model to combobox
		final DefaultComboBoxModel empModel = new DefaultComboBoxModel();
		empModel.addElement("employed");
		empModel.addElement("self-employed");
		empModel.addElement("unemployed");

		empCombo.setModel(empModel);
		empCombo.setSelectedIndex(0);
		empCombo.setEditable(true);

		
		okBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				String occupation = occupationField.getText();
				AgeCategory ageCat = (AgeCategory) ageList.getSelectedValue();
				String empCat = (String) empCombo.getSelectedItem();
				String taxId = taxField.getText();
				boolean indCitzn = citizenCheck.isSelected();
				String genderCommand = genderGroup.getSelection().getActionCommand();

				System.out.println(empCat);

				FormEvent ev = new FormEvent(this, name, occupation, ageCat
						.getId(), empCat, taxId, indCitzn , genderCommand );

				// check if formListener is set
				if (formListener != null)
					formListener.formEventOccured(ev);
				
				//reset fields
				nameField.setText(null);
				occupationField.setText(null);
				taxField.setText(null);
				maleRadio.setSelected(true);

			}

		});

		// setBorder using borderfactory
		Border innerBorder = BorderFactory.createTitledBorder("Add Person");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		// add components to layout
		layoutComponents();

	}

	public void setFormListener(FormListener formListener) {
		this.formListener = formListener;
	}

	public void layoutComponents() {
		// set layoutmanager
		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();

		// ///// first row /////////
		gc.gridx = 0;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.weightx = 1;
		gc.weighty = 0.1;
		gc.insets = new Insets(0, 0, 0, 5);
		add(nameLabel, gc);

		gc.gridx = 1;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(nameField, gc);

		// ///// second row /////////
		++gc.gridy;

		gc.gridx = 0;

		gc.weightx = 1;
		gc.weighty = 0.1;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(occupationLabel, gc);
		gc.gridx = 1;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(occupationField, gc);

		// ///// next row agelist /////////
		++gc.gridy;

		gc.weightx = 1;
		gc.weighty = 0.2;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Age : "), gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(ageList, gc);

		// ///// next row - combobox/////////
		++gc.gridy;

		gc.weightx = 1;
		gc.weighty = 0.2;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Employment : "), gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(empCombo, gc);

		// ///// next row - checkbox/////////
		++gc.gridy;

		gc.weightx = 1;
		gc.weighty = 0.2;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("INDIAN Citizen : "), gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(citizenCheck, gc);

		// ///// next row - taxlabel/////////
		++gc.gridy;

		gc.weightx = 1;
		gc.weighty = 0.2;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(taxLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(taxField, gc);

		// ///// next row - male radio/////////
		++gc.gridy;

		gc.weightx = 1;
		gc.weighty = 0.05;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Gender : "), gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(maleRadio, gc);

		// ///// next row - female radio/////////
		++gc.gridy;

		gc.weightx = 1;
		gc.weighty = 0.2;

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(femaleRadio, gc);

		// ///// next row ok button/////////
		++gc.gridy;

		gc.gridx = 1;

		gc.weightx = 1;
		gc.weighty = 2.0;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(okBtn, gc);

	}
}

// inner class for list items elements

class AgeCategory {
	private int id;
	private String text;

	public AgeCategory(int id, String s) {
		this.id = id;
		this.text = s;
	}

	public String toString() {
		return this.text;
	}

	public int getId() {
		return this.id;
	}
}