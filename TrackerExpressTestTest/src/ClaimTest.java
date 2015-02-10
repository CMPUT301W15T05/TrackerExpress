import junit.framework.TestCase;


public class ClaimTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testContructWithParameters(){
		try{
			Claim claim = constructWithParameters();
		}
		catch (Exception e){
			fail();
			System.out.println("Expense Item Constructor threw exception.");
		}
	}
	
	public void testContructWithoutParameters(){
		try{
			Claim claim = new Claim();
		}
		catch (Exception e){
			fail();
			System.out.println("Expense Item Constructor threw exception.");
		}
	}
	
	public void testIsComplete(){
		Claim claim = new Expense();
		assertFalse(claim.isComplete());
		
		Claim claim = constructWithParameters();
		assertTrue(claim.isComplete());
	}
	
	public void testMarkAsIncomplete(){
		Claim claim = constructWithParameters();
		claim.markAsIncomplete();
		assertFalse(claim.isComplete());
	}
	
	
	public void testUnmarkAsIncomplete(){
		Claim claim = constructWithParameters();
		claim.markAsIncomplete();
		claim.unmarkAsIncomplete();		
		assertTrue(claim.isComplete());
	}
	
	//even if they uncheck the incomplete button, if parameters are missing,
	//the claim should still be incomplete
	public void testUnmarkAsIncompleteWithMissingParameters(){
		Claim claim = new Expense();
		claim.markAsIncomplete();
		claim.unmarkAsIncomplete();		
		assertFalse(claim.isComplete());
	}
	
	
	public void testGetDate(){
		Claim claimItem = constructWithParameters();
		assertTrue(claimItem.getDate().equals(new Date(11111)));
	}
	
	//i wrote this test thinking Categories woud be an enum we make
	//but maybe it should just be res/values/string objects, referenced in java
	//like R.String.currency_CAD
	public void testGetCategory(){
		Claim claimItem = constructWithParameters();
		assertTrue(claimItem.getCategory().equals(Category.VEHICLE_RENTAL));
	}
	
	public void testGetDescription(){
		Claim claimItem = constructWithParameters();
		assertTrue(claimItem.getDescription().equals("A description"));
	}
	
	public void testGetAmountSpent(){
		Claim claimItem = constructWithParameters();
		assertTrue(claimItem.getAmountSpent().equals(1000));
	}
	
	public void testGetCurrency(){
		Claim claimItem = constructWithParameters();
		assertTrue(claimItem.getCurrency().equals(Currency.CAD));
	}
	
	
	
	
	
	public void testSetDate(){
		Claim claim = new Claim();
		claim.setDate(new Date(11111));
		assertTrue(claimItem.getDate().equals(new Date(11111)));
	}
	
	public void testSetCategory(){
		Claim claim = new Claim();
		claim.setCategory(Category.VEHICLE_RENTAL);
		assertTrue(claimItem.getCategory().equals(Category.VEHICLE_RENTAL));
	}
	
	public void testSetDescription(){
		Claim claim = new Claim();
		claim.setDescription("A description");
		assertTrue(claimItem.getDescription().equals("A description"));
	}
	
	public void testSetAmountSpent(){
		Claim claim = new Claim();
		claim.setAmountSpend(1000);
		assertTrue(claimItem.getAmountSpent().equals(1000));
	}
	
	public void testSetCurrency(){
		Claim claim = new Claim();
		claim.setCurrency(Currency.CAD);
		assertTrue(claimItem.getCurrency().equals(Currency.CAD));
	}

	
	private Claim constructWithParameters(){
		return new Expense(new Date(11111), Category.VEHICLE_RENTAL, "A description", 
				1000, Currency.CAD);
	}
	
		
	
	
}
