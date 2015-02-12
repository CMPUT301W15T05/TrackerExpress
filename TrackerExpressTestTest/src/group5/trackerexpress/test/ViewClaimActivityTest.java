package group5.trackerexpress.test;
import java.util.ArrayList;

import group5.trackerexpress.Claim;
import group5.trackerexpress.Expense;
import group5.trackerexpress.R;
import group5.trackerexpress.ViewClaimActivity;
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class ViewClaimActivityTest extends
		ActivityInstrumentationTestCase2<ViewClaimActivity> {
	
	Instrumentation instrumentation;
	
	public ViewClaimActivityTest() {
		super(ViewClaimActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testDeleteExpense (){
		final ListView lv_before = (ListView) findViewById(R.id.lv_expenses);
		final ArrayAdapter<Expense> adapter = (ArrayAdapter<Expense>) lv_before.getAdapter();
		final TextView tv_lv_item;
		final Button b_deleteExpense = (Button) findViewById(R.id.b_delete_expense);
		
		Activity activity = getActivity();
		
		String title = "Expense title";
		addExpense(title);
		
		final TextView returned;
		
		String retrieved = adapter.getItem(0).getTitle();
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				returned = (TextView) adapter.getView(0, tv_lv_item, lv_before);
				returned.performLongClick();
				b_deleteExpense.performClick();
			}
			
		});
				
		instrumentation.waitForIdleSync();

		// check if the first item is still the first item (i.e. expense deleted)
		assertTrue( !(adapter.getItem(0).getTitle().equals(title)) );
	}
	
	public void testEditExpense() {
		
	}
	
	private void addExpense(final String name){
		final Button b_addExpense = (Button) findViewById(R.id.b_add_expense);
		final EditText et_expenseTitle = (EditText) findViewById(R.id.et_expense_title);
		final Button b_saveExpense = (Button) findViewById(R.id.b_save_expense);
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				b_addExpense.performClick();
				et_expenseTitle.setText( name );
				b_saveExpense.performClick();
				// Assuming adding an incomplete claim will not prompt a new message
			}
			
		});
		
		instrumentation.waitForIdleSync();
	}
}
