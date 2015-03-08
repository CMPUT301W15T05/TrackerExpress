package group5.trackerexpress;

import java.util.UUID;

public class Tag extends TModel {
	
	private String tagString;
	
	private UUID uuid = UUID.randomUUID();

	public Tag(String tagString) {
		this.tagString = tagString;
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

}
