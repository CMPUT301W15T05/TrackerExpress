package group5.trackerexpress.test;

import group5.trackerexpress.Claim;
import group5.trackerexpress.Controller;
import group5.trackerexpress.Tag;
import group5.trackerexpress.TagMap;
import group5.trackerexpress.TestActivity;
import group5.trackerexpress.User;

import java.util.UUID;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class NewClaimTest extends ActivityInstrumentationTestCase2<TestActivity> {

	Context context;
	Claim claim;
	User user;
	
	public NewClaimTest() {
		super(TestActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		context = getActivity();
		user = new User(context);
		user.setName(context, "TestUser");
		claim = new Claim("test");
	}

	public void testIncomplete(){
		assertTrue("Claim should not be incomplete", claim.isIncomplete());
		claim.setIncomplete(context, false);
		assertFalse("Claim should be incomplete", claim.isIncomplete());
		claim.setIncomplete(context, true);
	}
	
	public void testUser() {
		claim.setuserName(context, user.getName());
		assertTrue("Name should be TestUser", claim.getuserName() == "TestUser");
	}
	
	public void testExpenseList(){
		assertTrue("ExpenseList should be empty", claim.getExpenseList().size() == 0);
	}
	
	public void testAddDestination(){
		claim.addDestination(context, "Test", "for Testing");
		assertTrue("Should have one destination", claim.getDestinationList().size() == 1);
	}
	
	public void testGetTagsIds() {
		Tag t = new Tag("TestTag");
		UUID u = t.getUuid();
		TagMap tMap = Controller.getTagMap(context);
		tMap.addTag(context, t);
		claim.getTagsIds(context).add(u);
		assertTrue("Should have one Tag", claim.getTagsIds(context).size() == 1);
		assertTrue("Should have TestTag tag", claim.getTagsIds(context).contains(u));
		assertTrue("Tag should be called TestTag", Controller.getTagMap(context).getTag(u).toString() == "TestTag");
	}
	
	public void testToStringTags() {
		Tag t2 = new Tag("TestTag2");
		TagMap tMap = Controller.getTagMap(context);
		tMap.addTag(context, t2);
		claim.getTagsIds(context).add(t2.getUuid());
		assertTrue("Should have 2 tags", claim.getTagsIds(context).size() == 2);
		assertTrue("Should be TestTag, TestTag2", claim.toStringTags(context) == "TestTag, TestTag2");
	}
}
