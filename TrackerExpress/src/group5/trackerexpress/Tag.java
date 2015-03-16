package group5.trackerexpress;

import java.util.UUID;

import android.content.Context;

public class Tag extends TModel {
	
	private String tagString;
	
	private UUID uuid;

	private boolean selected;

	public Tag(String tagString) {
		super();
		this.tagString = tagString;
		this.uuid = UUID.randomUUID();
		selected = true;
	}
	
	public UUID getUuid(){
		return uuid;
	}

	public void rename(Context context, String newName) {
		this.tagString = newName;
		notifyViews(context);
	}
	
	@Override
	public String toString(){
		return this.tagString;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(Context context, boolean selected) {
		this.selected = selected;
		notifyViews(context);
	}
	

}
