import java.util.Date;

import junit.framework.TestCase;


public class ExpenseItemTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testContructWithParameters(){
		try{
			ExpenseItem claim = constructWithParameters();
		}
		catch (Exception e){
			fail();
			System.out.println("Expense Item Constructor threw exception.");
		}
	}
	
	public void testContructWithoutParameters(){
		try{
			ExpenseItem claim = new ExpenseItem();
		}
		catch (Exception e){
			fail();
			System.out.println("Expense Item Constructor threw exception.");
		}
	}
	
	public void testIsComplete(){
		ExpenseItem claim = new Expense();
		assertFalse(claim.isComplete());
		
		ExpenseItem claim = constructWithParameters();
		assertTrue(claim.isComplete());
	}
	
	public void testMarkAsIncomplete(){
		ExpenseItem claim = constructWithParameters();
		claim.markAsIncomplete();
		assertFalse(claim.isComplete());
	}
	
	
	public void testUnmarkAsIncomplete(){
		ExpenseItem claim = constructWithParameters();
		claim.markAsIncomplete();
		claim.unmarkAsIncomplete();		
		assertTrue(claim.isComplete());
	}
	
	//even if they uncheck the incomplete button, if parameters are missing,
	//the claim should still be incomplete
	public void testUnmarkAsIncompleteWithMissingParameters(){
		ExpenseItem claim = new Expense();
		claim.markAsIncomplete();
		claim.unmarkAsIncomplete();		
		assertFalse(claim.isComplete());
	}
	
	
	public void testGetDate(){
		ExpenseItem claimItem = constructWithParameters();
		assertTrue(claimItem.getDate().equals(new Date(11111)));
	}
	
	//i wrote this test thinking Categories woud be an enum we make
	//but maybe it should just be res/values/string objects, referenced in java
	//like R.String.currency_CAD
	public void testGetCategory(){
		ExpenseItem claimItem = constructWithParameters();
		assertTrue(claimItem.getCategory().equals(Category.VEHICLE_RENTAL));
	}
	
	public void testGetDescription(){
		ExpenseItem claimItem = constructWithParameters();
		assertTrue(claimItem.getDescription().equals("A description"));
	}
	
	public void testGetAmountSpent(){
		ExpenseItem claimItem = constructWithParameters();
		assertTrue(claimItem.getAmountSpent().equals(1000));
	}
	
	public void testGetCurrency(){
		ExpenseItem claimItem = constructWithParameters();
		assertTrue(claimItem.getCurrency().equals(Currency.CAD));
	}
	
	
	
	
	
	public void testSetDate(){
		ExpenseItem claim = new ExpenseItem();
		claim.setDate(new Date(11111));
		assertTrue(claimItem.getDate().equals(new Date(11111)));
	}
	
	public void testSetCategory(){
		ExpenseItem claim = new ExpenseItem();
		claim.setCategory(Category.VEHICLE_RENTAL);
		assertTrue(claimItem.getCategory().equals(Category.VEHICLE_RENTAL));
	}
	
	public void testSetDescription(){
		ExpenseItem claim = new ExpenseItem();
		claim.setDescription("A description");
		assertTrue(claimItem.getDescription().equals("A description"));
	}
	
	public void testSetAmountSpent(){
		ExpenseItem claim = new ExpenseItem();
		claim.setAmountSpend(1000);
		assertTrue(claimItem.getAmountSpent().equals(1000));
	}
	
	public void testSetCurrency(){
		ExpenseItem claim = new ExpenseItem();
		claim.setCurrency(Currency.CAD);
		assertTrue(claimItem.getCurrency().equals(Currency.CAD));
	}

	
	private ExpenseItem constructWithParameters(){
		return new Expense(new Date(11111), Category.VEHICLE_RENTAL, "A description", 
				1000, Currency.CAD);
	}
	
		
	
	
}
