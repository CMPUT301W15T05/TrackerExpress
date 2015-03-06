package group5.trackerexpress.test;

import group5.trackerexpress.MainActivity;
import group5.trackerexpress.TagController;
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

public class TagControllerTest extends
ActivityInstrumentationTestCase2<MainActivity> {

	Instrumentation instrumentation;
	Activity activity;
	TagController controller;

	public TagControllerTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		instrumentation = getInstrumentation();
		activity = getActivity();
		controller = TagController.getTagController(activity);
	}
	

	
	public void testCreateTag(){
		long tagId = controller.addTagAndReturnId(activity, "Business");
		assertTrue("Tag couldn't be added", controller.getTag(tagId).equals("Business"));
		
		controller.deleteTag(activity, tagId);
	}
	
	
	public void testRenameTag(){
		long tagId = controller.addTagAndReturnId(activity, "Business");
		controller.renameTag(activity, tagId, "Pleasure");
		assertTrue("Tag couldn't be renamed", controller.getTag(tagId).equals("Pleasure"));

		controller.deleteTag(activity, tagId);
	}
	
	public void testDeleteTag(){
		long tagId = controller.addTagAndReturnId(activity, "Business");
		int numTagsBeforeDeletion = controller.getNumTags();
		
		controller.deleteTag(activity, tagId);

		assertTrue("Tag couldn't be deleted", controller.getTag(tagId) == null);
		assertTrue("Tag couldn't be deleted", controller.getNumTags() == numTagsBeforeDeletion - 1);
	}
	
}
