package group5.trackerexpress.test;

import group5.trackerexpress.Claim;
import group5.trackerexpress.ClaimList;
import group5.trackerexpress.Controller;
import group5.trackerexpress.Expense;
import group5.trackerexpress.ExpenseNotFoundException;
import group5.trackerexpress.MainActivity;
import group5.trackerexpress.TestActivity;
import android.app.Instrumentation;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

public class ClaimControllerTest extends ActivityInstrumentationTestCase2<TestActivity> {

	Instrumentation instrumentation;
	Context context;
	ClaimList claims;
	public ClaimControllerTest() {
		super(TestActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		instrumentation = getInstrumentation();
		context = getActivity();
		claims = Controller.getClaimList(context);
	}
	
	public void testAddExpense() throws ExpenseNotFoundException{
		Claim claim = new Claim ("Toronto");
		Expense expense = new Expense ("Supper");
		claim.getExpenseList().addExpense(context, expense);
		assertTrue("Could not add new expense", claim.getExpenseList().getExpense(expense.getUuid()).getTitle().equals("Supper"));
	}


	public void testRemoveExpense() throws ExpenseNotFoundException{
		Claim claim = new Claim ("Toronto");
		Expense expense = new Expense ("Supper");
		claim.getExpenseList().addExpense(context, expense);
		int size = claim.getExpenseList().size();
		claim.getExpenseList().removeExpense(context, expense.getUuid());
		assertTrue("Could not remove expense", claim.getExpenseList().size()==size-1);
	}

	public void testAddDestination(){
		Claim claim = new Claim ("Toronto");
		int size = claim.getDestinationList().size();
		claim.addDestination(context, "Hamilton", "Business");
		assertTrue("Size did not increase", size + 1 == claim.getDestinationList().size());
		assertTrue("Could not add destination", claim.getDestinationList().get(claim.getDestinationList().size()-1).getName() == "Hamilton");
		assertTrue("Could not add destination2", claim.getDestinationList().get(claim.getDestinationList().size()-1).getDescription() == "Business");

	}
}