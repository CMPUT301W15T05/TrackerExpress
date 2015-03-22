/**
 * 
 */
package group5.trackerexpress.test;

import java.util.ArrayList;
import java.util.UUID;

import android.test.ActivityInstrumentationTestCase2;
import group5.trackerexpress.Claim;
import group5.trackerexpress.ElasticSearchEngine;
import group5.trackerexpress.MainActivity;
import group5.trackerexpress.User;
import junit.framework.TestCase;

/**
 * 
 * @author crinklaw
 *
 */
public class ElasticSearchEngineTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	/**
	 * @param activityClass
	 */
	public ElasticSearchEngineTest(Class<MainActivity> activityClass) {
		super(MainActivity.class);
	}


	public void setup(){
		
	}
	

	public void testSubmitAndGet(){
		Claim claim = new Claim("Name");
		User user = new User(getActivity());
		user.setEmail(getActivity(), "foo@example.com");
		user.setName(getActivity(), "Foo Bar");
		
		UUID id = claim.getUuid();
		ElasticSearchEngine.submitClaim(claim, user);
		
		try {
		    Thread.sleep(2000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		
		ArrayList<Claim> claims = ElasticSearchEngine.getClaims();
	}
}
