package group5.trackerexpress;

import java.util.UUID;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class Tag.
 */
public class Tag extends TModel {
	
	/** The tag string. */
	private String tagString;
	
	/** The tag id */
	private UUID uuid;

	/** The selected. */
	private boolean selected;

	/**
	 * Instantiates a new tag.
	 *
	 * @param tagString the tag string
	 */
	public Tag(String tagString) {
		super();
		this.tagString = tagString;
		this.uuid = UUID.randomUUID();
		selected = true;
	}
	
	/**
	 * Gets the tag id.
	 *
	 * @return the uuid
	 */
	public UUID getUuid(){
		return uuid;
	}

	/**
	 * Rename the tag.
	 *
	 * @param context the context
	 * @param newName the new name
	 */
	public void rename(Context context, String newName) {
		this.tagString = newName;
		notifyViews(context);
	}
	
	/**
	 * Gets name of tag
	 * 
	 * @return tagString name of tag
	 */
	@Override
	public String toString(){
		return this.tagString;
	}

	/**
	 * Checks if is selected.
	 *
	 * @return true, if is selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Sets the selected tag.
	 *
	 * @param context the context
	 * @param selected the selected
	 */
	public void setSelected(Context context, boolean selected) {
		this.selected = selected;
		notifyViews(context);
	}
	

}
