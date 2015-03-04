package group5.trackerexpress.test;
import java.util.UUID;

import group5.trackerexpress.Expense;
import group5.trackerexpress.ExpenseList;
import junit.framework.TestCase;

// Mostly a model test for 05.01.01
public class ExpenseListTest extends TestCase {
	
	
	public void testEmptyExpenseList() {
		ExpenseList expList = new ExpenseList();
		assertTrue("Empty expense list", expList.size()==0);
	}
	
	public void testAddExpenseList() {
		ExpenseList expList = new ExpenseList();
		
		Expense testExp = new Expense();
		UUID testUuid = testExp.getUuid();
		expList.addExpense(testExp);
		
		assertTrue("Empty expense list", expList.size()==1);
		assertTrue("Test expense item not contained", 
					testExp.equals(expList.getExpense(testUuid)));
	}	
	
	
	public void testRemoveExpenseList() {
		ExpenseList expList = new ExpenseList();
		Expense testExp = new Expense();
		
		expList.addExpense(testExp);
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
		Expense e1 = new Expense();
		UUID e1uuid = e1.getUuid();
		e1.setTitle("1");
		Expense e2 = new Expense();
		UUID e2uuid = e2.getUuid();
		e2.setTitle("2");
		Expense e3 = new Expense();
		UUID e3uuid = e3.getUuid();
		e3.setTitle("3");

    	
		expenseList.add(e1);
		expenseList.add(e2);
		expenseList.add(e3);

		assertTrue("List size isn't correct", expenseList.size()==3);
		assertTrue("Item '1' is not first",  expenseList.getList().get(0).getTitle().equals("1") );
		assertTrue("Item '2' is not second", expenseList.getList().get(1).getTitle().equals("2") );
		assertTrue("Item '3' is not last", expenseList.getList().get(2).getTitle().equals("3") );

	}
}
