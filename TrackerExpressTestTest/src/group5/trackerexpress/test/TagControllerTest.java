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
	
	
}
