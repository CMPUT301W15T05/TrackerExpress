import java.util.Date;

import junit.framework.TestCase;


public class ExpenseItemTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testContructWithParameters(){
		try{
			ExpenseItem expense = constructWithParameters();
		}
		catch (Exception e){
			fail();
			System.out.println("Expense Item Constructor threw exception.");
		}
	}
	
	public void testContructWithoutParameters(){
		try{
			ExpenseItem expense = new ExpenseItem();
		}
		catch (Exception e){
			fail();
			System.out.println("Expense Item Constructor threw exception.");
		}
	}
	
	public void testIsComplete(){
		ExpenseItem expense = new Expense();
		assertFalse(expense.isComplete());
		
		ExpenseItem expense = constructWithParameters();
		assertTrue(expense.isComplete());
	}
	
	public void testMarkAsIncomplete(){
		ExpenseItem expense = constructWithParameters();
		expense.markAsIncomplete();
		assertFalse(expense.isComplete());
	}
	
	
	public void testUnmarkAsIncomplete(){
		ExpenseItem expense = constructWithParameters();
		expense.markAsIncomplete();
		expense.unmarkAsIncomplete();		
		assertTrue(expense.isComplete());
	}
	
	//even if they uncheck the incomplete button, if parameters are missing,
	//the claim should still be incomplete
	public void testUnmarkAsIncompleteWithMissingParameters(){
		ExpenseItem expense = new Expense();
		expense.markAsIncomplete();
		expense.unmarkAsIncomplete();		
		assertFalse(expense.isComplete());
	}
	
	
	public void testGetDate(){
		ExpenseItem expenseItem = constructWithParameters();
		assertTrue(expenseItem.getDate().equals(new Date(11111)));
	}
	
	//i wrote this test thinking Categories woud be an enum we make
	//but maybe it should just be res/values/string objects, referenced in java
	//like R.String.currency_CAD
	public void testGetCategory(){
		ExpenseItem expenseItem = constructWithParameters();
		assertTrue(expenseItem.getCategory().equals(Category.VEHICLE_RENTAL));
	}
	
	public void testGetDescription(){
		ExpenseItem expenseItem = constructWithParameters();
		assertTrue(expenseItem.getDescription().equals("A description"));
	}
	
	public void testGetAmountSpent(){
		ExpenseItem expenseItem = constructWithParameters();
		assertTrue(expenseItem.getAmountSpent().equals(1000));
	}
	
	public void testGetCurrency(){
		ExpenseItem expenseItem = constructWithParameters();
		assertTrue(expenseItem.getCurrency().equals(Currency.CAD));
	}
	
	
	
	
	
	public void testSetDate(){
		ExpenseItem expense = new ExpenseItem();
		expense.setDate(new Date(11111));
		assertTrue(expenseItem.getDate().equals(new Date(11111)));
	}
	
	public void testSetCategory(){
		ExpenseItem expense = new ExpenseItem();
		expense.setCategory(Category.VEHICLE_RENTAL);
		assertTrue(expenseItem.getCategory().equals(Category.VEHICLE_RENTAL));
	}
	
	public void testSetDescription(){
		ExpenseItem expense = new ExpenseItem();
		expense.setDescription("A description");
		assertTrue(expenseItem.getDescription().equals("A description"));
	}
	
	public void testSetAmountSpent(){
		ExpenseItem expense = new ExpenseItem();
		expense.setAmountSpend(1000);
		assertTrue(expenseItem.getAmountSpent().equals(1000));
	}
	
	public void testSetCurrency(){
		ExpenseItem expense = new ExpenseItem();
		expense.setCurrency(Currency.CAD);
		assertTrue(expenseItem.getCurrency().equals(Currency.CAD));
	}

	
	private ExpenseItem constructWithParameters(){
		return new Expense(new Date(11111), Category.VEHICLE_RENTAL, "A description", 
				1000, Currency.CAD);
	}
	
		
	
	
}
