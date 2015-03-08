package group5.trackerexpress;

import java.util.UUID;

public class Tag extends TModel {
	
	private String tagString;
<<<<<<< HEAD
	
	private UUID uuid = UUID.randomUUID();
=======
	private boolean selected;
>>>>>>> c3805c4aca3b3458a532fc2d8af5d89aa443e497

	public Tag(String tagString) {
		this.tagString = tagString;
		setSelected(true);
	}
	
	public UUID getUuid(){
		return uuid;
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
