package group5.trackerexpress.test;
import group5.trackerexpress.MainActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import group5.trackerexpress.R;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public MainActivityTest(String name) {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testReturnClaim(){
		
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

	
	public void testFilterTag(){
		View tag = createTagAndReturnTagTextView();
		
		tag.findViewById(R.id.tag_checkbox).performClick();
		
		clickTab(MainActivity.INDEX_OF_MY_CLAIMS_TAB);
		
		getActivity().findViewById(R.id.claims_list)
	}
	

	
	private View createTagAndReturnTagTextView() {
		
		clickTab(MainActivity.INDEX_OF_TAGS_TAB);
		
		int newItemPosition = getTagArrayAdapter().getCount();
		
		clickButton(R.id.create_tag_button);
		setText(R.id.create_tag_window, "Business");
		clickButton(R.id.confirm_create_tag_button);
		return (View) getTagArrayAdapter().getItem(newItemPosition);
	}

	private void setText(int id, String text) {
		((EditText) getActivity().findViewById(id)).setText(text);
	}
	
	private void clickButton(int buttonId) {
		((Button) getActivity().findViewById(buttonId)).performClick();
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
