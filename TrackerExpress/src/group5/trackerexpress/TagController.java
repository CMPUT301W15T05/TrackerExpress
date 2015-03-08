package group5.trackerexpress;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;

public class TagController implements TController {

	private static final String FILENAME = "tags.sav";
	private Map<UUID, Tag> tags;
	private Context context;
	private static TagController instance;
	
	private TagController(Context context){
		this.context = context;
	}


	public static TagController getInstance() throws ExceptionControllerNotInitialized {
		if (instance == null)		
			throw new ExceptionControllerNotInitialized();
		instance.loadData();
		return instance;
	}
	
	/*!
	 * Initializes the tag controller making a singleton and storing a context
	 * for file IO. MAKE SURE ACTIVITY THAT INITIALIZES DOESN'T STOP, THATS WOULD BE BAD
	 */
	public static TagController initialize(Context context) throws ExceptionControllerAlreadyInitialized {
		if (instance != null)
			throw new ExceptionControllerAlreadyInitialized();
		instance = new TagController(context);
		return instance;
	}
	
	public String getTag(UUID tagId){
		this.loadData();
		return tags.get(tagId).toString();
	}

	public void setTag(Tag tag){
		this.loadData();
		tags.put(tag.getUuid(), tag);
		this.saveData();
	}
	
	//Adds a tag, taking either a tag object or just a string.
	//Returns the UUID, incase you need it
	public UUID addTag(String tagString){
		this.loadData();
		Tag newTag = new Tag(tagString);
		tags.put(newTag.getUuid(), newTag);
		this.saveData();
		return newTag.getUuid();
	}
	
	public UUID addTag(Tag newTag){
		this.loadData();
		tags.put(newTag.getUuid(), newTag);
		this.saveData();
		return newTag.getUuid();
	}
	
	public void deleteTag(UUID tagId){
		this.loadData();
		tags.remove(tagId);
		this.saveData();
	}
	
	public void deleteTag(Tag tag){
		this.loadData();
		tags.remove(tag);
		this.saveData();
	}
	
	public void renameTag(UUID tagId, String newName){
		this.loadData();
		this.tags.get(tagId).rename(newName);
		this.saveData();
	}
	
	private void saveData() {
		try {
			new FileCourrier<Map<UUID, Tag>>().saveFile(context, FILENAME, tags);
		} catch (IOException e) {
			System.err.println ("Could not save tags.");
			throw new RuntimeException();
		}
	}

	private void loadData() {
		try {
			this.tags = new FileCourrier<Map<UUID, Tag>>().loadFile(context, FILENAME);
		} catch (IOException e) {
			System.err.println ("Tags file not found, making a fresh tags list.");
			this.tags = new HashMap<UUID, Tag>();
		}
	}


	public int getNumTags() {
		return tags.size();
	}

}
