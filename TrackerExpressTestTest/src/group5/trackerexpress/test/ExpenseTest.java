package group5.trackerexpress.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.widget.TextView;
import group5.trackerexpress.Claim;
import group5.trackerexpress.Controller;
import group5.trackerexpress.Expense;
import group5.trackerexpress.ExpenseNotFoundException;
import junit.framework.TestCase;

public class ExpenseTest extends TestCase {

	// Note: Might add ClaimList to grab claim from if we throw errors on same 
	// claim name creation
	private Claim claim;
	
	// All tests for Claim class should be dealt with to ensure setUp works 
	// properly. We are assuming Claim will not cause any test failures.
	public void setUp() { 
		claim = new Claim("Test Claim");
	}
	
	// this is a test for 04.08.01
	public void testAddExpense() {	
		// Claimant selects claim to edit and adds the expense:
		Expense testExpense = new Expense("Test Expense");
		claim.addExpense(testExpense); // Note: Might change to ExpenseList.add(Expense) instead
		
		try {
			assertEquals("Expense not in claim's expense list", claim.getExpense("TestExpense"), testExpense);
		} catch (ExpenseNotFoundException e) {
			fail("Expense not found.");
		}
	}
	
	// this is a test for 04.06.01
	public void testEditExpense() {
		GregorianCalendar d1 = new GregorianCalendar();

		d1.set(Calendar.YEAR, 1995);
		d1.set(Calendar.MONTH, 11);
		d1.set(Calendar.DAY_OF_MONTH, 12);

		Expense testExpense = new Expense("Test Expense");
		claim.addExpense(testExpense);
		testExpense.setDate(d1);
		
		assertEquals("Expense not updated", testExpense.getDate(), d1);
		
		try {
			
			Expense testExpenseEdited = claim.getExpense("Test Expense");
			assertEquals("Expense not updated when grabbed from claim", testExpenseEdited.getDate(), d1);
			
			
		} catch (ExpenseNotFoundException e) {
			fail("Expense not found.");
		}
	}
	
	// this is a test case for 04.07.01
	public void testDeleteExpense() {
		Expense testExpense = new Expense("Test Expense");
		claim.addExpense(testExpense);
		
		claim.removeExpense("Test Expense");
		
		try {
			claim.getExpense("Test Expense");
			fail("Expense not deleted.");
		}
		catch(ExpenseNotFoundException e) {
			
		}
	}
	

}
