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
import group5.trackerexpress.ElasticSearchEngine;
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

	
	private ElasticSearchEngine elasticSearchEngine = new ElasticSearchEngine();
	private Claim claim;
	private User user;
	private UUID id;
	
	/**
	 * @param activityClass
	 */
	public ElasticSearchEngineTest() {
		super(TestActivity.class);
	}

	public void setup(){
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

		ArrayList<Claim> claims = elasticSearchEngine.getClaims();
		assertTrue(claims.get(0).getClaimName().equals("Name"));
		
		elasticSearchEngine.deleteClaim(claim.getUuid());
	}
	
	public void testDelete(){

		ArrayList<Claim> claims = elasticSearchEngine.getClaims();
		int sizeBefore = claims.size();
		
		elasticSearchEngine.submitClaim(claim);

		try {
			Thread.sleep(2000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		elasticSearchEngine.deleteClaim(claim.getUuid());
		
		claims = elasticSearchEngine.getClaims();
		assertEquals(claims.size(), sizeBefore);
	}
	
	
	public void testApprove(){
		elasticSearchEngine.submitClaim(claim);

		try {
			Thread.sleep(2000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		elasticSearchEngine.approveClaim(claim.getUuid());

		ArrayList<Claim> claims = elasticSearchEngine.getClaims();

		for (Claim claimElement : claims){
			if (claimElement.getUuid() == claim.getUuid()){
				assertEquals("Claim status was not chagned to approved.", claimElement.getStatus(), Claim.APPROVED);
				elasticSearchEngine.deleteClaim(claim.getUuid());
				return;
			}
		}
		
		fail("Claim couldn't be added or retrived.");
	}
}
