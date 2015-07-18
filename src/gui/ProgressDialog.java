package gui;

//this class pops up dialog in the message pannel while retrieving 
//fucking messages from a simulated message server
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressDialog extends JDialog {

	private JButton cancelBtn;
	private JProgressBar progressBar;
	private ProgressDialogListener listener;

	public void setListener(ProgressDialogListener listener) {
		this.listener = listener;
	}

	public ProgressDialog(Window parent, String title) {
		// Modal dialogs block all input to some top-level windows. Whether a
		// particular window is blocked depends on dialog's type of modality;
		// this is called the "scope of blocking".

		// An APPLICATION_MODAL dialog blocks all top-level windows from the
		// same Java application except those from its own child hierarchy
		super(parent, title, ModalityType.APPLICATION_MODAL);

		cancelBtn = new JButton("Cancel");
		progressBar = new JProgressBar();
		// progressBar.setIndeterminate(true);

		progressBar.setStringPainted(true);
		progressBar.setString("Retrieveing messages......");
		progressBar.setMaximum(10);

		Dimension size = cancelBtn.getPreferredSize();
		size.width = 400;
		progressBar.setPreferredSize(size);

		setSize(400, 200);

		setLayout(new FlowLayout());
		add(progressBar);

		add(cancelBtn);
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listener != null) {
					listener.progressDialogCancelled();
				}

			}

		});

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				if (listener != null) {
					listener.progressDialogCancelled();
				} 
			}
			
		});
		pack();
		setLocationRelativeTo(parent);
	}

	// corrresponds to 100% value of completion for progressBar
	public void setMaximum(int value) {
		progressBar.setMaximum(value);
	}

	// current value of progress bar
	public void setValue(int value) {

		int progress = 100 * value / progressBar.getMaximum();
		progressBar.setString(String.format("%d%% complete", progress));
		progressBar.setValue(value);

	}

	@Override
	public void setVisible(final boolean visible) {

		// bring an async task in new thread to smooth ui during multi threading
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				// System.out.println("showing model dialog");

				if (visible == false) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					progressBar.setValue(0);
				}
				
				//set cursor
				if(visible)
				{
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				}else{
					setCursor(Cursor.getDefaultCursor());

				}
				// show progressDialog
				ProgressDialog.super.setVisible(visible);

				// System.out.println("finished showing model dialog");

			}
		});
	}

}
