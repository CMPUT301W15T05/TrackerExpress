package group5.trackerexpress.test;

import java.util.Date;

import group5.trackerexpress.Claim;
import group5.trackerexpress.ClaimList;
import group5.trackerexpress.Controller;
import group5.trackerexpress.Expense;
import group5.trackerexpress.ExpenseNotFoundException;
import junit.framework.TestCase;

public class ExpenseTest extends TestCase {

	
	private ClaimList claims;


	public void setup() {
		claims = new ClaimList();
		claims.add(new Claim("Test Claim"));
	}

	
	public void testAddExpense(){
	
		//Claimant selects claim to edit and adds the expense:
		claims.get("Test Claim").addExpense(new Expense("Test Expense"));
		
		try {
			claims.get("Test Claim").getExpense("TestExpense");
		} catch (ExpenseNotFoundException e) {
			fail("Expesne not found.");
		}
	}
	
	public void testEditExpense(){
		try {
			claims.get("Test Claim").addExpense(new Expense("Test Expense"));
			claims.get("Test Claim").getExpense("Test Expense").setDate(new Date(11111));

		
			assertTrue(claims.get("Test Claim").getExpense("Test Expense").getDate().equals(new Date(11111)));
			
			
		} catch (ExpenseNotFoundException e) {
			fail("Expesne not found.");
		}
	}
	
	public void testDeleteExpense(){
		claims.get("Test Claim").addExpense(new Expense("Test Expense"));
		
		claims.get("Test Claim").removeExpense("Test Expense");
		
		try {
			claims.get("Test Claim").getExpense("TestExpense");
			fail("Expense not deleted.");
		}
		catch(ExpenseNotFoundException e){
			
		}
	}
	

}
