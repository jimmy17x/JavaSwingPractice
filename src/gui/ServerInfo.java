package gui;

class ServerInfo {
	private String name;
	private int id;
	private boolean checked;

	public ServerInfo(String name, int id, boolean checked) {
		this.name = name;
		this.id = id;
		this.checked = checked;
	}

	public String toString() {
		return this.name;
	}

	public int getId() {
		return this.id;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean c) {
		checked = c;
	}
}
