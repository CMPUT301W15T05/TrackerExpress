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


	private ElasticSearchEngine elasticSearchEngine;
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
		elasticSearchEngine = new ElasticSearchEngine();
		claim = new Claim("Name");
		user = new User(getActivity());
		user.setEmail(getActivity(), "foo@example.com");
		user.setName(getActivity(), "Foo Bar");
	}


	public void testSubmitAndGet(){

		try {
			elasticSearchEngine.submitClaim(getActivity(), claim);

			assertTrue(elasticSearchEngine.getClaim(getActivity(), claim.getUuid()).getClaimName().equals("Name"));

			elasticSearchEngine.deleteClaim(getActivity(), claim.getUuid());

		} catch (IOException e) {
			fail("IO Exception.");
		}
	}

	public void testDelete(){

		try {
			Claim[] claims = elasticSearchEngine.getClaims(getActivity());
			int sizeBefore = claims.length;

			elasticSearchEngine.submitClaim(getActivity(), claim);

			Thread.sleep(2000);


			elasticSearchEngine.deleteClaim(getActivity(), claim.getUuid());

			Thread.sleep(1000);

			claims = elasticSearchEngine.getClaims(getActivity());
			assertEquals(sizeBefore, claims.length);
		} catch (IOException e) {
			fail("IO Exception.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void testApprove(){

		try {
			elasticSearchEngine.submitClaim(getActivity(), claim);

			Thread.sleep(2000);


			elasticSearchEngine.reviewClaim(getActivity(), claim.getUuid(), "Comment", Claim.APPROVED);

			Thread.sleep(1000);


			Claim[]claims = elasticSearchEngine.getClaims(getActivity());

			for (Claim claimElement : claims){
				if (claimElement.getUuid().equals(claim.getUuid())){
					assertEquals("Claim status was not chagned to approved.", Claim.APPROVED, claimElement.getStatus());
					elasticSearchEngine.deleteClaim(getActivity(), claim.getUuid());
					return;
				}
			}

			fail("Claim couldn't be added or retrived.");
		} catch (IOException e) {
			fail("IO Exception.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void testReturn(){


		try {

			elasticSearchEngine.submitClaim(getActivity(), claim);

			Thread.sleep(1000);


			elasticSearchEngine.reviewClaim(getActivity(), claim.getUuid(), "test comment", Claim.RETURNED);

			Thread.sleep(1000);


			Claim[] claims = elasticSearchEngine.getClaims(getActivity());

			for (Claim claimElement : claims){
				if (claimElement.getUuid().equals(claim.getUuid())){
					assertEquals("Claim status was not chagned to returned.", Claim.RETURNED, claimElement.getStatus());
					assertTrue("Claim comment wasn't added correctly.", "test comment".equals(claimElement.getComments()));
					elasticSearchEngine.deleteClaim(getActivity(), claim.getUuid());
					return;
				}
			}

			fail("Claim couldn't be added or retrived.");
		} catch (IOException e) {
			fail("IO Exception.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
