package group5.trackerexpress.test;

import java.util.UUID;

import group5.trackerexpress.Expense;
import group5.trackerexpress.ExpenseList;
import group5.trackerexpress.ExpenseNotFoundException;
import group5.trackerexpress.MainActivity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class ExpenseListTest extends ActivityInstrumentationTestCase2<MainActivity> {
	Context context;
	
	public ExpenseListTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		context = getActivity();
	}


	public void testAddExpenseList() throws ExpenseNotFoundException {
		ExpenseList expList = new ExpenseList();
		
		Expense testExp = new Expense();
		UUID testUuid = testExp.getUuid();
		expList.addExpense(testExp);
		
		assertTrue("Empty expense list", expList.size()==1);
		assertTrue("Test expense item not contained", 
					testExp.equals(expList.getExpense(testUuid)));
	}	
	
	
	public void testRemoveExpenseList() throws ExpenseNotFoundException {
		ExpenseList expList = new ExpenseList();
		Expense testExp = new Expense();
		UUID testUuid = testExp.getUuid();
		
		expList.addExpense(testExp);
		assertTrue("List size isn't big enough", expList.size()==1);
		assertTrue("Test expense item is not contained", 
					testExp.equals(expList.getExpense(testUuid)) );
		expList.deleteExpense(testExp.getUuid());
		assertTrue("List size isn't small enough", expList.size()==0);
		
		try{
			testExp.equals(expList.getExpense(testUuid));
			assertTrue("This should not have happened", false);
		} catch( IndexOutOfBoundsException e ){
			assertTrue("This should happen", true);			
		}
	}
	
	// this is a model test for 05.01.01
	public void testExpenseOrder() {
		ExpenseList expenseList = new ExpenseList();
		
		// Named so they come out in this order
		Expense e1 = new Expense();
		e1.setTitle(context, "1");
		Expense e2 = new Expense();
		e2.setTitle(context, "2");
		Expense e3 = new Expense();
		e3.setTitle(context, "3");

    	
		expenseList.addExpense(e1);
		expenseList.addExpense(e2);
		expenseList.addExpense(e3);

		assertTrue("Item '1' is not first",  
					expenseList.getExpenseList().get(0).getTitle().equals(e1.getTitle()) );
		assertTrue("Item '2' is not second", 
					expenseList.getExpenseList().get(1).getTitle().equals(e2.getTitle()) );
		assertTrue("Item '3' is not last", 
					expenseList.getExpenseList().get(2).getTitle().equals(e3.getTitle()) );

	}
	
}

/*
package group5.trackerexpress.test;
import java.util.UUID;

import group5.trackerexpress.Expense;
import group5.trackerexpress.ExpenseList;
import group5.trackerexpress.ExpenseNotFoundException;
import junit.framework.TestCase;

// Mostly a model test for 05.01.01
public class ExpenseListTest extends TestCase {
	
	
	public void testEmptyExpenseList() {
		ExpenseList expList = new ExpenseList();
		assertTrue("Empty expense list", expList.size()==0);
	}
	
	public void testAddExpenseList() throws ExpenseNotFoundException {
		ExpenseList expList = new ExpenseList();
		
		Expense testExp = new Expense();
		UUID testUuid = testExp.getUuid();
		expList.addExpense(testExp);
		
		assertTrue("Empty expense list", expList.size()==1);
		assertTrue("Test expense item not contained", 
					testExp.equals(expList.getExpense(testUuid)));
	}	
	
	
	public void testRemoveExpenseList() throws ExpenseNotFoundException {
		ExpenseList expList = new ExpenseList();
		Expense testExp = new Expense();
		UUID testUuid = testExp.getUuid();
		
		expList.addExpense(testExp);
		assertTrue("List size isn't big enough", expList.size()==1);
		assertTrue("Test expense item is not contained", 
					testExp.equals(expList.getExpense(testUuid)) );
		expList.deleteExpense(testExp.getUuid());
		assertTrue("List size isn't small enough", expList.size()==0);
		
		try{
			testExp.equals(expList.getExpense(testUuid));
			assertTrue("This should not have happened", false);
		} catch( IndexOutOfBoundsException e ){
			assertTrue("This shoould happen", true);			
		}
	}
	
	// this is a model test for 05.01.01
	public void testExpenseOrder() {
		ExpenseList expenseList = new ExpenseList();
		
		// Named so they come out in this order
		Expense e1 = new Expense();
		e1.setTitle("1");
		Expense e2 = new Expense();
		e2.setTitle("2");
		Expense e3 = new Expense();
		e3.setTitle("3");

    	
		expenseList.addExpense(e1);
		expenseList.addExpense(e2);
		expenseList.addExpense(e3);

		assertTrue("Item '1' is not first",  
					expenseList.getExpenseList().get(0).getTitle().equals(e1) );
		assertTrue("Item '2' is not second", 
					expenseList.getExpenseList().get(1).getTitle().equals(e2) );
		assertTrue("Item '3' is not last", 
					expenseList.getExpenseList().get(2).getTitle().equals(e3) );

	}
}*/
