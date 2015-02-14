package group5.trackerexpress.test;
import group5.trackerexpress.Expense;
import group5.trackerexpress.ExpenseList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import junit.framework.TestCase;


public class ExpenseListTest extends TestCase {
	
	
	
	public void testEmptyExpenseList(){
		ExpenseList expList = new ExpenseList();
		assertTrue("Empty expense list", expList.size()==0);
	}
	
	
	
	public void testAddExpenseList(){
		
		ExpenseList expList = new ExpenseList();
		String expName  = "An expense item";
		
		Expense testExp = new Expense(expName);
		expList.add(testExp);
		
		assertTrue("Empty expense list", expList.size()==1);
		assertTrue("Test expense item not contained", expList.contains(testExp));
	}
	
	
	
	public void testGetExpenseList(){
		ExpenseList expList = new ExpenseList();
		
		String expName  = "An expense item";
		Expense testExp = new Expense(expName);
		expList.add(testExp);
		Collection<Expense> expense = expList.getExpense();
		
		assertTrue("Empty expense list", expense.size()==1);
		assertTrue("Test expense item not contained", expense.contains(testExp));
	}
	
	
	
	public void testRemoveExpenseList(){
		
		ExpenseList expList = new ExpenseList();
		String expName  = "An expense item";
		Expense testExp = new Expense(expName);
		
		expList.add(testExp);
		assertTrue("List size isn't big enough", expList.size()==1);
		assertTrue("", expList.contains(testExp));
		expList.remove(testExp);
		assertTrue("List size isn't small enough", expList.size()==0);
		assertFalse("test expense item still contained", expList.contains(testExp));
	}
	
	public void testExpenseOrder(){
		ExpenseList expenseList = new ExpenseList();
		
		// Named so they come out in this order
		Expense e1 = new Expense("3");
		Expense e2 = new Expense("1");
		Expense e3 = new Expense("2");
		
		GregorianCalendar d1 = new GregorianCalendar();
		GregorianCalendar d2 = new GregorianCalendar();
		GregorianCalendar d3 = new GregorianCalendar();
		
		d1.set(Calendar.YEAR, 1995);
		d1.set(Calendar.MONTH, 11); // december = 12
		d1.set(Calendar.DAY_OF_MONTH, 12);
		
		d2.set(Calendar.YEAR, 1995);
		d2.set(Calendar.MONTH, 0); // january = 0
		d2.set(Calendar.DAY_OF_MONTH, 12);
		
		d3.set(Calendar.YEAR, 1995);
		d3.set(Calendar.MONTH, 1); // february = 1
		d3.set(Calendar.DAY_OF_MONTH, 12);
		
		
		e1.setDate(d1);
		e2.setDate(d2);
		e3.setDate(d3);
    	
		expenseList.add(e1);
		expenseList.add(e2);
		expenseList.add(e3);

		assertTrue ( expenseList.getList().get(0).getTitle().equals("1") );
		assertTrue ( expenseList.getList().get(1).getTitle().equals("2") );
		assertTrue ( expenseList.getList().get(2).getTitle().equals("3") );

	}
}
