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
	
	/**
	 * @param activityClass
	 */
	public ElasticSearchEngineTest() {
		super(TestActivity.class);
	}

	public void setup(){
		
	}


	public void testSubmitAndGet(){
		Claim claim = new Claim("Name");
		User user = new User(getActivity());
		user.setEmail(getActivity(), "foo@example.com");
		user.setName(getActivity(), "Foo Bar");

		UUID id = claim.getUuid();
		elasticSearchEngine.submitClaim(claim);

		try {
			Thread.sleep(2000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		ArrayList<Claim> claims = elasticSearchEngine.getClaims();
		assertTrue(claims.get(0).getClaimName().equals("Name"));
	}
}
