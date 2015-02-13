package group5.trackerexpress.test;
import group5.trackerexpress.Claim;
import group5.trackerexpress.Expense;
import group5.trackerexpress.MainActivity;
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import group5.trackerexpress.R;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	
	Instrumentation instrumentation;

	public MainActivityTest(String name) {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testReturnClaim(){
		
	}
	
	public void testDeleteClaim (){
		final ListView claimlist = (ListView) findViewById(R.id.claimlist);
		final ArrayAdapter<Claim> adapter = (ArrayAdapter<Claim>) claimlist.getAdapter();
		final TextView claim_name;
		final Button b_deleteClaim = (Button) findViewById(R.id.b_deleteClaim);
		
		Activity activity = getActivity();
		
		String title = "A Claim";
		addClaim(title);
		
		final TextView returned;
		
		String retrieved = adapter.getItem(0).getTitle();
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				returned = (TextView) adapter.getView(0, claim_name, claimlist);
				returned.performLongClick();
				b_deleteClaim.performClick();
			}
			
		});
				
		instrumentation.waitForIdleSync();

		// check if the first item is still the first item (i.e. claim deleted)
		assertTrue( !(adapter.getItem(0).getTitle().equals(title)) );
	}
	

	public void testEditclaim() {
		final String rename = "new claim name";
		
		final ListView claimlist = (ListView) findViewById(R.id.claimlist);
		final Button b_editclaim = (Button) findViewById(R.id.b_editclaim);
		final EditText et_claim_new_name = (EditText) findViewById(R.id.et_claim_new_name);
		final Button b_saveedit = (Button) findViewById(R.id.b_saveedit);
		final Button b_cancleedit = (Button) findViewById(R.id.b_cancleedit);
		final TextView claimlistItem;
		final ArrayAdapter<Claim> adapter = (ArrayAdapter<Claim>) claimlist.getAdapter();
		
		Activity activity = getActivity();
		addClaim(rename);
		
		String retrived = adapter.getItem(0).getTitle();
		
		final TextView returned;
		
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				returned = (TextView) adapter.getView(0, claimlistItem, claimlist);
				returned.performLongClick();
				b_editclaim.performClick();
				returned.setText(rename);
				b_saveedit.performClick();
			}
		});
				
		instrumentation.waitForIdleSync();

		// check if the first item is edited.
		assertTrue( adapter.getItem(0).getTitle().equals(rename) );
		
	}
	
	
	private void addClaim(final String name){
		final Button b_creatclaim = (Button) findViewById(R.id.b_creatclaim);
		final EditText et_claimTitle = (EditText) findViewById(R.id.et_claimTitle);
		final Button b_saveclaim = (Button) findViewById(R.id.b_saveclaim);
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				b_creatclaim.performClick();
				et_claimTitle.setText( name );
				b_saveclaim.performClick();
			}
			
		});
		
		instrumentation.waitForIdleSync();
	}
	
	
	
	
	
	//Note: Assumed that calling toString() on a listView element gives you that element's text
	public void testCreateTag(){
		
		clickTab(MainActivity.INDEX_OF_TAGS_TAB);
		
		View newTag = createTagAndReturnTagTextView();
		
		assertTrue(((EditText) newTag.findViewById(R.id.tag_text)).getText().equals("Business"));
		
		deleteTag(newTag);
	}


	public void testRenameTag(){
		
		View newTag = createTagAndReturnTagTextView();
		newTag.performLongClick();
		clickButton(R.id.rename_tag_button);
		setText(R.id.rename_tag_text_view, "Pleasure");
		clickButton(R.id.confirm_rename_tag_button);
		
		assertTrue(((EditText) newTag.findViewById(R.id.tag_text)).getText().equals("Pleasure"));
		
		deleteTag(newTag);
	}
	
	public void testDeleteTag(){
		
		int beforeCreatingTagCount = getTagArrayAdapter().getCount();
		View newTag = createTagAndReturnTagTextView();
		
		assertEquals("Can't do delete tag test, since create tag failed.", beforeCreatingTagCount + 1, getTagArrayAdapter().getCount());
		
		deleteTag(newTag);
		
		assertEquals("Delete tag did not work.", beforeCreatingTagCount, getTagArrayAdapter().getCount());
	}

	
	//not really complete, since it assumes claims already exist.
	public void testFilterTag(){
		View tag = createTagAndReturnTagTextView();
		
		tag.findViewById(R.id.tag_checkbox).performClick();
		
		
		clickTab(MainActivity.INDEX_OF_MY_CLAIMS_TAB);
		
		
		//TODO: need to add this tag to a claim
		
		

		//check if only one claim is displayed:
		ListAdapter adapter = ((ListView) getActivity().findViewById(R.id.claims_list_view)).getAdapter();
		assertEquals("Not right amount of claims displayed after filtering.", adapter.getCount(), 1);

		//check if claim has the expected name:
		View claimListItem = (View) adapter.getItem(0);
		assertTrue("The right claims didn't filter.", 
				((TextView) claimListItem.findViewById(R.id.action_settings)).getText().equals("Business Trip"));
		
		clickTab(MainActivity.INDEX_OF_TAGS_TAB);
		
		deleteTag(tag);
	}
	

	/*
	 * Takes the id of a text view and sets the text to a string.
	 */
	private void setText(int id, String text) {
		((EditText) getActivity().findViewById(id)).setText(text);
	}
	
	/*
	 * Takes the id of a button view and clicks on it.
	 */
	private void clickButton(int buttonId) {
		getActivity().findViewById(buttonId).performClick();
	}
	
	
	private View createTagAndReturnTagTextView() {
		
		clickTab(MainActivity.INDEX_OF_TAGS_TAB);
		
		int newItemPosition = getTagArrayAdapter().getCount();
		
		clickButton(R.id.create_tag_button);
		setText(R.id.create_tag_window, "Business");
		clickButton(R.id.confirm_create_tag_button);
		return (View) getTagArrayAdapter().getItem(newItemPosition);
	}


	
	private void deleteTag(View tag) {

		tag.performLongClick();
		clickButton(R.id.delete_tag_button);
		
		//maybe a confirm window?
		//clickButton(R.id.confirm_delete_tag_button);
	}

	private ListAdapter getTagArrayAdapter() {
		return ((ListView) getActivity().findViewById(R.id.tag_list_view)).getAdapter();
	}

	
	private void clickTab(int index){
		getActivity().getActionBar().getTabAt(index).select();
	}
}
