import java.util.Date;

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
			fail("Claim Constructor threw exception.");
		}
	}
	
	public void testContructWithoutParameters(){
		try{
			Claim claim = new Claim();
		}
		catch (Exception e){
			fail("Claim Constructor threw exception.");
		}
	}
	
	
	public void testIsComplete(){
		Claim claim = new Claim();
		assertFalse(claim.isComplete());
		
		claim = constructWithParameters();
		assertTrue(claim.isComplete());
		
		ExpenseItem e = new ExpenseItem();
		e.markAsIncomplete();
		claim.getExpenses().add(e);
		assertFalse(claim.isComplete());
	}


	
	public void testGetClaimantName(){
		Claim claim = constructWithParameters();
		assertTrue(claim.getClaimantName().equals("PETER"));
	}
	
	public void testGetStartDate(){
		Claim claim = constructWithParameters();
		assertTrue(claim.getStartDate().equals(new Date(11111)));
	}
	
	public void testGetEndDate(){
		Claim claim = constructWithParameters();
		assertTrue(claim.getEndDate().equals(new Date(22222)));
	}

	public void testGetDestinations(){
		Claim claim = constructWithParameters();
		
		List<Destination> expected = new ArrayList<Destination>();
		expected.add(new Destination("etown", "born here"));
		expected.add(new Destination("cow town", "reason"));
		
		assertTrue(claim.getDestinations().equals(expected));
	}
	
	
	
	public void testSetClaimantName(){
		Claim claim = constructWithParameters();
		assertTrue(claim.getClaimantName().equals("PETER"));
	}
	
	public void testSetStartDate(){
		Claim claim = constructWithParameters();
		claim.setStartDate(new Date(33333));
		assertTrue(claim.getStartDate().equals(new Date(33333)));
	}
	
	public void testSetEndDate(){
		Claim claim = constructWithParameters();
		claim.setEndDate(new Date(33333));
		assertTrue(claim.getEndDate().equals(new Date(33333)));
	}

	public void testSetDestinations(){
		Claim claim = constructWithParameters();
		claim.getDestinations().add(new Destination("third place", "third reason"));
		
		List<Destination> expected = new ArrayList<Destination>();
		expected.add(new Destination("etown", "born here"));
		expected.add(new Destination("cow town", "reason"));
		expected.add(new Destination("third place", "third reason"));
		
		assertTrue(claim.getDestinations().equals(expected));
	}
	
	
	
	private Claim constructWithParameters(){
		return new Claim("PETER", new Date(11111), new Date(22222), new Destination("etown", "born here"), new Destination("cow town", "reason"));
	}
	
		
	
	
}
