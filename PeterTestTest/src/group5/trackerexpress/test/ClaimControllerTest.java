package group5.trackerexpress.test;

import group5.trackerexpress.Claim;
import group5.trackerexpress.ClaimController;
import group5.trackerexpress.ClaimList;
import group5.trackerexpress.Expense;
import group5.trackerexpress.ExpenseNotFoundException;
import group5.trackerexpress.MainActivity;
import android.app.Instrumentation;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

public class ClaimControllerTest extends ActivityInstrumentationTestCase2<MainActivity> {

	Instrumentation instrumentation;
	Context context;
	ClaimList claims;
	public ClaimControllerTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		instrumentation = getInstrumentation();
		context = getActivity();
		claims = ClaimController.getInstance(context).getClaimList();
	}
	
	public void testAddExpense() throws ExpenseNotFoundException{
		Claim claim = new Claim ("Toronto");
		Expense expense = new Expense ("Supper");
		claim.addExpense(context, expense);
		assertTrue("Could not add new expense", claim.getExpenseList().getExpense(expense.getUuid()).getTitle().equals("Supper"));
	}


	public void testRemoveExpense() throws ExpenseNotFoundException{
		Claim claim = new Claim ("Toronto");
		Expense expense = new Expense ("Supper");
		claim.addExpense(context, expense);
		int size = claim.getExpenseList().size();
		claim.removeExpense(context, expense.getUuid());
		assertTrue("Could not remove expense", claim.getExpenseList().size()==size-1);
	}

	public void testAddDestination(){
		Claim claim = new Claim ("Toronto");
		int size = claim.getDestination().size();
		claim.addDestination(context, "Hamilton", "Business");
		assertTrue("Size did not increase", size + 1 == claim.getDestination().size());
		assertTrue("Could not add destination", claim.getDestination().get(claim.getDestination().size()-1)[0] == "Hamilton");
		assertTrue("Could not add destination2", claim.getDestination().get(claim.getDestination().size()-1)[1] == "Business");

	}
}

/*
package group5.trackerexpress.test;


import java.util.UUID;

import group5.trackerexpress.MainActivity;
import group5.trackerexpress.Tag;
import group5.trackerexpress.TagController;
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
		tags = TagController.getInstance(context).getTagMap();
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
*/