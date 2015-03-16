package group5.trackerexpress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import android.content.Context;

public class TagMap extends TModel{
	
	private static final String FILENAME = "tags.sav";
	private Map<UUID, Tag> tags;
	
	/*
	 * Constructor.
	 * 
	 * 
	 */
	public TagMap(Context context){
		super();
		tags = new HashMap<UUID, Tag>();
		loadData(context);
	}

	public void saveData(Context context) {
		try {
			new FileCourrier<TagMap>(this).saveFile(context, FILENAME, this);
		} catch (IOException e) {
			System.err.println ("Could not save tags.");
			throw new RuntimeException();
		}
	}

	public void loadData(Context context) {
		try {
			TagMap savedTagMap = new FileCourrier<TagMap>(this).loadFile(context, FILENAME);
			if (savedTagMap.tags == null)
				throw new FileNotFoundException();
			this.tags = savedTagMap.tags;
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
		makeSureViewsIsntNull();
		tag.addViews(this.views);
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
	
	@Override
	public void addView(TView view){
		super.addView(view);
		Iterator<Entry<UUID, Tag>> it = tags.entrySet().iterator();
		while (it.hasNext()) {
			it.next().getValue().addView(view);
		}
	}
}
