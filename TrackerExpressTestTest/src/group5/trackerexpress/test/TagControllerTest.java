package group5.trackerexpress.test;

import java.util.UUID;

import group5.trackerexpress.MainActivity;
import group5.trackerexpress.TagController;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class TagControllerTest extends
ActivityInstrumentationTestCase2<MainActivity> {

	Instrumentation instrumentation;
	Context context;
	TagController controller;

	public TagControllerTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		instrumentation = getInstrumentation();
		context = getActivity();
		TagController.initialize(context);
		controller = TagController.getInstance();
	}
	

	
	public void testCreateTag(){
		UUID tagId = controller.addTag("Business");
		assertTrue("Tag couldn't be added", controller.getTag(tagId).toString().equals("Business"));
		
		controller.deleteTag(tagId);
	}

	
	public void testRenameTag(){
		UUID tagId = controller.addTag("Business");
		controller.renameTag(tagId, "Pleasure");
		assertTrue("Tag couldn't be renamed", controller.getTag(tagId).equals("Pleasure"));

		controller.deleteTag(tagId);
	}
	
	public void testDeleteTag(){
		UUID tagId = controller.addTag("Business");
		int numTagsBeforeDeletion = controller.getNumTags();
		
		controller.deleteTag(tagId);

		assertTrue("Tag couldn't be deleted", controller.getTag(tagId) == null);
		assertTrue("Tag couldn't be deleted", controller.getNumTags() == numTagsBeforeDeletion - 1);
	}
	
}
