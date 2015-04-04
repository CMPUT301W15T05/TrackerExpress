/**
 * 
 */
package group5.trackerexpress.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import android.test.ActivityInstrumentationTestCase2;
import group5.trackerexpress.Claim;
import group5.trackerexpress.ElasticSearchEngineUnthreaded;
import group5.trackerexpress.MainActivity;
import group5.trackerexpress.TestActivity;
import group5.trackerexpress.User;
import junit.framework.TestCase;

/**
 * 
 * @author crinklaw
 *
 */
public class ElasticSearchEngineTest extends ActivityInstrumentationTestCase2<TestActivity> {

	
	private ElasticSearchEngineUnthreaded elasticSearchEngine;
	private Claim claim;
	private User user;
	private UUID id;
	
	/**
	 * @param activityClass
	 */
	public ElasticSearchEngineTest() {
		super(TestActivity.class);
	}

	public void setUp(){
		elasticSearchEngine = new ElasticSearchEngineUnthreaded();
		claim = new Claim("Name");
		user = new User(getActivity());
		user.setEmail(getActivity(), "foo@example.com");
		user.setName(getActivity(), "Foo Bar");
	}


	public void testSubmitAndGet(){

		elasticSearchEngine.submitClaim(claim);

		try {
			Thread.sleep(2000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		Claim[] claims = elasticSearchEngine.getClaims();
		assertTrue(claims[0].getClaimName().equals("Name"));
		
		elasticSearchEngine.deleteClaim(claim.getUuid());
	}
	
	public void testDelete(){

		Claim[] claims = elasticSearchEngine.getClaims();
		int sizeBefore = claims.length;
		
		elasticSearchEngine.submitClaim(claim);

		try {
			Thread.sleep(2000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		elasticSearchEngine.deleteClaim(claim.getUuid());
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		
		
		claims = elasticSearchEngine.getClaims();
		assertEquals(sizeBefore, claims.length);
	}
	
	
	public void testApprove(){
		elasticSearchEngine.submitClaim(claim);

		try {
			Thread.sleep(2000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		elasticSearchEngine.approveClaim(claim.getUuid());
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}


		Claim[]claims = elasticSearchEngine.getClaims();

		for (Claim claimElement : claims){
			if (claimElement.getUuid().equals(claim.getUuid())){
				assertEquals("Claim status was not chagned to approved.", Claim.APPROVED, claimElement.getStatus());
				elasticSearchEngine.deleteClaim(claim.getUuid());
				return;
			}
		}
		
		fail("Claim couldn't be added or retrived.");
	}
	
	
	public void testReturn(){
		elasticSearchEngine.submitClaim(claim);

		try {
			Thread.sleep(1000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		elasticSearchEngine.returnClaim(claim.getUuid(), "test comment");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}


		Claim[] claims = elasticSearchEngine.getClaims();

		for (Claim claimElement : claims){
			if (claimElement.getUuid().equals(claim.getUuid())){
				assertEquals("Claim status was not chagned to returned.", Claim.RETURNED, claimElement.getStatus());
				assertTrue("Claim comment wasn't added correctly.", "test comment".equals(claimElement.getComments()));
				elasticSearchEngine.deleteClaim(claim.getUuid());
				return;
			}
		}
		
		fail("Claim couldn't be added or retrived.");
	}
	
	
}
