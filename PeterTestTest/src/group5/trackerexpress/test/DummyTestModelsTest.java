package group5.trackerexpress.test;

import java.util.ArrayList;
import java.util.UUID;

import group5.trackerexpress.Claim;
import group5.trackerexpress.ClaimList;
import group5.trackerexpress.Date;
import group5.trackerexpress.DummyTestModels;
import group5.trackerexpress.Expense;
import group5.trackerexpress.ExpenseList;
import group5.trackerexpress.ExpenseNotFoundException;
import group5.trackerexpress.Receipt;
import group5.trackerexpress.User;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class DummyTestModelsTest extends
		ActivityInstrumentationTestCase2<DummyTestModels> {

	Context context;
	Claim claimModel;
	Expense expenseModel;
	ClaimList claimListModel;
	ExpenseList expenseListModel;
	Receipt receiptModel;
	User userModel;
	
	public DummyTestModelsTest() {
		super(DummyTestModels.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		context = getActivity();
	}

	
	/*
	 * Claim tests
	 */
	
	public void testDestinationReason(){
		Claim testClaim = new Claim( "Claim1" );
		ArrayList<String> shouldLookLike = new ArrayList<String>();
		for( int i = 0; i < 5; i++ ){
			testClaim.addDestination(context, Integer.toString(i), Integer.toString(i+1));
			shouldLookLike.add( Integer.toString(i) + " - " + Integer.toString(i+1) );
		}
		
		assertEquals( "Should be equal", shouldLookLike, testClaim.DestinationReason() );
	}
	
	public void testToStringDestinations(){
		Claim testClaim = new Claim( "Claim1" );
		for ( int i = 0;  i < 6; i++ ){
			testClaim.addDestination(context, Integer.toString(i), Integer.toString(i+1));
		}
		String shouldLookLike = "0, 1, 2, 3, 4, 5";
		assertEquals( "Should be equal", shouldLookLike, testClaim.toStringDestinations() );
		
		testClaim = new Claim( "Claim1" );
		testClaim.addDestination(context, "1", "Reason");
		shouldLookLike = "1";
		assertEquals( "Should be equal", shouldLookLike, testClaim.toStringDestinations() );
	}
	
	
	/* 
	 * ClaimList tests 
	 * */

	public void testEmptyClaimList() {
		ClaimList claimList = new ClaimList(context);
		assertTrue("Empty claim list", claimList.size()==0);
	}
	
	
	// this is test case for 01.01.01 and 01.02.01
	public void testAddClaimList() {
		ClaimList claimList = new ClaimList(context);
		String claimName = "A Claim";
		Claim testClaim = new Claim(claimName);
		claimList.addClaim(context, testClaim);
		assertTrue("Empty claim list", claimList.size()==1);
		assertTrue("Test claim not contained", 
					testClaim.equals(claimList.getClaim(testClaim.getUuid())));
	}
	
	
	// this is test case for 01.03.01
	public void testGetClaimList() {
		ClaimList claimList = new ClaimList(context);
		String claimName = "A Claim";
		Claim testClaim = new Claim(claimName);
		
		claimList.addClaim(context, testClaim);
		
		assertTrue("Empty claim list", claimList.size()==1);
		assertTrue("Test claim not contained",
					testClaim.equals( claimList.getClaim(testClaim.getUuid()) ));
	}
	
	
	// this is test case for 01.05.01
	public void testRemoveClaimList() {
		ClaimList claimList = new ClaimList(context);
		Claim testClaim = new Claim("Test Claim");
		UUID testUuid = testClaim.getUuid();
		claimList.addClaim(context, testClaim);
		assertTrue("List size isn't big enough", claimList.size()==1);
		assertTrue("Test claim not contained", 
					testClaim.equals(claimList.getClaim(testClaim.getUuid())));
		claimList.deleteClaim(context, testClaim.getUuid());
		assertTrue("List size isn't small enough", claimList.size()==0);
		Claim retrieved = claimList.getClaim(testUuid);
		assertTrue ( "nothing should be there", retrieved == null );
	}
	
	// this is test case for 02.02.01 and 02.01.01
	public void testClaimOrder() {
		ClaimList claimList = new ClaimList(context);
		
		// Named so they come out in this order
		Claim c1 = new Claim("3");
		Claim c2 = new Claim("1");
		Claim c3 = new Claim("2");
		
		
		Date d1 = new Date(1995, 12, 12);
		Date d2 = new Date(1995, 1, 12);
		Date d3 = new Date(1995, 2, 12);
		
		c1.setStartDate(context, d1);
		c2.setStartDate(context, d2);
		c3.setStartDate(context, d3);
    	
		claimList.addClaim(context, c1);
		claimList.addClaim(context, c2);
		claimList.addClaim(context, c3);
		
		assertTrue("List size isn't correct", claimList.size()==3);
		assertTrue("Claim '1' is not first", claimList.getAllClaims()[0].getClaimName().equals("1") );
		assertTrue("Claim '2' is not second", claimList.getAllClaims()[1].getClaimName().equals("2") );
		assertTrue("Claim '3' is not last", claimList.getAllClaims()[2].getClaimName().equals("3") );
		
	}
	
	
	/*
	 * ExpenseList tests
	 */

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
