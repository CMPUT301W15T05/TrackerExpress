package group5.trackerexpress;

public class Tag extends TModel {
	
	private String tagString;

	public Tag(String tagString) {
		this.tagString = tagString;
	}

	public void rename(String newName) {
		this.tagString = newName;
	}
	
	@Override
	public String toString(){
		return this.tagString;
	}

}
