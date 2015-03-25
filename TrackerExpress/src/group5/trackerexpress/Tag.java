package group5.trackerexpress;

import java.util.UUID;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class Tag.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class Tag extends TModel {
	
	/**
	 * generated serial number for Serializable type
	 */
	private static final long serialVersionUID = -6043555803464415928L;

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
	 * @param context Needed for file IO
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
	 * @param context Needed for file IO
	 * @param selected the selected
	 */
	public void setSelected(Context context, boolean selected) {
		this.selected = selected;
		notifyViews(context);
	}
	

}
