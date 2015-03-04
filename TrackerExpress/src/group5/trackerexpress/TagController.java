package group5.trackerexpress;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class TagController implements TController {

	private List<Tag> tags;
	private static TagController tagController;
	
	private TagController(Activity context){
		this.tags = new ArrayList<Tag>();
		
		//TODO: Use File I/O to populate tags
	}
	
	public static TagController getTagController(Activity context) {
		if (tagController == null)
			tagController = new TagController(context);
		return tagController;
	}
	
	//NOTE: tagIndex might end up having to be tagId, with tags being a map instead of a list.
	public Tag getTag(int tagIndex){
		return tags.get(tagIndex);
	}
	
	public void addTag(Activity context, Tag tag){
		tags.add(tag);
		this.saveData(context);
	}
	
	public void addTag(Activity context, String tagString){
		tags.add(new Tag(tagString));
		this.saveData(context);
	}
	
	public void removeTag(Activity context, Tag tag){
		tags.remove(tag);
		this.saveData(context);
	}
	
	public void removeTag(Activity context, int tagIndex){
		tags.remove(tagIndex);
		this.saveData(context);
	}
	
	public void renameTag(Activity context, int tagIndex, String newName){
		this.getTag(tagIndex).rename(newName);
		this.saveData(context);
	}

	
	private void saveData(Activity context) {
		//TODO: Save data using fileIO calls
	}
}
