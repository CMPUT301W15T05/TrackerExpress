/**
 * 
 */
package group5.trackerexpress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Performs elastic search operations. 
 * NOTE: DOES NOT USE THREADS, SO WILL NOT WORK IF CALLED DIRECTLY. Instead,
 * use ElasticSearchEngine, which will use this class, but on a seperate thread.
 * 
 * Based on original by Chenlei Zhang:
 * https://github.com/rayzhangcl/ESDemo
 * March 31 2015
 * 
 *  @See ElasticSearchEngine
 */
public class ElasticSearchEngineUnthreadedUser {

	// Http Connector
	private HttpClient httpclient = new DefaultHttpClient();

	// JSON Utilities
	private Gson gson = new Gson();
	/**
	 * 
	 */
	private static final String HTTP_PATH = "http://cmput301.softwareprocess.es:8080/testing/group5users/";

	/**
	 * Gets all registerd users from server
	 * 
	 * @return
	 */
	public User[] getUsers() {

		try {
			HttpPost searchRequest = new HttpPost(HTTP_PATH + "_search?pretty=1");
			//String query = 	"{\"query\" : {\"query_string\" : {\"default_field\" : \"ingredients\",\"query\" : \"" + str + "\"}}}";
			//StringEntity stringentity = new StringEntity(query);

			searchRequest.setHeader("Accept","application/json");
			//searchRequest.setEntity(stringentity);

			HttpResponse response = httpclient.execute(searchRequest);

			String status = response.getStatusLine().toString();
			System.out.println(status);

			String json = getEntityContent(response);

			Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse>(){}.getType();
			ElasticSearchSearchResponse esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
			//System.err.println(esResponse);

			ArrayList<User> users = new ArrayList<User>();
			for (ElasticSearchResponse userResponse : esResponse.getHits()) {
				User user = userResponse.getSource();
				users.add(user);
			}
			//searchRequest.getEntity().consumeContent();
			
			return users.toArray(new User[users.size()]);
			
		} catch(IOException e){
			throw new RuntimeException();
		}
	}


	/**
	 * 
	 * 
	 * @param user
	 */
	public void submitUser(User user) {
		try {
			HttpPost httpPost = new HttpPost(HTTP_PATH + user.getUuid());
			StringEntity stringentity = null;

			stringentity = new StringEntity(gson.toJson(user));

			httpPost.setHeader("Accept","application/json");
			Log.e("SetingEnttity", "SettingEntity");
			httpPost.setEntity(stringentity);
			HttpResponse response = null;
			Log.e("ABOUT", "About to execute");
			response = httpclient.execute(httpPost);
			Log.e("AFTER", "Executed");


			//Response code:
			String status = response.getStatusLine().toString();
			System.out.println(status);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
			String output;
			System.err.println("Output from Server -> ");
			while ((output = br.readLine()) != null) {
				System.err.println(output);
			}

			//EntityUtils.consume(entity);

			//httpPost.releaseConnection();
			
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			throw new RuntimeException();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
	

	
	/**
	 * get the http response and return json string
	 */
	private String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));
		String output;
		System.err.println("Output from Server -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:"+json);
		return json;
	}
	
	
	private class ElasticSearchSearchResponse {
	    int took;
	    boolean timed_out;
	    transient Object _shards;
	    Hits hits;
	    boolean exists;    
	    public Collection<ElasticSearchResponse> getHits() {
	        return hits.getHits();        
	    }
	    public Collection<User> getSources() {
	        Collection<User> out = new ArrayList<User>();
	        for (ElasticSearchResponse essrt : getHits()) {
	            out.add( essrt.getSource() );
	        }
	        return out;
	    }
	    public String toString() {
	        return (super.toString() + ":" + took + "," + _shards + "," + exists + ","  + hits);     
	    }
	}
	
	
	private class ElasticSearchResponse {
	    String _index;
	    String _type;
	    String _id;
	    int _version;
	    boolean exists;
	    User _source;
	    double max_score;
	    public User getSource() {
	        return _source;
	    }
	}
	
	public class Hits {
	    int total;
	    double max_score;
	    Collection<ElasticSearchResponse> hits;
	    public Collection<ElasticSearchResponse> getHits() {
	        return hits;
	    }
	    public String toString() {
	        return (super.toString()+","+total+","+max_score+","+hits);
	    }
	}


}