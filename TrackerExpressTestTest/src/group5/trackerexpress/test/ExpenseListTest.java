package group5.trackerexpress.test;
import group5.trackerexpress.Expense;
import group5.trackerexpress.ExpenseList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import junit.framework.TestCase;


public class ExpenseListTest extends TestCase {
	
	
	public void testEmptyExpenseList() {
		ExpenseList expList = new ExpenseList();
		assertTrue("Empty expense list", expList.size()==0);
	}
	
	
	
	public void testAddExpenseList() {
		ExpenseList expList = new ExpenseList();
		String expName = "An expense item";
		
		Expense testExp = new Expense(expName);
		expList.add(testExp);
		
		assertTrue("Empty expense list", expList.size()==1);
		assertTrue("Test expense item not contained", expList.contains(testExp));
	}
	
	
	
	public void testGetExpenseList() {
		ExpenseList expList = new ExpenseList();
		
		String expName = "An expense item";
		Expense testExp = new Expense(expName);
		expList.add(testExp);
		Collection<Expense> expense = expList.getList();
		
		assertTrue("Empty expense list", expense.size()==1);
		assertTrue("Test expense item not contained", expense.contains(testExp));
	}
	
	
	
	public void testRemoveExpenseList() {
		
		ExpenseList expList = new ExpenseList();
		String expName = "An expense item";
		Expense testExp = new Expense(expName);
		
		expList.add(testExp);
		assertTrue("List size isn't big enough", expList.size()==1);
		assertTrue("Test expense item is not contained", expList.contains(testExp));
		expList.remove(testExp);
		assertTrue("List size isn't small enough", expList.size()==0);
		assertFalse("Test expense item still contained", expList.contains(testExp));
	}
	
	// this is a model test for 05.01.01
	public void testExpenseOrder() {
		ExpenseList expenseList = new ExpenseList();
		
		// Named so they come out in this order
		Expense e1 = new Expense("1");
		Expense e2 = new Expense("2");
		Expense e3 = new Expense("3");

    	
		expenseList.add(e1);
		expenseList.add(e2);
		expenseList.add(e3);

		assertTrue("List size isn't correct", expenseList.size()==3);
		assertTrue("Item '1' is not first",  expenseList.getList().get(0).getTitle().equals("1") );
		assertTrue("Item '2' is not second", expenseList.getList().get(1).getTitle().equals("2") );
		assertTrue("Item '3' is not last", expenseList.getList().get(2).getTitle().equals("3") );

	}
}
