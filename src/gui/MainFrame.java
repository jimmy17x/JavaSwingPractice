package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.Controller;

// acts as controller for custom components MVC

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 5621615353761553974L;

	// add custom components
	// beginning content MVA

	private ToolBar toolbar;
	private FormPanel formPanel;
	private JFileChooser fileChooser;
	private Controller controller;
	private TablePanel tablePanel;
	private PrefsDialog prefsDialog;
	private Preferences prefs;
	private JSplitPane splitPane;
	private JTabbedPane tabPane;
	// add tree
	private MessagePanel messagePanel;

	public MainFrame() {

		super("Hello Swing !");
		setLayout(new BorderLayout());

		toolbar = new ToolBar();
		formPanel = new FormPanel();
		fileChooser = new JFileChooser();
		controller = new Controller();
		tablePanel = new TablePanel();
		prefsDialog = new PrefsDialog(this);
		prefs = Preferences.userRoot().node("db");
		tabPane = new JTabbedPane();
		messagePanel = new MessagePanel(this);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel,
				tabPane);

		// add tabs to the tabPane
		tabPane.addTab("Person Database", tablePanel);
		tabPane.addTab("Messages", messagePanel);

		// add listener to tabPane state changed
		tabPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int tabIndex = tabPane.getSelectedIndex();

				// if message tab selected
				if (tabIndex == 1) {
					messagePanel.refresh();
				}
				System.out.println(tabIndex);
			}

		});

		// add data to table panel
		tablePanel.setData(controller.getPeople());

		// set one touch expandables on splitpane
		splitPane.setOneTouchExpandable(true);

		tablePanel.setPersonTableListener(new PersonTableListener() {
			public void rowDeleted(int row) {
				// System.out.println(row);
				controller.removePerson(row);
			}
		});

		prefsDialog.setPrefsListener(new PrefsListener() {
			public void preferenceSet(String user, String pass, int port) {
				prefs.put("user", user);
				prefs.put("password", pass);
				prefs.putInt("port", port);

				try {
					controller.configure(port, user, pass);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(MainFrame.this,
							"Unable to reconnect");
				}

			}

		});

		prefsDialog.setDefaults(prefs.get("user", ""), prefs.get("pass", ""),
				prefs.getInt("port", 3306));
		fileChooser.addChoosableFileFilter(new PersonFileFilter());

		try {
			controller.configure(prefs.getInt("port", 3306),
					prefs.get("user", ""), prefs.get("pass", ""));
		} catch (Exception e1) {
			System.err.println("cant connect to database");
		}

		// set menu bar
		setJMenuBar(createMenuBar());
		setJMenuBar(createMenuBar());
		setJMenuBar(createMenuBar());
		
		refresh();

		// add(textPanel, BorderLayout.CENTER);
		// add(tablePanel, BorderLayout.CENTER);
		// add(formPanel, BorderLayout.WEST);
		add(toolbar, BorderLayout.PAGE_START);
		add(splitPane, BorderLayout.CENTER);

		// set StringListener interface for toolbar
		toolbar.setToolbarListener(new ToolbarListener() {

			@Override
			public void saveEventOccured() {

				// connect to db
				connect();

				// save data to db
				try {
					controller.save();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(MainFrame.this,
							"Unable to save to database.",
							"OOppss Database connection problem..",
							JOptionPane.ERROR_MESSAGE);
				}
				// System.out.println("save");
			}

			@Override
			public void refreshEventOccured() {

				refresh();
			}

		});

		formPanel.setFormListener(new FormListener() {

			public void formEventOccured(FormEvent e) {
				controller.addPerson(e);
				tablePanel.refresh();
			}
		});

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("window clos");
				controller.disconnect();
				dispose();
				System.gc();
			}

		});

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(500, 400));
		setSize(600, 500);
		setVisible(true);

	}

	protected void refresh() {
		// connect to db
		connect();
		try {
			// load data from db
			controller.load();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(MainFrame.this,
					"Unable to load from database.",
					"OOppss Database connection problem..",
					JOptionPane.ERROR_MESSAGE);
		}

		// refresh tablePanel
		tablePanel.refresh();

		// System.out.println("refresh");

	
		
	}

	private void connect() {
		try {
			controller.connect();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(MainFrame.this,
					"Cannot conenct to database .",
					"OOppss Database connection problem..",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// craeate file menu
		JMenu fileMenu = new JMenu("File");

		// add items to File menu
		JMenuItem exportDataItem = new JMenuItem("Export Data....");
		JMenuItem importDataItem = new JMenuItem("Import Data....");
		JMenuItem exitItem = new JMenuItem("Exit");

		fileMenu.add(exportDataItem);
		fileMenu.add(importDataItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);

		JMenu windowMenu = new JMenu("Window");
		JMenuItem prefsItem = new JMenuItem("Preferences...");

		// add item to Window menu
		JMenu showMenu = new JMenu("Show"); // a submenu in window menu item
		windowMenu.add(showMenu);
		windowMenu.add(prefsItem);

		JCheckBoxMenuItem showFormItem = new JCheckBoxMenuItem("Person Form");
		showFormItem.setSelected(true);
		showMenu.add(showFormItem);

		prefsItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				prefsDialog.setVisible(true);
			}

		});

		menuBar.add(fileMenu);
		menuBar.add(windowMenu);

		// actionListener for importData Mneu item
		importDataItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					try {
						controller.loadFromFile(fileChooser.getSelectedFile());
						tablePanel.refresh();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MainFrame.this,
								"Could not load data from file", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			}

		});

		// actionListener for exportData Menu item
		exportDataItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					try {
						controller.saveToFile(fileChooser.getSelectedFile());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MainFrame.this,
								"Could not save data from file", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			}

		});

		// add action listener to showFormItem
		showFormItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();

				// set splitpane location such tht formpanel is visible
				if (menuItem.isSelected()) {
					splitPane.setDividerLocation((int) formPanel
							.getMinimumSize().getWidth());
					;
				}

				formPanel.setVisible(menuItem.isSelected());

			}

		});

		// set mnemonics
		fileMenu.setMnemonic(KeyEvent.VK_F);
		exitItem.setMnemonic(KeyEvent.VK_X);

		// set accelerattor for exit item
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));
		// set accelerattor for import item
		importDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				ActionEvent.CTRL_MASK));
		// set accelerator for prefs item
		prefsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.CTRL_MASK));

		// add actionlistener to exitItem
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// //get username
				// String text = JOptionPane.showInputDialog(MainFrame.this,
				// "Enter User Name",
				// "User Confirmation", JOptionPane.OK_OPTION
				// | JOptionPane.QUESTION_MESSAGE);
				// System.out.println(text);

				int action = JOptionPane.showConfirmDialog(MainFrame.this,
						"Do you really wanna exit ?", "Confirm Exit !",
						JOptionPane.OK_CANCEL_OPTION);
				if (action == JOptionPane.OK_OPTION) {
					// System.exit(0);
					WindowListener[] listeners = getWindowListeners();
					for (WindowListener listener : listeners) {
						// call the previously set window closing method in
						// adapter for window listener
						listener.windowClosing(new WindowEvent(MainFrame.this,
								0));
						;
					}
				}
			}

		});

		return menuBar;
	}
}
