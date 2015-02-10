import java.util.Collection;

import junit.framework.TestCase;


public class ExpenselistTest extends TestCase {
	
	
	
	public void testEmptyExpenseList(){
		ExpenseList expList = new ExpenseList();
		assertTrue("Empty expense list", expList.size()==0);
	}
	
	
	
	public void testAddClaimList(){
		
		ExpenseList expList = new ExpenseList();
		String expName  = "An expense item";
		
		Expense testExp = new Expense(expName);
		expList.addClaim(testExp);
		
		assertTrue("Empty expense list", expList.size()==1);
		assertTrue("Test expense item not contained", expList.contains(testExp));
	}
	
	
	
	public void testGetClaimList(){
		ExpenseList expList = new ExpenseList();
		
		String expName  = "An expense item";
		Expense testExp = new Expense(expName);
		expList.addClaim(testExp);
		Collection<Expense> claim = expList.getExpense();
		
		assertTrue("Empty expense list", claim.size()==1);
		assertTrue("Test expense item not contained", claim.contains(testExp));
	}
	
	
	
	public void testRemoveClaimList(){
		
		ExpenseList expList = new ExpenseList();
		String expName  = "An expense item";
		Expense testExp = new Expense(expName);
		
		expList.addClaim(testExp);
		assertTrue("List size isn't big enough", expList.size()==1);
		assertTrue("", expList.contains(testExp));
		expList.removeClaim(testExp);
		assertTrue("List size isn't small enough", expList.size()==0);
		assertFalse("test expense item still contained", expList.contains(testExp));
	}
	
	
}
