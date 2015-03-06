package group5.trackerexpress;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;

public class TagController implements TController {

	private static final String FILENAME = "tags.sav";
	private Map<Long, Tag> tags;
	private static TagController tagController;
	
	private TagController(Activity context){
		
		try {
			this.tags = new FileManager<HashMap<Long, Tag>>().getFile(context, FILENAME);
		} catch (IOException e) {
			System.err.println ("Tags file not found, making a fresh tags list.");
			this.tags = new HashMap<Long, Tag>();
		}

	}
	
	public static TagController getTagController(Activity context) {
		if (tagController == null)
			tagController = new TagController(context);
		return tagController;
	}
	
	public String getTag(long tagId){
		return tags.get(tagId).toString();
	}
	
	public long addTagAndReturnId(Activity context, String tagString){
		long tagId = new Random().nextLong();
		tags.put(tagId, new Tag(tagString));
		this.saveData(context);
		return tagId;
	}
	
	public void deleteTag(Activity context, long tagId){
		tags.remove(tagId);
		this.saveData(context);
	}
	
	public void renameTag(Activity context, long tagId, String newName){
		this.tags.get(tagId).rename(newName);
		this.saveData(context);
	}

	
	private void saveData(Activity context) {
		//TODO: Save data using fileIO calls
	}
}
