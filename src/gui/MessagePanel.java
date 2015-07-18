package gui;

//adds message tree
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import model.Message;
import controller.MessageServer;

public class MessagePanel extends JPanel implements ProgressDialogListener {
	private JTree serverTree;
	private ServerTreeCellRenderer treeCellRenderer;
	private ServerTreeCellEditor treeCellEditor;
	private Set<Integer> selectedServers;
	private MessageServer messageServer;
	private ProgressDialog progressDialog;
	private SwingWorker<List<Message>, Integer> worker;
	private TextPanel textPanel;
	private JList messageList;
	private JSplitPane upperPane;
	private JSplitPane lowerPane;
	private DefaultListModel messageListModel;

	public MessagePanel(JFrame parent) {

		messageListModel = new DefaultListModel();
		treeCellRenderer = new ServerTreeCellRenderer();
		treeCellEditor = new ServerTreeCellEditor();
		selectedServers = new TreeSet<>();
		messageServer = new MessageServer();
		progressDialog = new ProgressDialog(parent, "Message  downloading.....");
		progressDialog.setListener(this);

		selectedServers.add(0);
		selectedServers.add(1);
		selectedServers.add(2);
		selectedServers.add(4);
		selectedServers.add(5);
		selectedServers.add(7);
		selectedServers.add(8);

		serverTree = new JTree(createTree());
		serverTree.setCellRenderer(treeCellRenderer);
		serverTree.setEditable(true);
		serverTree.setCellEditor(treeCellEditor);

		setLayout(new BorderLayout());

		textPanel = new TextPanel();
		messageList = new JList(messageListModel);

		messageList.setCellRenderer(new MessageListRenderer());
		messageList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Message m = (Message) messageList.getSelectedValue();
				textPanel.setText(m.getContents());

			}

		});

		// set selection model
		serverTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		messageServer.setSelectedServers(selectedServers);

		treeCellEditor.addCellEditorListener(new CellEditorListener() {

			@Override
			public void editingStopped(ChangeEvent e) {
				// TODO Auto-generated method stub

				ServerInfo info = (ServerInfo) treeCellEditor
						.getCellEditorValue();
				System.out.println(info + " " + info.getId() + " : "
						+ info.isChecked());

				int serverId = info.getId();
				if (info.isChecked()) {
					selectedServers.add(serverId);
				} else {
					selectedServers.remove(serverId);
				}

				messageServer.setSelectedServers(selectedServers);
				retrieveMessages();
			}

			@Override
			public void editingCanceled(ChangeEvent e) {

			}

		});

		// set selection listener
		// serverTree.addTreeSelectionListener(new TreeSelectionListener() {
		// public void valueChanged(TreeSelectionEvent e) {
		//
		// DefaultMutableTreeNode node = (DefaultMutableTreeNode) serverTree
		// .getLastSelectedPathComponent();
		//
		// Object userObject = node.getUserObject();
		// // if( userObject instanceof ServerInfo)
		// // {
		// // int id = ((ServerInfo)userObject).getId();
		// // System.out.println("nodde id = " + id);
		// // }
		// System.out.println(userObject);
		// }
		//
		// });

		// set split panes with JTree component
		lowerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(
				messageList), textPanel);
		upperPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(
				serverTree), lowerPane);

		// give equal weight to upper and lower panes
		upperPane.setResizeWeight(0.5);
		lowerPane.setResizeWeight(0.5);

		textPanel.setMinimumSize(new Dimension(10, 100));
		messageList.setMinimumSize(new Dimension(10, 100));

		add(upperPane, BorderLayout.CENTER);

	}

	public void refresh() {
		retrieveMessages();
	}

	private void retrieveMessages() {

		// System.out.println("msg waiting : " +
		// messageServer.getMessageCount());
		progressDialog.setMaximum(messageServer.getMessageCount());

		// overridden in ProgressDialog
		progressDialog.setVisible(true);

		// multi threading in swing
		// similar to async task class in android api
		worker = new SwingWorker<List<Message>, Integer>() {

			@Override
			protected List<Message> doInBackground() throws Exception {

				int count = 0;
				List<Message> retrievedMessages = new ArrayList<Message>();

				for (Message message : messageServer) {

					if (isCancelled())
						break;

					System.out.println(message);
					retrievedMessages.add(message);
					++count;
					publish(count);
				}
				return retrievedMessages;
			}

			@Override
			// called on main thread
			// recieves list of type publish() in doInbackGround(), second
			// argument of swingwroker instance
			protected void process(List<Integer> counts) {
				int retrieved = counts.get(counts.size() - 1);

				progressDialog.setValue(retrieved);
				// System.out.println("got : " + retrieved + " messages");
			}

			// when thread finished
			protected void done() {

				progressDialog.setVisible(false);
				if (isCancelled())
					return;

				try {
					List<Message> retrievedMessages = get();// this will recieve

					messageListModel.removeAllElements();

					for (Message message : retrievedMessages) {
						messageListModel.addElement(message);
					}
					
					messageList.setSelectedIndex(0);

					// stuff from
					// doInBackground()
					// System.out.println("Retrieved " +
					// retrievedMessages.size()
					// + " messages ");
				} catch (InterruptedException e) {

				} catch (ExecutionException e) {

				}

				// hide progressDialog
				progressDialog.setVisible(false);
				System.out.println("false");
			}

		};
		worker.execute();

	}

	private DefaultMutableTreeNode createTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Servers");

		DefaultMutableTreeNode branch1 = new DefaultMutableTreeNode("INDIA");

		DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(
				new ServerInfo("JK", 0, selectedServers.contains(0)));
		DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(
				new ServerInfo("SATHIALA", 1, selectedServers.contains(1)));
		DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(
				new ServerInfo("NEW DELHI", 2, selectedServers.contains(2)));

		branch1.add(server1);
		branch1.add(server2);
		branch1.add(server3);

		DefaultMutableTreeNode branch2 = new DefaultMutableTreeNode("Earth");

		DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(
				new ServerInfo("INDONESIA", 3, selectedServers.contains(3)));
		DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(
				new ServerInfo("UK", 4, selectedServers.contains(4)));
		DefaultMutableTreeNode server6 = new DefaultMutableTreeNode(
				new ServerInfo("USA", 5, selectedServers.contains(5)));

		branch2.add(server4);
		branch2.add(server5);
		branch2.add(server6);

		DefaultMutableTreeNode branch3 = new DefaultMutableTreeNode("MilkyWay");

		DefaultMutableTreeNode server7 = new DefaultMutableTreeNode(
				new ServerInfo("MOON", 6, selectedServers.contains(6)));
		DefaultMutableTreeNode server8 = new DefaultMutableTreeNode(
				new ServerInfo("MARS", 7, selectedServers.contains(7)));
		DefaultMutableTreeNode server9 = new DefaultMutableTreeNode(
				new ServerInfo("BLACK HOLE ", 8, selectedServers.contains(8)));

		branch3.add(server7);
		branch3.add(server8);
		branch3.add(server9);

		top.add(branch1);
		top.add(branch2);
		top.add(branch3);

		return top;
	}

	@Override
	public void progressDialogCancelled() {
		if (worker != null) {
			worker.cancel(true);

		}

	}
}
