package group5.trackerexpress;

public class Tag extends TModel {
	
	private String tagString;
	private boolean selected;

	public Tag(String tagString) {
		this.tagString = tagString;
		setSelected(true);
	}

	public void rename(String newName) {
		this.tagString = newName;
	}
	
	@Override
	public String toString(){
		return this.tagString;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	

}
