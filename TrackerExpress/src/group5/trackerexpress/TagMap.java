package group5.trackerexpress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.content.Context;

public class TagMap extends TModel{
	
	private static final String FILENAME = "tags.sav";
	private Map<UUID, Tag> tags;
	
	public TagMap(Context context){
		loadData(context);
	}

	public void saveData(Context context) {
		try {
			new FileCourrier<TagMap>(this).saveFile(context, FILENAME, this);
			//new FileCourrier<Map<UUID, Tag>>(this).saveFile(context, FILENAME, tags);
		} catch (IOException e) {
			System.err.println ("Could not save tags.");
			throw new RuntimeException();
		}
	}

	public void loadData(Context context) {
		TagMap tagMap;
		try {
			tagMap = new FileCourrier<TagMap>(this).loadFile(context, FILENAME);
			if (tagMap.tags == null) {
				System.err.println ("TAGMAP ALSO NULL ALSO NULL");
				this.tags = new HashMap<UUID, Tag>();
			} else {
				this.tags = tagMap.tags;
			}
			//this.tags = new FileCourrier<Map<UUID, Tag>>((Map<UUID, Tag>) this).loadFile(context, FILENAME);
		} catch (FileNotFoundException e) {
			System.err.println ("Tags file not found, making a fresh tags list.");
			this.tags = new HashMap<UUID, Tag>();
		} catch (IOException e){
			throw new RuntimeException();
		}
	}
	
	
	public Tag getTag(UUID id){
		return tags.get(id);
	}

	public void clear(Context context){
		tags.clear();
		notifyViews(context);
	}

	public boolean isEmpty() {
		return tags.isEmpty();
	}

	public void addTag(Context context, Tag tag) {
		tags.put(tag.getUuid(), tag);
		notifyViews(context);
	}

	public void deleteTag(Context context, UUID id) {
		tags.remove(id);
		notifyViews(context);
	}

	public int size() {
		return tags.size();
	}

	public ArrayList<Tag> getTags() {
		return new ArrayList<Tag>(tags.values());
	}

}
