package group5.trackerexpress.test;


import java.util.UUID;

import group5.trackerexpress.Controller;
import group5.trackerexpress.MainActivity;
import group5.trackerexpress.Tag;
import group5.trackerexpress.TagMap;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class TagControllerTest extends
ActivityInstrumentationTestCase2<MainActivity> {

	Instrumentation instrumentation;
	Context context;
	TagMap tags;

	public TagControllerTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		instrumentation = getInstrumentation();
		context = getActivity();
		tags = Controller.getTagMap(context);
	}
	

	
	public void testCreateTag(){
		Tag tag = new Tag("Business");
		tags.addTag(context, tag);
		assertTrue("Tag couldn't be added", tags.getTag(tag.getUuid()).toString().equals("Business"));
		
		tags.deleteTag(context, tag.getUuid());
	}

	
	public void testRenameTag(){
		Tag tag = new Tag("Business");
		tags.addTag(context, tag);
		tags.getTag(tag.getUuid()).rename(context, "Pleasure");
		assertTrue("Tag couldn't be renamed", tags.getTag(tag.getUuid()).toString().equals("Pleasure"));

		tags.deleteTag(context, tag.getUuid());
	}
	
	public void testDeleteTag(){
		Tag tag = new Tag("Business");
		tags.addTag(context, tag);
		int numTagsBeforeDeletion = tags.size();
		
		tags.deleteTag(context, tag.getUuid());

		assertTrue("Tag couldn't be deleted", tags.getTag(tag.getUuid()) == null);
		assertTrue("Tag couldn't be deleted", tags.size() == numTagsBeforeDeletion - 1);
	}
	
}
